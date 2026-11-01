package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MealType
import com.example.model.Workout
import com.example.ui.theme.CalorieCoral
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.NeonPulse
import com.example.ui.theme.PulseCyan
import com.example.viewmodel.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitPulseApp(
  viewModel: FitnessViewModel,
  modifier: Modifier = Modifier,
) {
  val uiState by viewModel.uiState.collectAsState()
  var showAddWorkoutDialog by remember { mutableStateOf(false) }
  var showLogMealDialog by remember { mutableStateOf(false) }
  var showNutrientAnalyzer by remember { mutableStateOf(false) }
  var showAiMealGenerator by remember { mutableStateOf(false) }
  var showCommunityDialog by remember { mutableStateOf(false) }
  var showArtifactDialog by remember { mutableStateOf(false) }
  var activeWorkoutToPlay by remember { mutableStateOf<Workout?>(null) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        colors =
          TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
          ),
        title = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            Box(
              modifier =
                Modifier.size(36.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(NeonPulse),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = Color(0xFF003818),
                modifier = Modifier.size(20.dp),
              )
            }
            Column {
              Text(
                text = "FitPulse",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              )
              Text(
                text = "Fitness & Nutrition Companion",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        },
        actions = {
          // Profile Switcher Pill
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier =
              Modifier.clickable { showCommunityDialog = true }
                .testTag("btn_top_profile_switcher"),
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
              horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
              Text(text = uiState.activeProfile.avatarEmoji, fontSize = 14.sp)
              Text(
                text = uiState.activeProfile.name.split(" ").firstOrNull() ?: "User",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
              )
            }
          }

          Spacer(modifier = Modifier.width(6.dp))

          // Public Share / Community Icon Button
          IconButton(
            onClick = { showCommunityDialog = true },
            modifier = Modifier.testTag("btn_top_share_community"),
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share App & Community",
              tint = PulseCyan,
              modifier = Modifier.size(20.dp),
            )
          }

          // Streak Pill
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(end = 10.dp),
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            ) {
              Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = "Streak",
                tint = CalorieCoral,
                modifier = Modifier.size(16.dp),
              )
              Spacer(modifier = Modifier.width(2.dp))
              Text(
                text = "${uiState.stats.streakDays}d",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
              )
            }
          }
        },
      )
    },
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("bottom_nav_bar"),
      ) {
        NavigationBarItem(
          selected = uiState.selectedTab == 0,
          onClick = { viewModel.setTab(0) },
          icon = {
            Icon(
              imageVector = if (uiState.selectedTab == 0) Icons.Filled.DateRange else Icons.Outlined.DateRange,
              contentDescription = "Today",
            )
          },
          label = { Text("Today") },
          modifier = Modifier.testTag("nav_tab_today"),
        )
        NavigationBarItem(
          selected = uiState.selectedTab == 1,
          onClick = { viewModel.setTab(1) },
          icon = {
            Icon(
              imageVector = if (uiState.selectedTab == 1) Icons.Filled.FitnessCenter else Icons.Outlined.FitnessCenter,
              contentDescription = "Workouts",
            )
          },
          label = { Text("Workouts") },
          modifier = Modifier.testTag("nav_tab_workouts"),
        )
        NavigationBarItem(
          selected = uiState.selectedTab == 2,
          onClick = { viewModel.setTab(2) },
          icon = {
            Icon(
              imageVector = if (uiState.selectedTab == 2) Icons.Filled.Restaurant else Icons.Outlined.Restaurant,
              contentDescription = "Nutrition",
            )
          },
          label = { Text("Nutrition") },
          modifier = Modifier.testTag("nav_tab_nutrition"),
        )
        NavigationBarItem(
          selected = uiState.selectedTab == 3,
          onClick = { viewModel.setTab(3) },
          icon = {
            Icon(
              imageVector = if (uiState.selectedTab == 3) Icons.Filled.TrendingUp else Icons.Outlined.TrendingUp,
              contentDescription = "Progress",
            )
          },
          label = { Text("Progress") },
          modifier = Modifier.testTag("nav_tab_progress"),
        )
      }
    },
    floatingActionButton = {
      if (uiState.selectedTab == 1) {
        FloatingActionButton(
          onClick = { showAddWorkoutDialog = true },
          containerColor = NeonPulse,
          contentColor = Color(0xFF003818),
          modifier = Modifier.testTag("fab_add_workout"),
        ) {
          Icon(Icons.Default.Add, contentDescription = "Add Workout")
        }
      } else if (uiState.selectedTab == 2) {
        FloatingActionButton(
          onClick = { showLogMealDialog = true },
          containerColor = PulseCyan,
          contentColor = Color(0xFF003444),
          modifier = Modifier.testTag("fab_log_meal"),
        ) {
          Icon(Icons.Default.Add, contentDescription = "Log Meal")
        }
      }
    },
  ) { paddingValues ->
    Box(
      modifier =
        Modifier.fillMaxSize()
          .padding(paddingValues)
          .background(MaterialTheme.colorScheme.background),
    ) {
      when (uiState.selectedTab) {
        0 -> TodayScreen(
          uiState = uiState,
          onToggleWorkout = viewModel::toggleWorkout,
          onStartWorkout = { workout -> activeWorkoutToPlay = workout },
          onAddWater = viewModel::addWaterGlass,
          onNavigateToWorkouts = { viewModel.setTab(1) },
          onNavigateToNutrition = { viewModel.setTab(2) },
          onOpenAiMealGenerator = { showAiMealGenerator = true },
          onOpenNutrientAnalyzer = { showNutrientAnalyzer = true },
          onOpenCommunity = { showCommunityDialog = true },
          onOpenArtifactDialog = { showArtifactDialog = true },
        )
        1 -> WorkoutsScreen(
          workouts = uiState.filteredWorkouts,
          selectedEnvironment = uiState.selectedEnvironment,
          selectedMuscleFilter = uiState.selectedMuscleFilter,
          onSelectEnvironment = viewModel::setEnvironmentFilter,
          onSelectMuscleFilter = viewModel::setMuscleFilter,
          onToggleWorkout = viewModel::toggleWorkout,
          onStartWorkout = { workout -> activeWorkoutToPlay = workout },
        )
        2 -> NutritionScreen(
          uiState = uiState,
          onLogMealClicked = { showLogMealDialog = true },
          onOpenNutrientAnalyzer = { showNutrientAnalyzer = true },
          onOpenAiMealGenerator = { showAiMealGenerator = true },
        )
        3 -> ProgressScreen(
          uiState = uiState,
          onOpenAiMealGenerator = { showAiMealGenerator = true },
          onOpenCommunity = { showCommunityDialog = true },
          onOpenArtifactDialog = { showArtifactDialog = true },
        )
      }
    }
  }

  // Active Animated Workout Session Dialog
  activeWorkoutToPlay?.let { workout ->
    WorkoutSessionDialog(
      workout = workout,
      onDismiss = { activeWorkoutToPlay = null },
      onComplete = { burned ->
        viewModel.completeActiveWorkout(workout.id, burned)
        activeWorkoutToPlay = null
      },
    )
  }

  // Food Nutrient & Protein Analyzer
  if (showNutrientAnalyzer) {
    NutrientAnalyzerDialog(
      onDismiss = { showNutrientAnalyzer = false },
      onLogMeal = { name, type, cal, p, c, f ->
        viewModel.logMeal(name, type, cal, p, c, f)
      },
    )
  }

  // AI Meal Plan Generator
  if (showAiMealGenerator) {
    AiMealPlanGeneratorDialog(
      onDismiss = { showAiMealGenerator = false },
      onApplyPlan = { meals ->
        viewModel.applyGeneratedMeals(meals)
      },
    )
  }

  if (showAddWorkoutDialog) {
    AddWorkoutDialog(
      onDismiss = { showAddWorkoutDialog = false },
      onConfirm = { title, category, targetMuscle, env, duration, calories ->
        viewModel.addWorkout(title, category, targetMuscle, env, duration, calories)
        showAddWorkoutDialog = false
      },
    )
  }

  if (showLogMealDialog) {
    LogMealDialog(
      onDismiss = { showLogMealDialog = false },
      onConfirm = { name, mealType, cal, p, c, f ->
        viewModel.logMeal(name, mealType, cal, p, c, f)
        showLogMealDialog = false
      },
    )
  }

  // Community, Multi-User Profile Switcher & Public Sharing Hub
  if (showCommunityDialog) {
    CommunityAndUsersDialog(
      userProfiles = uiState.userProfiles,
      activeProfileId = uiState.activeProfileId,
      buddies = uiState.buddies,
      publicShareUrl = uiState.publicShareUrl,
      inviteCode = uiState.inviteCode,
      onDismiss = { showCommunityDialog = false },
      onSwitchProfile = viewModel::switchProfile,
      onAddNewUser = viewModel::addNewUser,
      onAddBuddy = viewModel::addBuddy,
    )
  }

  // Shareable Workout & Nutrition Progress Artifact Dialog
  if (showArtifactDialog) {
    ShareableArtifactDialog(
      uiState = uiState,
      onDismiss = { showArtifactDialog = false },
    )
  }
}

