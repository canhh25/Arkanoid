# **Arkanoid Game – Object-Oriented Programming Project**

## Authors
Group: 6 - Class: 2526_INT2204_1

1. **Nguyễn Duy Hiệu** – 24020134  
2. **Trần Thùy Dương** – 24020107  
3. **Nguyễn Hữu Cảnh** – 24020044  
4. **Đặng Xuân Tùng** – 24020350  

**Instructor:** Kiều Văn Tuyên - Nguyễn Trung Hiếu

**Semester:** HK1 – Năm học 2024–2025  

---

## Description
This is a classic Arkanoid game developed in Java as a final project for Object-Oriented Programming course. The project demonstrates the implementation of OOP principles and design patterns.

**Key features:**
1. The game is developed using Java 17+ with JavaFX for GUI.
2. Implements core OOP principles: Encapsulation, Inheritance, Polymorphism, and Abstraction.
3. Applies multiple design patterns: Singleton, Factory Method.
4. Features multithreading for smooth gameplay and responsive UI.
5. Includes sound effects, animations, and power-up systems.

**Game mechanics:**  
- Control a paddle to bounce a ball and destroy bricks
- Collect power-ups for special abilities
- Progress through multiple levels with increasing difficulty

---

# UML Diagram
## Class Diagram

<Insert hình ảnh Class Diagram>

---

## Design Pattern Implementation

---

## 1. Singleton

**Used in:** Game Manager

**Purpose:** 

## 2. Abstract Factory

**Used in:** abc

**Purpose:** 

---

# Multithreading Implementation

The game uses multiple threads to ensure smooth performance:

1. Game Loop Thread: Updates game logic at 60 FPS
2. Rendering Thread: Handles graphics rendering (EDT for JavaFX Application Thread)
3. Audio Thread Pool: Plays sound effects asynchronously
   
---

# Installation
1. Clone the project from the repository.
2. Open the project in the IDE.
3. Run the project.
   
---

# Usage

## Controls
| Key | Action |
|-----|--------|
| ← | Move paddle left |
| → | Move paddle right |
| P | Pause / Resume game |
| SPACE | Lauch Ball |

## How to play
1. Start the game: Click "Start" from the main menu.
2. Control the paddle: Use arrow keys to move left and right.
3. Launch the ball: Press SPACE to launch the ball from the paddle.
4. Destroy bricks: Bounce the ball to hit and destroy bricks.
5. Collect power-ups: Catch falling power-ups for special abilities.
6. Avoid losing the ball: Keep the ball from falling below the paddle.
7. Complete the level: Destroy all destructible bricks to advance.

## Power-ups

| Icon | Name | Effect |
|--------|------|--------|
| ![Expand](src/main/resources/images/powerups/powerup_expand/powerup_expand_1.png) | **Expand Paddle** | Increases paddle width for a short duration |
| ![Fast Ball](src/main/resources/images/powerups/powerup_fast_ball/powerup_fast_ball_1.png) | **Fast Ball** | Increases ball speed temporarily |
| ![Extra Life](src/main/resources/images/powerups/powerup_life/powerup_life_1.png) | **Extra Life** | Grants one extra life |
| ![Multi Ball](src/main/resources/images/powerups/powerup_multi_ball/powerup_multi_ball_1.png) | **Multi Ball** | Spawns multiple balls simultaneously |

## Brick Types

| Image | Type | Durability |
|--------|------|-------------|
| ![Normal Brick](src/main/resources/images/brick/brick_green.png) | **Normal Brick** | Breaks after 1 hit |
| ![Hard Brick](src/main/resources/images/brick/brick_blue.png) | **Hard Brick** | Breaks after 2 hits |
| ![Very Hard Brick](src/main/resources/images/brick/brick_yellow.png) | **Very Hard Brick** | Breaks after 3 hits |
| ![Unbreakable Brick](src/main/resources/images/brick/brick_red.png) | **Unbreakable Brick** | Cannot be destroyed |

## Lives System
- Players start with **3 lives**.  
- Losing the ball decreases one life.  
- When lives reach **0**, the game ends with **Game Over**.  

## Scoring System 
- Normal Brick: 10 points
- Hard Brick: 20 points
- Very Hard Brick: 30 points
  
---

# **Demo**

## Main Menu
![Menu](src/main/resources/images/menu/menu.png)  
*Main menu screen of the game.*

## Gameplay Screens
| Map 1 | Map 2 | Map 3 | Map 4 |
|-------|-------|-------|-------|
| ![Map 1](src/main/resources/images/background/Pixel-Art%20Background%201/frame0001.png) | ![Map 2](src/main/resources/images/background/Pixel-Art%20Background%202/frame0001.png) | ![Map 3](src/main/resources/images/background/Pixel-Art%20Background%203/frame0001.png) | ![Map 4](src/main/resources/images/background/Pixel-Art%20Background%204/preview.png) |

## Video demo

---
# Future Improvements
## Planned Features

1. Enhanced gameplay
   - Support load/save game functionality and leaderboard system
   - Add shop and coins system to buy skins for paddle and ball
   - Boss battle at the end of the worlds
   - More power-up varieties (gun, freeze time, shield wall, etc.)
2. Additional game modes
   - Survival mode with endless level
   - Co-op multiplayer mode
   - Time attack mode
3. Technical improvements
   - Implement AI opponent mode
   - Add an online leaderboard

---

# Technologies Used

| Technology | Version | Purpose |
|-------------|----------|----------|
| Java | 17+ | Core language |
| JavaFX | 19.0.2 | GUI framework |
| Maven | 3.9+ | Build tool |

---

# License 
This project is developed for educational purposes only.

**Academic Integrity:** This code is provided as a reference. Please follow your institution's academic integrity policies.

---

# Notes
- The game was developed as part of the Object-Oriented Programming with Java course curriculum.
- All code is written by group members with guidance from the instructor.
- Some assets (images, sounds) may be used for educational purposes under fair use.
- The project demonstrates practical application of OOP concepts and design patterns.

