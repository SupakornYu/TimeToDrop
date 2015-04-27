package com.earthquake.se.timetodrop.ExpireAlarmNotification;

import com.earthquake.se.timetodrop.FoodDb;
import com.earthquake.se.timetodrop.R;
import com.earthquake.se.timetodrop.firstpage;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MyAlarmService extends Service{

        private NotificationManager mManager;
        private int num_notification = 0;
        private FoodDb mHelper;
        private SQLiteDatabase mDb;
        private Cursor mCursor;




    @Override
        public IBinder onBind(Intent arg0)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void onCreate()
        {
            // TODO Auto-generated method stub
            super.onCreate();

            mHelper = new FoodDb(this);
            mDb = mHelper.getReadableDatabase();
        }

        @SuppressWarnings("static-access")
        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
            super.onStartCommand(intent,flags, startId);

            ArrayList<ArrayList<String>> data_items = new ArrayList<ArrayList<String>>();
            ArrayList<String> each_row = new ArrayList<String>();

            Calendar calendar_now = Calendar.getInstance();
            //calendar.set(Calendar.MONTH, 4);
            //calendar.set(Calendar.YEAR, 2015);
            //calendar.set(Calendar.DAY_OF_MONTH, 24);
            calendar_now.setTimeInMillis(System.currentTimeMillis());

            calendar_now.set(Calendar.HOUR_OF_DAY, 0);
            calendar_now.set(Calendar.MINUTE, 0);
            calendar_now.set(Calendar.SECOND, 0);
            calendar_now.set(Calendar.MILLISECOND,0);

            int warning_items = 0;
            int expire_tomorrow = 0;
            int waste_items = 0;
            Calendar calendar_sim = Calendar.getInstance();

            String dtStart = "11/08/2013 08:48:10 April 30, 2015";
            SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");


            mCursor = mDb.rawQuery("SELECT * FROM " + mHelper.TABLE_NAME2 + " , " + mHelper.TABLE_NAME3 + " where " + mHelper.COL_P_id + " = " + mHelper.COLP_Photo_tag_id, null);
            mCursor.moveToFirst();

            while ( !mCursor.isAfterLast() ){
                each_row = new ArrayList<String>();
                each_row.add(mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Item_id)));
                each_row.add(mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Expire_date)));
                each_row.add(mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Warn_days)));
                each_row.add(mCursor.getString(mCursor.getColumnIndex(mHelper.COL_Path)));
                data_items.add(each_row);
                Log.i("data : ",""+each_row);
                mCursor.moveToNext();
            }



            for(int i = 0;i<data_items.size();i++) {
                try {
                    calendar_sim.setTime(format.parse(data_items.get(i).get(1)));
                }catch (Exception e) {
                    e.printStackTrace();
                }

                long diff = calendar_sim.getTimeInMillis() - calendar_now.getTimeInMillis() ;
                if(diff>0&&diff<=86400000){
                    expire_tomorrow +=1;
                }else if(diff<=0){
                    waste_items +=1;
                }

                long diff_warning = calendar_sim.getTimeInMillis() - (calendar_now.getTimeInMillis() + ((Long.parseLong(data_items.get(i).get(2))-1)*86400000)) ;
                if(diff_warning>0&&diff_warning<=86400000){
                    warning_items +=1;
                }

                Log.i("Expire Tomorrow : ",i+" "+expire_tomorrow+" "+calendar_now+" "+calendar_sim+" "+diff+" "+diff_warning);
                //String diffDays = String.valueOf((diff / (24 * 60 * 60 * 1000))+1);
                //Log.i("Data : ", "Item_id = " + data_items.get(i) + " " +data_items.get(i).get(1) +" "+ data_items.get(i).get(2)+" "+ data_items.get(i).get(3));
            }

            mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(this.getApplicationContext(),firstpage.class);

            //Notification notification = new Notification(R.mipmap.ic_launcher,"This is a test message!", System.currentTimeMillis());
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
            //notification.flags |= Notification.FLAG_AUTO_CANCEL;
            //notification.setLatestEventInfo(this.getApplicationContext(), "AlarmManagerDemo", "This is a test message! "+num_notification, pendingNotificationIntent);



            Notification noti = new Notification.Builder(this)
                    .setContentTitle("Time To Drop")
                    .setContentText(expire_tomorrow+" items will expired tomorrow. "+warning_items+" warning items. " +" and "+waste_items+" waste items.")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingNotificationIntent).build();
            noti.flags |= Notification.FLAG_AUTO_CANCEL;




            //mManager.notify(num_notification, notification);
            if(waste_items>0 || warning_items>0 || expire_tomorrow>0){
                mManager.notify(num_notification, noti);
            }

            //num_notification+=1;
            Log.i("Notification Count :", " "+num_notification);

            return START_STICKY;
        }

        @Override
        public void onDestroy()
        {
            // TODO Auto-generated method stub
            super.onDestroy();
        }

}
