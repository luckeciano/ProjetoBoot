package com.example.android.blocodenotas.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.blocodenotas.models.Rel;
import com.example.android.blocodenotas.utility.Constrel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Murilo on 31-Mar-16.
 */
public class RelManager {

    private Context mContext;
    private static RelManager sRelManagerInstance = null;

    public static RelManager newInstance(Context context) {
        if (sRelManagerInstance == null) {
            sRelManagerInstance = new RelManager(context.getApplicationContext());
        }

        return sRelManagerInstance;
    }

    private RelManager(Context context){
        this.mContext = context.getApplicationContext();
    }

    public long create(long note_id, long tag_id){
        ContentValues values = new ContentValues();
        values.put(Constrel.COLUMN_NOTE,note_id);
        values.put(Constrel.COLUMN_TAG,tag_id);
        Uri result = mContext.getContentResolver().insert(NoteContentProvider.CONTENT_URI_REL, values);
        return Long.parseLong(result.getLastPathSegment());
    }

    public List<Rel> getAllRels(){
        List<Rel> rels = new ArrayList<Rel>();
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI_REL, Constrel.COLUMNS, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                rels.add(Rel.getRelFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return rels;
    }

    public List<Long> getNotesFromTag(Long tag_id) {
        List<Rel> rels = new ArrayList<Rel>();
        List<Long> note_ids = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI_REL, Constrel.COLUMNS,
                Constrel.COLUMN_TAG + "='" + tag_id + "'",
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                rels.add(Rel.getRelFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        for (int i = 0; i < rels.size(); i++) {
            note_ids.add(rels.get(i).getNoteId());
        }
        return note_ids;
    }

    public List<Rel> getNoteRels(long note_id){
        List<Rel> mCurrentNoteRels, rels = new ArrayList<Rel>();
        mCurrentNoteRels = getAllRels();
        for (int i=0; i<mCurrentNoteRels.size(); i++) {
            if (mCurrentNoteRels.get(i).getNoteId()==note_id){
                rels.add(mCurrentNoteRels.get(i));
            }
        }
        return rels;
    }

    public void addRels(List<Rel> tags){
        for (int i=0; i<tags.size(); i++) {
            create(tags.get(i).getNoteId(),tags.get(i).getTagId());
        }
    }

    public void delete(Long note_id, Long tag_id){
        //Long del_id = exist(note_id,tag_id);
        mContext.getContentResolver().delete(   NoteContentProvider.CONTENT_URI_REL,
                                                Constrel.COLUMN_NOTE + "=" + note_id + " AND " + Constrel.COLUMN_TAG + "=" + tag_id,
                                                null);
    }

}
