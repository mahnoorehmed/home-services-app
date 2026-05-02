package com.example.myapplication.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.AuthViewModel
import com.example.myapplication.viewmodel.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderRegistrationScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    onRegistrationSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    val phoneNumber = authViewModel.currentPhoneNumber // Pre-filled from ViewModel
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("Electrician", "Plumber", "Cleaner", "Appliance Repair", "Beauty Services")
    val selectedCategories = remember { mutableStateListOf<String>() }

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.RoleDetermined) {
            val role = (authState as AuthState.RoleDetermined).role
            if (role == "provider") {
                authViewModel.resetState()
                onRegistrationSuccess()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEFE6FF)) // Solid soft pastel purple
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(72.dp))
            Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                Text(
                    text = "Become a Provider",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E) // Dark text
                )
                Text(
                    text = "Offer services & earn money",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .weight(1f, fill = false),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            placeholder = { Text("Full Name", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedBorderColor = Color(0xFF967BB6)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color(0xFFE0E0E0),
                                disabledTextColor = Color.Gray
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Custom Multi-Select Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = if (selectedCategories.isEmpty()) "Select Service Categories" else selectedCategories.joinToString(", "),
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { expanded = true },
                                enabled = false, // Allows click to pass through to Box
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = Color(0xFFE0E0E0),
                                    disabledTextColor = if(selectedCategories.isEmpty()) Color.Gray else Color.Black
                                )
                            )

                            // To capture clicks overlaying the disabled text field
                            Spacer(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Transparent)
                                    .clickable { expanded = !expanded }
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                            ) {
                                categories.forEach { category ->
                                    val isSelected = selectedCategories.contains(category)
                                    DropdownMenuItem(
                                        text = {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Checkbox(
                                                    checked = isSelected,
                                                    onCheckedChange = null, // Handled by row click
                                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF967BB6))
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(category)
                                            }
                                        },
                                        onClick = {
                                            if (isSelected) selectedCategories.remove(category)
                                            else selectedCategories.add(category)
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        val isLoading = authState is AuthState.Loading
                        Button(
                            onClick = {
                                authViewModel.registerProvider(
                                    name = fullName,
                                    serviceCategories = selectedCategories.joinToString(", ")
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(50),
                            enabled = fullName.isNotBlank() && selectedCategories.isNotEmpty() && !isLoading,
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
                                Text("Continue", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
