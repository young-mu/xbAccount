package com.young.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    private static final String TAG = "MainActivity";

    private Button addBtn;
    private Button viewBtn;
    private TextView mainTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        addBtn = (Button)findViewById(R.id.addBtn);
        viewBtn = (Button)findViewById(R.id.viewBtn);
        mainTv = (TextView)findViewById(R.id.mainTv);

        addBtn.setOnClickListener(this);
        viewBtn.setOnClickListener(this);
        mainTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.addBtn :
            Log.i(TAG, "onClick addBtn");
            Intent intent_add = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent_add);
            break;
        case R.id.viewBtn :
            Log.i(TAG, "onClick viewBtn");
            Intent intent_view = new Intent(MainActivity.this, ViewActivity.class);
            startActivity(intent_view);
            break;
        case R.id.mainTv :
            Log.i(TAG, "onClick mainTv");
            String str = getResources().getString(R.string.mainTv);
            mainTv.setText(str + " for xb");
        default :
            break;
        }
    }
}
