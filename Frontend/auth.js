// Đổi mặc định fallback thành CU_DAN để an toàn tuyệt đối
  const role = user?.role || localStorage.getItem('role') || 'CU_DAN';

  // ... (Giữ nguyên cụm MENU_CONFIG và hàm protect() như cũ) ...

  function renderMenu(activeKey) {
    const aside = document.querySelector('aside');
    if (!aside) return;

    const config = MENU_CONFIG[role];
    let html = '';

    config.sections.forEach((section, idx) => {
      if (idx > 0) html += '<div class="ndiv"></div>';
      html += `<div class="nav-sec"><span class="nav-lbl">${section.label}</span>`;
      section.items.forEach(item => {
        const isActive = item.key === activeKey;
        const badge = item.badge
          ? `<span class="nbadge">${item.badge}</span>`
          : item.badgeDanger
          ? `<span class="nbadge red">${item.badgeDanger}</span>`
          : '';
        html += `<a href="${item.href}" class="nav-a${isActive ? ' active' : ''}">${item.icon} ${item.label} ${badge}</a>`;
      });
      html += '</div>';
    });

    html += `<div class="sidebar-foot"><button class="logout" onclick="AUTH.logout()">🚪 Đăng xuất</button></div>`;
    aside.innerHTML = html;
  }

  function renderUser() {
    const nameEl    = document.querySelector('.uname');
    const roleEl    = document.querySelector('.urole');
    const avatarEl  = document.querySelector('.uavatar');
    if (!user) return;

    const roleLabel = { ADMIN: 'Quản trị viên', KE_TOAN: 'Kế toán', CU_DAN: 'Cư dân' };
    if (nameEl)   nameEl.textContent   = user.hoTen || user.username || 'Người dùng';
    if (roleEl)   roleEl.textContent   = roleLabel[role] || role;
    if (avatarEl) avatarEl.textContent = (user.hoTen || user.username || 'U')[0].toUpperCase();
  }

  // HÀM MỚI BỔ SUNG: Kiểm tra xem activeKey hiện tại có nằm trong MENU_CONFIG của người dùng không
  function checkPermission(activeKey) {
    if (!activeKey) return true; // Nếu trang không truyền key (VD: trang thông báo chung) thì cho qua
    
    const config = MENU_CONFIG[role];
    let isAllowed = false;
    
    // Duyệt qua menu của role hiện tại, nếu thấy key thì tức là được phép vào
    config.sections.forEach(section => {
      section.items.forEach(item => {
        if (item.key === activeKey) isAllowed = true;
      });
    });
    
    return isAllowed;
  }

  function logout() {
    if (confirm('Bạn có chắc muốn đăng xuất?')) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('role');
      window.location.href = 'login.html';
    }
  }

  // NÂNG CẤP HÀM INIT: Chặn cửa gay gắt hơn
  function init(activeKey = '') {
    if (!protect()) return; // Chặn nếu chưa có Token
    
    // Đá văng ra ngoài nếu cố tình vào trang không có quyền
    if (!checkPermission(activeKey)) {
        alert('Bạn không có quyền truy cập chức năng này!');
        // Trả về đúng trang chủ của từng nhóm quyền
        window.location.href = role === 'CU_DAN' ? 'cu-dan-nha-toi.html' : 'dashboard.html';
        return;
    }

    renderMenu(activeKey);
    renderUser();
  }

  return { init, protect, logout, role, user, token };
})();
