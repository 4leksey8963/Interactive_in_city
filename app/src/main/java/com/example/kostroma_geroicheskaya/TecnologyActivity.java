package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;
import java.util.List;

public class TecnologyActivity extends BaseActivity {

    String fromQR = "";
    String fromLink = "";
    private static List<Point> points = new ArrayList<>();
    private Point currentLocation;
    private Handler handler = new Handler();
    private Runnable locationRunnable;
    String serialisetext;
    Button locationButton;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tecnology);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setStatusBarColor(R.color.darkGreen);
        Button back = findViewById(R.id.Back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(TecnologyActivity.this, StationMenuActivity.class);
            startActivity(intent);
        });
        setPoints();
        startLocationUpdates();

        // кнопки работы с активностями Егора
        Button cameraButton = findViewById(R.id.CameraButton);
        ImageView cameraImage = findViewById(R.id.CameraImage);
        cameraButton.setOnClickListener(v -> {
            int indexOfStation = 0;
            try {
                serialisetext = deserializeText("serIndexStation", "index", TecnologyActivity.this);
                indexOfStation = Integer.parseInt(deserializeText("serIndexStation", "serIndexStation" + serialisetext, TecnologyActivity.this));
                switch (indexOfStation) {
                    case (0):
                        fromLink = getString(R.string.pantusovo_link);
                        break;
                    case (1):
                        fromLink = getString(R.string.ermakov_link);
                        break;
                    case (2):
                        fromLink = getString(R.string.golubkov_link);
                        break;
                    case (3):
                        fromLink = getString(R.string.pobedimskiy_link);
                        break;
                    case (4):
                        fromLink = getString(R.string.belenogov_link);
                        break;
                    case (5):
                        fromLink = getString(R.string.tank_link);
                        break;
                    default:
                        Toast.makeText(this, "Не удалось получить информацию о монументе", Toast.LENGTH_LONG).show();
                        return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Не удалось получить информацию о монументе", Toast.LENGTH_LONG).show();
                return;
            }
            fromQR = "";
            IntentIntegrator intentIntegrator = new IntentIntegrator(TecnologyActivity.this);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setPrompt("Просканируйте QR-код");
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
            intentIntegrator.initiateScan();
        });


        // кнопки работы с активностями Андрея и Лёши
        locationButton = findViewById(R.id.LocationButton);
        ImageView locationImage = findViewById(R.id.LocationImage);

        locationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getLastKnownLocation();
                if (currentLocation == null) {
                    Toast.makeText(getApplicationContext(), "Местоположение не определено", Toast.LENGTH_SHORT).show();
                } else if (isUserNearAnyPoint()) {
                    Toast.makeText(getApplicationContext(), "Удачно", Toast.LENGTH_SHORT).show();
                    serialisetext = deserializeText("serIndexStation", "index", TecnologyActivity.this);
                    if (!checkSerializedKeyExistence("station", "checkin" + serialisetext, TecnologyActivity.this)) {
                        serializeText("station", "station", "checkin" + serialisetext);
                        Integer temp = Integer.parseInt(deserializeText("progress_rewards", "progress_rewards", TecnologyActivity.this)) + 5;
                        serializeText(String.valueOf(temp), "progress_rewards", "progress_rewards");
                    }
                    Intent intent = new Intent(TecnologyActivity.this, StationMenuActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Вне радиуса меток", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {

            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (intentResult != null) {
                fromQR = intentResult.getContents();
                if (fromQR != null) {
                    if (fromQR.equals(fromLink)) {
                        Toast.makeText(getApplicationContext(), "Удачно", Toast.LENGTH_LONG).show();
                        if (!checkSerializedKeyExistence("station", "checkin" + serialisetext, TecnologyActivity.this)) {
                            serializeText("station", "station", "checkin" + serialisetext);
                            Integer temp = Integer.parseInt(deserializeText("progress_rewards", "progress_rewards", TecnologyActivity.this)) + 5;
                            serializeText(String.valueOf(temp), "progress_rewards", "progress_rewards");
                        }
                        Intent intent = new Intent(TecnologyActivity.this, StationMenuActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Не тот QR-код", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(TecnologyActivity.this, StationMenuActivity.class);
                        startActivity(intent);
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        } catch (Exception e) {
        }
    }

    // ЧЕКИН
    private void startLocationUpdates() {
        locationRunnable = new Runnable() {
            @Override
            public void run() {
                getLastKnownLocation();
                handler.postDelayed(this, 5000); // Повторять каждые 5 секунд
            }
        };
        handler.post(locationRunnable);
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = new Point(location.getLatitude(), location.getLongitude());
                            // Здесь вы можете использовать currentLocation
                            // Например, обновить UI или выполнить какие-то действия
                        } else {
                            Toast.makeText(getApplicationContext(), "Местоположение недоступно", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Местоположение не определено", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(locationRunnable);
    }

    private boolean isUserNearAnyPoint() {
        if (currentLocation == null) return false;
        int indexStation = Integer.parseInt(deserializeText("serIndexStation", "index", TecnologyActivity.this));
        double distance = getDistance(points.get(indexStation));
        if (distance <= 80) {
            return true;
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
    private void setPoints() {
        double[][] coordinates = {
                {57.74468688214571, 40.92947085805629}, {57.74641947101965, 40.928629731376105},
                {57.74879346993156, 40.916625737926175}, {57.753041044724526, 40.91279283213954},
                {57.75284520494153, 40.91086895601833}, {57.75641684358582, 40.899206921381875},
                //{57.7369430951736, 40.920482988011905}
                //{57.73569179708343,40.9198924933021}(ДОБАВЬ СВОЕ МЕСТОПОЛОЖЕНИЕ С КАРТ, ЧТОБЫ ПРОВЕРИТЬ ЧЕКИН)
        };
        for (int i = 0; i < coordinates.length; i++) {
            Point point = new Point(coordinates[i][0], coordinates[i][1]);
            points.add(point);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TecnologyActivity.this, StationMenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(locationRunnable);
        super.onStop();
    }
}