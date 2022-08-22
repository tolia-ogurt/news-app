package com.ogurt.newsapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ogurt.newsapp.R
import com.ogurt.newsapp.databinding.NewsItemBinding
import com.ogurt.newsapp.model.ArticleDomainModel

class RecyclerViewAdapter(
    val onItemClick: (ArticleDomainModel) -> Unit,
    val onShareClick: (ArticleDomainModel) -> Unit,
    val onBookmarkClick: (ArticleDomainModel, Boolean) -> Unit,
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val newsList = mutableListOf<ArticleDomainModel>()

    inner class ViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick(newsList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(items: List<ArticleDomainModel>) {
        newsList.clear()
        newsList.addAll(items)
        notifyDataSetChanged()
    }

    fun deleteItem(item: ArticleDomainModel) {
        val index = newsList.indexOfFirst {
            it.publishedAt == item.publishedAt && it.title == item.title
        }
        newsList.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = with(holder.binding) {
        with(newsList[position]) {
            Glide.with(ivNews.context)
                .load(urlToImage)
                .into(ivNews)
            tvTitleNews.text = title
            tvDescriptionNews.text = description
            tvSite.text = source?.name
            ivBookmark.setOnClickListener {
                onBookmarkClick(newsList[position], ivBookmark.isSelected)
            }
            ivBookmark.isChecked = isSelected
            ivShare.setOnClickListener {
                onShareClick(newsList[position])
            }
        }
    }

    override fun getItemCount(): Int = newsList.size
}