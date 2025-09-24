package com.technonext.ltd.assesment

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.technonext.ltd.assesment.routes.AppNavHost
import com.technonext.ltd.assesment.routes.Screen
import com.technonext.ltd.assesment.ui.home.HomeScreen
import com.technonext.ltd.assesment.ui.login.LoginScreen
import com.technonext.ltd.assesment.ui.registration.RegistrationScreen
import com.technonext.ltd.assesment.ui.theme.AssesmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavHost()
        }
    }
}
