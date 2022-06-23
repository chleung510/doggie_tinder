package com.oneparchy.doggietinder

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.oneparchy.doggietinder.fragments.ComposeFragment
import com.oneparchy.doggietinder.fragments.FeedFragment
import com.oneparchy.doggietinder.fragments.ProfileFragment
import com.parse.ParseUser
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currLocation: Location? = null

    private val UPDATE_INTERVAL = (5 * 60 * 1000 /* 5 mins */).toLong()
    private val FASTEST_INTERVAL: Long = 180000 /* 3 mins */

    private lateinit var fragmentManager: FragmentManager

    companion object {
        private const val TAG="MainActivity"
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.pseudo_app_name)

        fragmentManager = supportFragmentManager
        val bottom_Navigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        /* To check whether we have granted permission to access GPS */
        /* Request for accessing GPS if permission not granted */
        if (hasLocationPermission()){
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                currLocation = location

                bottom_Navigation.setOnItemSelectedListener {
                    //instantiate variable passed in as item (default = "it")
                        item ->
                    var fragmentToShow: Fragment? = null

                    when (item.itemId) {
                        R.id.actionHome -> {
                            fragmentToShow = FeedFragment()
                        }
                        R.id.actionCompose -> {
                            fragmentToShow = ComposeFragment()
                        }
                        R.id.actionProfile -> {
                            fragmentToShow = ProfileFragment()
                        }
                    }

                    if (fragmentToShow != null) {
                        val args = Bundle()
                        args.putString("key", "abc")

                            args.putDouble("CurrentLat", currLocation!!.latitude)
                            args.putDouble("CurrentLong", currLocation!!.longitude)

                        fragmentToShow.setArguments(args)

                        try {
                            fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
                        } catch (e: IllegalStateException) {
                            fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commitAllowingStateLoss()
                            Log.d(TAG, "Exception", e)
                        }
                    }
                    //Return true to signify that we have handled this user interaction
                    true
                }

                // Set default fragment selection
                bottom_Navigation.selectedItemId = R.id.actionHome

                val geoCoder = Geocoder(this)
                val currentLocation = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                if (location != null) {
                    Toast.makeText(this, "Location set to " +
                            currentLocation.first().locality + ", "+
                            currentLocation.first().countryCode, Toast.LENGTH_SHORT).show()
                    startLocationUpdates()
                }
            }
        } else {
            requestLocationService()
        }


    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined
        currLocation = location
        val msg = "Updated Location: " +
                location.latitude.toString() + "," +
                location.longitude.toString()
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // Returns true if user grants permission for using the GPS.
    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    // To request permission for using GPS
    private fun requestLocationService() {
        EasyPermissions.requestPermissions(
            this,
            "This app will not work without location service",
            FeedFragment.PERMISSION_LOCATION_REQUEST_CODE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    // For handling runtime result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        // if permission is not granted by user, show setting menu
        if (EasyPermissions.somePermissionDenied(this, perms.first())){
            SettingsDialog.Builder(this).build().show()
        } else {
            requestLocationService()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(this, "Location Access Granted!!!", Toast.LENGTH_SHORT).show()
    }

    //Inflate menu options and tie to this activity
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)

        return true
    }

    //Handle clicks on menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miLogout) {
            ParseUser.logOutInBackground()
            goToLoginActivity()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //Go to Login activity
    private fun goToLoginActivity() {
        val i = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    // Trigger new location updates at interval
    @SuppressLint("MissingPermission")
    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(UPDATE_INTERVAL)
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(
            mLocationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    // do work here
                    onLocationChanged(locationResult.lastLocation)
                }
            },
            Looper.myLooper()
        )
    }
}