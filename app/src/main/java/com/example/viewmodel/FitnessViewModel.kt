package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.AiMealRecommendation
import com.example.model.BuddyMember
import com.example.model.DailyNutritionSummary
import com.example.model.DailyProgressStats
import com.example.model.Exercise
import com.example.model.ExerciseType
import com.example.model.GeneratedMeal
import com.example.model.MealItem
import com.example.model.MealType
import com.example.model.UserProfile
import com.example.model.Workout
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FitnessUiState(
  val workouts: List<Workout> = emptyList(),
  val meals: List<MealItem> = emptyList(),
  val recommendations: List<AiMealRecommendation> = emptyList(),
  val stats: DailyProgressStats = DailyProgressStats(),
  val nutritionSummary: DailyNutritionSummary = DailyNutritionSummary(),
  val selectedTab: Int = 0,
  val selectedEnvironment: String = "All", // "All", "Gym", "Home"
  val selectedMuscleFilter: String = "All", // "All", "Biceps", "Triceps", "Abs", "Chest", "Back", "Shoulders", "Legs"
  val userProfiles: List<UserProfile> = emptyList(),
  val activeProfileId: String = "u1",
  val buddies: List<BuddyMember> = emptyList(),
  val publicShareUrl: String = "https://ais-pre-eejz7f6w5k4fezdmmiwdyp-133740415274.asia-northeast1.run.app",
  val inviteCode: String = "FIT-PULSE-8924",
) {
  val totalCaloriesBurned: Int
    get() = workouts.filter { it.completed }.sumOf { it.caloriesBurned }

  val totalCaloriesConsumed: Int
    get() = meals.sumOf { it.calories }

  val totalProteinConsumed: Int
    get() = meals.sumOf { it.proteinGrams }

  val totalCarbsConsumed: Int
    get() = meals.sumOf { it.carbsGrams }

  val totalFatConsumed: Int
    get() = meals.sumOf { it.fatGrams }

  val activeProfile: UserProfile
    get() = userProfiles.find { it.id == activeProfileId } ?: userProfiles.firstOrNull() ?: UserProfile("u1", "Athlete", "⚡")

  val filteredWorkouts: List<Workout>
    get() =
      workouts.filter { workout ->
        val matchesEnv =
          selectedEnvironment == "All" ||
            workout.environment.equals(selectedEnvironment, ignoreCase = true) ||
            workout.environment.equals("Both", ignoreCase = true)
        val matchesMuscle =
          selectedMuscleFilter == "All" ||
            workout.targetMuscle.equals(selectedMuscleFilter, ignoreCase = true)
        matchesEnv && matchesMuscle
      }
}

class FitnessViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(FitnessUiState())
  val uiState: StateFlow<FitnessUiState> = _uiState.asStateFlow()

  init {
    loadInitialData()
  }

  private fun loadInitialData() {
    val initialWorkouts =
      listOf(
        // ==========================================
        // 1. BICEPS (GYM & HOME)
        // ==========================================
        Workout(
          id = "w_biceps_gym",
          title = "Gym Dumbbell Biceps Blast",
          category = "Strength",
          targetMuscle = "Biceps",
          environment = "Gym",
          equipment = "Dumbbells",
          durationMinutes = 30,
          caloriesBurned = 240,
          intensity = "High",
          completed = false,
          exercises =
            listOf(
              Exercise("Standing Dumbbell Curls", ExerciseType.BICEP_CURLS, "Biceps", 35, 12, 0.18, "Dumbbells", "Lock elbows against your ribs, squeeze bicep peak for 1 second, controlled negative."),
              Exercise("Hammer Curls (Brachialis)", ExerciseType.HAMMER_CURLS, "Biceps & Forearms", 30, 12, 0.16, "Dumbbells", "Neutral thumbs-up grip builds peak thickness and forearm strength."),
              Exercise("Peak Isometric Curls", ExerciseType.BICEP_CURLS, "Biceps", 30, 10, 0.17, "Dumbbells", "Full range of motion, avoid using lower back momentum."),
            ),
        ),
        Workout(
          id = "w_biceps_home",
          title = "Home Bicep Peak & Arm Sculpt",
          category = "Strength",
          targetMuscle = "Biceps",
          environment = "Home",
          equipment = "Dumbbells / Resistance",
          durationMinutes = 25,
          caloriesBurned = 190,
          intensity = "Medium",
          completed = false,
          exercises =
            listOf(
              Exercise("Home Standing Curls", ExerciseType.BICEP_CURLS, "Biceps", 35, 15, 0.17, "Dumbbells / Band", "Slow 3-second descent for maximum time under tension."),
              Exercise("Cross-Body Hammer Curls", ExerciseType.HAMMER_CURLS, "Biceps", 30, 14, 0.16, "Dumbbells", "Curl across your chest towards opposite shoulder to hit outer bicep head."),
              Exercise("Isometric Bicep Hold", ExerciseType.BICEP_CURLS, "Biceps", 30, 1, 0.15, "Dumbbells", "Hold forearms at 90 degrees with maximum contraction."),
            ),
        ),

        // ==========================================
        // 2. TRICEPS (GYM & HOME)
        // ==========================================
        Workout(
          id = "w_triceps_gym",
          title = "Gym Overhead & Bench Tricep Power",
          category = "Hypertrophy",
          targetMuscle = "Triceps",
          environment = "Gym",
          equipment = "Dumbbell & Bench",
          durationMinutes = 30,
          caloriesBurned = 250,
          intensity = "High",
          completed = false,
          exercises =
            listOf(
              Exercise("Overhead Dumbbell Extension", ExerciseType.OVERHEAD_TRICEP_EXTENSION, "Triceps Long Head", 35, 12, 0.19, "Dumbbell", "Upper arms vertical by ears, full stretch behind neck, lockout at top."),
              Exercise("Weighted Bench Dips", ExerciseType.TRICEP_DIPS, "Triceps Lateral Head", 30, 12, 0.20, "Gym Bench", "Lower to 90 degrees elbow bend, press through heels of your palms."),
              Exercise("Diamond Push-Ups Finisher", ExerciseType.PUSH_UPS, "Triceps & Chest", 30, 12, 0.22, "Bodyweight", "Thumbs and index fingers forming a diamond shape under sternum."),
            ),
        ),
        Workout(
          id = "w_triceps_home",
          title = "Home Chair Dips & Diamond Push-Ups",
          category = "Strength",
          targetMuscle = "Triceps",
          environment = "Home",
          equipment = "Chair / Bodyweight",
          durationMinutes = 20,
          caloriesBurned = 180,
          intensity = "Medium",
          completed = true,
          exercises =
            listOf(
              Exercise("Chair Tricep Dips", ExerciseType.TRICEP_DIPS, "Triceps", 30, 15, 0.18, "Sturdy Chair", "Keep back close to the chair edge, elbows pointing straight back."),
              Exercise("Diamond Floor Push-Ups", ExerciseType.PUSH_UPS, "Triceps", 30, 12, 0.20, "Floor Mat", "Keep elbows tucked to sides, focus load onto triceps."),
              Exercise("Overhead Tricep Extension", ExerciseType.OVERHEAD_TRICEP_EXTENSION, "Triceps", 30, 14, 0.17, "Water Bottle / Dumbbell", "Extend vertically with strict elbow isolation."),
            ),
        ),

        // ==========================================
        // 3. ABS & CORE (GYM & HOME)
        // ==========================================
        Workout(
          id = "w_abs_home",
          title = "Home 6-Pack Abs & Bicycle Crusher",
          category = "Core",
          targetMuscle = "Abs",
          environment = "Home",
          equipment = "Floor Mat",
          durationMinutes = 20,
          caloriesBurned = 175,
          intensity = "High",
          completed = true,
          exercises =
            listOf(
              Exercise("Bicycle Crunches", ExerciseType.BICYCLE_CRUNCHES, "Abs & Obliques", 35, 26, 0.20, "Mat", "Rotate torso fully, bringing armpit toward opposite knee, slow cadence."),
              Exercise("Core Isometric Plank", ExerciseType.PLANK, "Transverse Abdominis", 45, 1, 0.16, "Mat", "Tuck pelvis and brace core like preparing for a heavy impact."),
              Exercise("Abdominal Floor Crunches", ExerciseType.CRUNCHES, "Upper Abs", 30, 20, 0.17, "Mat", "Contract upper abs to pull ribcage toward hips, exhale at top."),
            ),
        ),
        Workout(
          id = "w_abs_gym",
          title = "Gym Weighted Core & Hanging Abs",
          category = "Core",
          targetMuscle = "Abs",
          environment = "Gym",
          equipment = "Gym Mat & Plate",
          durationMinutes = 25,
          caloriesBurned = 210,
          intensity = "High",
          completed = false,
          exercises =
            listOf(
              Exercise("Weighted Isometric Plank", ExerciseType.PLANK, "Core & Lower Back", 45, 1, 0.19, "Weight Plate & Mat", "Plate on upper back, glutes locked, eliminate any hip sagging."),
              Exercise("Dynamic Bicycle Crunches", ExerciseType.BICYCLE_CRUNCHES, "Obliques & Rectus", 35, 28, 0.22, "Mat", "Constant rotational tension on each side."),
              Exercise("High Knees Cardio Core", ExerciseType.HIGH_KNEES, "Lower Abs & Cardio", 30, 45, 0.25, "Floor", "Drive knees up above hip level with energetic core compression."),
            ),
        ),

        // ==========================================
        // 4. CHEST (GYM & HOME)
        // ==========================================
        Workout(
          id = "w_chest_gym",
          title = "Gym Heavy Barbell Bench & Pec Sculpt",
          category = "Strength",
          targetMuscle = "Chest",
          environment = "Gym",
          equipment = "Barbell & Bench",
          durationMinutes = 40,
          caloriesBurned = 340,
          intensity = "High",
          completed = false,
          exercises =
            listOf(
              Exercise("Barbell Flat Bench Press", ExerciseType.BENCH_PRESS, "Pectorals Major", 40, 10, 0.26, "Barbell & Bench", "Plant feet firmly, arch upper back, bar descends to sternum, drive up."),
              Exercise("Standard Barbell Press Dropset", ExerciseType.BENCH_PRESS, "Chest", 35, 12, 0.24, "Barbell", "Smooth continuous tempo squeezing pecs at top."),
              Exercise("Floor Push-Ups Burnout", ExerciseType.PUSH_UPS, "Chest & Core", 30, 20, 0.22, "Floor", "Full depth chest touches floor, lock out pecs."),
            ),
        ),
        Workout(
          id = "w_chest_home",
          title = "Home Push-Up Variations & Chest Burn",
          category = "Hypertrophy",
          targetMuscle = "Chest",
          environment = "Home",
          equipment = "Bodyweight",
          durationMinutes = 25,
          caloriesBurned = 220,
          intensity = "Medium",
          completed = false,
          exercises =
            listOf(
              Exercise("Tempo Chest Push-Ups", ExerciseType.PUSH_UPS, "Chest Pectorals", 35, 16, 0.21, "Bodyweight", "3 seconds down, 1 second pause at bottom, explode up."),
              Exercise("Diamond Chest & Tricep Press", ExerciseType.PUSH_UPS, "Inner Pecs", 30, 12, 0.20, "Bodyweight", "Target inner chest fibers and tricep synergy."),
              Exercise("Isometric Chest Hold", ExerciseType.PLANK, "Chest & Core", 35, 1, 0.16, "Mat", "Hover 2 inches off floor in bottom push-up stance."),
            ),
        ),

        // ==========================================
        // 5. BACK (GYM & HOME)
        // ==========================================
        Workout(
          id = "w_back_gym",
          title = "Gym Deadlifts & Bent-Over Dumbbell Rows",
          category = "Strength",
          targetMuscle = "Back",
          environment = "Gym",
          equipment = "Dumbbells / Barbell",
          durationMinutes = 40,
          caloriesBurned = 360,
          intensity = "High",
          completed = false,
          exercises =
            listOf(
              Exercise("Bent-Over Dumbbell Rows", ExerciseType.BENT_OVER_ROWS, "Lats & Rhomboids", 40, 12, 0.24, "Dumbbells", "Flat spine at 45 degrees, pull elbows to hips, pinch scapula."),
              Exercise("Pull-Up Bar Hypertrophy", ExerciseType.PULL_UPS, "Latissimus Dorsi", 35, 8, 0.28, "Pull-up Bar", "Overhand grip, drive elbows down into floor, chin clears bar."),
              Exercise("Core Stability Plank", ExerciseType.PLANK, "Erector Spinae & Core", 35, 1, 0.16, "Mat", "Protective postural tension for lower back strength."),
            ),
        ),
        Workout(
          id = "w_back_home",
          title = "Home Doorway Pull-Ups & Row Sculpt",
          category = "Strength",
          targetMuscle = "Back",
          environment = "Home",
          equipment = "Pull-up Bar / Dumbbells",
          durationMinutes = 25,
          caloriesBurned = 230,
          intensity = "Medium",
          completed = false,
          exercises =
            listOf(
              Exercise("Bodyweight Pull-Ups", ExerciseType.PULL_UPS, "Back & Lats", 30, 8, 0.27, "Doorway Pull-up Bar", "Controlled hang without swinging, pull with back muscles."),
              Exercise("Single-Arm Dumbbell Rows", ExerciseType.BENT_OVER_ROWS, "Lats", 35, 12, 0.22, "Dumbbell & Chair", "Brace on chair, row dumbbell up to hip pocket."),
            ),
        ),

        // ==========================================
        // 6. SHOULDERS (GYM & HOME)
        // ==========================================
        Workout(
          id = "w_shoulders_gym",
          title = "Gym Overhead Press & Lateral Raises",
          category = "Hypertrophy",
          targetMuscle = "Shoulders",
          environment = "Gym",
          equipment = "Dumbbells",
          durationMinutes = 30,
          caloriesBurned = 260,
          intensity = "High",
          completed = false,
          exercises =
            listOf(
              Exercise("Overhead Dumbbell Shoulder Press", ExerciseType.SHOULDER_PRESS, "Front & Side Delts", 35, 12, 0.22, "Dumbbells", "Press overhead through full lockout without arching lower back."),
              Exercise("Strict Lateral Raises", ExerciseType.LATERAL_RAISES, "Side Deltoids", 30, 15, 0.18, "Dumbbells", "Lead with elbows, parallel to floor, build wide shoulder capping."),
            ),
        ),
        Workout(
          id = "w_shoulders_home",
          title = "Home Deltoid Sculpt & Lateral Burn",
          category = "Strength",
          targetMuscle = "Shoulders",
          environment = "Home",
          equipment = "Dumbbells / Water Bottles",
          durationMinutes = 20,
          caloriesBurned = 180,
          intensity = "Medium",
          completed = false,
          exercises =
            listOf(
              Exercise("High-Rep Lateral Raises", ExerciseType.LATERAL_RAISES, "Lateral Deltoids", 35, 20, 0.18, "Light Dumbbells", "Light weight, high tempo burn, strict form without swinging."),
              Exercise("Seated Overhead Press", ExerciseType.SHOULDER_PRESS, "Deltoids", 35, 14, 0.20, "Dumbbells & Chair", "Seat back straight, press dumbbells smooth overhead."),
            ),
        ),

        // ==========================================
        // 7. LEGS (GYM & HOME)
        // ==========================================
        Workout(
          id = "w_legs_gym",
          title = "Gym Heavy Barbell Squats & Lunges",
          category = "Strength",
          targetMuscle = "Legs",
          environment = "Gym",
          equipment = "Barbell & Rack",
          durationMinutes = 45,
          caloriesBurned = 420,
          intensity = "High",
          completed = false,
          exercises =
            listOf(
              Exercise("Barbell Back Squats", ExerciseType.BARBELL_SQUATS, "Quads & Glutes", 45, 10, 0.30, "Barbell & Rack", "Hip crease below knee, brace core, drive out of the hole with quads."),
              Exercise("Walking Dumbbell Lunges", ExerciseType.LUNGES, "Glutes & Hamstrings", 35, 20, 0.26, "Dumbbells", "Long stride, back knee kisses floor, drive through front heel."),
              Exercise("High Knees Burnout", ExerciseType.HIGH_KNEES, "Leg Explosiveness", 30, 50, 0.27, "Gym Floor", "Fast sprint cadence to empty the tank."),
            ),
        ),
        Workout(
          id = "w_legs_home",
          title = "Home Bodyweight Squats & Jump Lunges",
          category = "Strength",
          targetMuscle = "Legs",
          environment = "Home",
          equipment = "Bodyweight",
          durationMinutes = 25,
          caloriesBurned = 260,
          intensity = "High",
          completed = false,
          exercises =
            listOf(
              Exercise("Deep Bodyweight Squats", ExerciseType.SQUATS, "Quads & Glutes", 40, 25, 0.22, "Bodyweight", "Feet shoulder-width, deep squat, squeeze glutes at top."),
              Exercise("Forward Stepping Lunges", ExerciseType.LUNGES, "Quads & Balance", 35, 20, 0.21, "Bodyweight", "Alternate legs with smooth posture and knee control."),
              Exercise("Jumping Jacks Cardio Finisher", ExerciseType.JUMPING_JACKS, "Full Body Cardio", 30, 40, 0.19, "Bodyweight", "High energy jumping jacks for final metabolic boost."),
            ),
        ),
      )

    val initialMeals =
      listOf(
        MealItem(
          id = "m1",
          name = "Oatmeal with Whey & Berries",
          mealType = MealType.BREAKFAST,
          calories = 480,
          proteinGrams = 38,
          carbsGrams = 62,
          fatGrams = 9,
          time = "08:15 AM",
        ),
        MealItem(
          id = "m2",
          name = "Grilled Chicken Breast with Brown Rice & Avocado",
          mealType = MealType.LUNCH,
          calories = 690,
          proteinGrams = 54,
          carbsGrams = 68,
          fatGrams = 18,
          time = "01:00 PM",
        ),
        MealItem(
          id = "m3",
          name = "Greek Yogurt & Almonds",
          mealType = MealType.SNACK,
          calories = 240,
          proteinGrams = 22,
          carbsGrams = 14,
          fatGrams = 10,
          time = "04:30 PM",
        ),
      )

    val initialRecommendations =
      listOf(
        AiMealRecommendation(
          id = "r1",
          title = "Post-Workout Salmon & Sweet Potato Mash",
          tag = "Optimal Recovery",
          description = "Rich in Omega-3 fatty acids to reduce inflammation with complex carbs for glycogen replenishment.",
          calories = 620,
          protein = "46g Protein",
          prepTime = "25 mins",
        ),
        AiMealRecommendation(
          id = "r2",
          title = "Crispy Tofu & Edamame Quinoa Bowl",
          tag = "Plant-Based Power",
          description = "Complete amino acid profile with micronutrients and iron to sustain high energy levels.",
          calories = 540,
          protein = "34g Protein",
          prepTime = "20 mins",
        ),
        AiMealRecommendation(
          id = "r3",
          title = "Berry Antioxidant Protein Smoothie",
          tag = "Quick Muscle Fuel",
          description = "Fast-digesting whey isolate, blueberries, chia seeds, and unsweetened almond milk.",
          calories = 310,
          protein = "30g Protein",
          prepTime = "5 mins",
        ),
      )

    val initialProfiles =
      listOf(
        UserProfile("u1", "Santosh (Primary)", "⚡", "Muscle Growth & Strength", 2400, 7, 14, "Owner"),
        UserProfile("u2", "Alex (Powerlifter)", "🏋️", "Heavy Strength & Hypertrophy", 2800, 12, 26, "Member"),
        UserProfile("u3", "Elena (CrossFit)", "🔥", "Endurance & Agility", 2100, 5, 9, "Member"),
      )

    val initialBuddies =
      listOf(
        BuddyMember("b1", "Marcus Chen", "⚡", "Completed Leg Day at Gym 🏋️", 420, 14, true),
        BuddyMember("b2", "Sarah Miller", "🧘", "Sunset Yoga & Abs Done ✨", 210, 6, true),
        BuddyMember("b3", "David Park", "🥊", "Morning HIIT 25m 🔥", 310, 9, false),
        BuddyMember("b4", "Chloe Adams", "🏃", "5km Outdoor Run & Hydrated 💧", 380, 21, true),
      )

    _uiState.value =
      FitnessUiState(
        workouts = initialWorkouts,
        meals = initialMeals,
        recommendations = initialRecommendations,
        userProfiles = initialProfiles,
        activeProfileId = "u1",
        buddies = initialBuddies,
      )
  }

  fun switchProfile(profileId: String) {
    _uiState.update { state ->
      val profile = state.userProfiles.find { it.id == profileId }
      val newCalorieGoal = profile?.dailyCalorieGoal ?: state.nutritionSummary.calorieGoal
      state.copy(
        activeProfileId = profileId,
        nutritionSummary = state.nutritionSummary.copy(calorieGoal = newCalorieGoal),
      )
    }
  }

  fun addNewUser(name: String, goal: String, calories: Int, emoji: String) {
    val newId = UUID.randomUUID().toString()
    val newProfile =
      UserProfile(
        id = newId,
        name = name,
        avatarEmoji = emoji,
        goal = goal,
        dailyCalorieGoal = calories,
        streakDays = 1,
        completedWorkoutsCount = 0,
        role = "Member",
      )
    _uiState.update { state ->
      state.copy(
        userProfiles = state.userProfiles + newProfile,
        activeProfileId = newId,
        nutritionSummary = state.nutritionSummary.copy(calorieGoal = calories),
      )
    }
  }

  fun addBuddy(name: String, status: String, emoji: String) {
    val newBuddy =
      BuddyMember(
        id = UUID.randomUUID().toString(),
        name = name,
        avatarEmoji = emoji,
        status = status,
        caloriesBurnedToday = 0,
        streakDays = 1,
        isOnline = true,
      )
    _uiState.update { it.copy(buddies = listOf(newBuddy) + it.buddies) }
  }

  fun setTab(index: Int) {
    _uiState.update { it.copy(selectedTab = index) }
  }

  fun setEnvironmentFilter(env: String) {
    _uiState.update { it.copy(selectedEnvironment = env) }
  }

  fun setMuscleFilter(muscle: String) {
    _uiState.update { it.copy(selectedMuscleFilter = muscle) }
  }

  fun toggleWorkout(id: String) {
    _uiState.update { state ->
      val updated =
        state.workouts.map { workout ->
          if (workout.id == id) workout.copy(completed = !workout.completed) else workout
        }
      state.copy(workouts = updated)
    }
  }

  fun addWorkout(
    title: String,
    category: String,
    targetMuscle: String,
    environment: String,
    durationMinutes: Int,
    calories: Int,
  ) {
    val newWorkout =
      Workout(
        id = UUID.randomUUID().toString(),
        title = title,
        category = category,
        targetMuscle = targetMuscle,
        environment = environment,
        durationMinutes = durationMinutes,
        caloriesBurned = calories,
        intensity = "Medium",
        completed = false,
      )
    _uiState.update { it.copy(workouts = listOf(newWorkout) + it.workouts) }
  }

  fun logMeal(
    name: String,
    mealType: MealType,
    calories: Int,
    protein: Int,
    carbs: Int,
    fat: Int,
  ) {
    val newMeal =
      MealItem(
        id = UUID.randomUUID().toString(),
        name = name,
        mealType = mealType,
        calories = calories,
        proteinGrams = protein,
        carbsGrams = carbs,
        fatGrams = fat,
        time = "Just now",
      )
    _uiState.update { it.copy(meals = listOf(newMeal) + it.meals) }
  }

  fun addWaterGlass() {
    _uiState.update { state ->
      val newAmount = (state.stats.waterIntakeMl + 250).coerceAtMost(5000)
      state.copy(stats = state.stats.copy(waterIntakeMl = newAmount))
    }
  }

  fun completeActiveWorkout(workoutId: String, caloriesBurned: Int) {
    _uiState.update { state ->
      val updatedWorkouts =
        state.workouts.map { workout ->
          if (workout.id == workoutId) {
            workout.copy(
              completed = true,
              caloriesBurned = if (caloriesBurned > 0) caloriesBurned else workout.caloriesBurned,
            )
          } else workout
        }
      val updatedStats =
        state.stats.copy(
          activeMinutes = state.stats.activeMinutes + 25,
          stepsCount = state.stats.stepsCount + 1200,
        )
      state.copy(workouts = updatedWorkouts, stats = updatedStats)
    }
  }

  fun applyGeneratedMeals(generatedMeals: List<GeneratedMeal>) {
    val newMeals =
      generatedMeals.map { meal ->
        MealItem(
          id = UUID.randomUUID().toString(),
          name = meal.name,
          mealType = meal.type,
          calories = meal.calories,
          proteinGrams = meal.protein,
          carbsGrams = meal.carbs,
          fatGrams = meal.fat,
          time = "AI Meal Plan",
        )
      }
    _uiState.update { state ->
      state.copy(meals = newMeals + state.meals)
    }
  }
}
