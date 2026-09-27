package com.itfitness.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.itfitness.app.ui.theme.Graphit
import com.itfitness.app.ui.theme.NeonGreen
import com.itfitness.app.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TariffScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Оформление", color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Graphit)
            )
        },
        containerColor = Graphit
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Экран оформления подписки", color = TextPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("ВЕРНУТЬСЯ", color = Graphit)
            }
        }
    }
}
