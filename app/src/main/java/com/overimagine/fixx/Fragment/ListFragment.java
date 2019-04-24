package com.overimagine.fixx.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.overimagine.fixx.R;

import androidx.fragment.app.Fragment;

public class ListFragment extends Fragment {


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int i = 0;
        if (i == 0)
            return inflater.inflate(R.layout.fragment_list, container, false);
        else
            return inflater.inflate(R.layout.fragment_list_nothing, container, false);
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
