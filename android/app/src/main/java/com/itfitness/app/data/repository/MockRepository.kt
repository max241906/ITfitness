package com.itfitness.app.data.repository

import com.itfitness.app.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockRepository {
    private val trainersFlow = MutableStateFlow(
        listOf(
            Trainer("1", "Алексей Иванов", "Кроссфит, Силовые", "Мастер спорта", 1500, 5, "https://i.pravatar.cc/300?img=11"),
            Trainer("2", "Елена Смирнова", "Йога, Пилатес", "Сертифицированный инструктор", 1200, 3, "https://i.pravatar.cc/300?img=5"),
            Trainer("3", "Дмитрий Волков", "Бокс, Кардио", "Чемпион региона", 2000, 8, "https://i.pravatar.cc/300?img=12")
        )
    )

    fun getStories(): List<Story> = listOf(
        Story("1", "https://i.pravatar.cc/150?img=1", "Подбери\nсвой\nтариф", false, 0xFF4B79FF, 0xFF7A9EFF),
        Story("2", "https://i.pravatar.cc/150?img=2", "Как поменять\nклуб", false, 0xFFE97A63, 0xFFF29B88),
        Story("3", "https://i.pravatar.cc/150?img=3", "InBody", false, 0xFF7E57C2, 0xFF9575CD),
        Story("4", "https://i.pravatar.cc/150?img=4", "Первый\nвизит в клуб", false, 0xFF5D438F, 0xFF8362C2)
    )

    fun getBanners(): List<Banner> = listOf(
        Banner("1", "https://picsum.photos/600/300?random=1", ""), // These will be used for the large "DDX Action" banner equivalent
        Banner("2", "https://picsum.photos/600/300?random=2", "")
    )

    fun getTariffs(): List<Tariff> = listOf(
        Tariff("1", "LIGHT", 1900),
        Tariff("2", "LIGHT+", 2400)
    )

    fun getTrainers(): Flow<List<Trainer>> = trainersFlow.asStateFlow()

    fun toggleTrainerLike(trainerId: String) {
        val currentList = trainersFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == trainerId }
        if (index != -1) {
            val trainer = currentList[index]
            currentList[index] = trainer.copy(isLiked = !trainer.isLiked)
            trainersFlow.value = currentList
        }
    }

    fun getGroupClasses(): List<GroupClass> = listOf(
        GroupClass(
            id = "1",
            title = "STEP 45",
            trainerName = "Елена Арканникова",
            trainerAvatarUrl = "https://i.pravatar.cc/150?img=5",
            scheduleTime = "11:00",
            duration = "45 мин",
            location = "DDX Волгоград Акварель",
            zone = "Зона — Зал групповых программ",
            maxSlots = 20,
            bookedSlots = 20,
            isBookedByUser = true
        ),
        GroupClass(
            id = "2",
            title = "Stretching",
            trainerName = "Ларинина Алина",
            trainerAvatarUrl = "https://i.pravatar.cc/150?img=9",
            scheduleTime = "10:00",
            duration = "55 мин",
            location = "DDX Мытищи \"Красный кит\"",
            zone = "Зона — Зал групповых программ",
            maxSlots = 20,
            bookedSlots = 15,
            isBookedByUser = false
        ),
        GroupClass(
            id = "3",
            title = "Smart Start «Красивая осанка»",
            trainerName = "Стеценко Анна",
            trainerAvatarUrl = "https://i.pravatar.cc/150?img=10",
            scheduleTime = "11:30",
            duration = "30 мин",
            location = "DDX Мытищи \"Красный кит\"",
            zone = "Зона — Зал групповых программ",
            maxSlots = 15,
            bookedSlots = 15,
            isBookedByUser = true
        )
    )

    fun getUserProfile(): UserProfile = UserProfile(
        name = "Татьяна",
        balance = 0,
        remainingSessions = 0,
        avatarUrl = "https://i.pravatar.cc/150?img=47",
        subscriptionStatus = "Подписка"
    )

    fun getPaymentHistory(): List<PaymentTransaction> = emptyList()
}

val mockRepository = MockRepository()
