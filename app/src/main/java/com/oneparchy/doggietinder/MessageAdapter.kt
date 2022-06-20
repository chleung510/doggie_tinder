package com.oneparchy.doggietinder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oneparchy.doggietinder.models.Message

class MessageAdapter(val context: Context, val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        //Specify layout file to be used
        val view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        val message = messages.get(position)
        holder.bind(message as Message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivUserImg: ImageView
        val tvUserName: TextView
        val tvTimeStamp: TextView
        val tvMessage: TextView

        init {
            ivUserImg = itemView.findViewById(R.id.ivUserImg)
            tvUserName = itemView.findViewById(R.id.tvUserName)
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp)
            tvMessage = itemView.findViewById(R.id.tvMessage)
        }

        fun bind(message: Message){
            tvUserName.text = message.getUser()?.username
            tvMessage.text = message.getMessage()
            tvTimeStamp.text = message.getFormattedTimestamp()

            Glide.with(itemView.context).load(message.getUserImage()?.url).into(ivUserImg)
        }
    }
}