package com.itfitness.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

// Minimal custom icons if needed, using standard for now.
// Note: We use FitnessCenter for trainers (custom approach via composed icons or standard available)
import androidx.compose.material.icons.outlined.FitnessCenter

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Главная", Icons.Default.Home)
    object Schedule : Screen("schedule", "Расписание", Icons.Default.DateRange)
    object Trainers : Screen("trainers", "Тренеры", Icons.Outlined.FitnessCenter)
    object Profile : Screen("profile", "Профиль", Icons.Default.Person)
    object Auth : Screen("auth", "Авторизация", Icons.Default.Person)
    object Tariff : Screen("tariff", "Оформление", Icons.Default.Person)
}
