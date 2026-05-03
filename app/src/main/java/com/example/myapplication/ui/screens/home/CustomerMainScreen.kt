package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class BottomNavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

@Composable
fun CustomerMainScreen(
    modifier: Modifier = Modifier,
    onCategoryClicked: (String) -> Unit,
    onManageAddressesClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    val navItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, "tab_home"),
        BottomNavItem("Bookings", Icons.Filled.DateRange, Icons.Outlined.DateRange, "tab_bookings"),
        BottomNavItem("Profile", Icons.Filled.Person, Icons.Outlined.Person, "tab_profile")
    )

    var selectedTab by remember { mutableIntStateOf(0) }

    val accentPurple = Color(0xFF967BB6)
    val grayText = Color(0xFF757575)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = accentPurple,
                            selectedTextColor = accentPurple,
                            unselectedIconColor = grayText,
                            unselectedTextColor = grayText,
                            indicatorColor = Color(0xFFEFE6FF)
                        )
                    )
                }
            }
        },
        containerColor = Color(0xFFFAF8FF)
    ) { innerPadding ->
        // Show the appropriate screen based on selected tab
        when (selectedTab) {
            0 -> CustomerHomeScreen(
                modifier = Modifier.padding(innerPadding),
                onCategoryClicked = onCategoryClicked
            )
            1 -> BookingsPlaceholderScreen(
                modifier = Modifier.padding(innerPadding)
            )
            2 -> com.example.myapplication.ui.screens.profile.CustomerProfileScreen(
                modifier = Modifier.padding(innerPadding),
                onBackClicked = { selectedTab = 0 },
                onManageAddressesClicked = onManageAddressesClicked,
                onLogoutClicked = onLogoutClicked
            )
        }
    }
}

@Composable
private fun BookingsPlaceholderScreen(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = "📋",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "My Bookings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E)
            )
            Text(
                text = "Coming soon!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF757575)
            )
        }
    }
}


