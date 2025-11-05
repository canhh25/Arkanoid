# ARKANOID – OOP PROJECT

## Member
MSSV: 24020134 — Name: Nguyễn Duy Hiệu <br>
MSSV: 24020107 — Name: Trần Thùy Dương <br>
MSSV: 24020044 — Name: Nguyễn Hữu Cảnh <br>
MSSV: 24020350 — Name: Đặng Xuân Tùng 

## 🎯 Mục tiêu  
Phá vỡ tất cả các viên gạch bằng cách điều khiển thanh **paddle** đỡ bóng sao cho bóng không rơi xuống.  
Game được viết bằng **JavaFX**, tuân theo mô hình **MVC**.

---

## 🕹️ Cách chơi  
| Hành động | Phím |
|------------|------| 
| Di chuyển sang trái | ←|
| Di chuyển sang phải | →| 
| Tạm dừng / Tiếp tục | P |
---

## 🧱 Brick
| Hình ảnh | Loại gạch | Độ bền |
|-----------|------------|--------|
| ![Normal Brick](src/main/resources/images/brick/brick_green.png) | **Gạch thường** | 1 lần chạm |
| ![Hard Brick](src/main/resources/images/brick/brick_blue.png) | **Gạch cứng** | 2 lần chạm |
| ![VeryHard Brick](src/main/resources/images/brick/brick_yellow.png) | **Gạch siêu cứng** | 3 lần chạm|
| ![Unbreakable Brick](src/main/resources/images/brick/brick_red.png) | **Gạch không phá được** | Không thể phá |
---

## 💥 Power-ups  

| Hình ảnh | Tên | Hiệu ứng |
|-----------|------|----------|
| ![Expand](src/main/resources/images/powerups/powerup_expand/powerup_expand_1.png) | **Expand Paddle** | Làm **rộng paddle**  trong một khoảng thời gian nhất định |
| ![Fast Ball](src/main/resources/images/powerups/powerup_fast_ball/powerup_fast_ball_1.png) | **Fast Ball** | Bóng **di chuyển nhanh hơn** |
| ![Extra Life](src/main/resources/images/powerups/powerup_life/powerup_life_1.png) | **Extra Life** | Thêm **1 mạng** |
| ![Multi Ball](src/main/resources/images/powerups/powerup_multi_ball/powerup_multi_ball_1.png) | **Multi Ball** | Tạo thêm nhiều bóng cùng lúc |

---

## ❤️ Mạng sống  
- Người chơi bắt đầu với **3 mạng**.  
- Mỗi khi bóng rơi xuống dưới, mất 1 mạng.  
- Khi hết mạng: **Game Over**.

---

## 🏆 Thắng cuộc  
Phá **toàn bộ gạch có thể phá** để qua màn hoặc chiến thắng trò chơi.

---

## 🎨 Giao diện menu  

![Menu](src/main/resources/images/menu/menu.png)  
*Giao diện menu chính của game*  

## 🎮 Giao diện gameplay

| Map 1 | Map 2 | Map 3 | Map 4 |
|-------|-------|-------|-------|
| ![Map 1](src/main/resources/images/background/Pixel-Art%20Background%201/frame0001.png) | ![Map 2](src/main/resources/images/background/Pixel-Art%20Background%202/frame0001.png) | ![Map 3](src/main/resources/images/background/Pixel-Art%20Background%203/frame0001.png) | ![Map 4](src/main/resources/images/background/Pixel-Art%20Background%204/preview.png) |
