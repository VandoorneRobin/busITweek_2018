package cz.mendelu.busItWeek.library.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import cz.mendelu.busItWeek.library.R;


/**
 * The utilities for operations around the map.
 */
public class MapUtil {

    /**
     * Creates a colored circle marker and returns it.
     * @param context context
     * @param map google map
     * @param text the text of the marker
     * @param backgroundColor the background color of the marker
     * @param textStyle style of the text
     * @param location location of the marker
     * @return googla map Marker
     */
    public static Marker createColoredCircleMarker(Context context,
                                                   GoogleMap map,
                                                   String text,
                                                   int backgroundColor,
                                                   int textStyle,
                                                   LatLng location){

        IconGenerator mClusterIconGenerator = new IconGenerator(context);
        final Drawable clusterIcon = context.getResources().getDrawable(R.drawable.marker_circle_background);
        clusterIcon.setColorFilter(context.getResources().getColor(backgroundColor), PorterDuff.Mode.SRC_ATOP);

        mClusterIconGenerator.setBackground(clusterIcon);
        mClusterIconGenerator.setTextAppearance(textStyle);

        Bitmap icon = mClusterIconGenerator.makeIcon(text);
        Marker marker = map.addMarker(new MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap(icon)));

        return marker;

    }



}
