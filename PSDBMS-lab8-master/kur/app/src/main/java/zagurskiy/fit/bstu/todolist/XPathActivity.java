package zagurskiy.fit.bstu.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.List;

import zagurskiy.fit.bstu.todolist.activity.MainActivity;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

public class XPathActivity extends AppCompatActivity {

    //View
    ActionBar actionBar;
    Spinner category;
    ListView tasksList;
    private ActivityType type;

    //Data
    private LocalDate taskDate;
    private String[] categories = {"Категория...", "Работа", "Учеба", "Дом", "Хобби", "Прочее"};
    private CustomListAdapter customListAdapter;
    private List<Task> tasks;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xpath);

        binding();
        setListeners();
    }

    private void binding() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        context = this;

        category = findViewById(R.id.selectedCategory);
        tasksList = findViewById(R.id.tasksListByCategory);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.category_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        Bundle arguments = getIntent().getExtras();
        taskDate = LocalDate.parse(arguments.get("selectedDate").toString());
        type = (ActivityType) arguments.get("activityType");
        tasks = XMLHelper.readXML(this);
        customListAdapter = CustomListAdapter.builder()
                .tasks(tasks)
                .context(this)
                .displayDate(true)
                .type(type)
                .build();
        ;
        tasksList.setAdapter(customListAdapter);
    }

    private void setListeners() {
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!category.getSelectedItem().toString().equals(categories[0]))
                    tasks = XMLHelper.getTaskByCategory(context, category.getSelectedItem().toString());
                else
                    tasks = XMLHelper.readXML(context);
                customListAdapter.updateTasksList(tasks);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
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