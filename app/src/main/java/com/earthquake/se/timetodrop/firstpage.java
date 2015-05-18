package com.earthquake.se.timetodrop;

import com.cengalabs.flatui.FlatUI;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.software.shell.fab.ActionButton;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import android.app.PendingIntent;
import android.app.AlarmManager;
import com.earthquake.se.timetodrop.ExpireAlarmNotification.MyReceiver;


public class firstpage extends ActionBarActivity {
    private static final int INITIAL_DELAY_MILLIS = 300;
    FoodDb mHelper;
    SQLiteDatabase mDb;
    Cursor mCursor;

    DynamicListView listFood;
    private  ActionButton addButton;
    private Calendar calCurr;
    static ArrayList<String> detail = new ArrayList<String>();
    static ArrayList<String> exp_date = new ArrayList<String>();
    static ArrayList<Integer> arr_list_id = new ArrayList<Integer>();
    static ArrayList<String> img_uri = new ArrayList<String>();
    static ArrayList<String> day_till_Expire = new ArrayList<String>();
    static ArrayList<String> color_Code = new ArrayList<String>();
    static ArrayList<String> day_color = new ArrayList<String>();
   // private TextView TagColor;
    ArrayAdapter<String> adapterDir;

    private PendingIntent pendingIntent;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bar_color)));
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.iconlogo);
        manageDb();
        buttonSetting();
      //  TagColor = (TextView) findViewById(R.id.Tagcolor);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(firstpage.this, Add_Item.class);
                startActivity(intent);
            }
        });

        alarm_notification_service();









    }

    private void buttonSetting() {
        addButton = (ActionButton) findViewById(R.id.add_button);
        //color
        addButton.setButtonColor(getResources().getColor(R.color.fab_material_red_500));
        int buttonColor = addButton.getButtonColor();
        addButton.setButtonColorPressed(getResources().getColor(R.color.fab_material_red_900));
        int buttonColorPressed = addButton.getButtonColorPressed();
        boolean hasShadow = addButton.hasShadow();
        //shadow
        addButton.setShadowColor(getResources().getColor(R.color.fab_material_grey_500));
        int shadowColor = addButton.getShadowColor();
        addButton.setShadowRadius(5.0f);
        float shadowRadius = addButton.getShadowRadius();
        addButton.setShadowXOffset(3.5f);
        float shadowXOffset = addButton.getShadowXOffset();
        addButton.setShadowYOffset(3.0f);
        float shadowYOffset = addButton.getShadowYOffset();
        boolean hasImage = addButton.hasImage();
        addButton.setImageResource(R.drawable.fab_plus_icon);
        Drawable image = addButton.getImage();
        float imageSize = addButton.getImageSize();
    }

    private void alarm_notification_service(){
        //PendingIntent pendingIntent;
        Calendar calendar = Calendar.getInstance();

        //calendar.set(Calendar.MONTH, 4);
        //calendar.set(Calendar.YEAR, 2015);
        //calendar.set(Calendar.DAY_OF_MONTH, 24);
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 1);
        //calendar.set(Calendar.SECOND, 0);

        Intent myIntent = new Intent(firstpage.this, MyReceiver.class); //create myIntent
        pendingIntent = PendingIntent.getBroadcast(firstpage.this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 *15, pendingIntent);
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
            int tagID = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(mHelper.COL_G_id)));;
            String DayLeft = getCountDownDate(exp_Date,toDayDate);
            int dayLefts = Integer.parseInt(DayLeft);
            if (dayLefts < 3 ){
                day_color.add("#ff1229");
               ;
            } else if (dayLefts < 6 ){
               day_color.add("#ffd800");
            }
            else {
                day_color.add("#34d44e");
            }
            String colorCode =  getColorCode(tagID);
          //  TagColor.setBackgroundColor(Color.parseColor(colorCode));
           // Toast.makeText(this, colorCode ,Toast.LENGTH_LONG).show();
          //  String b = String.valueOf(daysLeft);
          //  Toast.makeText(this, b ,Toast.LENGTH_LONG).show();
                detail.add(mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Item_Detail)));
                exp_date.add("Exp Date:" + exp_Date);
                //img_uri.add(mCursor.getString(mCursor.getColumnIndex(mHelper.COL_P_id)));
                img_uri.add(imgPath);
                day_till_Expire.add(DayLeft);
                arr_list_id.add(id);
                color_Code.add(colorCode);
                mCursor.moveToNext();

            }


            listFood = (DynamicListView) findViewById(R.id.listFood);
            adapterDir = new MyListAdapter(this, detail, exp_date, img_uri,day_till_Expire,color_Code,day_color);
            SimpleSwipeUndoAdapter simpleSwipeUndoAdapter = new SimpleSwipeUndoAdapter(adapterDir, this, new MyOnDismissCallback(adapterDir));
        SwingBottomInAnimationAdapter animAdapter = new SwingBottomInAnimationAdapter(simpleSwipeUndoAdapter);
            animAdapter.setAbsListView(listFood);
            assert animAdapter.getViewAnimator() != null;
            animAdapter.getViewAnimator().setInitialDelayMillis(INITIAL_DELAY_MILLIS);
            listFood.setAdapter(animAdapter);
            listFood.enableSimpleSwipeUndo();
        }

    private String getColorCode(int tagID) {
        String colorCode;
        Cursor mCursor3;
        mHelper = new FoodDb(this);
        mDb = mHelper.getReadableDatabase();
        mDb = mHelper.getWritableDatabase();
        //String MY_QUERY = "SELECT "+mHelper.COL_Path+ " FROM " + FoodDb.TABLE_NAME3 + "WHERE"+mHelper.COLP_Photo_tag_id+"LIKE"+imgID;
        mCursor3 = mDb.rawQuery("SELECT * FROM " + FoodDb.TABLE_NAME1 +" WHERE "+mHelper.COL_Group_id+" LIKE '"+tagID+"'" , null);
        mCursor3.moveToFirst();
        colorCode = mCursor3.getString(2) ;
        mCursor3.close();

        return colorCode;
    }

    private String getCountDownDate(String exp_Date, Calendar toDayDate) {
        SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd,yyyy");
        Calendar expDate = Calendar.getInstance();
        expDate.set(Calendar.HOUR_OF_DAY, 0);
        expDate.set(Calendar.MINUTE, 0);
        expDate.set(Calendar.SECOND, 0);
        expDate.set(Calendar.MILLISECOND,0);

        try {
            expDate.setTime(format1.parse(exp_Date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = expDate.getTimeInMillis() - toDayDate.getTimeInMillis()+1;
        int diff_day = (int) ((diff / (24 * 60 * 60 * 1000))+1);
        if(diff<=0){
            diff_day = 0;
        }
        String diffDays = String.valueOf(diff_day);
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
        color_Code.clear();
        day_color.clear();


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
   /*     switch (item.getItemId()) {
            case R.id.action_new:
                Intent i = new Intent(getApplicationContext(), setting_page.class);
                startActivity(i);
                //overridePendingTransition(R.animator.animation1,R.animator.animation2);
                break;





        }*/

        return super.onOptionsItemSelected(item);

    }




    private class MyListAdapter extends ArrayAdapter<String> implements UndoAdapter {

        private final Context mContext;
     ArrayList<String> Detail= new ArrayList<String>();
        ArrayList<String> imgUri= new ArrayList<String>();
        ArrayList<String> expDate= new ArrayList<String>();
        ArrayList<String> daysLeft= new ArrayList<String>();
        MyListAdapter(final Context context, ArrayList<String> detail, ArrayList<String> exp_date, ArrayList<String> img_uri, ArrayList<String> day_till_Expire, ArrayList<String> color_Code, ArrayList<String> day_color) {
            mContext = context;
           /* Detail = detail;
            expDate = exp_date;
            imgUri=img_uri;
            daysLeft = day_till_Expire;*/
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
            TextView txtDay = (TextView) view.findViewById(R.id.daysLefttext);
            TextView txtDayLeft = (TextView) view.findViewById(R.id.daysLeft);
            TextView tagColor = (TextView) view.findViewById(R.id.Tagcolor);

            ImageView imageView = (ImageView) view.findViewById(R.id.list_imgView);

            // txtPosition.setPadding(10, 0, 0, 0);
            txtDetail.setText(detail.get(position));
            txtDate.setText(exp_date.get(position));
          int dayLefts = Integer.parseInt(day_till_Expire.get(position));
           String day = String.valueOf(dayLefts);
           txtDayLeft.setText(day);
            /*
            if (dayLefts < 3 ){
                txtDay.setTextColor(Color.parseColor("#ff1229"));
                txtDayLeft.setTextColor(Color.parseColor("#ff1229"));
            } else if (dayLefts < 6 ){
                txtDay.setTextColor(Color.parseColor("#ffd800"));
            txtDayLeft.setTextColor(Color.parseColor("#ffd800"));
        }*/
            txtDay.setTextColor(Color.parseColor(day_color.get(position)));
            txtDayLeft.setTextColor(Color.parseColor(day_color.get(position)));
            tagColor.setBackgroundColor(Color.parseColor(color_Code.get(position)));



            // set picasso
            int radius = 30;
            int stroke = 5;
            int margin = 5;
            int width = 400;
            int height = 400;
           // String imageUri = "file:///storage/emulated/0/DCIM/TTD/IMG_20150417_170436.jpg";
            Picasso.with(getApplicationContext()).load(img_uri.get(position)).resize(width, height).centerCrop()
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
                  //  getString(R.string.removed_positions, arr_list_id.get(Integer.parseInt(Arrays.toString(reverseSortedPositions).substring(1,2)))),

                   "Deleted Completed",
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

       /* // Delete image from storage
        String imgpath = img_uri.get(position);
        Toast.makeText(this,imgpath,Toast.LENGTH_LONG).show();
        File imgFile = new File(imgpath);
        boolean deleted = imgFile.delete();
*/
        mDb.close();
        detail.remove(position);
        arr_list_id.remove(position);
        exp_date.remove(position);
        img_uri.remove(position);
        day_till_Expire.remove(position);
        color_Code.remove(position);
        day_color.remove(position);
    }



}