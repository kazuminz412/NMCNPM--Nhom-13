# 🏢 BlueMoon - Hệ Thống Quản Lý Dân Cư Chung Cư (Nhóm 13)

BlueMoon là một giải pháp phần mềm quản lý chung cư toàn diện, được xây dựng nhằm mục đích số hóa và tự động hóa các quy trình quản lý hộ dân, nhân khẩu, phương tiện và đặc biệt là hệ thống thu/đóng phí của tòa nhà.

Dự án được thực hiện phục vụ cho Đồ án môn học **Nhập môn Công nghệ Phần mềm**.

---

## 🌟 Các Tính Năng Nổi Bật

Hệ thống được chia thành 2 phân hệ chính với cơ chế phân quyền chặt chẽ thông qua JWT:

### 1. Phân hệ Ban Quản Lý & Kế Toán
- **📊 Bảng Điều Khiển (Dashboard):** Thống kê trực quan doanh thu tổng, tỷ lệ thu phí, biểu đồ tròn thể hiện trạng thái đóng phí và biểu đồ cột so sánh khoản thu.
- **👨‍👩‍👧‍👦 Quản Lý Hộ Dân & Nhân Khẩu:** Thêm, sửa, xóa, tìm kiếm hộ dân, theo dõi chi tiết danh sách người đang cư trú và trạng thái (Thường trú, Tạm trú, Tạm vắng).
- **🚗 Quản Lý Phương Tiện:** Đăng ký và theo dõi xe cộ của từng hộ, tự động áp phí giữ xe hàng tháng.
- **💰 Quản Lý Thu Phí:** Tự động tạo hóa đơn định kỳ, ghi nhận đóng góp tự nguyện (Quỹ từ thiện, Quỹ khuyến học), quản lý lịch sử giao dịch và xuất biên lai thanh toán.
- **🔒 Phân Quyền:** Cấp quyền riêng biệt cho Admin (toàn quyền) và Kế Toán (chỉ quản lý thu phí).

### 2. Phân hệ Cổng Thông Tin Cư Dân
- **🏠 Nhà Của Tôi:** Cư dân đăng nhập để xem thông tin hợp đồng, thành viên trong nhà và danh sách xe cộ đã đăng ký.
- **🧾 Hóa Đơn & Thanh Toán:** Nhận thông báo hóa đơn hàng tháng, kiểm tra chi tiết các khoản phí và đóng góp tự nguyện. Tích hợp mã QR Code để thanh toán chuyển khoản dễ dàng.
- **📜 Lịch Sử Giao Dịch:** Xem lại toàn bộ lịch sử các lần đã đóng tiền.

---

## 🛠 Công Nghệ Sử Dụng

- **Frontend:** HTML5, CSS3, Vanilla JavaScript, Fetch API (Không phụ thuộc vào framework nặng, tối ưu tốc độ và dễ dàng tùy biến). Giao diện thiết kế theo phong cách Glassmorphism hiện đại.
- **Backend:** Java, Spring Boot 3, Spring Security, Spring Data JPA, JWT (JSON Web Token) đảm bảo tính năng bảo mật chuẩn RESTful API.
- **Database:** MySQL 8.0.

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Dự Án

### Bước 1: Khởi tạo Cơ sở dữ liệu (Database)
1. Mở phần mềm quản lý MySQL (XAMPP, MySQL Workbench, DBeaver, Navicat,...).
2. Tạo một database rỗng với tên: `bluemoon_db`.
3. Import file `database_backup_final.sql` (đính kèm trong mã nguồn) vào database vừa tạo. File này đã chứa sẵn cấu trúc bảng và toàn bộ 250 hộ dân cùng dữ liệu thực tế mẫu.

### Bước 2: Chạy Backend (Spring Boot)
1. Yêu cầu hệ thống đã cài đặt **Java JDK 17** trở lên.
2. Mở thư mục gốc của dự án bằng Terminal / Command Prompt.
3. Chạy lệnh sau để khởi động Server (chạy ở cổng 8080):
   ```bash
   mvn spring-boot:run
   ```
   *(Lưu ý: Bạn cũng có thể mở dự án bằng IntelliJ IDEA hoặc Eclipse và chạy class `BackendApplication.java`)*

### Bước 3: Chạy Frontend
1. Không cần cài đặt Node.js hay Build tool. Mọi thứ đã được đóng gói sẵn dạng tĩnh.
2. Mở thư mục `Frontend`.
3. Click đúp chuột để mở trực tiếp file `login.html` trên trình duyệt (Khuyến khích sử dụng tiện ích **Live Server** của VSCode để có trải nghiệm tốt nhất).

---

## 🔑 Tài Khoản Đăng Nhập Mẫu

Bạn có thể sử dụng các tài khoản sau để trải nghiệm trực tiếp hệ thống (Mật khẩu chung cho tất cả là: **`123456`**):

| Tên Đăng Nhập | Mật Khẩu | Vai Trò (Role) | Chức năng truy cập |
|---|---|---|---|
| `admin` | `123456` | Quản trị viên | Toàn quyền (Quản lý cư dân & Thu phí) |
| `ketoan` | `123456` | Kế toán | Chỉ được truy cập module Thu phí & Hóa đơn |
| `cudan01` | `123456` | Cư dân | Truy cập Cổng thông tin Cư dân (Hộ HK001) |
| `cudan02` | `123456` | Cư dân | Truy cập Cổng thông tin Cư dân (Hộ HK002) |

---
**© 2026 Nhóm 13 - Nhập môn Công nghệ Phần mềm**
