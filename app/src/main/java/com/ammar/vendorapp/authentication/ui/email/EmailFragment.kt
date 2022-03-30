package com.ammar.vendorapp.authentication.ui.email

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
import com.ammar.vendorapp.authentication.common.utils.setCustomErrors
import com.ammar.vendorapp.databinding.FragmentEmailBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmailFragment : Fragment(R.layout.fragment_email) {

    private val viewModel: EmailViewModel by viewModels()

    private var _binding: FragmentEmailBinding? = null

    private val binding get() = _binding!!

    private val args: EmailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentEmailBinding.bind(view)
        changeListeners()

        binding.loginBtn.setOnClickListener {
            if(args.forgotPassword) {
                viewModel.execute(EmailEvents.ForgotPassword)
            } else {
                viewModel.execute(EmailEvents.ValidateUser)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    binding.emailLayout.setCustomErrors(it.email.error)
                    if (it.loading) {
                        binding.apply {
                            loginBtn.background = ResourcesCompat.getDrawable(
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest { event ->
                    when (event) {
                        is EmailUiEvents.InvalidInputParameters -> {
                            Snackbar.make(
                                view,
                                "Invalid Parameters, Please Input the right values",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        is EmailUiEvents.Error -> {
                            binding.apply {
                                loginBtn.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.background_action_btn,
                                    null
                                )
                                progressBar.isVisible = false
                                signupButtonText.isVisible = true
                            }
                            Snackbar.make(view, event.error, Snackbar.LENGTH_LONG).show()
                        }
                        is EmailUiEvents.ForgotPasswordSuccess -> {
                            binding.apply {
                                loginBtn.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.background_action_btn,
                                    null
                                )
                                progressBar.isVisible = false
                                signupButtonText.isVisible = true
                            }
                            val email = viewModel.state.value.email.value
                            val action = EmailFragmentDirections.actionEmailFragmentToOtpFragment(
                                event.key,
                                email,
                                true
                            )
                            findNavController().navigate(action)
                        }
                        is EmailUiEvents.ValidateUserSuccess -> {
                            binding.apply {
                                loginBtn.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.background_action_btn,
                                    null
                                )
                                progressBar.isVisible = false
                                signupButtonText.isVisible = true
                            }
                            val email = viewModel.state.value.email.value
                            val action = EmailFragmentDirections.actionEmailFragmentToOtpFragment(
                                event.key,
                                email,
                                false
                            )
                            findNavController().navigate(action)
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

    private fun changeListeners() {
        binding.emailField.onChange {
            viewModel.execute(EmailEvents.ChangeEmail(it))
        }
    }

}