package com.example.fortest.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.fortest.DisplayParameters;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME ="valueDb";
    public static final String TABLE_NAME ="parameters";

    public static final String KEY_ID ="_id";
    public static final String NAME ="name";
    public static final String VALUE ="value";

    public static final String AUTOHUDNAME = "autoHud";
    public static final String ANALOGNAME = "analog";
    public static final String MLNAME = "ml";
    public static final String THEMECOLORNAME = "theme";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("
                + KEY_ID + " integer primary key, "
                + NAME + " text, "
                + VALUE + " integer)");

        ContentValues newValues = new ContentValues();
        newValues.put(NAME, AUTOHUDNAME);
        newValues.put(VALUE, DisplayParameters.autoHud ? 1: 0);
        db.insert(TABLE_NAME, null, newValues);

        newValues.clear();
        newValues.put(NAME, ANALOGNAME);
        newValues.put(VALUE, DisplayParameters.displayAnalog ? 1: 0);
        db.insert(TABLE_NAME, null, newValues);

        newValues.clear();
        newValues.put(NAME, MLNAME);
        newValues.put(VALUE, DisplayParameters.displayMiles ? 1: 0);
        db.insert(TABLE_NAME, null, newValues);

        newValues.clear();
        newValues.put(NAME, THEMECOLORNAME);
        newValues.put(VALUE, 1);
        db.insert(TABLE_NAME, null, newValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

}
