# Local Passwordless Authentication App (Email + OTP)

This project implements a **passwordless authentication flow** using **Email + OTP**, followed by a **session tracking screen**.  
All logic is implemented **locally** (no backend), focusing on **state management, time-based logic, and Jetpack Compose best practices**.

---

## ✨ Features

- Email-based login with locally generated OTP
- OTP rules enforced:
  - 6-digit OTP
  - 60-second expiry
  - Maximum 3 attempts
  - Resending OTP invalidates the previous one
- Live OTP countdown timer
- Session screen with:
  - Session start time
  - Live session duration (mm:ss)
  - Logout functionality
- Robust state handling using **ViewModel + sealed UI states**
- External SDK integration using **Timber logging**

---

## 🏗️ Architecture Overview

The app follows a **clean MVVM-style architecture** with one-way data flow.

UI (Jetpack Compose)

↓ observes

AuthUiState (sealed class)

↓ updated by

AuthViewModel

↓ uses

OtpManager (pure business logic)

↓ logs via

AnalyticsLogger (Timber)

### Key Principles
- UI is **stateless** and reacts only to state changes
- Business logic is fully isolated in the ViewModel and data layer
- No global mutable state
- Timers are lifecycle-safe using coroutines

---

## 🔐 OTP Logic & Expiry Handling

- OTPs are generated locally using `OtpManager`
- OTP data is stored **per email** using a `Map<String, OtpData>`
- Each OTP includes:
  - OTP value
  - Creation time
  - Expiry time (current time + 60 seconds)
  - Remaining attempts
- Validation checks are performed in this order:
  1. Expiry check
  2. Attempt exhaustion check
  3. OTP match check
- Resending OTP:
  - Invalidates the previous OTP
  - Resets attempt count

---

## 🧠 State Management

UI state is modeled using **sealed UI states**:

- `EmailEntry`
- `OtpEntry`
- `Session`

This ensures:
- Impossible UI states are prevented at compile time
- Clear and predictable screen transitions
- Easier reasoning during recomposition and rotation

Timers (OTP countdown & session duration) are managed **inside the ViewModel** using `viewModelScope`, ensuring:
- Survival across recompositions
- Proper cancellation on logout

---

## 📊 External SDK Integration (Timber)

**Timber** is used as the external SDK for structured logging.

### Why Timber?
- Lightweight and simple
- No backend or API keys required
- Ideal for logging app events in a local-only assignment

### Logged Events
- OTP generated
- OTP validation success
- OTP validation failure (incorrect / expired / attempts exceeded)
- User logout

Logs can be viewed using **Logcat** during runtime.

---

## 🧪 Debug OTP (Testing Purpose)

Since no backend or email service is used, the generated OTP is **displayed on the OTP screen for testing purposes only**.  
This is clearly marked as debug behavior and allows full end-to-end testing of the flow.

---

## 🤖 Use of AI Tools

AI tools (ChatGPT) were used for:
- Concept clarification (Compose, state management)
- Architecture validation
- Code review and refinement

All core logic, design decisions, and implementation were **fully understood and manually written**.

---

## ▶️ How to Run

1. Clone the repository
2. Open in **Android Studio**
3. Sync Gradle
4. Run on an emulator or physical Android device (API 24+)

No additional setup required.
Tested on a physical Android device.

---

## 📌 Tech Stack

- Kotlin
- Jetpack Compose (Material 3)
- ViewModel + UI State
- Kotlin Coroutines
- Timber (logging)

---

## ✅ Assignment Coverage

✔ Email + OTP login  
✔ OTP rules enforced  
✔ Session screen with live timer  
✔ State-safe Compose UI  
✔ External SDK integration  
✔ Clear documentation  
