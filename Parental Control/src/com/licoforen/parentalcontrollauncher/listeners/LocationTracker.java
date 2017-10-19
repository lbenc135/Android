package com.licoforen.parentalcontrollauncher.listeners;

import java.util.List;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;

public class LocationTracker extends BroadcastReceiver implements
		LocationListener {

	private static Context mContext;
	private static LocationManager locationManager;
	private static Criteria crta;
	private String provider;

	public LocationTracker() {
		super();
	}

	public LocationTracker(Context c) {
		mContext = c;

		locationManager = (LocationManager) c
				.getSystemService(Context.LOCATION_SERVICE);

		crta = new Criteria();
		crta.setAccuracy(Criteria.ACCURACY_FINE);
		crta.setAltitudeRequired(false);
		crta.setBearingRequired(false);
		crta.setCostAllowed(false);
	}

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		provider = locationManager.getBestProvider(crta, true);

		if (provider != null
				&& !provider.equals(LocationManager.PASSIVE_PROVIDER))
			locationManager.requestSingleUpdate(provider, this, null);
		else
			updateWithNewLocation(null);
	}

	private void updateWithNewLocation(Location location) {
		String addressString = "No address found!";

		if (location != null) {
			double lat = location.getLatitude();
			double lon = location.getLongitude();

			Geocoder gc = new Geocoder(mContext, Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocation(lat, lon, 1);
				StringBuilder sb = new StringBuilder();
				if (addresses.size() > 0) {
					Address address = (Address) addresses.get(0);
					sb.append(address.getAddressLine(0)).append(", ");
					sb.append(address.getLocality()).append(", ");
					sb.append(address.getCountryName());
				}
				addressString = sb.toString();
			} catch (Exception e) {
			}
		}
		ResourceLoader.addToLog("Location update:\n" + addressString);
	}

	@Override
	public void onLocationChanged(Location location) {
		updateWithNewLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
