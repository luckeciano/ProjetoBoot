
/**
 * Created by Adauto on 25/03/2016.
 */
package com.example.android.blocodenotas.adapters;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.blocodenotas.R;
import com.example.android.blocodenotas.models.Note;
import com.example.android.blocodenotas.models.Rel;
import com.example.android.blocodenotas.models.Tag;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder>{

    private List<Note> mNotes;

    private String mTagsText;
    private Context mContext;

    //constructor
    public NoteListAdapter(List<Note> notes, List<Tag> tags, List<Rel> rels, Context context){
        mNotes = notes;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Long note_id = mNotes.get(position).getId();

        mTagsText = mNotes.get(position).getNoteTags(mContext);

        holder.noteTitle.setText(mNotes.get(position).getTitle());
        holder.noteCreateDate.setText(mNotes.get(position).getReadableModifiedDate());
        holder.noteContent.setText(mNotes.get(position).getContent());
        holder.noteTags.setText(mTagsText);

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView noteTitle, noteCreateDate, noteContent, noteTags;



        public ViewHolder(View itemView) {
            super(itemView);
            noteTitle = (TextView)itemView.findViewById(R.id.text_view_note_title);
            noteCreateDate = (TextView)itemView.findViewById(R.id.text_view_note_date);
            noteContent = (TextView) itemView.findViewById(R.id.text_view_note_content);
            noteTags = (TextView) itemView.findViewById(R.id.text_view_note_tags);
        }

    }


    public void promptForDelete(final int position){
        String fieldToBeDeleted = mNotes.get(position).getTitle();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Delete " + fieldToBeDeleted + " ?");
        alertDialog.setMessage("Are you sure you want to delete the note " + fieldToBeDeleted + "?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNotes.remove(position);
                notifyItemRemoved(position);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
