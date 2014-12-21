package com.young.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ViewActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {

    private static final String TAG = "ViewActivity";

    private ListView listview;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> datalist;
    private ArrayList<Integer> pkid = new ArrayList<Integer>(); // primary key id
    private ArrayList<Float> price = new ArrayList<Float>();
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<String> time = new ArrayList<String>();
    private ArrayList<String> type = new ArrayList<String>();
    private ArrayList<String> method = new ArrayList<String>();
    private ArrayList<String> remark = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewaccount);

        listview = (ListView)findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);

        datalist = new ArrayList<Map<String, Object>>();
        adapter = new SimpleAdapter(this, getData(), R.layout.item,
                        new String[]{"numTt", "dateTt", "timeTt", "typeTt", "priceTt"},
                        new int[]{R.id.numTv_view, R.id.dateTv_view, R.id.timeTv_view, R.id.typeTv_view, R.id.priceTv_view});
        listview.setAdapter(adapter);
    }

    private List<Map<String, Object>> getData()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(ViewActivity.this, "xb_account");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursorNum = db.rawQuery("select count(*) from accounts", null);
        cursorNum.moveToFirst();
        int itemNum = (int)cursorNum.getLong(0);
        cursorNum.close();
        Log.d(TAG, "itemNum : " + itemNum);
        Cursor cursor = db.rawQuery("select * from accounts order by date, time", null);
        for (int i = 1; i <= itemNum; i++) {
            cursor.moveToNext();
            // get value from SQLite database
            pkid.add(cursor.getInt(cursor.getColumnIndex("id")));
            price.add(cursor.getFloat(cursor.getColumnIndex("price")));
            date.add(cursor.getString(cursor.getColumnIndex("date")));
            time.add(cursor.getString(cursor.getColumnIndex("time")));
            type.add(cursor.getString(cursor.getColumnIndex("type")));
            method.add(cursor.getString(cursor.getColumnIndex("method")));
            remark.add(cursor.getString(cursor.getColumnIndex("remark")));
            // print its value for debug
            Log.d(TAG, "row = " + i);
            Log.d(TAG, "id = " + pkid.get(i-1));
            Log.d(TAG, "price = " + price.get(i-1));
            Log.d(TAG, "date = " + date.get(i-1));
            Log.d(TAG, "time = " + time.get(i-1));
            Log.d(TAG, "type = " + type.get(i-1));
            Log.d(TAG, "metdho = " + method.get(i-1));
            Log.d(TAG, "remark = " + remark.get(i-1));
            // create map, fill in data and add to datalist
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("numTt", "- " + i + " -");
            map.put("dateTt", date.get(i-1));
            map.put("timeTt", time.get(i-1));
            map.put("typeTt", type.get(i-1));
            map.put("priceTt", price.get(i-1));
            datalist.add(map);
            Log.i(TAG, "add one list item successfully");
        }
        return datalist;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick");
        String str = "Method : " + method.get((int)id) + "\n" + "Remark : " + remark.get((int)id);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemLongClick");
        Intent intent_edit = new Intent(ViewActivity.this, EditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pkid", pkid.get((int)id));
        bundle.putFloat("price", price.get((int)id));
        bundle.putString("date", date.get((int)id));
        bundle.putString("time", time.get((int)id));
        bundle.putString("type", type.get((int)id));
        bundle.putString("method", method.get((int)id));
        bundle.putString("remark", remark.get((int)id));
        intent_edit.putExtras(bundle);
        startActivity(intent_edit);
        return false;
    }
}
