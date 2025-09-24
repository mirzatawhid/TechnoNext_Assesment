package com.technonext.ltd.assesment.routes

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.technonext.ltd.assesment.ui.home.HomeScreen
import com.technonext.ltd.assesment.ui.login.LoginScreen
import com.technonext.ltd.assesment.ui.registration.RegistrationScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) { LoginScreen(onNavigateToRegister = { navController.navigate(Screen.Register.route) }, onLoginSuccess = { navController.navigate(Screen.Home.route) }) }
        composable(Screen.Register.route) { RegistrationScreen(onRegisterSuccess = { navController.popBackStack() }) }
        composable(Screen.Home.route) { HomeScreen() }
    }
}
