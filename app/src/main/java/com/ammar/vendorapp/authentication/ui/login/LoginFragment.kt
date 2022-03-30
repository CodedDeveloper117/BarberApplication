package com.ammar.vendorapp.authentication.ui.login

import android.content.Intent
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
import com.ammar.vendorapp.databinding.FragmentLoginBinding
import com.ammar.vendorapp.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentLoginBinding.bind(view)

        registerTextListeners()
        binding.loginBtn.setOnClickListener {
            viewModel.execute(LoginEvents.Login)
        }

        binding.forgotPassword.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToEmailFragment(true)
            findNavController().navigate(action)
        }

        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.continueSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToEmailFragment(false)
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    setErrors(it)
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
                        is LoginUiEvents.InvalidInputParameters -> {
                            Snackbar.make(
                                view,
                                "Invalid Parameters, Please Input the right values",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        is LoginUiEvents.Error -> {
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
                        is LoginUiEvents.Success -> {
                            binding.apply {
                                loginBtn.background = ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.background_action_btn,
                                    null
                                )
                                progressBar.isVisible = false
                                signupButtonText.isVisible = true
                            }
                            findNavController().navigate(R.id.action_loginFragment_to_splashLaunchFragment)
                        }
                        is LoginUiEvents.UserNotValidated -> {
                            Toast.makeText(requireContext(), "User not validated", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setErrors(state: LoginState) {
        state.apply {
            binding.emailTextLayout.setCustomErrors(email.error)
            binding.passwordTextLayout.helperText = if (password.error.isNotBlank() && password.error != "blank") password.error else ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun registerTextListeners() {
        binding.emailTextField.onChange {
            viewModel.execute(LoginEvents.ChangeEmail(it))
        }

        binding.passwordTextField.onChange {
            viewModel.execute(LoginEvents.ChangePassword(it))
        }
    }
}