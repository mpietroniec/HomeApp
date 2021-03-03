package com.p.homeapp.views.addingTasksViews;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p.homeapp.R;
import com.p.homeapp.entities.Group;
import com.p.homeapp.entities.Task;
import com.p.homeapp.helpers.DateParser;
import com.p.homeapp.views.mainView.FragmentActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText eTxtTaskName;
    private TextView txtTaskDeadline, txtGroupName, txtTargetUsers;
    private ImageView ivDeleteDate;
    private Spinner spinTaskType, spinTaskGroup;
    private CheckBox chBoxTaskNotification;
    private Button btnSaveTask, btnAddTargetUsers;

    private LinearLayout llAddNotification;

    private DatabaseReference mRootRef;

    FirebaseUser fUser;

    private String groupId;
    private List<String> userGroupNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        spinTaskGroup = findViewById(R.id.spin_task_group);
        txtGroupName = findViewById(R.id.txt_group_name);
        txtTargetUsers = findViewById(R.id.txt_target_users);
        txtTaskDeadline = findViewById(R.id.txt_task_deadline);
        spinTaskType = findViewById(R.id.spin_task_type);
        chBoxTaskNotification = findViewById(R.id.chbox_task_notification);
        btnSaveTask = findViewById(R.id.btn_save_task);
        btnAddTargetUsers = findViewById(R.id.btn_add_target_users);
        ivDeleteDate = findViewById(R.id.iv_delete_date);
        eTxtTaskName = findViewById(R.id.etxt_task_name);
        llAddNotification = findViewById(R.id.ll_add_notification);

        userGroupNameList = new ArrayList<>();

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        if (groupId.equals("none")) {
            spinTaskGroup.setVisibility(View.VISIBLE);
            addGroupToSpinner();
        } else {
            txtGroupName.setVisibility(View.VISIBLE);
            setGroupName(groupId);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tasks_types, R.layout.activity_add_task_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTaskType.setAdapter(adapter);


        txtTaskDeadline.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

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

        ivDeleteDate.setOnClickListener(v -> {
            txtTaskDeadline.setText("");
            llAddNotification.setVisibility(View.GONE);
            ivDeleteDate.setVisibility(View.GONE);
        });


        mRootRef = FirebaseDatabase.getInstance().getReference("tasks");

        btnSaveTask.setOnClickListener(v -> {
            getTaskData();
        });
    }

    private void getTaskData() {
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
    }

    private void addGroupToSpinner() {
        FirebaseDatabase.getInstance().getReference().child("groups").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            userGroupNameList.clear();
                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                Group group = snapshot.getValue(Group.class);
                                if (group.getMembersId().contains(fUser.getUid())) {
                                    userGroupNameList.add(group.getName());
                                }
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                                    R.layout.activity_add_task_spinner_item, userGroupNameList);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinTaskGroup.setAdapter(arrayAdapter);
                        }
                    }
                });
    }

    private void setGroupName(String groupId) {
        FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            txtGroupName.setText(task.getResult().getValue(Group.class).getName());
                        }
                    }
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

        mRootRef.child(task.getId()).setValue(task);
        Toast.makeText(AddTaskActivity.this, "Added task", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddTaskActivity.this, FragmentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}