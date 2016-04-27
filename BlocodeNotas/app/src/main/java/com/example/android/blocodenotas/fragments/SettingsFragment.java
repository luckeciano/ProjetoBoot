package com.example.android.blocodenotas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.blocodenotas.R;

/**
 * Created by Adauto on 28/03/2016.
 */


public class SettingsFragment extends Fragment implements View.OnClickListener {

    private View mRootView;
    Button buttonOrderByTitle;
    Button buttonOrderByCreateTime;
    Button buttonOrderByModificationTime;

    private void makeToast(String mensagem){
        Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
    }


    public static SettingsFragment newInstance(long id) {
        SettingsFragment fragment = new SettingsFragment();
        if (id > 0) {
            Bundle bundle = new Bundle();
            bundle.putLong("id", id);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_settings,container,false);
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        buttonOrderByTitle = (Button) v.findViewById(R.id.button_order_title);
        buttonOrderByTitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                NoteListFragment.typeSort = 0;
                makeToast("Ordenado por título");
            }
        });

        buttonOrderByCreateTime = (Button) v.findViewById(R.id.button_order_creation_time);
        buttonOrderByCreateTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                NoteListFragment.typeSort = 1;
                makeToast("Ordenado por data de criação");
            }
        });

        buttonOrderByModificationTime = (Button) v.findViewById(R.id.button_order_mod_time);
        buttonOrderByModificationTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                NoteListFragment.typeSort = 2;
                makeToast("Ordenado por data de modificação");
            }
        });
        return v;
    }
    public void onClick(final View v) {
    }
}
