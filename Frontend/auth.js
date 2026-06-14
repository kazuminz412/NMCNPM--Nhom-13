
const AUTH = (() => {
  const token = localStorage.getItem('token');
  const userRaw = localStorage.getItem('user');
  const user = userRaw ? JSON.parse(userRaw) : null;
  const role = user?.role || localStorage.getItem('role') || 'ADMIN';
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
            { href: 'ho-dan.html',      icon: '🏠', label: 'Hộ dân',       key: 'hodan',      badge: '248' },
            { href: 'nhan-khau.html',   icon: '👥', label: 'Nhân khẩu',    key: 'nhankhau' },
            { href: 'phuong-tien.html', icon: '🚗', label: 'Phương tiện',  key: 'phuongtien' },
          ]
        },
        {
          label: 'Tài chính',
          items: [
            { href: 'danh-muc-phi.html', icon: '💲', label: 'Danh mục khoản phí', key: 'danhmucphi' },
            { href: 'hoa-don.html',      icon: '🧾', label: 'Hóa đơn',            key: 'hoadon' },
            { href: 'thanh-toan.html',   icon: '💳', label: 'Thanh toán',          key: 'thanhtoan' },
            { href: 'khoan-thu.html',    icon: '📋', label: 'Khoản thu',           key: 'khoanthu' },
            { href: 'thu-phi.html',      icon: '💰', label: 'Thu phí',             key: 'thuphi', badgeDanger: '5' },
            { href: 'lich-su.html',      icon: '📜', label: 'Lịch sử giao dịch',  key: 'lichsu' },
          ]
        },
        {
          label: 'Báo cáo',
          items: [
            { href: 'thong-ke.html', icon: '📈', label: 'Thống kê', key: 'thongke' },
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
          label: 'Tài chính',
          items: [
            { href: 'danh-muc-phi.html', icon: '💲', label: 'Danh mục khoản phí', key: 'danhmucphi' },
            { href: 'hoa-don.html',      icon: '🧾', label: 'Hóa đơn',            key: 'hoadon' },
            { href: 'thanh-toan.html',   icon: '💳', label: 'Thanh toán',          key: 'thanhtoan' },
            { href: 'khoan-thu.html',    icon: '📋', label: 'Khoản thu',           key: 'khoanthu' },
            { href: 'thu-phi.html',      icon: '💰', label: 'Thu phí',             key: 'thuphi', badgeDanger: '5' },
            { href: 'lich-su.html',      icon: '📜', label: 'Lịch sử giao dịch',  key: 'lichsu' },
          ]
        },
        {
          label: 'Báo cáo',
          items: [
            { href: 'thong-ke.html', icon: '📈', label: 'Thống kê', key: 'thongke' },
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
            { href: 'cu-dan-hoa-don.html',  icon: '🧾', label: 'Hóa đơn & Thanh toán', key: 'cudanhoadon' },
          ]
        },
      ]
    },
  };

  function renderMenu(activeKey) {
    const aside = document.querySelector('aside');
    if (!aside) return;

    const config = MENU_CONFIG[role] || MENU_CONFIG['ADMIN'];
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

    // Logout
    html += `<div class="sidebar-foot"><button class="logout" onclick="AUTH.logout()">🚪 Đăng xuất</button></div>`;
    aside.innerHTML = html;
  }
  function renderUser() {
    const nameEl    = document.querySelector('.uname');
    const roleEl    = document.querySelector('.urole');
    const avatarEl  = document.querySelector('.uavatar');
    if (!user) return;

    const roleLabel = { ADMIN: 'Quản trị viên', KE_TOAN: 'Kế toán', CU_DAN: 'Cư dân' };
    if (nameEl)   nameEl.textContent   = user.hoTen || user.username || 'Admin';
    if (roleEl)   roleEl.textContent   = roleLabel[role] || role;
    if (avatarEl) avatarEl.textContent = (user.hoTen || user.username || 'A')[0].toUpperCase();
  }
  function logout() {
    if (confirm('Bạn có chắc muốn đăng xuất?')) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      localStorage.removeItem('role');
      window.location.href = 'login.html';
    }
  }

  function init(activeKey = '') {
    if (!protect()) return;
    renderMenu(activeKey);
    renderUser();
  }

  return { init, protect, logout, role, user, token };
})();
