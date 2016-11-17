package com.example.aalap.FlickrImageGallery.Main;

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.aalap.FlickrImageGallery.R;
import com.example.aalap.FlickrImageGallery.Utils.PhotoData;

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

public class MainActivity extends AppCompatActivity {

    SearchView searchView;

    //constructing api related fields.
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

    JSONObject mainJsonObject;
    JSONArray photoJsonArray;
    JSONObject photoJsonObject;
    PhotoData oneRow;
    PhotoData[] listOfRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String tag) {
                if(isNetworkAvailable(getApplicationContext())) {

                    String mainAPI = FLICKR_BASE_URL + FLICKR_PHOTOS_SEARCH + MY_APIKEY + TAGS + tag + CONTENT_TYPE + MEDIA_TYPE + EXTRAS + PER_PAGE + FORMAT +
                            NOJSONCALLBACK;
                    OkHttpClient httpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(mainAPI).build();
                    Call call = httpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                //receive parcelable photo array using method.
                                PhotoData[] listOfPhotosParcelable = getPhotoData(response.body().string());
                                Intent intent = new Intent(MainActivity.this, ListOfPhotos.class);
                                intent.putExtra("photodata", listOfPhotosParcelable);
                                intent.putExtra("from", "main");
                                intent.putExtra("tag", tag);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else
                    Toast.makeText(getApplicationContext(), "no network", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private PhotoData[] getPhotoData(String jsondata) throws JSONException {
       //parse JSON response and obtain wach photo object values.
        mainJsonObject = new JSONObject(jsondata);
        photoJsonObject = mainJsonObject.getJSONObject("photos");
        photoJsonArray = photoJsonObject.getJSONArray("photo");
        listOfRows = new PhotoData[photoJsonArray.length()];

        if (photoJsonArray.length() > 0) {
            for (int i = 0; i < photoJsonArray.length(); i++) {
                oneRow = new PhotoData();
                oneRow.setCaption(photoJsonArray.getJSONObject(i).getString("title"));
                oneRow.setOwnerName(photoJsonArray.getJSONObject(i).getString("ownername"));
                oneRow.setSmallImage(photoJsonArray.getJSONObject(i).getString("url_sq"));
                settingLargeImage(i);
                listOfRows[i] = oneRow;
                ArrayList<String> largePicUrls = new ArrayList<>();
                largePicUrls.add(oneRow.getLargeImage());
            }
        }
        return listOfRows;
    }

    private void settingLargeImage(int i) throws JSONException {
        try {
            oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_o"));
            int width = Integer.parseInt(String.valueOf(photoJsonArray.getJSONObject(i).getString("width_o")));
            int height = Integer.parseInt(String.valueOf(photoJsonArray.getJSONObject(i).getString("height_o")));
            if (width > 4096 || height > 4096) {
                oneRow.setLargeImage(photoJsonArray.getJSONObject(i).getString("url_l"));
            }
            //extract image resource from api. Response is inconsistent so taken care of all the scenarios
            //to get the best available largest image resource url.
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
    }

    //checking internet connection status.
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable=true;
        }
        return isAvailable;

    }
}
