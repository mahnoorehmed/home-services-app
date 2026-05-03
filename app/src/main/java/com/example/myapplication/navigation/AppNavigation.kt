package com.example.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
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

    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = Modifier.fillMaxSize()
    ) {
            
            // 1. Splash Screen Configuration
            composable("splash") {
                SplashScreen()
                
                LaunchedEffect(Unit) {
                    delay(1500) // Show logo for 1.5 seconds minimum
                    
                    val currentUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                    
                    if (currentUser != null) {
                        // User is logged into Firebase
                        if (userRole == "provider") {
                            navController.navigate("provider_dashboard") {
                                popUpTo("splash") { inclusive = true }
                            }
                        } else {
                            navController.navigate("home") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    } else {
                        // Not logged in
                        navController.navigate("role_selection") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            }
            
            // 2. Role Selection (First screen for new users)
            composable("role_selection") {
                RoleSelectionScreen(
                    onRoleSelected = { selectedRole ->
                        // Pass the selected role to the login screen
                        val roleString = if (selectedRole == "PROVIDER") "provider" else "customer"
                        navController.navigate("login/$roleString")
                    },
                    onDevNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("role_selection") // Avoid deep backstack
                        }
                    }
                )
            }
            
            // 3. Login
            composable("login/{role}") { backStackEntry ->
                val role = backStackEntry.arguments?.getString("role") ?: "customer"
                LoginScreen(
                    authViewModel = authViewModel,
                    onContinueClicked = { phoneNumber ->
                        navController.navigate("otp/$phoneNumber/$role")
                    },
                    onDevNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("login") // Avoid deep backstack
                        }
                    }
                )
            }
            
            // 4. OTP Verification
            composable("otp/{phone}/{role}") { backStackEntry ->
                val phone = backStackEntry.arguments?.getString("phone") ?: ""
                val role = backStackEntry.arguments?.getString("role") ?: "customer"
                
                OtpScreen(
                    phoneNumber = phone,
                    selectedRole = role,
                    authViewModel = authViewModel,
                    onBackClicked = { navController.popBackStack() },
                    onVerifyClicked = { otp ->
                        // The actual verification happens inside OtpScreen via AuthViewModel
                    },
                    onRoleDetermined = { firebaseRole, status ->
                        scope.launch {
                            // If they selected Customer, we log them in as a Customer for this session
                            // regardless of their Firebase role (dual roles allowed)
                            if (role == "customer") {
                                userPreferences.saveLoginState(isLoggedIn = true, role = "customer")
                                navController.navigate("home") { popUpTo(0) }
                            } else {
                                // They selected Provider
                                if (firebaseRole == "provider") {
                                    userPreferences.saveLoginState(isLoggedIn = true, role = "provider")
                                    if (status == "pending") {
                                        navController.navigate("pending_approval") { popUpTo(0) }
                                    } else {
                                        navController.navigate("provider_dashboard") { popUpTo(0) }
                                    }
                                } else {
                                    // New provider, needs registration
                                    navController.navigate("provider_registration") { popUpTo(0) }
                                }
                            }
                        }
                    }
                )
            }
            
            // 5. Provider Registration
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
            
            // 8. Customer Home (Main Wrapper with Bottom Nav)
            composable("home") {
                com.example.myapplication.ui.screens.home.CustomerMainScreen(
                    onCategoryClicked = { category ->
                        navController.navigate("service_list/$category")
                    },
                    onManageAddressesClicked = {
                        navController.navigate("manage_addresses")
                    },
                    onLogoutClicked = {
                        scope.launch {
                            userPreferences.clearSession()
                            com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                        }
                        navController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                )
            }
            
            // 8b. Service List
            composable("service_list/{category}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                com.example.myapplication.ui.screens.home.ServiceListScreen(
                    category = category,
                    onBackClicked = { navController.popBackStack() },
                    onBookClicked = { service ->
                        // Navigate to booking screen (coming soon)
                    }
                )
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
