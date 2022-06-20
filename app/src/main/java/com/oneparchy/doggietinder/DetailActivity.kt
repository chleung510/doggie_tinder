package com.oneparchy.doggietinder

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.oneparchy.doggietinder.models.Message
import com.oneparchy.doggietinder.models.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser


class DetailActivity : AppCompatActivity() {

    private lateinit var ivDogImg: ImageView
    private lateinit var tvDogName: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvSex: TextView
    private lateinit var tvBreed: TextView
    private lateinit var tvUser: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btn_found: Button
    private lateinit var iv_checked: ImageView
    private lateinit var post: Post

    lateinit var rvMessage: RecyclerView
    lateinit var adapter: MessageAdapter
    var allMsgs: MutableList<Message> = mutableListOf()
    lateinit var swipeContainer: SwipeRefreshLayout




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        ivDogImg = findViewById(R.id.ivDogImg)
        tvDogName = findViewById(R.id.tvDogName)
        tvAge = findViewById(R.id.tvAge)
        tvSex = findViewById(R.id.tvSex)
        tvBreed = findViewById(R.id.tvBreed)
        tvUser = findViewById(R.id.tvUser)
        tvDescription = findViewById(R.id.tvDescription)
        btn_found = findViewById(R.id.btn_found)
        iv_checked = findViewById(R.id.iv_checked)

        post = intent.getParcelableExtra<Post>(POST_EXTRA) as Post


        Log.i(TAG, "Post is "+ post.getDogName())

        if (post.getUser()?.username.equals(ParseUser.getCurrentUser().username)){
            btn_found.setText("Found!")
            btn_found.setTextColor(Color.parseColor("#747679"))
            findViewById<Button>(R.id.btn_found).setOnClickListener {
                btn_found.setTextColor(Color.RED)
                post.setFound(true)
                post.saveInBackground { e ->
                    if (e != null) {
                        e.printStackTrace()
                        Toast.makeText(this, "Error completing process", Toast.LENGTH_SHORT).show()
                    } else {
                        findViewById<ImageView>(R.id.iv_checked).visibility = View.VISIBLE
                        Log.i(TAG, "Pet has marked as Found!")
                        Toast.makeText(this, "Pet has Found!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        if (post.isFound() == true){
            iv_checked.visibility = View.VISIBLE
            btn_found.setTextColor(Color.RED)
            btn_found.isClickable = false
        }

        tvDogName.text = post.getDogName()
        tvAge.text = post.getAge()
        tvSex.text = post.getSex()
        tvBreed.text = post.getBreed()
        tvUser.text = "@" + post.getUser()?.username
        tvDescription.text = post.getDescription()
        Glide.with(this).load(post.getImage()?.url).into(ivDogImg)

        findViewById<ImageButton>(R.id.btn_reply).setOnClickListener {
            // use intent to navigate to Reply Screen
            val intent = Intent(this@DetailActivity, ReplyActivity::class.java)
            intent.putExtra("POST_EXTRA", post)
            startActivityForResult(intent, REQUEST_CODE)
        }


        rvMessage = findViewById(R.id.rvMessage)
        adapter = MessageAdapter(this, allMsgs)
        rvMessage.adapter = adapter
        rvMessage.layoutManager = LinearLayoutManager(this)

        //Swipe to refresh
        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i(TAG,"Refreshing Feed")
            queryMsgs(post)
        }
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        queryMsgs(post)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE ) {
            queryMsgs(post)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun queryMsgs(post: Post){
        //Specify which class to query
        val query: ParseQuery<Message> = ParseQuery.getQuery(Message::class.java)
        //Find all the Message objects (including author)
        query.include(Message.KEY_USR)
        //return only the 10 latest posts, from newest to oldest
        query.limit = 10
        query.addDescendingOrder("createdAt")
        query.whereEqualTo(Message.KEY_POST, post)

        query.findInBackground(object : FindCallback<Message> {
            override fun done(messages: MutableList<Message>?, e: ParseException?) {
                if (e != null) {
                    Log.e(TAG, "Error retrieving messages")
                } else {
                    Log.i(TAG, "message query successful")
                    if (messages != null) {
                        for (message in messages) {
                            Log.i(TAG, "Post: " + message.getMessage() + ", User: " + message.getUser()?.username)
                        }
                        allMsgs.clear()
                        allMsgs.addAll(messages)
                        adapter.notifyDataSetChanged()
                        swipeContainer.isRefreshing = false
                    }
                }
            }
        } )
    }
    companion object {
        const val TAG = "DetailActivity"
        const val REQUEST_CODE = 20
    }
}