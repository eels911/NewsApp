package com.androiddevs.mvvmnewsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.newsapp.R
import com.example.android.newsapp.models.NewsHeadlines

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textTitle: TextView = itemView.findViewById(R.id.text_title)
        val textSource: TextView = itemView.findViewById(R.id.text_source)
        val img_headline: ImageView = itemView.findViewById(R.id.img_headline)

    }

    private val differCallback = object : DiffUtil.ItemCallback<NewsHeadlines>() {
        override fun areItemsTheSame(oldItem: NewsHeadlines, newItem: NewsHeadlines): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: NewsHeadlines, newItem: NewsHeadlines): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.news_list_items,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((NewsHeadlines) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.img_headline)
            holder.textSource.text = article.source?.name
            holder.textTitle.text = article.title
//            tvDescription.text = article.description
//            tvPublishedAt.text = article.publishedAt

            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }


        fun setOnItemClickListener(listener: (NewsHeadlines) -> Unit) {
            onItemClickListener = listener
        }
    }
}













