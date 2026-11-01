package com.example.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExerciseType
import com.example.ui.theme.CalorieCoral
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.NeonPulse
import com.example.ui.theme.PulseCyan
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WorkoutAnimator(
  exerciseType: ExerciseType,
  isPlaying: Boolean,
  targetMuscleName: String = "",
  modifier: Modifier = Modifier,
) {
  val transition = rememberInfiniteTransition(label = "workout_pro_anim")

  // Repetition cycle: 0.0f (Starting extension) to 1.0f (Full contraction/peak)
  val cycle by
    transition.animateFloat(
      initialValue = 0f,
      targetValue = if (isPlaying) 1f else 0.5f,
      animationSpec =
        infiniteRepeatable(
          animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
          repeatMode = RepeatMode.Reverse,
        ),
      label = "rep_cycle",
    )

  // Energy ripple aura
  val auraPulse by
    transition.animateFloat(
      initialValue = 0.85f,
      targetValue = if (isPlaying) 1.25f else 1.0f,
      animationSpec =
        infiniteRepeatable(
          animation = tween(durationMillis = 900, easing = LinearEasing),
          repeatMode = RepeatMode.Reverse,
        ),
      label = "aura_pulse",
    )

  val phaseText = if (cycle > 0.5f) "Squeeze & Contract" else "Controlled Lowering"
  val targetLabel =
    when (exerciseType) {
      ExerciseType.BICEP_CURLS, ExerciseType.HAMMER_CURLS -> "TARGET: BICEPS"
      ExerciseType.TRICEP_DIPS, ExerciseType.OVERHEAD_TRICEP_EXTENSION -> "TARGET: TRICEPS"
      ExerciseType.PLANK, ExerciseType.CRUNCHES, ExerciseType.BICYCLE_CRUNCHES -> "TARGET: ABS & CORE"
      ExerciseType.BENCH_PRESS, ExerciseType.PUSH_UPS -> "TARGET: CHEST (PECS)"
      ExerciseType.PULL_UPS, ExerciseType.BENT_OVER_ROWS -> "TARGET: LATS & BACK"
      ExerciseType.SHOULDER_PRESS, ExerciseType.LATERAL_RAISES -> "TARGET: DELTOIDS (SHOULDERS)"
      ExerciseType.SQUATS, ExerciseType.BARBELL_SQUATS, ExerciseType.LUNGES -> "TARGET: QUADS & GLUTES"
      else -> "TARGET: ${targetMuscleName.ifEmpty { "FULL BODY" }}"
    }

  Box(
    modifier = modifier.clip(RoundedCornerShape(24.dp)).background(Color(0xFF090E1A)),
    contentAlignment = Alignment.Center,
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height
      val cx = w / 2f
      val cy = h / 2f
      val scale = w / 260f

      // 1. Ambient Background Grid & Energy Radial Glow
      drawCircle(
        brush =
          Brush.radialGradient(
            colors = listOf(NeonPulse.copy(alpha = 0.18f), Color.Transparent),
            center = Offset(cx, cy),
            radius = (w / 2.2f) * auraPulse,
          ),
        radius = (w / 2.2f) * auraPulse,
        center = Offset(cx, cy),
      )

      // Gym Floor Mat / Line
      val floorY = cy + 72f * scale
      drawLine(
        color = Color(0xFF1E293B),
        start = Offset(cx - 90f * scale, floorY),
        end = Offset(cx + 90f * scale, floorY),
        strokeWidth = 6f * scale,
        cap = StrokeCap.Round,
      )
      drawLine(
        color = PulseCyan.copy(alpha = 0.5f),
        start = Offset(cx - 75f * scale, floorY),
        end = Offset(cx + 75f * scale, floorY),
        strokeWidth = 2f * scale,
        cap = StrokeCap.Round,
      )

      val bodyColor = Color(0xFFE2E8F0)
      val muscleHighlight = NeonPulse
      val muscleSecondary = CalorieCoral
      val equipmentColor = Color(0xFF94A3B8)
      val dumbbellWeightColor = Color(0xFF38BDF8)
      val headRadius = 13f * scale
      val limbStroke = 7.5f * scale

      when (exerciseType) {
        // ==========================================
        // 1. BICEPS: BICEP CURLS & HAMMER CURLS
        // ==========================================
        ExerciseType.BICEP_CURLS, ExerciseType.HAMMER_CURLS -> {
          val headCenter = Offset(cx, cy - 54f * scale)
          val neck = Offset(cx, cy - 40f * scale)
          val hips = Offset(cx, cy + 6f * scale)
          val leftFoot = Offset(cx - 16f * scale, floorY)
          val rightFoot = Offset(cx + 16f * scale, floorY)

          // Legs & Lower Body
          drawLine(color = bodyColor, start = hips, end = leftFoot, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = rightFoot, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Torso & Head
          drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
          drawLine(color = bodyColor, start = neck, end = hips, strokeWidth = limbStroke + 2f, cap = StrokeCap.Round)

          // Shoulders
          val leftShoulder = Offset(cx - 18f * scale, cy - 36f * scale)
          val rightShoulder = Offset(cx + 18f * scale, cy - 36f * scale)
          drawLine(color = bodyColor, start = leftShoulder, end = rightShoulder, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Elbows pinned at sides
          val leftElbow = Offset(cx - 20f * scale, cy - 8f * scale)
          val rightElbow = Offset(cx + 20f * scale, cy - 8f * scale)
          drawLine(color = bodyColor, start = leftShoulder, end = leftElbow, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = rightShoulder, end = rightElbow, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Forearm curls from down (cycle=0) to fully flexed upward (cycle=1)
          val curlAngle = -0.4f - (cycle * 2.3f)
          val forearmLen = 28f * scale
          val leftHand = Offset(leftElbow.x + sin(curlAngle) * forearmLen, leftElbow.y - cos(curlAngle) * forearmLen)
          val rightHand = Offset(rightElbow.x - sin(curlAngle) * forearmLen, rightElbow.y - cos(curlAngle) * forearmLen)

          drawLine(color = bodyColor, start = leftElbow, end = leftHand, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = rightElbow, end = rightHand, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Dumbbells in hands
          drawCircle(color = dumbbellWeightColor, radius = 7f * scale, center = leftHand)
          drawCircle(color = dumbbellWeightColor, radius = 7f * scale, center = rightHand)
          drawLine(color = equipmentColor, start = Offset(leftHand.x - 7f * scale, leftHand.y), end = Offset(leftHand.x + 7f * scale, leftHand.y), strokeWidth = 3f * scale)
          drawLine(color = equipmentColor, start = Offset(rightHand.x - 7f * scale, rightHand.y), end = Offset(rightHand.x + 7f * scale, rightHand.y), strokeWidth = 3f * scale)

          // MUSCLE HIGHLIGHT: Glowing Biceps Bulge on Upper Arms
          val bicepPeak = 4f * scale * cycle
          drawCircle(color = muscleHighlight, radius = 6f * scale + bicepPeak, center = Offset(leftElbow.x + 3f * scale, (leftShoulder.y + leftElbow.y) / 2f))
          drawCircle(color = muscleHighlight, radius = 6f * scale + bicepPeak, center = Offset(rightElbow.x - 3f * scale, (rightShoulder.y + rightElbow.y) / 2f))
        }

        // ==========================================
        // 2. TRICEPS: OVERHEAD EXTENSION & DIPS
        // ==========================================
        ExerciseType.OVERHEAD_TRICEP_EXTENSION, ExerciseType.TRICEP_DIPS -> {
          if (exerciseType == ExerciseType.TRICEP_DIPS) {
            // Gym Bench Dips
            val benchTopY = cy + 24f * scale
            // Bench block
            drawRoundRect(
              color = Color(0xFF1E293B),
              topLeft = Offset(cx - 55f * scale, benchTopY),
              size = Size(38f * scale, 48f * scale),
              cornerRadius = CornerRadius(6f * scale, 6f * scale),
            )

            // Body sinking (cycle=1) to locked out arms (cycle=0)
            val dipY = cycle * 24f * scale
            val headCenter = Offset(cx + 8f * scale, cy - 36f * scale + dipY)
            val hips = Offset(cx + 8f * scale, cy + 18f * scale + dipY)
            val feet = Offset(cx + 60f * scale, floorY)

            drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
            drawLine(color = bodyColor, start = Offset(headCenter.x, headCenter.y + 12f * scale), end = hips, strokeWidth = limbStroke + 2f, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = hips, end = feet, strokeWidth = limbStroke, cap = StrokeCap.Round)

            // Arms holding bench: hands planted at bench top
            val hand = Offset(cx - 18f * scale, benchTopY)
            val elbow = Offset(cx - 28f * scale, benchTopY - 14f * scale + (dipY * 0.8f))
            val shoulder = Offset(headCenter.x - 8f * scale, headCenter.y + 12f * scale)
            drawLine(color = bodyColor, start = shoulder, end = elbow, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = elbow, end = hand, strokeWidth = limbStroke, cap = StrokeCap.Round)

            // MUSCLE HIGHLIGHT: Triceps on back of arm
            drawCircle(color = muscleHighlight, radius = 7f * scale + (cycle * 3f * scale), center = Offset(elbow.x + 2f * scale, (shoulder.y + elbow.y) / 2f))
          } else {
            // Overhead Dumbbell Tricep Extension
            val headCenter = Offset(cx, cy - 42f * scale)
            val hips = Offset(cx, cy + 12f * scale)
            drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
            drawLine(color = bodyColor, start = Offset(cx, cy - 28f * scale), end = hips, strokeWidth = limbStroke + 2f, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = hips, end = Offset(cx - 16f * scale, floorY), strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = hips, end = Offset(cx + 16f * scale, floorY), strokeWidth = limbStroke, cap = StrokeCap.Round)

            // Upper arms vertical next to head
            val elbow = Offset(cx, cy - 62f * scale)
            drawLine(color = bodyColor, start = Offset(cx, cy - 28f * scale), end = elbow, strokeWidth = limbStroke, cap = StrokeCap.Round)

            // Forearms extending overhead (cycle=1) down behind neck (cycle=0)
            val extAngle = 3.14f * (0.3f + cycle * 0.7f)
            val dumbbellPos = Offset(elbow.x - cos(extAngle) * 28f * scale, elbow.y - sin(extAngle) * 28f * scale)
            drawLine(color = bodyColor, start = elbow, end = dumbbellPos, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawCircle(color = dumbbellWeightColor, radius = 9f * scale, center = dumbbellPos)

            // Triceps highlight
            drawCircle(color = muscleHighlight, radius = 7f * scale, center = Offset(elbow.x - 6f * scale, cy - 46f * scale))
          }
        }

        // ==========================================
        // 3. ABS & CORE: BICYCLE CRUNCHES & PLANK
        // ==========================================
        ExerciseType.BICYCLE_CRUNCHES, ExerciseType.CRUNCHES -> {
          // Lying on floor with mat
          val matY = cy + 34f * scale
          drawRoundRect(color = Color(0xFF1E293B), topLeft = Offset(cx - 80f * scale, matY), size = Size(160f * scale, 12f * scale), cornerRadius = CornerRadius(4f * scale, 4f * scale))

          val crunchCurl = cycle * 26f * scale
          val headCenter = Offset(cx - 40f * scale + (crunchCurl * 0.4f), matY - 26f * scale - crunchCurl)
          val hips = Offset(cx, matY - 8f * scale)
          val neck = Offset(cx - 24f * scale + (crunchCurl * 0.3f), matY - 18f * scale - (crunchCurl * 0.7f))

          drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
          drawLine(color = bodyColor, start = neck, end = hips, strokeWidth = limbStroke + 3f, cap = StrokeCap.Round)

          // Hands behind head, elbow driving forward
          val elbow = Offset(neck.x + 18f * scale + (cycle * 14f * scale), neck.y - 4f * scale)
          drawLine(color = bodyColor, start = neck, end = elbow, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Legs in bicycle motion: one knee tucked to chest, one extended
          val tuckedKnee = Offset(cx + 8f * scale - (cycle * 16f * scale), matY - 32f * scale)
          val extendedFoot = Offset(cx + 62f * scale, matY - 14f * scale)
          drawLine(color = bodyColor, start = hips, end = tuckedKnee, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = extendedFoot, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // MUSCLE HIGHLIGHT: Six-pack Abs Glowing Grid
          drawRoundRect(
            color = muscleHighlight.copy(alpha = 0.85f),
            topLeft = Offset(cx - 16f * scale, matY - 22f * scale - (crunchCurl * 0.4f)),
            size = Size(20f * scale, 16f * scale),
            cornerRadius = CornerRadius(4f * scale, 4f * scale),
          )
        }

        ExerciseType.PLANK -> {
          val breath = sin(cycle * 3.14f) * 4f * scale
          val headCenter = Offset(cx + 50f * scale, cy + 12f * scale + breath)
          val chest = Offset(cx + 32f * scale, cy + 18f * scale + breath)
          val hips = Offset(cx - 12f * scale, cy + 18f * scale + breath)
          val feet = Offset(cx - 62f * scale, floorY - 6f * scale)

          drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
          drawLine(color = bodyColor, start = chest, end = hips, strokeWidth = limbStroke + 3f, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = feet, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Forearm resting on floor
          val elbow = Offset(cx + 32f * scale, floorY)
          drawLine(color = bodyColor, start = chest, end = elbow, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = elbow, end = Offset(cx + 46f * scale, floorY), strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Core tension ring
          drawCircle(
            color = muscleHighlight,
            radius = (18f + (cycle * 6f)) * scale,
            center = Offset(cx + 8f * scale, cy + 20f * scale + breath),
            style = Stroke(width = 3.5f * scale),
          )
        }

        // ==========================================
        // 4. CHEST: BENCH PRESS & PUSH UPS
        // ==========================================
        ExerciseType.BENCH_PRESS -> {
          // Gym Flat Bench & Rack Posts
          val benchY = cy + 20f * scale
          drawRoundRect(color = Color(0xFF1E293B), topLeft = Offset(cx - 60f * scale, benchY), size = Size(120f * scale, 14f * scale), cornerRadius = CornerRadius(4f * scale, 4f * scale))
          // Bench legs
          drawLine(color = Color(0xFF334155), start = Offset(cx - 45f * scale, benchY + 14f * scale), end = Offset(cx - 45f * scale, floorY), strokeWidth = 5f * scale)
          drawLine(color = Color(0xFF334155), start = Offset(cx + 45f * scale, benchY + 14f * scale), end = Offset(cx + 45f * scale, floorY), strokeWidth = 5f * scale)

          // Body lying on bench
          val headCenter = Offset(cx - 44f * scale, benchY - 14f * scale)
          val chest = Offset(cx - 10f * scale, benchY - 12f * scale)
          val hips = Offset(cx + 25f * scale, benchY - 10f * scale)
          val feet = Offset(cx + 45f * scale, floorY)

          drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
          drawLine(color = bodyColor, start = headCenter, end = hips, strokeWidth = limbStroke + 4f, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = feet, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Barbell moving from chest (cycle=0) to arms locked (cycle=1)
          val barPressY = benchY - 14f * scale - (cycle * 36f * scale)
          val barbellStart = Offset(cx - 65f * scale, barPressY)
          val barbellEnd = Offset(cx + 45f * scale, barPressY)

          // Arms holding bar
          val hands = Offset(chest.x, barPressY)
          val elbow = Offset(chest.x + (16f * scale * (1f - cycle)), benchY + (10f * scale * (1f - cycle)))
          drawLine(color = bodyColor, start = chest, end = elbow, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = elbow, end = hands, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Barbell & 45lb Olympic Weight Plates
          drawLine(color = equipmentColor, start = barbellStart, end = barbellEnd, strokeWidth = 4f * scale)
          drawRoundRect(color = CalorieCoral, topLeft = Offset(barbellStart.x - 6f * scale, barPressY - 18f * scale), size = Size(10f * scale, 36f * scale), cornerRadius = CornerRadius(2f * scale, 2f * scale))
          drawRoundRect(color = CalorieCoral, topLeft = Offset(barbellEnd.x - 4f * scale, barPressY - 18f * scale), size = Size(10f * scale, 36f * scale), cornerRadius = CornerRadius(2f * scale, 2f * scale))

          // MUSCLE HIGHLIGHT: Pectoral Chest Contraction
          drawCircle(color = muscleHighlight, radius = 9f * scale + (cycle * 3f * scale), center = chest)
        }

        ExerciseType.PUSH_UPS -> {
          val pushDrop = 26f * scale * cycle
          val headCenter = Offset(cx + 48f * scale, cy + 10f * scale + pushDrop)
          val chest = Offset(cx + 28f * scale, cy + 18f * scale + pushDrop)
          val hips = Offset(cx - 16f * scale, cy + 20f * scale + (pushDrop * 0.8f))
          val feet = Offset(cx - 62f * scale, cy + 32f * scale)

          drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
          drawLine(color = bodyColor, start = chest, end = hips, strokeWidth = limbStroke + 2f, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = feet, strokeWidth = limbStroke, cap = StrokeCap.Round)

          val elbow = Offset(cx + 18f * scale + (12f * scale * cycle), cy + 30f * scale + pushDrop)
          drawLine(color = bodyColor, start = chest, end = elbow, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = elbow, end = Offset(cx + 28f * scale, floorY), strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Chest highlight
          drawCircle(color = muscleHighlight, radius = 9f * scale, center = chest)
        }

        // ==========================================
        // 5. BACK: PULL UPS & BENT OVER ROWS
        // ==========================================
        ExerciseType.PULL_UPS, ExerciseType.BENT_OVER_ROWS -> {
          if (exerciseType == ExerciseType.PULL_UPS) {
            // Overhead Pull-up Bar
            val barY = cy - 64f * scale
            drawLine(color = equipmentColor, start = Offset(cx - 60f * scale, barY), end = Offset(cx + 60f * scale, barY), strokeWidth = 5f * scale)

            // Body pulling up from hang (cycle=0) to chin over bar (cycle=1)
            val pullY = -cycle * 36f * scale
            val headCenter = Offset(cx, barY + 36f * scale + pullY)
            val chest = Offset(cx, headCenter.y + 14f * scale)
            val hips = Offset(cx, chest.y + 34f * scale)

            drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
            drawLine(color = bodyColor, start = chest, end = hips, strokeWidth = limbStroke + 4f, cap = StrokeCap.Round)

            // Hands on bar, elbows bending down as body rises
            val leftHand = Offset(cx - 30f * scale, barY)
            val rightHand = Offset(cx + 30f * scale, barY)
            val leftElbow = Offset(cx - 34f * scale, chest.y - (10f * scale * (1f - cycle)))
            val rightElbow = Offset(cx + 34f * scale, chest.y - (10f * scale * (1f - cycle)))

            drawLine(color = bodyColor, start = chest, end = leftElbow, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = leftElbow, end = leftHand, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = chest, end = rightElbow, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = rightElbow, end = rightHand, strokeWidth = limbStroke, cap = StrokeCap.Round)

            // Bent knees
            val knees = Offset(cx + 10f * scale, hips.y + 24f * scale)
            drawLine(color = bodyColor, start = hips, end = knees, strokeWidth = limbStroke, cap = StrokeCap.Round)

            // MUSCLE HIGHLIGHT: V-Taper Lats on Back
            drawRoundRect(color = muscleHighlight, topLeft = Offset(cx - 18f * scale, chest.y), size = Size(36f * scale, 20f * scale), cornerRadius = CornerRadius(6f * scale, 6f * scale))
          } else {
            // Bent-over Dumbbell Rows
            val headCenter = Offset(cx + 26f * scale, cy - 28f * scale)
            val hips = Offset(cx - 18f * scale, cy - 4f * scale)
            val feet = Offset(cx - 10f * scale, floorY)

            drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
            drawLine(color = bodyColor, start = Offset(cx + 16f * scale, cy - 20f * scale), end = hips, strokeWidth = limbStroke + 3f, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = hips, end = feet, strokeWidth = limbStroke, cap = StrokeCap.Round)

            // Arm rowing dumbbell up to hip
            val shoulder = Offset(cx + 12f * scale, cy - 16f * scale)
            val elbow = Offset(cx - 4f * scale, cy - 32f * scale * cycle - 8f * scale)
            val dumbbell = Offset(cx + 2f * scale, cy + 18f * scale * (1f - cycle) - 4f * scale)

            drawLine(color = bodyColor, start = shoulder, end = elbow, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = elbow, end = dumbbell, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawCircle(color = dumbbellWeightColor, radius = 8f * scale, center = dumbbell)

            // Lats Back highlight
            drawCircle(color = muscleHighlight, radius = 9f * scale, center = Offset(cx - 4f * scale, cy - 12f * scale))
          }
        }

        // ==========================================
        // 6. SHOULDERS: OVERHEAD PRESS & LATERAL RAISES
        // ==========================================
        ExerciseType.SHOULDER_PRESS, ExerciseType.LATERAL_RAISES -> {
          val headCenter = Offset(cx, cy - 46f * scale)
          val neck = Offset(cx, cy - 32f * scale)
          val hips = Offset(cx, cy + 10f * scale)
          drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
          drawLine(color = bodyColor, start = neck, end = hips, strokeWidth = limbStroke + 2f, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = Offset(cx - 16f * scale, floorY), strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = Offset(cx + 16f * scale, floorY), strokeWidth = limbStroke, cap = StrokeCap.Round)

          val leftShoulder = Offset(cx - 20f * scale, neck.y)
          val rightShoulder = Offset(cx + 20f * scale, neck.y)

          if (exerciseType == ExerciseType.SHOULDER_PRESS) {
            // Dumbbells pressing straight overhead
            val pressProgress = cycle * 42f * scale
            val leftHand = Offset(cx - 24f * scale, cy - 24f * scale - pressProgress)
            val rightHand = Offset(cx + 24f * scale, cy - 24f * scale - pressProgress)
            val leftElbow = Offset(cx - 32f * scale, cy - 10f * scale - (pressProgress * 0.5f))
            val rightElbow = Offset(cx + 32f * scale, cy - 10f * scale - (pressProgress * 0.5f))

            drawLine(color = bodyColor, start = leftShoulder, end = leftElbow, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = leftElbow, end = leftHand, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = rightShoulder, end = rightElbow, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = rightElbow, end = rightHand, strokeWidth = limbStroke, cap = StrokeCap.Round)

            drawCircle(color = dumbbellWeightColor, radius = 7f * scale, center = leftHand)
            drawCircle(color = dumbbellWeightColor, radius = 7f * scale, center = rightHand)
          } else {
            // Lateral raises: arms flying out sideways to parallel with shoulders
            val armRaise = cycle * 40f * scale
            val leftHand = Offset(cx - 52f * scale, cy + 18f * scale - armRaise)
            val rightHand = Offset(cx + 52f * scale, cy + 18f * scale - armRaise)

            drawLine(color = bodyColor, start = leftShoulder, end = leftHand, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawLine(color = bodyColor, start = rightShoulder, end = rightHand, strokeWidth = limbStroke, cap = StrokeCap.Round)
            drawCircle(color = dumbbellWeightColor, radius = 6f * scale, center = leftHand)
            drawCircle(color = dumbbellWeightColor, radius = 6f * scale, center = rightHand)
          }

          // Deltoid Shoulder Caps Highlight
          drawCircle(color = muscleHighlight, radius = 8f * scale, center = leftShoulder)
          drawCircle(color = muscleHighlight, radius = 8f * scale, center = rightShoulder)
        }

        // ==========================================
        // 7. LEGS: BARBELL SQUATS & LUNGES
        // ==========================================
        ExerciseType.SQUATS, ExerciseType.BARBELL_SQUATS -> {
          val squatDrop = 34f * scale * cycle
          val headCenter = Offset(cx, cy - 48f * scale + squatDrop)
          val neck = Offset(cx, cy - 34f * scale + squatDrop)
          val hips = Offset(cx - 14f * scale * cycle, cy + 8f * scale + squatDrop)

          drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
          drawLine(color = bodyColor, start = neck, end = hips, strokeWidth = limbStroke + 3f, cap = StrokeCap.Round)

          val leftKnee = Offset(cx + 22f * scale * cycle, cy + 36f * scale + (squatDrop * 0.4f))
          val rightKnee = Offset(cx + 14f * scale * cycle, cy + 36f * scale + (squatDrop * 0.4f))
          val leftFoot = Offset(cx - 10f * scale, floorY)
          val rightFoot = Offset(cx + 18f * scale, floorY)

          drawLine(color = bodyColor, start = hips, end = leftKnee, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = leftKnee, end = leftFoot, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = rightKnee, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = rightKnee, end = rightFoot, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Barbell resting across upper traps
          val barY = neck.y + 2f * scale
          drawLine(color = equipmentColor, start = Offset(cx - 50f * scale, barY), end = Offset(cx + 50f * scale, barY), strokeWidth = 4f * scale)
          drawRoundRect(color = CalorieCoral, topLeft = Offset(cx - 52f * scale, barY - 14f * scale), size = Size(8f * scale, 28f * scale), cornerRadius = CornerRadius(2f * scale, 2f * scale))
          drawRoundRect(color = CalorieCoral, topLeft = Offset(cx + 44f * scale, barY - 14f * scale), size = Size(8f * scale, 28f * scale), cornerRadius = CornerRadius(2f * scale, 2f * scale))

          // Quads & Glutes Highlight
          drawCircle(color = muscleHighlight, radius = 9f * scale, center = hips)
          drawCircle(color = muscleHighlight, radius = 8f * scale, center = Offset((hips.x + leftKnee.x) / 2f, (hips.y + leftKnee.y) / 2f))
        }

        ExerciseType.LUNGES -> {
          val lungeDrop = 28f * scale * cycle
          val headCenter = Offset(cx, cy - 48f * scale + lungeDrop)
          val neck = Offset(cx, cy - 34f * scale + lungeDrop)
          val hips = Offset(cx, cy + 8f * scale + lungeDrop)

          drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
          drawLine(color = bodyColor, start = neck, end = hips, strokeWidth = limbStroke + 2f, cap = StrokeCap.Round)

          val frontKnee = Offset(cx + 26f * scale, cy + 32f * scale + lungeDrop)
          val frontFoot = Offset(cx + 28f * scale, floorY)
          val backKnee = Offset(cx - 24f * scale, cy + 42f * scale + lungeDrop)
          val backFoot = Offset(cx - 36f * scale, floorY)

          drawLine(color = bodyColor, start = hips, end = frontKnee, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = frontKnee, end = frontFoot, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = backKnee, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = backKnee, end = backFoot, strokeWidth = limbStroke, cap = StrokeCap.Round)

          // Quads & Hamstring Highlight
          drawCircle(color = muscleHighlight, radius = 8f * scale, center = frontKnee)
        }

        // ==========================================
        // 8. CARDIO: JUMPING JACKS & HIGH KNEES
        // ==========================================
        ExerciseType.JUMPING_JACKS -> {
          val jumpY = -18f * scale * cycle
          val headCenter = Offset(cx, cy - 50f * scale + jumpY)
          val neck = Offset(cx, cy - 36f * scale + jumpY)
          val hips = Offset(cx, cy + 10f * scale + jumpY)

          drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
          drawLine(color = bodyColor, start = neck, end = hips, strokeWidth = limbStroke, cap = StrokeCap.Round)

          val armSpan = 45f * scale * cycle
          val armY = -30f * scale * cycle
          val leftHand = Offset(cx - 20f * scale - armSpan, cy - 10f * scale + jumpY + armY)
          val rightHand = Offset(cx + 20f * scale + armSpan, cy - 10f * scale + jumpY + armY)
          drawLine(color = bodyColor, start = neck, end = leftHand, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = neck, end = rightHand, strokeWidth = limbStroke, cap = StrokeCap.Round)

          val legSpread = 38f * scale * cycle
          drawLine(color = bodyColor, start = hips, end = Offset(cx - 10f * scale - legSpread, floorY + jumpY), strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = Offset(cx + 10f * scale + legSpread, floorY + jumpY), strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawCircle(color = muscleHighlight, radius = 6f * scale, center = hips)
        }

        ExerciseType.HIGH_KNEES -> {
          val legUp = cycle * 38f * scale
          val headCenter = Offset(cx, cy - 50f * scale)
          val neck = Offset(cx, cy - 36f * scale)
          val hips = Offset(cx, cy + 10f * scale)

          drawCircle(color = bodyColor, radius = headRadius, center = headCenter)
          drawLine(color = bodyColor, start = neck, end = hips, strokeWidth = limbStroke, cap = StrokeCap.Round)

          val kneeUp = Offset(cx + 18f * scale, cy + 16f * scale - legUp)
          drawLine(color = bodyColor, start = hips, end = kneeUp, strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = kneeUp, end = Offset(cx + 16f * scale, cy + 46f * scale - (legUp * 0.8f)), strokeWidth = limbStroke, cap = StrokeCap.Round)
          drawLine(color = bodyColor, start = hips, end = Offset(cx - 10f * scale, floorY), strokeWidth = limbStroke, cap = StrokeCap.Round)

          drawCircle(color = muscleHighlight, radius = 7f * scale, center = kneeUp)
        }
      }
    }

    // Top & Bottom Form Guidance Overlays
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = Color(0xCC0F172A),
      modifier = Modifier.align(Alignment.TopCenter).padding(top = 10.dp),
    ) {
      Text(
        text = targetLabel,
        color = NeonPulse,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
      )
    }

    Surface(
      shape = RoundedCornerShape(12.dp),
      color = Color(0xCC0F172A),
      modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 10.dp),
    ) {
      Text(
        text = phaseText,
        color = PulseCyan,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
      )
    }
  }
}
