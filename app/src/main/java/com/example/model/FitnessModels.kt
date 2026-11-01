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
  val role: String = "Owner",
)

data class BuddyMember(
  val id: String,
  val name: String,
  val avatarEmoji: String,
  val status: String,
  val caloriesBurnedToday: Int,
  val streakDays: Int,
  val isOnline: Boolean = true,
)
