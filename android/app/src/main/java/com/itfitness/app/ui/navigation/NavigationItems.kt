package com.itfitness.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Главная", Icons.Default.Home)
    object Schedule : Screen("schedule", "Календарь", Icons.Default.DateRange)
    object Trainers : Screen("trainers", "Тренеры", Icons.Default.Star)
    object Profile : Screen("profile", "Профиль", Icons.Default.Person)
    object Auth : Screen("auth", "Авторизация", Icons.Default.Person)
    object Tariff : Screen("tariff", "Оформление", Icons.Default.Person)
}
