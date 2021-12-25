package zagurskiy.fit.bstu.todolist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zagurskiy.fit.bstu.todolist.activity.AddTaskActivity;
import zagurskiy.fit.bstu.todolist.activity.MainActivity;
import zagurskiy.fit.bstu.todolist.utils.ActivityType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomListAdapter extends BaseAdapter {

    private List<Task> tasks;
    private Context context;

    private boolean displayDate;
    private ActivityType type;


    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void updateTasksList(List<Task> filteredTasks) {
        tasks.clear();
        tasks.addAll(filteredTasks);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.task_item, null);

        TextView itemDescription = (TextView) view.findViewById(R.id.itemDescription);
        TextView itemCategory = (TextView) view.findViewById(R.id.itemCategory);
        ImageButton editItem = (ImageButton) view.findViewById(R.id.btnEditItem);
        ImageButton deleteItem = (ImageButton) view.findViewById(R.id.btnDeleteItem);
        TextView itemDate = (TextView) view.findViewById(R.id.itemDate);

        CheckBox checkItem = (CheckBox) view.findViewById(R.id.check);

        editItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddTaskActivity.class);
            intent.putExtra("selectedDate", tasks.get(position).getDate());
            intent.putExtra("activityType", type);
            intent.putExtra("taskToEdit", tasks.get(position));
            context.startActivity(intent);
        });

        deleteItem.setOnClickListener(v -> {
            tasks.remove(tasks.get(position));
            XMLHelper.writeXML(context, tasks);
            MainActivity.tasks = XMLHelper.readXML(context);
            this.notifyDataSetChanged();

        });
        checkItem.setOnClickListener(v -> {
            tasks.get(position).setDone(true);
            XMLHelper.writeXML(context, tasks);
            MainActivity.tasks = XMLHelper.readXML(context);
            this.notifyDataSetChanged();
        });

        checkItem.setChecked(tasks.get(position).isDone());
        itemDescription.setText(tasks.get(position).getDescription());
        itemCategory.setText(tasks.get(position).getCategory());
        itemDate.setText("Дата: " + tasks.get(position).getDate());

        return view;
    }
}
