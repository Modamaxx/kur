package zagurskiy.fit.bstu.todolist.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

import zagurskiy.fit.bstu.todolist.BaseActivity;
import zagurskiy.fit.bstu.todolist.R;
import zagurskiy.fit.bstu.todolist.Task;
import zagurskiy.fit.bstu.todolist.repository.DBHelper;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

public class AddTaskActivity extends AppCompatActivity {

    ActionBar actionBar;
    EditText description;
    TextView date;
    Button save;

    DBHelper dbHelper;
    SQLiteDatabase db;
    String displayCategory;
    LocalDate taskDate;
    ActivityType activityType;
    Task editedTask;

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
        save = findViewById(R.id.btnSave);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    private void setData() {
        actionBar.setDisplayHomeAsUpEnabled(true);
        editedTask = null;

        Intent intent = getIntent();
        taskDate = LocalDate.parse(intent.getExtras().get("selectedDate").toString());
        this.date.setText("Дата: " + taskDate);

        editedTask = (Task) intent.getSerializableExtra("taskToEdit");
        if (editedTask == null) return;

        description.setText(editedTask.getDescription());
    }

    private void setListeners() {
        save.setOnClickListener(view -> {
            String descriptionTask = description.getText().toString();
            if(descriptionTask.length()==0){
                Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent backActivityIntent = getIntent();
            activityType = (ActivityType) backActivityIntent.getExtras().get("activityType");
            displayCategory = activityType.getDisplayName();

            if (editedTask != null) {
                editedTask.setDescription(description.getText().toString());
                dbHelper.update(db, editedTask);
            } else {
                Task task = Task.builder()
                        .description(descriptionTask)
                        .category(displayCategory)
                        .date(taskDate)
                        .done(false)
                        .build();
                dbHelper.insert(db, task);
            }

            Intent intentActivity = new Intent(this, BaseActivity.class);
            intentActivity.putExtra("selectedDate", taskDate);
            intentActivity.putExtra("activityType", activityType);
            startActivity(intentActivity);
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
}