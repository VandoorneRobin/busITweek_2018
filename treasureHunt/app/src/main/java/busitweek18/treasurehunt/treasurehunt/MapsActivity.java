package busitweek18.treasurehunt.treasurehunt;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

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

import java.util.HashMap;
import java.util.Map;

import cz.mendelu.busItWeek.library.BeaconTask;
import cz.mendelu.busItWeek.library.CodeTask;
import cz.mendelu.busItWeek.library.GPSTask;
import cz.mendelu.busItWeek.library.StoryLine;
import cz.mendelu.busItWeek.library.Task;
import cz.mendelu.busItWeek.library.beacons.BeaconDefinition;
import cz.mendelu.busItWeek.library.beacons.BeaconUtil;
import cz.mendelu.busItWeek.library.map.MapUtil;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private StoryLine storyLine;
    private Task currentTask;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private BeaconUtil beaconUtil;
    private HashMap<Task, Marker> markers = new HashMap<>();
    private LatLngBounds.Builder latLngBoundsBuilder;


    private static final String TAG = MapsActivity.class.getSimpleName();



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
                // Code task
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
