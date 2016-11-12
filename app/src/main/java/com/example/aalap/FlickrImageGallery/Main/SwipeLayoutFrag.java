package com.example.aalap.FlickrImageGallery.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.aalap.FlickrImageGallery.R;

/**
 * Created by Aalap on 2016-09-09.
 */
public class SwipeLayoutFrag extends Fragment {

    static int index;
    static Context context;
    static String largeURL;

    public static Fragment newInstance(Context cont, int position, String url){
        SwipeLayoutFrag swipeLayoutFrag = new SwipeLayoutFrag();
        index = position;
        context = cont;
        largeURL = url;
        return swipeLayoutFrag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.swipe_layout, container, false);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
