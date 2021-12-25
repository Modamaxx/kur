package zagurskiy.fit.bstu.todolist.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import zagurskiy.fit.bstu.todolist.BaseActivity;
import zagurskiy.fit.bstu.todolist.R;
import zagurskiy.fit.bstu.todolist.Task;
import zagurskiy.fit.bstu.todolist.XPathActivity;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

public class MainActivity extends AppCompatActivity {

    //View
    Button selectedDateButton;
    TextView selectedDate;
    LocalDate localDate;
    Calendar calendar;

    Button studyButton;
    Button workButton;
    Button sleepButton;
    Button meetingsButton;
    Button sportsButton;
    Button leisureButton;


    public static List<Task> tasks;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding();
        setListeners();
        setData();
    }

    private void binding() {
        selectedDateButton = findViewById(R.id.btnSelectDate);
        selectedDate = findViewById(R.id.txtSelectDate);
        calendar = Calendar.getInstance();


        studyButton = findViewById(R.id.studyButton);
        workButton = findViewById(R.id.workButton);
        sleepButton = findViewById(R.id.sleepButton);
        meetingsButton = findViewById(R.id.meetingsButton);
        sportsButton = findViewById(R.id.sportsButton);
        leisureButton = findViewById(R.id.leisureButton);
    }

    private void setData() {
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            String selectedDate = arguments.get("selectedDate").toString();
            this.selectedDate.setText(selectedDate);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(selectedDate.substring(0, 2))); //substring получает строку по индуксу от кого и до кокого
            calendar.set(Calendar.MONTH, Integer.parseInt(selectedDate.substring(3, 5)) - 1);
            calendar.set(Calendar.YEAR, Integer.parseInt(selectedDate.substring(6, 10)));
            localDate=calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
              localDate= Calendar.getInstance().getTime()
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            selectedDate.setText(localDate.toString());
        }
    }


    private void setListeners() {

        DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            localDate = calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            selectedDate.setText(localDate.toString());
        };

        selectedDateButton.setOnClickListener(view ->
                new DatePickerDialog(MainActivity.this, d,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show());

        studyButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.STUDY);
            startActivity(intent);
        });
        workButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.WORK);
            startActivity(intent);
        });
        sleepButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.SLEEP);
            startActivity(intent);
        });
        meetingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.MEETING);
            startActivity(intent);
        });
        sportsButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.SPORT);
            startActivity(intent);
        });
        leisureButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, BaseActivity.class);
            intent.putExtra("selectedDate", localDate);
            intent.putExtra("activityType", ActivityType.LEISURE);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_xpath, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.xpath:
                Intent intent = new Intent(this, XPathActivity.class);
                intent.putExtra("selectedDate", selectedDate.getText());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}