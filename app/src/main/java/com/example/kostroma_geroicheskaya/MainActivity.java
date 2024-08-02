package com.example.kostroma_geroicheskaya;


import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateReason;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.MapType;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.transport.TransportFactory;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE = 1;
    public static Intent intentM;
    private MapView mapView;

    private float currentAzimuth = 0f;
    // points, chosePoint

    private Point nextPoint;
    private MapObjectCollection mapObjects;
    private static List<Point> points = new ArrayList<>();
    private List<PlacemarkMapObject> placemarks = new ArrayList<>();
    private static Integer chosePoint;

    private FusedLocationProviderClient fusedLocationClient;
    private PlacemarkMapObject placemarkA;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private String json;
    ImageButton hideRoadButton;

    private static final int YOUR_REQUEST_CODE = 1;

    //Компас
    private View compassImage;
    private ImageButton back;

    private CameraListener cameraListener;

    private float currentRotation = 0f;

    private long lastUpdateTime = 0;
    private ValueAnimator rotationAnimator;

    private boolean initialPositionSet = false;

    private boolean initialZoomCompleted = false;

    //Местоположение
    private ImageButton locationButton;
    private Point currentLocation;

    private LocationCallback locationCallback;

    private static final float ALPHA = 0.15f; // Коэффициент фильтрации (0-1). Меньшее значение = более плавное движение
    private float filteredAzimuth = 0f;
    private static final long UPDATE_INTERVAL_MS = 50; // Интервал обновления в миллисекундах

    //Ориентация
    private SensorManager sensorManager;

    private PolylineMapObject routePolyline;
    private PolylineMapObject routePolylineNextPoint;

    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean lastAccelerometerSet = false;

    private boolean lastMagnetometerSet = false;
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];

    private List<Integer>  imgOfStations;
    Button checkProximityButton;

    Boolean complateRoute;


    private boolean addPlacemarksBool = true;
    private static String apiKey;


    private int locationMode = 4; // начальный режи
    private  boolean flag = false;
    private Location previousLocation;

    private enum LocationMode {
        NORMAL, FOLLOW
    }

    private LocationMode currentLocationMode = LocationMode.NORMAL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);

            if (apiKey == null)
            {
                apiKey = "9473def6-b1a2-48c7-9219-e280fd8c50ed";

                MapKitFactory.setApiKey(apiKey);
                MapKitFactory.initialize(this);
                intentM = getIntent();
                addPlacemarksBool = false;

                nextPoint = new Point(57.74652116366075, 40.92196775275044);
            }
            setStatusBarColor(R.color.darkGreen);
            setContentView(R.layout.activity_main);
            mapView = findViewById  (R.id.mapview);
            mapObjects = mapView.getMap().getMapObjects();
            mapView.getMap().setMapType(MapType.VECTOR_MAP);
            mapView.getMap().setMapStyle(setMaps(R.raw.maps));

            back = findViewById(R.id.back);
            back.setOnClickListener(v -> {
                Intent intent = new Intent(this, ModeMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            });

            int countStation = Integer.parseInt(deserializeText("serComplateStation", "serComplateStation", MainActivity.this));
            if (countStation == 5)
                complateRoute = true;
            else
                complateRoute = false;
            imgOfStations = Arrays.asList(R.drawable.point_map1_4,  R.drawable.point_map2_4, R.drawable.point_map3_4,  R.drawable.point_map4_4, R.drawable.point_map5_4, R.drawable.point_map6_4);

            createLandmarks(addPlacemarksBool);
            updatePlacemarks();
            if (nextPoint == null)
                nextPoint = new Point(57.74652116366075, 40.92196775275044);
            moveCameraToLocation(nextPoint);

            //Местоположение


            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            locationButton = findViewById(R.id.location_button);
            locationButton.setImageResource(R.drawable.location_4); // изображение для первого режима
            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentLocation == null) {
                        Toast.makeText(MainActivity.this, "Включите режим геолокации", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    switch (currentLocationMode) {
                        case NORMAL:
                            currentLocationMode = LocationMode.FOLLOW;
                            break;
                        case FOLLOW:
                            currentLocationMode = LocationMode.NORMAL;
                            break;
                    }
                    updateUIForLocationMode();
                }
            });


            //Компас
            compassImage = findViewById(R.id.compass_view);

            // Добавляем слушатель изменения камеры
            cameraListener = new CameraListener() {
                @Override
                public void onCameraPositionChanged(Map map, CameraPosition cameraPosition, CameraUpdateReason cameraUpdateReason, boolean finished) {

                    if (finished) {
                        updateCompassRotation(cameraPosition.getAzimuth());

                        if (currentLocation != null) {
                            if (cameraPosition.getTarget() != null && !isCameraAtCurrentLocation(cameraPosition.getTarget())) {
                                currentLocationMode = LocationMode.NORMAL;
                                updateUIForLocationMode();
                            }
                        } else {
                            locationButton.setImageResource(R.drawable.location_4);
                            startLocationUpdates();
                        }
                    }


                }

            };
            mapView.getMap().addCameraListener(cameraListener);

            // Добавляем обработчик нажатия на компас
            compassImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveToCurrentLocation();
                    flag = false;
                    updateArrowOrientation(currentAzimuth);
                    mapView.getMap().move(
                            new CameraPosition(mapView.getMap().getCameraPosition().getTarget(),
                                    mapView.getMap().getCameraPosition().getZoom(),
                                    0.0f,
                                    0.0f),
                            new Animation(Animation.Type.SMOOTH, 0.3f),
                            null);
                }
            });


            //Ориентация
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


            hideRoadButton = findViewById(R.id.hideRoad);
            hideRoadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleRouteVisibility();
                }
            });

        }
        catch (Exception e) {
            showToast(e.getMessage());
        }

    }

    private boolean isCameraAtCurrentLocation(Point target) {
        if (currentLocation == null) return false;

        double radius = 10; // радиус в метрах
        double distance = calculateDistance(target, currentLocation);
        return distance <= radius;
    }

    private double calculateDistance(Point point1, Point point2) {
        double lat1 = Math.toRadians(point1.getLatitude());
        double lon1 = Math.toRadians(point1.getLongitude());
        double lat2 = Math.toRadians(point2.getLatitude());
        double lon2 = Math.toRadians(point2.getLongitude());

        double earthRadius = 6371000; // радиус Земли в метрах
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }


    // ЧЕКИН
    private boolean isUserNearAnyPoint() {
        if (currentLocation == null) return false;

        for (Point point : points) {
            double distance = getDistance(point);
            if (distance <= 80) {
                return true;
            }
        }
        return false;
    }

    private double getDistance(Point point) {
        if (currentLocation == null) return Double.MAX_VALUE;

        double lat1 = Math.toRadians(currentLocation.getLatitude());
        double lon1 = Math.toRadians(currentLocation.getLongitude());
        double lat2 = Math.toRadians(point.getLatitude());
        double lon2 = Math.toRadians(point.getLongitude());

        double earthRadius = 6371000; //meters
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c;
    }

    // ПЕРЕОТРИСОВКА КАРТИНОК ПОД СТАНЦИЮ
    private Bitmap[] iconsForStations(int img) {
        Bitmap[] mapImg = new Bitmap[4];
        mapImg[0] = createBitmapIcon(img, 100, 120);
        mapImg[1] = createActiveBitmapIcon(img, 130, 150);
        mapImg[2] = createDisabledBitmapIcon(img, 100, 120);
        mapImg[3] = createCompletedBitmapIcon(img, 100, 120);

        return mapImg;
    }


    private Point calculateNewTarget(float azimuth) {
        // Определяем шаг для перемещения точки (например, в этом примере используем шаг 0.001 градуса)
        double step = 0.001;

        // Получаем текущий угол азимута стрелки
        float currentArrowAzimuth = placemarkA.getDirection();


        // Вычисляем новую широту и долготу на основе текущей позиции и азимута
        double currentLat = mapView.getMap().getCameraPosition().getTarget().getLatitude();
        double currentLon = mapView.getMap().getCameraPosition().getTarget().getLongitude();

        double newLat = currentLat + step * Math.cos(Math.toRadians(currentArrowAzimuth));
        double newLon = currentLon + step * Math.sin(Math.toRadians(currentArrowAzimuth));

        return new Point(newLat, newLon);
    }

    private void moveMapToNewTarget(Point newTarget) {
        // Перемещение карты к новой целевой точке
        mapView.getMap().move(
                new CameraPosition(newTarget, mapView.getMap().getCameraPosition().getZoom(), 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null);
    }




    private void setPoints() {
        double[][] coordinates = {
                {57.74468688214571, 40.92947085805629}, {57.74641947101965, 40.928629731376105},
                {57.74879346993156, 40.916625737926175}, {57.753041044724526, 40.91279283213954},
                {57.75284520494153, 40.91086895601833}, {57.75641684358582, 40.899206921381875},
                //{57.73569179708343,40.9198924933021}
        };
        for (int i = 0; i < coordinates.length; i++) {
            Point point = new Point(coordinates[i][0], coordinates[i][1]);
            points.add(point);
        }
    }

    private void createLandmarks(boolean addPlacemarksBool) {
        if (!addPlacemarksBool)
            setPoints();
        for (int i = 0; i < points.size(); i++) {
            Bitmap[] icons = iconsForStations(imgOfStations.get(i));
            PlacemarkMapObject placemark = mapObjects.addPlacemark(points.get(i));
            placemark.setIcon(ImageProvider.fromBitmap(icons[0]), new IconStyle().setAnchor(new PointF(0.5f, 1.0f)));
            placemark.addTapListener(placemarkTapListener);
            placemarks.add(placemark);
        }
    }


    private final MapObjectTapListener placemarkTapListener = (mapObject, point) -> {
        try {
            if (mapObject instanceof PlacemarkMapObject) {
                Intent intent;
                int index = placemarks.indexOf(mapObject);
                if (index > Integer.parseInt(deserializeText("BeforeHero", "BeforeHero", MainActivity.this))) {
                    serializeText(String.valueOf(index), "BeforeHero", "BeforeHero");
                    intent = new Intent(MainActivity.this, BeforeStationMeetActivity.class);
                }
                else {
                    intent = new Intent(MainActivity.this, StationMenuActivity.class);
                }
                serializeText(index + "", "serIndexStation", "index");
                serializeText(index + "", "serIndexStation", "serIndexStation"+String.valueOf(index));
                startActivity(intent);
                updatePlacemarks();
            }
            return true;
        }
        catch (Exception e) {
            showToast(e.getMessage());
        }
        return true;
    };


    private void updatePlacemarks() {

        for (int i = 0; i < placemarks.size(); i++) {
            PlacemarkMapObject placemark = placemarks.get(i);
            IconStyle iconStyle = new IconStyle().setAnchor(new PointF(0.5f, 0.96f));
            Bitmap[] icons = iconsForStations(imgOfStations.get(i));

            placemark.setIcon(ImageProvider.fromBitmap(icons[2]), iconStyle);  // Будущая
            placemark.setZIndex(0f);
            placemark.removeTapListener(placemarkTapListener);

        }

        int countStation = Integer.parseInt(deserializeText("serComplateStation", "serComplateStation", MainActivity.this));
        if (countStation == 5)
        {
            complateRoute = true;
        }
        else
            countStation += 1;



        for (int i = 0; i <= countStation; i++) {
            PlacemarkMapObject placemark = placemarks.get(i);
            IconStyle iconStyle = new IconStyle().setAnchor(new PointF(0.5f, 0.96f));

            Bitmap[] icons = iconsForStations(imgOfStations.get(i));

            if (i == countStation  && !complateRoute) {
                placemark.setIcon(ImageProvider.fromBitmap(icons[1]), iconStyle);  // Активная
                nextPoint = placemark.getGeometry();
                placemark.setZIndex(1f);
                placemark.addTapListener(placemarkTapListener);
            } else {

                placemark.setIcon(ImageProvider.fromBitmap(icons[3]), iconStyle);  // Пройденная
                placemark.setZIndex(0f);
                placemark.addTapListener(placemarkTapListener);
            }
        }
    }

    private void freezeScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void unfreezeScreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    // ПРИБЛИЖЕНИЕ К МАРШРУТУ
    private void moveCameraToLocation(Point location) {
        freezeScreen();
        mapView.getMap().move(
                new CameraPosition(location, 15.0f, 250.0f, 0.0f),
                new com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, 3),
                new Map.CameraCallback() {
                    @Override
                    public void onMoveFinished(boolean completed) {
                        if (completed) {

                            initialZoomCompleted = true;

                            // После построения маршрута, переходим к текущему местоположению
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    unfreezeScreen();
                                    buildRouteForLandmarks();
                                    if(!complateRoute)
                                        updateRouteToNextPoint();
                                    //moveToCurrentLocation();
                                }
                            }, 1000); // Задержка в 1 секунду перед переходом к текущему местоположению
                        }
                    }
                }
        );
    }

    private void buildRouteForLandmarks() {
        List<RequestPoint> waypoints = new ArrayList<>();
        for (Point point : points) {
            waypoints.add(new RequestPoint(point, RequestPointType.WAYPOINT, null, null));
        }

        TransportFactory.getInstance().createPedestrianRouter().requestRoutes(
                waypoints, new TimeOptions(), false,
                new Session.RouteListener() {
                    @Override
                    public void onMasstransitRoutes(@NonNull List<Route> routes) {
                        if (!routes.isEmpty()) {
                            routePolyline = mapObjects.addPolyline(routes.get(0).getGeometry());
                            if(!complateRoute)
                                updateRouteToNextPoint();
                        }
                    }

                    @Override
                    public void onMasstransitRoutesError(@NonNull Error error) {
                        showToast("Error building route: " + error.toString());
                    }
                }
        );
    }

    private void updateRouteToNextPoint() {
        buildRoute();
    }


    //ОТОБРАЖЕНИЕ МАШРУТА ДО БЛИЖАЙЩЕЙ ТОЧКИ
    private void buildRoute() {
        try {
            List<RequestPoint> waypoints = new ArrayList<>();
            waypoints.add(new RequestPoint(currentLocation, RequestPointType.WAYPOINT, null, null));
            waypoints.add(new RequestPoint(nextPoint, RequestPointType.WAYPOINT, null, null));

            TransportFactory.getInstance().createPedestrianRouter().requestRoutes(
                    waypoints, new TimeOptions(), false,
                    new Session.RouteListener() {
                        @Override
                        public void onMasstransitRoutes(@NonNull List<Route> routes) {
                            if (!routes.isEmpty()) {
                                if (routePolylineNextPoint != null)
                                    mapObjects.remove(routePolylineNextPoint);

                                // Добавляем полилинию на карту
                                routePolylineNextPoint = mapObjects.addPolyline(routes.get(0).getGeometry());

                                // Изменяем цвет полилинии
                                routePolylineNextPoint.setStrokeColor(0xFF0bab09); // Устанавливаем зеленый цвет

                                // Можно также настроить другие свойства:
                                //routePolylineNextPoint.setStrokeWidth(5f); // Устанавливаем ширину линии
                                //routePolylineNextPoint.setOutlineColor(Color.BLACK); // Устанавливаем цвет обводки
                                //routePolylineNextPoint.setOutlineWidth(0.5f); // Устанавливаем ширину обводки
                            }
                        }

                        @Override
                        public void onMasstransitRoutesError(@NonNull Error error) {
                            showToast("Error building route: " + error.toString());
                        }
                    }
            );
        }
        catch (Exception e) {}
    }




    private void toggleRouteVisibility() {
        if (routePolyline != null) {
            if (routePolyline.isVisible()) {
                routePolyline.setVisible(false);
                hideRoadButton.setImageResource(R.drawable.all_routesdark);
                showToast("Маршрут скрыт");

            } else {
                routePolyline.setVisible(true);
                hideRoadButton.setImageResource(R.drawable.all_routes);
                showToast("Маршрут отображен");
            }
        }
    }


    private Bitmap createBitmapIcon(int drawableResId, int width, int height) {
        Drawable drawable = getResources().getDrawable(drawableResId);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    private Bitmap createActiveBitmapIcon(int drawableResId, int width, int height) {
        Bitmap originalBitmap = createBitmapIcon(drawableResId, width, height);
        Bitmap activeBitmap = Bitmap.createBitmap(width + 20, height + 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(activeBitmap);
        canvas.drawBitmap(originalBitmap, 10, 10, null);
        return activeBitmap;
    }
    private Bitmap createDisabledBitmapIcon(int drawableResId, int width, int height) {
        Bitmap originalBitmap = createBitmapIcon(drawableResId, width, height);
        Bitmap disabledBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(disabledBitmap);
        Paint paint = new Paint();
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(filter);
        paint.setAlpha(200);
        canvas.drawBitmap(originalBitmap, 0, 0, paint);
        return disabledBitmap;
    }

    private Bitmap createCompletedBitmapIcon(int drawableResId, int width, int height) {
        Bitmap originalBitmap = createBitmapIcon(drawableResId, width, height);
        Bitmap completedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(completedBitmap);
        Paint paint = new Paint();

        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[] {
                0.2f, 0, 0, 0, 0,
                0, 1f, 0, 0, 40,
                0, 0, 0.2f, 0, 0,
                0, 0, 0, 1, 0
        });
        paint.setAlpha(200);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        canvas.drawBitmap(originalBitmap, 0, 0, paint);

        return completedBitmap;
    }


    private void updatePlacemarkA(Point point) {
        if (placemarkA == null) {
            placemarkA = mapView.getMap().getMapObjects().addPlacemark(point);
            placemarkA.setIcon(ImageProvider.fromResource(this, R.drawable.strelka));


            IconStyle iconStyle = new IconStyle();
            iconStyle.setAnchor(new PointF(0.5f, 0.5f));
            iconStyle.setRotationType(RotationType.ROTATE);
            iconStyle.setZIndex(1f);
            iconStyle.setScale(1.0f);
            placemarkA.setIconStyle(iconStyle);
        } else {
            placemarkA.setGeometry(point);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, ModeMenuActivity.class);
        startActivity(intent);
    }



    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        try {
            super.onStart();
            MapKitFactory.getInstance().onStart();
            mapView.onStart();
            updatePlacemarks();

        }
        catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (rotationAnimator != null) {
            rotationAnimator.cancel();
        }

        // Остановка обновлений местоположения
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }

        // Освобождение ресурсов карты
        if (mapView != null) {
            mapView.onStop();
        }

        // Обнуление ссылок
        mapView = null;
        fusedLocationClient = null;
        locationCallback = null;

