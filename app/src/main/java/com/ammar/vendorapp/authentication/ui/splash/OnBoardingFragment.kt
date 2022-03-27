package com.ammar.vendorapp.authentication.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ammar.vendorapp.R
import com.ammar.vendorapp.databinding.FragmentOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment(R.layout.fragment_on_boarding) {

    private var _binding: FragmentOnBoardingBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentOnBoardingBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}