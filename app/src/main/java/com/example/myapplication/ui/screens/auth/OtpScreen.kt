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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    onBackClicked: () -> Unit,
    onVerifyClicked: (String) -> Unit
) {
    var otp by remember { mutableStateOf("") }
    // In actual Firebase auth, you will use PhoneAuthProvider.ForceResendingToken.
    // For now we simulate the state.
    var isResendEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxSize()
            // Using a rich Coffee Brown gradient
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6F4E37), // Coffee Brown top
                        MaterialTheme.colorScheme.background // Fading into background perfectly
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
                    color = Color.White.copy(alpha = 0.2f), // Semi-transparent white to contrast against Coffee
                    modifier = Modifier.size(48.dp),
                    onClick = onBackClicked
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                        contentDescription = "Back",
                        modifier = Modifier.padding(12.dp),
                        tint = Color.White // White Back icon
                    )
                }
                
                Text(
                    text = "Verify OTP",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White, // White text against Coffee
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
                            color = Color(0xFF1E1E1E)
                        )
                        Text(
                            text = "We sent a 4-digit code to $phoneNumber",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF757575),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    // Simulated 4-digit OTP Input using a single outlined text field for simplicity in this demo
                    OutlinedTextField(
                        value = otp,
                        onValueChange = { if(it.length <= 4) otp = it },
                        placeholder = { Text("----", letterSpacing = 8.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFF6F4E37),
                            unfocusedTextColor = Color(0xFF1E1E1E),
                            focusedTextColor = Color(0xFF1E1E1E)
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
                                color = if (isResendEnabled) Color(0xFF6F4E37) else Color(0xFF757575).copy(alpha=0.5f)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Button
                    Button(
                        onClick = { onVerifyClicked(otp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(50),
                        enabled = otp.length == 4,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A3B32), // Strong Brown
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFBCAAA4),
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text("Verify Continue", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
