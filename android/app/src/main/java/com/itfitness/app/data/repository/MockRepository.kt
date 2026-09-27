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
        Story("1", "https://i.pravatar.cc/150?img=1", "Акции"),
        Story("2", "https://i.pravatar.cc/150?img=2", "Новый зал"),
        Story("3", "https://i.pravatar.cc/150?img=3", "Тренеры"),
        Story("4", "https://i.pravatar.cc/150?img=4", "Мерч"),
        Story("5", "https://i.pravatar.cc/150?img=5", "Советы")
    )

    fun getBanners(): List<Banner> = listOf(
        Banner("1", "https://picsum.photos/600/300?random=1", ""),
        Banner("2", "https://picsum.photos/600/300?random=2", ""),
        Banner("3", "https://picsum.photos/600/300?random=3", "")
    )

    fun getTariffs(): List<Tariff> = listOf(
        Tariff("1", "LIGHT", 1900),
        Tariff("2", "LIGHT+", 2400),
        Tariff("3", "MEDIUM", 2800),
        Tariff("4", "ULTRA", 3200)
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
        GroupClass("1", "Morning Yoga", "Елена Смирнова", "10:00", 10, 7),
        GroupClass("2", "Crossfit Hard", "Алексей Иванов", "18:00", 12, 12),
        GroupClass("3", "Boxing Base", "Дмитрий Волков", "19:30", 8, 2)
    )

    fun getUserProfile(): UserProfile = UserProfile("Иван Поддубный", 15000, 4)

    fun getPaymentHistory(): List<PaymentTransaction> = listOf(
        PaymentTransaction("1", "2023-10-01", 3200, "Успешно"),
        PaymentTransaction("2", "2023-09-01", 3200, "Успешно"),
        PaymentTransaction("3", "2023-08-01", 2800, "Успешно")
    )
}

val mockRepository = MockRepository()
