package com.ammar.vendorapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import com.ammar.vendorapp.ui.theme.AmethystPurple
import com.ammar.vendorapp.ui.theme.DarkViolet

@Composable
fun AuthTextField(
    icon: ImageVector,
    placeholder: String,
    type: KeyboardType
) {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = {
            Text(text = placeholder, style = MaterialTheme.typography.body1)
        },
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = "email Icon")
        },
        keyboardOptions = KeyboardOptions(keyboardType = type, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {

        }),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = DarkViolet,
            unfocusedBorderColor = AmethystPurple,
            textColor = Color.DarkGray,
            cursorColor = DarkViolet,
            leadingIconColor = DarkViolet,
            placeholderColor = AmethystPurple
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PasswordTextField(
    icon: ImageVector = Icons.Filled.Lock,
    placeholder: String = "Password",
    type: KeyboardType = KeyboardType.Password
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = {
            Text(text = placeholder, style = MaterialTheme.typography.body1)
        },
        trailingIcon = {
            val (icon, iconColor) = if (passwordVisible) {
                Pair(Icons.Filled.Visibility, AmethystPurple)
            } else {
                Pair(Icons.Filled.VisibilityOff, AmethystPurple)
            }
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = icon, contentDescription = "visibility")
            }
        },
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = "email Icon")
        },
        keyboardOptions = KeyboardOptions(keyboardType = type, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {

        }),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = DarkViolet,
            unfocusedBorderColor = AmethystPurple,
            textColor = Color.DarkGray,
            cursorColor = DarkViolet,
            leadingIconColor = DarkViolet,
            placeholderColor = AmethystPurple,
            trailingIconColor = Color.Gray
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PhoneTextField(
    icon: ImageVector = Icons.Filled.Lock,
    placeholder: String = "Password",
    type: KeyboardType = KeyboardType.Password,
    value: String = ""
) {
    val appendedString = buildAnnotatedString {
        withStyle(SpanStyle(color = AmethystPurple)) {
            append("+234 ")
        }
        toAnnotatedString()
    }
    OutlinedTextField(
        value = "$appendedString $value",
        onValueChange = {},
        placeholder = {
            Text(text = placeholder, style = MaterialTheme.typography.body1)
        },
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = "phone")
        },
        keyboardOptions = KeyboardOptions(keyboardType = type, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {

        }),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = DarkViolet,
            unfocusedBorderColor = AmethystPurple,
            textColor = Color.DarkGray,
            cursorColor = DarkViolet,
            leadingIconColor = DarkViolet,
            placeholderColor = AmethystPurple,
        ),
        modifier = Modifier.fillMaxWidth()
    )
}