# Public App Access & Private User Login Plan

## 1. Privacy & Public Access Architecture
- **Zero Owner Email or Personal Data Exposure**:
  - Your personal email and account credentials are **never** stored in the codebase, shared links, or UI.
  - Public visitors opening the shared FitPulse link only see the FitPulse app and their own sign-in screen.
- **Strict Email Privacy for All Users**:
  - When any user signs in with their email or Google account, their email address is kept strictly private (masked in their own account settings and **never** visible to other users or workout buddies).
  - Public/community leaderboards and invite cards display **only** the user's chosen **Display Name / Username** and **Avatar Emoji**.

---

## 2. Visual Design & User Experience (`AuthScreen.kt` & `CommunityAndUsersDialog.kt`)
- **Modern FitPulse Sign-In & Sign-Up Screen (`AuthScreen.kt`)**:
  - **Sign In / Create Account with Email**: Clean Material 3 form where any public user can enter their own email address, password, display name, fitness goal, and avatar emoji.
  - **Continue with Google**: One-tap Google account sign-in option for fast onboarding.
  - **Privacy Guarantee Banner**: Clear badge on the login screen confirming *"Your email is private and never shared publicly. Only your chosen display name is visible to workout buddies."*
- **Account Privacy & Sign-Out Controls**:
  - In the Top App Bar and **Community & Multi-User Hub**, signed-in users can view their masked email (e.g., `a***@gmail.com`), toggle **"Hide my profile from public leaderboard"**, switch accounts, or click **Sign Out** to return to the login screen.

---

## 3. Planned File Changes
- `app/src/main/java/com/example/model/FitnessModels.kt`:
  - Update `UserProfile` to include `maskedEmail: String`, `authProvider: String` (`"Email"` or `"Google"`), and `isPublicProfile: Boolean` (with no personal/owner email anywhere).
- `app/src/main/java/com/example/ui/AuthScreen.kt`:
  - Create the full-bleed dark-neon **FitPulse Login & Registration Screen** supporting Email Sign-In, New Account Registration, and Google Sign-In.
- `app/src/main/java/com/example/viewmodel/FitnessViewModel.kt`:
  - Add `isAuthenticated`, `signInWithEmail(...)`, `signUpWithEmail(...)`, `signInWithGoogle(...)`, `toggleProfilePrivacy()`, and `signOut()` state management.
- `app/src/main/java/com/example/ui/CommunityAndUsersDialog.kt` & `FitPulseApp.kt`:
  - Gate the app behind `AuthScreen` when signed out, add a **Sign Out / Account Privacy** section in the profile dialog, and ensure all shared links/cards never include any email address.
