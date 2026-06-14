# 🚀 Animaciones Jetpack Compose - Laboratorio 13

Una aplicación Android moderna desarrollada con **Jetpack Compose** que demuestra el poder de las APIs de animación para crear interfaces de usuario fluidas, interactivas y visualmente atractivas.

## 📱 Vista General
Esta aplicación está organizada de forma modular y presenta 5 secciones interactivas que cubren desde conceptos básicos hasta una implementación compleja en un prototipo de videojuego.

---

## ✨ Características Principales

### 1. 👁️ Control de Visibilidad
- Uso de `AnimatedVisibility` para entradas y salidas suaves.
* **Efectos:** Combinación de `fadeIn` + `scaleIn` y `fadeOut` + `scaleOut`.
* **Diseño:** Cuadro estilizado con degradados modernos.

### 2. 🎨 Transiciones de Color
- Implementación de `animateColorAsState`.
* **Funcionalidad:** Cambio fluido entre los colores del tema y variantes personalizadas.
* **Interactividad:** Botones que reflejan el estado del color actual.

### 3. 📏 Dinámicas de Tamaño y Posición
- Uso de `animateDpAsState` y `Modifier.offset`.
* **Física:** Aplicación de `spring` (resortes) para un movimiento elástico y natural.
* **Control:** Expansión y desplazamiento simultáneo de elementos UI.

### 4. 🔄 Cambio de Contenido Dinámico
- Implementación de `AnimatedContent`.
* **Estados:** Manejo de estados de carga (`Loading`), éxito (`Content`) y error (`Error`).
* **Transiciones:** Desplazamiento vertical con desvanecimiento entre estados.

### 5. 🎮 Prototipo de Videojuego (Mini-Game: Cyber-Battle)
- Ejecución en una ventana dedicada horizontal (`GameActivity` en Landscape).
- Control fluido continuo mediante `Joystick` con calibración de densidad (`LocalDensity`).
- **Múltiples APIs de Animación combinadas:**
  * **Efecto Pop al recibir daño:** Animación de escala de resorte (`animateFloatAsState` con `dampingRatio = Spring.DampingRatioHighBouncy`) que aumenta momentáneamente de tamaño (efecto "pop") al impactar una bala y rebota a su estado original.
  * **Textos de daño flotantes:** Partículas de texto dinámicas (`💥 -8% HP`, `⚡ IMPACTO!`, `🛡️ ESQUIVADO`) animadas frame a frame hacia arriba con desvanecimiento de opacidad (`alpha`).
  * **Fondo con Paralaje 3D Activo:**
    * **Estrellas en movimiento continuo:** 45 estrellas distribuidas en 3 planos de profundidad viajando a velocidades diferenciadas hacia la izquierda.
    * **Nebulosas Dinámicas:** 3 nubes espaciales gigantes en tonos cian, violeta e índigo flotando muy lentamente en el fondo.
    * **Cinturón de Asteroides:** Escombros espaciales (`🪨` y `☄️`) en rotación continua y deriva lineal.
  * **Estrellas Fugaces:** Trazos rápidos en diagonal con gradientes lineales atenuantes en un Canvas dinámico.
  * **Invulnerabilidad con Parpadeo:** Transición infinita (`rememberInfiniteTransition` con `animateFloat`) para oscilar el canal `alpha` del jugador mientras es inmune.
  * **Trayectoria de Vuelo del Jefe:** Desplazamiento vertical ondulado continuo mediante `animateOffsetAsState` en base a una función senoidal.
  * **HUD de salud LED neón:** Barras de vida que brillan mediante sombras pulsantes (`rememberInfiniteTransition`) y un gradiente tricolor que simula un núcleo incandescente.
  * **Visibilidad del Jefe:** Entrada y salida con `AnimatedVisibility` usando `fadeIn` + `scaleIn` y `fadeOut` + `scaleOut`.

---

## 🛠️ Tecnologías y Herramientas
* **Lenguaje:** Kotlin 2.2.10
* **UI Framework:** Jetpack Compose con Material Design 3
* **Arquitectura:** Modular por paquetes (visibilidad, color, dimensiones, contenido, videojuego)
* **Iconografía:** Material Icons Extended
* **Build System:** Gradle (Kotlin DSL)

---

## 📂 Estructura del Proyecto
```text
com.example.glab_s13_bpareja_2025
├── components
│   ├── color        # Ejercicio 2: Color
│   ├── comun        # Tarjetas y elementos compartidos
│   ├── contenido    # Ejercicio 4: Estados
│   ├── dimensiones  # Ejercicio 3: Layout
│   ├── videojuego   # Prototipo Final
│   └── visibilidad  # Ejercicio 1: Intro
└── MainActivity     # Orquestador principal
```

---

## 👤 Autor
**IVAN APAZA**  
*Laboratorio 13 - Desarrollo de Aplicaciones Móviles*
