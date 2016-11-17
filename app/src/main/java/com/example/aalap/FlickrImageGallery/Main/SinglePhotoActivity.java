package com.example.aalap.FlickrImageGallery.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aalap.FlickrImageGallery.R;
import com.example.aalap.FlickrImageGallery.Utils.PhotoData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import uk.co.senab.photoview.PhotoViewAttacher;

public class SinglePhotoActivity extends FragmentActivity {

    public static final int REFRESH_ADAPTER = 5;
    ViewPager viewPager;
    ArrayList<String> largePicURLs;
    int index;
    String currentPicURL;
    ArrayList<SwipeLayoutFrag> frags;
    public Context context;
    MyPagerAdapter myPagerAdapter;
    ImageView delete;
    ArrayList<PhotoData> deletedPhotoObjects;
    boolean hasDeleted = false;
    public static final String TAG = SinglePhotoActivity.class.getSimpleName();
    PhotoData[] receivedPhotoParcelable;
    ArrayList<PhotoData> a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        largePicURLs = getIntent().getStringArrayListExtra("largePicUrls");
        index = getIntent().getIntExtra("pos", 0);
        currentPicURL = getIntent().getStringExtra("url");
        frags = new ArrayList<SwipeLayoutFrag>();
        deletedPhotoObjects = new ArrayList<>();

        for (int i = 0; i < largePicURLs.size(); i++) {
            frags.add((SwipeLayoutFrag) SwipeLayoutFrag.newInstance(getApplicationContext(), i, largePicURLs.get(i)));
        }


        a = getIntent().getParcelableArrayListExtra("photoArrayList");


        myPagerAdapter = new MyPagerAdapter(context, largePicURLs);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setCurrentItem(index, true);
        myPagerAdapter.notifyDataSetChanged();
    }

    public interface OnDeleteListener{
        void onItemDeleted(String largeImageUrl);
    }

    public class MyPagerAdapter extends PagerAdapter{

        private ArrayList<String> pictureURLs;
        private Context context;
        private LayoutInflater layoutInflater;
        public ImageView largeImage;
        TextView indexText, total;
        ImageView back;
        PhotoViewAttacher getmPhotoViewAttacher;
        OnDeleteListener onDeleteListener = new OnDeleteListener() {
            @Override
            public void onItemDeleted(String largeImageUrl) {
                Log.d(TAG, "onItemDeleted: "+largeImageUrl);
            }
        };


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
            delete = (ImageView) itemView.findViewById(R.id.delete);
           // Parcelable[] parcelable = getIntent().getParcelableArrayExtra("parc");
            //receivedPhotoParcelable = Arrays.copyOf(parcelable, parcelable.length, PhotoData[].class);


            back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(hasDeleted){
                        Intent toUpdateList = new Intent(v.getContext(), ListOfPhotos.class);
                        toUpdateList.putExtra("largePicURLs", pictureURLs);
                        toUpdateList.putExtra("deletedPhotoObjects", true);
                        toUpdateList.putExtra("from", "single");
                        toUpdateList.putExtra("deletedPicsArray", deletedPhotoObjects);
                        toUpdateList.putExtra("tag", getIntent().getStringExtra("tag"));
                        toUpdateList.putExtra("testA", a);
                        startActivity(toUpdateList);
                        //startActivityForResult(toUpdateList, REFRESH_ADAPTER);
                    }

                    else
                        finish();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 removePicture(position);
                }
            });

            Picasso.with(getApplicationContext()).load(pictureURLs.get(position)).into(largeImage);
//            getmPhotoViewAttacher = new PhotoViewAttacher(largeImage); //pinch zoom library
            indexText.setText((position+1)+"");
            total.setText(frags.size()+"");
            container.addView(itemView);

            return itemView;

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout)object);
        }

        public void removePicture(int position){

            hasDeleted = true;

            onDeleteListener.onItemDeleted(pictureURLs.get(position)); //test listener.
            a.remove(a.get(position));

            frags.remove(frags.get(position));
            pictureURLs.remove(pictureURLs.get(position));
            myPagerAdapter.notifyDataSetChanged();
            total.setText(pictureURLs.size()+"");
            viewPager.setCurrentItem(position);
        }




    }
}
