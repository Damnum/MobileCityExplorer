package com.javacodegeeks.android.googlemaps;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


// Explenation: wofür sind die ganzen elemente in den ordnern zuständig
	// http://www.androidpit.de/de/android/wiki/view/Android_Anfänger_Workshop

/*	Overlay
 * 		http://code.google.com/intl/de-DE/android/add-ons/google-apis/reference/com/google/android/maps/MapView.html
 * 		http://code.google.com/intl/de-DE/android/add-ons/google-apis/reference/com/google/android/maps/Overlay.html
 * 
 */



//*************************************************************************************


public class GMapsActivity extends BaseMapActivity implements OnClickListener {

// variable declaration
	private MapView mapView;
	
	//main Button declaration
	/*private Button button_search;
	private Button button_filter;
	private Button button_traffic;
	private Button button_mytrip;
	private Button button_settings;
	
	//"mytrip" sub button declaration
	private Button subButton_culture;
	private Button subButton_top10;
	private Button subButton_cityTour; 
	
	//"settings" sub button declaration
	private Button subButton_profil;
	private Button subButton_network;*/ 
	
	//test for add & remove marker
	private Button button_add;
	private Button button_remove;
	private Button button_info;
	
//	MapOverlay mapOverlay;

	
	
	MapController mapController;
	
	//context for later
	static Context context;
	
	//only for testing POI 
	public static TransparentPanel transparent_panel_poi;
	public static TextView TextSample; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get application context for later use
        context = getApplicationContext();
        
        //zoom-functionalitiy
        mapView = (MapView) findViewById(R.id.map_view);       
        mapView.setBuiltInZoomControls(true);

        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        
        
        setItemizedOverlay(new MyItemizedOverlay(
        		context.getResources().getDrawable(R.drawable.general), context));
        
        listOfOverlays.add(getItemizedOverlay());

		
        
		mapController = mapView.getController();
		
		//centering Map		
		mapController.animateTo(calculate("49.48853488197713", "8.47118854522705")); //zentriert karte auf punkt
		mapController.setZoom(15);
		
	
		
//Menu
		//TO DO: 
		// auslagern, dynamische Klasse erstellen
		//2 Event-Möglichkeiten: 
		//	-anonyme-Methode wie in Button1
		//  oder 
		//  -eigene Klassen Methode "onclick" für alle verwenden, bsp. button2
		
		//Custom Button-Tutorial
		//http://developer.android.com/resources/tutorials/views/hello-formstuff.html#CustomButton
        
//		Button button1 = (Button) findViewById(R.id.button_search);	
//        button1.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View view) {
//				Toast.makeText(GMapsActivity.this, "SEARCH",Toast.LENGTH_SHORT).show();
//        }});
        

        //main buttons
        /*button_search = (Button) findViewById(R.id.button_search);
        button_search.setOnClickListener(this);
        
        button_filter = (Button) findViewById(R.id.button_filter);
        button_filter.setOnClickListener(this);
    	
        button_traffic = (Button) findViewById(R.id.button_traffic);
        button_traffic.setOnClickListener(this);
    	
        button_mytrip = (Button) findViewById(R.id.button_mytrip);
        button_mytrip.setOnClickListener(this);  
        
        button_settings = (Button) findViewById(R.id.button_settings);
        button_settings.setOnClickListener(this);*/ 
        
        
       
        //"mytrip" sub buttons
    	/*subButton_culture = (Button) findViewById(R.id.subbutton_culture);
    	subButton_culture.setOnClickListener(this);
        
    	subButton_top10 = (Button) findViewById(R.id.subbutton_top10);
    	subButton_top10.setOnClickListener(this);
        
    	subButton_cityTour = (Button) findViewById(R.id.subbutton_cityTour); 
    	subButton_cityTour.setOnClickListener(this);
        

        //"settings" sub buttons
        subButton_profil = (Button) findViewById(R.id.subbutton_profil); 
        subButton_profil.setOnClickListener(this);
        
        subButton_network = (Button) findViewById(R.id.subbutton_network); 
        subButton_network.setOnClickListener(this);*/
        	
        
        //test for add & remove marker
        button_add = (Button) findViewById(R.id.button2_add); 
        button_add.setOnClickListener(this);
        
