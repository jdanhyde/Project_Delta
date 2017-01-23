package com.example.damion.loginscreen;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class gameScreen extends FragmentActivity implements OnConnectionFailedListener, OnMapReadyCallback {

    public static final String TAG = "gameActivity";
    private GoogleApiClient mGoogleApiClient;
    LatLng playerLoc = new LatLng(0,0);

    @Override
    protected void onDestroy(Bundle savedInstanceState) {
        super.onDestroy(savedInstanceState);
        String url = "http://www.redwoodmediaco.com/compsci/userLogin.php?username=" + user;
        System.out.println("URL: " + url);
        new MainActivity.tryLogin().execute(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Permission checks
        setContentView(R.layout.activity_game_screen);

        //set up location
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                playerLoc = new LatLng(location.getLatitude(), location.getLongitude());
                Log.i(TAG, String.valueOf(playerLoc.latitude)+ " , " + String.valueOf(playerLoc.longitude));
                CameraUpdateFactory.newLatLng(playerLoc);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
        //set up map
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .addApi(AppIndex.API).build();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    @Override
    public void onResume(){
        super.onResume();


    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());


    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, String.format("permissions issue"));
            return;
        }
        //set camera view
        CameraUpdateFactory.newLatLng(playerLoc);
        //googleMap.setMinZoomPreference(12.0f);
        //googleMap.setMaxZoomPreference(24.0f);

        //get nearby locations
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {

            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    if(!placeLikelihood.getPlace().getPlaceTypes().contains(1021)){
                    Log.i(TAG, String.format("Place '%s' has likelihood: %g and type" ,
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood(),
                            placeLikelihood.getPlace().getPlaceTypes().toString()));
                                googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(placeLikelihood.getPlace().getLatLng().latitude, placeLikelihood.getPlace().getLatLng().longitude))
                                        .title(placeLikelihood.getPlace().getName().toString() + " " + placeLikelihood.getPlace().getPlaceTypes().toString()));
                    }
                }

                if (likelyPlaces.getCount()<0){
                    Log.e(TAG, String.format("No Places detected"));
                }
            }
        });

    }
}