package com.p.homeapp.views.updateTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
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

import com.p.homeapp.R;
import com.p.homeapp.views.addingTasksViews.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TaskUpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText eTxtTaskNameUpdate;
    private TextView txtTaskDeadlineUpdate;
    private ImageView ivDeleteDateUpdate;
    private LinearLayout llNotificationUpdate;
    private Spinner spinTaskTypeUpdate;
    private CheckBox chBoxTaskNotificationUpdate;
    private Button btnTaskUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_update);

        Spinner spinner = findViewById(R.id.spin_update_task_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.tasks_types, R.layout.activity_add_task_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        eTxtTaskNameUpdate = findViewById(R.id.etxt_update_task_name);
        txtTaskDeadlineUpdate = findViewById(R.id.txt_update_task_deadline);
        txtTaskDeadlineUpdate.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        llNotificationUpdate = findViewById(R.id.ll_notification_update);
        txtTaskDeadlineUpdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                llNotificationUpdate.setVisibility(View.VISIBLE);
                ivDeleteDateUpdate.setVisibility(View.VISIBLE);
            }
        });

        ivDeleteDateUpdate = findViewById(R.id.iv_delete_date_update);
        ivDeleteDateUpdate.setOnClickListener(v -> {
            txtTaskDeadlineUpdate.setText("");
            llNotificationUpdate.setVisibility(View.GONE);
            ivDeleteDateUpdate.setVisibility(View.GONE);
        });

        spinTaskTypeUpdate = findViewById(R.id.spin_update_task_type);
        chBoxTaskNotificationUpdate = findViewById(R.id.chbox_update_task_notification);
        btnTaskUpdate = findViewById(R.id.btn_task_update);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        TextView textView = findViewById(R.id.txt_update_task_deadline);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(calendar.getTime());

        textView.setText(dateString);
    }
}