        button_remove = (Button) findViewById(R.id.button3_remove); 
        button_remove.setOnClickListener(this);
        
        button_info = (Button) findViewById(R.id.button_info);
        button_info.setOnClickListener(this);

        
        // for POI
//        transparent_panel_poi = (TransparentPanel)findViewById(R.id.transparent_panel_poi);
//        TextSample = (TextView) findViewById(R.id.textview);
        
        //nur für test
//		itemizedOverlay = new MyItemizedOverlay(this.getResources().getDrawable(R.drawable.marker_blau)); //individuelles Bild
		
        //add automatically a few marker to Layer1
//		setOverlay1();

        
		
//********************* GPS *************************************
        
//update gps-position automatically
    //it works with emulator, does it work with real device?
        // http://developer.android.com/reference/android/location/LocationManager.html#requestLocationUpdates%28java.lang.String,%20long,%20float,%20android.location.LocationListener%29
		//TO DO:
        // Expection-Handling???
        // power-saving? update-intervall? 
        	//http://developer.android.com/guide/topics/location/obtaining-user-location.html
//        LocationListener myLocationListener = new GeoUpdateHandler(); 
//        
//        Criteria c = new Criteria();	//http://developer.android.com/reference/android/location/Criteria.html	
//        c.setAccuracy(Criteria.ACCURACY_FINE);
//        
//        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        String provider = locationManager.getBestProvider(c, true);
//        boolean isGpsEnabled = locationManager.isProviderEnabled(provider); 
//
//        if (isGpsEnabled)
//        {
//                 Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                 locationManager.requestLocationUpdates(provider, 1, 1, myLocationListener);
//                //current settings: update every 30sec if 1meter position changed 
//                 
//         		// Initialize the location fields
//         		if (location != null) {
//         			System.out.println("Provider " + provider + " has been selected.");
////         			int lat = (int) (location.getLatitude());
////         			int lng = (int) (location.getLongitude());
//         			//latituteField.setText(String.valueOf(lat));
//         			//longitudeField.setText(String.valueOf(lng));
//         		} 
//         		else 
//         		{
//         			//latituteField.setText("Provider not available");
//         			//longitudeField.setText("Provider not available");
//         		}
        } 
        //else?
        	//message to user that GPS should enable. no automatic gps activation is possible
        
		//short and simple version        
		        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000,	10, myLocationListener);
				//current settings: update every 30sec if 10meter position changed 
        
        //further infos:
        //added in AndroidManifest.xml the Permission
        	//<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
//********************* GPS PART END *************************************
//    }



//******************* Buttons *******************************************
	//mainButtons
//	private boolean ButtonVisibiltyCheck_search = false;
//	private boolean ButtonVisibiltyCheck_filter = false;
//	private boolean ButtonVisibiltyCheck_traffic = false;
//	private boolean ButtonVisibiltyCheck_mytrip = false;
//	private boolean ButtonVisibiltyCheck_settings = false;
	
//	//subButtons "mytrip"
//	private boolean subButtonVisibiltyCheck_culture = false;
//	private boolean subButtonVisibiltyCheck_top10= false;
//	private boolean subButtonVisibiltyCheck_citytour = false;
//
//	//subButtons "settings"
//	private boolean subButtonVisibiltyCheck_profil = false;
//	private boolean subButtonVisibiltyCheck_network= false;
	

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		/* TO DO:
		 * 		for button_mytrip & button_settings:
		 * 			- focus button enable? how?
		 * 			- panel visibilty is redudant, better solution? maybe array?
		 * 			- invisible instead gone? which is why better? http://developer.android.com/reference/android/view/View.html#GONE
		 * 		for all buttons:
		 * 			- redudance!!!!!!!!!!!!!!!!!
		 * 			
		 * help: have a look into  the "class PopupPanel" at https://github.com/commonsguy/cw-advandroid/blob/master/Maps/ILuvNooYawk/src/com/commonsware/android/maps/NooYawk.java
		 */
		
	
