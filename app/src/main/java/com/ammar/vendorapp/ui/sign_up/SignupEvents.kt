package com.ammar.vendorapp.ui.sign_up

import com.ammar.vendorapp.ui.login.LoginEvents
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

sealed class SignupEvents {
    data class SignupInWithGoogle(val task: Task<GoogleSignInAccount>): SignupEvents()
}
