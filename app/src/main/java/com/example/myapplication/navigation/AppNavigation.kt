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
import com.example.myapplication.ui.screens.auth.ProviderRegistrationScreen
import com.example.myapplication.ui.screens.auth.PendingApprovalScreen
import com.example.myapplication.ui.screens.home.ProviderDashboardScreen
import com.example.myapplication.ui.screens.profile.AddressManagementScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.AuthState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(userPreferences: UserPreferences) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val authViewModel: AuthViewModel = viewModel()
    
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
                            if (userRole == "provider") {
                                // Simple check, ideally should check status too, but we can do that in Splash or a router screen
                                navController.navigate("provider_dashboard") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            } else {
                                navController.navigate("home") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        } else {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }
                }
            }
            
            // 2. Role Selection (Shown after OTP if user is new)
            composable("role_selection") {
                RoleSelectionScreen(
                    onRoleSelected = { selectedRole ->
                        if (selectedRole == "PROVIDER") {
                            navController.navigate("provider_registration")
                        } else {
                            navController.navigate("home")
                        }
                    },
                    onDevNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("role_selection") // Avoid deep backstack
                        }
                    }
                )
            }
            
            // 3. Provider Registration
            composable("provider_registration") {
                ProviderRegistrationScreen(
                    authViewModel = authViewModel,
                    onRegistrationSuccess = {
                        navController.navigate("pending_approval") {
                            popUpTo("role_selection") { inclusive = true }
                        }
                    }
                )
            }
            
            // 4. Login
            composable("login") {
                LoginScreen(
                    authViewModel = authViewModel,
                    onContinueClicked = { phoneNumber ->
                        navController.navigate("otp/$phoneNumber")
                    },
                    onDevNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("login") // Avoid deep backstack
                        }
                    }
                )
            }
            
            // 5. OTP Verification
            composable("otp/{phone}") { backStackEntry ->
                val phone = backStackEntry.arguments?.getString("phone") ?: ""
                
                OtpScreen(
                    phoneNumber = phone,
                    authViewModel = authViewModel,
                    onBackClicked = { navController.popBackStack() },
                    onVerifyClicked = { otp ->
                        // The actual verification happens inside OtpScreen via AuthViewModel
                    },
                    onRoleDetermined = { role, status ->
                        scope.launch {
                            if (role != null) {
                                userPreferences.saveLoginState(isLoggedIn = true, role = role)
                                if (role == "provider") {
                                    if (status == "pending") {
                                        navController.navigate("pending_approval") { popUpTo(0) }
                                    } else {
                                        navController.navigate("provider_dashboard") { popUpTo(0) }
                                    }
                                } else {
                                    navController.navigate("home") { popUpTo(0) }
                                }
                            } else {
                                // New User
                                navController.navigate("role_selection") { popUpTo(0) }
                            }
                        }
                    }
                )
            }
            

            // 6. Pending Approval
            composable("pending_approval") {
                PendingApprovalScreen(
                    onLogoutClicked = {
                        scope.launch {
                            userPreferences.clearSession()
                            com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                        }
                        navController.navigate("role_selection") {
                            popUpTo(0)
                        }
                    }
                )
            }
            
            // 7. Provider Dashboard
            composable("provider_dashboard") {
                ProviderDashboardScreen(
                    onLogoutClicked = {
                        scope.launch {
                            userPreferences.clearSession()
                            com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                        }
                        navController.navigate("role_selection") {
                            popUpTo(0)
                        }
                    }
                )
            }
            
            // 8. Profile
            composable("home") {
                androidx.compose.material3.Text("Customer Profile Placeholder")
            }
            
            // 9. Manage Addresses
            composable("manage_addresses") {
                AddressManagementScreen(
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
