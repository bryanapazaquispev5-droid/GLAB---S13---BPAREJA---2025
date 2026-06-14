# Laboratory 13: Animations in Jetpack Compose

## Student Information
- **Name:** BPAREJA
- **Group:** [Your Group]
- **Cycle:** [Your Cycle]

## Objectives
- Practice the creation and customization of animations in Jetpack Compose.
- Implement visibility, color, size, and position animations.
- Use `AnimatedVisibility`, `animateColorAsState`, `animateDpAsState`, and `AnimatedContent`.

## Exercises Completed

### Exercise 1: Visibility Animation
- Implemented a toggle button to show/hide a blue box.
- Used `AnimatedVisibility` with `fadeIn` and `fadeOut` effects.

### Exercise 2: Color Animation
- Implemented a button to transition between Blue and Green colors.
- Used `animateColorAsState` with a `tween` animation specification.

### Exercise 3: Size and Position Animation
- Implemented a button to simultaneously change the size and position of a red box.
- Used `animateDpAsState` for size and `Modifier.offset` for position.

### Exercise 4: Content Transition
- Implemented a state machine (Loading, Content, Error) using an enum.
- Used `AnimatedContent` for smooth transitions between states with custom fade animations.

### Final Exercise: Videogame Prototype
- Developed a mini-game prototype combining all previous techniques.
- Features character movement, attack animations (scaling), enemy health bar animation, and enemy appearance/disappearance effects.

## Observation Question

**1. Selecciona una parte del laboratorio que te haya resultado más desafiante. Explica por qué fue difícil y qué hiciste para resolverlo.**
*Respuesta:* La parte más desafiante fue el prototipo de videojuego combinado. Coordinar múltiples estados (ataque, movimiento, vida del enemigo) y asegurar que las animaciones se sintieran fluidas requirió un uso cuidadoso de `animate*AsState` y `LaunchedEffect` para manejar los efectos secundarios de las animaciones.

**2. Explica cómo usaste la IA para el desarrollo de este laboratorio.**
*Respuesta:* Se utilizó la IA para estructurar el proyecto de Jetpack Compose, proporcionar plantillas de código para las APIs de animación específicas y ayudar a depurar las transiciones de estado entre las fases del juego.

## Commits
Commits were made for each step following best practices in English.
