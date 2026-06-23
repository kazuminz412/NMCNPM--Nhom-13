const AUTH = (() => {
  const token = localStorage.getItem('token');
  const userRaw = localStorage.getItem('user');
  const user = userRaw ? JSON.parse(userRaw) : null;
  
  // Đổi mặc định fallback thành CU_DAN để an toàn tuyệt đối
  let role = (user?.role || localStorage.getItem('role') || 'CU_DAN').toUpperCase();
  if (role !== 'ADMIN' && role !== 'KE_TOAN') role = 'CU_DAN'; // Khóa chặt role hợp lệ

  function protect() {
    if (!token) {
      window.location.href = 'login.html';
      return false;
    }
    return true;
  }

  const MENU_CONFIG = {
    ADMIN: {
      sections: [
        {
          label: 'Tổng quan',
          items: [
            { href: 'dashboard.html', icon: '📊', label: 'Bảng điều khiển', key: 'dashboard' },
          ]
        },
        {
          label: 'Quản lý dân cư',
          items: [
            { href: 'ho-dan.html',      icon: '🏠', label: 'Hộ dân',       key: 'hodan' },
            { href: 'nhan-khau.html',   icon: '👥', label: 'Nhân khẩu',    key: 'nhankhau' },
            { href: 'tam-tru.html',     icon: '📝', label: 'Tạm trú / Tạm vắng', key: 'tamtru' },
            { href: 'phuong-tien.html', icon: '🚗', label: 'Phương tiện',  key: 'phuongtien' },
          ]
        },
        {
          label: 'Tài chính',
          items: [
            { href: 'danh-muc-phi.html', icon: '💲', label: 'Danh mục khoản phí', key: 'danhmucphi' },
            { href: 'hoa-don.html',      icon: '🧾', label: 'Hóa đơn',            key: 'hoadon' },
            { href: 'khoan-thu.html',    icon: '📋', label: 'Khoản thu',          key: 'khoanthu' },
            { href: 'thu-phi.html',      icon: '💰', label: 'Thu phí',            key: 'thuphi' },
          ]
        },
        {
          label: 'Hệ thống',
          items: [
            { href: 'phan-quyen.html', icon: '🛡️', label: 'Phân quyền', key: 'phanquyen' },
          ]
        },
      ]
    },

    KE_TOAN: {
      sections: [
        {
          label: 'Tổng quan',
          items: [
            { href: 'dashboard.html', icon: '📊', label: 'Bảng điều khiển', key: 'dashboard' },
          ]
        },
        {
          label: 'Tài chính',
          items: [
            { href: 'danh-muc-phi.html', icon: '💲', label: 'Danh mục khoản phí', key: 'danhmucphi' },
            { href: 'hoa-don.html',      icon: '🧾', label: 'Hóa đơn',            key: 'hoadon' },
            { href: 'khoan-thu.html',    icon: '📋', label: 'Khoản thu',          key: 'khoanthu' },
            { href: 'thu-phi.html',      icon: '💰', label: 'Thu phí',            key: 'thuphi' },
          ]
        },
      ]
    },

    CU_DAN: {
      sections: [
        {
          label: 'Góc cư dân',
          items: [
            { href: 'cu-dan-nha-toi.html',  icon: '🏠', label: 'Nhà của tôi',        key: 'nhatoi' },
            { href: 'cu-dan-hoa-don.html',  icon: '🧾', label: 'Hóa đơn', key: 'cudanhoadon' },
            { href: 'cu-dan-thanh-toan.html', icon: '💳', label: 'Thanh toán', key: 'cudanthanhtoan' },
            { href: 'cu-dan-lich-su.html',  icon: '📜', label: 'Lịch sử', key: 'cudanlichsu' }
          ]
        },
      ]
    },
  };

  function renderMenu(activeKey) {
    const aside = document.querySelector('aside');
    if (!aside) return;

    // Fallback an toàn nếu role bị lỗi trong localStorage
    const config = MENU_CONFIG[role] || MENU_CONFIG['CU_DAN'];
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

  // Kiểm tra xem activeKey hiện tại có nằm trong MENU_CONFIG của người dùng không
  function checkPermission(activeKey) {
    if (!activeKey) return true; // Nếu trang không truyền key thì cho qua
    
    const config = MENU_CONFIG[role] || MENU_CONFIG['CU_DAN'];
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

  // Chặn cửa gay gắt hơn
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
