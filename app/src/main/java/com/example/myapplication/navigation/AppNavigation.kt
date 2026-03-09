package com.example.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.SplashScreen
import com.example.myapplication.data.UserPreferences
import com.example.myapplication.ui.screens.auth.LoginScreen
import com.example.myapplication.ui.screens.auth.OtpScreen
import com.example.myapplication.ui.screens.auth.RoleSelectionScreen
import com.example.myapplication.ui.screens.home.ProfileScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(userPreferences: UserPreferences) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    
    // Collect the user state from DataStore
    val isLoggedIn by userPreferences.isLoggedInFlow.collectAsState(initial = null)
    val userRole by userPreferences.userRoleFlow.collectAsState(initial = null)

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            
            // 1. Splash Screen Configuration
            composable("splash") {
                SplashScreen()
                
                // Once we know if we are logged in or not, transition
                LaunchedEffect(isLoggedIn) {
                    if (isLoggedIn != null) {
                        delay(1500) // Show logo for 1.5 seconds minimum
                        
                        if (isLoggedIn == true) {
                            navController.navigate("home") {
                                popUpTo("splash") { inclusive = true }
                            }
                        } else {
                            navController.navigate("role_selection") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }
                }
            }
            
            // 2. Role Selection
            composable("role_selection") {
                RoleSelectionScreen(
                    onRoleSelected = { selectedRole ->
                        // In a real app we'd save the role here if we wanted to enforce it before Login
                        // For now we pass it forward as an argument in the Nav Graph
                        navController.navigate("login/$selectedRole")
                    }
                )
            }
            
            // 3. Login
            composable("login/{role}") { backStackEntry ->
                val role = backStackEntry.arguments?.getString("role") ?: "CUSTOMER"
                LoginScreen(
                    role = role,
                    onContinueClicked = { phoneNumber ->
                        navController.navigate("otp/$phoneNumber/$role")
                    }
                )
            }
            
            // 4. OTP Verification
            composable("otp/{phone}/{role}") { backStackEntry ->
                val phone = backStackEntry.arguments?.getString("phone") ?: ""
                val role = backStackEntry.arguments?.getString("role") ?: "CUSTOMER"
                
                OtpScreen(
                    phoneNumber = phone,
                    onVerifyClicked = { _ ->
                        // Fake successful login: Save to DataStore!
                        scope.launch {
                            userPreferences.saveLoginState(isLoggedIn = true, role = role)
                        }
                        // The Splash LaunchEffect or our navigation state handles transitioning to Home.
                        // But we want to do it manually here for perfect UI flow.
                        navController.navigate("home") {
                            popUpTo(0) // Clear entire backstack!
                        }
                    }
                )
            }
            
            // 5. Home / Profile (Placeholder)
            composable("home") {
                ProfileScreen(
                    role = userRole,
                    onLogoutClicked = {
                        scope.launch {
                            userPreferences.clearSession()
                        }
                        navController.navigate("role_selection") {
                            popUpTo(0) // Clear backstack!
                        }
                    }
                )
            }
        }
    }
}
