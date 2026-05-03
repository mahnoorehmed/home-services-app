package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomerHomeScreen(
    modifier: Modifier = Modifier,
    onCategoryClicked: (String) -> Unit
) {
    // Figma colors
    val headerGradientTop = Color(0xFFB8A9E8)    // Soft purple top
    val headerGradientBottom = Color(0xFFEFE6FF)  // Fades into light lavender
    val cardBg = Color(0xFFF3EEFF)                // Very light purple card bg
    val iconBg = Color(0xFFE8DFFF)                // Icon circle background
    val darkText = Color(0xFF1E1E1E)
    val grayText = Color(0xFF757575)
    val accentPurple = Color(0xFF967BB6)
    val pageBg = Color(0xFFFAF8FF)                // Near-white page background

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(pageBg)
            .verticalScroll(rememberScrollState())
    ) {
        // ── PURPLE HEADER ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(headerGradientTop, headerGradientBottom)
                    )
                )
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 32.dp)
        ) {
            Column {
                Text(
                    text = "Hi, Mahnoor ✨",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = darkText
                )
                Text(
                    text = "What service do you need today?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = darkText.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── PROMO BANNER ──
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Quality Guaranteed",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = darkText
                            )
                            Text(
                                text = "Verified professionals for every home service.",
                                style = MaterialTheme.typography.bodySmall,
                                color = darkText.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        
                        // Icon / Graphic
                        Surface(
                            shape = CircleShape,
                            color = accentPurple.copy(alpha = 0.2f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "🛡️", fontSize = 24.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── CATEGORIES SECTION ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = darkText
            )
            Text(
                text = "See all",
                style = MaterialTheme.typography.labelLarge,
                color = accentPurple
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── CATEGORY GRID (2x2) ──
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Row 1
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryCard(
                    title = "Home Cleaning",
                    subtitle = "Quick & affordable",
                    emoji = "🏠",
                    cardBg = cardBg,
                    iconBg = iconBg,
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClicked("Cleaner") }
                )
                CategoryCard(
                    title = "Plumber",
                    subtitle = "Leaks & fittings",
                    emoji = "🔧",
                    cardBg = cardBg,
                    iconBg = iconBg,
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClicked("Plumber") }
                )
            }

            // Row 2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryCard(
                    title = "Electrician",
                    subtitle = "Wiring & repairs",
                    emoji = "⚡",
                    cardBg = cardBg,
                    iconBg = iconBg,
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClicked("Electrician") }
                )
                CategoryCard(
                    title = "Beauty at Home",
                    subtitle = "Salon services",
                    emoji = "💆",
                    cardBg = cardBg,
                    iconBg = iconBg,
                    modifier = Modifier.weight(1f),
                    onClick = { onCategoryClicked("Beauty Services") }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun CategoryCard(
    title: String,
    subtitle: String,
    emoji: String,
    cardBg: Color,
    iconBg: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Icon circle
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = iconBg,
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = emoji, fontSize = 24.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E)
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF757575),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
