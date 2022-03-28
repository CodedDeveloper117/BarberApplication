package com.ammar.vendorapp.authentication.ui.email

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ammar.vendorapp.R
import com.ammar.vendorapp.authentication.ui.login.LoginViewModel
import com.ammar.vendorapp.databinding.FragmentEmailBinding
import com.ammar.vendorapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailFragment : Fragment(R.layout.fragment_email) {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentEmailBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentEmailBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}