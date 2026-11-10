package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.CoachPlanPackage
import com.example.model.MealType
import com.example.model.SubscriptionTier
import com.example.model.SupplementGearItem
import com.example.ui.theme.CalorieCoral
import com.example.ui.theme.EnergyAmber
import com.example.ui.theme.NeonPulse
import com.example.ui.theme.PulseCyan
import com.example.viewmodel.FitnessUiState
import java.util.Locale
import kotlinx.coroutines.delay

@Composable
fun MonetizationHubDialog(
  uiState: FitnessUiState,
  initialSection: Int = 0, // 0 = FitPulse Pro, 1 = Store, 2 = Coaching, 3 = Earnings
  onDismiss: () -> Unit,
  onUpgradeSubscription: (SubscriptionTier) -> Unit,
  onWatchRewardedAd: () -> Unit,
  onAffiliateItemClick: (SupplementGearItem) -> Unit,
  onQuickLogSupplementProtein: (String, Int, Int) -> Unit,
  onBookCoachPackage: (String, String) -> Unit,
) {
  var selectedSection by remember { mutableIntStateOf(initialSection) }
  var selectedTierOption by remember {
    mutableStateOf(
      if (uiState.subscriptionTier == SubscriptionTier.FREE) SubscriptionTier.PRO_ANNUAL
      else uiState.subscriptionTier
    )
  }
  var isWatchingAd by remember { mutableStateOf(false) }
  var adProgress by remember { mutableStateOf(0f) }
  var statusBanner by remember { mutableStateOf<String?>(null) }
  val uriHandler = LocalUriHandler.current

  LaunchedEffect(isWatchingAd) {
    if (isWatchingAd) {
      adProgress = 0f
      for (step in 1..10) {
        delay(180)
        adProgress = step / 10f
      }
      isWatchingAd = false
      onWatchRewardedAd()
      statusBanner = "Rewarded Ad Completed! +3 Free Pro AI Credits unlocked."
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      modifier = Modifier.fillMaxSize().testTag("monetization_hub_dialog"),
      color = MaterialTheme.colorScheme.background,
    ) {
      Column(modifier = Modifier.fillMaxSize()) {
        // Top Header Bar
        Surface(
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 4.dp,
        ) {
          Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
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
                      .clip(RoundedCornerShape(12.dp))
                      .background(Brush.linearGradient(listOf(EnergyAmber, NeonPulse))),
                  contentAlignment = Alignment.Center,
                ) {
                  Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = null,
                    tint = Color(0xFF002810),
                    modifier = Modifier.size(24.dp),
                  )
                }
                Column {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                  ) {
                    Text(
                      text = "FitPulse Pro & Store",
                      style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    )
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color =
                        if (uiState.isProSubscriber) NeonPulse.copy(alpha = 0.2f)
                        else EnergyAmber.copy(alpha = 0.2f),
                    ) {
                      Text(
                        text = uiState.subscriptionTier.badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (uiState.isProSubscriber) NeonPulse else EnergyAmber,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                      )
                    }
                  }
                  Text(
                    text = "Subscriptions • Gear Affiliate Store • 1-on-1 Coaching • Revenue",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                }
              }

              IconButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_close_monetization_hub"),
              ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4-Pillar Navigation Tabs
            val tabs = listOf("PRO Pass", "Gear & Whey", "1:1 Coach", "Earnings")
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
              tabs.forEachIndexed { index, title ->
                val isSelected = selectedSection == index
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color =
                    if (isSelected) NeonPulse
                    else MaterialTheme.colorScheme.surfaceVariant,
                  modifier =
                    Modifier.weight(1f)
                      .clickable { selectedSection = index }
                      .testTag("tab_monetize_$index"),
                ) {
                  Text(
                    text = title,
                    style =
                      MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                      ),
                    color = if (isSelected) Color(0xFF003818) else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 9.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                  )
                }
              }
            }
          }
        }

        // Status Feedback Banner
        AnimatedVisibility(visible = statusBanner != null) {
          statusBanner?.let { msg ->
            Surface(
              color = NeonPulse.copy(alpha = 0.16f),
              modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
              shape = RoundedCornerShape(12.dp),
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  modifier = Modifier.weight(1f),
                ) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonPulse, modifier = Modifier.size(18.dp))
                  Text(
                    text = msg,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = NeonPulse,
                  )
                }
                Text(
                  text = "Dismiss",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.clickable { statusBanner = null },
                )
              }
            }
          }
        }

        // Main Content Body
        LazyColumn(
          modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
          contentPadding = PaddingValues(vertical = 16.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
          when (selectedSection) {
            // =========================================================================
            // TAB 0: FITPULSE PRO SUBSCRIPTIONS & REWARDED AD CREDITS
            // =========================================================================
            0 -> {
              item {
                Card(
                  shape = RoundedCornerShape(20.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                  border = BorderStroke(1.dp, NeonPulse.copy(alpha = 0.4f)),
                  modifier = Modifier.fillMaxWidth(),
                ) {
                  Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically,
                    ) {
                      Column {
                        Text(
                          text = "Unlock FitPulse PRO",
                          style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                          color = NeonPulse,
                        )
                        Text(
                          text = "Current Plan: ${uiState.subscriptionTier.displayName}",
                          style = MaterialTheme.typography.bodySmall,
                          color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                      }
                      Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = PulseCyan.copy(alpha = 0.16f),
                      ) {
                        Text(
                          text =
                            if (uiState.isProSubscriber) "UNLIMITED AI"
                            else "${uiState.proAiCredits} AI Credits",
                          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                          color = PulseCyan,
                          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        )
                      }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val proBenefits =
                      listOf(
                        "Unlimited AI Daily Meal Plans & Custom Macro Splits",
                        "Full 3D/Procedural Muscle Form Animations for All 14+ Routines",
                        "Advanced Protein Bioavailability & Micronutrient Tracking",
                        "Ad-Free Workout Sessions & Priority Cloud Backup",
                      )
                    proBenefits.forEach { benefit ->
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp),
                      ) {
                        Icon(
                          imageVector = Icons.Default.Verified,
                          contentDescription = null,
                          tint = NeonPulse,
                          modifier = Modifier.size(18.dp),
                        )
                        Text(
                          text = benefit,
                          style = MaterialTheme.typography.bodySmall,
                          color = MaterialTheme.colorScheme.onSurface,
                        )
                      }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Plan Options
                    val availableTiers =
                      listOf(
                        SubscriptionTier.PRO_MONTHLY,
                        SubscriptionTier.PRO_ANNUAL,
                        SubscriptionTier.LIFETIME_VIP,
                      )
                    availableTiers.forEach { tier ->
                      val isSelected = selectedTierOption == tier
                      val isCurrent = uiState.subscriptionTier == tier
                      Surface(
                        shape = RoundedCornerShape(14.dp),
                        color =
                          if (isSelected) NeonPulse.copy(alpha = 0.14f)
                          else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border =
                          BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) NeonPulse else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                          ),
                        modifier =
                          Modifier.fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedTierOption = tier }
                            .testTag("tier_option_${tier.name}"),
                      ) {
                        Row(
                          modifier = Modifier.fillMaxWidth().padding(14.dp),
                          horizontalArrangement = Arrangement.SpaceBetween,
                          verticalAlignment = Alignment.CenterVertically,
                        ) {
                          Column {
                            Row(
                              verticalAlignment = Alignment.CenterVertically,
                              horizontalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                              Text(
                                text = tier.displayName,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                              )
                              if (tier == SubscriptionTier.PRO_ANNUAL) {
                                Surface(
                                  shape = RoundedCornerShape(6.dp),
                                  color = EnergyAmber.copy(alpha = 0.2f),
                                ) {
                                  Text(
                                    text = "MOST POPULAR",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = EnergyAmber,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                  )
                                }
                              }
                            }
                            Text(
                              text = tier.priceLabel,
                              style = MaterialTheme.typography.bodySmall,
                              color = PulseCyan,
                            )
                          }
                          if (isCurrent) {
                            Text(
                              text = "ACTIVE",
                              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                              color = NeonPulse,
                            )
                          }
                        }
                      }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                      onClick = {
                        onUpgradeSubscription(selectedTierOption)
                        statusBanner = "Activated ${selectedTierOption.displayName}! Unlimited Pro features unlocked."
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = NeonPulse, contentColor = Color(0xFF003818)),
                      shape = RoundedCornerShape(12.dp),
                      modifier = Modifier.fillMaxWidth().testTag("btn_activate_pro_subscription"),
                    ) {
                      Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                      Spacer(modifier = Modifier.width(6.dp))
                      Text(
                        text =
                          if (uiState.subscriptionTier == selectedTierOption)
                            "Current Active Plan (${selectedTierOption.displayName})"
                          else "Start ${selectedTierOption.displayName}",
                        fontWeight = FontWeight.ExtraBold,
                      )
                    }
                  }
                }
              }

              // Rewarded Ad Pass Card (AdMob Monetization Stream)
              item {
                Card(
                  shape = RoundedCornerShape(18.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                  modifier = Modifier.fillMaxWidth(),
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                      Box(
                        modifier =
                          Modifier.size(40.dp)
                            .clip(CircleShape)
                            .background(EnergyAmber.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center,
                      ) {
                        Icon(Icons.Default.PlayCircleFilled, contentDescription = null, tint = EnergyAmber, modifier = Modifier.size(22.dp))
                      }
                      Column(modifier = Modifier.weight(1f)) {
                        Text(
                          text = "Free Athlete? Watch Sponsor Video",
                          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        )
                        Text(
                          text = "Unlock +3 Pro AI Meal & Nutrient Credits for free via Rewarded Ad",
                          style = MaterialTheme.typography.labelSmall,
                          color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                      }
                    }

                    if (isWatchingAd) {
                      Spacer(modifier = Modifier.height(12.dp))
                      Text(
                        text = "Playing Fitness Sponsor Video... ${(adProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = EnergyAmber,
                      )
                      Spacer(modifier = Modifier.height(6.dp))
                      LinearProgressIndicator(
                        progress = { adProgress },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = EnergyAmber,
                      )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                      onClick = { if (!isWatchingAd) isWatchingAd = true },
                      enabled = !isWatchingAd,
                      border = BorderStroke(1.dp, EnergyAmber),
                      shape = RoundedCornerShape(10.dp),
                      modifier = Modifier.fillMaxWidth().testTag("btn_watch_rewarded_ad"),
                    ) {
                      Text(
                        text = if (isWatchingAd) "Watching Sponsor Ad..." else "Watch 15s Ad (+3 Free AI Credits)",
                        color = EnergyAmber,
                        fontWeight = FontWeight.Bold,
                      )
                    }
                  }
                }
              }
            }

            // =========================================================================
            // TAB 1: SUPPLEMENTS & HOME GYM GEAR AFFILIATE STORE
            // =========================================================================
            1 -> {
              item {
                Text(
                  text = "Verified Supplements & Home Gym Store",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                  text = "Curated high-protein isolates, creatine, and home equipment. Every purchase supports FitPulse via affiliate partner commissions.",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }

              items(uiState.storeItems) { item ->
                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                  modifier = Modifier.fillMaxWidth().testTag("store_item_${item.id}"),
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
                        modifier = Modifier.weight(1f),
                      ) {
                        Box(
                          modifier =
                            Modifier.size(44.dp)
                              .clip(RoundedCornerShape(12.dp))
                              .background(PulseCyan.copy(alpha = 0.15f)),
                          contentAlignment = Alignment.Center,
                        ) {
                          Text(text = item.emoji, fontSize = 22.sp)
                        }
                        Column {
                          Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                          )
                          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                              shape = RoundedCornerShape(6.dp),
                              color = NeonPulse.copy(alpha = 0.15f),
                            ) {
                              Text(
                                text = item.specBadge,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = NeonPulse,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                              )
                            }
                            Surface(
                              shape = RoundedCornerShape(6.dp),
                              color = EnergyAmber.copy(alpha = 0.15f),
                            ) {
                              Text(
                                text = item.affiliateCommission,
                                style = MaterialTheme.typography.labelSmall,
                                color = EnergyAmber,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                              )
                            }
                          }
                        }
                      }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                      text = item.benefit,
                      style = MaterialTheme.typography.bodySmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically,
                    ) {
                      Column {
                        Text(
                          text = item.priceDisplay,
                          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                          color = PulseCyan,
                        )
                        Text(
                          text = "${item.clicksCount} athletes ordered",
                          style = MaterialTheme.typography.labelSmall,
                          color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                      }

                      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (item.id == "s1") {
                          OutlinedButton(
                            onClick = {
                              onQuickLogSupplementProtein("Gold Whey Isolate Shake", 120, 27)
                              statusBanner = "Logged +27g Protein (120 kcal) to Today's Nutrition!"
                            },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                          ) {
                            Text("+Log 27g P", fontSize = 11.sp, color = NeonPulse)
                          }
                        }
                        Button(
                          onClick = {
                            onAffiliateItemClick(item)
                            statusBanner = "Tracked Affiliate Partner Click for ${item.name}!"
                            runCatching { uriHandler.openUri(item.storeUrl) }
                          },
                          colors = ButtonDefaults.buttonColors(containerColor = PulseCyan, contentColor = Color(0xFF003444)),
                          shape = RoundedCornerShape(10.dp),
                          modifier = Modifier.testTag("btn_buy_affiliate_${item.id}"),
                        ) {
                          Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                          Spacer(modifier = Modifier.width(4.dp))
                          Text("View Deal", fontWeight = FontWeight.Bold)
                        }
                      }
                    }
                  }
                }
              }
            }

            // =========================================================================
            // TAB 2: 1-ON-1 PERSONAL COACHING & CUSTOM TRANSFORMATION PLANS
            // =========================================================================
            2 -> {
              item {
                Text(
                  text = "1-on-1 Personal Coaching & Custom Plans",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                  text = "Sell direct high-ticket transformation programs, custom macro coaching, and weekly form check-ins.",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
              }

              items(uiState.coachPackages) { pkg ->
                var customNote by remember { mutableStateOf(pkg.userNotes) }
                Card(
                  shape = RoundedCornerShape(18.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                  border =
                    if (pkg.isBooked) BorderStroke(1.5.dp, NeonPulse)
                    else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                  modifier = Modifier.fillMaxWidth().testTag("coach_package_${pkg.id}"),
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically,
                    ) {
                      Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PulseCyan.copy(alpha = 0.16f),
                      ) {
                        Text(
                          text = "${pkg.durationWeeks} WEEKS • ${pkg.coachSpecialty.uppercase()}",
                          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                          color = PulseCyan,
                          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        )
                      }
                      Text(
                        text = pkg.priceDisplay,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = NeonPulse,
                      )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                      text = pkg.title,
                      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    pkg.deliverables.forEach { line ->
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(vertical = 2.dp),
                      ) {
                        Icon(
                          imageVector = Icons.Default.CheckCircle,
                          contentDescription = null,
                          tint = NeonPulse,
                          modifier = Modifier.size(15.dp),
                        )
                        Text(
                          text = line,
                          style = MaterialTheme.typography.bodySmall,
                          color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                      }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                      value = customNote,
                      onValueChange = { customNote = it },
                      label = { Text("Your Target Weight / Schedule / Injuries") },
                      singleLine = true,
                      modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                      onClick = {
                        onBookCoachPackage(pkg.id, customNote.ifBlank { "Ready to start custom plan" })
                        statusBanner = "Booked ${pkg.title}! Coach intake confirmed."
                      },
                      colors =
                        ButtonDefaults.buttonColors(
                          containerColor = if (pkg.isBooked) PulseCyan else NeonPulse,
                          contentColor = Color(0xFF003818),
                        ),
                      shape = RoundedCornerShape(10.dp),
                      modifier = Modifier.fillMaxWidth().testTag("btn_book_coach_${pkg.id}"),
                    ) {
                      Text(
                        text =
                          if (pkg.isBooked) "Booked & Active ✓ (Update Goal Notes)"
                          else "Book 1-on-1 Coaching (${pkg.priceDisplay})",
                        fontWeight = FontWeight.ExtraBold,
                      )
                    }
                  }
                }
              }
            }

            // =========================================================================
            // TAB 3: CREATOR EARNINGS & MONETIZATION DASHBOARD
            // =========================================================================
            3 -> {
              item {
                val stats = uiState.revenueStats
                Card(
                  shape = RoundedCornerShape(20.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                  border = BorderStroke(1.5.dp, NeonPulse.copy(alpha = 0.5f)),
                  modifier = Modifier.fillMaxWidth().testTag("creator_revenue_card"),
                ) {
                  Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      horizontalArrangement = Arrangement.SpaceBetween,
                      verticalAlignment = Alignment.CenterVertically,
                    ) {
                      Column {
                        Text(
                          text = "Estimated Monthly App Revenue",
                          style = MaterialTheme.typography.labelMedium,
                          color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                          text = String.format(Locale.US, "$%.2f / mo", stats.totalEstimatedMonthlyUsd),
                          style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                          color = NeonPulse,
                        )
                      }
                      Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = NeonPulse,
                        modifier = Modifier.size(34.dp),
                      )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4 Revenue Streams Breakdown
                    RevenueStreamRow(
                      label = "1. FitPulse Pro Subscriptions (${stats.activeProSubscribers} active)",
                      amountUsd = stats.subscriptionRevenueUsd,
                      accentColor = NeonPulse,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RevenueStreamRow(
                      label = "2. 1-on-1 Coaching Plans (${stats.coachBookingsCount} booked)",
                      amountUsd = stats.coachingRevenueUsd,
                      accentColor = PulseCyan,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RevenueStreamRow(
                      label = "3. Supplement & Gear Affiliates (${stats.affiliateClicks} clicks)",
                      amountUsd = stats.affiliateCommissionUsd,
                      accentColor = EnergyAmber,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    RevenueStreamRow(
                      label = "4. AdMob Rewarded Video Ads (${stats.rewardedAdViews} views)",
                      amountUsd = stats.adRevenueUsd,
                      accentColor = CalorieCoral,
                    )
                  }
                }
              }

              item {
                Card(
                  shape = RoundedCornerShape(16.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                  modifier = Modifier.fillMaxWidth(),
                ) {
                  Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                      text = "How to Connect Live Payouts (Play Store & Bank)",
                      style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                      color = PulseCyan,
                    )
                    Text(
                      text = "1. Google Play Billing: Export the signed AAB/APK from AI Studio, upload to Google Play Console, and link your bank account under Payments Profile.",
                      style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                      text = "2. Amazon Associates / Supplement Affiliates: Replace the store URLs with your personal `?tag=your-affiliate-id` link to earn 8%–12% on every whey & dumbbell order.",
                      style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                      text = "3. Google AdMob: Connect your AdMob Rewarded Unit ID to monetize free users watching 15s videos for AI meal credits.",
                      style = MaterialTheme.typography.bodySmall,
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
}

@Composable
private fun RevenueStreamRow(
  label: String,
  amountUsd: Double,
  accentColor: Color,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.weight(1f),
    )
    Text(
      text = String.format(Locale.US, "$%.2f", amountUsd),
      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
      color = accentColor,
    )
  }
}