//		TransparentPanel transpPanel_middle_search = (TransparentPanel)findViewById(R.id.transparent_panel_search);
//		TransparentPanel transpPanel_middle_traffic = (TransparentPanel)findViewById(R.id.transparent_panel_traffic);
//		TransparentPanel transpPanel_middle_culture = (TransparentPanel)findViewById(R.id.transparent_panel_culture);
//		TransparentPanel transpPanel_middle_top10 = (TransparentPanel)findViewById(R.id.transparent_panel_top10);
//		TransparentPanel transpPanel_middle_citytour = (TransparentPanel)findViewById(R.id.transparent_panel_citytour);
//
//		TransparentPanel_vertical transpPanel_vert_filter = (TransparentPanel_vertical)findViewById(R.id.transparent_vertical_filter);
//		TransparentPanel_vertical transpPanel_vert_mytrip = (TransparentPanel_vertical)findViewById(R.id.transparent_vertical_mytrip);
//		TransparentPanel_vertical transpPanel_vert_settings = (TransparentPanel_vertical)findViewById(R.id.transparent_vertical_settings);
		
		if (v == button_add) 
		{		
		//Change mode to ADD
			getItemizedOverlay().setMode(Constants.Mode.ADD);							
		}
		
		else if (v ==button_remove) 
		{
		//Change mode to REMOVE
			getItemizedOverlay().setMode(Constants.Mode.REMOVE);
		}
		
		else if (v == button_info){
			getItemizedOverlay().setMode(Constants.Mode.INFO);
		}

	}

	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
//************* End Part Buttons ********************************************
	
	//global methode to transform GPS into GeoPoint
	public GeoPoint calculate(String coordinateA, String coordinateB)
	{
	    double lat = Double.parseDouble(coordinateA);
	    double lng = Double.parseDouble(coordinateB);
	    
	    GeoPoint point = new GeoPoint( (int) (lat * 1E6), (int) (lng * 1E6));
	   
		return point;
	} 	

	
//---------------------------GPS AND OLD CODE---------------------------------------	
	
	
	// Define an array containing the food overlay items
//	private OverlayItem [] foodItem = {
//			new OverlayItem( calculate("49.48598748026435","8.470399975776672"), "Food Title 1", "Food snippet 1"), 
//			new OverlayItem( calculate("49.48591603959153","8.471800088882446"), "Food Title 2", "Food snippet 2"),
//			new OverlayItem( calculate("49.48697718422595","8.472387492656708"), "Food Title 3", "Food snippet 3") 
//	};

	//method for adding 3 markers simultaneous on the map
//	public void setOverlay1()
//	{	
//
//		int foodLength = foodItem.length;
//	    	
//	        Drawable drawable1 = this.getResources().getDrawable(R.drawable.knifefork_small); 
//	        itemizedOverlay = new MyItemizedOverlay(drawable1); 
//	        // Display all three items at once
//	        for(int i=0; i<foodLength; i++){
//	        	itemizedOverlay.addOverlay(foodItem[i], drawable1);
//	        	 
//	        }
//
//	        Toast.makeText( getBaseContext(),foodItem[0].getPoint().toString(),
//                    Toast.LENGTH_SHORT).show(); 
//	        
//	        OverlayItem.add(itemizedOverlay); 
//	}
	
	
  	
//********************* GPS Listener *************************************
//update current gps-position
//doesnt work
	//http://www.vogella.de/articles/AndroidLocationAPI/article.html
		//http://stackoverflow.com/questions/4885358/gps-coordinates-on-google-map
	
	
/*TO DO:
 * 	-testing on a real device
 * 
 * 
 */
//public class GeoUpdateHandler implements LocationListener {
//
//	public void onLocationChanged(Location location) {
//		int lat = (int) (location.getLatitude() * 1E6);
//		int lng = (int) (location.getLongitude() * 1E6);
//		GeoPoint point = new GeoPoint(lat, lng);
//		mapController.animateTo(point); //	mapController.setCenter(point);
//	}
//
//	public void onProviderDisabled(String provider) {
//	}
//
//	public void onProviderEnabled(String provider) {
//	}
//
//	public void onStatusChanged(String provider, int status, Bundle extras) {
//	}
//}
//********************* GPS Listener Part End *************************************


}
	