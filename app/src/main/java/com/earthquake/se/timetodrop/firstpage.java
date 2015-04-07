package com.earthquake.se.timetodrop;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class firstpage extends ActionBarActivity {
    FoodDb mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    ListView listFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bar_color)));
        mHelper = new FoodDb(this);
        mDb = mHelper.getReadableDatabase();

        mCursor = mDb.rawQuery("SELECT * FROM " + FoodDb.TABLE_NAME2, null);
        ArrayList<String> arr_list = new ArrayList<String>();
        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            arr_list.add("Name : " + mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Name)));
            mCursor.moveToNext();

        }

        listFood = (ListView) findViewById(R.id.listFood);
    }
    public void onStop() {
        super.onStop();
        mHelper.close();
        mDb.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_firstpage, menu);
        return true;
        //return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_new:
                Intent i = new Intent(getApplicationContext(), Add_Item.class);
                startActivity(i);
                overridePendingTransition(R.animator.animation1,R.animator.animation2);
                break;





        }

        return super.onOptionsItemSelected(item);

    }
}