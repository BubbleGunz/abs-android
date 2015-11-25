package group9.android_project;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * Created by Halling- on 2015-11-25.
 */
public class MyLocationListener  {
    public static Position GetCurrentPostion(Context context)
    {
        LocationManager lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Position position = new Position();
        position.longitude = (float)longitude;
        position.latitude = (float)latitude;
        return position;
    }
}
