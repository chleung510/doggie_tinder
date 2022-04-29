package com.oneparchy.doggietinder.fragments

import android.util.Log
import com.oneparchy.doggietinder.models.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: FeedFragment() {

    companion object {
        private const val TAG = "ProfileFragment"
    }

    override fun queryPosts() {
        //Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Find all the Post objects (including author)
        query.include(Post.KEY_USR)
        //return only posts made by the currently signed in user
        query.whereEqualTo(Post.KEY_USR, ParseUser.getCurrentUser())
        //return only the 20 latest posts, from newest to oldest
        query.limit = 20
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
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                        swipeContainer.isRefreshing = false
                    }
                }
            }
        } )
    }
}