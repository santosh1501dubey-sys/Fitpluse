package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonPulse
import com.example.ui.theme.PulseCyan

@Composable
fun AuthScreen(
  onSignInWithEmail: (email: String, password: String) -> Unit,
  onSignUpWithEmail: (
    name: String,
    email: String,
    password: String,
    goal: String,
    calories: Int,
    emoji: String,
  ) -> Unit,
  onSignInWithGoogle: (email: String, displayName: String) -> Unit,
  onContinueAsGuest: () -> Unit,
  modifier: Modifier = Modifier,
) {
  var authMode by remember { mutableIntStateOf(0) } // 0 = Sign In, 1 = Create Account
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var displayName by remember { mutableStateOf("") }
  var selectedGoal by remember { mutableStateOf("Muscle Growth & Strength") }
  var calorieTargetText by remember { mutableStateOf("2400") }
  var selectedEmoji by remember { mutableStateOf("⚡") }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var showGoogleSignInModal by remember { mutableStateOf(false) }

  val goals =
    listOf(
      "Muscle Growth & Strength",
      "Lean Fat Loss & Abs",
      "Athletic Conditioning",
      "Home Bodyweight Mastery",
    )
  val emojis = listOf("⚡", "🏋️", "🔥", "🥊", "🧘", "🏃", "🏆")

  Box(
    modifier =
      modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .testTag("auth_screen_root"),
  ) {
    Column(
      modifier =
        Modifier.fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 20.dp, vertical = 28.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      // Brand Header
      Box(
        modifier =
          Modifier.size(64.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(listOf(NeonPulse, PulseCyan))),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Default.FitnessCenter,
          contentDescription = null,
          tint = Color(0xFF003818),
          modifier = Modifier.size(34.dp),
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = "Welcome to FitPulse",
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
        color = MaterialTheme.colorScheme.onBackground,
      )
      Text(
        text = "Public Fitness & Nutrition Companion • Private User Accounts",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Privacy Shield Guarantee Banner
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = PulseCyan.copy(alpha = 0.12f),
        modifier =
          Modifier.fillMaxWidth()
            .border(1.dp, PulseCyan.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .testTag("banner_privacy_guarantee"),
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
          Icon(
            imageVector = Icons.Default.VerifiedUser,
            contentDescription = null,
            tint = PulseCyan,
            modifier = Modifier.size(22.dp),
          )
          Column {
            Text(
              text = "100% Email & Account Privacy",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = PulseCyan,
            )
            Text(
              text = "Your email and personal details are never published or shown to other users. Only your chosen display name appears in the app.",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Auth Mode Switcher (Sign In vs Create Account)
      Row(
        modifier =
          Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
      ) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = if (authMode == 0) NeonPulse else Color.Transparent,
          modifier =
            Modifier.weight(1f)
              .clickable {
                authMode = 0
                errorMessage = null
              }
              .testTag("tab_auth_sign_in"),
        ) {
          Text(
            text = "Sign In with Email",
            style =
              MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (authMode == 0) FontWeight.Bold else FontWeight.Medium,
              ),
            color = if (authMode == 0) Color(0xFF003818) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 10.dp),
            textAlign = TextAlign.Center,
          )
        }

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = if (authMode == 1) NeonPulse else Color.Transparent,
          modifier =
            Modifier.weight(1f)
              .clickable {
                authMode = 1
                errorMessage = null
              }
              .testTag("tab_auth_sign_up"),
        ) {
          Text(
            text = "Create Account",
            style =
              MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (authMode == 1) FontWeight.Bold else FontWeight.Medium,
              ),
            color = if (authMode == 1) Color(0xFF003818) else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 10.dp),
            textAlign = TextAlign.Center,
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Form Card
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          if (authMode == 1) {
            OutlinedTextField(
              value = displayName,
              onValueChange = {
                displayName = it
                errorMessage = null
              },
              label = { Text("Public Display Name / Username") },
              placeholder = { Text("e.g., Jordan_Fit") },
              leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
              modifier = Modifier.fillMaxWidth().testTag("input_auth_display_name"),
              singleLine = true,
            )
          }

          OutlinedTextField(
            value = email,
            onValueChange = {
              email = it
              errorMessage = null
            },
            label = { Text("Your Email Address (Kept Private)") },
            placeholder = { Text("athlete@example.com") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().testTag("input_auth_email"),
            singleLine = true,
          )

          OutlinedTextField(
            value = password,
            onValueChange = {
              password = it
              errorMessage = null
            },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().testTag("input_auth_password"),
            singleLine = true,
          )

          if (authMode == 1) {
            Text(
              text = "Select Fitness Goal:",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(goals) { goal ->
                val selected = selectedGoal == goal
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = if (selected) PulseCyan else MaterialTheme.colorScheme.surfaceVariant,
                  modifier = Modifier.clickable { selectedGoal = goal },
                ) {
                  Text(
                    text = goal,
                    style =
                      MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                      ),
                    color = if (selected) Color(0xFF003444) else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                  )
                }
              }
            }

            OutlinedTextField(
              value = calorieTargetText,
              onValueChange = { calorieTargetText = it },
              label = { Text("Daily Calorie Goal (kcal)") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier.fillMaxWidth().testTag("input_auth_calories"),
              singleLine = true,
            )

            Text(
              text = "Choose Avatar Badge:",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(emojis) { emoji ->
                val isSel = selectedEmoji == emoji
                Surface(
                  shape = CircleShape,
                  color = if (isSel) NeonPulse.copy(alpha = 0.28f) else MaterialTheme.colorScheme.surfaceVariant,
                  border = if (isSel) androidx.compose.foundation.BorderStroke(1.5.dp, NeonPulse) else null,
                  modifier = Modifier.clickable { selectedEmoji = emoji },
                ) {
                  Text(text = emoji, fontSize = 20.sp, modifier = Modifier.padding(8.dp))
                }
              }
            }
          }

          errorMessage?.let { msg ->
            Text(
              text = msg,
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.error,
              modifier = Modifier.testTag("text_auth_error"),
            )
          }

          Button(
            onClick = {
              if (email.isBlank() || !email.contains("@")) {
                errorMessage = "Please enter a valid email address."
                return@Button
              }
              if (password.length < 4) {
                errorMessage = "Please enter a password (at least 4 characters)."
                return@Button
              }
              if (authMode == 0) {
                onSignInWithEmail(email.trim(), password)
              } else {
                val chosenName =
                  displayName.trim().ifEmpty {
                    email.substringBefore("@").replaceFirstChar { it.uppercase() }
                  }
                val cal = calorieTargetText.toIntOrNull() ?: 2400
                onSignUpWithEmail(chosenName, email.trim(), password, selectedGoal, cal, selectedEmoji)
              }
            },
            colors =
              ButtonDefaults.buttonColors(
                containerColor = NeonPulse,
                contentColor = Color(0xFF003818),
              ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_submit_email_auth"),
          ) {
            Text(
              text = if (authMode == 0) "Sign In with Email" else "Create Private Account",
              fontWeight = FontWeight.ExtraBold,
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
              text = "  OR  ",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
          }

          // Google Sign-In Button
          OutlinedButton(
            onClick = { showGoogleSignInModal = true },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_google_sign_in"),
          ) {
            Icon(
              imageVector = Icons.Default.AccountCircle,
              contentDescription = null,
              tint = PulseCyan,
              modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Continue with Google Account",
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface,
            )
          }

          // Quick Guest / Athlete Access
          TextButton(
            onClick = onContinueAsGuest,
            modifier = Modifier.fillMaxWidth().testTag("btn_quick_guest_login"),
          ) {
            Text(
              text = "Continue as Guest Athlete (No Email Required)",
              color = PulseCyan,
              fontWeight = FontWeight.SemiBold,
            )
          }
        }
      }
    }
  }

  if (showGoogleSignInModal) {
    var googleEmail by remember { mutableStateOf("") }
    var googleDisplayName by remember { mutableStateOf("") }
    var googleError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
      onDismissRequest = { showGoogleSignInModal = false },
      title = { Text("Sign In with Google") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "Enter your Google email and public display name. Your email address will be masked and kept strictly private.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
          OutlinedTextField(
            value = googleEmail,
            onValueChange = {
              googleEmail = it
              googleError = null
            },
            label = { Text("Google Email") },
            placeholder = { Text("yourname@gmail.com") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().testTag("input_google_email"),
            singleLine = true,
          )
          OutlinedTextField(
            value = googleDisplayName,
            onValueChange = { googleDisplayName = it },
            label = { Text("Public Display Name (Optional)") },
            placeholder = { Text("e.g., Alex Runner") },
            modifier = Modifier.fillMaxWidth().testTag("input_google_display_name"),
            singleLine = true,
          )
          googleError?.let {
            Text(
              text = it,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.error,
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            if (googleEmail.isBlank() || !googleEmail.contains("@")) {
              googleError = "Please enter a valid Google email address."
              return@Button
            }
            val cleanName =
              googleDisplayName.trim().ifEmpty {
                googleEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
              }
            onSignInWithGoogle(googleEmail.trim(), cleanName)
            showGoogleSignInModal = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = NeonPulse, contentColor = Color(0xFF003818)),
          modifier = Modifier.testTag("btn_confirm_google_sign_in"),
        ) {
          Text("Sign In", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showGoogleSignInModal = false }) {
          Text("Cancel")
        }
      },
    )
  }
}
