package com.earthquake.se.timetodrop;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private Button CreateButton ;
    private Button btn1;
    private Button viewDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bar_color)));
        
        initialWidget();
        btn1.setOnClickListener(this);
        CreateButton.setOnClickListener(this);
        viewDB.setOnClickListener(this);



    }

    private void initialWidget() {
        btn1 = (Button) findViewById(R.id.button1);
        CreateButton = (Button) findViewById(R.id.NewItem);
        viewDB =  (Button) findViewById(R.id.viewbtn);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClick(View v){


        switch (v.getId()) {
            case R.id.button1:
                Toast.makeText(MainActivity.this,"Hello World",Toast.LENGTH_LONG).show();
                break;
            case R.id.NewItem:
                Intent i = new Intent(getApplicationContext(), Add_Item.class);
                startActivity(i);
                break;
            case R.id.viewbtn:
                Intent j = new Intent(getApplicationContext(), ViewDB.class);
                startActivity(j);
                break;
        }





    }
}
