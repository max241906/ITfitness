package com.itfitness.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.itfitness.app.ui.theme.BackgroundLight
import com.itfitness.app.ui.theme.PrimaryPurple
import com.itfitness.app.ui.theme.PrimaryPurpleLight
import com.itfitness.app.ui.theme.SurfaceWhite
import com.itfitness.app.ui.theme.TextPrimaryDark
import com.itfitness.app.ui.theme.TextSecondaryGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerDetailScreen(
    navController: NavController,
    trainerId: String,
    viewModel: MainViewModel = viewModel()
) {
    val trainers by viewModel.trainers.collectAsState()
    val trainer = trainers.find { it.id == trainerId }

    if (trainer == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Тренер не найден")
        }
        return
    }

    var selectedPackage by remember { mutableIntStateOf(1) }
    val packages = listOf(1, 5, 10, 20)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(trainer.name, color = TextPrimaryDark) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад", tint = TextPrimaryDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite)
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                AsyncImage(
                    model = trainer.imageUrl,
                    contentDescription = trainer.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = trainer.specialization,
                    color = PrimaryPurple,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Стаж: ${trainer.experienceYears} лет",
                    color = TextSecondaryGrey,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "О тренере",
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = trainer.bio,
                    color = TextPrimaryDark,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    text = "Выбрать пакет тренировок",
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    packages.forEach { pkg ->
                        val isSelected = selectedPackage == pkg
                        Button(
                            onClick = { selectedPackage = pkg },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) PrimaryPurple else PrimaryPurpleLight,
                                contentColor = if (isSelected) SurfaceWhite else PrimaryPurple
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(pkg.toString())
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                val discount = when (selectedPackage) {
                    1 -> 1.0
                    5 -> 0.95
                    10 -> 0.90
                    20 -> 0.85
                    else -> 1.0
                }
                val totalPrice = (trainer.pricePerSession * selectedPackage * discount).toInt()

                Button(
                    onClick = { /* Handle purchase */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Приобрести за $totalPrice ₽",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
