package com.example.android.blocodenotas.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.android.blocodenotas.utility.Constag;
import com.example.android.blocodenotas.utility.Constants;
import com.example.android.blocodenotas.utility.Constrel;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Adauto on 23/03/2016.
 */
public class NoteContentProvider extends ContentProvider {
    private DatabaseHelper dbHelper;

    private static final String BASE_PATH_NOTE = Constants.NOTES_TABLE;
    private static final int NOTE = 100;
    private static final int NOTES = 101;
    private static final String BASE_PATH_TAG = Constag.TAG_TABLE;
    private static final int TAG = 200;
    private static final int TAGS = 201;
    private static final String BASE_PATH_REL = Constrel.REL_TABLE;
    private static final int REL = 300;
    private static final int RELS = 301;
    private static final String AUTHORITY = "com.example.android.blocodenotas.data.provider";
    public static final Uri CONTENT_URI_NOTE = Uri.parse("content://" + AUTHORITY +"/" + BASE_PATH_NOTE);
    public static final Uri CONTENT_URI_TAG = Uri.parse("content://" + AUTHORITY +"/" + BASE_PATH_TAG);
    public static final Uri CONTENT_URI_REL = Uri.parse("content://" + AUTHORITY +"/" + BASE_PATH_REL);

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        static {
            URI_MATCHER.addURI(AUTHORITY, BASE_PATH_NOTE,NOTES);
            URI_MATCHER.addURI(AUTHORITY,BASE_PATH_NOTE + "/#",NOTE );
            URI_MATCHER.addURI(AUTHORITY, BASE_PATH_TAG,TAGS);
            URI_MATCHER.addURI(AUTHORITY,BASE_PATH_TAG + "/#",TAG );
            URI_MATCHER.addURI(AUTHORITY, BASE_PATH_REL,RELS);
            URI_MATCHER.addURI(AUTHORITY,BASE_PATH_REL + "/#",REL );
        }

    private SQLiteQueryBuilder queryBuilder;

    @Override
    public boolean onCreate(){
        dbHelper = new DatabaseHelper(getContext());
        return false;
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String [] selectionArgs, String sortOrder ){
        queryBuilder = new SQLiteQueryBuilder();



        int type = URI_MATCHER.match(uri);
        switch (type){
            case NOTE:
                queryBuilder.setTables(Constants.NOTES_TABLE);
                //checkColumns(projection);
                break;
            case NOTES:
                queryBuilder.setTables(Constants.NOTES_TABLE);
                //checkColumns(projection);
                //queryBuilder.appendWhere(Constants.COLUMN_ID + "=" + projection[0]);
                break;
            case TAG:
                queryBuilder.setTables(Constag.TAG_TABLE);
                //checkColumns(projection);
                break;
            case TAGS:
                queryBuilder.setTables(Constag.TAG_TABLE);
                //checkColumns(projection);
                //queryBuilder.appendWhere(Constag.COLUMN_ID + "=" + projection[0]);
                break;
            case REL:
                queryBuilder.setTables(Constrel.REL_TABLE);
                //checkColumns(projection);
                break;
            case RELS:
                queryBuilder.setTables(Constrel.REL_TABLE);
                //checkColumns(projection);
                //queryBuilder.appendWhere(Constrel.COLUMN_ID + "=" + projection[0]);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor =  queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public String getType( Uri uri){
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        Uri value;
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Long id;
        switch (type){
            case NOTES:
                id = db.insert(Constants.NOTES_TABLE, null, values);
                value = Uri.parse(BASE_PATH_NOTE + "/" + id);
                break;
            case TAGS:
                id = db.insert(Constag.TAG_TABLE, null, values);
                value = Uri.parse(BASE_PATH_TAG + "/" + id);
                break;
            case RELS:
                id = db.insert(Constrel.REL_TABLE, null, values);
                value = Uri.parse(BASE_PATH_REL + "/" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return value;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        String id;
        switch (type) {
            case NOTES:
                affectedRows = db.delete(Constants.NOTES_TABLE, selection, selectionArgs);
                break;

            case NOTE:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.delete(Constants.NOTES_TABLE, Constants.COLUMN_ID + "=" + id, null);
                } else {
                    affectedRows = db.delete(Constants.NOTES_TABLE, Constants.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case TAGS:
                affectedRows = db.delete(Constag.TAG_TABLE, selection, selectionArgs);
                break;

            case TAG:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.delete(Constag.TAG_TABLE, Constag.COLUMN_ID + "=" + id, null);
                } else {
                    affectedRows = db.delete(Constag.TAG_TABLE, Constag.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case RELS:
                affectedRows = db.delete(Constrel.REL_TABLE, selection, selectionArgs);
                break;

            case REL:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.delete(Constrel.REL_TABLE, Constrel.COLUMN_ID + "=" + id, null);
                } else {
                    affectedRows = db.delete(Constrel.REL_TABLE, Constrel.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int type = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        String id;
        switch (type) {
            case NOTES:
                affectedRows = db.update(Constants.NOTES_TABLE, values, selection, selectionArgs);
                break;

            case NOTE:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.update(Constants.NOTES_TABLE, values, Constants.COLUMN_ID + "=" + id, null);
                } else {
                    affectedRows = db.update(Constants.NOTES_TABLE, values, Constants.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            case TAGS:
                affectedRows = db.update(Constag.TAG_TABLE, values, selection, selectionArgs);
                break;

            case TAG:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.update(Constag.TAG_TABLE, values, Constag.COLUMN_ID + "=" + id, null);
                } else {
                    affectedRows = db.update(Constag.TAG_TABLE, values, Constag.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            case RELS:
                affectedRows = db.update(Constrel.REL_TABLE, values, selection, selectionArgs);
                break;

            case REL:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    affectedRows = db.update(Constrel.REL_TABLE, values, Constrel.COLUMN_ID + "=" + id, null);
                } else {
                    affectedRows = db.update(Constrel.REL_TABLE, values, Constrel.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }

    private void checkColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> request = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> available = new HashSet<String>(Arrays.asList(Constants.COLUMNS));
            if (!available.containsAll(request)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }



}
