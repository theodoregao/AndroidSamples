package gao.shun.sg.placeaware;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Theodore on 2017/11/4.
 */

public class Geofencing implements ResultCallback {
    private static final String TAG = Geofencing.class.getSimpleName();

    private static final float GEOFENCE_RADIUS = 50; // 50 meters
    private static final long GEOFENCE_TIMEOUT = 24 * 60 * 60 * 1000; // 24 hours

    private Context context;
    private GoogleApiClient googleApiClient;
    private PendingIntent pendingIntent;
    private List<Geofence> geofences;

    public Geofencing(Context context, GoogleApiClient googleApiClient) {
        this.context = context;
        this.googleApiClient = googleApiClient;
        pendingIntent = null;
        geofences = new ArrayList<>();
    }

    public void registerAllGeofences() {
        if (googleApiClient == null || !googleApiClient.isConnected() || geofences == null || geofences.size() == 0)
            return;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Permission is not granted", Toast.LENGTH_LONG).show();
            return;
        }

        LocationServices.GeofencingApi.addGeofences(googleApiClient,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }

    public void unRegisterAllGeofences() {
        if (googleApiClient == null || googleApiClient.isConnected())
            return;

        LocationServices.GeofencingApi.removeGeofences(googleApiClient,
                getGeofencePendingIntent()
        ).setResultCallback(this);
    }

    public void updateGeofencesList(PlaceBuffer places) {
        geofences = new ArrayList<>();
        if (places == null || places.getCount() == 0) return;
        for (Place place: places) {
            String placeUID = place.getId();
            double placeLat = place.getLatLng().latitude;
            double placeLng = place.getLatLng().longitude;
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(placeUID)
                    .setExpirationDuration(GEOFENCE_TIMEOUT)
                    .setCircularRegion(placeLat, placeLng, GEOFENCE_RADIUS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            geofences.add(geofence);
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofences);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (pendingIntent != null) return pendingIntent;
        Intent intent = new Intent(context, GeofenceBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void onResult(@NonNull Result result) {
        Log.e(TAG, String.format("Error adding/removing geofence : %s", result.getStatus().toString()));
    }
}
