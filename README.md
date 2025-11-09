# **Arkanoid Game – Object-Oriented Programming Project**

## 👥 **Authors**
**Group** — Class **K69I-IT1**  
1. **Nguyễn Duy Hiệu** – 24020134  
2. **Trần Thùy Dương** – 24020107  
3. **Nguyễn Hữu Cảnh** – 24020044  
4. **Đặng Xuân Tùng** – 24020350  

**Instructor:** Kiều Văn Tuyên - Nguyễn Trung Hiếu

**Semester:** HK1 – Năm học 2024–2025  

---

## 🎯 **Description**
This is a classic Arkanoid game developed in Java as a final project for Object-Oriented Programming course. The project demonstrates the implementation of OOP principles and design patterns.

**Key features:**
1. The game is developed using Java 17+ with JavaFX for GUI.
2. Implements core OOP principles: Encapsulation, Inheritance, Polymorphism, and Abstraction.
3. Applies multiple design patterns: Singleton, Factory Method.
4. Features multithreading for smooth gameplay and responsive UI.
5. Includes sound effects, animations, and power-up systems.

**Main Objective:**  
Break all the destructible bricks by controlling the **paddle** to bounce the ball without letting it fall.

---

## 🧩 **OOP Concepts Used**
1. **Encapsulation** – Private attributes and getters/setters for controlled access.  
2. **Inheritance** – Common behaviors shared by game objects like `Ball`, `Brick`, `Paddle`, etc.  
3. **Polymorphism** – Different brick types and power-ups override their unique behaviors.  
4. **Abstraction** – Abstract base classes define core structure of game entities.  

---

## 🧠 **Design Pattern**
The game follows the **Model–View–Controller (MVC)** pattern:

- **Model:** Game objects and logic (`Ball`, `Brick`, `Paddle`, etc.)  
- **View:** Game rendering (JavaFX UI)  
- **Controller:** Handles user input and game events  

---

## 🎮 **Game Controls**

| Key | Action |
|-----|--------|
| ← | Move paddle left |
| → | Move paddle right |
| P | Pause / Resume game |

---

## 🧱 **Brick Types**

| Image | Type | Durability |
|--------|------|-------------|
| ![Normal Brick](src/main/resources/images/brick/brick_green.png) | **Normal Brick** | Breaks after 1 hit |
| ![Hard Brick](src/main/resources/images/brick/brick_blue.png) | **Hard Brick** | Breaks after 2 hits |
| ![Very Hard Brick](src/main/resources/images/brick/brick_yellow.png) | **Very Hard Brick** | Breaks after 3 hits |
| ![Unbreakable Brick](src/main/resources/images/brick/brick_red.png) | **Unbreakable Brick** | Cannot be destroyed |

---

## 💥 **Power-ups**

| Image | Name | Effect |
|--------|------|--------|
| ![Expand](src/main/resources/images/powerups/powerup_expand/powerup_expand_1.png) | **Expand Paddle** | Increases paddle width for a short duration |
| ![Fast Ball](src/main/resources/images/powerups/powerup_fast_ball/powerup_fast_ball_1.png) | **Fast Ball** | Increases ball speed temporarily |
| ![Extra Life](src/main/resources/images/powerups/powerup_life/powerup_life_1.png) | **Extra Life** | Grants one extra life |
| ![Multi Ball](src/main/resources/images/powerups/powerup_multi_ball/powerup_multi_ball_1.png) | **Multi Ball** | Spawns multiple balls simultaneously |

---

## ❤️ **Lives System**
- Players start with **3 lives**.  
- Losing the ball decreases one life.  
- When lives reach **0**, the game ends with **Game Over**.  

---

## 🏆 **Winning Condition**
Destroy **all breakable bricks** to complete the level and win the game.

---

## 🎨 **Game Interface**

### Main Menu
![Menu](src/main/resources/images/menu/menu.png)  
*Main menu screen of the game.*

### Gameplay Screens
| Map 1 | Map 2 | Map 3 | Map 4 |
|-------|-------|-------|-------|
| ![Map 1](src/main/resources/images/background/Pixel-Art%20Background%201/frame0001.png) | ![Map 2](src/main/resources/images/background/Pixel-Art%20Background%202/frame0001.png) | ![Map 3](src/main/resources/images/background/Pixel-Art%20Background%203/frame0001.png) | ![Map 4](src/main/resources/images/background/Pixel-Art%20Background%204/preview.png) |

---

## ⚙️ **Technologies Used**

| Technology | Version | Purpose |
|-------------|----------|----------|
| Java | 17+ | Core language |
| JavaFX | 19.0.2 | GUI framework |
| Maven | 3.9+ | Build tool |

---

## 🚀 **How to Run**

1. Clone the repository:
   ```bash
   git clone https://github.com/[username]/Arkanoid-OOP.git
