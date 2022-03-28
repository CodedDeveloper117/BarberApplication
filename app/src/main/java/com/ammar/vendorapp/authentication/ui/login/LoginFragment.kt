package com.ammar.vendorapp.authentication.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ammar.vendorapp.R
import com.ammar.vendorapp.authentication.common.utils.onChange
import com.ammar.vendorapp.authentication.ui.sign_up.SignupState
import com.ammar.vendorapp.databinding.FragmentLoginBinding
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
        if(error.isNotBlank()) {
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