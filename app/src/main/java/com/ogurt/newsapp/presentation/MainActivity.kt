package com.ogurt.newsapp.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.ogurt.newsapp.R
import com.ogurt.newsapp.databinding.ActivityMainBinding
import com.ogurt.newsapp.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setNavigationBars()
    }

    fun setNavigationBars() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.ic_app)
        supportActionBar?.title = getString(R.string.app_name)
        binding.bottomNav.visibility = View.VISIBLE
        val controller = binding.bottomNav.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.nav_graph_home_page,
                R.navigation.nav_graph_bookmarks
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            null
        )
        currentNavController = controller
    }

    fun changeBottomBarForNewsDetailFragment() {
        binding.bottomNav.visibility = View.GONE
    }

}