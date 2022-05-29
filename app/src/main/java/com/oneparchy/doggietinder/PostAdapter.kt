package com.oneparchy.doggietinder

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oneparchy.doggietinder.models.Post

const val POST_EXTRA = "POST_EXTRA"
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvDogName: TextView
        val ivDogImg: ImageView
        val tvAge: TextView
        val tvSex: TextView
        val tvBreed: TextView
        val tvDescription: TextView

        init {
            tvDogName = itemView.findViewById(R.id.tvDogName)
            ivDogImg = itemView.findViewById(R.id.ivDogImg)
            tvAge = itemView.findViewById(R.id.tvAge)
            tvSex = itemView.findViewById(R.id.tvSex)
            tvBreed = itemView.findViewById(R.id.tvBreed)
            tvDescription = itemView.findViewById(R.id.tvDescription)

            itemView.setOnClickListener(this)
        }

        fun bind(post: Post) {
            tvDogName.text = post.getDogName()
            tvAge.text = post.getAge()
            tvSex.text = post.getSex()
            tvBreed.text = post.getBreed()
            tvDescription.text = post.getDescription()
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivDogImg)
        }

        override fun onClick(p0: View?) {
            // To get notified which post is clicked
            val post = posts[adapterPosition]
            // user intent to nagvigate to detail activity
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(POST_EXTRA, post)
            context.startActivity(intent)
        }


    }
}