package com.nyit.datausagemonitor;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SetMobilePlanActivity extends AppCompatActivity {
    int mStartDay,mDataLimit, savedStartDay, savedDatelimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mobile_plan);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        savedDatelimit = preferences.getInt("DATALIMITS", 1);
        savedStartDay = preferences.getInt("STARTINGDAY", 2 );

        mStartDay = savedStartDay;

        final EditText selectDateView = findViewById(R.id.select_start_date_calendar_view);
        selectDateView.setText(savedStartDay +"th of the month", TextView.BufferType.NORMAL);
        EditText dataLimitView = findViewById(R.id.data_limit_input_view);
        dataLimitView.setText(savedDatelimit +"", TextView.BufferType.NORMAL );

        Button saveBt = findViewById(R.id.save_button_set_plan);



        selectDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SetMobilePlanActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                mStartDay = day;
                                selectDateView.setText(mStartDay +"th of the month", TextView.BufferType.NORMAL);
                                Toast.makeText(SetMobilePlanActivity.this, "day is :" + mStartDay, Toast.LENGTH_SHORT).show();

                            }
                        }, year ,month , dayOfMonth);

                datePickerDialog.show();


            }

        });


        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText dataLimitView = findViewById(R.id.data_limit_input_view);
                mDataLimit = Integer.parseInt(dataLimitView.getText().toString());

                SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putInt("STARTINGDAY", mStartDay);
                editor1.putInt("DATALIMITS", mDataLimit);
                editor1.commit();






                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }
}

