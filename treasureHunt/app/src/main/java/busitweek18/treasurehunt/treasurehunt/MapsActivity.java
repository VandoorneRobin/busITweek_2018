package busitweek18.treasurehunt.treasurehunt;

import android.content.res.Resources;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.HashMap;
import java.util.Map;

import cz.mendelu.busItWeek.library.BeaconTask;
import cz.mendelu.busItWeek.library.ChoicePuzzle;
import cz.mendelu.busItWeek.library.CodeTask;
import cz.mendelu.busItWeek.library.GPSTask;
import cz.mendelu.busItWeek.library.ImageSelectPuzzle;
import cz.mendelu.busItWeek.library.Puzzle;
import cz.mendelu.busItWeek.library.SimplePuzzle;
import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.Task;
import cz.mendelu.busItWeek.library.beacons.BeaconDefinition;
import cz.mendelu.busItWeek.library.beacons.BeaconUtil;
import cz.mendelu.busItWeek.library.map.MapUtil;
import cz.mendelu.busItWeek.library.qrcode.QRCodeUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, GoogleMap.OnMarkerClickListener {


    private GoogleMap mMap;
    private LatLngBounds.Builder latLngBoundsBuilder;
    //classes from the engine
    private StoryLine storyLine;
    private Task currentTask;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    private BeaconUtil beaconUtil;

    private HashMap<Task, Marker> markers = new HashMap<>();
    private LatLngBounds.Builder latLngBounds;

    private static final String TAG = MapsActivity.class.getSimpleName();

    private ImageButton qrButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        storyLine = StoryLine.open(this, TreasureHuntStoryLineDbHelper.class);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);


       qrButton = findViewById(R.id.qr_code_button);

        beaconUtil = new BeaconUtil(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        initializeTasks();
    }

    private void initializeTasks() {
        latLngBoundsBuilder = new LatLngBounds.Builder();

        for(Task task : storyLine.taskList()) {
            Marker marker = null;
            if(task instanceof GPSTask) {
                // GPS task
            } else if (task instanceof BeaconTask) {
                // Beacon task
                BeaconDefinition definition = new BeaconDefinition((BeaconTask) task) {
                    @Override
                    public void execute() {
                        // TODO Run puzzle activity
                        //runPuzzleActivity(currentTask.getPuzzle());
                    }
                };

                beaconUtil.addBeacon(definition);
            } else if (task instanceof CodeTask) {
                Marker newMarker = MapUtil.createColoredCircleMarker(
                        this,
                        mMap,
                        task.getName(),
                        R.color.colorPrimary,
                        R.style.marker_text_style,
                        new LatLng(task.getLatitude(), task.getLongitude())
                );
            }

            int src;
            try {
                HashMap<String, Integer> resources = TreasureHuntStoryLineDbHelper.markerResources;
                src = resources.get(task.getName());
            }
            catch (Exception ex) {
                src = R.drawable.ic_delete_cross;
            }
            MarkerOptions markerOptions =
                    new MarkerOptions()
                            .position(new LatLng(task.getLatitude(), task.getLongitude()))
                            .title(task.getName())
                            .icon(BitmapDescriptorFactory
                                    .fromResource(
                                            src
                                    )
                            );
            marker = mMap.addMarker(markerOptions);
            markers.put(task, marker);
            latLngBoundsBuilder.include(new LatLng(task.getLatitude(), task.getLongitude()));
        }
        updateMarkers();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds bounds = latLngBoundsBuilder.build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 200);

                mMap.animateCamera(cameraUpdate);
            }
        });
    }

    private void updateMarkers() {
        for(Map.Entry<Task, Marker> entry : markers.entrySet()) {
            if(currentTask != null) {
                // do something
                if(currentTask.getName().equals(entry.getKey().getName())) {
                    entry.getValue().setVisible(true);
                } else {
                    entry.getValue().setVisible(false);
                }
            } else {
                entry.getValue().setVisible(true); // TODO set back to false
            }
        }
    }



    private void initialiazeListeners() {
        if (currentTask != null) {
            if (currentTask instanceof GPSTask) {
                if (googleApiClient.isConnected()) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                        return;
                    }
                    LocationServices
                            .FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                }
//                qrButton.setVisibility(View.GONE);
            }

            if (currentTask instanceof BeaconTask) {
                beaconUtil.startRanging();
                qrButton.setVisibility(View.GONE);

            }

            if (currentTask instanceof CodeTask) {
                qrButton.setVisibility(View.VISIBLE);
            }
            zoomToNewTask(new LatLng(currentTask.getLatitude(), currentTask.getLongitude()));
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("map ", "onConnected");
        initialiazeListeners();

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("map ", "onConnectionSuspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("map ", "onConnectionFailed");
    }

    private void cancelListeners() {
        if (googleApiClient.isConnected()) {
            LocationServices
                    .FusedLocationApi
                    .removeLocationUpdates(googleApiClient, this);
        }
        if (beaconUtil.isRanging()){
            beaconUtil.stopRanging();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, "Location" + location.getLatitude() + " - " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        if (currentTask != null && currentTask instanceof GPSTask){
            double radius = ((GPSTask) currentTask).getRadius();
            LatLng userPosition = new LatLng(location.getLatitude(),location.getLongitude());
            LatLng taskPosition = new LatLng(currentTask.getLatitude(),currentTask.getLongitude());
            if (SphericalUtil.computeDistanceBetween (userPosition, taskPosition) < radius){
                runPuzzleActivity(currentTask.getPuzzle());
            }
        }
    }

    private void runPuzzleActivity (Puzzle puzzle){
        if (puzzle instanceof SimplePuzzle){
            Intent intent = new Intent(this, SimplePuzzleActivity.class);
            startActivity(intent);
        }
        if (puzzle instanceof ImageSelectPuzzle){
            Intent intent = new Intent(this, ImageSelectActivity.class);
            startActivity(intent);
        }
        if (puzzle instanceof ChoicePuzzle){
            Intent intent = new Intent(this, TextSelectActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentTask = storyLine.currentTask();
        if(currentTask == null){
            Intent intent = new Intent (this, FinishActivity.class);
            startActivity(intent);
        }else{
            initialiazeListeners();
            //updateMarkers();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelListeners();
    }
    private void zoomToNewTask(LatLng position){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position,15);

        if(cameraUpdate.equals(null)){
            mMap.animateCamera(cameraUpdate);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currentTask = storyLine.currentTask();
        if(currentTask != null && currentTask instanceof CodeTask){
            String result = QRCodeUtil.onScanResult(this, requestCode,resultCode,data);
            CodeTask codeTask = (CodeTask) currentTask;
            if(codeTask.getQR().equals(result)){
                runPuzzleActivity(currentTask.getPuzzle());
            }
        }
    }

    public void scanForQRCode(View view) {
        QRCodeUtil.startQRScan(this);

    }
    @Override
    public boolean onMarkerClick(Marker marker) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("skip task?");
        builder.setMessage("Do you want to skip the current task?");
        builder.setCancelable(true);
        builder.setPositiveButton("SKIP TASK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                currentTask.skip();
                currentTask = storyLine.currentTask();
                if (currentTask == null){

                }else{
                    cancelListeners();
                    initialiazeListeners();
                }
               // updateMarkers();
                zoomToNewTask(new LatLng(currentTask.getLatitude(), currentTask.getLongitude()));
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
        return false;
    }
}
