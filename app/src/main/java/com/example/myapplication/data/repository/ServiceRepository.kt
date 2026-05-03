package com.example.myapplication.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Data class representing a service offered by a provider.
 * Currently populated from the provider's Firestore document.
 */
data class ServiceItem(
    val providerId: String = "",
    val providerName: String = "",
    val category: String = "",
    val description: String = "",
    val price: Int = 0,
    val duration: String = "",
    val rating: Double = 0.0
)

class ServiceRepository {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Fetch providers that offer services in the given category.
     * Queries the 'users' collection for providers whose serviceCategory contains the category string.
     * For now fetches all providers (regardless of approval status) for testing.
     * TODO: Add .whereEqualTo("status", "approved") once admin approval flow is live.
     */
    suspend fun getProvidersByCategory(category: String): Result<List<ServiceItem>> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("role", "provider")
                .get()
                .await()

            val services = snapshot.documents.mapNotNull { doc ->
                val serviceCategory = doc.getString("serviceCategory") ?: ""
                // Check if this provider offers services in the requested category
                if (serviceCategory.contains(category, ignoreCase = true)) {
                    val name = doc.getString("name") ?: "Unknown Provider"
                    val uid = doc.id

                    // Generate realistic mock sub-services based on category
                    ServiceItem(
                        providerId = uid,
                        providerName = name,
                        category = category,
                        description = getDefaultDescription(category),
                        price = getDefaultPrice(category),
                        duration = getDefaultDuration(category),
                        rating = 4.5
                    )
                } else {
                    null
                }
            }

            Result.success(services)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all unique categories from registered providers.
     */
    suspend fun getAllCategories(): Result<List<String>> {
        return try {
            val snapshot = db.collection("users")
                .whereEqualTo("role", "provider")
                .get()
                .await()

            val categories = snapshot.documents
                .mapNotNull { it.getString("serviceCategory") }
                .flatMap { it.split(",").map { cat -> cat.trim() } }
                .distinct()

            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Default values until providers specify their own pricing
    private fun getDefaultDescription(category: String): String = when {
        category.contains("Clean", ignoreCase = true) -> "Supplies included"
        category.contains("Plumb", ignoreCase = true) -> "Leaks & fittings"
        category.contains("Electric", ignoreCase = true) -> "Wiring & repairs"
        category.contains("Beauty", ignoreCase = true) -> "Salon services"
        category.contains("Appliance", ignoreCase = true) -> "All appliance types"
        else -> "Professional service"
    }

    private fun getDefaultPrice(category: String): Int = when {
        category.contains("Clean", ignoreCase = true) -> 2500
        category.contains("Plumb", ignoreCase = true) -> 1800
        category.contains("Electric", ignoreCase = true) -> 2000
        category.contains("Beauty", ignoreCase = true) -> 3000
        category.contains("Appliance", ignoreCase = true) -> 2200
        else -> 1500
    }

    private fun getDefaultDuration(category: String): String = when {
        category.contains("Clean", ignoreCase = true) -> "2-3 hours"
        category.contains("Plumb", ignoreCase = true) -> "1-2 hours"
        category.contains("Electric", ignoreCase = true) -> "1-2 hours"
        category.contains("Beauty", ignoreCase = true) -> "1-3 hours"
        category.contains("Appliance", ignoreCase = true) -> "1-2 hours"
        else -> "1-2 hours"
    }
}
