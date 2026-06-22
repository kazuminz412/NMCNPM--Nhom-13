# 🌳 Chiến Lược Phân Nhánh (Git Flow)

## 📋 Tổng Quan

Dự án sử dụng **Git Flow** - một mô hình phân nhánh mạnh mẽ cho phép nhiều người làm việc song song mà không xung đột.

```
┌─────────────────────────────────────────────────────────────┐
│                       main (Master)                         │
│            ⚠️ Production-ready code only                    │
│          🔒 Nhóm trưởng quyền merge duy nhất              │
└────────────────┬────────────────────────────────────────────┘
                 │
                 │ (Merge từ develop)
                 │
┌────────────────▼────────────────────────────────────────────┐
│                      develop                                │
│                 🏗️ Integration branch                       │
│          ✅ Testing & Quality Assurance                     │
│         👨‍💼 Scrum Master/Team Lead merge code               │
└────────────────┬────────────────────────────────────────────┘
       ▲         │         ▲         │         ▲
       │         │         │         │         │
    Merge    Merge    Merge    Merge    Merge
    from     from     from     from     from
       │         │         │         │         │
┌──────┴──────┐  │  ┌──────┴──────┐  │  ┌──────┴──────┐
│ feature/... │  │  │ feature/... │  │  │ feature/... │
│ (Frontend)  │  │  │ (Backend)   │  │  │ (Database)  │
└─────────────┘  │  └─────────────┘  │  └─────────────┘
                 │                    │
          (Các developer làm trên feature branch)
```

---

## 🎯 Các Nhánh Chính

### 1. 🔴 **main** (Master Branch)
**Tên:** `main`

**Đặc Điểm:**
- ✅ Code đã hoàn hảo, sẵn sàng nộp
- ✅ Đã kiểm tra kỹ lưỡng
- ✅ Không có bug/lỗi

**Ai được merge vào:**
- 🔐 **Chỉ Nhóm Trưởng** (Project Owner)

**Quy Tắc:**
- Không ai commit trực tiếp vào main
- Chỉ nhận merge từ develop sau khi kiểm tra
- Mỗi merge tạo một release tag

**Ví dụ tag:**
```bash
v1.0.0 - Release đầu tiên
v1.0.1 - Patch fix
v1.1.0 - Minor update
v2.0.0 - Major update
```

---

### 2. 🟡 **develop** (Integration Branch)
**Tên:** `develop`

**Đặc Điểm:**
- 🏗️ Nhánh "công trường"
- 📦 Tích hợp tất cả tính năng
- ✅ Đã pass kiểm tra của Tester
- 🔄 Luôn trong trạng thái stable

**Ai được merge vào:**
- 👨‍💼 **Scrum Master / Team Lead**
- 🧪 Sau khi Tester kiểm tra xong

**Quy Tắc:**
- Không commit trực tiếp (chỉ merge từ feature)
- Phải đi qua Pull Request (PR) review
- Tất cả test phải pass

---

### 3. 🟢 **feature/** (Feature Branches)
**Tên:** `feature/tên-tính-năng`

**Đặc Điểm:**
- 👤 Nhánh riêng của từng developer
- 🎯 Để làm một tính năng cụ thể
- 🔀 Branch từ `develop`

**Quy Tắc Đặt Tên:**

#### Frontend
```
feature/login-ui
feature/hienthi-hodan
feature/dashboard-chart
feature/resident-list
feature/fee-calculator
feature/fee-report
```

#### Backend
```
feature/login-api
feature/crud-hodan
feature/fee-calculation-service
feature/payment-processing
feature/authentication-middleware
feature/report-generator
```

#### Database
```
database/tao-bang-hodan
database/tao-bang-fee
database/tao-bang-payment
database/seed-data
database/migration-v1.1
```

**Quy Trình:**
```bash
# 1. Cập nhật develop mới nhất
git checkout develop
git pull origin develop

# 2. Tạo feature branch
git checkout -b feature/ten-tinh-nang

# 3. Làm công việc trên branch này
# ... code ...
git add .
git commit -m "feat: mô tả tính năng"

# 4. Push lên GitHub
git push origin feature/ten-tinh-nang

# 5. Tạo Pull Request (PR)
# - Trên GitHub, chọn "Compare & pull request"
# - Set base branch: develop
# - Set compare branch: feature/ten-tinh-nang
# - Viết mô tả chi tiết

# 6. Chờ review từ team lead
# 7. Sau khi approve, merge vào develop
# 8. Xóa feature branch
```

