package com.earthquake.se.timetodrop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class AddNew_Item extends ActionBarActivity implements View.OnClickListener{

    private static Button CameraBtn;
    private static Button saveBtn;
    private static EditText editFoodName;
    private static int CAMERA_ACTIVITY_REQ = 1;
    private static int GALLERY_REQ = 2;
    private static final String[] Image_Action = {"Camera", "Gallery"};
    MyDbHelper mHelper;
    SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new__item);
        mHelper = new MyDbHelper(this);
        mDb = mHelper.getWritableDatabase();
        initialWidget();
        CameraBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);


    }


    private void initialWidget() {
        saveBtn = (Button)  findViewById(R.id.saveBtn);
        CameraBtn = (Button) findViewById(R.id.CameraBtn);
        editFoodName = (EditText)findViewById(R.id.editText2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new__item, menu);
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.CameraBtn:
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNew_Item.this);
                builder.setTitle("Choose Existing");
                builder.setItems(Image_Action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected = Image_Action[which];
                        if (Image_Action[which].equals("Camera")) {
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, CAMERA_ACTIVITY_REQ);
                        }
                        else if (Image_Action[which].equals("Gallery")) {

                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent,GALLERY_REQ);
                        }
                        }


                });
                builder.setNegativeButton("cancel", null);
                builder.create();
                builder.show();

                //Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent, CAMERA_ACTIVITY_REQ);
                break;
            case R.id.saveBtn:
                String foodName = editFoodName.getText().toString();
                if(foodName.length() != 0) {
                    Cursor mCursor = mDb.rawQuery("SELECT * FROM " + MyDbHelper.TABLE_NAME2
                            + " WHERE " + MyDbHelper.COL_Name + "='" + foodName + "'"
                            , null);
                    if (mCursor.getCount() == 0) {
                        mDb.execSQL("INSERT INTO " + MyDbHelper.TABLE_NAME2 + " ("
                                + MyDbHelper.COL_Name + ") VALUES ('" + foodName
                                + "');");
                    }
                    editFoodName.setText("");

                    Toast.makeText(getApplicationContext(), "Finish!!!", Toast.LENGTH_SHORT).show();
                        }
                }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (imageReturnedIntent != null) {
            if (resultCode == RESULT_OK && requestCode == CAMERA_ACTIVITY_REQ) {
                Bitmap photo;
                photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                ImageView img = (ImageView) findViewById(R.id.imageView);
                img.setImageBitmap(photo);
            } else if (resultCode == RESULT_OK && requestCode == GALLERY_REQ) {
                Uri uripath = imageReturnedIntent.getData();
                String[] arrFilePath = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(uripath,arrFilePath,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(arrFilePath[0]);
                String strPath = cursor.getString(columnIndex);
                cursor.close();
                ImageView img = (ImageView) findViewById(R.id.imageView);
                img.setImageBitmap(BitmapFactory.decodeFile(strPath));

                //  Toast.makeText(AddNew_Item.this,strPath.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
