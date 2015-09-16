package com.example.commonframe.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Tyrael on 9/16/15.
 */
public final class GoogleMapUtils {

    private static double maxSpeed;
    private static double minSpeed;
    private static float COLOR_RANGE = 120f;


    private static double getMaxSpeed(List<Location> location) {
        double speed = 0;

        for (Location position : location) {
            if (position.getSpeed() > speed) {
                speed = position.getSpeed();
            }
        }

        return speed;
    }

    private static double getMinSpeed(List<Location> location) {
        if (location.size() > 0) {
            if (location.get(0) != null) {
                double speed = location.get(0).getSpeed();

                for (Location position : location) {
                    if (position.getSpeed() < speed) {
                        speed = position.getSpeed();
                    }
                }
                return speed;
            }
        }

        return 0;
    }

    private static int getColorBySpeed(double spped) {
        double total = maxSpeed - minSpeed;
        double number = spped - minSpeed;
        float H = (float) ((number / total) * COLOR_RANGE);
        float S = 0.9f;
        float V = 0.9f;
        if (total == 0) {
            H = 0;
        }
        if (H < 0) {
            H = 0;
        } else if (H > COLOR_RANGE) {
            H = COLOR_RANGE;
        }
        return Color.HSVToColor(new float[]{H, S, V});
    }

    private static Bitmap createRoute(GoogleMap map, int width, int height, List<Location> location) {
        Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new CornerPathEffect(2));
        paint.setAntiAlias(true);

        int startColor = getColorBySpeed(location.get(0).getSpeed());
        int endColor;

        Bitmap routeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas routeCanvas = new Canvas(routeBitmap);

        for (int i = 0; i < location.size() - 1; i++) {
            Point startPoint = map.getProjection().toScreenLocation(new LatLng(location.get(i).getLatitude(), location.get(i).getLongitude()));
            Point endPoint = map.getProjection().toScreenLocation(new LatLng(location.get(i + 1).getLatitude(), location.get(i + 1).getLongitude()));

            endColor = getColorBySpeed(location.get(i + 1).getSpeed());

            Shader gradienShader = new LinearGradient(startPoint.x, startPoint.y, endPoint.x, endPoint.y, startColor, endColor, Shader.TileMode.MIRROR);
            paint.setShader(gradienShader);

            startColor = endColor;

            routeCanvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint);
        }

        return routeBitmap;
    }


    public static void drawRouteOnMap(GoogleMap map, List<Location> location, int containerWidth, int containerHeight) {
        Bitmap bitmap = createRoute(map, containerWidth, containerHeight, location);
        GroundOverlayOptions overlay = new GroundOverlayOptions();
        overlay.image(BitmapDescriptorFactory.fromBitmap(bitmap));
        overlay.positionFromBounds(map.getProjection().getVisibleRegion().latLngBounds);
        map.addGroundOverlay(overlay);

    }

}
