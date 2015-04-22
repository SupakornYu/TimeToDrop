package com.earthquake.se.timetodrop;

import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
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

import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class firstpage extends ActionBarActivity {
    private static final int INITIAL_DELAY_MILLIS = 300;
    FoodDb mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;
    DynamicListView listFood;
    private Calendar calCurr;
    static ArrayList<String> detail = new ArrayList<String>();
    static ArrayList<String> exp_date = new ArrayList<String>();
    static ArrayList<Integer> arr_list_id = new ArrayList<Integer>();
    static ArrayList<String> img_uri = new ArrayList<String>();
    static ArrayList<String> day_till_Expire = new ArrayList<String>();
    ArrayAdapter<String> adapterDir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bar_color)));
        manageDb();
       // getTodayDate();


    }


    private void manageDb() {
        mHelper = new FoodDb(this);
        mDb = mHelper.getReadableDatabase();
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT * FROM " + FoodDb.TABLE_NAME2, null);
        mCursor.moveToFirst();
        clearArray();
        Calendar toDayDate = Calendar.getInstance();
        while (!mCursor.isAfterLast()) {
            int id = mCursor.getInt(0);
            int imgID = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(mHelper.COL_P_id)));
            String exp_Date = mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Expire_date));
            String imgPath = getImgPath(imgID);
            String DayLeft = getCountDownDate(exp_Date,toDayDate);

          //  String b = String.valueOf(daysLeft);
          //  Toast.makeText(this, b ,Toast.LENGTH_LONG).show();
                detail.add(mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Item_Detail)));
                exp_date.add("Expire Date : " + exp_Date);
                //img_uri.add(mCursor.getString(mCursor.getColumnIndex(mHelper.COL_P_id)));
                img_uri.add(imgPath);
                day_till_Expire.add(DayLeft);
                arr_list_id.add(id);
                mCursor.moveToNext();

            }


            listFood = (DynamicListView) findViewById(R.id.listFood);
            adapterDir = new MyListAdapter(this, detail, exp_date, img_uri,day_till_Expire);
            SimpleSwipeUndoAdapter simpleSwipeUndoAdapter = new SimpleSwipeUndoAdapter(adapterDir, this, new MyOnDismissCallback(adapterDir));
            AlphaInAnimationAdapter animAdapter = new AlphaInAnimationAdapter(simpleSwipeUndoAdapter);
            animAdapter.setAbsListView(listFood);
            assert animAdapter.getViewAnimator() != null;
            animAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
            listFood.setAdapter(animAdapter);
            listFood.enableSimpleSwipeUndo();
        }

    private String getCountDownDate(String exp_Date, Calendar toDayDate) {
        SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd,yyyy");
        Calendar expDate = Calendar.getInstance();
        try {
            expDate.setTime(format1.parse(exp_Date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = expDate.getTimeInMillis() - toDayDate.getTimeInMillis()+1;
        String diffDays = String.valueOf((diff / (24 * 60 * 60 * 1000))+1);

        return diffDays;
    }


    private String getImgPath(int imgID) {
        String picPath = null;
        Cursor mCursor2;
        mHelper = new FoodDb(this);
        mDb = mHelper.getReadableDatabase();
        mDb = mHelper.getWritableDatabase();
        //String MY_QUERY = "SELECT "+mHelper.COL_Path+ " FROM " + FoodDb.TABLE_NAME3 + "WHERE"+mHelper.COLP_Photo_tag_id+"LIKE"+imgID;
        mCursor2 = mDb.rawQuery("SELECT * FROM " + FoodDb.TABLE_NAME3 +" WHERE "+mHelper.COLP_Photo_tag_id+" LIKE '"+imgID+"'" , null);
        mCursor2.moveToFirst();
            picPath = mCursor2.getString(2) ;
        mCursor2.close();

        return picPath;


    }

    public void clearArray(){
        detail.clear();
        arr_list_id.clear();
        exp_date.clear();
        day_till_Expire.clear();
        img_uri.clear();


    }
    public void onStop() {
        super.onStop();
       mHelper.close();
        mDb.close();

    }
 public void onResume(){
        super.onResume();
        manageDb();


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
                //overridePendingTransition(R.animator.animation1,R.animator.animation2);
                break;





        }

        return super.onOptionsItemSelected(item);

    }



    private class MyListAdapter extends ArrayAdapter<String> implements UndoAdapter {

        private final Context mContext;
     ArrayList<String> Detail= new ArrayList<String>();
        ArrayList<String> imgUri= new ArrayList<String>();
        ArrayList<String> expDate= new ArrayList<String>();
        ArrayList<String> daysLeft= new ArrayList<String>();
        MyListAdapter(final Context context, ArrayList<String> detail, ArrayList<String> exp_date, ArrayList<String> img_uri, ArrayList<String> day_till_Expire) {
            mContext = context;
            Detail = detail;
            expDate = exp_date;
            imgUri=img_uri;
            daysLeft = day_till_Expire;
            for (int i = 0; i < firstpage.detail.size(); i++) {
               add(firstpage.detail.get(i));

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
                view = LayoutInflater.from(mContext).inflate(R.layout.list_row_dynamiclistview,parent,false);
            }
            TextView txtDetail = (TextView) view.findViewById(R.id.list_row_draganddrop_textview);
            TextView txtDate = (TextView) view.findViewById(R.id.expDate);
            TextView txtDayLeft = (TextView) view.findViewById(R.id.daysLeft);
            ImageView imageView = (ImageView) view.findViewById(R.id.list_imgView);

            // txtPosition.setPadding(10, 0, 0, 0);
            txtDetail.setText(Detail.get(position));
            txtDate.setText(expDate.get(position));
            txtDayLeft.setText(daysLeft.get(position));
            // set picasso
            int radius = 30;
            int stroke = 5;
            int margin = 5;
            int width = 400;
            int height = 400;
           // String imageUri = "file:///storage/emulated/0/DCIM/TTD/IMG_20150417_170436.jpg";
            Picasso.with(getApplicationContext()).load(imgUri.get(position)).resize(width, height)
                    // .transform(new RoundedRectTransformation(radius, stroke, margin))

                    .into(imageView);
            //((TextView) view.findViewById(R.id.list_row_draganddrop_textview)).setText(getItem(position));

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

            adapterDir.notifyDataSetChanged();
            for (int position : reverseSortedPositions) {
                mAdapter.remove(position);
            }

            if (mToast != null) {
                mToast.cancel();
            }

            int id =  arr_list_id.get(Integer.parseInt(Arrays.toString(reverseSortedPositions).substring(1, 2)));
            int posittion =Integer.parseInt(Arrays.toString(reverseSortedPositions).substring(1,2));

            mToast = Toast.makeText(
                    firstpage.this,
                    getString(R.string.removed_positions, arr_list_id.get(Integer.parseInt(Arrays.toString(reverseSortedPositions).substring(1,2)))),
                    Toast.LENGTH_LONG
            );
            mToast.show();
            deleteRow(id,posittion);

        }

    }

    private void deleteRow(int id,int position) {
        mDb = mHelper.getWritableDatabase();
        // delete file from database
        mDb.execSQL("DELETE FROM " + FoodDb.TABLE_NAME2
                + " WHERE " +FoodDb.COL_Item_id+ "='"+id+"';");

        mDb.close();
        detail.remove(position);
        arr_list_id.remove(position);
        exp_date.remove(position);
    }

}