@Composable
fun TodayScreen(
  uiState: com.example.viewmodel.FitnessUiState,
  onToggleWorkout: (String) -> Unit,
  onStartWorkout: (Workout) -> Unit,
  onAddWater: () -> Unit,
  onNavigateToWorkouts: () -> Unit,
  onNavigateToNutrition: () -> Unit,
  onOpenAiMealGenerator: () -> Unit,
  onOpenNutrientAnalyzer: () -> Unit,
  onOpenCommunity: () -> Unit = {},
  onOpenArtifactDialog: () -> Unit = {},
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    contentPadding = PaddingValues(vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item {
      // Hero Calorie & Burn Dashboard Card
      Card(
        modifier = Modifier.fillMaxWidth().testTag("hero_dashboard_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "Daily Calorie & Nutrient Balance",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.SemiBold,
            )
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = NeonPulse.copy(alpha = 0.16f),
              modifier =
                Modifier.clickable { onOpenArtifactDialog() }
                  .testTag("btn_export_artifact_today"),
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
              ) {
                Icon(
                  imageVector = Icons.Default.EmojiEvents,
                  contentDescription = null,
                  tint = NeonPulse,
                  modifier = Modifier.size(14.dp),
                )
                Text(
                  text = "Export Card",
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = NeonPulse,
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column {
              Text(
                text = "${uiState.totalCaloriesConsumed}",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
              )
              Text(
                text = "Consumed (kcal)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }

            Box(
              modifier =
                Modifier.height(44.dp)
                  .width(1.dp)
                  .background(MaterialTheme.colorScheme.outline),
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "${uiState.nutritionSummary.calorieGoal}",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
              )
              Text(
                text = "Target Goal",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }

            Box(
              modifier =
                Modifier.height(44.dp)
                  .width(1.dp)
                  .background(MaterialTheme.colorScheme.outline),
            )

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "${uiState.totalCaloriesBurned}",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = CalorieCoral,
              )
              Text(
                text = "Burned (kcal)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          val progress =
            (uiState.totalCaloriesConsumed.toFloat() / uiState.nutritionSummary.calorieGoal.toFloat())
              .coerceIn(0f, 1f)
          LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
            color = NeonPulse,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
          )

          Spacer(modifier = Modifier.height(14.dp))
          // Macro mini summary
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            Text(
              text = "Protein: ${uiState.totalProteinConsumed}/${uiState.nutritionSummary.proteinGoalGrams}g",
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
              color = NeonPulse,
            )
            Text(
              text = "Carbs: ${uiState.totalCarbsConsumed}g",
              style = MaterialTheme.typography.bodySmall,
              color = PulseCyan,
            )
            Text(
              text = "Fat: ${uiState.totalFatConsumed}g",
              style = MaterialTheme.typography.bodySmall,
              color = EnergyAmber,
            )
          }
        }
      }
    }

    // Quick Action Bar: Measure Nutrients & AI Meal Plan
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .weight(1f)
            .clickable { onOpenNutrientAnalyzer() }
            .testTag("action_measure_nutrients"),
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Box(
              modifier = Modifier.size(36.dp).clip(CircleShape).background(PulseCyan.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(Icons.Default.Scale, contentDescription = null, tint = PulseCyan, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = "Measure Food",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              )
              Text(
                text = "Protein & macros",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }

        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier
            .weight(1f)
            .clickable { onOpenAiMealGenerator() }
            .testTag("action_ai_meal_plan"),
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            Box(
              modifier = Modifier.size(36.dp).clip(CircleShape).background(NeonPulse.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonPulse, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = "AI Meal Plan",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              )
              Text(
                text = "Goal recipes",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        }
      }
    }

    // Public Workout Hub & Community Card
    item {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        ),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onOpenCommunity() }
          .testTag("card_community_hub_today"),
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(PulseCyan, NeonPulse))),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = Icons.Default.Public,
                contentDescription = null,
                tint = Color(0xFF003818),
                modifier = Modifier.size(22.dp),
              )
            }
            Column {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
              ) {
                Text(
                  text = "Public Workout Hub",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = NeonPulse.copy(alpha = 0.2f),
                ) {
                  Text(
                    text = "LIVE",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = NeonPulse,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                  )
                }
              }
              Text(
                text = "${uiState.userProfiles.size} Active Athletes • ${uiState.buddies.size} Workout Buddies",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
          Button(
            onClick = onOpenCommunity,
            colors = ButtonDefaults.buttonColors(containerColor = PulseCyan),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("btn_share_invite_today"),
          ) {
            Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF003444), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Invite", color = Color(0xFF003444), fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    item {
      // Water Tracker Quick Card
      Card(
        modifier = Modifier.fillMaxWidth().testTag("water_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      ) {
        Row(
          modifier = Modifier.fillMaxWidth().padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            Box(
              modifier =
                Modifier.size(44.dp)
                  .clip(CircleShape)
                  .background(PulseCyan.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = Icons.Default.Opacity,
                contentDescription = null,
                tint = PulseCyan,
                modifier = Modifier.size(24.dp),
              )
            }
            Column {
              Text(
                text = "Hydration",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
              )
              Text(
                text = "${uiState.stats.waterIntakeMl} / ${uiState.stats.waterGoalMl} ml",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }

          Button(
            onClick = onAddWater,
            colors = ButtonDefaults.buttonColors(containerColor = PulseCyan),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("btn_add_water"),
          ) {
            Text("+250 ml", color = Color(0xFF003444), fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Today's Workouts (Tap Play to Animate)",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
        TextButton(onClick = onNavigateToWorkouts) {
          Text("See All (${uiState.workouts.size})", color = NeonPulse)
        }
      }
    }

    items(uiState.workouts.take(3)) { workout ->
      WorkoutItemCard(
        workout = workout,
        onToggle = { onToggleWorkout(workout.id) },
        onStart = { onStartWorkout(workout) },
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Today's Meals",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
        TextButton(onClick = onNavigateToNutrition) {
          Text("View Nutrition", color = PulseCyan)
        }
      }
    }

    items(uiState.meals.take(3)) { meal ->
      MealItemCard(meal = meal)
    }
  }
}

@Composable
fun WorkoutsScreen(
  workouts: List<Workout>,
  selectedEnvironment: String,
  selectedMuscleFilter: String,
  onSelectEnvironment: (String) -> Unit,
  onSelectMuscleFilter: (String) -> Unit,
  onToggleWorkout: (String) -> Unit,
  onStartWorkout: (Workout) -> Unit,
) {
  val environments = listOf("All", "Gym", "Home")
  val muscleGroups = listOf("All", "Biceps", "Triceps", "Abs", "Chest", "Back", "Shoulders", "Legs")

  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp),
  ) {
    item {
      Text(
        text = "Workout Routines & Exercises",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      )
      Text(
        text = "Gym & Home plans with real-time animated muscle guidance",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      Spacer(modifier = Modifier.height(10.dp))

      // 1. Gym vs Home Environment Tabs
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        environments.forEach { env ->
          val isSelected = selectedEnvironment.equals(env, ignoreCase = true)
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) PulseCyan else MaterialTheme.colorScheme.surfaceVariant,
            modifier =
              Modifier.weight(1f)
                .clickable { onSelectEnvironment(env) }
                .testTag("filter_env_$env"),
          ) {
            Text(
              text = when (env) {
                "Gym" -> "Gym 🏋️"
                "Home" -> "Home 🏠"
                else -> "All Locations"
              },
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              ),
              color = if (isSelected) Color(0xFF003444) else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(vertical = 10.dp),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 2. Muscle Groups Filter Chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
      ) {
        items(muscleGroups) { muscle ->
          val isSelected = selectedMuscleFilter.equals(muscle, ignoreCase = true)
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) NeonPulse.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, NeonPulse) else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
            modifier =
              Modifier.clickable { onSelectMuscleFilter(muscle) }
                .testTag("filter_muscle_$muscle"),
          ) {
            Text(
              text = muscle,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
              ),
              color = if (isSelected) NeonPulse else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Showing ${workouts.size} routine${if (workouts.size == 1) "" else "s"}",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    if (workouts.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        ) {
          Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = PulseCyan, modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text("No workouts match selected filter", style = MaterialTheme.typography.titleSmall)
            Text("Try switching the location or muscle group above.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    } else {
      items(workouts) { workout ->
        WorkoutItemCard(
          workout = workout,
          onToggle = { onToggleWorkout(workout.id) },
          onStart = { onStartWorkout(workout) },
        )
      }
    }
  }
}

@Composable
fun WorkoutItemCard(
  workout: Workout,
  onToggle: () -> Unit,
  onStart: () -> Unit,
) {
  Card(
    modifier =
      Modifier.fillMaxWidth()
        .animateContentSize()
        .testTag("workout_card_${workout.id}"),
    shape = RoundedCornerShape(18.dp),
    colors =
      CardDefaults.cardColors(
        containerColor =
          if (workout.completed)
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
          else MaterialTheme.colorScheme.surface,
      ),
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Box(
          modifier =
            Modifier.size(44.dp)
              .clip(CircleShape)
              .background(
                if (workout.completed) NeonPulse.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.surfaceVariant,
              )
              .clickable { onToggle() },
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector =
              if (workout.completed) Icons.Default.CheckCircle
              else Icons.Outlined.CheckCircleOutline,
            contentDescription = if (workout.completed) "Completed" else "Not completed",
            tint = if (workout.completed) NeonPulse else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp),
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = workout.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color =
              if (workout.completed)
                MaterialTheme.colorScheme.onSurfaceVariant
              else MaterialTheme.colorScheme.onSurface,
          )
          Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 2.dp),
          ) {
            Surface(shape = RoundedCornerShape(6.dp), color = PulseCyan.copy(alpha = 0.18f)) {
              Text(
                text = workout.environment,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = PulseCyan,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              )
            }
            Surface(shape = RoundedCornerShape(6.dp), color = NeonPulse.copy(alpha = 0.18f)) {
              Text(
                text = workout.targetMuscle,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = NeonPulse,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Equipment & Routine info
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column {
          Text(
            text = "Equipment: ${workout.equipment}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "${workout.durationMinutes} mins",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(text = "•", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
              text = "${workout.caloriesBurned} kcal",
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
              color = CalorieCoral,
            )
            Text(text = "•", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
              text = workout.intensity,
              style = MaterialTheme.typography.labelSmall,
              color = EnergyAmber,
            )
          }
        }

        // Start Animated Session Button
        Button(
          onClick = onStart,
          colors = ButtonDefaults.buttonColors(containerColor = NeonPulse),
          shape = RoundedCornerShape(12.dp),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
          modifier = Modifier.testTag("btn_start_workout_${workout.id}"),
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF003818), modifier = Modifier.size(18.dp))
            Text("Start Animation", color = Color(0xFF003818), fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
        }
      }
    }
  }
}

