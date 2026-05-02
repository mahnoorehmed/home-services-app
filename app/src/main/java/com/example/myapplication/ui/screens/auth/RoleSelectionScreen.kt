package com.example.myapplication.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RoleSelectionScreen(
    modifier: Modifier = Modifier,
    onRoleSelected: (String) -> Unit,
    onDevNavigate: (String) -> Unit = {}
) {
    var showDevMenu by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Welcome to HomeGlow",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "How would you like to use the app?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onRoleSelected("CUSTOMER") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("I am a Customer", style = MaterialTheme.typography.titleMedium)
            }
            
            OutlinedButton(
                onClick = { onRoleSelected("PROVIDER") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("I am a Service Provider", style = MaterialTheme.typography.titleMedium)
            }
        }

        // Developer Debug Menu Button
        TextButton(
            onClick = { showDevMenu = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 32.dp),
            colors = ButtonDefaults.textButtonColors(containerColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f))
        ) {
            Text("🔧 Dev", color = androidx.compose.ui.graphics.Color(0xFF1E1E1E), fontWeight = FontWeight.Black, style = MaterialTheme.typography.labelMedium)
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
