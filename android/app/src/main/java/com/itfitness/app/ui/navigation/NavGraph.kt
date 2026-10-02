package com.itfitness.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itfitness.app.ui.screens.HomeScreen
import com.itfitness.app.ui.screens.ScheduleScreen
import com.itfitness.app.ui.screens.TrainersScreen
import com.itfitness.app.ui.screens.ProfileScreen
import com.itfitness.app.ui.screens.AuthScreen
import com.itfitness.app.ui.screens.TariffScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Schedule.route) {
            ScheduleScreen(navController = navController)
        }
        composable(Screen.Trainers.route) {
            TrainersScreen(navController = navController)
        }
        composable("trainer_detail/{trainerId}") { backStackEntry ->
            val trainerId = backStackEntry.arguments?.getString("trainerId") ?: return@composable
            com.itfitness.app.ui.screens.TrainerDetailScreen(navController = navController, trainerId = trainerId)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Auth.route) {
            AuthScreen(navController = navController)
        }
        composable(Screen.Tariff.route) {
            TariffScreen(navController = navController)
        }
    }
}
