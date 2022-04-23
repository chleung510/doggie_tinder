package com.oneparchy.doggietinder.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oneparchy.doggietinder.MainActivity
import com.oneparchy.doggietinder.PostAdapter
import com.oneparchy.doggietinder.R
import com.oneparchy.doggietinder.models.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


class FeedFragment : Fragment() {

    lateinit var postsRecyclerView: RecyclerView
    lateinit var adapter: PostAdapter
    var allPosts : MutableList<Post>  = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Where we set up views and listeners...

        postsRecyclerView = view.findViewById(R.id.postRecyclerView)

        // Setup adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts)
        postsRecyclerView.adapter = adapter

        // Setting up layout Manager on RecyclerView
        postsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    //For query all posts in Parse server
    fun queryPosts(){

        //specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        //To find all post objects
        query.include(Post.KEY_DOGNAME)
        query.include(Post.KEY_AGE)
        query.include(Post.KEY_BREED)
        //Returns Post in descending order
        query.addDescendingOrder("createdAt")

        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e(MainActivity.TAG, "Error in fetching posts")
                } else {
                    if (posts != null){
                        for (post in posts){
                            Log.i(
                                TAG, "Post: " + post.getDescription() +
                                    ",  Name: " + post.getDogName() +
                                    ",  Age: " + post.getAge() +
                                    ",  Breed: " + post.getBreed())
                        }
                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "FeedFragment"
    }
}