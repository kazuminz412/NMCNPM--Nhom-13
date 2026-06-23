const API = 'http://localhost:8080/api';

// Lấy token từ LocalStorage
const token = localStorage.getItem('token');

// Bảo vệ Route: Nếu chưa đăng nhập mà cố vào các trang nội bộ thì đuổi về login
if (!window.location.pathname.includes('login.html') && !token) {
    window.location.href = 'login.html';
}

//  Hàm đăng xuất dùng chung cho mọi trang
function handleLogout() {
    if (confirm('Bạn có chắc muốn đăng xuất?')) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = 'login.html';
    }
}

// Hàm reset lỗi UI dùng chung khi đóng Modal
function clearFormErrors() {
    document.querySelectorAll('.error').forEach(e => e.classList.remove('error'));
    document.querySelectorAll('.show-error').forEach(e => e.classList.remove('show-error'));
}