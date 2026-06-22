# 🚀 Hướng Dẫn Cài Đặt

## 📋 Tiền Điều Kiện

Trước khi bắt đầu, đảm bảo bạn đã cài đặt:
- Git
- Node.js v14+ (hoặc Python 3.8+)
- MySQL 5.7+ (hoặc PostgreSQL)
- npm hoặc yarn

## 1️⃣ Clone Repository

```bash
# Clone từ GitHub
git clone https://github.com/kazuminz412/NMCNPM--Nh-m-13.git

# Di chuyển vào thư mục dự án
cd NMCNPM--Nh-m-13
```

## 2️⃣ Cấu Hình Biến Môi Trường

### Tạo file `.env` trong thư mục root:

```bash
cp .env.example .env
```

### Chỉnh sửa file `.env`:

```env
# === DATABASE ===
DB_HOST=localhost
DB_PORT=3306
DB_USER=root
DB_PASSWORD=your_password
DB_NAME=bluemoon_db

# === SERVER ===
NODE_ENV=development
PORT=5000
SERVER_URL=http://localhost:5000

# === CLIENT ===
REACT_APP_API_URL=http://localhost:5000

# === JWT ===
JWT_SECRET=your_super_secret_key_here
JWT_EXPIRE=7d

# === MAIL (Optional) ===
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USER=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

## 3️⃣ Cài Đặt Cơ Sở Dữ Liệu

### Option A: MySQL

```bash
# Đăng nhập MySQL
mysql -u root -p

# Tạo database
CREATE DATABASE bluemoon_db;
CREATE USER 'bluemoon_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON bluemoon_db.* TO 'bluemoon_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Option B: Sử dụng Docker

```bash
# Tạo container MySQL
docker run --name bluemoon_mysql \
  -e MYSQL_ROOT_PASSWORD=root_password \
  -e MYSQL_DATABASE=bluemoon_db \
  -e MYSQL_USER=bluemoon_user \
  -e MYSQL_PASSWORD=secure_password \
  -p 3306:3306 \
  -d mysql:8.0
```

## 4️⃣ Cài Đặt Backend

### Bước 1: Di chuyển vào thư mục backend
```bash
cd src/backend
```

### Bước 2: Cài đặt dependencies
```bash
npm install
# hoặc
yarn install
```

### Bước 3: Chạy database migrations
```bash
npm run migrate
# hoặc
npm run db:setup
```

### Bước 4: Seed dữ liệu mẫu (tùy chọn)
```bash
npm run seed
```

### Bước 5: Khởi động server
```bash
# Development mode
npm run dev

# Production mode
npm start
```

Server sẽ chạy trên: `http://localhost:5000`

## 5️⃣ Cài Đặt Frontend

### Bước 1: Di chuyển vào thư mục frontend (từ root)
```bash
cd src/frontend
```

### Bước 2: Cài đặt dependencies
```bash
npm install
# hoặc
yarn install
```

### Bước 3: Khởi động development server
```bash
npm start
# hoặc
yarn start
```

Frontend sẽ tự động mở tại: `http://localhost:3000`

## 6️⃣ Cài Đặt Với Docker (Tùy Chọn)

### Bước 1: Build Docker images
```bash
docker-compose build
```

### Bước 2: Khởi động containers
```bash
docker-compose up -d
```

### Bước 3: Chạy migrations
```bash
docker-compose exec backend npm run migrate
```

### Kiểm tra services
```bash
docker-compose ps
```

Truy cập ứng dụng tại:
- Frontend: `http://localhost:3000`
- Backend API: `http://localhost:5000`
- MySQL: `localhost:3306`

## 7️⃣ Xác Minh Cài Đặt

### Kiểm tra Backend
```bash
curl http://localhost:5000/api/health
# Kết quả: {"status": "OK"}
```

### Kiểm tra Database
```bash
mysql -u bluemoon_user -p bluemoon_db
SHOW TABLES;
EXIT;
```

### Kiểm tra Frontend
```bash
# Mở trình duyệt và truy cập:
http://localhost:3000
```

## 🔧 Troubleshooting

### Lỗi: "Port already in use"
```bash
# Tìm process sử dụng port
lsof -i :5000
# Hoặc Windows:
netstat -ano | findstr :5000

# Đổi port trong .env
PORT=5001
```

### Lỗi: "Cannot connect to database"
```bash
# Kiểm tra MySQL service
mysql -u root -p

# Kiểm tra thông tin kết nối trong .env
# DB_HOST, DB_USER, DB_PASSWORD
```

### Lỗi: "Dependencies not installed"
```bash
# Xóa node_modules và reinstall
rm -rf node_modules package-lock.json
npm install
```

### Lỗi: "CORS error"
```bash
# Kiểm tra CORS configuration trong backend
# Đảm bảo REACT_APP_API_URL trong .env khớp với SERVER_URL
```

## 📝 Lệnh Hữu Ích

### Backend
```bash
npm run dev        # Run development server
npm start          # Run production server
npm test           # Run tests
npm run lint       # Check code quality
npm run migrate    # Run database migrations
npm run seed       # Seed sample data
npm run build      # Build for production
```

### Frontend
```bash
npm start          # Run development server
npm test           # Run tests
npm run build      # Build for production
npm run lint       # Check code quality
npm run eject      # Eject from create-react-app (⚠️ không thể undo)
```

## 🎯 Bước Tiếp Theo

1. ✅ Đọc [USER_GUIDE.md](./USER_GUIDE.md) để hiểu cách sử dụng hệ thống
2. ✅ Xem [ARCHITECTURE.md](./ARCHITECTURE.md) để hiểu kiến trúc
3. ✅ Đọc [docs/database_design.md](./docs/database_design.md) để tìm hiểu CSDL
4. ✅ Kiểm tra [docs/api_documentation.md](./docs/api_documentation.md) cho API details

## 📞 Cần Giúp Đỡ?

- Tạo issue trên GitHub
- Liên hệ team development
- Kiểm tra FAQ section

---

**Cập nhật lần cuối:** 2026-05-22
