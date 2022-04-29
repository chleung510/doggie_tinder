package com.oneparchy.doggietinder

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oneparchy.doggietinder.models.Post


class PostAdapter(val content: Context, val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {

        val view = LayoutInflater.from(content).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {

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


    // ViewHolder is responsible for actual layout the recycler view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView
        val tvDogName: TextView
        val tvSex: TextView
        val tvAge: TextView
        val tvBreed: TextView

        init {
            ivImage = itemView.findViewById(R.id.ivImage)
            tvDogName = itemView.findViewById(R.id.tvDogName)
            tvSex = itemView.findViewById(R.id.tvSex)
            tvAge = itemView.findViewById(R.id.tvAge)
            tvBreed = itemView.findViewById(R.id.tvBreed)
        }

        fun bind(post: Post) {
            tvDogName.text = post.getDogName()
            tvSex.text = post.getSex()
            tvAge.text = post.getAge()
            tvBreed.text = post.getBreed()

            Glide.with(itemView).load(post.getImage()?.url).into(ivImage)
        }
    }
}