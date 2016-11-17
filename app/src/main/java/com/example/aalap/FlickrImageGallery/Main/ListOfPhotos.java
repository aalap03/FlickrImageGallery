package com.example.aalap.FlickrImageGallery.Main;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aalap.FlickrImageGallery.R;
import com.example.aalap.FlickrImageGallery.Utils.PhotoData;
import com.squareup.picasso.Picasso;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListOfPhotos extends AppCompatActivity{

    RecyclerView tableRecycler;
    TableAdapter tableAdapter;
    String tag;

    private static String TAG = ListOfPhotos.class.getSimpleName();
    ArrayList<String> largePicUrls;
    TextView headerTitle;
    ImageView back;
    boolean isDeleted = false;
    PhotoData[] arrayOfPhotoObject;
    Parcelable[] parcelablePhotosArray;
    ArrayList<PhotoData> photoArrayList;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        headerTitle = (TextView) findViewById(R.id.tagTitle);
        back = (ImageView) findViewById(R.id.back_to_home);
        tableRecycler = (RecyclerView) findViewById(R.id.table_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tableRecycler.setLayoutManager(linearLayoutManager);
        largePicUrls = new ArrayList<String>();

        if (getIntent() != null) {

            from = getIntent().getStringExtra("from");

            if(from!=null) {

                if(from.equalsIgnoreCase("main")) {

                    //received parcelable data.
                    parcelablePhotosArray = getIntent().getParcelableArrayExtra("photodata");
                    //converted into array of photodata type.
                    arrayOfPhotoObject = Arrays.copyOf(parcelablePhotosArray, parcelablePhotosArray.length, PhotoData[].class);
                    //converted array to arraylist as we require keeping track of deleted photos.
                    photoArrayList = new ArrayList<>();

                    for(int i = 0; i< arrayOfPhotoObject.length; i++){
                        largePicUrls.add(arrayOfPhotoObject[i].getLargeImage());
                        photoArrayList.add(arrayOfPhotoObject[i]);
                    }
                    //setting adapter with full sized photos received from api.
                    tableAdapter = new TableAdapter(photoArrayList);
                    tableRecycler.setAdapter(tableAdapter);
                    tableAdapter.notifyDataSetChanged();
                }

                else if(from.equalsIgnoreCase("single")){
                    //if it comes from singlephoto activity we can keep track of deleted items as
                    //as per that set the new photoarray to the adapter.
                    photoArrayList = getIntent().getParcelableArrayListExtra("updatedList");
                    for(int i = 0; i< photoArrayList.size(); i++){
                        largePicUrls.add(photoArrayList.get(i).getLargeImage());
                    }
                    //setting adapter with possible updated data without the deleted ones.
                    tableAdapter = new TableAdapter(photoArrayList);
                    tableRecycler.setAdapter(tableAdapter);
                    tableAdapter.notifyDataSetChanged();
                }
            }
            tag = getIntent().getStringExtra("tag");
            headerTitle.setText(tag);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class TableAdapter extends RecyclerView.Adapter<TableAdapter.RowHolder>{

        ArrayList<PhotoData> dataArrayList;

        public TableAdapter(ArrayList<PhotoData> dataArrayList){

            this.dataArrayList = dataArrayList;
        }

        @Override
        public TableAdapter.RowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.table_list_item, parent, false);
            return new RowHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TableAdapter.RowHolder holder, final int position){


            final String rowCaption, rowOwner, largeURL, thumbURL;

            rowCaption = dataArrayList.get(position).getCaption();
            rowOwner = dataArrayList.get(position).getOwnerName();
            largeURL = dataArrayList.get(position).getLargeImage();
            thumbURL = dataArrayList.get(position).getThumbnail();


            holder.caption.setText(rowCaption);
            holder.userName.setText(rowOwner);
            Picasso.with(getApplicationContext()).load(thumbURL).into(holder.thumbNail);

            holder.oneRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d(TAG, "onClick: whatsup");

                    if(MainActivity.isNetworkAvailable(v.getContext())) {
                        Intent intent = new Intent(getApplicationContext(), SinglePhotoActivity.class);
                        //keep track of correct photo and its position.
                        intent.putExtra("pos", position);
                        intent.putExtra("tag", tag);
                        intent.putExtra("photoArrayList", photoArrayList);
                        intent.putExtra("url", largeURL);
                        intent.putStringArrayListExtra("largePicUrls", largePicUrls);

                        startActivity(intent);
                        finish();
                    }

                    else
                        Toast.makeText(ListOfPhotos.this, "Sorry no network", Toast.LENGTH_SHORT).show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return dataArrayList.size();
        }


        class RowHolder extends RecyclerView.ViewHolder{

            ImageView thumbNail;
            TextView caption, userName;
            ViewGroup oneRow;

            public RowHolder(View itemView) {
                super(itemView);
                thumbNail = (ImageView) itemView.findViewById(R.id.thumbnail);
                caption = (TextView) itemView.findViewById(R.id.caption);
                userName = (TextView) itemView.findViewById(R.id.userName);
                oneRow = (ViewGroup) itemView.findViewById(R.id.onerow);
            }
        }

    }
}



