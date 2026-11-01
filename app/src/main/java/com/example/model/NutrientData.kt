package com.example.model

enum class ExerciseType {
  // Arms: Biceps & Triceps
  BICEP_CURLS,
  HAMMER_CURLS,
  TRICEP_DIPS,
  OVERHEAD_TRICEP_EXTENSION,
  
  // Core & Abs
  PLANK,
  BICYCLE_CRUNCHES,
  CRUNCHES,
  
  // Upper Body: Chest, Back & Shoulders
  BENCH_PRESS,
  PUSH_UPS,
  PULL_UPS,
  BENT_OVER_ROWS,
  SHOULDER_PRESS,
  LATERAL_RAISES,
  
  // Lower Body: Legs
  SQUATS,
  BARBELL_SQUATS,
  LUNGES,
  
  // Cardio & Warmup
  JUMPING_JACKS,
  HIGH_KNEES,
}

data class Exercise(
  val name: String,
  val type: ExerciseType,
  val targetMuscle: String,
  val durationSeconds: Int,
  val targetReps: Int,
  val caloriesBurnRate: Double, // kcal per sec
  val equipment: String = "Bodyweight",
  val tips: String,
)

data class FoodNutrientInfo(
  val name: String,
  val servingDescription: String,
  val grams: Int,
  val calories: Int,
  val protein: Double,
  val carbs: Double,
  val fat: Double,
  val fiber: Double,
  val potassiumMg: Int,
  val ironMg: Double,
  val grade: String, // e.g. "A+", "A", "B+"
)

data class GeneratedMealPlan(
  val planTitle: String,
  val dietType: String,
  val totalCalories: Int,
  val totalProtein: Int,
  val totalCarbs: Int,
  val totalFat: Int,
  val meals: List<GeneratedMeal>,
)

data class GeneratedMeal(
  val type: MealType,
  val name: String,
  val calories: Int,
  val protein: Int,
  val carbs: Int,
  val fat: Int,
  val prepTimeMinutes: Int,
  val ingredients: List<String>,
  val instructions: String,
)
