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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    role: String,
    onContinueClicked: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            // Using a rich Coffee Brown gradient based on user request
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF967BB6), // Amethyst top
                        MaterialTheme.colorScheme.background // Fading into background perfectly
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            // Top App Branding
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                text = "HomeGlow",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White // White contrast against Coffee gradient
            )
            Text(
                text = "Sign in to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha=0.8f),
                modifier = Modifier.padding(top = 8.dp)
            )
            
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
                    Button(
                        onClick = { onContinueClicked(phoneNumber) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(50),
                        enabled = phoneNumber.length >= 10,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF967BB6), // Strong Amethyst
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFDFDAE6),
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text("Send OTP", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    
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
    }
}
