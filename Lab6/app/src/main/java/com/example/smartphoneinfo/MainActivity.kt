package com.example.smartphoneinfo

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.HardwarePropertiesManager
import android.os.HardwarePropertiesManager.DEVICE_TEMPERATURE_CPU
import android.os.HardwarePropertiesManager.TEMPERATURE_CURRENT
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var locationManager : LocationManager? = null

    private var lightSensor : Sensor? = null
    private var sensorManager : SensorManager? = null

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        lightingTextView.text = "Lighting sensor: " + event!!.values[0]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1000)
        }

        // Create persistent LocationManager reference
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        try {
            // Request location updates
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch(ex: SecurityException) {
            Log.d("myTag", "Security Exception, no location available")
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        lightSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            coordinatesTextView.text = "GPS coordinates: " + location.longitude + " : " + location.latitude
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onResume() {
        super.onResume()

        sensorManager!!.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        sensorManager!!.unregisterListener(this)
    }

    override fun onStop() {
        super.onStop()
        //TODO show notification
    }
}
