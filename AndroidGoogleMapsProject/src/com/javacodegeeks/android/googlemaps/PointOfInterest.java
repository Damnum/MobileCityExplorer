package com.javacodegeeks.android.googlemaps;

import android.graphics.Bitmap;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class PointOfInterest extends OverlayItem {
	private String name;
	private GeoPoint point;
	private Bitmap bitmap;
	
	public PointOfInterest(String name, GeoPoint point, Bitmap bitmap){
		super(point, name, "b");
		this.name = name;
		this.point = point;
		this.bitmap= bitmap;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public GeoPoint getPoint() {
		return point;
	}
	public void setPoint(GeoPoint point) {
		this.point = point;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;

	}
	
	
	
}
