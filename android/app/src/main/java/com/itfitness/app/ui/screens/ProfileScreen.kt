package com.itfitness.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.itfitness.app.data.models.PaymentTransaction
import com.itfitness.app.ui.theme.BackgroundLight
import com.itfitness.app.ui.theme.PrimaryPurple
import com.itfitness.app.ui.theme.SurfaceWhite
import com.itfitness.app.ui.theme.TextPrimaryDark
import com.itfitness.app.ui.theme.TextSecondaryGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val userProfile = viewModel.getUserProfile()
    val paymentHistory = viewModel.getPaymentHistory()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Профиль",
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceWhite)
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        if (!isLoggedIn) {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Войдите в профиль",
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Чтобы просматривать баланс и историю платежей",
                    color = TextSecondaryGrey,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { navController.navigate(com.itfitness.app.ui.navigation.Screen.Auth.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("Войти", color = SurfaceWhite)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    BalanceSection(balance = userProfile.balance, remainingSessions = userProfile.remainingSessions)
                    Spacer(modifier = Modifier.height(24.dp))
                }

            item {
                AiProgramBanner()
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SupportChatStub()
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "ИСТОРИЯ ПЛАТЕЖЕЙ",
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(paymentHistory) { transaction ->
                PaymentTransactionItem(transaction = transaction)
            }
        }
        }
    }
}

@Composable
fun BalanceSection(balance: Int, remainingSessions: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ваш баланс",
                color = TextSecondaryGrey,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$balance ₽",
                color = PrimaryPurple,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Осталось тренировок: $remainingSessions",
                color = TextPrimaryDark,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { /* Top up logic */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "ПОПОЛНИТЬ",
                    color = SurfaceWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AiProgramBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(PrimaryPurple, androidx.compose.ui.graphics.Color(0xFF8362C2))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = "ИИ ПРОГРАММА ТРЕНИРОВОК",
                color = SurfaceWhite,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Сгенерируйте персональный план с помощью нейросети",
                color = SurfaceWhite.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SupportChatStub() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Поддержка",
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Напишите нам...", color = TextSecondaryGrey) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = PrimaryPurple
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryPurple,
                    unfocusedBorderColor = TextSecondaryGrey,
                    focusedTextColor = TextPrimaryDark,
                    unfocusedTextColor = TextPrimaryDark
                ),
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}

@Composable
fun PaymentTransactionItem(transaction: PaymentTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = transaction.date,
                color = TextPrimaryDark,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = transaction.status,
                color = PrimaryPurple,
                fontSize = 12.sp
            )
        }
        Text(
            text = "- ${transaction.amount} ₽",
            color = TextPrimaryDark,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}
