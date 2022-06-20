package com.oneparchy.doggietinder.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.oneparchy.doggietinder.PostAdapter
import com.oneparchy.doggietinder.R
import com.oneparchy.doggietinder.TimeFormatter
import com.oneparchy.doggietinder.models.Post
import com.parse.*

class ProfileFragment: FeedFragment() {

    private var miles: Double = 5.0 // Default value of 5.0 miles for filtering posts
    private var custMiles: Double = 0.0
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private lateinit var iv_user: ImageView
    private lateinit var tv_userName: TextView
    private lateinit var tv_description: TextView
    private lateinit var tv_createdAt: TextView

    companion object {
        private const val TAG = "ProfileFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Following is user profile */
        iv_user = view.findViewById(R.id.iv_user)
        tv_userName = view.findViewById(R.id.tv_userName)
        tv_description = view.findViewById(R.id.tv_description)
        tv_createdAt = view.findViewById(R.id.tv_createdAt)

        tv_userName.text = ParseUser.getCurrentUser().username
        //tv_description.text = ParseUser.getCurrentUser().get
        tv_createdAt.text = "Joined since " + TimeFormatter.getTimeStamp(ParseUser.getCurrentUser().createdAt.toString())
        Glide.with(view.context).load(ParseUser.getCurrentUser().getParseFile("caption")?.url).centerCrop()
            .transform(CircleCrop()).into(iv_user)

        /* Following is to attach adapter to recycler view and set up layout manager */
        rvPosts = view.findViewById(R.id.rvPosts)
        adapter = PostAdapter(requireContext(), allPosts)
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(requireContext())

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
            this@ProfileFragment.miles = custMiles
            Log.i(TAG, "Miles: " + this@ProfileFragment.miles)
            queryPosts()
        }
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            if (position == 0){
                this@ProfileFragment.miles = 5.0
                queryPosts()
                Log.d(TAG, "Filter changed to 5 miles")
            } else if (position == 1) {
                this@ProfileFragment.miles = 10.0
                queryPosts()
                Log.d(TAG, "Filter changed to 10 miles")
            } else if (position == 2) {
                this@ProfileFragment.miles = 15.0
                queryPosts()
                Log.d(TAG, "Filter changed to 15 miles")
            } else {
                this@ProfileFragment.miles = 500.0
                queryPosts()
                Log.d(TAG, "Filter changed to 500 miles")
            }
        }
        queryPosts()
    }
    override fun queryPosts() {
        //Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Find all the Post objects (including author)
        query.include(Post.KEY_USR)
        //return only posts made by the currently signed in user
        query.whereEqualTo(Post.KEY_USR, ParseUser.getCurrentUser())
        //return only the 20 latest posts, from newest to oldest
        query.limit = 10
        query.addDescendingOrder("createdAt")
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error retrieving posts")
                } else {
                    Log.i(TAG, "Posts query successful")
                    if (posts != null) {
                        for (post in posts) {
                            Log.i(TAG, "Post: " + post.getDescription() + ", User: " + post.getUser()?.username)
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
}