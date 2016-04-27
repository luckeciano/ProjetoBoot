package com.example.android.blocodenotas.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.blocodenotas.utility.Constants;
import com.example.android.blocodenotas.utility.Constag;
import com.example.android.blocodenotas.utility.Constrel;

/**
 * Created by Adauto on 23/03/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "blocodenotas.db";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_NOTE);
        db.execSQL(CREATE_TABLE_REL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion ) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.NOTES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Constag.TAG_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Constrel.REL_TABLE);
        onCreate(db);
    }

    private static final String CREATE_TABLE_TAG = "create table "
            + Constag.TAG_TABLE
            + "("
            + Constag.COLUMN_ID + " integer primary key autoincrement, "
            + Constag.COLUMN_NAME + " text not null " + ")";

    private static final String CREATE_TABLE_REL = "create table "
            + Constrel.REL_TABLE
            + "("
            + Constrel.COLUMN_ID + " integer primary key autoincrement, "
            + Constrel.COLUMN_TAG + " text not null,"
            + Constrel.COLUMN_NOTE + " integer not null" + " )";

    private static final String CREATE_TABLE_NOTE = "create table "
            + Constants.NOTES_TABLE
            + "("
            + Constants.COLUMN_ID + " integer primary key autoincrement, "
            + Constants.COLUMN_TITLE + " text not null, "
            + Constants.COLUMN_CONTENT + " text not null, "
            + Constants.COLUMN_MODIFIED_TIME + " integer not null, "
            + Constants.COLUMN_CREATED_TIME + " integer not null " + ")";
}
