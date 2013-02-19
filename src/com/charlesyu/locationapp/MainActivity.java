package com.charlesyu.locationapp;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements LocationListener{

	// View Attributes
	Button btnStart, btnStop;
	// Map Location Attributes
	private GoogleMap map;
	private LocationManager locationManager;
	private String provider;
	private double curLat, curLong;
	private LatLng prevLoc, curLoc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }
    
    private void setup(){
    	// Initialize views
    	btnStart = (Button)findViewById(R.id.btnStart);
    	btnStop = (Button)findViewById(R.id.btnStop);
    	
    	// Create map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        
        // Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the location provider 
	    provider = locationManager.getBestProvider(new Criteria(), true);
	    
	    btnStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Start location update
				Toast.makeText(MainActivity.this, "Location update started!", Toast.LENGTH_SHORT).show();
				locationManager.requestLocationUpdates(provider, 10000, 10, MainActivity.this);
			}
		});
	    
	    btnStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Stop location update and toast current location
				locationManager.removeUpdates(MainActivity.this);
				Toast.makeText(MainActivity.this, "Current Location: \nLat: "+curLat+" Long: "+curLong, Toast.LENGTH_SHORT).show();
			}
		});
        
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.d("HERE", "Location changed!");
		prevLoc = curLoc;
		// Add a marker
		curLat = location.getLatitude();
		curLong = location.getLongitude();
	    curLoc = new LatLng(curLat, curLong);
	    map.addMarker(new MarkerOptions().position(curLoc));
	    // Draw a trail to previous location
	    if(prevLoc != null){
		    map.addPolyline(new PolylineOptions()
		       .add(prevLoc, curLoc)
		       .width(5)
		       .color(Color.RED));
	    }
	    // Animate to new location
	    map.animateCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 15));
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
