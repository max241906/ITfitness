package com.itfitness.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.itfitness.app.data.models.Trainer
import com.itfitness.app.ui.theme.BackgroundLight
import com.itfitness.app.ui.theme.PrimaryPurple
import com.itfitness.app.ui.theme.SurfaceWhite
import com.itfitness.app.ui.theme.TextPrimaryDark
import com.itfitness.app.ui.theme.TextSecondaryGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainersScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val trainers by viewModel.trainers.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Тренеры",
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite)
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(trainers) { trainer ->
                TrainerCard(trainer = trainer, onClick = {
                    navController.navigate("trainer_detail/${trainer.id}")
                }, onLikeClick = {
                    viewModel.toggleTrainerLike(trainer.id)
                })
            }
        }
    }
}

@Composable
fun TrainerCard(trainer: Trainer, onClick: () -> Unit, onLikeClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = trainer.imageUrl,
                    contentDescription = trainer.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = onLikeClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (trainer.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (trainer.isLiked) Color.Red else Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = trainer.name,
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trainer.specialization,
                    color = PrimaryPurple,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Стаж: ${trainer.experienceYears} лет",
                        color = TextSecondaryGrey,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${trainer.pricePerSession} ₽ / занятие",
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
