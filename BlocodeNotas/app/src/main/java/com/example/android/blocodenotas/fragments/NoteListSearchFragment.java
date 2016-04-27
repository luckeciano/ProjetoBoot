package com.example.android.blocodenotas.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.blocodenotas.R;
import com.example.android.blocodenotas.activities.NoteEditorActivity;
import com.example.android.blocodenotas.adapters.NoteListAdapter;
import com.example.android.blocodenotas.data.NoteManager;
import com.example.android.blocodenotas.data.RelManager;
import com.example.android.blocodenotas.data.TagManager;
import com.example.android.blocodenotas.models.Note;
import com.example.android.blocodenotas.models.Rel;
import com.example.android.blocodenotas.models.Tag;

import java.util.List;

/**
 * Created by Adauto on 19/04/2016.
 */
public class NoteListSearchFragment extends Fragment {

    String query;

    private View mRootView;
    private List<Note> mNotes;
    private List<Tag> mTags;
    private List<Rel> mRels;
    private RecyclerView mRecyclerView;
    private NoteListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public void setQuery (String querySearch) {
        query = querySearch;
        // Required empty public constructor
    }

    public NoteListSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and hold the reference
        //in mRootView
        mRootView = inflater.inflate(R.layout.fragment_search_results, container, false);

        setupList();
        return mRootView;
    }

    private void setupList() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.note_search_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final GestureDetector mGestureDetector =
                new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildLayoutPosition(child);
                    Note selectedNote = mNotes.get(position);
                    Intent editorIntent = new Intent(getActivity(), NoteEditorActivity.class);
                    editorIntent.putExtra("id", selectedNote.getId());
                    startActivity(editorIntent);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        //ZZZZZZZ - AQUI!!!
        mNotes = NoteManager.newInstance(getActivity()).getAllNotesByTag(query,NoteListFragment.typeSort);
        mTags = TagManager.newInstance(getActivity()).getAllTags();
        mRels = RelManager.newInstance(getActivity()).getAllRels();
        mAdapter = new NoteListAdapter(mNotes, mTags, mRels, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

}
