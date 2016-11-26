package com.cursoandroid.myopinion;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by igor on 22/10/16.
 */

public class GPSController implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    public Location mCurrentLocation;



    GPSController(Context c)
    {
        if (!isGooglePlayServicesAvailable(c)) {
            return;
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(c)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void onStart()
    {
        mGoogleApiClient.connect();
    }

    public void onStop()
    {
        mGoogleApiClient.disconnect();
    }

    public void onPause()
    {
        stopLocationUpdates();
    }

    public void onResume()
    {
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    private boolean isGooglePlayServicesAvailable(Context c) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(c);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, (Activity) c, 0).show();
            return false;
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }


    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public double getLatitude()
    {
        return mCurrentLocation.getLatitude();
    }

    public double getLongitude()
    {
        return mCurrentLocation.getLongitude();
    }

    public String getEstado(Context c)
    {
        Geocoder geocoder = new Geocoder(c, Locale.getDefault());
        String state = "";
    try {
        List<Address> list = geocoder.getFromLocation(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),1);
        if (list != null && list.size() > 0)
        {
            Address address = list.get(0);
            return address.getAdminArea();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
        return state;
    }

    public String getCidade(Context c)
    {
        Geocoder geocoder = new Geocoder(c, Locale.getDefault());
        String state = "";
        try {
            List<Address> list = geocoder.getFromLocation(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),1);
            if (list != null && list.size() > 0)
            {
                Address address = list.get(0);
                return address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return state;
    }

    public double[] getCoordenada(Context c,String address)
    {
        double[] coordenadas = {0,0};
        Geocoder geocoder = new Geocoder(c);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(address, 1);

        if(addresses.size() > 0) {
            coordenadas[0] = addresses.get(0).getLatitude();
            coordenadas[1] = addresses.get(0).getLongitude();
        }

        } catch (IOException e) {
            e.printStackTrace();;
        }
        return coordenadas;
    }


    public String getAddressFromLocation(Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        List<Address> addressList = null;

            addressList = geocoder.getFromLocation(this.getLatitude(), this.getLongitude(), 1);

            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                    sb.append(address.getAddressLine(i)).append("\n");
//                }
                String endereco = address.getAddressLine(0);
                sb.append(endereco.substring(endereco.lastIndexOf("-")+2) + "-"+ address.getAddressLine(1)) ;

                result = sb.toString();
            }


        return result;
    }

}
