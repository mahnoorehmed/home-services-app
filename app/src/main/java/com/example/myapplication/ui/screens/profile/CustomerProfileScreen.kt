package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomerProfileScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onManageAddressesClicked: () -> Unit = {},
    onLogoutClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    // New theme colors
    val amethyst = Color(0xFF967BB6)
    val lavender = Color(0xFFDFDAE6)
    val eggplant = Color(0xFF231D2B)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(lavender) // Lavender background
            .verticalScroll(scrollState)
    ) {
        // TOP HEADER BLOCK
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 32.dp)
        ) {
            Column {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = eggplant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(amethyst.copy(alpha = 0.3f)) // Soft amethyst placeholder
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // User Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mahnoor Ahmed",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = eggplant
                        )
                        Text(
                            text = "+92 3XX XXXXXXX",
                            style = MaterialTheme.typography.bodyMedium,
                            color = eggplant.copy(alpha = 0.7f)
                        )
                    }
                    
                    // Edit pill button
                    Button(
                        onClick = { /* TODO: Edit Profile Action */ },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = amethyst,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Edit", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // CONTENT SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Saved Addresses
            Text(
                text = "Saved Addresses",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = eggplant
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Address Card (Uncolored/Transparent with Amethyst Border)
            OutlinedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, amethyst),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Home",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = eggplant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Street 12, Block B • Lahore",
                            style = MaterialTheme.typography.bodySmall,
                            color = eggplant.copy(alpha = 0.7f)
                        )
                    }
                    
                    // Edit Pill — using TextButton so clicks work reliably
                    TextButton(
                        onClick = onManageAddressesClicked,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = amethyst.copy(alpha = 0.1f),
                            contentColor = amethyst
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Edit",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add New Address Button
            Button(
                onClick = onManageAddressesClicked,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = amethyst,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "+ Add new address",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Settings Section
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = eggplant
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Notifications Button
            Button(
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = amethyst,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            // Help Support Button
            Button(
                onClick = { /* TODO */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = amethyst,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    Text(
                        text = "Help Support",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            // Logout Button
            Button(
                onClick = onLogoutClicked,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = eggplant,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    Text(
                        text = "Log out",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