---

### 4. 🔵 **bugfix/** (Bug Fix Branches)
**Tên:** `bugfix/ten-bug`

**Đặc Điểm:**
- 🐛 Dùng để sửa bug trên develop
- 🔀 Branch từ `develop`
- 📝 Giống feature branch nhưng cho bug fix

**Ví dụ:**
```
bugfix/fix-login-error
bugfix/database-connection-timeout
bugfix/calculation-rounding-error
bugfix/cors-error
```

---

### 5. 🔶 **hotfix/** (Hotfix Branches)
**Tên:** `hotfix/ten-hotfix`

**Đặc Điểm:**
- 🚨 Sửa bug khẩn cấp trên production (main)
- 🔀 Branch từ `main`
- ⏰ Ưu tiên cao nhất

**Quy Trình:**
```bash
# 1. Tạo hotfix branch từ main
git checkout main
git pull origin main
git checkout -b hotfix/ten-hotfix

# 2. Sửa bug
# ... code ...
git commit -m "hotfix: mô tả bug"

# 3. Merge vào main
git checkout main
git merge hotfix/ten-hotfix

# 4. Merge vào develop để đồng bộ
git checkout develop
git merge hotfix/ten-hotfix

# 5. Xóa hotfix branch
git branch -d hotfix/ten-hotfix
```

---

## 📊 Quy Trình Làm Việc Chi Tiết

### 📝 Bước 1: Lấy Task từ Project Manager

```
Task: Tạo trang Login UI
- Người assign: Bạn Frontend
- Loại: Feature
- Deadline: 2 ngày
```

### 🌿 Bước 2: Tạo Feature Branch

```bash
# Cập nhật develop mới nhất
git checkout develop
git pull origin develop

# Tạo feature branch từ develop
git checkout -b feature/login-ui

# Kiểm tra bạn đang ở nhánh nào
git branch
# * feature/login-ui
#   develop
#   main
```

### 💻 Bước 3: Làm Việc trên Feature Branch

```bash
# Sửa/thêm file
# Ví dụ: src/frontend/pages/LoginPage.js

# Commit thay đổi
git add .
git commit -m "feat(frontend): create login UI page"
git commit -m "feat(frontend): add form validation"
git commit -m "feat(frontend): add error handling"

# Push lên GitHub
git push origin feature/login-ui
```

### 🔄 Bước 4: Tạo Pull Request (PR)

1. Truy cập GitHub repository
2. Nhấn nút **"Compare & pull request"**
3. Điền thông tin:

```markdown
## 📋 Mô Tả
Tạo trang login UI với form đăng nhập, xác thực input, hiển thị lỗi

## 🔗 Liên Kết Issues
Closes #123

## ✅ Checklist
- [x] Code viết sạch, tuân thủ style guide
- [x] Thêm comments cho complex logic
- [x] Kiểm tra trên nhiều trình duyệt
- [x] Kiểm tra responsive design
- [x] Không có console errors/warnings

## 📸 Screenshots
[Attach screenshots of the login page]
```

4. Set:
   - **Base branch:** `develop`
   - **Compare branch:** `feature/login-ui`

5. Nhấn **"Create pull request"**

### 👀 Bước 5: Team Lead Review Code

Team Lead sẽ:
- ✅ Review code
- ✅ Kiểm tra logic
- ✅ Comment nếu cần sửa
- ✅ Approve hoặc request changes

Nếu cần sửa:
```bash
# Sửa theo comment
# ... code ...
git add .
git commit -m "refactor: update based on review"
git push origin feature/login-ui
# PR sẽ tự động cập nhật
```

### ✅ Bước 6: Merge vào Develop

Sau khi approve:
1. **Team Lead** sẽ nhấn **"Merge pull request"**
2. Code tự động merge vào develop
3. **Xóa feature branch**

```bash
# Sau merge, xóa local branch
git checkout develop
git pull origin develop
git branch -d feature/login-ui
```