@Composable
fun NutritionScreen(
  uiState: com.example.viewmodel.FitnessUiState,
  onLogMealClicked: () -> Unit,
  onOpenNutrientAnalyzer: () -> Unit,
  onOpenAiMealGenerator: () -> Unit,
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
  ) {
    item {
      Text(
        text = "Nutritional Intake & Analysis",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      )
      Text(
        text = "Measure nutrients, protein, and generate AI meal plans",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    // Two Action Cards: Nutrient Scanner + AI Meal Generator
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
      ) {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = PulseCyan.copy(alpha = 0.15f)),
          modifier = Modifier
            .weight(1f)
            .clickable { onOpenNutrientAnalyzer() }
            .testTag("card_measure_nutrients_screen"),
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Icon(Icons.Default.Scale, contentDescription = null, tint = PulseCyan, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Measure Nutrients", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Text("Calculate Protein, Carbs, Fats per gram", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }

        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = NeonPulse.copy(alpha = 0.15f)),
          modifier = Modifier
            .weight(1f)
            .clickable { onOpenAiMealGenerator() }
            .testTag("card_ai_meals_screen"),
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonPulse, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("AI Meal Generator", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Text("Goal-tailored recipes and macro splits", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          }
        }
      }
    }

    item {
      // Macro breakdown Card
      Card(
        modifier = Modifier.fillMaxWidth().testTag("macros_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Macronutrients Target",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
          )
          Spacer(modifier = Modifier.height(12.dp))

          // Protein
          MacroRow(
            label = "Protein",
            current = uiState.totalProteinConsumed,
            goal = uiState.nutritionSummary.proteinGoalGrams,
            color = NeonPulse,
          )
          Spacer(modifier = Modifier.height(8.dp))

          // Carbs
          MacroRow(
            label = "Carbs",
            current = uiState.totalCarbsConsumed,
            goal = uiState.nutritionSummary.carbsGoalGrams,
            color = PulseCyan,
          )
          Spacer(modifier = Modifier.height(8.dp))

          // Fat
          MacroRow(
            label = "Fats",
            current = uiState.totalFatConsumed,
            goal = uiState.nutritionSummary.fatGoalGrams,
            color = EnergyAmber,
          )
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "Logged Meals (${uiState.meals.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
        TextButton(onClick = onLogMealClicked) {
          Text("+ Quick Log", color = PulseCyan)
        }
      }
    }

    items(uiState.meals) { meal ->
      MealItemCard(meal = meal)
    }
  }
}

