package com.earthquake.se.timetodrop;


import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.dd.CircularProgressButton;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.widget.ArrayAdapter;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.software.shell.fab.ActionButton;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;



public class Add_Item extends ActionBarActivity implements View.OnClickListener,SurfaceHolder.Callback
        , Camera.PictureCallback, Camera.ShutterCallback {
    Camera mCamera;
    private DatePickerDialog mDatePicker;
    CircularProgressButton mCircularButtonSimple;
    CircularProgressButton mCircularButtonComplete;
    CircularProgressButton mCircularButtonError;
    SurfaceView mSurfaceView;
    SurfaceHolder surfaceHolder;
    boolean saveState = false;
    private String timeStamp;
    private ActionButton photoBtn;
    private ActionButton RetakeBtn;
    private Button mDateButton;
    private Button saveBtn;
    private FlatButton one;
    private FlatButton two;
    private FlatButton three;
    private FlatButton four;
    private FlatButton five;
    private FlatButton sky;
    private FlatButton grape;
    private FlatButton blue;
    private FlatButton green;
    private FlatButton yellow;
    private FlatButton orange;
    private FlatButton red;
    private FlatButton grey;
    private Calendar mCalendar;
    private TextView mTextDate;
    private TextView ItemName;
    private  Uri imgUri;
    private int notification_day = 1;
   private String tagColor = "grey";
    FoodDb mHelper;
    SQLiteDatabase mDb;
    Calendar toDayDate = Calendar.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //flatUI
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.SEA);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.BLOSSOM, false));
       // actionBar.setIcon(R.drawable.ic_action_new);
        setContentView(R.layout.activity_add__item);
        mHelper = new FoodDb(this);
        mDb = mHelper.getWritableDatabase();
        initialWidget();
        buttonSetting1();
        buttonSetting2();
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bar_color)));
        grey.getBackground().setColorFilter(FlatUI.SNOW, PorterDuff.Mode.DARKEN);
        one.getBackground().setColorFilter(FlatUI.SEA, PorterDuff.Mode.DARKEN);
        sky.setOnClickListener(this);
        red.setOnClickListener(this);
        yellow.setOnClickListener(this);
        blue.setOnClickListener(this);
        orange.setOnClickListener(this);
        grape.setOnClickListener(this);
        grey.setOnClickListener(this);
        green.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        photoBtn.setOnClickListener(this);
        RetakeBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        mDateButton.setOnClickListener(this);
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mDatePicker = DatePickerDialog.newInstance(onDateSetListener,
                mCalendar.get(Calendar.YEAR),       // ปี
                mCalendar.get(Calendar.MONTH),      // เดือน
                mCalendar.get(Calendar.DAY_OF_MONTH),// วัน (1-31)
                false);

        ////////buttoncode////////////////////
    /*    mCircularButtonSimple = (CircularProgressButton)
                findViewById(R.id.circular_button_simple);

        mCircularButtonSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCircularButtonSimple.getProgress() == 0) {
                    mCircularButtonSimple.setProgress(50);
                } else if (mCircularButtonSimple.getProgress() == 100) {
                    mCircularButtonSimple.setProgress(0);
                } else {
                    mCircularButtonSimple.setProgress(100);
                }
            }
        });*/






    }

    private void buttonSetting1() {
        photoBtn = (ActionButton) findViewById(R.id. photoBtn);
        //color
        photoBtn.setButtonColor(getResources().getColor(R.color.fab_material_red_500));
        int buttonColor =  photoBtn.getButtonColor();
        photoBtn.setButtonColorPressed(getResources().getColor(R.color.fab_material_red_900));
        int buttonColorPressed =  photoBtn.getButtonColorPressed();
        boolean hasShadow =  photoBtn.hasShadow();
        //shadow
        photoBtn.setShadowColor(getResources().getColor(R.color.fab_material_grey_500));
        int shadowColor =  photoBtn.getShadowColor();
        photoBtn.setShadowRadius(5.0f);
        float shadowRadius =  photoBtn.getShadowRadius();
        photoBtn.setShadowXOffset(3.5f);
        float shadowXOffset =  photoBtn.getShadowXOffset();
        photoBtn.setShadowYOffset(3.0f);
        float shadowYOffset =  photoBtn.getShadowYOffset();
        boolean hasImage =  photoBtn.hasImage();
        photoBtn.setImageResource(R.drawable.ic_action_photo);
        Drawable image =  photoBtn.getImage();
        float imageSize =  photoBtn.getImageSize();
    }

    private void buttonSetting2() {
        RetakeBtn = (ActionButton) findViewById(R.id.RePhotoBtn);
        //color
        RetakeBtn.setButtonColor(getResources().getColor(R.color.fab_material_red_500));
        int buttonColor =  RetakeBtn.getButtonColor();
        RetakeBtn.setButtonColorPressed(getResources().getColor(R.color.fab_material_red_900));
        int buttonColorPressed = RetakeBtn.getButtonColorPressed();
        boolean hasShadow =  RetakeBtn.hasShadow();
        //shadow
        RetakeBtn.setShadowColor(getResources().getColor(R.color.fab_material_grey_500));
        int shadowColor =  RetakeBtn.getShadowColor();
        RetakeBtn.setShadowRadius(5.0f);
        float shadowRadius =  RetakeBtn.getShadowRadius();
        RetakeBtn.setShadowXOffset(3.5f);
        float shadowXOffset =  RetakeBtn.getShadowXOffset();
        RetakeBtn.setShadowYOffset(3.0f);
        float shadowYOffset =  RetakeBtn.getShadowYOffset();
        boolean hasImage =  RetakeBtn.hasImage();
        RetakeBtn.setImageResource(R.drawable.ic_action_refresh);
        Drawable image =  RetakeBtn.getImage();
        float imageSize =  RetakeBtn.getImageSize();
    }





    private void initialWidget() {
        mSurfaceView = (SurfaceView) findViewById(R.id.cameraView);
        photoBtn = (ActionButton) findViewById(R.id.photoBtn);
        RetakeBtn = (ActionButton) findViewById(R.id.RePhotoBtn);
        mDateButton = (Button) findViewById(R.id.button_date);
        mTextDate = (TextView) findViewById(R.id.text_Date);
        mCalendar = Calendar.getInstance();
        ItemName = (TextView) findViewById(R.id.Item_name);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        one = (FlatButton) findViewById(R.id.one);
        two = (FlatButton) findViewById(R.id.two);
        three = (FlatButton) findViewById(R.id.three);
        four = (FlatButton) findViewById(R.id.four);
        five = (FlatButton) findViewById(R.id.five);
        sky = (FlatButton) findViewById(R.id.sky);
        blue = (FlatButton) findViewById(R.id.blue);
        green = (FlatButton) findViewById(R.id.green);
        red = (FlatButton) findViewById(R.id.red);
        grey = (FlatButton) findViewById(R.id.grey);
        grape = (FlatButton) findViewById(R.id.grape);
        orange = (FlatButton) findViewById(R.id.orange);
        yellow = (FlatButton) findViewById(R.id.yellow);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__item, menu);
        return true;
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        photoBtn.setVisibility(View.VISIBLE);
        RetakeBtn.setVisibility(View.INVISIBLE);
        try {
            // open the camera
            mCamera = Camera.open();
        } catch (RuntimeException e) {
            // check for exceptions
            System.err.println(e);
            return;
        }
        Camera.Parameters param;
        param = mCamera.getParameters();
        param.setRotation(90);
        // modify parameter
        param.setRotation(90);
        param.setJpegQuality(100);
        mCamera.setParameters(param);
        mCamera.setDisplayOrientation(90);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            System.err.println(e);
            return;
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {


        if(v.equals(photoBtn)) {
                saveState = true;
                mCamera.takePicture(null, null, Add_Item.this);
        }else if(v.equals(RetakeBtn)){
                photoBtn.setVisibility(View.VISIBLE);
                RetakeBtn.setVisibility(View.INVISIBLE);
                mCamera.startPreview();
        }  else if(v.equals(mDateButton)) {
                mDatePicker.setYearRange(2000, 2020);
                mDatePicker.show(getSupportFragmentManager(), "datePicker");
            ////notification///////////////////////////
                }else if(v.equals(one)){
                notification_day = 1;
                one.getBackground().setColorFilter(FlatUI.SEA, PorterDuff.Mode.DARKEN);
                two.getAttributes().setTheme(FlatUI.SEA, getResources());
                three.getAttributes().setTheme(FlatUI.SEA, getResources());
                four.getAttributes().setTheme(FlatUI.SEA, getResources());
                five.getAttributes().setTheme(FlatUI.SEA, getResources());
                }else if(v.equals(two)){
                notification_day = 2;
                one.getAttributes().setTheme(FlatUI.SEA, getResources());
                two.getBackground().setColorFilter(FlatUI.SEA, PorterDuff.Mode.DARKEN);
                three.getAttributes().setTheme(FlatUI.SEA, getResources());
                four.getAttributes().setTheme(FlatUI.SEA, getResources());
                five.getAttributes().setTheme(FlatUI.SEA, getResources());
                }else if(v.equals(three)){
                notification_day = 3;
                one.getAttributes().setTheme(FlatUI.SEA, getResources());
                two.getAttributes().setTheme(FlatUI.SEA, getResources());
                three.getBackground().setColorFilter(FlatUI.SEA, PorterDuff.Mode.DARKEN);
                four.getAttributes().setTheme(FlatUI.SEA, getResources());
                five.getAttributes().setTheme(FlatUI.SEA, getResources());
                }else if(v.equals(four)){
                notification_day = 4;
                one.getAttributes().setTheme(FlatUI.SEA, getResources());
                two.getAttributes().setTheme(FlatUI.SEA, getResources());
                three.getAttributes().setTheme(FlatUI.SEA, getResources());
                four.getBackground().setColorFilter(FlatUI.SEA, PorterDuff.Mode.DARKEN);
                five.getAttributes().setTheme(FlatUI.SEA, getResources());
                }else if(v.equals(five)){
                notification_day = 5;
                one.getAttributes().setTheme(FlatUI.SEA, getResources());
                two.getAttributes().setTheme(FlatUI.SEA, getResources());
                three.getAttributes().setTheme(FlatUI.SEA, getResources());
                four.getAttributes().setTheme(FlatUI.SEA, getResources());
                five.getBackground().setColorFilter(FlatUI.SEA, PorterDuff.Mode.DARKEN);
                ////notification///////////////////////////////////
        }else if(v.equals(sky)){
            tagColor = "sky";
            sky.getBackground().setColorFilter(FlatUI.SKY, PorterDuff.Mode.DARKEN);
            red.getAttributes().setTheme(FlatUI.CANDY, getResources());
            orange.getAttributes().setTheme(FlatUI.ORANGE, getResources());
            yellow.getAttributes().setTheme(FlatUI.SAND, getResources());
            green.getAttributes().setTheme(FlatUI.GRASS, getResources());
            grape.getAttributes().setTheme(FlatUI.GRAPE, getResources());
            blue.getAttributes().setTheme(FlatUI.SEA, getResources());
            grey.getAttributes().setTheme(FlatUI.SNOW, getResources());
        }else if(v.equals(red)){
            tagColor = "red";
            sky.getAttributes().setTheme(FlatUI.SKY, getResources());
            red.getBackground().setColorFilter(FlatUI.CANDY, PorterDuff.Mode.DARKEN);
            orange.getAttributes().setTheme(FlatUI.ORANGE, getResources());
            yellow.getAttributes().setTheme(FlatUI.SAND, getResources());
            green.getAttributes().setTheme(FlatUI.GRASS, getResources());
            grape.getAttributes().setTheme(FlatUI.GRAPE, getResources());
            blue.getAttributes().setTheme(FlatUI.SEA, getResources());
            grey.getAttributes().setTheme(FlatUI.SNOW, getResources());
        }else if(v.equals(orange)){
            tagColor = "orange";
            sky.getAttributes().setTheme(FlatUI.SKY, getResources());
            red.getAttributes().setTheme(FlatUI.CANDY, getResources());
            orange.getBackground().setColorFilter(FlatUI.ORANGE, PorterDuff.Mode.DARKEN);
            yellow.getAttributes().setTheme(FlatUI.SAND, getResources());
            green.getAttributes().setTheme(FlatUI.GRASS, getResources());
            grape.getAttributes().setTheme(FlatUI.GRAPE, getResources());
            blue.getAttributes().setTheme(FlatUI.SEA, getResources());
            grey.getAttributes().setTheme(FlatUI.SNOW, getResources());
        }else if(v.equals(yellow)){
            tagColor = "yellow";
            sky.getAttributes().setTheme(FlatUI.SKY, getResources());
            red.getAttributes().setTheme(FlatUI.CANDY, getResources());
            orange.getAttributes().setTheme(FlatUI.ORANGE, getResources());
            yellow.getBackground().setColorFilter(FlatUI.SAND, PorterDuff.Mode.DARKEN);
            green.getAttributes().setTheme(FlatUI.GRASS, getResources());
            grape.getAttributes().setTheme(FlatUI.GRAPE, getResources());
            blue.getAttributes().setTheme(FlatUI.SEA, getResources());
            grey.getAttributes().setTheme(FlatUI.SNOW, getResources());
        }else if(v.equals(green)){
            tagColor = "green";
            sky.getAttributes().setTheme(FlatUI.SKY, getResources());
            red.getAttributes().setTheme(FlatUI.CANDY, getResources());
            orange.getAttributes().setTheme(FlatUI.ORANGE, getResources());
            yellow.getAttributes().setTheme(FlatUI.SAND, getResources());
            green.getBackground().setColorFilter(FlatUI.GRASS, PorterDuff.Mode.DARKEN);
            grape.getAttributes().setTheme(FlatUI.GRAPE, getResources());
            blue.getAttributes().setTheme(FlatUI.SEA, getResources());
            grey.getAttributes().setTheme(FlatUI.SNOW, getResources());
        }else if(v.equals(grape)){
            tagColor = "grape";
            sky.getAttributes().setTheme(FlatUI.SKY, getResources());
            red.getAttributes().setTheme(FlatUI.CANDY, getResources());
            orange.getAttributes().setTheme(FlatUI.ORANGE, getResources());
            yellow.getAttributes().setTheme(FlatUI.SAND, getResources());
            green.getAttributes().setTheme(FlatUI.GRASS, getResources());
            grape.getBackground().setColorFilter(FlatUI.GRAPE, PorterDuff.Mode.DARKEN);
            blue.getAttributes().setTheme(FlatUI.SEA, getResources());
            grey.getAttributes().setTheme(FlatUI.SNOW, getResources());
        }else if(v.equals(blue)) {
            tagColor = "blue";
            sky.getAttributes().setTheme(FlatUI.SKY, getResources());
            red.getAttributes().setTheme(FlatUI.CANDY, getResources());
            orange.getAttributes().setTheme(FlatUI.ORANGE, getResources());
            yellow.getAttributes().setTheme(FlatUI.SAND, getResources());
            green.getAttributes().setTheme(FlatUI.GRASS, getResources());
            grape.getAttributes().setTheme(FlatUI.GRAPE, getResources());
            grey.getAttributes().setTheme(FlatUI.SNOW, getResources());
            blue.getBackground().setColorFilter(FlatUI.SEA, PorterDuff.Mode.DARKEN);
        }else if(v.equals(grey)){
                tagColor = "grey";
                sky.getAttributes().setTheme(FlatUI.SKY, getResources());
                red.getAttributes().setTheme(FlatUI.CANDY, getResources());
                orange.getAttributes().setTheme(FlatUI.ORANGE, getResources());
                yellow.getAttributes().setTheme(FlatUI.SAND, getResources());
                green.getAttributes().setTheme(FlatUI.GRASS, getResources());
                grape.getAttributes().setTheme(FlatUI.GRAPE, getResources());
                blue.getAttributes().setTheme(FlatUI.SEA, getResources());
                grey.getBackground().setColorFilter(FlatUI.SNOW, PorterDuff.Mode.DARKEN);

        }else if(v.equals(saveBtn)) {
                String foodName = ItemName.getText().toString();
                String expireDate = mTextDate.getText().toString();
                int TagId = getTagId(tagColor);
                int diffDay = getCountDownDate(expireDate,toDayDate);
                if (foodName.length() == 0){
                    foodName = " ";

                }
                else {
                    foodName = ItemName.getText().toString();

                }
                if(expireDate.length() != 0 && imgUri != null && diffDay > 0) {
                    mDb.execSQL("INSERT INTO " + FoodDb.TABLE_NAME3 + " ("
                            + FoodDb.COL_Path + ") VALUES ('" + imgUri
                            + "');");
                    int photoId = getLastID();

                    /*Cursor mCursor = mDb.rawQuery("SELECT * FROM " + FoodDb.TABLE_NAME2
                            + " WHERE " + FoodDb.COL_Expire_date + "='" + expireDate + "';"
                            , null);*/
                    //if (mCursor.getCount() == 0) {
                        mDb.execSQL("INSERT INTO " + FoodDb.TABLE_NAME2 + " ("
                            + FoodDb.COL_Item_Detail +","+FoodDb.COL_Expire_date+ ","+FoodDb.COL_P_id+","+FoodDb.COL_Warn_days+","+FoodDb.COL_G_id+") VALUES ('" + foodName
                            + "','"+expireDate+"','"+photoId+"','"+notification_day+"','"+TagId+"');");


                    //}


                    ItemName.setText("");
                    //
                    //
                    //Toast.makeText(getApplicationContext(), "Finish!!!   "+TagId +tagColor, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Add Successful", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                } else if(expireDate.length() == 0){
                    Toast.makeText(getApplicationContext(),"Please Input Expire Date",Toast.LENGTH_SHORT).show();

                } else if(imgUri == null){
                    Toast.makeText(getApplicationContext(),"Please Take a Photo",Toast.LENGTH_SHORT).show();

                } else if(diffDay <= 0){
                    Toast.makeText(getApplicationContext(),"Please Intput Valid Expire Date",Toast.LENGTH_SHORT).show();

                }
            }

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    Intent imgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    String imageFileName = "IMG_" + timeStamp + ".jpg";
    File imgFolder = new File(Environment.getExternalStorageDirectory(), "DCIM/TTD");
    imgFolder.mkdirs();
    File output = new File(imgFolder, imageFileName);
    imgUri = Uri.fromFile(output);
    imgIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);

    OutputStream os;
        try {
            os = getContentResolver().openOutputStream(imgUri);
            os.write(data);
            os.flush();
            os.close();
          Toast.makeText(getApplicationContext(),"photo has been taken ", Toast.LENGTH_SHORT).show();
            photoBtn.setVisibility(View.INVISIBLE);
            RetakeBtn.setVisibility(View.VISIBLE);
        } catch (FileNotFoundException e) {
        } catch (IOException e) { }
        Log.d("Camera","Restart Preview");
      //  refreshCamera();


    }

    @Override
    public void onShutter() {

    }

    public int getLastID() {
        final String MY_QUERY = "SELECT MAX("+mHelper.COLP_Photo_tag_id+") FROM " + FoodDb.TABLE_NAME3;
        Cursor cur = mDb.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }


    private DatePickerDialog.OnDateSetListener onDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

                    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
                    mCalendar.set(year, month, day);
                    Date date = mCalendar.getTime();
                    String textDate = dateFormat.format(date);
                    mTextDate.setText(textDate);
                }
            };
    private int getCountDownDate(String exp_Date, Calendar toDayDate) {
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

        return diff_day;
    }

    private int getTagId(String Color) {

        Cursor mCursor4;
        mHelper = new FoodDb(this);
        mDb = mHelper.getReadableDatabase();
        mDb = mHelper.getWritableDatabase();
        //String MY_QUERY = "SELECT "+mHelper.COL_Path+ " FROM " + FoodDb.TABLE_NAME3 + "WHERE"+mHelper.COLP_Photo_tag_id+"LIKE"+imgID;
        mCursor4 = mDb.rawQuery("SELECT * FROM " + FoodDb.TABLE_NAME1 +" WHERE "+mHelper.COL_Colour+" LIKE '"+Color+"'" , null);
        mCursor4.moveToFirst();
        String tagID = mCursor4.getString(0) ;
        int t_id = Integer.parseInt(tagID);
        mCursor4.close();

        return t_id;


    }
}
