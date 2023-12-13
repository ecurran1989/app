package com.example.ecocarbontracker;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.ecocarbontracker.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    final private int REQUEST_COARSE_ACCESS = 123;
    boolean permissionGranted = false;
    LocationManager lm;
    LocationListener locationListener;
    private ActivityMapsBinding binding;
    private PlacesClient placesClient;
    private Toast locationToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Places API
        Places.initialize(getApplicationContext(), "AIzaSyAQDm0UMye3O50Lg_QWsNENAaXfWtc8RV8");
        placesClient = Places.createClient(this);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Request both coarse and fine location permissions
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_COARSE_ACCESS);
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (lm != null && locationListener != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                }, REQUEST_COARSE_ACCESS);
                return;
            } else {
                permissionGranted = true;
            }
            if (permissionGranted) {
                lm.removeUpdates(locationListener);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_ACCESS:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                } else {
                    permissionGranted = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    ACCESS_FINE_LOCATION}, REQUEST_COARSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        fetchNearbyRecyclingCenters();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {


                fetchPlaceDetails(point); // places

                Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
                try {

                    List<Address> addresses = geoCoder.getFromLocation(point.latitude, point.longitude, 1);
                    Address address = addresses.get(0);
                    String add = "";

                    if (addresses.size() > 0) {
                        add += address.getAddressLine(0) + "\n";
                        LatLng p = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(p).title(add));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 12.0f));
                    }
                    Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // New method to fetch place details using Places API
    private void fetchPlaceDetails(LatLng latLng) {
        // Create a FindCurrentPlaceRequest
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        );

        // Perform the place request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        placesClient.findCurrentPlace(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindCurrentPlaceResponse response = task.getResult();

                if (response != null) {
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Place place = placeLikelihood.getPlace();
                        LatLng placeLatLng = place.getLatLng();

                        // Check if the place is a recycling center
                        if (isLikelyRecyclingCenter(place)) {
                            // Add a marker for each nearby recycling center
                            mMap.addMarker(new MarkerOptions()
                                    .position(placeLatLng)
                                    .title(place.getName()));

                            // Move the camera to the first nearby recycling center
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, 12.0f));
                            break; // Stop processing places after finding a recycling center
                        }
                    }
                }
            } else {
                // Handle the error
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("PlacesAPI", "Place request failed: " + exception.getMessage());
                }
            }
        });
    }


    private void fetchNearbyRecyclingCenters() {
        // Define keywords for recycling centers
        List<String> keywordList = Arrays.asList("recycling", "civic amenity", "bin", "skips", "waste");

        // Iterate through keywords and fetch places
        for (String keyword : keywordList) {
            // Create a FindCurrentPlaceRequest for the current keyword
            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(
                    Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.TYPES)
            );

            // Perform the place request
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Handle the case where the user hasn't granted the location permission.
                return;
            }
            placesClient.findCurrentPlace(request).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();

                    if (response != null) {
                        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                            Place place = placeLikelihood.getPlace();
                            LatLng placeLatLng = new LatLng(
                                    place.getLatLng().latitude,
                                    place.getLatLng().longitude);

                            // Check if the place is a recycling center
                            if (isLikelyRecyclingCenter(place)) {
                                // Add a custom marker for each nearby recycling center
                                addCustomMarker(placeLatLng, place.getName());
                            }
                        }
                    }
                } else {
                    // Handle the error
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.e("PlacesAPI", "Place request failed: " + exception.getMessage());
                    }
                }
            });
        }

        // Move the camera to the user's location
        LatLng userLocation = getLastKnownLocation();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10.0f));
    }







    // Method to check if the place is likely a recycling center
    private boolean isLikelyRecyclingCenter(Place place) {
        // Check if the place name or types contain any of the specified keywords
        boolean hasRecyclingKeyword = containsKeyword(place.getName(), "recycling", "civic amenity", "bin", "skips", "waste");

        // Check if the place types include POINT_OF_INTEREST or ESTABLISHMENT
        List<Place.Type> recyclingTypes = Arrays.asList(
                Place.Type.POINT_OF_INTEREST,
                Place.Type.ESTABLISHMENT
        );

        List<Place.Type> placeTypes = place.getTypes();

        // Return true if it has the recycling keyword and is one of the specified types
        return hasRecyclingKeyword && placeTypes != null && !Collections.disjoint(placeTypes, recyclingTypes);
    }

    // Method to add a custom marker at a specific location
    private void addCustomMarker(LatLng position, String title) {
        // Customize the marker icon as needed (e.g., using a custom bitmap)
        BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(R.drawable.green_marker); // Replace with your custom marker icon
        int height = 100;
        int width = 100;

        // Create a new Bitmap from the customMarker
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.green_marker);
        Bitmap smallMarker = Bitmap.createScaledBitmap(originalBitmap, width, height, false);

        // Add the scaled marker to the map
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
    }





    // Method to get the last known location
    private LatLng getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return new LatLng(0, 0); // Default to (0, 0) if location permission is not granted
        }
        Location lastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return (lastLocation != null) ? new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()) : new LatLng(0, 0);
    }

    // Method to add a custom marker at a specific location




    private boolean containsKeyword(String text, String... keywords) {
        if (text == null) {
            return false;
        }
        text = text.toLowerCase(); // Convert text to lowercase for case-insensitive comparison
        for (String keyword : keywords) {
            if (text.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }




    private boolean hasPlaceType(Place place, Place.Type type) {
        return place != null && place.getTypes() != null && place.getTypes().contains(type);
    }


    private boolean hasPlaceSubtype(Place place, Place.Type subtype) {
        return place != null && place.getTypes() != null && place.getTypes().contains(subtype);
    }



    private boolean isPlaceType(Place place, Place.Type type) {
        return place != null && place.getTypes() != null && place.getTypes().contains(type);
    }





    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                String toastMessage = "Current Location: Lat " + location.getLatitude() +
                        " Lng: " + location.getLongitude();

                // Cancel the previous toast if it exists
                if (locationToast != null) {
                    locationToast.cancel();
                }

                // Show the new toast
                locationToast = Toast.makeText(getBaseContext(), toastMessage, Toast.LENGTH_SHORT);
                locationToast.show();

                LatLng p = new LatLng(location.getLatitude(), location.getLongitude());

                Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses = null;
                String add = "";
                try{
                    addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address address = addresses.get(0);

                    if(addresses.size() > 0) {
                        add += address.getAddressLine(0);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

                mMap.addMarker(new MarkerOptions()
                        .position(p)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title(add));

            }
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }

}