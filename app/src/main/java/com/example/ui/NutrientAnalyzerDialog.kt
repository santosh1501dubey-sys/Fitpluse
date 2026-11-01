package com.example.ui

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.FoodNutrientInfo
import com.example.model.MealType
import com.example.ui.theme.CalorieCoral
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.NeonPulse
import com.example.ui.theme.PulseCyan

@Composable
fun NutrientAnalyzerDialog(
  onDismiss: () -> Unit,
  onLogMeal: (name: String, mealType: MealType, cal: Int, protein: Int, carbs: Int, fat: Int) -> Unit,
) {
  val foodDatabase = remember {
    listOf(
      FoodNutrientInfo("Chicken Breast (Skinless)", "Cooked / Grilled", 100, 165, 31.0, 0.0, 3.6, 0.0, 256, 1.0, "A+ Lean Muscle"),
      FoodNutrientInfo("Whey Protein Isolate", "1 Scoop (30g)", 30, 120, 27.0, 1.5, 0.5, 0.0, 140, 0.6, "A+ Pure Protein"),
      FoodNutrientInfo("Atlantic Salmon", "Fillet Grilled", 100, 208, 20.4, 0.0, 13.4, 0.0, 363, 0.8, "A Omega-3 Rich"),
      FoodNutrientInfo("Greek Yogurt (Non-fat)", "Plain Container", 100, 59, 10.0, 3.6, 0.4, 0.0, 141, 0.1, "A Probiotic & Protein"),
      FoodNutrientInfo("Whole Eggs (Boiled)", "2 Large Eggs", 100, 155, 12.6, 1.1, 10.6, 0.0, 126, 1.2, "A Complete Amino Acids"),
      FoodNutrientInfo("Rolled Oats", "Dry Uncooked", 100, 389, 16.9, 66.3, 6.9, 10.6, 429, 4.7, "A Complex Carbs"),
      FoodNutrientInfo("Brown Rice (Steamed)", "1 Cooked Cup", 100, 123, 2.7, 25.6, 1.0, 1.6, 86, 0.5, "B+ Fiber Energy"),
      FoodNutrientInfo("Quinoa (Cooked)", "Organic Grain", 100, 120, 4.4, 21.3, 1.9, 2.8, 172, 1.5, "A Super Grain"),
      FoodNutrientInfo("Sweet Potato (Baked)", "Medium Potato", 100, 90, 2.0, 20.7, 0.2, 3.3, 475, 0.7, "A High Potassium"),
      FoodNutrientInfo("Firm Tofu", "Plant Protein Block", 100, 83, 10.0, 2.3, 5.3, 1.0, 121, 1.6, "A Plant Protein"),
      FoodNutrientInfo("Almonds (Raw)", "Handful", 30, 173, 6.4, 6.1, 15.0, 3.8, 218, 1.1, "A Heart Healthy Fats"),
      FoodNutrientInfo("Broccoli (Steamed)", "Fresh Florets", 100, 35, 2.4, 7.2, 0.4, 3.3, 316, 0.7, "A+ Micronutrient Dense")
    )
  }

  var selectedFood by remember { mutableStateOf(foodDatabase[0]) }
  var servingGramsText by remember { mutableStateOf("${selectedFood.grams}") }
  var searchQuery by remember { mutableStateOf("") }
  var selectedMealType by remember { mutableStateOf(MealType.LUNCH) }

  val grams = servingGramsText.toIntOrNull() ?: selectedFood.grams
  val multiplier = (grams.toDouble() / selectedFood.grams.toDouble()).coerceAtLeast(0.1)

  val calculatedCalories = (selectedFood.calories * multiplier).toInt()
  val calculatedProtein = (selectedFood.protein * multiplier).toInt()
  val calculatedCarbs = (selectedFood.carbs * multiplier).toInt()
  val calculatedFat = (selectedFood.fat * multiplier).toInt()
  val calculatedFiber = String.format("%.1f", selectedFood.fiber * multiplier)
  val calculatedPotassium = (selectedFood.potassiumMg * multiplier).toInt()
  val calculatedIron = String.format("%.1f", selectedFood.ironMg * multiplier)

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(0.95f).clip(RoundedCornerShape(24.dp)),
      color = MaterialTheme.colorScheme.surface,
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
      ) {
        // Top Header
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
              Icon(Icons.Default.Scale, contentDescription = null, tint = PulseCyan, modifier = Modifier.size(20.dp))
            }
            Text(
              text = "Nutrient & Protein Meter",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
          }

          IconButton(onClick = onDismiss, modifier = Modifier.testTag("btn_close_nutrient_analyzer")) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Food Selector Chips
        Text(
          text = "Select or search food item:",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth(),
        ) {
          items(foodDatabase) { food ->
            val isSelected = food.name == selectedFood.name
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) NeonPulse.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
              border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, NeonPulse) else null,
              modifier = Modifier
                .clickable {
                  selectedFood = food
                  servingGramsText = "${food.grams}"
                }
                .testTag("chip_food_${food.name}"),
            ) {
              Text(
                text = food.name,
                style = MaterialTheme.typography.bodySmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                ),
                color = if (isSelected) NeonPulse else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weight / Grams Input
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
          OutlinedTextField(
            value = servingGramsText,
            onValueChange = { servingGramsText = it },
            label = { Text("Portion Weight (grams)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f).testTag("input_nutrient_grams"),
            singleLine = true,
          )

          Surface(
            shape = RoundedCornerShape(12.dp),
            color = NeonPulse.copy(alpha = 0.15f),
            modifier = Modifier.padding(top = 6.dp),
          ) {
            Text(
              text = selectedFood.grade,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = NeonPulse,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Measured Nutrient Dashboard Card
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier.fillMaxWidth().testTag("nutrient_breakdown_card"),
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Column {
                Text(
                  text = selectedFood.name,
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                  text = "$grams grams portion",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }

              Text(
                text = "$calculatedCalories kcal",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = CalorieCoral,
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Protein Highlight Gauge
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              Text(
                text = "Protein Content",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = NeonPulse,
              )
              Text(
                text = "${calculatedProtein}g",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = NeonPulse,
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            val proteinPercent = (calculatedProtein.toFloat() / 50f).coerceIn(0f, 1f)
            LinearProgressIndicator(
              progress = { proteinPercent },
              modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
              color = NeonPulse,
              trackColor = MaterialTheme.colorScheme.surface,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Other Nutrients: Carbs, Fats, Fiber, Potassium
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
            ) {
              NutrientStatPill("Carbs", "${calculatedCarbs}g", PulseCyan)
              NutrientStatPill("Fat", "${calculatedFat}g", EnergyAmber)
              NutrientStatPill("Fiber", "${calculatedFiber}g", Color(0xFF4ADE80))
              NutrientStatPill("Potassium", "${calculatedPotassium}mg", Color(0xFFA78BFA))
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Meal Type Selector
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(text = "Log into:", style = MaterialTheme.typography.labelSmall)
          MealType.values().forEach { type ->
            val isSelected = selectedMealType == type
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) PulseCyan else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.clickable { selectedMealType = type },
            ) {
              Text(
                text = type.name.take(4),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) Color(0xFF003444) else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action Button
        Button(
          onClick = {
            onLogMeal(
              "${selectedFood.name} (${grams}g)",
              selectedMealType,
              calculatedCalories,
              calculatedProtein,
              calculatedCarbs,
              calculatedFat,
            )
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = NeonPulse),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_log_measured_food"),
        ) {
          Text(
            text = "Add to Today's Diary (+${calculatedProtein}g Protein)",
            color = Color(0xFF003818),
            fontWeight = FontWeight.Bold,
          )
        }
      }
    }
  }
}

@Composable
fun NutrientStatPill(label: String, value: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(text = value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = color)
  }
}
