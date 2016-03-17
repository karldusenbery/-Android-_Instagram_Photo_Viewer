package app.instagramclient.android.example.com.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";

    //array of photo objects
    private ArrayList<InstagramPhoto> photos;

    //define the adapter
    InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        //initialize ArrayList
        photos = new ArrayList<>();

        //creates the adapter and links it to the data source(photos ArrayList of objects)
        aPhotos = new InstagramPhotosAdapter(this, photos);

        //find the ListView form the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);

        //set the adapter, binding it to the ListView
        lvPhotos.setAdapter(aPhotos);

        //lest step in the onCreate method
        //SEND OUT API REQUEST FOR THE LATEST PHOTOS TAGGED WITH A CERTAIN TAG
        fetchTaggedPhotos();

    }


    //send out the network request to get tagged photos
    /*
    CodePath Client ID: e05c462ebd86446ea48a5af73769b602
	-  Recent Tagged: https://api.instagram.com/v1/tags/{tag-name}/media/recent?access_token=ACCESS-TOKEN
     */
    public void fetchTaggedPhotos() {

        String url = "https://api.instagram.com/v1/tags/pugs/media/recent?client_id=" + CLIENT_ID;

        //creates the network client
        AsyncHttpClient client = new AsyncHttpClient();

        //trigger the GET request
        client.get(url, null, new JsonHttpResponseHandler(){

            //onSuccess (it worked, and it's a "200 success code")

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //do something on successful GET of the the JSON object
                //Expecting a JSON object (not JSON array!)
                //Iterate each of the photo items and decode the item into a Java object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data"); //this is getting the array of posts

                    //iterate the array of posts
                    for (int i = 0; i < photosJSON.length(); i++){

                        //get the JSON object at that position in the array photosJSON
                        JSONObject photoJSON = photosJSON.getJSONObject(i);

                        //decode the attributes of he JSON object into a data model
                        InstagramPhoto photo = new InstagramPhoto(); //empty photo object that will be filled with info

                        //Author Name: { "data" => [X] => "user"  => "username" }
                        photo.username = photoJSON.getJSONObject("user").getString("username");

                        //Caption: { "data" => [X] => "caption" => "text" }
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");

                        //Type: { "data" => [X] => "type" } ("image" or "video")
                        //photo.type = photoJSON.getJSONObject("type").getString("image");

                        //image/video URL: { "data" => [X] => "images"  => "standard_resolution" => "url" }
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");

                        //height
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");

                        //likes count
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");

                        //add each photo object to the ArrayList of photo objects
                        photos.add(photo);

                    }

                } catch (JSONException e ) {
                    e.printStackTrace();
                }

                //callback
                aPhotos.notifyDataSetChanged();
            }


            //onFailure (it did not work)
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //do something on failure to GET the the JSON object
            }
        });

    }
}
