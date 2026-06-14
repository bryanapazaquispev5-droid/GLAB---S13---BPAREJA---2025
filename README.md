# 🚀 Jetpack Compose Animations - Laboratory 13

A modern Android application developed with **Jetpack Compose** showcasing advanced animation APIs to create smooth, interactive, and visually stunning user interfaces.

## 📱 Project Overview
This application is organized in a highly modular fashion, presenting 5 interactive sections ranging from basic transition concepts to a complete arcade video game.

---

## ✨ Key Features

### 1. 👁️ Visibility Control
- Uses `AnimatedVisibility` for elegant component entries and exits.
- **Effects:** Combined `fadeIn` + `scaleIn` and `fadeOut` + `scaleOut` transitions.
- **UI Design:** Stretched cards with modern background gradients.

### 2. 🎨 Color Transitions
- Implements `animateColorAsState` for smooth palette transitions.
- **Functionality:** Real-time fluid color morphing matching the primary material theme.
- **Interactive Controls:** Toggle buttons reflecting the current selected states.

### 3. 📏 Size and Position Dynamics
- Utilizes `animateDpAsState` and `Modifier.offset`.
- **Physics Engine:** Custom `spring` configuration providing natural elastic bounces.
- **Control:** Simultaneous scaling and coordinate shifting of UI components.

### 4. 🔄 Dynamic Content Swapping
- Implements `AnimatedContent` for clean UI state transitions.
- **State Machine:** Handles `Loading`, `Content` (success), and `Error` layouts.
- **Transitions:** Elegant vertical slide-and-fade animations.

### 5. 🎮 Retro Arcade Video Game (Mini-Game: Cyber-Battle)
- **Dedicated Windows:** Launches in a lock-oriented landscape view (`GameActivity`).
- **Lag-Free Movement:** Joystick with screen-pixel density scaling (`LocalDensity`) updating at 60 FPS.
- **Character Animation (Lottie JSON Integration):**
  - Displays high-quality vector animations for both the player (`bueno.json`) and the boss (`malo.json`).
  - Animates the Lottie composition loop infinitely at 60 FPS.
- **Advanced Compose Animation Mashup:**
  - **Impact Pop Scale Effect:** Leverages a spring-based scale animation (`animateFloatAsState` with `dampingRatio = Spring.DampingRatioHighBouncy`) that triggers a visual "pop" bounce whenever a bullet hits a target, immediately returning it to normal size.
  - **Floating Damage/Text Particles:** Fades and scrolls custom indicators (`💥 -12% HP`, `⚡ IMPACT!`, `🛡️ DODGED`) upward frame by frame.
  - **Active Parallax Space Background:**
    - **Parallax Starfield:** 45 stars scrolling across 3 distinct speed layers.
    - **Nebula Clouds:** 3 slow-moving gaseous dust clouds with radial color gradients (Cyan, Indigo, and Fuchsia).
    - **Asteroid Belt:** Rotating space debris (`🪨` and `☄️`) with independent spin rates.
  - **Shooting Stars:** Draws diagonal velocity vectors on a canvas layer with linear gradient tails.
  - **HUD LED Health Bars:** Glowing indicators utilizing pulsing drop-shadows and vertical core-white LED gradients.
  - **Programmatic 8-bit Sound Synthesizer:** Real-time PCM wave generation via `AudioTrack`:
    - *Laser Shot*: Square wave sliding frequency pitch (`1000Hz` to `300Hz`).
    - *Explosions*: Attenuated white noise bursts.
    - *Damage Buzzer*: Low-frequency square wave.
    - *Victory Arpeggio*: Upbeat ascending major chord progression.
    - *Defeat Chime*: Descending somber minor melody.
    - *Power-Up Sound*: Rapid ascending arpeggio.
  - **Boss Rage Mode:** Activates at $\le$ 50% Boss HP, accelerating the BGM loop speed and triggering a 4-way diagonal laser spray pattern.
  - **Combos & Scores:** Scoring multiplier linked to uninterrupted hits, saved locally via `SharedPreferences`.
  - **Screen Shake:** Oscillates the main game frame on player hit.
- **Balanced Game Difficulty:**
  - Player lasers do **1% damage** (requires 100 hits to defeat the boss).
  - Boss projectiles do **12% damage** (player is defeated in 8-9 hits).
  - Faster and more aggressive bullet patterns in boss Rage Mode.

---

## 🛠️ Architecture & Technologies
- **Language:** Kotlin 2.2.10
- **UI Toolkit:** Jetpack Compose with Material Design 3
- **Animations:** Lottie Compose 6.4.0
- **Icons:** Material Icons Extended
- **Build Tool:** Gradle (Kotlin DSL) with Version Catalogs

---

## 📂 Project Structure
```text
com.example.glab_s13_bpareja_2025
├── components
│   ├── color        # Exercise 2: Color transitions
│   ├── comun        # Shared common UI cards & Joystick
│   ├── contenido    # Exercise 4: Content states
│   ├── dimensiones  # Exercise 3: Size & position animations
│   ├── videojuego   # Final Arcade Game
│   │   ├── BgmManager.kt        # 2-Voice BGM loop tracker
│   │   ├── GameBackground.kt    # Space Parallax Background
│   │   ├── GameModels.kt        # Data structures & parameters
│   │   ├── GameScreen.kt        # Main orchestrator & state loop
│   │   ├── HealthBar.kt         # Custom neon LED health bar
│   │   ├── HighScoreManager.kt  # Persistent SharedPreferences records
│   │   ├── LottieCharacter.kt   # Lottie JSON player & boss renders
│   │   ├── ParticleSystem.kt    # Sparks & explosion emitters
│   │   └── SoundManager.kt      # Real-time PCM synthesizer
│   └── visibilidad  # Exercise 1: Visibility entry/exit
└── MainActivity     # Main application launcher
```

---

## 👤 Credits
**IVAN APAZA**  
*Laboratorio 13 - Mobile Application Development*
