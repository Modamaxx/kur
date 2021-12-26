package zagurskiy.fit.bstu.todolist;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import zagurskiy.fit.bstu.todolist.activity.AddTaskActivity;
import zagurskiy.fit.bstu.todolist.activity.MainActivity;
import zagurskiy.fit.bstu.todolist.dataBase.DBHelper;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

public class BaseActivity extends AppCompatActivity {

    TextView displayName;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Button addTaskButton;
    LocalDate taskDate;
    private List<Task> filteredTasks;
    public static List<Task> tasks;
    private CustomListAdapter customListAdapter;
    ListView tasksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        addTaskButton = findViewById(R.id.btnAddTaskWork);
        tasksList = findViewById(R.id.tasksListWork);
        ActivityType type = (ActivityType) getIntent().getExtras().get("activityType");
        LocalDate selectedDate = (LocalDate) getIntent().getExtras().get("selectedDate");
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();


        if (!dbHelper.isExist(db)) {
            dbHelper.initialization(db);
        }

        setListener(type, selectedDate);
        setData(type, selectedDate);
    }

    private void setListener(ActivityType type, LocalDate selectedDate) {
        addTaskButton.setOnClickListener(view -> {
            Intent f = getIntent();
            taskDate = selectedDate;
            Intent intent = new Intent(this, AddTaskActivity.class);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("activityType", type);
            startActivity(intent);
        });
    }

    private void setData(ActivityType type, LocalDate selectedDate) {
        tasks = XMLHelper.getTaskByCategory(this, type.getDisplayName());
        taskDate = selectedDate;
        displayName = findViewById(R.id.textView2);
        String valueActivity = dbHelper.selectRaw(db, type, this);
        displayName.setText(valueActivity);
        setFilteredTasks(taskDate);

        customListAdapter = CustomListAdapter.builder()
                .tasks(filteredTasks)
                .context(this)
                .displayDate(false)
                .type(type)
                .build();
        tasksList.setAdapter(customListAdapter);
    }


    private void setFilteredTasks(LocalDate selectedDate) {
        filteredTasks = tasks.stream()
                .filter(t -> t.isDisplayed(selectedDate))
                .collect(Collectors.toList());
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
