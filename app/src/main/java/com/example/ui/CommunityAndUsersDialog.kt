package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.BuddyMember
import com.example.model.UserProfile
import com.example.ui.theme.CalorieCoral
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.NeonPulse
import com.example.ui.theme.PulseCyan

@Composable
fun CommunityAndUsersDialog(
  userProfiles: List<UserProfile>,
  activeProfileId: String,
  buddies: List<BuddyMember>,
  publicShareUrl: String,
  inviteCode: String,
  onDismiss: () -> Unit,
  onSwitchProfile: (String) -> Unit,
  onAddNewUser: (name: String, goal: String, calories: Int, emoji: String) -> Unit,
  onAddBuddy: (name: String, status: String, emoji: String) -> Unit,
) {
  val context = LocalContext.current
  var showAddUserDialog by remember { mutableStateOf(false) }
  var showAddBuddyDialog by remember { mutableStateOf(false) }
  var selectedTab by remember { mutableStateOf(0) } // 0: Share & Profiles, 1: Workout Buddies

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(0.95f).height(640.dp).clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface,
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
              modifier = Modifier.size(36.dp).clip(CircleShape).background(PulseCyan.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(Icons.Default.Public, contentDescription = null, tint = PulseCyan, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = "Community & Multi-User",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              )
              Text(
                text = "Share app, invite buddies & switch profiles",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.testTag("btn_close_community_dialog")) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tabs: Share & Users vs Buddies
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (selectedTab == 0) NeonPulse else MaterialTheme.colorScheme.surfaceVariant,
            modifier =
              Modifier.weight(1f).clickable { selectedTab = 0 }.testTag("tab_profiles_share"),
          ) {
            Text(
              text = "Profiles & Public Link",
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
              ),
              color = if (selectedTab == 0) Color(0xFF003818) else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(vertical = 10.dp),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
          }

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (selectedTab == 1) PulseCyan else MaterialTheme.colorScheme.surfaceVariant,
            modifier =
              Modifier.weight(1f).clickable { selectedTab = 1 }.testTag("tab_workout_buddies"),
          ) {
            Text(
              text = "Workout Buddies (${buddies.size})",
              style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
              ),
              color = if (selectedTab == 1) Color(0xFF003444) else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(vertical = 10.dp),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
          LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp),
          ) {
            // Public Share Card
            item {
              Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().testTag("card_public_share"),
              ) {
                Column(modifier = Modifier.padding(16.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                      Icon(Icons.Default.Share, contentDescription = null, tint = PulseCyan, modifier = Modifier.size(20.dp))
                      Text(
                        text = "Public App Access & Link",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                      )
                    }
                    Surface(
                      shape = RoundedCornerShape(8.dp),
                      color = NeonPulse.copy(alpha = 0.2f),
                    ) {
                      Text(
                        text = "PUBLIC READY",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NeonPulse,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = "Share this public URL with friends, gym partners, or athletes so they can use FitPulse:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )

                  Spacer(modifier = Modifier.height(10.dp))

                  // URL box
                  Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF0F172A),
                    modifier = Modifier.fillMaxWidth(),
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                      Text(
                        text = publicShareUrl,
                        style = MaterialTheme.typography.labelSmall,
                        color = PulseCyan,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                      )
                      IconButton(
                        onClick = {
                          val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                          val clip = ClipData.newPlainText("FitPulse URL", publicShareUrl)
                          clipboard.setPrimaryClip(clip)
                          Toast.makeText(context, "Public Link Copied!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(28.dp).testTag("btn_copy_public_url"),
                      ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.White, modifier = Modifier.size(16.dp))
                      }
                    }
                  }

                  Spacer(modifier = Modifier.height(10.dp))

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                  ) {
                    Button(
                      onClick = {
                        val shareIntent = Intent().apply {
                          action = Intent.ACTION_SEND
                          putExtra(
                            Intent.EXTRA_TEXT,
                            "Hey! Check out FitPulse — track gym & home workouts with animations, measure protein/nutrients, and get AI meal plans: $publicShareUrl",
                          )
                          type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share FitPulse App"))
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = PulseCyan),
                      shape = RoundedCornerShape(10.dp),
                      modifier = Modifier.weight(1f).testTag("btn_share_app_intent"),
                    ) {
                      Icon(Icons.Default.Share, contentDescription = null, tint = Color(0xFF003444), modifier = Modifier.size(16.dp))
                      Spacer(modifier = Modifier.width(6.dp))
                      Text("Share App Link", color = Color(0xFF003444), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                      onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Invite Code", inviteCode)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Invite Code $inviteCode Copied!", Toast.LENGTH_SHORT).show()
                      },
                      shape = RoundedCornerShape(10.dp),
                      modifier = Modifier.testTag("btn_copy_invite_code"),
                    ) {
                      Text("Code: $inviteCode", fontSize = 11.sp)
                    }
                  }
                }
              }
            }

            // User Profiles Header
            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                  text = "Switch User / Athlete Profile",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                TextButton(onClick = { showAddUserDialog = true }, modifier = Modifier.testTag("btn_open_add_user")) {
                  Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null, tint = NeonPulse, modifier = Modifier.size(16.dp))
                    Text("+ Add User", color = NeonPulse, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }

            // Profiles list
            items(userProfiles) { profile ->
              val isCurrent = profile.id == activeProfileId
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                  containerColor = if (isCurrent) NeonPulse.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                ),
                border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.5.dp, NeonPulse) else null,
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onSwitchProfile(profile.id) }
                  .testTag("profile_item_${profile.id}"),
              ) {
                Row(
                  modifier = Modifier.padding(14.dp).fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                  ) {
                    Box(
                      modifier = Modifier.size(42.dp).clip(CircleShape).background(Color(0xFF0F172A)),
                      contentAlignment = Alignment.Center,
                    ) {
                      Text(text = profile.avatarEmoji, fontSize = 20.sp)
                    }
                    Column {
                      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                          text = profile.name,
                          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                          color = if (isCurrent) NeonPulse else MaterialTheme.colorScheme.onSurface,
                        )
                        if (isCurrent) {
                          Surface(shape = RoundedCornerShape(4.dp), color = NeonPulse) {
                            Text("ACTIVE", color = Color(0xFF003818), fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                          }
                        }
                      }
                      Text(
                        text = "${profile.goal} • ${profile.dailyCalorieGoal} kcal goal",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                      )
                    }
                  }

                  if (isCurrent) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = NeonPulse)
                  }
                }
              }
            }
          }
        } else {
          // Workout Buddies Section
          LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
          ) {
            item {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                  text = "Workout Partners & Team",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                )
                TextButton(onClick = { showAddBuddyDialog = true }, modifier = Modifier.testTag("btn_open_add_buddy")) {
                  Text("+ Add Buddy", color = PulseCyan, fontWeight = FontWeight.Bold)
                }
              }
            }

            items(buddies) { buddy ->
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth(),
              ) {
                Row(
                  modifier = Modifier.padding(14.dp).fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                  ) {
                    Box(
                      modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF0F172A)),
                      contentAlignment = Alignment.Center,
                    ) {
                      Text(text = buddy.avatarEmoji, fontSize = 18.sp)
                    }
                    Column {
                      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                          text = buddy.name,
                          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        )
                        Box(
                          modifier = Modifier.size(8.dp).clip(CircleShape).background(if (buddy.isOnline) NeonPulse else Color.Gray),
                        )
                      }
                      Text(
                        text = buddy.status,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                      )
                    }
                  }

                  Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                      Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = CalorieCoral, modifier = Modifier.size(14.dp))
                      Text(
                        text = "${buddy.streakDays}d",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = CalorieCoral,
                      )
                    }
                    Text(
                      text = "${buddy.caloriesBurnedToday} kcal",
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  // Add User Dialog
  if (showAddUserDialog) {
    var newUserName by remember { mutableStateOf("") }
    var newUserGoal by remember { mutableStateOf("Muscle Hypertrophy") }
    var newUserCalories by remember { mutableStateOf("2500") }
    val emojiOptions = listOf("⚡", "🏋️", "🔥", "🥊", "🧘", "🏃", "🏆")
    var selectedEmoji by remember { mutableStateOf("⚡") }

    AlertDialog(
      onDismissRequest = { showAddUserDialog = false },
      title = { Text("Add User / Athlete Profile") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          OutlinedTextField(
            value = newUserName,
            onValueChange = { newUserName = it },
            label = { Text("Athlete / User Name") },
            modifier = Modifier.fillMaxWidth().testTag("input_new_user_name"),
            singleLine = true,
          )

          OutlinedTextField(
            value = newUserGoal,
            onValueChange = { newUserGoal = it },
            label = { Text("Primary Fitness Goal") },
            modifier = Modifier.fillMaxWidth().testTag("input_new_user_goal"),
            singleLine = true,
          )

          OutlinedTextField(
            value = newUserCalories,
            onValueChange = { newUserCalories = it },
            label = { Text("Daily Calorie Target (kcal)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().testTag("input_new_user_calories"),
            singleLine = true,
          )

          Text("Choose Avatar Emoji:", style = MaterialTheme.typography.labelSmall)
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(emojiOptions) { emoji ->
              val isSel = selectedEmoji == emoji
              Surface(
                shape = CircleShape,
                color = if (isSel) NeonPulse.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant,
                border = if (isSel) androidx.compose.foundation.BorderStroke(1.5.dp, NeonPulse) else null,
                modifier = Modifier.clickable { selectedEmoji = emoji },
              ) {
                Text(text = emoji, fontSize = 20.sp, modifier = Modifier.padding(8.dp))
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (newUserName.isNotBlank()) {
              val cal = newUserCalories.toIntOrNull() ?: 2400
              onAddNewUser(newUserName, newUserGoal, cal, selectedEmoji)
              showAddUserDialog = false
            }
          },
          modifier = Modifier.testTag("btn_confirm_create_user"),
        ) {
          Text("Add Profile")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddUserDialog = false }) {
          Text("Cancel")
        }
      },
    )
  }

  // Add Buddy Dialog
  if (showAddBuddyDialog) {
    var buddyName by remember { mutableStateOf("") }
    var buddyStatus by remember { mutableStateOf("Joined FitPulse community 🏋️") }

    AlertDialog(
      onDismissRequest = { showAddBuddyDialog = false },
      title = { Text("Invite Workout Buddy") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = buddyName,
            onValueChange = { buddyName = it },
            label = { Text("Friend's Name / Username") },
            modifier = Modifier.fillMaxWidth().testTag("input_buddy_name"),
            singleLine = true,
          )
          OutlinedTextField(
            value = buddyStatus,
            onValueChange = { buddyStatus = it },
            label = { Text("Activity / Note") },
            modifier = Modifier.fillMaxWidth().testTag("input_buddy_status"),
            singleLine = true,
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (buddyName.isNotBlank()) {
              onAddBuddy(buddyName, buddyStatus, "🤝")
              showAddBuddyDialog = false
            }
          },
          modifier = Modifier.testTag("btn_confirm_add_buddy"),
        ) {
          Text("Add Buddy")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddBuddyDialog = false }) {
          Text("Cancel")
        }
      },
    )
  }
}
