package com.p.homeapp.views.addingTasksViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p.homeapp.R;
import com.p.homeapp.entities.Task;
import com.p.homeapp.helpers.DateParser;
import com.p.homeapp.views.mainView.FragmentActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private EditText eTxtTaskName;
    private TextView eTxtTaskDeadline;
    private Spinner spinnerTaskType;
    private CheckBox chBoxTaskNotification;
    private Button saveTaskButton;

    private DatabaseReference mRootRef;

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
        spinnerTaskType = (Spinner) findViewById(R.id.id_task_type_spinner);
        chBoxTaskNotification = findViewById(R.id.id_task_notification_checkbox);
        saveTaskButton = findViewById(R.id.id_save_task_btn);

        mRootRef = FirebaseDatabase.getInstance().getReference();

        eTxtTaskDeadline.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.id_task_owners_container, new FragmentAddTask()).commit();

        saveTaskButton.setOnClickListener(v -> {
            String taskName = eTxtTaskName.getText().toString();
            String taskType = spinnerTaskType.getSelectedItem().toString();
            String taskDeadline = eTxtTaskDeadline.getText().toString();
            boolean taskNotification = chBoxTaskNotification.isChecked();
            Task task = new Task();
            task.setTaskName(taskName);
            task.setTaskType(taskType);
            task.setDeadline(DateParser.stringToDateParser(taskDeadline));
            task.setTaskNotification(taskNotification);

            saveTask(task);
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        TextView textView = (TextView) findViewById(R.id.id_task_deadline_txt);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(calendar.getTime());

        textView.setText(dateString);
    }

    public void saveTask(Task task) {

        FirebaseDatabase.getInstance().getReference().child("tasks").push().setValue(task);
        Toast.makeText(AddTaskActivity.this, "Added task", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddTaskActivity.this, FragmentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}