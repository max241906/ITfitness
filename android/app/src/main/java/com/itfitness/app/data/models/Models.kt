package com.itfitness.app.data.models

data class Story(
    val id: String,
    val imageUrl: String,
    val title: String,
    val isViewed: Boolean = false
)

data class Banner(
    val id: String,
    val imageUrl: String,
    val actionUrl: String
)

data class Tariff(
    val id: String,
    val name: String,
    val price: Int,
    val period: String = "месяц"
)

data class Trainer(
    val id: String,
    val name: String,
    val specialization: String,
    val bio: String,
    val pricePerSession: Int,
    val experienceYears: Int,
    val imageUrl: String,
    var isLiked: Boolean = false
)

data class GroupClass(
    val id: String,
    val title: String,
    val trainerName: String,
    val scheduleTime: String,
    val maxSlots: Int,
    val bookedSlots: Int
)

data class PaymentTransaction(
    val id: String,
    val date: String,
    val amount: Int,
    val status: String
)

data class UserProfile(
    val name: String,
    val balance: Int,
    val remainingSessions: Int
)
