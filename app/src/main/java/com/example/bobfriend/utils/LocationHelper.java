package com.example.bobfriend.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

public class LocationHelper {
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private OnLocationReceivedListener onLocationReceivedListener;

    public interface OnLocationReceivedListener {
        void onLocationReceived(double latitude, double longitude);
        void onLocationError(String error);
    }

    public LocationHelper(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void getCurrentLocation(OnLocationReceivedListener listener) {
        this.onLocationReceivedListener = listener;

        if (!isLocationPermissionGranted()) {
            listener.onLocationError("위치 권한이 필요합니다");
            return;
        }

        if (!isLocationEnabled()) {
            listener.onLocationError("GPS를 켜주세요");
            return;
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (onLocationReceivedListener != null) {
                    onLocationReceivedListener.onLocationReceived(
                            location.getLatitude(),
                            location.getLongitude()
                    );
                }
                stopLocationUpdates();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {
                if (onLocationReceivedListener != null) {
                    onLocationReceivedListener.onLocationError("GPS가 비활성화되었습니다");
                }
            }
        };

        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,
                        10,
                        locationListener
                );
            } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        1000,
                        10,
                        locationListener
                );
            }

            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null) {
                listener.onLocationReceived(
                        lastKnownLocation.getLatitude(),
                        lastKnownLocation.getLongitude()
                );
                stopLocationUpdates();
            }

        } catch (SecurityException e) {
            listener.onLocationError("위치 권한이 없습니다");
        }
    }

    private Location getLastKnownLocation() {
        if (!isLocationPermissionGranted()) {
            return null;
        }

        try {
            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (gpsLocation != null && networkLocation != null) {
                return gpsLocation.getTime() > networkLocation.getTime() ? gpsLocation : networkLocation;
            } else if (gpsLocation != null) {
                return gpsLocation;
            } else {
                return networkLocation;
            }
        } catch (SecurityException e) {
            return null;
        }
    }

    public void stopLocationUpdates() {
        if (locationManager != null && locationListener != null) {
            try {
                locationManager.removeUpdates(locationListener);
            } catch (SecurityException e) {
                // 권한 오류 무시
            }
        }
    }

    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // 두 지점 간의 거리 계산 (단위: km)
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    // 거리에 따른 텍스트 반환
    public static String getDistanceText(double distance) {
        if (distance < 1) {
            return String.format("%.0fm", distance * 1000);
        } else {
            return String.format("%.1fkm", distance);
        }
    }
}