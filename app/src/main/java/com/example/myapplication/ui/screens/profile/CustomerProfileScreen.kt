package com.example.myapplication.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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

    // Figma Colors
    val headerGradientTop = Color(0xFFE5D5FF)
    val headerGradientBottom = Color(0xFFCDB4FF)
    val pageBg = Color(0xFFFAF8FF)
    val cardBg = Color.White
    val accentPurple = Color(0xFFBCA1FF)
    val darkText = Color(0xFF1E1E1E)
    val grayText = Color(0xFF757575)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(pageBg)
            .verticalScroll(scrollState)
    ) {
        // ── TOP HEADER BLOCK (Purple Gradient) ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(headerGradientTop, headerGradientBottom)
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = darkText
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Avatar (Blueish circle from Figma)
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Color(0xFF8AB4F8),
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // User Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mahnoor Ahmed",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = darkText
                        )
                        Text(
                            text = "+92 3XX XXXXXXX",
                            style = MaterialTheme.typography.bodyMedium,
                            color = darkText.copy(alpha = 0.7f)
                        )
                    }

                    // Edit button
                    Button(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.3f),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Edit",
                            fontWeight = FontWeight.Bold,
                            color = darkText
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── SAVED ADDRESSES ──
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Saved Addresses",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = darkText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Address Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            color = darkText
                        )
                        Text(
                            text = "Street 12, Block B • Lahore",
                            style = MaterialTheme.typography.bodySmall,
                            color = grayText,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF3EEFF),
                        modifier = Modifier.clickable { onManageAddressesClicked() }
                    ) {
                        Text(
                            text = "Edit",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = darkText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add new address button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onManageAddressesClicked() },
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Text(
                    text = "+ Add new address",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = darkText
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ── SETTINGS ──
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = darkText
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Notifications
            SettingItemCard(title = "Notifications")

            Spacer(modifier = Modifier.height(12.dp))

            // Help Support
            SettingItemCard(title = "Help Support")

            Spacer(modifier = Modifier.height(24.dp))

            // Log out
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLogoutClicked() },
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFF5EDFF) // Soft purple logout bg
            ) {
                Text(
                    text = "Log out",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentPurple
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun SettingItemCard(title: String) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable { /* TODO */ },
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E1E1E)
        )
    }
}
