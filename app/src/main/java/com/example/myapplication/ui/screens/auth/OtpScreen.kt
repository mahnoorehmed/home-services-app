package com.example.myapplication.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    selectedRole: String,
    authViewModel: AuthViewModel,
    onBackClicked: () -> Unit,
    onVerifyClicked: (String) -> Unit,
    onRoleDetermined: (String?, String?) -> Unit
) {
    var otp by remember { mutableStateOf("") }
    // In actual Firebase auth, you will use PhoneAuthProvider.ForceResendingToken.
    // For now we simulate the state.
    var isResendEnabled by remember { mutableStateOf(true) }
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.RoleDetermined) {
            val determined = authState as AuthState.RoleDetermined
            authViewModel.resetState()
            onRoleDetermined(determined.role, determined.status)
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // Top App Bar Area
            Spacer(modifier = Modifier.height(48.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    modifier = Modifier.size(48.dp),
                    onClick = onBackClicked,
                    shadowElevation = 2.dp
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Back",
                        modifier = Modifier.padding(12.dp),
                        tint = Color(0xFF231D2B)
                    )
                }
                
                Text(
                    text = "Verify OTP",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF231D2B), // Dark text instead of white
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.size(48.dp)) // balance center
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
                            text = "Enter the code",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF231D2B)
                        )
                        Text(
                            text = "We sent a 6-digit code to $phoneNumber",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF757575),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    // Simulated 6-digit OTP Input using a single outlined text field for simplicity in this demo
                    OutlinedTextField(
                        value = otp,
                        onValueChange = { if(it.length <= 6) otp = it },
                        placeholder = { Text("------", letterSpacing = 8.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFF967BB6),
                            unfocusedTextColor = Color(0xFF231D2B),
                            focusedTextColor = Color(0xFF231D2B)
                        ),
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(letterSpacing = 24.sp, textAlign = TextAlign.Center)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Didn't get it?",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF757575)
                        )
                        TextButton(
                            onClick = { /* TODO: Trigger Firebase ForceResendToken here */ },
                            enabled = isResendEnabled,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Resend OTP",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (isResendEnabled) Color(0xFF967BB6) else Color(0xFF757575).copy(alpha=0.5f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Button
                    val isLoading = authState is AuthState.Loading
                    Button(
                        onClick = { authViewModel.verifyOtp(otp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(50),
                        enabled = otp.length == 6 && !isLoading,
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
                            Text("Verify Continue", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(
                        text = "You can change phone number from Login screen",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF757575),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
