package com.young.account;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.young.account.DatabaseHelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	
	private static final String TAG = "AddActivity";
	private static boolean okFlag = false;
	private static boolean cancelFlag = false;
	
	private Calendar calendar;
	private int curYear, presetYear;
	private int curMonth, presetMonth;
	private int curDay, presetDay;
	private int curHour, presetHour;
	private int curMinute, presetMinute;

	private EditText priceEt;
	private EditText dateEt;
	private EditText timeEt;
	private Button okBtn;
	private Button cancelBtn;
	private RadioGroup typeRg;
	private RadioButton typeBtn1;
	private RadioButton typeBtn2;
	private RadioButton typeBtn3;
	private RadioButton typeBtn4;
	private RadioButton typeBtn5;
	private RadioButton typeBtn6;
	private RadioButton typeBtn7;
	private String type;
	private RadioGroup methodRg;
	private RadioButton methodBtn1;
	private RadioButton methodBtn2;
	private RadioButton methodBtn3;
	private RadioButton methodBtn4;
	private String method;
	private EditText remarkEt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addaccount);
		findViewById(R.id.background).setOnClickListener(this);
		
		// get date and time 
		calendar = Calendar.getInstance();
		curYear = calendar.get(Calendar.YEAR);
		presetYear = curYear;
		curMonth = calendar.get(Calendar.MONTH) + 1;
		presetMonth = curMonth - 1;
		curDay = calendar.get(Calendar.DAY_OF_MONTH);
		presetDay = curDay;
		curHour = calendar.get(Calendar.HOUR_OF_DAY);
		presetHour = curHour;
		curMinute = calendar.get(Calendar.MINUTE);
		presetMinute = curMinute;
			
		// findview
		priceEt = (EditText)findViewById(R.id.priceEt);
		dateEt = (EditText)findViewById(R.id.dateEt);
		timeEt = (EditText)findViewById(R.id.timeEt);
		okBtn = (Button)findViewById(R.id.okBtn);
		cancelBtn = (Button)findViewById(R.id.cancelBtn);
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
		
		// initialization
		String fmonth = (curMonth / 10 == 0) ? "0"+curMonth : curMonth+"";
    	String fday = (curDay / 10 == 0) ? "0"+curDay : curDay+"";
		dateEt.setText(curYear+"/"+fmonth+"/"+fday);
		String fhour = (curHour / 10 == 0) ? "0"+curHour : curHour+"";
		String fminute = (curMinute / 10 == 0) ? "0"+curMinute : curMinute+"";
		timeEt.setText(fhour+":"+fminute);
		dateEt.setInputType(InputType.TYPE_NULL);
		timeEt.setInputType(InputType.TYPE_NULL);
		type = typeBtn1.getText().toString();
		method = methodBtn1.getText().toString();
		
		// set listener
		dateEt.setOnClickListener(this);
		timeEt.setOnClickListener(this);
		typeRg.setOnCheckedChangeListener(this);
		methodRg.setOnCheckedChangeListener(this);
		okBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		
		// show keyboard
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
		    @Override
		    public void run() {
		        // Contextµ÷ÓÃgetSystemService£¨MainActivity.this.getSystemService£©
		        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE); 
		        imm.showSoftInput(priceEt, 0);
		    }
		}, 500);
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
	    case R.id.okBtn :
	    	Log.i(TAG, "onClick okBtn");
	    	
	    	// get all contents and check price validity 
	    	String priceStr = priceEt.getText().toString();
	    	Float price2submit = 0.0f;
	    	if (!priceStr.equals("")) {
	    		price2submit = Float.parseFloat(priceStr);
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
	    	String date2submit = dateEt.getText().toString();
	    	String time2submit = timeEt.getText().toString();
	    	String type2submit = type;
	    	String method2submit = method;
	    	String remark2submit = remarkEt.getText().toString();
	    	
	    	// add to SQLite database
	    	Log.i(TAG, "add acoount item to sqlite database");
	    	DatabaseHelper dbHelper = new DatabaseHelper(AddActivity.this, "xb_account");
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			ContentValues value = new ContentValues();
			value.put("price", price2submit);
			value.put("date", date2submit);
			value.put("time",  time2submit);
			value.put("type",  type2submit);
			value.put("method", method2submit);
			value.put("remark",  remark2submit);
			db.insert("accounts", null, value);
			Log.i(TAG, "add success");
			
			// show success info and exit to home page
	    	String str2 = "[add success]" + "\n\n"
    				+ "Price : " + price2submit + "\n" 
    				+ "Date : " + date2submit + "\n" 
    				+ "Time : " + time2submit + "\n"
    				+ "Type : " + type2submit + "\n" 
    				+ "Method :" + method2submit + "\n"
    				+ "Remark : " + remark2submit;
			Toast toast2 = Toast.makeText(this, str2, Toast.LENGTH_SHORT);
	    	toast2.setGravity(Gravity.CENTER, 0, 0);
	    	toast2.show();
			Timer timer = new Timer();
	    	timer.schedule(new TimerTask() {
	    		@Override
	    		public void run() {
	    			finish();
	    		}
	    	}, 2500);
	    	
	    	// set okFlag as true to remove shared_pref
	    	okFlag = true;
	    	break;
	    case R.id.cancelBtn :
	    	Log.i(TAG, "onClick cancelBtn");
	    	// set cancelFlag as true to remove shared_pref
	    	cancelFlag = true;
	    	finish();
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
			type = typeBtn1.getText().toString();
			break;
		case R.id.typeBtn2 :
			Log.i(TAG, "onCheckedChanged typeBtn2");
			type = typeBtn2.getText().toString();
			break;
		case R.id.typeBtn3 :
			Log.i(TAG, "onCheckedChanged typeBtn3");
			type = typeBtn3.getText().toString();
			break;
		case R.id.typeBtn4 :
			Log.i(TAG, "onCheckedChanged typeBtn4");
			type = typeBtn4.getText().toString();
			break;
		case R.id.typeBtn5 :
			Log.i(TAG, "onCheckedChanged typeBtn5");
			type = typeBtn5.getText().toString();
			break;
		case R.id.typeBtn6 :
			Log.i(TAG, "onCheckedChanged typeBtn6");
			type = typeBtn6.getText().toString();
			break;
		case R.id.typeBtn7 :
			Log.i(TAG, "onCheckedChanged typeBtn7");
			type = typeBtn7.getText().toString();
			break;
		case R.id.methodBtn1 :
			Log.i(TAG, "onCheckedChanged methodBtn1");
			method = methodBtn1.getText().toString();
			break;
		case R.id.methodBtn2 :
			Log.i(TAG, "onCheckedChanged methodBtn2");
			method = methodBtn2.getText().toString();
			break;
		case R.id.methodBtn3 :
			Log.i(TAG, "onCheckedChanged methodBtn3");
			method = methodBtn3.getText().toString();
			break;
		case R.id.methodBtn4 :
			Log.i(TAG, "onCheckedChanged methodBtn4");
			method = methodBtn4.getText().toString();
			break;
		default :
			break;
		}
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    Log.i(TAG, "onPause");
	    if (okFlag | cancelFlag) {
	    	okFlag = false;
	    	cancelFlag = false;
	    	// remove shared_prefs key-value pairs 
	    	SharedPreferences sp = getSharedPreferences("addItem", MODE_PRIVATE); 
	    	Editor editor = sp.edit();
	    	editor.remove("price");
	    	editor.remove("date");
	    	editor.remove("time");
	    	editor.remove("type");
	    	editor.remove("method");
	    	editor.remove("remark");
	    	editor.commit();
	    } else {
	    	// add shared_prefs key-value pairs 
		    SharedPreferences sp = getSharedPreferences("addItem", MODE_PRIVATE); 
		    Editor editor = sp.edit();
		    String tmpPrice = priceEt.getText().toString();
		    if (tmpPrice.equals("")) {
		    	editor.putFloat("price", 0.0f);
		    } else {
		    	editor.putFloat("price", Float.parseFloat(tmpPrice));
		    }
		    editor.putString("date", dateEt.getText().toString());
		    editor.putString("time",  timeEt.getText().toString());
		    editor.putString("type", type);
		    editor.putString("method", method);
		    editor.putString("remark", remarkEt.getText().toString());
		    editor.commit();
	    }
	}

	@Override
	protected void onResume() {
	    super.onResume();
	    Log.i(TAG, "onResume");
	        
	    SharedPreferences sp = getSharedPreferences("addItem", MODE_PRIVATE);
	    // set priceEt if getString succeed
	    Float price = sp.getFloat("price", 0.0f);
	    Log.i(TAG, "price = " + price);
	    if (price != 0.0) {
	    	priceEt.setText(price + "");
	    	priceEt.setSelection((price + "").length()); // set the cursor in the end
	    }
	   
	    // set dateEt if getString succeed
	    String date = sp.getString("date", "");
	    Log.i(TAG, "date = " + date);
	    if (!date.equals("")) {
	    	dateEt.setText(date);
	    }
	    // set timeEt if getString succeed
	    String time = sp.getString("time", "");
	    Log.i(TAG, "time = " + time);
	    if (!time.equals("")) {
	    	timeEt.setText(time);
	    }
	    // check typeGp if getString succeed
	    String type = sp.getString("type", "");
	    Log.i(TAG, "type = " + type);
	    if (type.equals(getResources().getString(R.string.typeBtn1))) {
	    	typeBtn1.setChecked(true);
	    } else if (type.equals(getResources().getString(R.string.typeBtn2))) {
	    	typeBtn2.setChecked(true);
	    } else if (type.equals(getResources().getString(R.string.typeBtn3))) {
	    	typeBtn3.setChecked(true);
	    } else if (type.equals(getResources().getString(R.string.typeBtn4))) {
	    	typeBtn4.setChecked(true);
	    } else if (type.equals(getResources().getString(R.string.typeBtn5))) {
	    	typeBtn5.setChecked(true);
	    } else if (type.equals(getResources().getString(R.string.typeBtn6))) {
	    	typeBtn6.setChecked(true);
	    } else if (type.equals(getResources().getString(R.string.typeBtn7))) {
	    	typeBtn7.setChecked(true);
	    }    
	    // check methodGp if getString succeed
	    String method = sp.getString("method", "");
	    Log.i(TAG, "method = " + method);
	    if (method.equals(getResources().getString(R.string.methodBtn1))) {
	    	methodBtn1.setChecked(true);
	    } else if (method.equals(getResources().getString(R.string.methodBtn2))) {
	    	methodBtn2.setChecked(true); 
	    } else if (method.equals(getResources().getString(R.string.methodBtn3))) {
	    	methodBtn3.setChecked(true); 
	    } else if (method.equals(getResources().getString(R.string.methodBtn4))) {
	    	methodBtn4.setChecked(true); 
	    }
	    // set remarkEt if getString succeed
	    String remark = sp.getString("remark", "");
	    Log.i(TAG, "remark = " + remark);
	    if (!remark.equals("")) {
	    	remarkEt.setText(remark);
	    }
	}
	
}
