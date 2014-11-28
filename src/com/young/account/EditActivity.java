package com.young.account;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class EditActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

    private static final String TAG = "EditActivity";

    private int presetYear;
    private int presetMonth;
    private int presetDay;
    private int presetHour;
    private int presetMinute;

    private int pkid;
    private float oldPrice, newPrice;
    private String oldDate, newDate;
    private String oldTime, newTime;
    private String oldRemark, newRemark;
    private EditText priceEt;
    private EditText dateEt;
    private EditText timeEt;
    private RadioGroup typeRg;
    private RadioButton typeBtn1;
    private RadioButton typeBtn2;
    private RadioButton typeBtn3;
    private RadioButton typeBtn4;
    private RadioButton typeBtn5;
    private RadioButton typeBtn6;
    private RadioButton typeBtn7;
    private String oldType, newType;
    private RadioGroup methodRg;
    private RadioButton methodBtn1;
    private RadioButton methodBtn2;
    private RadioButton methodBtn3;
    private RadioButton methodBtn4;
    private String oldMethod, newMethod;
    private EditText remarkEt;

    private Button updateBtn;
    private Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addaccount);
        findViewById(R.id.background).setOnClickListener(this);

        // findview
        priceEt = (EditText)findViewById(R.id.priceEt);
        dateEt = (EditText)findViewById(R.id.dateEt);
        timeEt = (EditText)findViewById(R.id.timeEt);
        typeRg = (RadioGroup)findViewById(R.id.typeRg);
        typeBtn1 = (RadioButton)findViewById(R.id.typeBtn1);
        typeBtn2 = (RadioButton)findViewById(R.id.typeBtn2);
        typeBtn3 = (RadioButton)findViewById(R.id.typeBtn3);
        typeBtn4 = (RadioButton)findViewById(R.id.typeBtn4);
        typeBtn5 = (RadioButton)findViewById(R.id.typeBtn5);
        typeBtn6 = (RadioButton)findViewById(R.id.typeBtn6);
        typeBtn7 = (RadioButton)findViewById(R.id.typeBtn7);
        methodRg = (RadioGroup)findViewById(R.id.methodRg);
        methodBtn1 = (RadioButton)findViewById(R.id.methodBtn1);
        methodBtn2 = (RadioButton)findViewById(R.id.methodBtn2);
        methodBtn3 = (RadioButton)findViewById(R.id.methodBtn3);
        methodBtn4 = (RadioButton)findViewById(R.id.methodBtn4);
        remarkEt = (EditText)findViewById(R.id.remarkEt);
        updateBtn = (Button)findViewById(R.id.okBtn);
        deleteBtn = (Button)findViewById(R.id.cancelBtn);

        // get values from bundle
        Bundle bundle = this.getIntent().getExtras();
        pkid = bundle.getInt("pkid");
        oldPrice = bundle.getFloat("price");
        oldDate = bundle.getString("date");
        String[] tmpDate = oldDate.split("/", 3);
        presetYear = Integer.parseInt(tmpDate[0]);
        presetMonth = Integer.parseInt(tmpDate[1]) - 1;
        presetDay = Integer.parseInt(tmpDate[2]);
        Log.d(TAG, "presetDate : " + presetYear + " " + presetMonth + " " + presetDay);
        oldTime = bundle.getString("time");
        String[] tmpTime = oldTime.split(":", 2);
        presetHour = Integer.parseInt(tmpTime[0]);
        presetMinute = Integer.parseInt(tmpTime[1]);
        Log.d(TAG, "presetTime : " + presetHour + " " + presetMinute);
        oldRemark = bundle.getString("remark");
        // fill in price/date/time/remark EditText
        priceEt.setText(oldPrice + "");
        dateEt.setText(oldDate + "");
        dateEt.setInputType(InputType.TYPE_NULL);
        timeEt.setText(oldTime + "");
        timeEt.setInputType(InputType.TYPE_NULL);
        remarkEt.setText(oldRemark + "");
        // fill in type/method GroupRadio
        oldType = bundle.getString("type");
        newType = oldType;
        Log.i(TAG, "type = " + oldType);
        if (oldType.equals(getResources().getString(R.string.typeBtn1))) {
            typeBtn1.setChecked(true);
        } else if (oldType.equals(getResources().getString(R.string.typeBtn2))) {
            typeBtn2.setChecked(true);
        } else if (oldType.equals(getResources().getString(R.string.typeBtn3))) {
            typeBtn3.setChecked(true);
        } else if (oldType.equals(getResources().getString(R.string.typeBtn4))) {
            typeBtn4.setChecked(true);
        } else if (oldType.equals(getResources().getString(R.string.typeBtn5))) {
            typeBtn5.setChecked(true);
        } else if (oldType.equals(getResources().getString(R.string.typeBtn6))) {
            typeBtn6.setChecked(true);
        } else if (oldType.equals(getResources().getString(R.string.typeBtn7))) {
            typeBtn7.setChecked(true);
        }
        oldMethod = bundle.getString("method");
        newMethod = oldMethod;
        Log.i(TAG, "method = " + oldMethod);
        if (oldMethod.equals(getResources().getString(R.string.methodBtn1))) {
            methodBtn1.setChecked(true);
        } else if (oldMethod.equals(getResources().getString(R.string.methodBtn2))) {
            methodBtn2.setChecked(true);
        } else if (oldMethod.equals(getResources().getString(R.string.methodBtn3))) {
            methodBtn3.setChecked(true);
        } else if (oldMethod.equals(getResources().getString(R.string.methodBtn4))) {
            methodBtn4.setChecked(true);
        }

        // set the cursor in the end
        priceEt.requestFocus();
        priceEt.setSelection(priceEt.getText().length());

        // change button text
        updateBtn.setText("Update");
        deleteBtn.setText("Delete");

        // set listener
        dateEt.setOnClickListener(this);
        timeEt.setOnClickListener(this);
        typeRg.setOnCheckedChangeListener(this);
        methodRg.setOnCheckedChangeListener(this);
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.background :
            Log.i(TAG, "onClick background");
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            break;
        case R.id.dateEt :
            Log.i(TAG, "onClick dateEt");
            new DatePickerDialog(this, new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // preset value is the one modified last time
                    presetYear = year;
                    presetMonth = monthOfYear;
                    presetDay = dayOfMonth;
                    String fmonth = ((monthOfYear+1) / 10 == 0) ? "0"+(monthOfYear+1) : (monthOfYear+1)+"";
                    String fday = (dayOfMonth / 10 == 0) ? "0"+dayOfMonth : dayOfMonth+"";
                    dateEt.setText(year+"/"+fmonth+"/"+fday);
                }
            }, presetYear, presetMonth, presetDay).show();
            break;
        case R.id.timeEt :
            Log.i(TAG, "onClick timeEt");
            new TimePickerDialog(this, new OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                    presetHour = hourOfDay;
                    presetMinute = minute;
                    String fhour = (hourOfDay / 10 == 0) ? "0"+hourOfDay : hourOfDay+"";
                    String fminute = (minute / 10 == 0) ? "0" + minute : minute+"";
                    timeEt.setText(fhour+":"+fminute);
                }
            }, presetHour, presetMinute, true).show();
            break;
        case R.id.okBtn : // updateBtn
            Log.i(TAG, "onClick updateBtn");
            // get all contents and check price validity
            String priceStr = priceEt.getText().toString();
            if (!priceStr.equals("")) {
                newPrice = Float.parseFloat(priceStr);
            } else {
                String str1 = "[add fail]" + "\n\n" + "Price cannot be null";
                Toast toast1 = Toast.makeText(this, str1, Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.CENTER, 0, 0);
                toast1.show();
                priceEt.requestFocus();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.showSoftInput(priceEt, 0);
                    }
                }, 200);
                break;
            }
            newDate = dateEt.getText().toString();
            newTime = timeEt.getText().toString();
            newRemark = remarkEt.getText().toString();
            // update one account item in SQLite database
            DatabaseHelper dbHelper = new DatabaseHelper(EditActivity.this, "xb_account");
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            if (oldPrice != newPrice || oldDate != newDate || oldTime != newTime ||
                oldType != newType || oldMethod != newMethod || oldRemark != newRemark) {
                Log.i(TAG, "update one acoount item in sqlite database");
                Log.i(TAG, "oldPrice = " + oldPrice + ", newPrice = " + newPrice);
                Log.i(TAG, "oldDate = " + oldDate + ", newDate = " + newDate);
                Log.i(TAG, "oldTime = " + oldTime + ", newTime = " + newTime);
                Log.i(TAG, "oldType = " + oldType + ", newType = " + newType);
                Log.i(TAG, "oldMethod = " + oldMethod + ", newMethod = " + newMethod);
                Log.i(TAG, "oldRemark = " + oldRemark + ", newRemark = " + newRemark);
                db.execSQL("update accounts set price = " + newPrice +
                                             ", date = \"" + newDate +
                                             "\", time = \"" + newTime +
                                             "\", type = \"" + newType +
                                             "\", method = \"" + newMethod +
                                             "\", remark = \"" + newRemark + "\" where id = " + pkid);
                // show success info and exit to view page
                String str2 = "[update success]";
                Toast toast2 = Toast.makeText(this, str2, Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.CENTER, 0, 0);
                toast2.show();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            } else {
                String str3 = "[update fail]" + "\n\n" + "no one changed";
                Toast toast3 = Toast.makeText(this, str3, Toast.LENGTH_SHORT);
                toast3.setGravity(Gravity.CENTER, 0, 0);
                toast3.show();
            }
            break;
        case R.id.cancelBtn : // deleteBtn
            Log.i(TAG, "onClick deleteBtn");
            DatabaseHelper dbHelper2 = new DatabaseHelper(EditActivity.this, "xb_account");
            SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
            db2.execSQL("delete from accounts where id=" + pkid);
            String str = "[delete success]";
            Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finish();
                }
            }, 2500);
            break;
        default :
            break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
        case R.id.typeBtn1 :
            Log.i(TAG, "onCheckedChanged typeBtn1");
            newType = typeBtn1.getText().toString();
            break;
        case R.id.typeBtn2 :
            Log.i(TAG, "onCheckedChanged typeBtn2");
            newType = typeBtn2.getText().toString();
            break;
        case R.id.typeBtn3 :
            Log.i(TAG, "onCheckedChanged typeBtn3");
            newType = typeBtn3.getText().toString();
            break;
        case R.id.typeBtn4 :
            Log.i(TAG, "onCheckedChanged typeBtn4");
            newType = typeBtn4.getText().toString();
            break;
        case R.id.typeBtn5 :
            Log.i(TAG, "onCheckedChanged typeBtn5");
            newType = typeBtn5.getText().toString();
            break;
        case R.id.typeBtn6 :
            Log.i(TAG, "onCheckedChanged typeBtn6");
            newType = typeBtn6.getText().toString();
            break;
        case R.id.typeBtn7 :
            Log.i(TAG, "onCheckedChanged typeBtn7");
            newType = typeBtn7.getText().toString();
            break;
        case R.id.methodBtn1 :
            Log.i(TAG, "onCheckedChanged methodBtn1");
            newMethod = methodBtn1.getText().toString();
            break;
        case R.id.methodBtn2 :
            Log.i(TAG, "onCheckedChanged methodBtn2");
            newMethod = methodBtn2.getText().toString();
            break;
        case R.id.methodBtn3 :
            Log.i(TAG, "onCheckedChanged methodBtn3");
            newMethod = methodBtn3.getText().toString();
            break;
        case R.id.methodBtn4 :
            Log.i(TAG, "onCheckedChanged methodBtn4");
            newMethod = methodBtn4.getText().toString();
            break;
        default :
            break;
        }
    }
}
