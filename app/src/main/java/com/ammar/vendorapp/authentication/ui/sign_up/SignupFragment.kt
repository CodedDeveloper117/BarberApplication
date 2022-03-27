package com.ammar.vendorapp.authentication.ui.sign_up

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ammar.vendorapp.R
import com.ammar.vendorapp.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {

    private val viewModel: SignupViewModel by viewModels()

    private var _binding: FragmentSignupBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSignupBinding.bind(view)
        viewModel.execute(
            SignupEvents.Register(
                "coded.developer098@gmail.com",
                "Emmanuel",
                "Emmanuel",
                "Jason",
                "Peculiar"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}