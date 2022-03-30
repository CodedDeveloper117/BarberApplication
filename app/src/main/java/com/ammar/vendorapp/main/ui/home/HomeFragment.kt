package com.ammar.vendorapp.main.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ammar.vendorapp.R
import com.ammar.vendorapp.common.data.User
import com.ammar.vendorapp.databinding.FragmentHomeBinding
import com.ammar.vendorapp.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeBinding.bind(view)
        val user = (activity as MainActivity).intent.extras?.getSerializable("user") as User
        val text = "Hello, ${user.firstName} ${user.lastName}"
        binding.user.text = text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}