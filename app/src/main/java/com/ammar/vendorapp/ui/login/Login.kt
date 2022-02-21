package com.ammar.vendorapp.ui.login

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ammar.vendorapp.R
import com.ammar.vendorapp.data.firebase.FirebaseAuthentication
import com.ammar.vendorapp.ui.components.AuthTextField
import com.ammar.vendorapp.ui.components.PasswordTextField
import com.ammar.vendorapp.ui.navigation.NavigationScreens
import com.ammar.vendorapp.ui.theme.AmethystPurple
import com.ammar.vendorapp.ui.theme.DarkViolet
import com.ammar.vendorapp.ui.theme.GoogleRed
import com.google.android.gms.auth.api.signin.GoogleSignIn

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val startForResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                    viewModel.execute(LoginEvents.LoggedInWithGoogle(task))
                }
            }
        }
    )
    val state = viewModel.state.collectAsState().value
    val googleSignInClient = FirebaseAuthentication.getGoogleLoginAuth(context)

    LaunchedEffect(key1 = state) {
        if (state.error.isNotBlank() && !state.loading) {
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
        }
        if (!state.loading && state.data != null) {
            navController.navigate(NavigationScreens.Home.route) {
                popUpTo(NavigationScreens.Home.route) {
                    saveState = false
                }
                restoreState = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkViolet)
    ) {
        LazyColumn(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
                .background(Color.White)
                .padding(horizontal = 13.dp, vertical = 15.dp)
        ) {
            item {
                Text(
                    text = "Welcome back",
                    style = MaterialTheme.typography.h1.copy(color = DarkViolet)
                )
                Spacer(modifier = Modifier.height(20.dp))
                AuthTextField(
                    icon = Icons.Default.Email,
                    placeholder = "E-mail",
                    type = KeyboardType.Email
                )
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextField()
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = false, onClick = { }, colors = RadioButtonDefaults.colors(
                                unselectedColor = AmethystPurple,
                                selectedColor = DarkViolet
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Remember Me",
                            style = MaterialTheme.typography.body1.copy(
                                fontSize = 14.sp,
                                color = Color.DarkGray
                            )
                        )
                    }
                    Text(
                        text = "Forgot Password?",
                        style = MaterialTheme.typography.body1.copy(
                            color = DarkViolet,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline
                        ),
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.h2.copy(color = DarkViolet)
                    )
                    FloatingActionButton(onClick = { /*TODO*/ }, backgroundColor = DarkViolet) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .border(2.dp, Color.Gray.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                    .clickable {
                        startForResult.launch(googleSignInClient.signInIntent)
                    }
                    .padding(vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "google icon",
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Login with Google",
                            style = MaterialTheme.typography.body1.copy(color = Color.DarkGray)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate(NavigationScreens.Signup.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = false
                            }
                            restoreState = false
                        }
                    },
                    text = "Signup",
                    style = MaterialTheme.typography.body1.copy(
                        color = DarkViolet,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(Modifier.height(30.dp))
            }
        }
        if (state.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.7f))
                ) {
                    CircularProgressIndicator(
                        color = DarkViolet, modifier = Modifier.align(
                            Alignment.Center
                        )
                    )
                }
            }
        }


    }
}