@Composable
fun MacroRow(
  label: String,
  current: Int,
  goal: Int,
  color: Color,
) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(text = label, style = MaterialTheme.typography.bodySmall)
      Text(
        text = "$current / ${goal}g",
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    val progress = (current.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
    LinearProgressIndicator(
      progress = { progress },
      modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
      color = color,
      trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
  }
}

@Composable
fun MealItemCard(meal: com.example.model.MealItem) {
  Card(
    modifier = Modifier.fillMaxWidth().testTag("meal_card_${meal.id}"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier =
          Modifier.size(40.dp)
            .clip(CircleShape)
            .background(PulseCyan.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Default.Fastfood,
          contentDescription = null,
          tint = PulseCyan,
          modifier = Modifier.size(20.dp),
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = meal.name,
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        )
        Text(
          text = "${meal.mealType.name.lowercase().replaceFirstChar { it.uppercase() }} • ${meal.time}",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Column(horizontalAlignment = Alignment.End) {
        Text(
          text = "${meal.calories} kcal",
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
          color = CalorieCoral,
        )
        Text(
          text = "${meal.proteinGrams}g P",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = NeonPulse,
        )
      }
    }
  }
}

@Composable
fun ProgressScreen(
  uiState: com.example.viewmodel.FitnessUiState,
  onOpenAiMealGenerator: () -> Unit,
  onOpenCommunity: () -> Unit = {},
  onOpenArtifactDialog: () -> Unit = {},
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Activity & Nutrition Insights",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
          )
          Text(
            text = "Weekly performance and AI meal recommendations",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
        Button(
          onClick = onOpenArtifactDialog,
          colors = ButtonDefaults.buttonColors(containerColor = NeonPulse, contentColor = Color(0xFF003818)),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("btn_export_artifact_progress"),
        ) {
          Icon(Icons.Default.EmojiEvents, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Export Card", fontWeight = FontWeight.Bold)
        }
      }
    }

    item {
      // Stats 2x2 grid
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
      ) {
        Card(
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Daily Steps",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${uiState.stats.stepsCount}",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              color = NeonPulse,
            )
            Text(
              text = "Goal: ${uiState.stats.stepsGoal}",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }

        Card(
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Text(
              text = "Active Time",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "${uiState.stats.activeMinutes}m",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              color = PulseCyan,
            )
            Text(
              text = "Goal: ${uiState.stats.activeMinutesGoal}m",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = "AI Meal Recommendations",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
        TextButton(onClick = onOpenAiMealGenerator) {
          Text("Generate Custom", color = NeonPulse)
        }
      }
    }

    items(uiState.recommendations) { rec ->
      Card(
        modifier = Modifier.fillMaxWidth().testTag("ai_meal_rec_${rec.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = NeonPulse.copy(alpha = 0.15f),
            ) {
              Text(
                text = rec.tag,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = NeonPulse,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              )
            }
            Text(
              text = rec.prepTime,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = rec.title,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = rec.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
              text = "${rec.calories} kcal",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = CalorieCoral,
            )
            Text(
              text = rec.protein,
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = NeonPulse,
            )
          }
        }
      }
    }

    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onOpenCommunity() }
          .testTag("card_community_hub_progress"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
              Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(PulseCyan.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
              ) {
                Icon(Icons.Default.Group, contentDescription = null, tint = PulseCyan, modifier = Modifier.size(20.dp))
              }
              Column {
                Text(
                  text = "Multi-User & Buddies",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                  text = "${uiState.userProfiles.size} Profiles • ${uiState.buddies.size} Friends",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }
            }
            TextButton(
              onClick = onOpenCommunity,
              modifier = Modifier.testTag("btn_manage_community_progress"),
            ) {
              Text("Switch / Add", color = PulseCyan, fontWeight = FontWeight.Bold)
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Active Athlete: ${uiState.activeProfile.avatarEmoji} ${uiState.activeProfile.name} (${uiState.activeProfile.dailyCalorieGoal} kcal target). Invite workout partners or add family members anytime.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }
  }
}

