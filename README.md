# Arkanoid

## Member
MSSV: 24020134 — Name: Nguyễn Duy Hiệu <br>
MSSV: 24020107 — Name: Trần Thùy Dương <br>
MSSV: 24020044 — Name: Nguyễn Hữu Cảnh <br>
MSSV: 24020350 — Name: Đặng Xuân Tùng 

# 🧱 ARKANOID – OOP PROJECT

## 🎯 Mục tiêu  
Phá vỡ tất cả các viên gạch bằng cách điều khiển thanh **paddle** đỡ bóng sao cho bóng không rơi xuống.  
Game được viết bằng **JavaFX**, tuân theo mô hình **MVC**.

---

## 🕹️ Cách chơi  
| Hành động | Phím |
|------------|------|
| Di chuyển sang trái | `←`|
| Di chuyển sang phải | `→`|
| Tạm dừng / Tiếp tục | `P` |
---

## 💥 Power-ups  

| Hình ảnh | Tên | Hiệu ứng |
|-----------|------|----------|
| ![Expand](src/main/resources/images/powerups/powerup_expand/icon.png) | **Expand Paddle** | Làm **rộng paddle** giúp dễ đỡ bóng hơn |
| ![Fast Ball](src/main/resources/images/powerups/powerup_fast_ball/icon.png) | **Fast Ball** | Bóng **di chuyển nhanh hơn** |
| ![Extra Life](src/main/resources/images/powerups/powerup_life/icon.png) | **Extra Life** | Thêm **1 mạng** |
| ![Multi Ball](src/main/resources/images/powerups/powerup_multi_ball/icon.png) | **Multi Ball** | Tạo thêm nhiều bóng cùng lúc |

> 📝 Lưu ý: Đảm bảo mỗi thư mục `powerup_*` có 1 ảnh đại diện, ví dụ `icon.png`.

---

## ❤️ Mạng sống  
- Người chơi bắt đầu với **3 mạng**.  
- Mỗi khi bóng rơi xuống dưới, mất 1 mạng.  
- Khi hết mạng → **Game Over**.

---

## 🏆 Thắng cuộc  
Phá **toàn bộ gạch** để qua màn hoặc chiến thắng trò chơi.

---

## 🎨 Giao diện mẫu  

![Menu](src/main/resources/images/menu/menu_preview.png)  
*Giao diện menu chính của game*  

![Gameplay](src/main/resources/images/background/Pixel-Art%20Background%201/preview.png)  
*Một màn chơi với nền pixel-art*

---

## 🧰 Cấu trúc dự án  

