package com.example.aalap.FlickrImageGallery.Main;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListOfPhotos extends AppCompatActivity {

    RecyclerView tableRecycler;
    TableAdapter tableAdapter;
    String tag;

    //setting api link
    private static final String FLICKR_BASE_URL = "https://api.flickr.com/services/rest/?method=";
    private static final String FLICKR_PHOTOS_SEARCH = "flickr.photos.search";
    private static final String MY_APIKEY = "&api_key=0c47b6819071c44d57c284bdb180e53d";
    private static final String TAGS = "&text=";
    private static final String CONTENT_TYPE = "&content_type=6";
    private static final String MEDIA_TYPE = "&media=photos";
    private static final String EXTRAS = "&extras=owner_name%2C+url_sq%2C+url_t%2C+url_s%2C+url_q%2C+url_m%2C+url_n%2C+url_z%2C+url_c%2C+url_l%2C+url_o";
    private static final String PER_PAGE = "&per_page=20&page=1";
    private static final String FORMAT = "&format=json";
    private static final String NOJSONCALLBACK = "&nojsoncallback=1";

    private static String TAG = ListOfPhotos.class.getSimpleName();
    String mainAPI, from;
    JSONObject mainJsonObject;
    JSONArray photoJsonArray;
    PhotoData oneRow;
    ArrayList<PhotoData> listOfRows;
    ArrayList<String> largePicUrls;
    TextView tagTitle;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
            from = getIntent().getStringExtra("from");
        }
        listOfRows = new ArrayList<PhotoData>();
        largePicUrls = new ArrayList<String>();
        tagTitle = (TextView) findViewById(R.id.tagTitle);
        back = (ImageView) findViewById(R.id.back_to_home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tagTitle.setText(tag);
        tableRecycler = (RecyclerView) findViewById(R.id.table_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tableRecycler.setLayoutManager(linearLayoutManager);

        mainAPI = FLICKR_BASE_URL + FLICKR_PHOTOS_SEARCH + MY_APIKEY + TAGS + tag + CONTENT_TYPE + MEDIA_TYPE + EXTRAS + PER_PAGE + FORMAT +
                    NOJSONCALLBACK;

            OkHttpClient httpClient = new OkHttpClient();
            Request request = new Request.Builder().url(mainAPI).build();
            Call call = httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "sorry no network", Toast.LENGTH_LONG).show();
                        }
                    });

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {
                        mainJsonObject = new JSONObject(response.body().string());

                        JSONObject photoJsonObject = mainJsonObject.getJSONObject("photos");
                        photoJsonArray = photoJsonObject.getJSONArray("photo");

                        if (photoJsonArray.length() > 0) {
                            for (int i = 0; i < photoJsonArray.length(); i++) {

                                oneRow = new PhotoData();
                                oneRow.setCaption(photoJsonArray.getJSONObject(i).getString("title"));
                                oneRow.setOwnerName(photoJsonArray.getJSONObject(i).getString("ownername"));
                                oneRow.setThumbnail(photoJsonArray.getJSONObject(i).getString("url_sq"));
                                try {
                                    oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_o"));
                                    int width = Integer.parseInt(String.valueOf(photoJsonArray.getJSONObject(i).getString("width_o")));
                                    int height = Integer.parseInt(String.valueOf(photoJsonArray.getJSONObject(i).getString("height_o")));
                                    if (width > 4096 || height > 4096) {
                                        oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_l"));
                                    }
                                    //extract image resource from api. Response is inconsistent so taken care of all the scenarios
                                    //to get the best available image resource url.
                                } catch (JSONException j) {
                                    try {
                                        oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_l"));
                                    } catch (JSONException e) {
                                        try {
                                            oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_c"));
                                        } catch (JSONException a) {
                                            try {
                                                oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_z"));
                                            } catch (JSONException b) {
                                                try {
                                                    oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_n"));
                                                } catch (JSONException c) {
                                                    try {
                                                        oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_m"));
                                                    } catch (JSONException d) {
                                                        try {
                                                            oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_q"));
                                                        } catch (JSONException f) {
                                                            oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_s"));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }


                                listOfRows.add(oneRow);
                                largePicUrls.add(oneRow.getLargeImage());
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tableAdapter = new TableAdapter(listOfRows);
                                    tableRecycler.setAdapter(tableAdapter);
                                    tableAdapter.notifyDataSetChanged();

                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Photos not found", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class TableAdapter extends RecyclerView.Adapter<TableAdapter.RowHolder>{

        ArrayList<PhotoData> listOfPhotos;
        public TableAdapter(ArrayList<PhotoData> listOfPhotos){
            this.listOfPhotos = listOfPhotos;
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
            rowCaption = listOfPhotos.get(position).getCaption();
            rowOwner = listOfPhotos.get(position).getOwnerName();
            largeURL = listOfPhotos.get(position).getLargeImage();
            thumbURL = listOfPhotos.get(position).getThumbnail();

            holder.caption.setText(rowCaption);
            holder.userName.setText(rowOwner);
            Picasso.with(getApplicationContext()).load(thumbURL).into(holder.thumbNail);

            holder.oneRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(MainActivity.isNetworkAvailable(v.getContext())) {
                        Intent intent = new Intent(getApplicationContext(), SinglePhotoActivity.class);
                        intent.putExtra("pos", position);
                        intent.putExtra("url", largeURL);
                        intent.putExtra("size", listOfPhotos.size());
                        Log.d(TAG, "inholder:" + position);
                        intent.putExtra("tag", tag);
                        intent.putStringArrayListExtra("largePicUrls", largePicUrls);
                        startActivity(intent);
                    }

                    else
                        Toast.makeText(ListOfPhotos.this, "Sorry no network", Toast.LENGTH_SHORT).show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return listOfPhotos.size();
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



