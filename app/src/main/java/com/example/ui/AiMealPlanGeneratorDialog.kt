package com.example.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.GeneratedMeal
import com.example.model.GeneratedMealPlan
import com.example.model.MealType
import com.example.ui.theme.CalorieCoral
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.NeonPulse
import com.example.ui.theme.PulseCyan
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AiMealPlanGeneratorDialog(
  onDismiss: () -> Unit,
  onApplyPlan: (List<GeneratedMeal>) -> Unit,
) {
  val goals = remember {
    listOf(
      "Muscle Hypertrophy" to "High protein, surplus calories for lean mass",
      "Shred & Fat Loss" to "High protein, clean deficit, satiety focused",
      "Keto Athletic" to "Low carb, healthy fats, sustained ketones",
      "Plant-Based Power" to "100% plant sources with complete amino profile",
    )
  }

  val caloriePresets = listOf(1800, 2200, 2600, 3000)

  var selectedGoalIndex by remember { mutableIntStateOf(0) }
  var selectedCalories by remember { mutableIntStateOf(2400) }
  var isGenerating by remember { mutableStateOf(false) }
  var generatedPlan by remember { mutableStateOf<GeneratedMealPlan?>(null) }

  val coroutineScope = rememberCoroutineScope()

  fun generatePlan() {
    isGenerating = true
    coroutineScope.launch {
      // Realistic simulation of AI inference with tailored recipe synthesis
      delay(1200L)
      val goal = goals[selectedGoalIndex].first

      val meals = when (selectedGoalIndex) {
        0 -> listOf(
          GeneratedMeal(MealType.BREAKFAST, "Protein Power Oatmeal & Blueberries", 520, 42, 65, 10, 10, listOf("80g Rolled Oats", "1.5 scoops Whey Isolate", "100g Blueberries", "1 tbsp Chia seeds"), "Cook oats with water, let cool 2 mins, stir in whey isolate and top with blueberries."),
          GeneratedMeal(MealType.LUNCH, "Grilled Chicken, Brown Rice & Avocado", 740, 58, 72, 22, 25, listOf("200g Chicken Breast", "150g Cooked Brown Rice", "1/2 Avocado", "Steamed Broccoli"), "Season chicken with paprika & garlic. Grill 6 mins per side. Serve over warm rice."),
          GeneratedMeal(MealType.DINNER, "Wild Salmon Fillet with Quinoa & Asparagus", 680, 52, 48, 26, 20, listOf("220g Atlantic Salmon", "120g Quinoa", "8 spears Asparagus", "1 tbsp Olive Oil"), "Pan-sear salmon in olive oil skin down for 5 mins, flip and finish with lemon and steamed asparagus."),
          GeneratedMeal(MealType.SNACK, "Greek Yogurt, Raw Almonds & Honey", 340, 26, 22, 14, 5, listOf("200g Non-fat Greek Yogurt", "25g Raw Almonds", "1 tsp Raw Honey"), "Combine Greek yogurt in a bowl, top with crushed almonds and a drizzle of honey.")
        )
        1 -> listOf(
          GeneratedMeal(MealType.BREAKFAST, "Egg White & Spinach Scramble with Toast", 360, 35, 26, 8, 12, listOf("5 Egg Whites + 1 Whole Egg", "100g Fresh Spinach", "1 slice Whole Grain Toast"), "Sauté spinach in non-stick spray, pour whisked egg whites and scramble until firm."),
          GeneratedMeal(MealType.LUNCH, "Zesty Lemon Turkey & Cauliflower Rice Bowl", 480, 48, 24, 14, 20, listOf("200g Lean Ground Turkey 93/7", "200g Cauliflower Rice", "Diced Bell Peppers", "Zesty Lime Herb Seasoning"), "Brown turkey in a skillet with garlic and lime, add cauliflower rice and peppers."),
          GeneratedMeal(MealType.DINNER, "Seared Cod with Roasted Mediterranean Veggies", 440, 42, 28, 12, 25, listOf("250g Pacific Cod Fillet", "Zucchini & Cherry Tomatoes", "Fresh Rosemary & Garlic"), "Bake cod with herbs at 400°F (200°C) for 15 minutes with olive oil sprayed vegetables."),
          GeneratedMeal(MealType.SNACK, "Cottage Cheese with Strawberries & Cinnamon", 220, 24, 18, 4, 3, listOf("180g Low-fat Cottage Cheese", "100g Sliced Strawberries", "Dash of Ceylon Cinnamon"), "Layer cottage cheese in a glass, top with sliced strawberries and dust with cinnamon.")
        )
        2 -> listOf(
          GeneratedMeal(MealType.BREAKFAST, "Avocado, Bacon & Pasture-Raised Eggs", 560, 28, 6, 48, 12, listOf("3 Large Eggs", "2 strips Uncured Bacon", "1/2 Hass Avocado"), "Fry eggs in bacon drippings sunny side up, serve alongside sliced avocado with sea salt."),
          GeneratedMeal(MealType.LUNCH, "Ribeye Steak with Garlic Butter Green Beans", 780, 55, 8, 62, 20, listOf("250g Grass-fed Ribeye", "150g Green Beans", "2 tbsp Grass-fed Butter"), "Sear ribeye 4 minutes per side in a smoking hot cast iron pan, baste with butter and garlic."),
          GeneratedMeal(MealType.DINNER, "Keto Creamy Salmon Chowder", 650, 46, 7, 50, 25, listOf("200g Salmon chunks", "1/2 cup Heavy Cream", "Diced Celery & Dill"), "Simmer salmon in vegetable broth and cream with fresh dill until rich and velvety."),
          GeneratedMeal(MealType.SNACK, "Macadamia Nuts & Dark Cocoa Keto Clusters", 310, 6, 4, 32, 5, listOf("40g Roasted Macadamia Nuts", "10g 90% Dark Chocolate"), "Pair roasted salted macadamia nuts with dark chocolate nibs.")
        )
        else -> listOf(
          GeneratedMeal(MealType.BREAKFAST, "Tofu Scramble with Turmeric & Whole Grain Sourdough", 410, 28, 45, 12, 15, listOf("200g Firm Tofu Crumbled", "Turmeric, Nutritional Yeast", "Baby Kale", "1 slice Sourdough"), "Scramble crumbled tofu with turmeric and nutritional yeast until golden, serve over toasted sourdough."),
          GeneratedMeal(MealType.LUNCH, "Tempeh & Edamame Quinoa Power Bowl", 620, 42, 68, 18, 20, listOf("150g Marinated Tempeh", "100g Edamame Beans", "120g Quinoa", "Tahini Lime Dressing"), "Pan-fry tempeh triangles, assemble over fluffy quinoa, steamed edamame and drizzle with tahini."),
          GeneratedMeal(MealType.DINNER, "Lentil & Chickpea Protein Curry with Spinach", 580, 36, 76, 14, 30, listOf("1 cup Red Lentils", "1/2 can Chickpeas", "Coconut Milk", "Garam Masala", "Baby Spinach"), "Slow simmer lentils and chickpeas with aromatics and coconut milk, fold in spinach at the end."),
          GeneratedMeal(MealType.SNACK, "Peanut Butter & Plant Protein Hemp Shake", 320, 26, 18, 16, 5, listOf("1 scoop Pea/Rice Protein", "1 tbsp Natural Peanut Butter", "1 tbsp Hemp Hearts", "Almond Milk"), "Blend all ingredients with crushed ice for a creamy plant protein shake.")
        )
      }

      generatedPlan = GeneratedMealPlan(
        planTitle = "$goal Custom Plan",
        dietType = goal,
        totalCalories = meals.sumOf { it.calories },
        totalProtein = meals.sumOf { it.protein },
        totalCarbs = meals.sumOf { it.carbs },
        totalFat = meals.sumOf { it.fat },
        meals = meals
      )
      isGenerating = false
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(0.95f).height(620.dp).clip(RoundedCornerShape(24.dp)),
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
              modifier = Modifier.size(36.dp).clip(CircleShape).background(NeonPulse.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center,
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonPulse, modifier = Modifier.size(20.dp))
            }
            Text(
              text = "AI Meal Plan Generator",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
          }

          IconButton(onClick = onDismiss, modifier = Modifier.testTag("btn_close_ai_meal_generator")) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (generatedPlan == null) {
          // Setup preferences
          Text(
            text = "Select your primary fitness & diet goal:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          Spacer(modifier = Modifier.height(8.dp))

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            goals.forEachIndexed { index, (title, subtitle) ->
              val isSelected = selectedGoalIndex == index
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                  containerColor = if (isSelected) NeonPulse.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                ),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, NeonPulse) else null,
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { selectedGoalIndex = index }
                  .testTag("goal_option_$index"),
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = title,
                      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                      color = if (isSelected) NeonPulse else MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                      text = subtitle,
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                  }
                  if (isSelected) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = NeonPulse)
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Daily Calorie Target: $selectedCalories kcal",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
          ) {
            caloriePresets.forEach { cal ->
              val isSelected = selectedCalories == cal
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) PulseCyan else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                  .weight(1f)
                  .clickable { selectedCalories = cal }
                  .testTag("cal_preset_$cal"),
              ) {
                Text(
                  text = "$cal",
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                  color = if (isSelected) Color(0xFF003444) else MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.padding(vertical = 10.dp),
                  textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
              }
            }
          }

          Spacer(modifier = Modifier.weight(1f))

          Button(
            onClick = { generatePlan() },
            enabled = !isGenerating,
            colors = ButtonDefaults.buttonColors(containerColor = NeonPulse),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("btn_trigger_ai_generation"),
          ) {
            if (isGenerating) {
              CircularProgressIndicator(color = Color(0xFF003818), modifier = Modifier.size(24.dp))
              Spacer(modifier = Modifier.width(10.dp))
              Text("Synthesizing tailored meal plan...", color = Color(0xFF003818))
            } else {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF003818))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Generate AI Meal Plan", color = Color(0xFF003818), fontWeight = FontWeight.Bold)
            }
          }
        } else {
          // Display Generated Results
          val plan = generatedPlan!!
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column {
              Text(
                text = plan.planTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = NeonPulse,
              )
              Text(
                text = "${plan.totalCalories} kcal • ${plan.totalProtein}g Protein • ${plan.totalCarbs}g Carbs",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }

            OutlinedButton(
              onClick = { generatedPlan = null },
              shape = RoundedCornerShape(8.dp),
            ) {
              Text("Change", style = MaterialTheme.typography.labelSmall)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
          ) {
            items(plan.meals) { meal ->
              Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth(),
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                  ) {
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = PulseCyan.copy(alpha = 0.2f),
                    ) {
                      Text(
                        text = meal.type.name,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = PulseCyan,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                      )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                      Text(
                        text = "${meal.calories} kcal",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = CalorieCoral,
                      )
                      Text(
                        text = "${meal.protein}g P",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = NeonPulse,
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = meal.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "Ingredients: ${meal.ingredients.joinToString(", ")}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "Recipe: ${meal.instructions}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = {
              onApplyPlan(plan.meals)
              onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = NeonPulse),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_apply_ai_plan"),
          ) {
            Text("Apply & Log Meals to Diary", color = Color(0xFF003818), fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
