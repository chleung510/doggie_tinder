package com.oneparchy.doggietinder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oneparchy.doggietinder.models.Post

class PostAdapter(val context: Context, val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Specify layout file to be used
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts.get(position)
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAuthor: TextView
        val ivPicture: ImageView
        val tvPostContent: TextView

        init {
            tvAuthor = itemView.findViewById(R.id.tvAuthor)
            ivPicture = itemView.findViewById(R.id.ivPicture)
            tvPostContent = itemView.findViewById(R.id.tvPostContent)
        }

        fun bind(post: Post) {
            tvAuthor.text = post.getUser()?.username
            tvPostContent.text = post.getDescription()
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivPicture)
        }
    }
}