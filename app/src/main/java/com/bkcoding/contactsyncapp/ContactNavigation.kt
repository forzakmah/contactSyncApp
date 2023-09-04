package com.bkcoding.contactsyncapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.bkcoding.contactsyncapp.ui.screen.ContactScreen

object Routing {
    /**
     * route name for
     */
    const val contactScreen = "contactScreen"
}

/**
 * Basic graph navigation
 * @param modifier [Modifier]
 * @param navController [NavHostController]
 * @param startDestination [String]
 */
@Composable
fun ContactNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routing.contactScreen
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        /**
         * List of the contacts
         */
        composable(
            route = Routing.contactScreen
        ) {
            ContactScreen()
        }
    }
}
