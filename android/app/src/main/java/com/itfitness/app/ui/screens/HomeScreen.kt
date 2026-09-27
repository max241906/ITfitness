package com.itfitness.app.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.itfitness.app.data.models.Banner
import com.itfitness.app.data.models.GroupClass
import com.itfitness.app.data.models.Story
import com.itfitness.app.data.models.UserProfile
import com.itfitness.app.ui.navigation.Screen
import com.itfitness.app.ui.theme.PrimaryPurple
import com.itfitness.app.ui.theme.PrimaryPurpleLight
import com.itfitness.app.ui.theme.SuccessGreen
import com.itfitness.app.ui.theme.SurfaceWhite
import com.itfitness.app.ui.theme.TextPrimaryDark
import com.itfitness.app.ui.theme.TextSecondaryGrey
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val stories = viewModel.getStories()
    val banners = viewModel.getBanners()
    val userProfile = viewModel.getUserProfile()
    val nearestClass = viewModel.getGroupClasses().firstOrNull { it.isBookedByUser }

    Scaffold(
        topBar = {
            HomeTopBar(userProfile, onNotificationsClick = { navController.navigate(Screen.Auth.route) })
        },
        containerColor = SurfaceWhite
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                StoriesSection(stories = stories)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                BannersSection(banners = banners)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                ClubPassButton()
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                if (nearestClass != null) {
                    NearestScheduleSection(nearestClass, onSeeAllClick = { navController.navigate(Screen.Schedule.route) })
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HomeTopBar(userProfile: UserProfile, onNotificationsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = userProfile.avatarUrl,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.LightGray, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = userProfile.name,
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = TextPrimaryDark,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = PrimaryPurple,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = userProfile.subscriptionStatus,
                        color = SurfaceWhite,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        IconButton(
            onClick = onNotificationsClick,
            modifier = Modifier
                .background(PrimaryPurpleLight, RoundedCornerShape(12.dp))
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = PrimaryPurple
            )
        }
    }
}

@Composable
fun StoriesSection(stories: List<Story>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(stories) { story ->
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(story.gradientStart), Color(story.gradientEnd))
                        )
                    )
                    .border(2.dp, PrimaryPurple, RoundedCornerShape(20.dp))
            ) {
                // In a real app we'd load the background image. Here we just show the title for the "quick link" style.
                Text(
                    text = story.title,
                    color = SurfaceWhite,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(12.dp),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannersSection(banners: List<Banner>) {
    if (banners.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { banners.size })

    LaunchedEffect(Unit) {
        while (true) {
            delay(8000)
            val nextPage = (pagerState.currentPage + 1) % banners.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        pageSpacing = 16.dp
    ) { page ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            // Using placeholder color since realistic image is hard to fetch reliably in mock
            Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF3C2DE))) {
                Column(modifier = Modifier.padding(24.dp).fillMaxWidth(0.6f)) {
                    Text(
                        "Видеотренировки DDX Action",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Научись заниматься эффективно и безопасно в зале и дома",
                        fontSize = 12.sp,
                        color = TextPrimaryDark.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceWhite, contentColor = Color(0xFFD66D92))
                    ) {
                        Text("Активировать")
                    }
                }
            }
        }
    }
}

@Composable
fun ClubPassButton() {
    Button(
        onClick = { /* Pass logic */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
        shape = RoundedCornerShape(32.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.QrCodeScanner,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = SurfaceWhite
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Пропуск в клуб",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = SurfaceWhite
            )
        }
    }
}

@Composable
fun NearestScheduleSection(nearestClass: GroupClass, onSeeAllClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onSeeAllClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Расписание",
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Твои ближайшие тренировки",
                    color = TextSecondaryGrey,
                    fontSize = 14.sp
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "See all",
                tint = TextSecondaryGrey
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = nearestClass.scheduleTime,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "08.10, ср",
                        color = TextSecondaryGrey,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = nearestClass.title,
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = nearestClass.location, color = TextPrimaryDark, fontSize = 14.sp)
                        Text(text = nearestClass.zone, color = TextSecondaryGrey, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = nearestClass.trainerAvatarUrl,
                        contentDescription = nearestClass.trainerName,
                        modifier = Modifier.size(24.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = nearestClass.trainerName, color = TextPrimaryDark, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryPurple)
                    ) {
                        Text("Отменить")
                    }
                }
            }
        }
    }
}
