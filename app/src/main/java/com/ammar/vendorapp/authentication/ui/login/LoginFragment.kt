package com.ammar.vendorapp.authentication.ui.login

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
import com.ammar.vendorapp.R
import com.ammar.vendorapp.authentication.common.utils.onChange
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

        binding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
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
                            Intent(requireContext(), MainActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            }.also {
                                requireContext().startActivity(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setErrors(state: LoginState) {
        state.apply {
            setError(email.error, binding.emailTextLayout)
            binding.passwordTextLayout.helperText = state.password.error
        }
    }

    private fun setError(error: String, layout: TextInputLayout) {
        if (error.isNotBlank()) {
            layout.isErrorEnabled = true
            layout.error = error
        } else {
            layout.isErrorEnabled = false
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