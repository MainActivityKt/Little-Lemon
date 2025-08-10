package com.safire.littlelemon.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.safire.littlelemon.EMAIL_ADDRESS
import com.safire.littlelemon.FIRST_NAME
import com.safire.littlelemon.SCREEN
import com.safire.littlelemon.SURNAME
import com.safire.littlelemon.USER_PROFILE
import androidx.core.content.edit


@Composable
fun UserProfile(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)

    val firstName = sharedPreferences.getString(FIRST_NAME, "John")

    val lastName = sharedPreferences.getString(SURNAME, "Doe")
    val email = sharedPreferences.getString(EMAIL_ADDRESS, "someone@example.com") // email address


    Scaffold (topBar = {
        TopBar()
    }){ contentPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            Text(
                text = "User information",
                style = typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text("First Name: $firstName", modifier = Modifier.padding(top = 8.dp),
                style = typography.bodyMedium)
            Text("Last Name: $lastName", modifier = Modifier.padding(top = 8.dp), style = typography.bodyMedium)
            Text("Email: $email", modifier = Modifier.padding(top = 8.dp), style = typography.bodyMedium
            )

            Button(
                onClick = {
                    sharedPreferences.edit { clear() }


                    navController.popBackStack(SCREEN.ONBOARDING.route, false)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Log out")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    UserProfile(navController)
}
