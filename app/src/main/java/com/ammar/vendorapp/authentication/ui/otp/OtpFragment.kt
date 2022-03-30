package com.ammar.vendorapp.authentication.ui.otp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ammar.vendorapp.R
import com.ammar.vendorapp.authentication.common.utils.onChange
import com.ammar.vendorapp.databinding.FragmentOtpBinding
import com.ammar.vendorapp.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class OtpFragment : Fragment(R.layout.fragment_otp) {

    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OtpViewModel by viewModels()

    private val args: OtpFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentOtpBinding.bind(view)

        viewModel.execute(OtpEvents.ChangeParameters(key = args.key, email = args.email))

        binding.otpView.onChange {
            viewModel.execute(OtpEvents.ChangeOtp(it))
        }

        binding.verifyOtpBtn.setOnClickListener {
            viewModel.execute(OtpEvents.VerifyOtp)
        }

        binding.resendOtp.setOnClickListener {
            viewModel.execute(OtpEvents.ResendOtp)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    if(it.loading) {
                        binding.apply {
                            verifyOtpBtn.background = ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.background_action_btn_outlined,
                                null
                            )
                            progressBar.isVisible = true
                            signupButtonText.isVisible = false
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest { event ->
                    when(event) {
                        is OtpUiEvents.Success -> {
                            binding.apply {
                                verifyOtpBtn.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.background_action_btn,
                                    null
                                )
                                progressBar.isVisible = false
                                signupButtonText.isVisible = true
                            }
                            if(args.forgotPassword) {
                                val action = OtpFragmentDirections.actionOtpFragmentToChangePasswordFragment(event.token.token)
                                findNavController().navigate(action)
                            } else {
                                findNavController().navigate(R.id.action_otpFragment_to_splashLaunchFragment)
                            }
                        }
                        is OtpUiEvents.Error -> {
                            binding.apply {
                                verifyOtpBtn.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.background_action_btn,
                                    null
                                )
                                progressBar.isVisible = false
                                signupButtonText.isVisible = true
                            }
                        }
                        is OtpUiEvents.OtpResendSuccessful -> {
                            binding.apply {
                                verifyOtpBtn.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.background_action_btn,
                                    null
                                )
                                progressBar.isVisible = false
                                signupButtonText.isVisible = true
                            }
                        }
                        is OtpUiEvents.InvalidInputParameters -> {
                            Snackbar.make(view, "Invalid Input Parameters", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}