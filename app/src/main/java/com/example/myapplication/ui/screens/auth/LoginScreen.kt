package com.example.myapplication.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onContinueClicked: (String) -> Unit,
    onDevNavigate: (String) -> Unit = {}
) {
    var phoneNumber by remember { mutableStateOf("") }
    var showDevMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current as Activity
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.CodeSent) {
            authViewModel.resetState()
            onContinueClicked(phoneNumber)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEFE6FF), // Soft pastel purple top
                        Color(0xFFD3E3FD)  // Soft pastel blue bottom
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            
            // Top App Branding
            Spacer(modifier = Modifier.height(72.dp))
            Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                Text(
                    text = "HomeGlow",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E) // Dark text
                )
                Text(
                    text = "Sign in to continue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // CRISP WHITE Rounded Box containing the Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .weight(1f, fill = false),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header inside card
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Welcome",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF231D2B) // Eggplant text
                        )
                        Text(
                            text = "Enter your phone number to get an OTP",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF757575), // Gray text
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Text(
                            text = "Phone number",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF231D2B)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    // Text Field
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        placeholder = { Text("+92 3XX XXXXXXX", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFF967BB6),
                            unfocusedTextColor = Color(0xFF231D2B),
                            focusedTextColor = Color(0xFF231D2B)
                        ),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    // Button
                    val isLoading = authState is AuthState.Loading
                    Button(
                        onClick = { authViewModel.sendOtp(phoneNumber, context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(50),
                        enabled = phoneNumber.length >= 10 && !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFC4B5FD), // Soft pastel purple button
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFE0E0E0),
                            disabledContentColor = Color.White
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Send OTP", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Click here to Become a Provider",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF757575),
                        modifier = Modifier.clickable {
                            // User clicked to become provider outside of normal registration. 
                            // Depending on requirements, we can navigate directly. For now, matching UI.
                        }
                    )
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(
                        text = "By continuing, you agree to Terms Privacy",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF757575),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }

        // Developer Debug Menu Button
        TextButton(
            onClick = { showDevMenu = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 32.dp),
            colors = ButtonDefaults.textButtonColors(containerColor = Color.White.copy(alpha = 0.7f))
        ) {
            Text("🔧 Dev", color = Color(0xFF1E1E1E), fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelMedium)
        }

        // Developer Debug Menu Dialog
        if (showDevMenu) {
            AlertDialog(
                onDismissRequest = { showDevMenu = false },
                title = { Text("Developer Debug Menu") },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = { showDevMenu = false; onDevNavigate("login") }, modifier = Modifier.fillMaxWidth()) { Text("Phone Auth Screen") }
                        Button(onClick = { showDevMenu = false; onDevNavigate("otp/1234567890") }, modifier = Modifier.fillMaxWidth()) { Text("OTP Screen") }
                        Button(onClick = { showDevMenu = false; onDevNavigate("role_selection") }, modifier = Modifier.fillMaxWidth()) { Text("Role Selection") }
                        Button(onClick = { showDevMenu = false; onDevNavigate("provider_registration") }, modifier = Modifier.fillMaxWidth()) { Text("Provider Registration") }
                        Button(onClick = { showDevMenu = false; onDevNavigate("pending_approval") }, modifier = Modifier.fillMaxWidth()) { Text("Pending Approval") }
                        Button(onClick = { showDevMenu = false; onDevNavigate("provider_dashboard") }, modifier = Modifier.fillMaxWidth()) { Text("Provider Dashboard / Earnings & Toggle") }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showDevMenu = false }) { Text("Close") }
                }
            )
        }
    }
}
