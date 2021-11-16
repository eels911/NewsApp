package com.example.android.newsapp.models

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.newsapp.R
import com.squareup.picasso.Picasso

//class CustomAdapter(private val context: Context,private val headlines: List<NewsHeadlines>): RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {
//
//    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textTitle: TextView = itemView.findViewById(R.id.text_title)
//        val textSource: TextView = itemView.findViewById(R.id.text_source)
//        val img_headline: ImageView = itemView.findViewById(R.id.main_container)
//        val cardView: CardView = itemView.findViewById(R.id.main_container)
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_list_items,parent,false)
//        return CustomViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//
//        holder.textTitle.text[position]
//        holder.textSource.text[position]
//
//        if (headlines[position].urlToImage != null){
//            Picasso.get().load(headlines[position].urlToImage).into(holder.img_headline)
//        }
//    }
//
//    override fun getItemCount(): Int = headlines.size
//}