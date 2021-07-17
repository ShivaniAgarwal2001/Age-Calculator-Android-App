package com.example.agecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText date_ip, time_ip, current_date_ip;
    TextView age_day, age_month, age_year,
            next_b_day, next_b_month, next_b_year;
    private int year, month, day, hour, minute;
    private int b_year, b_month, b_day,
            c_year, c_month, c_day;
    Date dob, current;

//    Summary
    TextView result_months, result_weeks, result_days,
        result_hours, result_min, result_sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current_date_ip = findViewById(R.id.current_date_ip);
        date_ip = findViewById(R.id.date_ip);
//        time_ip = findViewById(R.id.time_ip);

        current_date_ip.setOnClickListener(this::onClick);
        date_ip.setOnClickListener(this);
//        time_ip.setOnClickListener(this);

        setTodaysDate();

        date_ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //.....................
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            //......................
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(date_ip.getText())) {
                    calculateAge();
                    nextBirthday();
                } else {
                    Toasty.warning(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

    }

    private void setTodaysDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        String current_date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
//        Date date = new Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH) - 1);
        current_date_ip.setText(current_date);
    }

    @Override
    public void onClick(View v) {
//        if (v == current_date_ip) {}
        if (v == date_ip) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            c_year = year;
            c_month = month;
            c_day = day;
            Toast.makeText(this, "Year:" + year + " Month:" + (month + 1) + "Day:" + day, Toast.LENGTH_SHORT).show();

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

//                            String b_year = DateFormat.getDateInstance().format(year);
//                            String b_month = DateFormat.getDateInstance().format(monthOfYear);
//                            String b_day = DateFormat.getDateInstance().format(dayOfMonth);
//                    SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyyy");
//                            String strdate = formatter.format(c);


//                    date_ip.setText(dayOfMonth + "/" + String.valueOf((monthOfYear + 1)) + "/" + year);
                    Toast.makeText(MainActivity.this, "day:" + dayOfMonth + " Month:" + (monthOfYear + 1) + " Year:" + year, Toast.LENGTH_SHORT).show();
                    b_year = year;
                    b_month = monthOfYear;
                    b_day = dayOfMonth;

                    monthOfYear=monthOfYear+1;

                    dob = new Date(year, monthOfYear-1, dayOfMonth);
                    date_ip.setText(dayOfMonth + "/" + String.valueOf((monthOfYear)) + "/" + year);

                    Log.v("check date_ip","............."+date_ip +"..............."+monthOfYear+"========"+dob);
//                    String birth_date = DateFormat.getDateInstance().format(dob);
//                    date_ip.setText(birth_date);

                }
            }, year, month, day);
            datePickerDialog.show();
        }
        if (v == time_ip) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            Toast.makeText(this, "Hour:" + hour + " Minute:" + minute, Toast.LENGTH_SHORT).show();

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            time_ip.setText(hourOfDay + ":" + minute);
                        }
                    }, hour, minute, false);
            timePickerDialog.show();
        }
    }


    private void calculateAge() {
        SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyyy");

        final Calendar c = Calendar.getInstance();
//        c_year = c.get(Calendar.YEAR);
//        c_month = c.get(Calendar.MONTH) + 1;
//        c_day = c.get(Calendar.DAY_OF_MONTH);
        current = new Date(c_year, c_month, c_day);

        if (dob.after(current)) {
            Toasty.error(MainActivity.this, "Birthday can't be in future", Toast.LENGTH_SHORT, true).show();
            return;
        }
        // days of every month
        int month[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // if birth date is greater then current birth month then do not count this month and add 30 to the date so as to subtract the date and get the remaining days
        if (b_day > c_day) {
            c_day += month[b_month - 1];
            c_month = c_month - 1;
        }

        // if birth month exceeds current month, then do not count this year and add 12 to the month so that we can subtract and find out the difference
        if (b_month > c_month) {
            c_year = c_year - 1;
            c_month = c_month + 12;
        }

        // calculate date, month, year
        int calculated_date = c_day - b_day;
        int calculated_month = c_month - b_month;
        int calculated_year = c_year - b_year;

        int res_month = calculated_year*12 + calculated_month;
        int res_week = res_month*4 ;
        int res_day = res_week*7 + calculated_date;

        int res_hr = res_day*24;
        int res_min = res_hr*60;
        int res_sec = res_min*60;

//        DateC dateCaculator=DateCalculator.calculateAge(startDate.get(Calendar.DAY_OF_MONTH),startDate.get(Calendar.MONTH)+1,startDate.get(Calendar.YEAR),endDate.get(Calendar.DAY_OF_MONTH),endDate.get(Calendar.MONTH)+1,endDate.get(Calendar.YEAR));

        age_day = findViewById(R.id.age_day);
        age_month = findViewById(R.id.age_month);
        age_year = findViewById(R.id.age_year);

        age_day.setText(String.valueOf(calculated_date));
//        Integer new_age_month = (Integer) calculated_month + (-1);
        age_month.setText(String.valueOf(calculated_month));
        age_year.setText(String.valueOf(calculated_year));

//        Summary
        result_months = (TextView) findViewById(R.id.result_months);
        result_weeks = (TextView) findViewById(R.id.result_weeks);
        result_days = (TextView) findViewById(R.id.result_days);

        result_hours = (TextView) findViewById(R.id.result_hours);
        result_min = (TextView) findViewById(R.id.result_min);
        result_sec = (TextView) findViewById(R.id.result_sec);

        result_months.setText(String.valueOf(res_month)+" months");
        result_weeks.setText(String.valueOf(res_week)+" weeks");
        result_days.setText(String.valueOf(res_day)+" days");
        result_hours.setText(String.valueOf(res_hr)+" hrs");
        result_min.setText(String.valueOf(res_min)+" min");
        result_sec.setText(String.valueOf(res_sec)+" sec");



    }

    private void nextBirthday() {

        current = new Date(c_year, c_month, c_day);
        Date nextdob = dob;
        Log.v("Check_current","-----"+current +"===");
        Log.v("Check_dob","-----"+nextdob +"===");

        long difference = nextdob.getTime() - current.getTime();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(difference);

        next_b_day = findViewById(R.id.next_b_day);
        next_b_month = findViewById(R.id.next_b_month);

        Log.v("day of week",""+String.valueOf(cal.get(Calendar.DAY_OF_WEEK)) +"''''''''''"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH))+"''''''''''"+String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
        Log.v("Check Datatype","----->>>"+next_b_day.getClass().getName());

        next_b_month.setText(String.valueOf(cal.get(Calendar.MONTH)));
        Integer next_birth_day = (Integer) cal.get(Calendar.DAY_OF_MONTH) + (-1);
        next_b_day.setText(String.valueOf(next_birth_day));

    }
}