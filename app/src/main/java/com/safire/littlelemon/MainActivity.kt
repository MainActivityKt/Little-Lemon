package com.safire.littlelemon

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.safire.littlelemon.ui.HomeScreen
import com.safire.littlelemon.ui.Onboarding
import com.safire.littlelemon.ui.UserProfile
import com.safire.littlelemon.ui.theme.LittleLemonTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "little-lemon")
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LittleLemonTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LittleLemonApp(
                        database,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            if (database.menuItemDao().isEmpty()) {
                saveMenuToDatabase(fetchMenu())
            }
        }
    }


    private suspend fun fetchMenu(): List<MenuItemNetwork> {
        val url =
            "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"
        val response = httpClient.get(url).body<MenuNetwork>()
        return response.menuItems
    }

    private fun saveMenuToDatabase(menuItemsNetwork: List<MenuItemNetwork>) {
        val menuItemsRoom = menuItemsNetwork.map { it.toMenuItemRoom() }
        database.menuItemDao().insertAll(*menuItemsRoom.toTypedArray())
    }
}

@Composable
fun LittleLemonApp(database: AppDatabase, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavigationComposable(navController = navController, database)
}

@Composable
fun NavigationComposable(navController: NavHostController, database: AppDatabase) {

    val hasUserData = hasUserDataInSharedPreferences()

    NavHost(
        navController = navController,
        startDestination = if (hasUserData) SCREEN.HOME.route else SCREEN.ONBOARDING.route
    ) {


        composable(SCREEN.ONBOARDING.route) {
            Onboarding(navController)
        }
        composable(SCREEN.HOME.route) {
            HomeScreen(navController, database)
        }
        composable(SCREEN.USER_PROFILE.route) {
            UserProfile(navController)
        }
    }
}

@Composable
fun hasUserDataInSharedPreferences(): Boolean {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(USER_PROFILE, Context.MODE_PRIVATE)
    val email = sharedPreferences.getString(EMAIL_ADDRESS, "") ?: ""
    return email.isNotBlank()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LittleLemonTheme {

    }
}