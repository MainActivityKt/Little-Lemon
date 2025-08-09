package com.safire.littlelemon.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.safire.littlelemon.EMAIL_ADDRESS
import com.safire.littlelemon.FIRST_NAME
import com.safire.littlelemon.R
import com.safire.littlelemon.SCREEN
import com.safire.littlelemon.SURNAME
import com.safire.littlelemon.USER_PROFILE


@Composable
fun Onboarding(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }


    val sharedPreferences =
        LocalContext.current.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)

    Scaffold(
        topBar = {
            TopBar()
        }
    ) { contentPadding ->

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Let's get to know you", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First name", style = MaterialTheme.typography.bodyMedium) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                surname,
                { surname = it },
                label = { Text("Last name", style = MaterialTheme.typography.bodyMedium) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            var isValidEmail by rememberSaveable { mutableStateOf(true) }
            OutlinedTextField(
                value = emailAddress,
                onValueChange = {
                    emailAddress = it
                    isValidEmail = it.isEmailValid()
                },
                label = { Text("Email Address", style = MaterialTheme.typography.bodyMedium) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = !isValidEmail,
                supportingText = {
                    if (!isValidEmail) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Invalid email",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                trailingIcon = {
                    if (!isValidEmail)
                        Icon(
                            Icons.Default.Warning,
                            "warning",
                            tint = MaterialTheme.colorScheme.error
                        )
                })

            Spacer(modifier = Modifier.height(16.dp))

            val context = LocalContext.current

            Button(onClick = {
                if (firstName.isEmpty() || surname.isEmpty() || emailAddress.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Error: one or more fields are empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    sharedPreferences.edit(commit = true) { putString(FIRST_NAME, firstName) }
                    sharedPreferences.edit(commit = true) { putString(EMAIL_ADDRESS, emailAddress) }
                    sharedPreferences.edit(commit = true) { putString(SURNAME, surname) }

                    Toast.makeText(context, "Successfully registered", Toast.LENGTH_LONG).show()
                    navController.navigate(SCREEN.HOME)
                }

            }) {
                Text(
                    "Register", style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

fun String.isEmailValid(): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    val navController = rememberNavController()
    Onboarding(navController)
}