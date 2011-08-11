package com.javacodegeeks.android.googlemaps;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

//based on code snippet:
//http://blog.pocketjourney.com/2008/03/19/tutorial-2-mapview-google-map-hit-testing-for-display-of-popup-windows/

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	//ADD, REMOVE or VIEW
	private int mode;
	
	private int markerMode;
	
	private Paint innerPaint, borderPaint, textPaint;
	
	private Context context;

	//Our Icon we will draw
	
	//Point of Interests
	private ArrayList<PointOfInterest> pois = new ArrayList<PointOfInterest>();
	
	//PoI that is currently selected. null if nothing selected
	private PointOfInterest poiToDelete;
	
	private PointOfInterest poiToInfo;

	/**
	 * Creates a new Overlay containing our PoIs
	 * @param defaultMarker 
	 * @param context
	 */
	public MyItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		pois = new ArrayList<PointOfInterest>();
		this.context = context;
		
		//refresh the view
		populate();

	}
	
	/**
	 * draw all PoIs
	 */
    @Override
	public void draw(Canvas canvas, MapView	mapView, boolean shadow) {
    	
   		drawPoi(canvas, mapView, shadow);
   		drawInfoWindow(canvas, mapView, shadow);
    }
    
    /**
     * 
     * @param canvas
     * @param mapView
     * @param shadow
     */
    private void drawPoi(Canvas canvas, MapView mapView, boolean shadow){
    	
    

		Point screenCoords = new Point();
		
		//run through PoI-List and draw all PoIs
    	for(PointOfInterest poi: pois){	  
    		//translate to pixel view
    		mapView.getProjection().toPixels(poi.getPoint(), screenCoords);
    		
    		//draw the item
    		//right position see
    		//http://blog.pocketjourney.com/2008/03/19/tutorial-2-mapview-google-map-hit-testing-for-display-of-popup-windows/
    		canvas.drawBitmap(poi.getBitmap(), screenCoords.x - poi.getBitmap().getWidth()/2, screenCoords.y - poi.getBitmap().getHeight(),null);
    	}
    }

    /**
     * add a new poi to the list
     */
	@Override
	protected OverlayItem createItem(int i) {
		return pois.get(i);
	}

	/**
	 * tracks if poi was hit. If yes, return the poi that was hit. if no return null
	 * @param mapView
	 * @param tapPoint
	 * @return
	 */
  	private PointOfInterest getHitPoi(MapView mapView, GeoPoint tapPoint) {

		// Track which poi was hit…if any
		PointOfInterest hitPoi = null;
	
		RectF hitTestRecr = new RectF();
	
		
		for(PointOfInterest poi: pois){
	
		Point screenCoords = new Point();
		// As above, translate MapLocation lat/long to screen coordinates
		mapView.getProjection().toPixels(poi.getPoint(), screenCoords);
	
		// Use this information to create a ‘hit” testing Rectangle to represent the size
		// of our location’s icon at the correct location on the screen. 
		// As we want the base of our balloon icon to be at the exact location of
		// our map location, we set our Rectangle’s location so the bottom-middle of
		// our icon is at the screen coordinates of our map location (shown above).
		hitTestRecr.set(-poi.getBitmap().getWidth()/2,-poi.getBitmap().getHeight(),poi.getBitmap().getWidth()/2,0);
	
		// Create a 'hit' testing Rectangle w/size and coordinates of our icon
		// Set the 'hit' testing Rectangle with the size and coordinates of our on screen icon
		hitTestRecr.offset(screenCoords.x,screenCoords.y);
	
		//  Finally test for a match between our 'hit' Rectangle and the location clicked by the user
		mapView.getProjection().toPixels(tapPoint, screenCoords);
		if (hitTestRecr.contains(screenCoords.x,screenCoords.y)) {
			hitPoi = poi;
			break;
		}

	}
	return hitPoi;

	}
  	
	
	private void drawInfoWindow(Canvas canvas, MapView mapView, boolean shadow) {
    	if ( poiToInfo != null) {
    		if ( shadow) {
    			//no shadow
    		} else {
		// Again get our screen coordinate
		Point destinationPoi = new Point();
		mapView.getProjection().toPixels(poiToInfo.getPoint(), destinationPoi);

		// Setup the info window with the right size & location
		int INFO_WINDOW_WIDTH = 125;
		int INFO_WINDOW_HEIGHT = 25;
		RectF infoWindowRect = new RectF(0,0,INFO_WINDOW_WIDTH,INFO_WINDOW_HEIGHT);
		int infoWindowOffsetX = destinationPoi.x-INFO_WINDOW_WIDTH/2;
		int infoWindowOffsetY = destinationPoi.y-INFO_WINDOW_HEIGHT-poiToInfo.getBitmap().getHeight();
		infoWindowRect.offset(infoWindowOffsetX,infoWindowOffsetY);

		// Draw inner info window
		canvas.drawRoundRect(infoWindowRect, 5, 5, getInnerPaint());

		// Draw border for info window
		canvas.drawRoundRect(infoWindowRect, 5, 5, getBorderPaint());

		// Draw the MapLocation’s name
		int TEXT_OFFSET_X = 10;
		int TEXT_OFFSET_Y = 15;
		canvas.drawText(poiToInfo.getName(),infoWindowOffsetX+TEXT_OFFSET_X,infoWindowOffsetY+TEXT_OFFSET_Y,getTextPaint());
    		}
		}
	}
	
	public Paint getInnerPaint() {
		if ( innerPaint == null) {
			innerPaint = new Paint();
			innerPaint.setARGB(225, 75, 75, 75); //gray
			innerPaint.setAntiAlias(true);
		}
		return innerPaint;
	}
	
	public Paint getBorderPaint() {
		if ( borderPaint == null) {
			borderPaint = new Paint();
			borderPaint.setARGB(255, 255, 255, 255);
			borderPaint.setAntiAlias(true);
			borderPaint.setStyle(Style.STROKE);
			borderPaint.setStrokeWidth(2);
		}
		return borderPaint;
	}
	
	public Paint getTextPaint() {
		if ( textPaint == null) {
			textPaint = new Paint();
			textPaint.setARGB(255, 255, 255, 255);
			textPaint.setAntiAlias(true);
		}
		return textPaint;
	}
  	
  	/**
  	 * called if screen is touched.
  	 * Adds a new poi if mode == ADD
  	 * Removes the hit poi if mode == REMOVE
  	 */
	@Override
	public boolean onTap(GeoPoint p, MapView mapView)  { 
		boolean isHandled = false;
		  
	    //Add Marker
	    switch (mode){

	    
	    case Constants.Mode.ADD:
			int marker = R.drawable.general;
			String name = "";
			//create a new Icon that is drawable
			switch(markerMode){
			case Constants.MarkerMode.GENERAL:
				marker = R.drawable.general;
				name = "Nothing Special";
				break;
			case Constants.MarkerMode.RESTAURANT:
				marker = R.drawable.restaurant;
				name = "Restaurant";
				break;
			case Constants.MarkerMode.CHURCH:
				marker = R.drawable.church;
				name = "Church";
				break;
			}
			

			pois.add(new PointOfInterest(name,p,
					BitmapFactory.decodeResource(context.getResources(),marker)));

	    

	    isHandled = true;
	    break;
	          
	    //Remove the selected item from the overlay
		case Constants.Mode.REMOVE:
		//test whether we should remove the poi
			poiToDelete = getHitPoi(mapView,p);
		    if (poiToDelete != null){
		    	pois.remove(poiToDelete);
		        isHandled = true;
		    }
				break;
				
	    case Constants.Mode.INFO:
	    	poiToInfo = getHitPoi(mapView,p);
	    	if (poiToInfo != null){
	    		isHandled = true;
	    	}
	    }
		//  return true if we handled this onTap()
		return isHandled;
	}

	// Returns present number of items in list
	@Override
	public int size() {
		return pois.size();
	}


