package com.ogurt.newsapp.presentation.news_list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ogurt.newsapp.adapter.RecyclerViewAdapter
import com.ogurt.newsapp.databinding.NewsListFragmentBinding
import com.ogurt.newsapp.model.ArticleDomainModel
import com.ogurt.newsapp.presentation.bookmarks.BookmarksFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsListFragment : Fragment() {

    private val viewModel: NewsListViewModel by viewModels()
    private val binding by lazy { NewsListFragmentBinding.inflate(layoutInflater) }

    private val adapter by lazy {
        RecyclerViewAdapter(::openNewsDetail, ::onShareClicked, viewModel::transferringBookmarks)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        subscribeLiveData()
        viewModel.getNews()
        initAdapter()
        searchNews()
        return binding.root
    }

    private fun onShareClicked(article: ArticleDomainModel) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, article.url)
            type = INTENT_TYPE
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun openNewsDetail(article: ArticleDomainModel) {
        val navigation =
            NewsListFragmentDirections.actionNewsListFragmentToNewsDetailFragment(article.url)
        findNavController().navigate(navigation)
    }

    private fun initAdapter() {
        binding.recyclerView.adapter = adapter
        viewModel.news.observe(viewLifecycleOwner, adapter::update)
    }

    private fun searchNews() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchNews(binding.etSearch.text.toString()
                    .ifEmpty { DEFAULT_SEARCH_WORD })
                true
            } else {
                false
            }
        }
    }

    private fun subscribeLiveData() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (it) View.GONE else View.VISIBLE
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val DEFAULT_SEARCH_WORD = "tesla"
        const val INTENT_TYPE = "text/plain"
    }
}