@Composable
fun AddWorkoutDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, category: String, targetMuscle: String, environment: String, duration: Int, calories: Int) -> Unit,
) {
  var title by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Strength") }
  var targetMuscle by remember { mutableStateOf("Biceps") }
  var environment by remember { mutableStateOf("Gym") }
  var durationText by remember { mutableStateOf("30") }
  var caloriesText by remember { mutableStateOf("250") }

  val muscles = listOf("Biceps", "Triceps", "Abs", "Chest", "Back", "Shoulders", "Legs")
  val environments = listOf("Gym", "Home")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add Custom Workout Routine") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Workout Name") },
          modifier = Modifier.fillMaxWidth().testTag("input_workout_title"),
          singleLine = true,
        )

        Text(text = "Target Muscle Group:", style = MaterialTheme.typography.labelSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          items(muscles) { m ->
            val isSel = targetMuscle == m
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSel) NeonPulse else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.clickable { targetMuscle = m },
            ) {
              Text(
                text = m,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                ),
                color = if (isSel) Color(0xFF003818) else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              )
            }
          }
        }

        Text(text = "Environment:", style = MaterialTheme.typography.labelSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          environments.forEach { env ->
            val isSel = environment == env
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSel) PulseCyan else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.clickable { environment = env },
            ) {
              Text(
                text = if (env == "Gym") "Gym 🏋️" else "Home 🏠",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = if (isSel) Color(0xFF003444) else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              )
            }
          }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = durationText,
            onValueChange = { durationText = it },
            label = { Text("Mins") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f).testTag("input_workout_duration"),
            singleLine = true,
          )
          OutlinedTextField(
            value = caloriesText,
            onValueChange = { caloriesText = it },
            label = { Text("Calories (kcal)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1.3f).testTag("input_workout_calories"),
            singleLine = true,
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            val dur = durationText.toIntOrNull() ?: 30
            val cal = caloriesText.toIntOrNull() ?: 200
            onConfirm(title, category, targetMuscle, environment, dur, cal)
          }
        },
        modifier = Modifier.testTag("btn_confirm_add_workout"),
      ) {
        Text("Add Routine")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    },
  )
}

