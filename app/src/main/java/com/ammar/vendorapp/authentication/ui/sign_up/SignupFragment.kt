package com.ammar.vendorapp.authentication.ui.sign_up

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ammar.vendorapp.R
import com.ammar.vendorapp.authentication.common.utils.onChange
import com.ammar.vendorapp.authentication.common.utils.setCustomErrors
import com.ammar.vendorapp.databinding.FragmentSignupBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {

    private val viewModel: SignupViewModel by viewModels()

    private var _binding: FragmentSignupBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSignupBinding.bind(view)
        registerTextListeners()

        binding.loginBtn.setOnClickListener {
            viewModel.execute(SignupEvents.Register)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    setErrors(it)
                    if(it.loading) {
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
                viewModel.events.collectLatest {
                    when (it) {
                        is SignupUiEvents.InvalidInputParameters -> {
                            Snackbar.make(
                                view,
                                "Invalid Parameters, Please Input the right values",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        is SignupUiEvents.Error -> {
                            binding.apply {
                                loginBtn.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.background_action_btn,
                                    null
                                )
                                progressBar.isVisible = false
                                signupButtonText.isVisible = true
                            }
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                        }
                        is SignupUiEvents.Success -> {
                            Toast.makeText(requireContext(), it.key, Toast.LENGTH_LONG).show()
                            binding.apply {
                                loginBtn.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.background_action_btn,
                                    null
                                )
                                progressBar.isVisible = false
                                signupButtonText.isVisible = true
                            }
                            val action =
                                SignupFragmentDirections.actionSignupFragmentToOtpFragment(it.key, viewModel.state.value.email.value, false)
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

    private fun registerTextListeners() {
        binding.emailField.onChange {
            viewModel.execute(SignupEvents.ChangeEmail(it))
        }

        binding.passwordField.onChange {
            viewModel.execute(SignupEvents.ChangePassword(it))
        }

        binding.usernameField.onChange {
            viewModel.execute(SignupEvents.ChangeUsername(it))
        }

        binding.lastnameField.onChange {
            viewModel.execute(SignupEvents.ChangeLastname(it))
        }

        binding.firstnameField.onChange {
            viewModel.execute(SignupEvents.ChangeFirstname(it))
        }

    }

    private fun setErrors(state: SignupState) {
        state.apply {
            binding.firstNameTextLayout.setCustomErrors(firstname.error)
            binding.lastNameTextLayout.setCustomErrors(lastname.error)
            binding.emailTextLayout.setCustomErrors(email.error)
            binding.passwordTextLayout.helperText = if (password.error.isNotBlank() && password.error != "blank") password.error else ""
            binding.userNameTextLayout.setCustomErrors(username.error)
        }
    }
}