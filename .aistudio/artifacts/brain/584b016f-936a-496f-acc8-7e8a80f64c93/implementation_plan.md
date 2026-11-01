# Artifacts Guide & Shareable Progress Cards Plan

## 1. What Are Artifacts?
Depending on what you are looking at, **Artifacts** refer to three distinct things:

1. **AI Studio Artifacts (Plans & Media)**:
   - Interactive documents (like this **Implementation Plan** card!) or generated images created during chat sessions so you can review, edit, or click **Proceed** to build.
2. **Android Build Artifacts (APK / AAB)**:
   - The installable Android app file (`app-debug.apk` or release `.aab`) compiled from your project.
   - **How to get your APK**: Use the **Settings / Export** menu in the top-right corner of AI Studio to download the project ZIP, push to GitHub, or generate an APK to install on your phone.
3. **In-App Shareable Artifacts (FitPulse Cards & Reports)**:
   - Exportable workout achievement cards, nutrition summaries, and custom meal plans that athletes can share directly from FitPulse.

---

## 2. Proposed Enhancement: FitPulse Shareable Progress & Workout Cards
If you click **Proceed**, we will add an **Artifact & Report Exporter** inside FitPulse so you and your workout buddies can generate and share visual summary cards:

### Key Features to Build
- **Shareable Workout Milestone Card**:
  - Generates a styled summary card showing completed workouts, target muscles trained (Biceps, Triceps, Abs, Chest, Back, Shoulders, Legs), total calories burned, and active streak.
  - One-tap **Share via Android Share Sheet** or **Copy Formatted Report**.
- **Exportable Nutrition & Protein Report**:
  - Summarizes daily protein, carbs, fats, and AI-generated meal plans into a clean shareable card for coaches or workout buddies.
- **Buddy Challenge Snapshot**:
  - Lets any profile in the Multi-User Hub export their weekly leaderboard standing and invite code in a single card.

### Planned File Changes
- `app/src/main/java/com/example/ui/ShareableArtifactDialog.kt`: New interactive preview and export dialog for Workout Cards, Macro Reports, and Buddy Leaderboard snapshots.
- `app/src/main/java/com/example/ui/FitPulseApp.kt`: Add a quick **"Export Summary Card"** action on the **Today** and **Progress** tabs.
