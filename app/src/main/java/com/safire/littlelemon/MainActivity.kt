package com.safire.littlelemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.safire.littlelemon.ui.theme.LittleLemonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()




        setContent {
            LittleLemonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LittleLemonApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LittleLemonApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val isUserDataStored = false

    Text(
        text = "Hello, world!",
        modifier = modifier
    )
}

@Composable
fun NavigationComposable(navController: NavHostController, isUserDataStored: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (isUserDataStored) SCREEN.ONBOARDING.route else SCREEN.HOME.route
    ) {


        composable(SCREEN.ONBOARDING.route) {  } //TODO
        composable(SCREEN.HOME.route) {  }   //TODO
        composable(SCREEN.USER_PROFILE.route) {


        }   //TODO
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LittleLemonTheme {
        LittleLemonApp()
    }
}