package com.example.model

data class Workout(
  val id: String,
  val title: String,
  val category: String,
  val targetMuscle: String = "Full Body",
  val environment: String = "Home", // "Gym" or "Home"
  val equipment: String = "Bodyweight",
  val durationMinutes: Int,
  val caloriesBurned: Int,
  val intensity: String,
  val completed: Boolean = false,
  val iconName: String = "fitness_center",
  val exercises: List<Exercise> = emptyList(),
)

data class MealItem(
  val id: String,
  val name: String,
  val mealType: MealType,
  val calories: Int,
  val proteinGrams: Int,
  val carbsGrams: Int,
  val fatGrams: Int,
  val time: String,
)

enum class MealType {
  BREAKFAST,
  LUNCH,
  DINNER,
  SNACK,
}

data class DailyNutritionSummary(
  val calorieGoal: Int = 2400,
  val proteinGoalGrams: Int = 160,
  val carbsGoalGrams: Int = 220,
  val fatGoalGrams: Int = 65,
)

data class AiMealRecommendation(
  val id: String,
  val title: String,
  val tag: String,
  val description: String,
  val calories: Int,
  val protein: String,
  val prepTime: String,
)

data class DailyProgressStats(
  val streakDays: Int = 7,
  val activeMinutes: Int = 45,
  val activeMinutesGoal: Int = 60,
  val waterIntakeMl: Int = 2250,
  val waterGoalMl: Int = 3000,
  val stepsCount: Int = 8450,
  val stepsGoal: Int = 10000,
)

data class UserProfile(
  val id: String,
  val name: String,
  val avatarEmoji: String = "⚡",
  val goal: String = "Muscle Growth & Strength",
  val dailyCalorieGoal: Int = 2400,
  val streakDays: Int = 7,
  val completedWorkoutsCount: Int = 12,
  val role: String = "Athlete",
  val maskedEmail: String = "Private Account",
  val authProvider: String = "Email",
  val isPublicProfile: Boolean = true,
)

fun maskEmailAddress(email: String): String {
  val trimmed = email.trim()
  if (trimmed.isEmpty() || !trimmed.contains("@")) return "Private Account"
  val parts = trimmed.split("@")
  val local = parts[0]
  val domain = parts.getOrNull(1) ?: "mail.com"
  val visibleChar = local.firstOrNull() ?: 'u'
  return "${visibleChar}***@$domain"
}

data class BuddyMember(
  val id: String,
  val name: String,
  val avatarEmoji: String,
  val status: String,
  val caloriesBurnedToday: Int,
  val streakDays: Int,
  val isOnline: Boolean = true,
)

enum class SubscriptionTier(val displayName: String, val priceLabel: String, val badgeText: String) {
  FREE("Free Athlete", "$0", "FREE"),
  PRO_MONTHLY("FitPulse Pro Monthly", "$4.99 / mo (₹199)", "PRO"),
  PRO_ANNUAL("FitPulse Pro Annual", "$29.99 / yr (₹999)", "PRO • SAVE 50%"),
  LIFETIME_VIP("FitPulse Lifetime VIP", "$59.99 once (₹2,499)", "VIP LIFETIME"),
}

data class SupplementGearItem(
  val id: String,
  val name: String,
  val category: String, // "Supplements" or "Home Gym Gear"
  val specBadge: String,
  val benefit: String,
  val priceDisplay: String,
  val affiliateCommission: String,
  val emoji: String,
  val storeUrl: String,
  val clicksCount: Int = 0,
)

data class CoachPlanPackage(
  val id: String,
  val title: String,
  val coachSpecialty: String,
  val durationWeeks: Int,
  val priceDisplay: String,
  val deliverables: List<String>,
  val isBooked: Boolean = false,
  val userNotes: String = "",
)

data class CreatorRevenueStats(
  val activeProSubscribers: Int = 48,
  val subscriptionRevenueUsd: Double = 342.50,
  val rewardedAdViews: Int = 1240,
  val adRevenueUsd: Double = 68.20,
  val affiliateClicks: Int = 315,
  val affiliateCommissionUsd: Double = 194.80,
  val coachBookingsCount: Int = 9,
  val coachingRevenueUsd: Double = 441.00,
) {
  val totalEstimatedMonthlyUsd: Double
    get() = subscriptionRevenueUsd + adRevenueUsd + affiliateCommissionUsd + coachingRevenueUsd
}


