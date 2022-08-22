package com.ogurt.newsapp.presentation.bookmarks

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
import com.ogurt.newsapp.R
import com.ogurt.newsapp.adapter.RecyclerViewAdapter
import com.ogurt.newsapp.databinding.BookmarksFragmentBinding
import com.ogurt.newsapp.model.ArticleDomainModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksFragment : Fragment(R.layout.bookmarks_fragment) {

    private val viewModel: BookmarksViewModel by viewModels()
    private val binding by lazy { BookmarksFragmentBinding.inflate(layoutInflater) }
    private val adapter by lazy {
        RecyclerViewAdapter(::openNewsDetail, ::onShareClicked, viewModel::transferringBookmarks)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        subscribeLiveData()
        viewModel.getNewsFromBookmark()
        searchDatabase()
        initAdapter()
        return binding.root
    }

    private fun initAdapter() {
        binding.recyclerView.adapter = adapter
        viewModel.newsBookmark.observe(viewLifecycleOwner, adapter::update)
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
            BookmarksFragmentDirections.actionBookmarksFragmentToNewsDetailFragment(article.url)
        findNavController().navigate(navigation)
    }

    private fun searchDatabase() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDataBase(binding.etSearch.text)
                    .observe(viewLifecycleOwner, adapter::update)
                true
            } else {
                false
            }
        }
    }

    private fun subscribeLiveData() = with(viewModel) {
        deleteItemAction.observe(viewLifecycleOwner, adapter::deleteItem)
        isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            binding.recyclerView.visibility = if (it) View.GONE else View.VISIBLE
        }
        error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val INTENT_TYPE = "text/plain"
    }
}