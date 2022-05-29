package com.oneparchy.doggietinder.fragments

import android.annotation.SuppressLint
import android.location.*
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.LatLng
import com.oneparchy.doggietinder.PostAdapter
import com.oneparchy.doggietinder.R
import com.oneparchy.doggietinder.models.Post
import com.parse.*
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


open class FeedFragment : Fragment(), EasyPermissions.PermissionCallbacks, AdapterView.OnItemSelectedListener {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currLocation: Location? = null

    private val UPDATE_INTERVAL = (5 * 60 * 1000 /* 5 mins */).toLong()
    private val FASTEST_INTERVAL: Long = 180000 /* 3 mins */
    private var miles: Double = 5.0 // Default value of 5.0 miles for filtering posts

    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var rvPosts: RecyclerView
    lateinit var adapter: PostAdapter
    var allPosts: MutableList<Post> = mutableListOf()
    lateinit var autoCompleteTextView: AutoCompleteTextView

    companion object {
        private const val TAG="FeedFragment"
        const val PERMISSION_LOCATION_REQUEST_CODE = 8
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Following is to attach adapter to recycler view and set up layout manager */
        rvPosts = view.findViewById(R.id.rvPosts)
        adapter = PostAdapter(requireContext(), allPosts)
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(requireContext())

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        /* Followings is for swipe to refresh */
        swipeContainer = view.findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i(TAG,"Refreshing Feed")
            queryPosts()
        }
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        /* Following is for creating miles dropdown menu. */
        val miles = resources.getStringArray(R.array.miles_array)
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, miles)
        autoCompleteTextView.setAdapter(arrayAdapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            if (position == 0){
                this@FeedFragment.miles = 5.0
                queryPosts()
                Log.d(TAG, "Filter changed to 5 miles")
            } else if (position == 1) {
                this@FeedFragment.miles = 10.0
                queryPosts()
                Log.d(TAG, "Filter changed to 10 miles")
            } else if (position == 2) {
                this@FeedFragment.miles = 15.0
                queryPosts()
                Log.d(TAG, "Filter changed to 15 miles")
            } else {
                this@FeedFragment.miles = 500.0
                queryPosts()
                Log.d(TAG, "Filter changed to 500 miles")
            }
        }

        /* To check whether post belongs to current user, then show "Found button" */



        /* To check whether we have granted permission to access GPS */
        /* Request for accessing GPS if permission not granted */
        if (hasLocationPermission()){
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                currLocation = location
                val geoCoder = Geocoder(requireContext())
                val currentLocation = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                if (location != null) {
                    Toast.makeText(requireContext(), "Location set to " +
                            currentLocation.first().locality + ", "+
                            currentLocation.first().countryCode, Toast.LENGTH_SHORT).show()
                    startLocationUpdates()
                    queryPosts()
                }
            }
        } else {
            requestLocationService()
        }
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
        val settingsClient = LocationServices.getSettingsClient(requireContext())
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        LocationServices.getFusedLocationProviderClient(requireContext()).requestLocationUpdates(
            mLocationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    // do work here
                    onLocationChanged(locationResult.lastLocation)
                }
            },
            Looper.myLooper()
        )
    }

    fun onLocationChanged(location: Location) {
        // New location has now been determined
        currLocation = location
        val msg = "Updated Location: " +
                location.latitude.toString() + "," +
                location.longitude.toString()
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        queryPosts()
    }

    // Returns true if user grants permission for using the GPS.
    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
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
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationService()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(requireContext(), "Location Access Granted!!!", Toast.LENGTH_SHORT).show()
    }

    open fun queryPosts() {
        //Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Find all the Post objects (including author)
        query.include(Post.KEY_USR)
        //return only the 10 latest posts, from newest to oldest
        query.limit = 10
        query.addDescendingOrder("createdAt")
        // returns posts created within default value of 5 miles for current location.
        query.whereWithinMiles(
            "location",
            ParseGeoPoint(currLocation!!.latitude, currLocation!!.longitude),
            miles
        )
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error retrieving posts")
                } else {
                    Log.i(TAG, "Posts query successful")
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(TAG, "Post: " + post.getDescription() +
                                    ", User: " + post.getUser()?.username +
                                    ", Location: " + post.getLocation().toString() +
                                    ", City: " + post.getCity() )
                        }
                        allPosts.clear()
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                        swipeContainer.isRefreshing = false
                    }
                }
            }
        } )

    }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
            if (parent != null) {
                // miles = parent.getItemAtPosition(pos) as Double
                //queryPosts()
                Log.i(TAG, parent.getItemAtPosition(pos) as String)
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
}