@Composable
fun LogMealDialog(
  onDismiss: () -> Unit,
  onConfirm: (name: String, mealType: MealType, cal: Int, p: Int, c: Int, f: Int) -> Unit,
) {
  var name by remember { mutableStateOf("") }
  var caloriesText by remember { mutableStateOf("450") }
  var proteinText by remember { mutableStateOf("30") }
  var carbsText by remember { mutableStateOf("45") }
  var fatText by remember { mutableStateOf("12") }
  var selectedMealType by remember { mutableStateOf(MealType.LUNCH) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Log Food / Meal") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Food / Meal Description") },
          modifier = Modifier.fillMaxWidth().testTag("input_meal_name"),
          singleLine = true,
        )
        OutlinedTextField(
          value = caloriesText,
          onValueChange = { caloriesText = it },
          label = { Text("Calories (kcal)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth().testTag("input_meal_calories"),
          singleLine = true,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = proteinText,
            onValueChange = { proteinText = it },
            label = { Text("Protein (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true,
          )
          OutlinedTextField(
            value = carbsText,
            onValueChange = { carbsText = it },
            label = { Text("Carbs (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true,
          )
          OutlinedTextField(
            value = fatText,
            onValueChange = { fatText = it },
            label = { Text("Fat (g)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true,
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (name.isNotBlank()) {
            val cal = caloriesText.toIntOrNull() ?: 300
            val p = proteinText.toIntOrNull() ?: 20
            val c = carbsText.toIntOrNull() ?: 30
            val f = fatText.toIntOrNull() ?: 10
            onConfirm(name, selectedMealType, cal, p, c, f)
          }
        },
        modifier = Modifier.testTag("btn_confirm_log_meal"),
      ) {
        Text("Save")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    },
  )
}
