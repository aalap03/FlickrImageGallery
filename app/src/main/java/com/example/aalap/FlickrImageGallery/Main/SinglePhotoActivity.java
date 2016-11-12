package com.example.aalap.FlickrImageGallery.Main;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aalap.FlickrImageGallery.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import uk.co.senab.photoview.PhotoViewAttacher;

public class SinglePhotoActivity extends FragmentActivity {

    ViewPager viewPager;
    ArrayList<String> pictures;
    int index;
    String largeURL;
    ArrayList<SwipeLayoutFrag> frags;
    public Context context;
    MyPagerAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        pictures = getIntent().getStringArrayListExtra("largePicUrls");
        index = getIntent().getIntExtra("pos", 0);
        largeURL = getIntent().getStringExtra("url");
        frags = new ArrayList<SwipeLayoutFrag>();


        for (int i = 0; i < pictures.size(); i++) {
            frags.add((SwipeLayoutFrag) SwipeLayoutFrag.newInstance(getApplicationContext(), i, pictures.get(i)));
        }

        myPagerAdapter = new MyPagerAdapter(context, pictures);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setCurrentItem(index, true);
        myPagerAdapter.notifyDataSetChanged();
    }

    public class MyPagerAdapter extends PagerAdapter{

        private ArrayList<String> pictureURLs;
        private Context context;
        private LayoutInflater layoutInflater;
        public ImageView largeImage;
        TextView indexText, total;
        ImageView back;
        PhotoViewAttacher getmPhotoViewAttacher;


        public MyPagerAdapter(Context context, ArrayList<String> pictureURLs){
            this.context = context;
            this.pictureURLs = pictureURLs;
        }

        @Override
        public int getCount() {
            return pictureURLs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view==(LinearLayout)object);
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View itemView = layoutInflater.inflate(R.layout.swipe_layout, container, false);

            largeImage = (ImageView) itemView.findViewById(R.id.large_photo);
            indexText = (TextView) itemView.findViewById(R.id.index);
            total = (TextView) itemView.findViewById(R.id.size);
            back = (ImageView) itemView.findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            Picasso.with(getApplicationContext()).load(pictureURLs.get(position)).into(largeImage);
            getmPhotoViewAttacher = new PhotoViewAttacher(largeImage);
            indexText.setText((position+1)+"");
            total.setText(frags.size()+"");
            container.addView(itemView);

            return itemView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout)object);
        }
    }

}
