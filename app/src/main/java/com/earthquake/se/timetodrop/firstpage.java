package com.earthquake.se.timetodrop;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;


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
        final ArrayList<String> arr_list = new ArrayList<String>();
        mCursor.moveToFirst();
        while (!mCursor.isAfterLast()) {
            arr_list.add("Name : " + mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Name)));
            for (int i = 1; i < 50; i++) {
                arr_list.add("Lorem ipsum quis leo pharetra item #" + i);
            }
            mCursor.moveToNext();

        }

        ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr_list);
        SwingLeftInAnimationAdapter animationAdapter = new SwingLeftInAnimationAdapter(adapterDir);
        listFood = (ListView) findViewById(R.id.listFood);
        animationAdapter.setAbsListView(listFood);
        listFood.setAdapter(animationAdapter);

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
                break;




        }

        return super.onOptionsItemSelected(item);

    }
}