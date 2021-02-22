package com.p.homeapp.views.addingTasksViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText eTxtTaskName;
    private TextView txtTaskDeadline;
    private ImageView ivDeleteDate;
    private Spinner spinTaskType;
    private CheckBox chBoxTaskNotification;
    private Button saveTaskBtn;

    private LinearLayout llAddNotification;

    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Spinner spinner =  findViewById(R.id.spin_task_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tasks_types, R.layout.activity_add_task_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        eTxtTaskName = findViewById(R.id.etxt_task_name);
        txtTaskDeadline = findViewById(R.id.txt_task_deadline);
        txtTaskDeadline.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        llAddNotification = findViewById(R.id.ll_add_notification);
        txtTaskDeadline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                llAddNotification.setVisibility(View.VISIBLE);
                ivDeleteDate.setVisibility(View.VISIBLE);
            }
        });

        ivDeleteDate = findViewById(R.id.iv_delete_date);
        ivDeleteDate.setOnClickListener(v -> {
            txtTaskDeadline.setText("");
            llAddNotification.setVisibility(View.GONE);
            ivDeleteDate.setVisibility(View.GONE);
        });

        spinTaskType = findViewById(R.id.spin_task_type);
        chBoxTaskNotification = findViewById(R.id.chbox_task_notification);
        saveTaskBtn = findViewById(R.id.btn_save_task);

        mRootRef = FirebaseDatabase.getInstance().getReference("tasks");

        getSupportFragmentManager().beginTransaction().replace(R.id.container_add_task_owners, new FragmentAddTask()).commit();

        saveTaskBtn.setOnClickListener(v -> {
            String taskName = eTxtTaskName.getText().toString();
            String taskType = spinTaskType.getSelectedItem().toString();
            String taskDeadline = txtTaskDeadline.getText().toString();
            boolean taskNotification = chBoxTaskNotification.isChecked();

            String taskId = mRootRef.push().getKey();

            Task task = new Task();
            task.setTaskName(taskName);
            task.setTaskType(taskType);
            task.setDeadline(DateParser.stringToDateParser(taskDeadline));
            task.setTaskNotification(taskNotification);
            task.setId(taskId);

            saveTask(task);
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        TextView textView = findViewById(R.id.txt_task_deadline);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(calendar.getTime());

        textView.setText(dateString);
    }

    public void saveTask(Task task) {

        FirebaseDatabase.getInstance().getReference("tasks").child(task.getId()).setValue(task);
        Toast.makeText(AddTaskActivity.this, "Added task", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddTaskActivity.this, FragmentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}