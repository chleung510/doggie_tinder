package com.oneparchy.doggietinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.oneparchy.doggietinder.models.Message
import com.oneparchy.doggietinder.models.Post
import com.parse.ParseObject
import com.parse.ParseUser


class ReplyActivity: AppCompatActivity() {

    companion object {
        const val TAG = "ReplyFragment"
    }

    lateinit var etReplyMsg: EditText
    lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)

        //Set listeners etc. here
        etReplyMsg = findViewById(R.id.etReplyMsg)
        btnSubmit = findViewById(R.id.btnSubmit)
        val post = intent.getParcelableExtra<Post>(POST_EXTRA) as Post

        Log.i("ReplyFragment", post.objectId)

        btnSubmit.setOnClickListener {
            //Send post to server
            //Grab the post lost dog's info from the edit text
            val replyMsg = etReplyMsg.text.toString()
            val user = ParseUser.getCurrentUser()
            if (replyMsg != "") {
                submitMsg(replyMsg, user, post)
            } else {
                Log.i(TAG, "Message cannot be blank")
                Toast.makeText(this, "Message cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //Create the post and submit it to Parse
   private fun submitMsg(replyMsg: String,
                          user: ParseUser,
                         post: ParseObject
   ) {
      val message = Message()
        message.setMessage(replyMsg)
        message.setUser(user)
        message.setPost(post)
        message.saveInBackground { e ->
         if (e != null) {
              e.printStackTrace()
             Toast.makeText(this, "Error making reply", Toast.LENGTH_SHORT).show()
         } else {
               Log.i(TAG, "Successfully made reply!")
               Toast.makeText(this, "Successfully replied!", Toast.LENGTH_SHORT).show()
               etReplyMsg.setText("")
             val intent = Intent()
             setResult(RESULT_OK, intent)
             finish()
            }
       }
    }
}