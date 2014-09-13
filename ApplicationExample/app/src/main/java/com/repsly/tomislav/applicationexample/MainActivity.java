package com.repsly.tomislav.applicationexample;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.repsly.tomislav.applicationexample.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.BatchUpdateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {


    private TextView locationName;
    private TextView locationAddress;
    private TextView latitudeLongitude;
    private EditText editName;
    private EditText editAddress;
    private TextView tvIsConnected;
    private Button changeName;
    private Button changeAddress;
    private Button showOnMap;
    private Button takePicture;
    private ExampleDatabaseAdapter exampleHelper;
    private GPSTracker lokator = new GPSTracker(this);
    private String mCurrentPhotoPath;

    private int id = 0;
    private static final int TAKE_PHOTO = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exampleHelper = new ExampleDatabaseAdapter(this);
        id = exampleHelper.getIndex();

        if(id<1){

        exampleHelper.inputData();
            id = exampleHelper.getIndex();
        }

        // get reference to the views
        locationName = (TextView) findViewById(R.id.location_name);
        locationAddress = (TextView) findViewById(R.id.location_address);
        latitudeLongitude = (TextView) findViewById(R.id.location_lat_lng);
        editName = (EditText) findViewById(R.id.edit_name);
        editAddress = (EditText) findViewById(R.id.edit_address);
        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        changeName = (Button) findViewById(R.id.change_name);
        changeAddress = (Button) findViewById(R.id.change_address);
        showOnMap = (Button) findViewById(R.id.show_map);
        takePicture = (Button) findViewById(R.id.take_picture);

        // check if you are connected or not
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are connected");
        }
        else{
            tvIsConnected.setText("You are NOT connected");
        }
        lokator.getLocation();

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute("https://api.foursquare.com/v2/venues/explore?client_id=HDF2DF3TD4L" +
                "LG1JCZJJCGHVXNGWBWS1XW2SKDVHMZW5XBJOP&client_secret=DHEZYQIQSJKCRQPK3RZXJNM2F0PTG5DXVAVDL2OH2PDHCMNA" +
                "&v=20130815&ll=" + lokator.getLatitude() + "," + lokator.getLongitude()+"&radius=10000&limit=1");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void TakePhoto(View v){

        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        if (i.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

                Log.i("news", "bad news");
            }

            if (photoFile != null) {
                i.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(i, TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("image/jpeg");
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse(mCurrentPhotoPath));
            startActivity(Intent.createChooser(i, "Share Image"));
        }
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json=new JSONObject(result);
                json=json.getJSONObject("response");
                JSONArray groups = json.getJSONArray("groups");
                JSONArray items = groups.getJSONObject(0).getJSONArray("items");

                exampleHelper.updateLocationNameById(id,items.getJSONObject(0).getJSONObject("venue").getString("name"));
                exampleHelper.updateAddressById(id,items.getJSONObject(0).getJSONObject("venue").getJSONObject("location").getString("address"));
                exampleHelper.updateLatitudeById(id,items.getJSONObject(0).getJSONObject("venue").getJSONObject("location").getString("lat"));
                exampleHelper.updateLongitudeById(id,items.getJSONObject(0).getJSONObject("venue").getJSONObject("location").getString("lng"));

                locationName.setText(exampleHelper.getLocationName());
                locationAddress.setText(exampleHelper.getAddress());
                latitudeLongitude.setText(exampleHelper.getLatitude()+ "," + exampleHelper.getLongitude());


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void ChangeName(View v){

        exampleHelper.updateLocationNameById(id,editName.getText().toString());
        locationName.setText(exampleHelper.getLocationName());
    }

    public void ChangeAddress(View v){

        exampleHelper.updateAddressById(id,editAddress.getText().toString());
        locationAddress.setText(exampleHelper.getAddress());
    }

    public void OpenMap(View v){

        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q="+ exampleHelper.getLatitude() +","
                + exampleHelper.getLongitude() +"("+ exampleHelper.getLocationName() + ")&iwloc=A&hl=es");
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
