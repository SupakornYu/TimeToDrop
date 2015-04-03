package com.earthquake.se.timetodrop;


import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Add_Item extends ActionBarActivity implements View.OnClickListener,SurfaceHolder.Callback
        , Camera.PictureCallback, Camera.ShutterCallback {
    Camera mCamera;
    SurfaceView mSurfaceView;
    boolean saveState = false;
    private String timeStamp;
    private static Button photoBtn;
    private static Button RetakeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__item);
        initialWidget();
        mSurfaceView.getHolder().addCallback(this);
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        photoBtn.setOnClickListener(this);
        RetakeBtn.setOnClickListener(this);
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    }
    private void initialWidget() {
        mSurfaceView = (SurfaceView) findViewById(R.id.cameraView);
        photoBtn = (Button) findViewById(R.id.photoBtn);
        RetakeBtn = (Button) findViewById(R.id.RePhotoBtn);
        photoBtn.setVisibility(View.VISIBLE);
        RetakeBtn.setVisibility(View.INVISIBLE);
    }

    public void onResume() {
        Log.d("System","onResume");
        super.onResume();
        if (mCamera == null) {
            photoBtn.setVisibility(View.VISIBLE);
            RetakeBtn.setVisibility(View.INVISIBLE);
        }
        mCamera = Camera.open();



    }

    public void onPause() {
        Log.d("System","onPause");
        super.onPause();
        if (mCamera != null){
            //              mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;

        }
        //mCamera.release();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__item, menu);
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

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("CameraSystem","surfaceChanged");
       Camera.Parameters params = mCamera.getParameters();
       /*List<Camera.Size> previewSize = params.getSupportedPreviewSizes();
        List<Camera.Size> pictureSize = params.getSupportedPictureSizes();
        params.setPictureSize(pictureSize.get(0).width, pictureSize.get(0).height);
        params.setPreviewSize(previewSize.get(0).width, previewSize.get(0).height);*/
        params.setRotation(90);
        params.setJpegQuality(100);
        mCamera.setParameters(params);
        mCamera.setDisplayOrientation(90);

        try {
            mCamera.setPreviewDisplay(mSurfaceView.getHolder());
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.photoBtn:
                saveState = true;
                mCamera.takePicture(Add_Item.this,null,null,Add_Item.this);
            case R.id.RePhotoBtn:
                photoBtn.setVisibility(View.VISIBLE);
                RetakeBtn.setVisibility(View.INVISIBLE);
                mCamera.startPreview();


        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

    Intent imgIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    String imageFileName = "IMG_" + timeStamp + ".jpg";
    File imgFolder = new File(Environment.getExternalStorageDirectory(), "DCIM/TTD");
    imgFolder.mkdirs();
    File output = new File(imgFolder, imageFileName);
    Uri uri = Uri.fromFile(output);
    imgIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

    OutputStream os;
        try {
            os = getContentResolver().openOutputStream(uri);
            os.write(data);
            os.flush();
            os.close();
            Toast.makeText(getApplicationContext(), imageFileName, Toast.LENGTH_SHORT).show();
            photoBtn.setVisibility(View.INVISIBLE);
            RetakeBtn.setVisibility(View.VISIBLE);
        } catch (FileNotFoundException e) {
        } catch (IOException e) { }
        Log.d("Camera","Restart Preview");
       mCamera.stopPreview();
       saveState = false;

    }

    @Override
    public void onShutter() {

    }


}
