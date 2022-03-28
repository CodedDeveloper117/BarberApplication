package com.ammar.vendorapp.authentication.ui.sign_up

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ammar.vendorapp.R
import com.ammar.vendorapp.authentication.common.utils.onChange
import com.ammar.vendorapp.databinding.FragmentSignupBinding
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
            setError(firstname.error, binding.firstNameTextLayout)
            setError(lastname.error, binding.lastNameTextLayout)
            setError(email.error, binding.emailTextLayout)
            binding.passwordTextLayout.helperText = password.error
            setError(username.error, binding.userNameTextLayout)
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
}