//--------------------------GETTER/SETTER---------------------------------
	/**
	 * 
	 * @return mode determines if app is in ADD or REMOVE mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * 
	 * @param mode determines if app is in ADD or REMOVE mode
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getMarkerMode() {
		return markerMode;
	}

	public void setMarkerMode(int markerMode) {
		this.markerMode = markerMode;
	}
}

//----------------OLD METHODS--------------------------------------------------------

//// Handle tap events on overlay icons
//@Override
//protected boolean onTap(int i){
//	
//	/*	In this case we will just put a transient Toast message on the screen indicating that we have
//	captured the relevant information about the overlay item.  In a more serious application one
//	could replace the Toast with display of a customized view with the title, snippet text, and additional
//	features like an embedded image, video, or sound, or links to additional information. (The lat and
//	lon variables return the coordinates of the icon that was clicked, which could be used for custom
//	positioning of a display view.)*/
//	
//
//	System.out.println("onTap");
//	System.out.println("BEFORE REMOVE" + get());
//	
//	if (mode == Mode.REMOVE){
//
//		try {
//		removeItem(myOverlayItems.get(i));
//		} catch (Exception e){
//			System.err.println(e);
//		}
//
//		System.out.println("AFTER REMOVE: " + get());
//		System.out.println("size: " + myOverlayItems.size());
//	}
//	
//	
////	GeoPoint  gpoint = myOverlayItems.get(i).getPoint();
////	double lat = gpoint.getLatitudeE6()/1e6;
////	double lon = gpoint.getLongitudeE6()/1e6;
////	String toast = "Title: "+myOverlayItems.get(i).getTitle();
////	toast += "\nText: "+myOverlayItems.get(i).getSnippet();
////	toast += 	"\nSymbol coordinates: Lat = "+lat+" Lon = "+lon+" (microdegrees)";
//	//Toast.makeText(GMapsActivity.context, toast, Toast.LENGTH_LONG).show(); //PopUp
//	
//	//global Item, damit es durch "button remove individuel marker" gelöscht werden kann
////	GMapsActivity.overlayItem = myOverlayItems.get(i);
//	
//	//modify and make it easier
//	//open an own Panel "transparent_panel_poi" for further object informations
////	GMapsActivity.transparent_panel_poi.setVisibility(View.VISIBLE);
//	//GMapsActivity.TextSample.setText("asdf");
//	//cant set TextView-content? why?
//	return true;
///*
//		OverlayItem item = mapOverlays.get(index);
//		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//		dialog.setTitle(item.getTitle());
//		dialog.setMessage(item.getSnippet());
//		dialog.show();
//*/
//}

