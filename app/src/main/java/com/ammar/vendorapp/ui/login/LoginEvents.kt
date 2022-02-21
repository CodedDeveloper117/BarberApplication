package com.ammar.vendorapp.ui.login

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

sealed class LoginEvents {
    data class LoggedInWithGoogle(val task: Task<GoogleSignInAccount>): LoginEvents()
}