package com.example.fortest;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.fortest.API.WeatherAPI;
import com.example.fortest.GPS.LocListener;
import com.example.fortest.GPS.LocListenerInterface;
import com.example.fortest.GPS.LocationData;
import com.example.fortest.dataBase.DBHelper;
import com.example.fortest.drawing.Themes;
import com.example.fortest.drawing.view.DistView;
import com.example.fortest.drawing.view.MiniView;
import com.example.fortest.drawing.view.SpeedView;
import com.example.fortest.drawing.view.WeatherView;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LocListenerInterface {
    private final int LOCPERMCODE = 111;
    private LocationManager locationManager;
    private Location lastLocation;
    private LocListener locListener;
    private SpeedView vSpeed;
    private DistView vDist;
    private MiniView vAverageSpeed;
    private WeatherView vWeather;
    private long startTime;
    private float distanceForTime;
    private WeatherAPI weatherAPI;
    private CompositeDisposable disposable;
    private double lon;
    private double lat;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private View vMain;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();
    }

    private void init(){

        DBHelper dbHelper = new DBHelper(MainActivity.this);
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_NAME,
                null, null, null, null, null,null);
        if (cursor.moveToFirst()){

            int valueInd = cursor.getColumnIndex(DBHelper.VALUE);

            DisplayParameters.autoHud = (cursor.getInt(valueInd) == 1);

            if (cursor.moveToNext())
                DisplayParameters.displayAnalog = (cursor.getInt(valueInd) == 1);

            if (cursor.moveToNext())
                DisplayParameters.displayMiles = (cursor.getInt(valueInd) == 1);

            if (cursor.moveToNext()) {
                switch (cursor.getInt(valueInd)) {
                    case 1:
                        Themes.mainThemeColor = Themes.elementsColorOrange;
                        break;
                    case 2:
                        Themes.mainThemeColor = Themes.elementsColorWhite;
                        break;
                    case 3:
                        Themes.mainThemeColor = Themes.elementsColorRed;
                        break;
                }
            }

        }
        cursor.close();

        vMain = findViewById(R.id.mainPart);

        startTime = System.currentTimeMillis();
        disposable = new CompositeDisposable();

        View tools = findViewById(R.id.tools_btn);
        vSpeed = findViewById(R.id.speedView);
        vDist = findViewById(R.id.distanceView);
        View vReset = findViewById(R.id.resetView);
        vAverageSpeed = findViewById(R.id.averageSpeedView);
        vWeather = findViewById(R.id.weatherView);

        ActivityResultLauncher<Intent> settingsActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_CANCELED){
                        ContentValues newValue = new ContentValues();

                        newValue.put(DBHelper.VALUE, DisplayParameters.autoHud ? 1: 0);
                        String where = DBHelper.KEY_ID + "=1";
                        database.update(DBHelper.TABLE_NAME, newValue, where, null);

                        newValue.clear();
                        newValue.put(DBHelper.VALUE, DisplayParameters.displayAnalog? 1: 0);
                        where = DBHelper.KEY_ID + "=2";
                        database.update(DBHelper.TABLE_NAME, newValue, where, null);

                        newValue.clear();
                        newValue.put(DBHelper.VALUE, DisplayParameters.displayMiles? 1: 0);
                        where = DBHelper.KEY_ID + "=3";
                        database.update(DBHelper.TABLE_NAME, newValue, where, null);

                        if (Themes.mainThemeColor.equals(Themes.elementsColorOrange)){
                            newValue.clear();
                            newValue.put(DBHelper.VALUE, 1);
                            where = DBHelper.KEY_ID + "=4";
                            database.update(DBHelper.TABLE_NAME, newValue, where, null);
                        } else if (Themes.mainThemeColor.equals(Themes.elementsColorWhite)){
                            newValue.clear();
                            newValue.put(DBHelper.VALUE, 2);
                            where = DBHelper.KEY_ID + "=4";
                            database.update(DBHelper.TABLE_NAME, newValue, where, null);
                        } else if (Themes.mainThemeColor.equals(Themes.elementsColorRed)){
                            newValue.clear();
                            newValue.put(DBHelper.VALUE, 3);
                            where = DBHelper.KEY_ID + "=4";
                            database.update(DBHelper.TABLE_NAME, newValue, where, null);
                        }


                        refreshViews();

                        if (DisplayParameters.displayHud)
                            vMain.animate().rotation(180f);
                        else
                            vMain.animate().rotation(0f);
                    }
                });

        tools.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            settingsActivityLauncher.launch(intent);
        });

        vReset.setOnClickListener(v -> {
            lastLocation = null;
            LocationData.resetDistance();
            LocationData.resetAvrSpeed();
            refreshViews();
            startTime = System.currentTimeMillis();

        });

        vWeather.setOnClickListener(v ->
        {
            setTemp();
            refreshViews();
        });

        String BASE_URL = "http://api.openweathermap.org";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        weatherAPI = retrofit.create(WeatherAPI.class);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        locListener = new LocListener();
        locListener.setLocListenerInterface(this);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                float[] rotMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotMatrix, event.values);
                float[] newRotMatrix = new float[16];
                SensorManager.remapCoordinateSystem(
                        rotMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        newRotMatrix);

                float[] orientations = new float[3];
                SensorManager.getOrientation(newRotMatrix, orientations);
                for (int i = 0; i < 3 ; i++) {
                    orientations[i] = (float)(Math.toDegrees(orientations[i]));
                }

                int x = (int)orientations[1];
                if (x >= 80 && !DisplayParameters.displayHud ){
                    DisplayParameters.displayHud = true;
                    vMain.animate().rotation(180f);
                    refreshViews();
                } else if (x <= 40 && DisplayParameters.displayHud ){
                    DisplayParameters.displayHud = false;
                    vMain.animate().rotation(0f);
                    refreshViews();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        checkPermissions();
        setTemp();
        refreshViews();
    }

    @Override
    public void onLocationChanged(Location location) {
        long millis = System.currentTimeMillis() - startTime;

        LocationData.speed = location.getSpeed();

        if (location.hasSpeed() && LocationData.speed > (1.5f / 3.6f) && lastLocation != null) {
            LocationData.distance += lastLocation.distanceTo(location);
            distanceForTime += lastLocation.distanceTo(location);
        }

        if (millis >= 60000 * 10f) {
            LocationData.avrSpeed = distanceForTime / (int) (millis / 1000); // met/h
            startTime = System.currentTimeMillis();
            distanceForTime = 0;
        }

        lastLocation = location;

        refreshViews();

        lat = location.getLatitude();
        lon = location.getLongitude();
    }

    public void refreshViews(){

        final String m = "m";
        final String km = "km";
        final String kmh = "km/h";
        final String mh = "m/h";

        if (DisplayParameters.displayMiles){
            vSpeed.setText(mh);
            vAverageSpeed.setText(mh);
            vDist.setText(m);
        } else {
            vSpeed.setText(kmh);
            vAverageSpeed.setText(kmh);
            vDist.setText(km);
        }

        vSpeed.setNum(LocationData.getSpeed());
        vSpeed.invalidate();

        vDist.setNum(LocationData.getDistance());
        vDist.invalidate();

        vAverageSpeed.setNum(LocationData.getAvrSpeed());
        vAverageSpeed.invalidate();

        vWeather.invalidate();
    }

    private void checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCPERMCODE);
        } else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            try {
                lat = location.getLatitude();
                lon = location.getLongitude();
            } catch (Exception e){
                Log.e("LAT, LOG EXP", e.getMessage());
                Toast.makeText(MainActivity.this, "Не удается получить координаты", Toast.LENGTH_LONG).show();
            }
            setTemp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCPERMCODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            checkPermissions();
        } else
            Toast.makeText(this, "Разрешите использовать ваше местоположение", Toast.LENGTH_LONG).show();
    }

    public void setTemp(){
        if (lat != 0 && lon != 0){
            String apikey = "b0c5e3da90a7d3a34cfd95752144c206";
            disposable.add(weatherAPI.getWeather(lat, lon, apikey)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((responseData, throwable) -> {
                        if (responseData == null){
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            LocationData.temp = Math.round(responseData.getMain().getTemp() - 273);
                            LocationData.description = responseData.getWeather().get(0).getDescription();
                            vWeather.setWeather(LocationData.temp, LocationData.description );
                            vWeather.invalidate();
                            Toast.makeText(MainActivity.this, "Погода обновлена", Toast.LENGTH_SHORT).show();
                        }
                    }));

        } else
            Toast.makeText(MainActivity.this, "Не удается получить координаты для погоды", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DisplayParameters.autoHud){
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }
}

