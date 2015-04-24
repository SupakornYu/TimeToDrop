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

        public static final String TABLE_NAME1 = "Colour_tag";
        public static final String COL_Group_id = "Colour_tag_id";
        public static final String COL_Colour = "Colour_name";
        public static final String COL_Colour_code = "Colour_code";
        public static final String COL_Group_Detail = "Colour_tag_detail";

        public static final String TABLE_NAME2 = "Item";
        public static final String COL_Item_id = "Item_id";
        public static final String COL_Expire_date = "Expire_date";
        public static final String COL_Warn_days = "Warn_days";
        public static final String COL_G_id = "Group_id";
        public static final String COL_Item_Detail = "Item_detail";
        public static final String COL_P_id = "Photo_id";


        public static final String TABLE_NAME3 = "Photo_tag";
        public static final String COLP_Photo_tag_id = "Photo_tag_id";
        public static final String COL_Position = "Position";
        public static final String COL_Path = "Path";


        public FoodDb(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + TABLE_NAME1 +" (" + COL_Group_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_Colour + " TEXT , " + COL_Colour_code + " TEXT , " + COL_Group_Detail + " TEXT DEFAULT NULL );");

            db.execSQL("CREATE TABLE " + TABLE_NAME2 +" (" + COL_Item_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_Expire_date + " TEXT, " + COL_Warn_days + " INTEGER, " + COL_G_id + " INTEGER, "
                    + COL_Item_Detail + " TEXT, " + COL_P_id + " INTEGER DEFAULT NULL, "
                    + "FOREIGN KEY(" + COL_G_id + ") REFERENCES "+ TABLE_NAME2 +"("+ COL_Group_id +"),"
                    + "FOREIGN KEY(" + COL_P_id + ") REFERENCES "+ TABLE_NAME3 +"("+ COLP_Photo_tag_id +"));");

           db.execSQL("CREATE TABLE " + TABLE_NAME3 +" ("+ COLP_Photo_tag_id +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_Position + " TEXT," + COL_Path + " TEXT );");


            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Colour + ", "+ COL_Colour_code + ") VALUES ('sky','#13b7d2');");
            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Colour + ", "+ COL_Colour_code + ") VALUES ('orange','#ff7244');");
            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Colour + ", "+ COL_Colour_code + ") VALUES ('Green','#2cb081');");
            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Colour + ", "+ COL_Colour_code + ") VALUES ('grape','#695b8e');");
            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Colour + ", "+ COL_Colour_code + ") VALUES ('red','#FF0000');");
            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Colour + ", "+ COL_Colour_code + ") VALUES ('blue','#2f72da');");
            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Colour + ", "+ COL_Colour_code + ") VALUES ('yellow','#fabf57');");
            db.execSQL("INSERT INTO " + TABLE_NAME1 + " (" + COL_Colour + ", "+ COL_Colour_code + ") VALUES ('grey','#e0e0e0');");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
            onCreate(db);
        }




    }

