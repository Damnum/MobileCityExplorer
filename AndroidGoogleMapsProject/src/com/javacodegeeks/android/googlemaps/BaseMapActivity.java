package com.javacodegeeks.android.googlemaps;

import com.google.android.maps.MapActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public abstract class BaseMapActivity extends MapActivity{
	
	private MyItemizedOverlay itemizedOverlay;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.general:
	    	this.itemizedOverlay.setMarkerMode(Constants.MarkerMode.GENERAL);
	    	break;
	    case R.id.restaurant:
	    	this.itemizedOverlay.setMarkerMode(Constants.MarkerMode.RESTAURANT);
	    	break;
	    case R.id.church:
	    	this.itemizedOverlay.setMarkerMode(Constants.MarkerMode.CHURCH);
	    	break;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	    return super.onOptionsItemSelected(item);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public MyItemizedOverlay getItemizedOverlay() {
		return itemizedOverlay;
	}

	public void setItemizedOverlay(MyItemizedOverlay itemizedOverlay) {
		this.itemizedOverlay = itemizedOverlay;
	}
}
