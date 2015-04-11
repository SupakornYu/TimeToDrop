package com.earthquake.se.timetodrop;

import java.util.ArrayList;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewDB extends ActionBarActivity {
    FoodDb mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    ListView listFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_db);
        mHelper = new FoodDb(this);
        mDb = mHelper.getReadableDatabase();

        mCursor = mDb.rawQuery("SELECT * FROM " + FoodDb.TABLE_NAME2, null);
        ArrayList<String> arr_list = new ArrayList<String>();
        mCursor.moveToFirst();
        while(!mCursor.isAfterLast() ){
            arr_list.add("Name : " + mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Item_Detail)) );
            mCursor.moveToNext();
        }
        ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_listview, arr_list);
        listFood = (ListView)findViewById(R.id.listFood);
        listFood.setAdapter(adapterDir);




    }
    public void onStop() {
        super.onStop();
        mHelper.close();
        mDb.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
