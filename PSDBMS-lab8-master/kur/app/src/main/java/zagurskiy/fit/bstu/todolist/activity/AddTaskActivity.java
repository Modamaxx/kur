package zagurskiy.fit.bstu.todolist.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import zagurskiy.fit.bstu.todolist.BaseActivity;
import zagurskiy.fit.bstu.todolist.R;
import zagurskiy.fit.bstu.todolist.Task;
import zagurskiy.fit.bstu.todolist.XMLHelper;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

public class AddTaskActivity extends AppCompatActivity {

    //View
    ActionBar actionBar;
    EditText description;
    TextView date;
    Spinner category;
    Button save;


    String displayCategory;
    //Data
    LocalDate taskDate;
    ActivityType activityType;
    Task editableTask;
    String[] categories = {"Категория...", "Учеба", "Работа", "Сон", "Встречи", "Спорт", "Досуг"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        binding();
        setListeners();
        setData();
    }

    private void binding() {
        actionBar = getSupportActionBar();
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
//        category = findViewById(R.id.category);
        save = findViewById(R.id.btnSave);

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.category_item, categories);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        category.setAdapter(adapter);
    }

    private void setData() {
        actionBar.setDisplayHomeAsUpEnabled(true);
        editableTask = null;

        Intent intent = getIntent();
        taskDate = LocalDate.parse(intent.getExtras().get("selectedDate").toString());
        this.date.setText("Дата: " + taskDate);

        editableTask = (Task) intent.getSerializableExtra("taskToEdit");
        if (editableTask == null) return;
        description.setText(editableTask.getDescription());
        category.setSelection(Arrays.asList(categories).indexOf(editableTask.getCategory()));
        date.setText("Дата: " + editableTask.getDate());

    }

    private void setListeners() {
        save.setOnClickListener(view -> {
            if (editableTask != null)
                XMLHelper.deleteTask(this, editableTask);

            String descrip = description.getText().toString();
            if (descrip.equals(""))
                descrip = " ";

            Intent f = getIntent();
            activityType = (ActivityType) f.getExtras().get("activityType");
            displayCategory= activityType.getDisplayName();
            Task task = new Task(descrip, displayCategory, taskDate, false);
            List<Task> tasks = XMLHelper.readXML(this);

            tasks.add(task);
            XMLHelper.writeXML(this, tasks);
            Intent intentStudyActivity = new Intent(this, BaseActivity.class);
            intentStudyActivity.putExtra("selectedDate", taskDate);
            intentStudyActivity.putExtra("activityType",activityType);
            startActivity(intentStudyActivity);
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("selectedDate", taskDate);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}