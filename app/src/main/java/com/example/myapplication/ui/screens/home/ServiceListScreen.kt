package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.repository.ServiceItem
import com.example.myapplication.viewmodel.ServiceListState
import com.example.myapplication.viewmodel.ServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceListScreen(
    category: String,
    onBackClicked: () -> Unit,
    onBookClicked: (ServiceItem) -> Unit,
    modifier: Modifier = Modifier,
    serviceViewModel: ServiceViewModel = viewModel()
) {
    val serviceState by serviceViewModel.serviceListState.collectAsState()

    // Fetch providers when screen loads
    LaunchedEffect(category) {
        serviceViewModel.fetchProvidersByCategory(category)
    }

    // Figma colors
    val pageBg = Color(0xFFFAF8FF)
    val darkText = Color(0xFF1E1E1E)
    val accentPurple = Color(0xFFC4B5FD)
    val selectedChipBg = Color(0xFFC4B5FD)
    val unselectedChipBg = Color.White
    val chipBorder = Color(0xFFE0E0E0)
    val priceColor = Color(0xFF967BB6)
    val cardIconBg = Color(0xFFE8DFFF)

    // Display title mapping
    val displayTitle = when {
        category.contains("Clean", ignoreCase = true) -> "Home Cleaning"
        category.contains("Plumb", ignoreCase = true) -> "Plumber"
        category.contains("Electric", ignoreCase = true) -> "Electrician"
        category.contains("Beauty", ignoreCase = true) -> "Beauty at Home"
        category.contains("Appliance", ignoreCase = true) -> "Appliance Repair"
        else -> category
    }

    var selectedFilter by remember { mutableStateOf("Popular") }
    val filters = listOf("Popular", "Low Price")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = displayTitle,
                        fontWeight = FontWeight.Bold,
                        color = darkText
                    )
                },
                navigationIcon = {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(40.dp),
                        onClick = onBackClicked,
                        shadowElevation = 1.dp
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.padding(8.dp),
                            tint = darkText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = pageBg),
                modifier = Modifier.statusBarsPadding()
            )
        },
        containerColor = pageBg
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ── FILTER CHIPS ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else darkText
                            )
                        },
                        shape = RoundedCornerShape(50),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = selectedChipBg,
                            containerColor = unselectedChipBg
                        ),
                        border = if (!isSelected) FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = false,
                            borderColor = chipBorder
                        ) else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── SERVICE LIST ──
            when (val state = serviceState) {
                is ServiceListState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF967BB6))
                    }
                }

                is ServiceListState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "😕",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Couldn't load services",
                                style = MaterialTheme.typography.titleMedium,
                                color = darkText
                            )
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF757575),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp)
                            )
                        }
                    }
                }

                is ServiceListState.Success -> {
                    val services = when (selectedFilter) {
                        "Low Price" -> state.services.sortedBy { it.price }
                        else -> state.services // "Popular" — default order
                    }

                    if (services.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "🔍", fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No providers available yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = darkText
                                )
                                Text(
                                    text = "Check back soon!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF757575),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(services) { service ->
                                ServiceCard(
                                    service = service,
                                    priceColor = priceColor,
                                    accentPurple = accentPurple,
                                    cardIconBg = cardIconBg,
                                    onBookClicked = { onBookClicked(service) }
                                )
                            }
                        }
                    }
                }

                is ServiceListState.Idle -> { /* Initial state, do nothing */ }
            }
        }
    }
}

@Composable
private fun ServiceCard(
    service: ServiceItem,
    priceColor: Color,
    accentPurple: Color,
    cardIconBg: Color,
    onBookClicked: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Service icon
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = cardIconBg,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = getServiceEmoji(service.category),
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Service info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.providerName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E1E1E)
                )
                Text(
                    text = "${service.duration} • ${service.description}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = "PKR ${"%,d".format(service.price)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = priceColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Book button
            Button(
                onClick = onBookClicked,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentPurple,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Book",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun getServiceEmoji(category: String): String = when {
    category.contains("Clean", ignoreCase = true) -> "🧹"
    category.contains("Plumb", ignoreCase = true) -> "🔧"
    category.contains("Electric", ignoreCase = true) -> "⚡"
    category.contains("Beauty", ignoreCase = true) -> "💆"
    category.contains("Appliance", ignoreCase = true) -> "🔌"
    else -> "🛠️"
}
