package com.earthquake.se.timetodrop;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;


import java.util.ArrayList;
import java.util.Arrays;


public class firstpage extends ActionBarActivity {
    private static final int INITIAL_DELAY_MILLIS = 300;
    FoodDb mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    DynamicListView listFood;
    static ArrayList<String> arr_list = new ArrayList<String>();
    static ArrayList<Integer> arr_list_id = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bar_color)));
        mHelper = new FoodDb(this);
        mDb = mHelper.getReadableDatabase();
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT * FROM " + FoodDb.TABLE_NAME2, null);
        mCursor.moveToFirst();
        arr_list.clear();
        while (!mCursor.isAfterLast()) {
            int id = mCursor.getInt(0);
            arr_list.add("Name : " + mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Name)));
            arr_list_id.add(id);

            mCursor.moveToNext();

        }



        listFood = (DynamicListView) findViewById(R.id.listFood);
        ArrayAdapter<String> adapterDir = new MyListAdapter(this);
        SimpleSwipeUndoAdapter simpleSwipeUndoAdapter = new SimpleSwipeUndoAdapter(adapterDir, this, new MyOnDismissCallback(adapterDir));
        AlphaInAnimationAdapter animAdapter = new AlphaInAnimationAdapter(simpleSwipeUndoAdapter);
        animAdapter.setAbsListView(listFood);
        assert animAdapter.getViewAnimator() != null;
        animAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
        listFood.setAdapter(animAdapter);
        listFood.enableSimpleSwipeUndo();



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
                Intent i = new Intent(getApplicationContext(), AddNew_Item.class);
                startActivity(i);
                break;




        }

        return super.onOptionsItemSelected(item);

    }

    private static class MyListAdapter extends ArrayAdapter<String> implements UndoAdapter {

        private final Context mContext;

        MyListAdapter(final Context context) {
            mContext = context;
            for (int i = 0; i < arr_list.size(); i++) {
                add(arr_list.get(i)+String.valueOf(arr_list_id.get(i)));

            }
        }


        @Override
        public long getItemId(final int position) {
            return getItem(position).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_row_dynamiclistview, parent, false);
            }

            ((TextView) view.findViewById(R.id.list_row_draganddrop_textview)).setText(getItem(position));

            return view;
        }

        @NonNull
        @Override
        public View getUndoView(final int position, final View convertView, @NonNull final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.undo_row, parent, false);
            }
            return view;
        }

        @NonNull
        @Override
        public View getUndoClickView(@NonNull final View view) {
            return view.findViewById(R.id.undo_row_undobutton);
        }
    }
    private class MyOnDismissCallback implements OnDismissCallback {

        private final ArrayAdapter<String> mAdapter;

        @Nullable
        private Toast mToast;

        MyOnDismissCallback(final ArrayAdapter<String> adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                mAdapter.remove(position);
            }

            if (mToast != null) {
                mToast.cancel();
            }
            mDb = mHelper.getWritableDatabase();
            // delete file from database
           mDb.execSQL("DELETE FROM " + FoodDb.TABLE_NAME2
                    + " WHERE " +FoodDb.COL_Itemid+ "='"+arr_list_id.get(Integer.parseInt(Arrays.toString(reverseSortedPositions).substring(1,2)))+"';");
           mDb.close();
            mToast = Toast.makeText(
                    firstpage.this,
                    getString(R.string.removed_positions, arr_list_id.get(Integer.parseInt(Arrays.toString(reverseSortedPositions).substring(1,2)))),
                    Toast.LENGTH_LONG
            );
            mToast.show();

        }

    }

}