package com.young.account;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ViewActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {

    private static final String TAG = "xbAccount";
    private static final String subTAG = "(ViewActivity) ";
    private Context mContext;

    private ActionBar actionbar;
    private ListView listview;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> datalist;
    private ArrayList<Integer> pkid = new ArrayList<Integer>(); // primary key id
    private ArrayList<Integer> sync = new ArrayList<Integer>();
    private ArrayList<Float> price = new ArrayList<Float>();
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<String> time = new ArrayList<String>();
    private ArrayList<String> type = new ArrayList<String>();
    private ArrayList<String> method = new ArrayList<String>();
    private ArrayList<String> remark = new ArrayList<String>();

    private boolean syncfail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewaccount);
        mContext = ViewActivity.this;

        listview = (ListView)findViewById(R.id.listview);
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);

        datalist = new ArrayList<Map<String, Object>>();
        adapter = new SimpleAdapter(this, getData(), R.layout.item,
                        new String[]{"numTt", "dateTt", "timeTt", "typeTt", "priceTt"},
                        new int[]{R.id.numTv_view, R.id.dateTv_view, R.id.timeTv_view, R.id.typeTv_view, R.id.priceTv_view});
        listview.setAdapter(adapter);
        Bmob.initialize(this, "xxxxx");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_sync:
            syncData();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void syncData() {
        DatabaseHelper dbHelper = new DatabaseHelper(ViewActivity.this, "xb_account");
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursorNum = db.rawQuery("select count(*) from accounts where sync=0", null);
        cursorNum.moveToFirst();
        final int itemNum = (int)cursorNum.getLong(0);
        cursorNum.close();
        Log.d(TAG, subTAG + "itemNum : " + itemNum);
        if (itemNum >= 1) {
            Cursor cursor = db.rawQuery("select * from accounts where sync=0", null);
            List<BmobObject> itemobjs = new ArrayList<BmobObject>();
            Item itemobj = new Item();
            for (int i = 1; i <= itemNum; i++) {
                cursor.moveToNext();
                // get value from SQLite database
                int pkid = cursor.getInt(cursor.getColumnIndex("id"));
                int sync = cursor.getInt(cursor.getColumnIndex("sync"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String method = cursor.getString(cursor.getColumnIndex("method"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                // print its value for debug
                Log.d(TAG, subTAG + "row = " + i + ", " +
                                    "id = " + pkid + ", " +
                                    "sync = " + sync + ", " +
                                    "price = " + price + ", " +
                                    "date = " + date + ", " +
                                    "method = " + method + ", " +
                                    "remark = " + remark);
                // upload to cloud
                itemobj.setId(pkid);
                itemobj.setSync(sync);
                itemobj.setPrice(price);
                itemobj.setDate(date);
                itemobj.setTime(time);
                itemobj.setType(type);
                itemobj.setMethod(method);
                itemobj.setRemark(remark);
                itemobjs.add(itemobj);
            }
            new BmobObject().insertBatch(this, itemobjs, new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, subTAG + "sync ok!");
                    String upSync = "update accounts set sync=1 where sync=0";
                    db.execSQL(upSync);
                    Toast.makeText(mContext, "sync " + itemNum + " successfully",  Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(int code, String msg) {
                    syncfail = true;
                    Log.d(TAG, subTAG + "sync failed! msg = " + msg);
                    Toast.makeText(mContext, "sync failed",  Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mContext, "no need to sync",  Toast.LENGTH_SHORT).show();
        }
    }

    private List<Map<String, Object>> getData() {
        DatabaseHelper dbHelper = new DatabaseHelper(ViewActivity.this, "xb_account");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursorNum = db.rawQuery("select count(*) from accounts", null);
        cursorNum.moveToFirst();
        int itemNum = (int)cursorNum.getLong(0);
        cursorNum.close();
        Log.d(TAG, subTAG + "itemNum : " + itemNum);
        Cursor cursor = db.rawQuery("select * from accounts order by date, time", null);
        for (int i = 1; i <= itemNum; i++) {
            cursor.moveToNext();
            // get value from SQLite database
            pkid.add(cursor.getInt(cursor.getColumnIndex("id")));
            sync.add(cursor.getInt(cursor.getColumnIndex("sync")));
            price.add(cursor.getFloat(cursor.getColumnIndex("price")));
            date.add(cursor.getString(cursor.getColumnIndex("date")));
            time.add(cursor.getString(cursor.getColumnIndex("time")));
            type.add(cursor.getString(cursor.getColumnIndex("type")));
            method.add(cursor.getString(cursor.getColumnIndex("method")));
            remark.add(cursor.getString(cursor.getColumnIndex("remark")));
            // print its value for debug
            Log.d(TAG, subTAG + "row = " + i + ", " +
                                "id = " + pkid.get(i-1) + ", " +
                                "sync = " + sync.get(i-1) + ", " +
                                "price = " + price.get(i-1) + ", " +
                                "date = " + date.get(i-1) + ", " +
                                "time = " + time.get(i-1) + ", " +
                                "type = " + type.get(i-1) + ", " +
                                "method = " + method.get(i-1) + ", " +
                                "remark = " + remark.get(i-1));
            // create map, fill in data and add to datalist
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("numTt", "- " + i + " -");
            map.put("dateTt", date.get(i-1));
            map.put("timeTt", time.get(i-1));
            map.put("typeTt", type.get(i-1));
            map.put("priceTt", price.get(i-1));
            datalist.add(map);
            Log.i(TAG, subTAG + "add one list item successfully");
        }
        return datalist;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, subTAG + "onItemClick");
        String str = "Method : " + method.get((int)id) + "\n" + "Remark : " + remark.get((int)id) + "\n" + "Sync : " + sync.get((int)id);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, subTAG + "onItemLongClick");
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
