package com.example.android.blocodenotas.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.blocodenotas.models.Tag;
import com.example.android.blocodenotas.utility.Constag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Murilo on 29-Mar-16.
 */
public class TagManager {

    private Context mContext;
    private static TagManager sTagManagerInstance = null;

    public static TagManager newInstance(Context context) {
        if (sTagManagerInstance == null) {
            sTagManagerInstance = new TagManager(context.getApplicationContext());
        }

        return sTagManagerInstance;
    }

    private TagManager(Context context){
        this.mContext = context.getApplicationContext();
    }

    public long create(String name){
        ContentValues values = new ContentValues();
        values.put(Constag.COLUMN_NAME,name);
        //zzz
        Uri result = mContext.getContentResolver().insert(NoteContentProvider.CONTENT_URI_TAG, values);
        return Long.parseLong(result.getLastPathSegment());
    }
    public Long exist(String name){
        String[] projection = {Constag.COLUMN_ID,Constag.COLUMN_NAME};
        Cursor result = mContext.getContentResolver().query(    NoteContentProvider.CONTENT_URI_TAG,
                                                                projection,
                                                                Constag.COLUMN_NAME + "='" + name + "'",
                                                                null, null);
        result.moveToFirst();
        System.out.println("DEBUG: " + result.getCount());
        System.out.println("DEBUG: " + result.toString());
        if (result.getCount()!=0){
            result.moveToFirst();
            return result.getLong(result.getColumnIndex(Constag.COLUMN_ID));
        }
        return null;
    }


    //AQUIIIIII - Adicionar a busca pela query
    public List<Tag> getAllTags(String querySearch){
        List<Tag> tags = new ArrayList<Tag>();
        return tags;
    }


    public List<Tag> getAllTags(){
        List<Tag> tags = new ArrayList<Tag>();
        //zzz
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI_TAG, Constag.COLUMNS, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                tags.add((Tag.getTagFromCursor(cursor)));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return tags;
    }

    public String getAllTagsString(){
        List<Tag> mCurrentNoteTags;
        StringBuilder s = new StringBuilder("");
        mCurrentNoteTags = getAllTags();
        for (int i=0; i<mCurrentNoteTags.size(); i++) {
            s.append(mCurrentNoteTags.get(i).getTag());
            s.append(";");
        }
        return s.toString();
    }



    public void addTags(List<String> tags, Long note_id){
        List<Long> tagsIdsList = new ArrayList<>();
        List<Long> oldTagsList = NoteManager.newInstance(mContext).getNote(note_id).getTagsIds(mContext);
        for(int i=0; i<tags.size(); i++){
            System.out.println("TagManager - addTags - tags.get(i)" + tags.get(i));
            Long tag_id = exist(tags.get(i));
            if (tag_id==null){
                tag_id = create(tags.get(i));
                System.out.println("TagManager - addTags - tag_id==null");

            }
            tagsIdsList.add(tag_id);
            System.out.println("TagManager - addTags - add(tag_id):"+tag_id);
        }
        List<Long> newTagsList = tagsIdsList;
        for (int i=0; i<tagsIdsList.size(); i++) {
            if (oldTagsList.contains(tagsIdsList.get(i))){
                oldTagsList.remove(tagsIdsList.get(i));
                newTagsList.remove(tagsIdsList.get(i));
            }
        }
        for(int i=0; i<oldTagsList.size(); i++) {
            RelManager.newInstance(mContext).delete(note_id, oldTagsList.get(i));
        }
        for(int i=0; i<newTagsList.size(); i++) {
            System.out.println("TagManager - addTags - Create new Rel:"+newTagsList.get(i));
            RelManager.newInstance(mContext).create(note_id, newTagsList.get(i));
        }
    }


    public Tag getTag(Long id){
        Tag tag;
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI_TAG,
                Constag.COLUMNS, Constag.COLUMN_ID + "=" +id,null,null,null);
        if(cursor != null){
            cursor.moveToFirst();
            tag = Tag.getTagFromCursor(cursor);
            return tag;
        }
        return null;
    }

    public Long getTagId(String name){
        Tag tag;
        Cursor cursor = mContext.getContentResolver().query(NoteContentProvider.CONTENT_URI_TAG,
                Constag.COLUMNS, Constag.COLUMN_NAME + "=" +name,null,null,null);
        if(cursor != null){
            cursor.moveToFirst();
            tag = Tag.getTagFromCursor(cursor);
            return tag.getId();
        }
        else {
            return create(name);
        }
    }

    public void delete(Tag tag){
        mContext.getContentResolver().delete(NoteContentProvider.CONTENT_URI_TAG,Constag.COLUMN_ID + "=" + tag.getId(), null);
    }

}