// Удаление слушателя камеры
        if (mapView != null && mapView.getMap() != null && cameraListener != null) {
            mapView.getMap().removeCameraListener(cameraListener);
        }
        super.onDestroy();

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        try {
            super.onResume();
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //startLocationUpdates();
                if (currentLocation != null) {
                    currentLocationMode = LocationMode.FOLLOW;
                    updateUIForLocationMode();
                }
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, YOUR_REQUEST_CODE);
            }
        }
        catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            sensorManager.unregisterListener(sensorEventListener);
            if (fusedLocationClient != null && locationCallback != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        }
        catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("intentM", intentM);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            intentM = savedInstanceState.getParcelable("intentM");
        }
    }

    //Чтение JSON
    //-------------------------------------------------------------------------------------------------------------------------------
    public String setMaps(int filejson) {
        InputStream is = getResources().openRawResource(R.raw.maps);
        byte[] buffer = new byte[0];
        try {
            buffer = new byte[is.available()];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            is.read(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        try {
            is.close();
            return json;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    //Местоположение
    //-------------------------------------------------------------------------------------------------------------------------------

    private void moveToCurrentLocation() {
        if (currentLocation != null) {
            mapView.getMap().move(
                    new CameraPosition(currentLocation, 18.0f, 0, 0),
                    new Animation(Animation.Type.SMOOTH, 1),
                    new Map.CameraCallback() {
                        @Override
                        public void onMoveFinished(boolean completed) {
                            if (completed) {
                                updateArrowOrientation(currentAzimuth);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    // Устанавливаем режим FOLLOW после центрирования
                                        currentLocationMode = LocationMode.FOLLOW;
                                        updateUIForLocationMode();
                                    }
                                });
                            }
                        }
                    }
            );
        } else {
            // Если местоположение еще не определено, ждем и пробуем снова
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveToCurrentLocation();
                }
            }, 1000);
        }
    }
    private void updateUIForLocationMode() {
        switch (currentLocationMode) {
            case NORMAL:
                locationButton.setImageResource(R.drawable.location_1);
                mapView.getMap().setRotateGesturesEnabled(true);
                mapView.getMap().setTiltGesturesEnabled(true);
                break;
            case FOLLOW:
                locationButton.setImageResource(R.drawable.location_2);
                mapView.getMap().setRotateGesturesEnabled(true);
                mapView.getMap().setTiltGesturesEnabled(true);
                moveToCurrentLocation();
                break;
        }
    }

    private void updateLocationOnMap(Location location) {
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            currentLocation = new Point(latitude, longitude);

            if (!complateRoute)
                updateRouteToNextPoint();

            updatePlacemarkA(currentLocation);

            if (currentLocationMode == LocationMode.FOLLOW) {
                moveToCurrentLocation();
            }


            if (!initialPositionSet) {
                initialPositionSet = true;
                currentLocationMode = LocationMode.FOLLOW;
                updateUIForLocationMode();
                moveToCurrentLocation();
            }
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(1000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateLocationOnMap(location);
                    if (currentLocationMode == LocationMode.FOLLOW) {
                        moveToCurrentLocation();
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "В разрешении отказано", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //-------------------------------------------------------------------------------------------------------------------------------


    //Компас
    //-------------------------------------------------------------------------------------------------------------------------------
    private void updateCompassRotation(float azimuth) {
        float targetRotation = -azimuth;

        // Убедимся, что поворот происходит по кратчайшему пути
        float rotationDifference = targetRotation - currentRotation;
        if (rotationDifference > 180) rotationDifference -= 360;
        if (rotationDifference < -180) rotationDifference += 360;

        final float finalTargetRotation = currentRotation + rotationDifference;

        rotationAnimator = ValueAnimator.ofFloat(currentRotation, finalTargetRotation);
        rotationAnimator.setDuration(250); // Длительность анимации в миллисекундах
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                if (compassImage != null) {
                    compassImage.setRotation(animatedValue);
                }
                currentRotation = animatedValue;
            }
        });
        rotationAnimator.start();
    }

    //Ориентация
    //-------------------------------------------------------------------------------------------------------------------------------
    private void updateArrowOrientation(float azimuth) {
        if (placemarkA != null) {
            placemarkA.setDirection(azimuth);
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor == accelerometer) {
                System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
                lastAccelerometerSet = true;
            } else if (event.sensor == magnetometer) {
                System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
                lastMagnetometerSet = true;
            }
            if (lastAccelerometerSet && lastMagnetometerSet) {
                SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer);
                SensorManager.getOrientation(rotationMatrix, orientation);
                float azimuthInRadians = orientation[0];
                float azimuthInDegrees = (float) Math.toDegrees(azimuthInRadians);

                // Нормализуем в диапазоне [0, 360)
                float correctedAzimuth = (azimuthInDegrees + 360) % 360;

                // Применяем фильтр низких частот
                filteredAzimuth = ALPHA * correctedAzimuth + (1 - ALPHA) * filteredAzimuth;

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastUpdateTime > UPDATE_INTERVAL_MS) {
                    currentAzimuth = filteredAzimuth;
                    updateArrowOrientation(currentAzimuth);
                    lastUpdateTime = currentTime;
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Не требуется реализация
        }
    };
    //-------------------------------------------------------------------------------------------------------------------------------
}
