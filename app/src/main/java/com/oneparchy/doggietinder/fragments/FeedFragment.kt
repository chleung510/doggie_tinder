package com.oneparchy.doggietinder.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.oneparchy.doggietinder.PostAdapter
import com.oneparchy.doggietinder.R
import com.oneparchy.doggietinder.models.Post
import com.parse.*


open class FeedFragment : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var swipeContainer: SwipeRefreshLayout
    lateinit var rvPosts: RecyclerView
    lateinit var adapter: PostAdapter
    var allPosts: MutableList<Post> = mutableListOf()
    lateinit var autoCompleteTextView: AutoCompleteTextView
    private var miles: Double = 5.0 // Default value of 5.0 miles for filtering posts
    private var custMiles: Double = 0.0
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0


    companion object {
        private const val TAG="FeedFragment"
        const val PERMISSION_LOCATION_REQUEST_CODE = 8
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentLat = arguments?.getDouble("CurrentLat")!!
        currentLong = arguments?.getDouble("CurrentLong")!!

        Log.i(TAG, "currentLat: " + currentLat)
        Log.i(TAG, "currentLong: " + currentLong)
        custMiles = arguments?.getDouble("key")!!
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
        // Inflate the layout for this fragment




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

        if (custMiles > 0.0){
            currentLat = arguments?.getDouble("CurrentLat")!!
            currentLong = arguments?.getDouble("CurrentLong")!!
            Log.i(TAG, "currentLat: " + currentLat)
            Log.i(TAG, "currentLong: " + currentLong)
            this@FeedFragment.miles = custMiles
            Log.i(TAG, "Miles: " + this@FeedFragment.miles)
            queryPosts()
        }
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
        queryPosts()
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
            ParseGeoPoint(currentLat, currentLong),
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
                                    ", City: " + post.getCity()+
                                    ", Time created: " + post.getFormattedTimestamp())
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
