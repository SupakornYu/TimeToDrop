package com.earthquake.se.timetodrop;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
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
import com.dd.CircularProgressButton;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.widget.ArrayAdapter;
import com.fourmob.datetimepicker.date.DatePickerDialog;

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
    private static ImageButton photoBtn;
    private static ImageButton RetakeBtn;
    private Button mDateButton;
    private Button saveBtn;
    private Calendar mCalendar;
    private TextView mTextDate;
    private TextView ItemName;
    private  Uri imgUri;

    FoodDb mHelper;
    SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //flatUI
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.SEA);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(FlatUI.getActionBarDrawable(this, FlatUI.GRASS, false));
        setContentView(R.layout.activity_add__item);
        mHelper = new FoodDb(this);
        mDb = mHelper.getWritableDatabase();
        initialWidget();
        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bar_color)));
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




    private void initialWidget() {
        mSurfaceView = (SurfaceView) findViewById(R.id.cameraView);
        photoBtn = (ImageButton) findViewById(R.id.photoBtn);
        RetakeBtn = (ImageButton) findViewById(R.id.RePhotoBtn);
        mDateButton = (Button) findViewById(R.id.button_date);
        mTextDate = (TextView) findViewById(R.id.text_Date);
        mCalendar = Calendar.getInstance();
        ItemName = (TextView) findViewById(R.id.Item_name);
        saveBtn = (Button) findViewById(R.id.saveBtn);

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
        }else if(v.equals(saveBtn)) {
                String foodName = ItemName.getText().toString();
                String expireDate = mTextDate.getText().toString();
                if (foodName.length() == 0){
                    foodName = " ";

                }
                else {
                    foodName = ItemName.getText().toString();

                }
                if(expireDate.length() != 0 && imgUri != null) {
                    mDb.execSQL("INSERT INTO " + FoodDb.TABLE_NAME3 + " ("
                            + FoodDb.COL_Path + ") VALUES ('" + imgUri
                            + "');");
                    int photoId = getLastID();

                    /*Cursor mCursor = mDb.rawQuery("SELECT * FROM " + FoodDb.TABLE_NAME2
                            + " WHERE " + FoodDb.COL_Expire_date + "='" + expireDate + "';"
                            , null);*/
                    //if (mCursor.getCount() == 0) {
                        mDb.execSQL("INSERT INTO " + FoodDb.TABLE_NAME2 + " ("
                            + FoodDb.COL_Item_Detail +","+FoodDb.COL_Expire_date+ ","+FoodDb.COL_P_id+") VALUES ('" + foodName
                            + "','"+expireDate+"','"+photoId+"');");

                    //}


                    ItemName.setText("");
                    Toast.makeText(getApplicationContext(), "Finish!!!   "+photoId, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), "Finish!!!", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                } else {
                    Toast.makeText(getApplicationContext(),"Please Expire Date",Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getApplicationContext(), imgUri.getPath(), Toast.LENGTH_SHORT).show();
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
}
