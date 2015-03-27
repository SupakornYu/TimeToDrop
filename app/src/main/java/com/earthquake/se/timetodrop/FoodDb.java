package com.earthquake.se.timetodrop;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by imuntol on 24/3/2558.
 */
public class FoodDb extends SQLiteOpenHelper {

        private static final String DB_NAME = "BTS";
        private static final int DB_VERSION = 1;

        public static final String TABLE_NAME1 = "Type";
        public static final String COL_Type_name = "Type_name";

        public static final String TABLE_NAME2 = "Item";
        public static final String COL_Name = "Name";
        public static final String COL_Type_id = "Type_id";
        public static final String COL_Expried_date = "Expried_date";
        public static final String COL_Alarm = "Alarm";
        public static final String COL_Tag_id = "Tag_id" ;

        public static final String TABLE_NAME3 = "Tag";
        public static final String COL_Pic_name = "Pic_name";
        public static final String COL_Position = "Position";


        public FoodDb(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + TABLE_NAME1 +" (Type_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_Type_name + " TEXT);");

            db.execSQL("CREATE TABLE " + TABLE_NAME2 +" (Item_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_Name + " TEXT, " + COL_Type_id + " INTEGER, " + COL_Expried_date + " TEXT, "
                    + COL_Alarm + "TEXT," + COL_Tag_id + " INTEGER DEFAULT NULL, "
                    + "FOREIGN KEY(" + COL_Type_id + ") REFERENCES Type(Type_id),"
                    + "FOREIGN KEY(" + COL_Tag_id + ") REFERENCES Tag(Tag_id));");

           db.execSQL("CREATE TABLE " + TABLE_NAME3 +" (Tag_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_Pic_name + " TEXT," + COL_Position + " TEXT );");


            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Type_name +") VALUES ('Food');");
            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Type_name +") VALUES ('Medicine');");
            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Type_name +") VALUES ('Snack');");
            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Type_name +") VALUES ('Milk');");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
            onCreate(db);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
            onCreate(db);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
            onCreate(db);
        }




    }

