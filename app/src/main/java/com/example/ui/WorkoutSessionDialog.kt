package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Exercise
import com.example.model.ExerciseType
import com.example.model.Workout
import com.example.ui.theme.CalorieCoral
import com.example.ui.theme.DeepDarkBg
import com.example.ui.theme.NeonPulse
import com.example.ui.theme.PulseCyan
import kotlinx.coroutines.delay

@Composable
fun WorkoutSessionDialog(
  workout: Workout,
  onDismiss: () -> Unit,
  onComplete: (caloriesBurned: Int) -> Unit,
) {
  val exerciseList = remember(workout) {
    if (workout.exercises.isNotEmpty()) {
      workout.exercises
    } else {
      when (workout.targetMuscle.lowercase()) {
        "biceps" -> listOf(
          Exercise("Standing Dumbbell Curls", ExerciseType.BICEP_CURLS, "Biceps", 35, 12, 0.18, "Dumbbells", "Pin elbows to ribs, squeeze bicep peak for 1s, don't swing hips."),
          Exercise("Hammer Curls", ExerciseType.HAMMER_CURLS, "Biceps & Forearms", 30, 12, 0.16, "Dumbbells", "Neutral grip with thumbs up to target the brachialis and forearm thickness."),
          Exercise("Concentration Peak Curls", ExerciseType.BICEP_CURLS, "Biceps", 30, 10, 0.17, "Dumbbells", "Lock tricep against inner thigh, isolate bicep without momentum.")
        )
        "triceps" -> listOf(
          Exercise("Overhead Tricep Extension", ExerciseType.OVERHEAD_TRICEP_EXTENSION, "Triceps", 35, 12, 0.19, "Dumbbell", "Keep upper arms vertical next to ears, extend fully at the top."),
          Exercise("Bench Tricep Dips", ExerciseType.TRICEP_DIPS, "Triceps", 30, 15, 0.20, "Bench / Chair", "Lower until elbows hit 90 degrees, press through palms to lockout."),
          Exercise("Diamond Push-Ups", ExerciseType.PUSH_UPS, "Triceps & Chest", 30, 12, 0.22, "Bodyweight", "Bring thumbs and index fingers together to shift load onto lateral tricep head.")
        )
        "abs", "core" -> listOf(
          Exercise("Bicycle Crunches", ExerciseType.BICYCLE_CRUNCHES, "Abs & Obliques", 30, 24, 0.20, "Mat", "Touch opposite elbow to knee slowly with full rotation."),
          Exercise("Core Isometric Plank", ExerciseType.PLANK, "Transverse Abdominis", 45, 1, 0.16, "Mat", "Tuck pelvis, squeeze glutes, pull belly button towards spine."),
          Exercise("Abdominal Floor Crunches", ExerciseType.CRUNCHES, "Upper Abs", 30, 20, 0.18, "Mat", "Lift shoulder blades off mat by contracting abs, do not pull on neck.")
        )
        "chest" -> listOf(
          Exercise("Barbell Flat Bench Press", ExerciseType.BENCH_PRESS, "Chest Pectorals", 40, 10, 0.25, "Barbell & Bench", "Retract scapula, lower bar to mid-chest, drive up with pec squeeze."),
          Exercise("Tempo Floor Push-Ups", ExerciseType.PUSH_UPS, "Chest & Core", 35, 15, 0.22, "Bodyweight", "3-second descent, explosive press up, keep neck in neutral alignment."),
          Exercise("Overhead Tricep & Chest Finisher", ExerciseType.OVERHEAD_TRICEP_EXTENSION, "Chest & Arms", 30, 12, 0.18, "Dumbbells", "Full stretch on chest ribcage and tricep contraction.")
        )
        "back" -> listOf(
          Exercise("Bent-Over Dumbbell Rows", ExerciseType.BENT_OVER_ROWS, "Lats & Rhomboids", 35, 12, 0.23, "Dumbbells", "Hinge at 45 degrees, pull dumbbells towards hips, squeeze shoulder blades."),
          Exercise("Bodyweight Pull-Ups", ExerciseType.PULL_UPS, "Lats & Biceps", 30, 8, 0.27, "Pull-up Bar", "Chest to bar, pull elbows down to floor, avoid kicking legs."),
          Exercise("Plank Core Stabilizer", ExerciseType.PLANK, "Lower Back & Core", 35, 1, 0.15, "Mat", "Keep spine neutral to build protective spinal erector endurance.")
        )
        "shoulders" -> listOf(
          Exercise("Overhead Dumbbell Shoulder Press", ExerciseType.SHOULDER_PRESS, "Anterior & Lateral Deltoids", 35, 12, 0.22, "Dumbbells", "Press overhead into an arch, avoid arching lower spine."),
          Exercise("Standing Lateral Raises", ExerciseType.LATERAL_RAISES, "Side Deltoid Cap", 30, 15, 0.18, "Dumbbells", "Lead with elbows, pause at shoulder height, build the V-taper look.")
        )
        "legs" -> listOf(
          Exercise("Barbell Back Squats", ExerciseType.BARBELL_SQUATS, "Quads & Glutes", 45, 12, 0.28, "Barbell", "Crease of hip below parallel, drive knees out, push through midfoot."),
          Exercise("Walking Dumbbell Lunges", ExerciseType.LUNGES, "Hamstrings & Glutes", 35, 20, 0.24, "Dumbbells", "Take a big step, drop back knee near floor, maintain upright torso."),
          Exercise("Explosive Squat Jumps", ExerciseType.SQUATS, "Leg Power", 30, 15, 0.26, "Bodyweight", "Descend smoothly and explode upward softly absorbing landing.")
        )
        else -> listOf(
          Exercise("Jumping Jacks Warmup", ExerciseType.JUMPING_JACKS, "Full Body Cardio", 30, 25, 0.18, "Bodyweight", "Land softly on balls of feet and maintain rhythmic breathing."),
          Exercise("Barbell Squats", ExerciseType.BARBELL_SQUATS, "Legs & Core", 40, 15, 0.25, "Barbell / Bodyweight", "Push hips back, knees track over toes, keep chest high."),
          Exercise("Standing Bicep Curls", ExerciseType.BICEP_CURLS, "Biceps", 35, 12, 0.18, "Dumbbells", "Elbows tight, rotate wrist up, squeeze biceps."),
          Exercise("Diamond Push-Ups", ExerciseType.PUSH_UPS, "Chest & Triceps", 35, 15, 0.22, "Bodyweight", "Elbows at 45 degrees, full depth, chest touches hands."),
          Exercise("Core Isometric Plank", ExerciseType.PLANK, "Abs & Spine", 40, 1, 0.16, "Mat", "Glutes locked, abs braced like preparing for a punch.")
        )
      }
    }
  }

  var currentExerciseIndex by remember { mutableIntStateOf(0) }
  val currentExercise = exerciseList[currentExerciseIndex]

  var secondsLeft by remember(currentExerciseIndex) { mutableIntStateOf(currentExercise.durationSeconds) }
  var isPlaying by remember { mutableStateOf(true) }
  var totalCaloriesBurned by remember { mutableIntStateOf(0) }

  // Timer loop
  LaunchedEffect(currentExerciseIndex, isPlaying, secondsLeft) {
    if (isPlaying && secondsLeft > 0) {
      delay(1000L)
      secondsLeft -= 1
      totalCaloriesBurned += (currentExercise.caloriesBurnRate * 10).toInt().coerceAtLeast(1)
    } else if (isPlaying && secondsLeft == 0) {
      if (currentExerciseIndex < exerciseList.lastIndex) {
        currentExerciseIndex += 1
      }
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(0.95f).clip(RoundedCornerShape(24.dp)),
      color = DeepDarkBg,
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Column {
            Text(
              text = workout.title,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
              Surface(shape = RoundedCornerShape(6.dp), color = PulseCyan.copy(alpha = 0.2f)) {
                Text(
                  text = workout.environment,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = PulseCyan,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                )
              }
              Surface(shape = RoundedCornerShape(6.dp), color = NeonPulse.copy(alpha = 0.2f)) {
                Text(
                  text = workout.targetMuscle,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = NeonPulse,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                )
              }
              Text(
                text = "• ${currentExerciseIndex + 1}/${exerciseList.size}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8),
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("btn_close_workout_session"),
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Animated Exercise Canvas with Muscle Highlighting & Equipment
        WorkoutAnimator(
          exerciseType = currentExercise.type,
          isPlaying = isPlaying,
          targetMuscleName = currentExercise.targetMuscle,
          modifier = Modifier.size(240.dp).testTag("workout_animator_canvas"),
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = currentExercise.name,
          style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
          color = Color.White,
        )

        Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(top = 2.dp),
        ) {
          Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF1E293B)) {
            Text(
              text = "Equipment: ${currentExercise.equipment}",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF38BDF8),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            )
          }
          Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF1E293B)) {
            Text(
              text = "${currentExercise.targetReps} reps target",
              style = MaterialTheme.typography.labelSmall,
              color = NeonPulse,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            )
          }
        }

        Text(
          text = currentExercise.tips,
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF94A3B8),
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Progress bar for current exercise
        val progress = 1f - (secondsLeft.toFloat() / currentExercise.durationSeconds.toFloat())
        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
          color = NeonPulse,
          trackColor = Color(0xFF1E293B),
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Live stats counters
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
          // Timer Box
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              Icon(Icons.Default.Timer, contentDescription = null, tint = PulseCyan, modifier = Modifier.size(18.dp))
              Text(
                text = "${secondsLeft}s",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
              )
            }
          }

          // Calorie Burn Ticker
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = CalorieCoral, modifier = Modifier.size(18.dp))
              Text(
                text = "$totalCaloriesBurned kcal",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CalorieCoral,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Controls
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          // Play/Pause button
          Button(
            onClick = { isPlaying = !isPlaying },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = NeonPulse),
            modifier = Modifier.size(56.dp).testTag("btn_play_pause_workout"),
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (isPlaying) "Pause" else "Resume",
              tint = Color(0xFF003818),
              modifier = Modifier.size(28.dp),
            )
          }

          Spacer(modifier = Modifier.width(20.dp))

          // Next exercise or Finish
          if (currentExerciseIndex < exerciseList.lastIndex) {
            Button(
              onClick = { currentExerciseIndex += 1 },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.testTag("btn_next_exercise"),
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
              ) {
                Text("Next", color = Color.White)
                Icon(Icons.Default.SkipNext, contentDescription = null, tint = Color.White)
              }
            }
          } else {
            Button(
              onClick = { onComplete(totalCaloriesBurned) },
              colors = ButtonDefaults.buttonColors(containerColor = PulseCyan),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.testTag("btn_finish_workout"),
            ) {
              Text("Complete Workout!", color = Color(0xFF003444), fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}
