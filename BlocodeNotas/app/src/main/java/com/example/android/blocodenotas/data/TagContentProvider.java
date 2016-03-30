package com.example.android.blocodenotas.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.android.blocodenotas.utility.Constants;
import com.example.android.blocodenotas.utility.Constag;

import java.util.Arrays;
import java.util.HashSet;
/**
 * Created by Murilo on 30-Mar-16.
 */
public class TagContentProvider extends ContentProvider  {
        private DatabaseHelper dbHelper;

        private static final String BASE_PATH_NOTE = "tags";
        private static final int NOTE = 100;
        private static final int NOTES = 101;
        private static final String AUTHORITY = "com.example.android.blocodenotas.data.provider";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY +"/" + BASE_PATH_NOTE);

        private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        static {
            URI_MATCHER.addURI(AUTHORITY, BASE_PATH_NOTE,NOTES);
            URI_MATCHER.addURI(AUTHORITY,BASE_PATH_NOTE + "/#",NOTE );
        }

        @Override
        public boolean onCreate(){
            dbHelper = new DatabaseHelper(getContext());
            return false;
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String [] selectionArgs, String sortOrder ){
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(Constag.TAG_TABLE);
            checkColumns(projection);

            int type = URI_MATCHER.match(uri);
            switch (type){
                case NOTE:
                    break;
                case NOTES:
                    queryBuilder.appendWhere(Constants.COLUMN_ID + "=" + projection[0]);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor =  queryBuilder.query(db,projection,selection,selectionArgs,null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
            return cursor;
        }

        @Override
        public String getType( Uri uri){
            return null;
        }

        @Override
        public Uri insert(Uri uri, ContentValues values){
            int type = URI_MATCHER.match(uri);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Long id;
            switch (type){
                case NOTES:
                    id = db.insert(Constants.NOTES_TABLE, null, values);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.parse(BASE_PATH_NOTE + "/" + id);
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs){
            int type = URI_MATCHER.match(uri);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int affectedRows;
            switch (type) {
                case NOTES:
                    affectedRows = db.delete(Constants.NOTES_TABLE, selection, selectionArgs);
                    break;

                case NOTE:
                    String id = uri.getLastPathSegment();
                    if (TextUtils.isEmpty(selection)) {
                        affectedRows = db.delete(Constants.NOTES_TABLE, Constants.COLUMN_ID + "=" + id, null);
                    } else {
                        affectedRows = db.delete(Constants.NOTES_TABLE, Constants.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
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
            switch (type) {
                case NOTES:
                    affectedRows = db.update(Constants.NOTES_TABLE, values, selection, selectionArgs);
                    break;

                case NOTE:
                    String id = uri.getLastPathSegment();
                    if (TextUtils.isEmpty(selection)) {
                        affectedRows = db.update(Constants.NOTES_TABLE, values, Constants.COLUMN_ID + "=" + id, null);
                    } else {
                        affectedRows = db.update(Constants.NOTES_TABLE, values, Constants.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
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