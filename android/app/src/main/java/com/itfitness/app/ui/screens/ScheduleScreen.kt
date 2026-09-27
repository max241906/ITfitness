package com.itfitness.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.itfitness.app.data.models.GroupClass
import com.itfitness.app.ui.theme.PrimaryPurple
import com.itfitness.app.ui.theme.SuccessGreen
import com.itfitness.app.ui.theme.SurfaceWhite
import com.itfitness.app.ui.theme.TextPrimaryDark
import com.itfitness.app.ui.theme.TextSecondaryGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val groupClasses = viewModel.getGroupClasses()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Расписание тренировок",
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimaryDark)
                    }
                },
                actions = {
                    IconButton(onClick = { /* filters */ }) {
                        Icon(imageVector = Icons.Outlined.Tune, contentDescription = "Filters", tint = TextPrimaryDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite)
            )
        },
        containerColor = Color(0xFFF3F4F9)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            DatePickerHeader()

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(groupClasses) { gClass ->
                    ScheduleClassCard(gClass = gClass)
                }
            }
        }
    }
}

@Composable
fun DatePickerHeader() {
    val days = listOf("пт" to "24", "сб" to "25", "вс" to "26", "пн" to "27", "вт" to "28", "ср" to "29", "чт" to "30")
    val selectedIndex = 3 // "пн 27"

    Surface(
        color = SurfaceWhite,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 2.dp
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(days.size) { index ->
                val (dayName, dayNum) = days[index]
                val isSelected = index == selectedIndex

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            if (isSelected) PrimaryPurple else SurfaceWhite,
                            CircleShape
                        )
                        .border(
                            BorderStroke(1.dp, if (isSelected) PrimaryPurple else Color(0xFFEEEEEE)),
                            CircleShape
                        )
                        .clickable { /* Select day */ },
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayName,
                        color = if (isSelected) SurfaceWhite else TextSecondaryGrey,
                        fontSize = 12.sp
                    )
                    Text(
                        text = dayNum,
                        color = if (isSelected) SurfaceWhite else TextPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ScheduleClassCard(gClass: GroupClass) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = gClass.scheduleTime,
                    color = PrimaryPurple,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = gClass.duration,
                    color = TextSecondaryGrey,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = gClass.title,
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = PrimaryPurple,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = gClass.location, color = TextPrimaryDark, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = gClass.trainerAvatarUrl,
                    contentDescription = gClass.trainerName,
                    modifier = Modifier.size(24.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = gClass.trainerName, color = TextPrimaryDark, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (gClass.isBookedByUser) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Есть запись", color = SuccessGreen, fontSize = 14.sp)
                    }

                    OutlinedButton(
                        onClick = { /* cancel */ },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryPurple),
                        border = BorderStroke(1.dp, PrimaryPurple)
                    ) {
                        Text("Отменить")
                    }
                } else {
                    Text(
                        text = "Свободно ${gClass.maxSlots - gClass.bookedSlots} из ${gClass.maxSlots} мест",
                        color = TextSecondaryGrey,
                        fontSize = 14.sp
                    )

                    Button(
                        onClick = { /* book */ },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Записаться")
                    }
                }
            }
        }
    }
}