### 🧪 Bước 7: Tester Kiểm Tra

- 🧪 Tester test tính năng trên develop
- ✅ Nếu OK: Sẵn sàng merge vào main
- ❌ Nếu có bug: Tạo bugfix branch

### 📦 Bước 8: Release lên Main

Khi tất cả features đã test OK:

```bash
# Nhóm trưởng merge develop vào main
git checkout main
git pull origin main
git merge develop
git tag v1.0.0
git push origin main
git push origin --tags
```

---

## 👥 Vai Trò và Quyền Hạn

| Vai Trò | main | develop | feature | Review PR |
|---------|------|---------|---------|-----------|
| **Nhóm Trưởng** | ✅ Merge | ✅ Merge | ❌ | ✅ |
| **Team Lead** | ❌ | ✅ Merge | ✅ Create | ✅ Approve |
| **Developer** | ❌ | ❌ | ✅ Create | ⚠️ Review |
| **Tester** | ❌ | ❌ | ❌ | ⚠️ Feedback |

---

## 📋 Quy Tắc Commit

### Format Commit Message

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types
```
feat      - Tính năng mới
fix       - Sửa bug
refactor  - Cấu trúc lại code (không thay đổi logic)
style     - Thay đổi format code (không thay đổi logic)
test      - Thêm test
docs      - Cập nhật tài liệu
perf      - Cải thiện performance
chore     - Thay đổi build, dependencies, ...
```

### Scopes (Ví dụ)
```
frontend
backend
database
config
docs
```

### Examples
```
feat(frontend): add login page UI
fix(backend): resolve JWT token validation error
refactor(database): optimize user query
docs(readme): add installation instructions
test(backend): add fee calculation tests
```

---

## 🔄 Ví Dụ Workflow Thực Tế

### Scenario: 3 người làm 3 tính năng song song

**Frontend Dev - Tạo trang Dashboard:**
```bash
git checkout -b feature/dashboard-ui
# ... code ...
git push origin feature/dashboard-ui
# Tạo PR merge vào develop
```

**Backend Dev - Tạo API Fee Calculation:**
```bash
git checkout -b feature/fee-calculation-api
# ... code ...
git push origin feature/fee-calculation-api
# Tạo PR merge vào develop
```

**Database Dev - Tạo bảng Fee:**
```bash
git checkout -b database/tao-bang-fee
# ... code ...
git push origin database/tao-bang-fee
# Tạo PR merge vào develop
```

**Timeline:**
```
Time 1: Dashboard PR đã merge ✅
Time 2: Fee Calculation API PR đã merge ✅
Time 3: Database Fee table PR đã merge ✅
        → Tất cả merge vào develop
        → Tester test tính năng tích hợp
        → Bug được tìm thấy & fix
        → Tất cả PR được approve
        → Merge develop vào main
        → Release v1.0.0 ✅
```

---

## ⚠️ Các Lỗi Thường Gặp

### 1. ❌ Commit trực tiếp vào develop
```bash
# WRONG ❌
git checkout develop
git add .
git commit -m "..."
git push origin develop

# RIGHT ✅
git checkout -b feature/ten-tinh-nang
git add .
git commit -m "..."
git push origin feature/ten-tinh-nang
# Tạo PR
```

### 2. ❌ Quên pull develop trước tạo feature
```bash
# WRONG ❌
git checkout -b feature/login

# RIGHT ✅
git checkout develop
git pull origin develop
git checkout -b feature/login
```

### 3. ❌ Merge conflicts
```bash
# Nếu có conflict khi merge
# GitHub sẽ show: "This branch has conflicts"

# Fix local
git checkout feature/login-ui
git merge develop
# ... resolve conflicts ...
git add .
git commit -m "Merge develop into feature/login-ui"
git push origin feature/login-ui
```

### 4. ❌ Xóa branch sai
```bash
# Nếu vô tình xóa branch
git reflog  # Xem history

# Recover branch
git checkout -b feature/login-ui <commit-sha>
```

---

## 📚 Tài Liệu Tham Khảo

- [A successful Git branching model](https://nvie.com/posts/a-successful-git-branching-model/)
- [GitHub Flow](https://guides.github.com/introduction/flow/)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

**Cập nhật lần cuối:** 2026-05-22
