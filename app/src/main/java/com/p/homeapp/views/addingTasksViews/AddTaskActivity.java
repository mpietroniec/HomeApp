package com.p.homeapp.views.addingTasksViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.p.homeapp.R;
import com.p.homeapp.entities.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private EditText eTxtTaskName;
    private TextView eTxtTaskDeadline;
    private Spinner spinnerTaskType;
    private CheckBox taskNotification;
    private Button saveTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Spinner spinner = (Spinner) findViewById(R.id.id_task_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tasks_types, R.layout.activity_add_task_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        eTxtTaskName = findViewById(R.id.id_task_name_etxt);

        eTxtTaskDeadline = findViewById(R.id.id_task_deadline_txt);
        eTxtTaskDeadline.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        spinnerTaskType = (Spinner) findViewById(R.id.id_task_type_spinner);
        taskNotification = findViewById(R.id.id_task_notification_checkbox);

        getSupportFragmentManager().beginTransaction().replace(R.id.id_task_owners_container, new FragmentAddTask()).commit();

        saveTaskButton = findViewById(R.id.id_save_task_btn);
        saveTaskButton.setOnClickListener(v -> {
            String taskName = eTxtTaskName.getText().toString();
            String taskType = spinnerTaskType.getSelectedItem().toString();
            String taskDeadline = eTxtTaskDeadline.getText().toString();

            Task task = new Task();
            task.setTaskName(taskName);
            task.setTaskType(taskType);
            task.setDeadline(taskDeadline);

            saveTask(task);
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //String currentDateString = DateFormat.getDateInstance(dateFormat).format(calendar.getTime());
        String currentDateString = dateFormat.format(calendar.getTime());

        TextView textView = (TextView) findViewById(R.id.id_task_deadline_txt);
        textView.setText(currentDateString);
    }

    public void saveTask(Task task) {
        FirebaseDatabase.getInstance().getReference().child("task").push().setValue(task);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}