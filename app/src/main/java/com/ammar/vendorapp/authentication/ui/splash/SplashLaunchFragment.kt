package com.ammar.vendorapp.authentication.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ammar.vendorapp.R
import com.ammar.vendorapp.authentication.data.repositories.DatastoreRepository
import com.ammar.vendorapp.databinding.FragmentLaunchBinding
import com.ammar.vendorapp.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private const val TAG = "SplashLaunchFragment"

@AndroidEntryPoint
class SplashLaunchFragment : Fragment(R.layout.fragment_launch) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when(event) {
                        is SplashUiEvents.Success -> {
                            val bundle = Bundle()
                            bundle.putSerializable("user", event.user)
                            Intent(requireContext(), MainActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                putExtras(bundle)
                            }.also {
                                requireContext().startActivity(it)
                            }
                        }
                        is SplashUiEvents.Error -> {
                            Log.d(TAG, "onViewCreated: ${event.error}")
                            Snackbar.make(view, event.error, Snackbar.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_splashLaunchFragment_to_loginFragment)
                        }
                        is SplashUiEvents.InitialState -> {

                        }
                        is SplashUiEvents.InvalidToken -> {
                            delay(1500L)
                            findNavController().navigate(R.id.action_splashLaunchFragment_to_loginFragment)
                        }
                    }
                }
            }
        }
    }

}