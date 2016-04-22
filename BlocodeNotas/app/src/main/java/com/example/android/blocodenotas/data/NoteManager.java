package com.example.android.blocodenotas.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.blocodenotas.models.Note;
import com.example.android.blocodenotas.utility.Constants;
import com.example.android.blocodenotas.utility.Constrel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adauto on 27/03/2016.
 */
public class NoteManager {
    private Context mContext;
    private static NoteManager sNoteManagerInstance = null;

    public static NoteManager newInstance(Context context) {
        if (sNoteManagerInstance == null) {
            sNoteManagerInstance = new NoteManager(context.getApplicationContext());
        }

        return sNoteManagerInstance;
    }

    private NoteManager(Context context){
        this.mContext = context.getApplicationContext();
    }

    public long create(Note note){
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE,note.getTitle());
        values.put(Constants.COLUMN_CONTENT, note.getContent());
        //values.put(Constants.COLUMN_TAGS,note.getTags());
        values.put(Constants.COLUMN_CREATED_TIME,System.currentTimeMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME,System.currentTimeMillis());
        Uri result = mContext.getContentResolver().insert(NoteContentProvider.CONTENT_URI_NOTE,values);
        long id = Long.parseLong(result.getLastPathSegment());
        return id;
    }

    public List<Note> getAllNotes(int typeSort){
        List<Note> notes = new ArrayList<Note>();
        Cursor cursor;
        if (typeSort == 1) {
            cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI_NOTE, Constants.COLUMNS, null, null, Constants.COLUMN_CREATED_TIME);
        }
        else if (typeSort == 2){
            cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI_NOTE, Constants.COLUMNS, null, null, Constants.COLUMN_MODIFIED_TIME);
        }
        else{ cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI_NOTE, Constants.COLUMNS, null, null, Constants.COLUMN_TITLE);}
        if(cursor != null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                notes.add((Note.getNotefromCursor(cursor)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return notes;
    }

    public List<Note> getAllNotesByTag(String tag, int typeSort) {
        List<Note> notes = new ArrayList<Note>();
        Long tag_id = TagManager.newInstance(mContext).exist(tag);
        List<Long> note_ids = RelManager.newInstance(mContext).getNotesFromTag(tag_id);
        String order;
        if (typeSort == 1) {order = Constants.COLUMN_CREATED_TIME;}
        else if (typeSort == 2){order = Constants.COLUMN_MODIFIED_TIME;}
        else{ order = Constants.COLUMN_TITLE;}


        for (int i = 0; i < note_ids.size(); i++) {
            Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI_NOTE, Constants.COLUMNS,
                    Constants.COLUMN_ID + "='" + note_ids.get(i) + "'",
                    null, order);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    notes.add((Note.getNotefromCursor(cursor)));
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        return notes;
    }
    /*
    //Adicionar aqui dentro a busca pela query
    public List<Note> getAllNotes(String querySearch,int typeSort){
        List<Note> note = new ArrayList<Note>();
        return note;
    }*/

    public Note getNote(Long id){
        Note note;
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI_NOTE,
                Constants.COLUMNS, Constants.COLUMN_ID + "=" +id,null,null,null);
        if(cursor != null){
            cursor.moveToFirst();
            note = Note.getNotefromCursor(cursor);
            return note;
        }
        return null;
    }

    public void update(Note note){
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_TITLE,note.getTitle());
        values.put(Constants.COLUMN_CONTENT, note.getContent());
        //values.put(Constants.COLUMN_TAGS,note.getTags());
        values.put(Constants.COLUMN_CREATED_TIME,note.getDateCreated().getTimeInMillis());
        values.put(Constants.COLUMN_MODIFIED_TIME,System.currentTimeMillis());
        mContext.getContentResolver().update(NoteContentProvider.CONTENT_URI_NOTE,
                values, Constants.COLUMN_ID + "=" + note.getId(), null);
    }

    public void delete(Note note){
        mContext.getContentResolver().delete(NoteContentProvider.CONTENT_URI_NOTE, Constants.COLUMN_ID + "=" + note.getId(), null);
    }

    public void generate_PDF(){


    }


}
