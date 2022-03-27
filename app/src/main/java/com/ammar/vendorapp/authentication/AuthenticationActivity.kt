package com.ammar.vendorapp.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.ammar.vendorapp.R
import com.ammar.vendorapp.databinding.ActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment
    private lateinit var navGraph: NavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navGraph = navController.navInflater.inflate(R.navigation.authentication_graph)
        //val isFromMainActivity = intent.getBooleanExtra("is_from_main_activity", false)
        navGraph.setStartDestination(R.id.splashFragment)

        navController.graph = navGraph
    }
}