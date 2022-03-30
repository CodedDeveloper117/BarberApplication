package com.ammar.vendorapp.authentication.ui.change_password

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
import com.ammar.vendorapp.authentication.common.utils.setCustomErrors
import com.ammar.vendorapp.authentication.ui.email.EmailFragmentDirections
import com.ammar.vendorapp.authentication.ui.email.EmailUiEvents
import com.ammar.vendorapp.databinding.FragmentChangePasswordBinding
import com.ammar.vendorapp.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private var _binding: FragmentChangePasswordBinding? = null

    private val viewModel: ChangePasswordViewModel by viewModels()
    private val binding get() = _binding!!
    private val args: ChangePasswordFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentChangePasswordBinding.bind(view)
        changeListeners()

        binding.loginBtn.setOnClickListener {
            viewModel.execute(ChangePasswordEvents.Continue(args.token))
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest {
                    binding.retypeLayout.setCustomErrors(it.retypePassword.error)
                    binding.passwordLayout.setCustomErrors(it.password.error)
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
                        is ChangePasswordUiEvents.InvalidInputParameters -> {
                            Snackbar.make(
                                view,
                                "Invalid Parameters, Please Input the right values",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        is ChangePasswordUiEvents.Error -> {
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
                        is ChangePasswordUiEvents.Success -> {
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

    private fun changeListeners() {
        binding.passwordField.onChange {
            viewModel.execute(ChangePasswordEvents.ChangePassword(it))
        }

        binding.retypePasswordField.onChange {
            viewModel.execute(
                ChangePasswordEvents.ChangeRetypePassword(
                    it,
                    viewModel.state.value.password.value
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}