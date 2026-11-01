package com.example.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.CalorieCoral
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.NeonPulse
import com.example.ui.theme.PulseCyan
import com.example.viewmodel.FitnessUiState

@Composable
fun ShareableArtifactDialog(
  uiState: FitnessUiState,
  onDismiss: () -> Unit,
) {
  var selectedCardType by remember { mutableIntStateOf(0) } // 0 = Workout Card, 1 = Macro Report, 2 = Buddy Challenge
  var copiedFeedback by remember { mutableStateOf(false) }
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  val completedWorkouts = uiState.workouts.filter { it.completed }
  val trainedMuscles =
    completedWorkouts.map { it.targetMuscle }.distinct().ifEmpty {
      listOf("Biceps", "Triceps", "Abs", "Chest")
    }

  val reportText =
    remember(selectedCardType, uiState) {
      when (selectedCardType) {
        0 ->
          buildString {
            appendLine("⚡ FITPULSE WORKOUT MILESTONE CARD ⚡")
            appendLine("Athlete: ${uiState.activeProfile.avatarEmoji} ${uiState.activeProfile.name} (${uiState.activeProfile.goal})")
            appendLine("🔥 Active Streak: ${uiState.stats.streakDays} Days")
            appendLine("🏋️ Completed Routines: ${completedWorkouts.size}/${uiState.workouts.size}")
            appendLine("💥 Total Burned Today: ${uiState.totalCaloriesBurned} kcal")
            appendLine("💪 Target Muscles Trained: ${trainedMuscles.joinToString(", ")}")
            appendLine("Join me on FitPulse: ${uiState.publicShareUrl} (Code: ${uiState.inviteCode})")
          }
        1 ->
          buildString {
            appendLine("🥗 FITPULSE NUTRITION & MACRO REPORT 🥗")
            appendLine("Athlete: ${uiState.activeProfile.avatarEmoji} ${uiState.activeProfile.name}")
            appendLine("🎯 Calorie Balance: ${uiState.totalCaloriesConsumed} / ${uiState.nutritionSummary.calorieGoal} kcal")
            appendLine("🥩 Protein: ${uiState.totalProteinConsumed}g / ${uiState.nutritionSummary.proteinGoalGrams}g")
            appendLine("🍚 Carbs: ${uiState.totalCarbsConsumed}g | 🥑 Fat: ${uiState.totalFatConsumed}g")
            appendLine("💧 Hydration: ${uiState.stats.waterIntakeMl}/${uiState.stats.waterGoalMl} ml")
            appendLine("Logged Meals (${uiState.meals.size}): ${uiState.meals.joinToString(", ") { "${it.name} (${it.proteinGrams}g P)" }}")
          }
        else ->
          buildString {
            appendLine("🏆 FITPULSE BUDDY LEADERBOARD SNAPSHOT 🏆")
            appendLine("Host Athlete: ${uiState.activeProfile.avatarEmoji} ${uiState.activeProfile.name} • ${uiState.stats.streakDays}d Streak")
            appendLine("Active Household Profiles: ${uiState.userProfiles.size}")
            appendLine("Top Buddies:")
            uiState.buddies.take(4).forEachIndexed { idx, buddy ->
              appendLine("  ${idx + 1}. ${buddy.avatarEmoji} ${buddy.name} — ${buddy.streakDays}d streak (${buddy.status})")
            }
            appendLine("Join Squad Link: ${uiState.publicShareUrl}")
            appendLine("Invite Code: ${uiState.inviteCode}")
          }
      }
    }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      modifier =
        Modifier.fillMaxWidth(0.94f)
          .clip(RoundedCornerShape(24.dp))
          .testTag("dialog_shareable_artifact"),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
    ) {
      Column(
        modifier =
          Modifier.fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
      ) {
        // Header
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
              modifier =
                Modifier.size(40.dp)
                  .clip(CircleShape)
                  .background(Brush.linearGradient(listOf(NeonPulse, PulseCyan))),
              contentAlignment = Alignment.Center,
            ) {
              Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = Color(0xFF003818),
                modifier = Modifier.size(22.dp),
              )
            }
            Column {
              Text(
                text = "Shareable Progress Cards",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              )
              Text(
                text = "Export Workout, Macro & Buddy Artifacts",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("btn_close_artifact_dialog"),
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Card Type Selector Tabs
        Row(
          modifier =
            Modifier.fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
          val tabs = listOf("Workout Card", "Macro Report", "Squad Card")
          tabs.forEachIndexed { index, label ->
            val selected = selectedCardType == index
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (selected) NeonPulse else Color.Transparent,
              modifier =
                Modifier.weight(1f)
                  .clickable {
                    selectedCardType = index
                    copiedFeedback = false
                  }
                  .testTag("artifact_tab_$index"),
            ) {
              Text(
                text = label,
                style =
                  MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                  ),
                color = if (selected) Color(0xFF003818) else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visual Artifact Card Preview
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF101923)),
          modifier =
            Modifier.fillMaxWidth()
              .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(listOf(NeonPulse, PulseCyan)),
                shape = RoundedCornerShape(20.dp),
              )
              .testTag("artifact_preview_card"),
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            // Badge Header inside visual card
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
              ) {
                Text(text = uiState.activeProfile.avatarEmoji, fontSize = 24.sp)
                Column {
                  Text(
                    text = uiState.activeProfile.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color.White,
                  )
                  Text(
                    text = uiState.activeProfile.goal,
                    style = MaterialTheme.typography.labelSmall,
                    color = PulseCyan,
                  )
                }
              }
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = CalorieCoral.copy(alpha = 0.2f),
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                  Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = CalorieCoral,
                    modifier = Modifier.size(15.dp),
                  )
                  Text(
                    text = "${uiState.stats.streakDays}d Streak",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = CalorieCoral,
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.12f))
            Spacer(modifier = Modifier.height(14.dp))

            when (selectedCardType) {
              0 -> {
                // Workout Milestone Preview
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = NeonPulse,
                    modifier = Modifier.size(18.dp),
                  )
                  Text(
                    text = "WORKOUT MILESTONE SUMMARY",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = NeonPulse,
                  )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  ArtifactStatPill(
                    label = "Calories Burned",
                    value = "${uiState.totalCaloriesBurned} kcal",
                    accent = CalorieCoral,
                  )
                  ArtifactStatPill(
                    label = "Completed",
                    value = "${completedWorkouts.size}/${uiState.workouts.size}",
                    accent = NeonPulse,
                  )
                  ArtifactStatPill(
                    label = "Active Mins",
                    value = "${uiState.stats.activeMinutes}m",
                    accent = PulseCyan,
                  )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                  text = "Muscles Targeted: ${trainedMuscles.joinToString(" • ")}",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.White.copy(alpha = 0.85f),
                )
              }
              1 -> {
                // Nutrition & Protein Report Preview
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = PulseCyan,
                    modifier = Modifier.size(18.dp),
                  )
                  Text(
                    text = "DAILY PROTEIN & MACRO REPORT",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = PulseCyan,
                  )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  ArtifactStatPill(
                    label = "Protein",
                    value = "${uiState.totalProteinConsumed}g",
                    accent = NeonPulse,
                  )
                  ArtifactStatPill(
                    label = "Carbs",
                    value = "${uiState.totalCarbsConsumed}g",
                    accent = PulseCyan,
                  )
                  ArtifactStatPill(
                    label = "Healthy Fats",
                    value = "${uiState.totalFatConsumed}g",
                    accent = EnergyAmber,
                  )
                }
                Spacer(modifier = Modifier.height(12.dp))
                val proteinProgress =
                  (uiState.totalProteinConsumed.toFloat() / uiState.nutritionSummary.proteinGoalGrams.coerceAtLeast(1))
                    .coerceIn(0f, 1f)
                LinearProgressIndicator(
                  progress = { proteinProgress },
                  modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                  color = NeonPulse,
                  trackColor = Color.White.copy(alpha = 0.15f),
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "Calorie Intake: ${uiState.totalCaloriesConsumed} / ${uiState.nutritionSummary.calorieGoal} kcal • Water: ${uiState.stats.waterIntakeMl} ml",
                  style = MaterialTheme.typography.labelSmall,
                  color = Color.White.copy(alpha = 0.8f),
                )
              }
              else -> {
                // Buddy Leaderboard Preview
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = EnergyAmber,
                    modifier = Modifier.size(18.dp),
                  )
                  Text(
                    text = "PUBLIC SQUAD & BUDDY LEADERBOARD",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = EnergyAmber,
                  )
                }
                Spacer(modifier = Modifier.height(10.dp))
                uiState.buddies.take(3).forEachIndexed { idx, buddy ->
                  Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                  ) {
                    Text(
                      text = "#${idx + 1} ${buddy.avatarEmoji} ${buddy.name}",
                      style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                      color = Color.White,
                    )
                    Text(
                      text = "${buddy.streakDays}d 🔥 • ${buddy.caloriesBurnedToday} kcal",
                      style = MaterialTheme.typography.labelSmall,
                      color = NeonPulse,
                    )
                  }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = "Invite Code: ${uiState.inviteCode}",
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = PulseCyan,
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = "FITPULSE VERIFIED ARTIFACT",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f),
              )
              Text(
                text = uiState.inviteCode,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = NeonPulse,
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons: Copy Report & Native Share
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          OutlinedButton(
            onClick = {
              clipboardManager.setText(AnnotatedString(reportText))
              copiedFeedback = true
            },
            modifier = Modifier.weight(1f).testTag("btn_copy_artifact_report"),
          ) {
            Icon(
              imageVector = if (copiedFeedback) Icons.Default.Check else Icons.Default.ContentCopy,
              contentDescription = null,
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (copiedFeedback) "Copied!" else "Copy Card")
          }

          Button(
            onClick = {
              val sendIntent =
                Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(Intent.EXTRA_SUBJECT, "My FitPulse Progress Card")
                  putExtra(Intent.EXTRA_TEXT, reportText)
                }
              val shareIntent = Intent.createChooser(sendIntent, "Share FitPulse Artifact Card")
              context.startActivity(shareIntent)
            },
            colors =
              ButtonDefaults.buttonColors(
                containerColor = NeonPulse,
                contentColor = Color(0xFF003818),
              ),
            modifier = Modifier.weight(1f).testTag("btn_share_artifact_card"),
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = null,
              modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Share Card", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
private fun ArtifactStatPill(
  label: String,
  value: String,
  accent: Color,
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = Color.White.copy(alpha = 0.06f),
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = value,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
        color = accent,
      )
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = Color.White.copy(alpha = 0.7f),
      )
    }
  }
}
