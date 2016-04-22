package com.example.android.blocodenotas.models;

import android.content.Context;
import android.database.Cursor;

import com.example.android.blocodenotas.data.RelManager;
import com.example.android.blocodenotas.data.TagManager;
import com.example.android.blocodenotas.utility.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Stack;


/**
 * Created by Adauto on 25/03/2016.
 */
public class Note {
    private Long id;
    private String title;
    private String content;
    private Stack<Tag> tags;
    private Calendar dateCreated;
    private Calendar dateModified;

    public String getReadableModifiedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy - HH:mm ", Locale.getDefault());
        sdf.setTimeZone(getDataModified().getTimeZone());
        Date modifiedDate = getDataModified().getTime();
        String displayDate = sdf.format(modifiedDate);
        return displayDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteTags(Context context){
        List<Tag> mTags = TagManager.newInstance(context).getAllTags();
        List<Rel> mRels = RelManager.newInstance(context).getAllRels();
        List<Long> tagsList = new ArrayList<>();
        StringBuilder s = new StringBuilder("");
        for (int i=0; i<mRels.size(); i++) {
            if(mRels.get(i).getNoteId()==this.id){
                System.out.println("note_id:"+this.id+"// adding to tag list:"+mRels.get(i).getTagId());
                tagsList.add(mRels.get(i).getTagId());
                //mTagsText.append(mTags());
            }
        }
        for (int j=0; j<tagsList.size(); j++) {

            for (int i = 0; i < mTags.size(); i++) {

                if(tagsList.get(j)==mTags.get(i).getId()){
                    System.out.println("j:"+j+"// i:"+i);
                    System.out.println("mTags.get(i).getTag()"+mTags.get(i).getTag());
                    System.out.println("tagsList.get(j)"+tagsList.get(j));
                    s.append(mTags.get(i).getTag());
                    s.append("; ");
                }
            }
        }
        return s.toString();
    }

    public List<Tag> getTags(Context context){
        List<Tag> mTags = TagManager.newInstance(context).getAllTags();
        List<Rel> mRels = RelManager.newInstance(context).getAllRels();
        List<Tag> tagsList = new ArrayList<Tag>();
        Long tag_id;
        for (int i=0; i<mRels.size(); i++) {
            if(mRels.get(i).getNoteId()==this.id){
                tag_id = mRels.get(i).getTagId();
                for (int j=0; j< mTags.size(); j++){
                    if (mTags.get(j).getId()==tag_id){
                        tagsList.add(mTags.get(j));
                    }
                }
            }
        }
        return tagsList;
    }

    public List<Long> getTagsIds(Context context){
        List<Tag> mTags = TagManager.newInstance(context).getAllTags();
        List<Rel> mRels = RelManager.newInstance(context).getAllRels();
        List<Long> tagsList = new ArrayList<>();
        Long tag_id;
        for (int i=0; i<mRels.size(); i++) {
            if(mRels.get(i).getNoteId()==this.id){
                tag_id = mRels.get(i).getTagId();
                tagsList.add(tag_id);
            }
        }
        return tagsList;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Calendar getDataModified() {
        return dateModified;
    }

    public void setDataModified(Calendar dataModified) {
        this.dateModified = dataModified;
    }

    public static Note getNotefromCursor(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TITLE)));
        note.setContent(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CONTENT)));

        //get Calendar instance
        Calendar calendar = GregorianCalendar.getInstance();

        //set the calendar time to date created
        calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_CREATED_TIME)));
        note.setDateCreated(calendar);

        //set the calendar time to date modified
        calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_MODIFIED_TIME)));
        note.setDataModified(calendar);
        return note;
    }

}