////Important Workaround:
////http://developmentality.wordpress.com/2009/10/19/android-itemizedoverlay-arrayindexoutofboundsexception-nullpointerexception-workarounds/
//private void doPopulate(){
//	setLastFocusedIndex(-1);
//	populate();
//}

//public boolean onTouchEvent(MotionEvent event, MapView mapView) 
//{   
//  
//  marker = context.getResources().getDrawable(R.drawable.marker_blau);
//  
//  System.out.println("ontouchevent ");
//  if (event.getAction() == 1) {                
//        
//
//      //Add Marker
//      switch (mode){
//      case Mode.ADD:
//    	  
//    	  
//       PointOfInterest poi = new PointOfInterest("a",
//  		  (int) event.getX()- marker.getIntrinsicWidth()/2,	 
//  		  (int) event.getY()- marker.getIntrinsicHeight()
//       );
//       pois.add(poi);
//       
//      //Create new Item and add it to the transparent overlay
//      addItem(
//          new OverlayItem(poi.getPoint(),poi.getName(), "b"), marker          
//      );
//      break;
//      
//      //Remove the selected item from the overlay
//        case Mode.REMOVE:
//		
//			
//		break;
//      }
//
//  }                            
//  return false;
//}

////gives entire List as String value back
//public String get() 
//{
//	StringBuilder sb = new StringBuilder();
//    for (int i = 0; i < myOverlayItems.size(); i++) {
//        sb.append("\n " + myOverlayItems.get(i) + " " + i);
//    }
//	return sb.toString();
//}

//checks List if specific item exists
//public boolean check (OverlayItem overlayitem)
//{	
//	System.out.println("checkitem, index "+ myOverlayItems.indexOf(overlayitem));
//    int index = myOverlayItems.indexOf(overlayitem);
//    if (index == -1)
//      return false;
//    else
//      return true;
//}

//correct Position is important!!! 
//how to set the correct position
// http://blog.pocketjourney.com/2008/03/19/tutorial-2-mapview-google-map-hit-testing-for-display-of-popup-windows/

//ADD
//INPUT: ITEM
//and add it to existing list
//public void addItem(OverlayItem overlayitem,Drawable icon)
//{
//	setDrawable(overlayitem, icon);
//	myOverlayItems.add(overlayitem);
//	doPopulate();
//}

//ADD 
//INPUT: coordinateA + coordinateB + name
//then build new ITEM and add it to existing list
//can modify to make it easier
//private void addItem(String coordinateA, String coordinateB, String name,Drawable icon)
//{
//	GMapsActivity gmapsactivity = new GMapsActivity();
//	GeoPoint geopoint = gmapsactivity.calculate(coordinateA, coordinateB);
//	
//	OverlayItem overlayitem = new OverlayItem(geopoint, "Info", name);
//	setDrawable(overlayitem, icon);
//	myOverlayItems.add(overlayitem);
//	
//	populate();
//}

//// Removes overlay item i
//private boolean removeItem(int i){
//	if(i >= 0)
//	{
//		myOverlayItems.remove(i);
//		setLastFocusedIndex(-1);
//		populate();
//		return true;
//	}
//	
//	return false;
//}

// Removes overlayitem if item exists
//private boolean removeItem(OverlayItem overlayitem){
//	System.out.println("removeitem");
//	boolean bool = check(overlayitem);
//	if (bool == true)
//	{
//		int index = myOverlayItems.indexOf(overlayitem);
//		myOverlayItems.remove(index);
//		doPopulate();
//		System.out.println("removed");
//		return bool;
//	}
//	else 
//		return bool;
//}
//public void setDrawable (OverlayItem overlayItem, Drawable icon)
//{
//	System.out.println("setDrawable");
//	icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
//	overlayItem.setMarker(icon);
//}