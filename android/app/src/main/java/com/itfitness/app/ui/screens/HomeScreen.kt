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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
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
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
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
    val tariffs = viewModel.getTariffs()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val nearestClass = viewModel.getGroupClasses().firstOrNull { it.isBookedByUser }

    Scaffold(
        topBar = {
            HomeTopBar(onNotificationsClick = {
                if (!isLoggedIn) {
                    navController.navigate(Screen.Auth.route)
                }
            })
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
                TariffsSection(tariffs = tariffs, onTariffClick = { navController.navigate(Screen.Tariff.route) })
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                CalendarSummarySection(onDayClick = { navController.navigate(Screen.Schedule.route) })
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                if (nearestClass != null) {
                    NearestScheduleSection(nearestClass, onSeeAllClick = { navController.navigate(Screen.Schedule.route) })
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                MapSection()
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HomeTopBar(onNotificationsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "IT FITNESS",
            color = PrimaryPurple,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )

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


@Composable
fun TariffsSection(tariffs: List<Tariff>, onTariffClick: (Tariff) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "ВЫБЕРИ СВОЙ ТАРИФ",
            color = TextPrimaryDark,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        tariffs.forEach { tariff ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable { onTariffClick(tariff) },
                colors = CardDefaults.cardColors(containerColor = PrimaryPurpleLight),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tariff.name,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "${tariff.price} ₽ / ${tariff.period}",
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Mock remaining tariffs if not present in list
        val extraTariffs = listOf("MEDIUM" to 2800, "ULTRA" to 3200)
        extraTariffs.forEach { (name, price) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable { onTariffClick(Tariff("mock", name, price)) },
                colors = CardDefaults.cardColors(containerColor = PrimaryPurpleLight),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        color = PrimaryPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "$price ₽ / месяц",
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarSummarySection(onDayClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onDayClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Календарь (Ближайшие 7 дней)",
                color = TextPrimaryDark,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "See all",
                tint = TextSecondaryGrey
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        val days = listOf("пн" to "1", "вт" to "2", "ср" to "3", "чт" to "4", "пт" to "5", "сб" to "6", "вс" to "7")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEachIndexed { index, (dayName, dayNum) ->
                val hasEvent = index == 2 || index == 5
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (hasEvent) PrimaryPurple else SurfaceWhite,
                            CircleShape
                        )
                        .border(
                            BorderStroke(1.dp, if (hasEvent) PrimaryPurple else Color(0xFFEEEEEE)),
                            CircleShape
                        )
                        .clickable { onDayClick() },
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayName,
                        color = if (hasEvent) SurfaceWhite else TextSecondaryGrey,
                        fontSize = 10.sp
                    )
                    Text(
                        text = dayNum,
                        color = if (hasEvent) SurfaceWhite else TextPrimaryDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun MapSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Наши клубы",
            color = TextPrimaryDark,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
        ) {
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        setMultiTouchControls(true)
                        val mapController = controller
                        mapController.setZoom(11.0)
                        val startPoint = GeoPoint(55.7558, 37.6173) // Moscow center
                        mapController.setCenter(startPoint)

                        // Add marker
                        val marker = Marker(this)
                        marker.position = startPoint
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = "IT FITNESS (Центральный)"
                        overlays.add(marker)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
