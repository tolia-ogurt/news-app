package com.ogurt.newsapp.presentation.news_detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ogurt.newsapp.databinding.NewsDetailFragmentBinding
import com.ogurt.newsapp.presentation.MainActivity


class NewsDetailFragment : Fragment() {

    private val binding by lazy { NewsDetailFragmentBinding.inflate(layoutInflater) }
    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?,
            ): Boolean {
                return false
            }
        }
        binding.webView.loadUrl(args.url)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).changeBottomBarForNewsDetailFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setNavigationBars()
    }
}