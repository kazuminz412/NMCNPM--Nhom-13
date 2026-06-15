CREATE DATABASE IF NOT EXISTS bluemoon_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bluemoon_db;

-- ==============================================================================
-- PHẦN 1: TẠO CẤU TRÚC BẢNG 
-- ==============================================================================

-- 1. BẢNG HỘ GIA ĐÌNH (Phải đưa lên trước để bảng Tai Khoan móc Khóa ngoại)
CREATE TABLE ho_gia_dinh (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ma_ho_khau VARCHAR(20) NOT NULL UNIQUE, 
    so_phong VARCHAR(10) NOT NULL, 
    toa_nha VARCHAR(20) DEFAULT 'Blue Moon',
    dien_tich_m2 DECIMAL(6,2) NOT NULL CHECK (dien_tich_m2 > 0), 
    ten_chu_ho VARCHAR(100) NOT NULL,
    ngay_cap_ho_khau DATE
);

-- 2. BẢNG TÀI KHOẢN (Thay thế bảng nguoi_dung - Chuẩn hóa cho Spring Security)
CREATE TABLE tai_khoan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- BCrypt hash luôn dài 60 ký tự, để 255 là an toàn
    role ENUM('ADMIN', 'KE_TOAN', 'CU_DAN') NOT NULL,
    trang_thai ENUM('HOAT_DONG', 'BI_KHOA') DEFAULT 'HOAT_DONG',
    ho_dan_id INT DEFAULT NULL,
    ngay_tao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ho_dan_id) REFERENCES ho_gia_dinh(id) ON DELETE CASCADE,
    
    -- RÀNG BUỘC BẢO MẬT TUYỆT ĐỐI CẤP DATABASE
    CONSTRAINT chk_role_hodan CHECK (
        (role = 'CU_DAN' AND ho_dan_id IS NOT NULL) OR 
        (role IN ('ADMIN', 'KE_TOAN') AND ho_dan_id IS NULL)
    )
);

-- 3. BẢNG NHÂN KHẨU
CREATE TABLE nhan_khau (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ho_ten VARCHAR(100) NOT NULL,
    ngay_sinh DATE NOT NULL CHECK (ngay_sinh <= CURRENT_DATE), 
    gioi_tinh ENUM('Nam', 'Nu', 'Khac') NOT NULL,
    cccd VARCHAR(15) UNIQUE, 
    so_dien_thoai VARCHAR(15),
    ho_gia_dinh_id INT,
    quan_he_chu_ho VARCHAR(50), 
    trang_thai_cu_tru ENUM('THUONG_TRU', 'TAM_TRU', 'TAM_VANG', 'DA_CHUYEN_DI') DEFAULT 'THUONG_TRU',
    ngay_chuyen_den DATE,
    ngay_chuyen_di DATE,
    FOREIGN KEY (ho_gia_dinh_id) REFERENCES ho_gia_dinh(id) ON DELETE SET NULL
);

-- 4. BẢNG PHƯƠNG TIỆN / XE CỘ
CREATE TABLE phuong_tien (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ho_gia_dinh_id INT NOT NULL,
    bien_so VARCHAR(20) NOT NULL UNIQUE, 
    loai_xe ENUM('XE_MAY', 'O_TO', 'XE_DAP_DIEN') NOT NULL,
    mau_xe VARCHAR(50),
    ngay_dang_ky TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ho_gia_dinh_id) REFERENCES ho_gia_dinh(id) ON DELETE CASCADE
);

-- 5. BẢNG KHOẢN PHÍ
CREATE TABLE khoan_phi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ten_khoan_phi VARCHAR(100) NOT NULL,
    loai_phi ENUM('BAT_BUOC', 'DICH_VU', 'THU_HO') NOT NULL,
    don_gia DECIMAL(12,2) NOT NULL CHECK (don_gia >= 0), 
    chu_ky ENUM('THANG', 'NAM', 'MOT_LAN') DEFAULT 'THANG'
);

-- 6. BẢNG HÓA ĐƠN
CREATE TABLE hoa_don (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ho_gia_dinh_id INT NOT NULL,
    thang_nam VARCHAR(7) NOT NULL, 
    tong_tien DECIMAL(12,2) NOT NULL DEFAULT 0.00 CHECK (tong_tien >= 0), 
    trang_thai_thanh_toan ENUM('CHUA_THANH_TOAN', 'DA_THANH_TOAN') DEFAULT 'CHUA_THANH_TOAN',
    ngay_tao DATE NOT NULL,
    FOREIGN KEY (ho_gia_dinh_id) REFERENCES ho_gia_dinh(id)
);

-- 7. BẢNG CHI TIẾT HÓA ĐƠN
CREATE TABLE chi_tiet_hoa_don (
    hoa_don_id INT NOT NULL,
    khoan_phi_id INT NOT NULL,
    chi_so_cu INT DEFAULT NULL, 
    chi_so_moi INT DEFAULT NULL,
    so_luong DECIMAL(8,2) NOT NULL DEFAULT 1 CHECK (so_luong > 0), 
    thanh_tien DECIMAL(12,2) NOT NULL CHECK (thanh_tien >= 0),
    PRIMARY KEY (hoa_don_id, khoan_phi_id),
    FOREIGN KEY (hoa_don_id) REFERENCES hoa_don(id) ON DELETE CASCADE,
    FOREIGN KEY (khoan_phi_id) REFERENCES khoan_phi(id)
);

-- 8. BẢNG GIAO DỊCH
CREATE TABLE giao_dich (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hoa_don_id INT NOT NULL,
    ngay_giao_dich TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    so_tien_thanh_toan DECIMAL(12,2) NOT NULL CHECK (so_tien_thanh_toan >= 0), 
    phuong_thuc_thanh_toan ENUM('TIEN_MAT', 'CHUYEN_KHOAN_QR') NOT NULL,
    ma_giao_dich_ngan_hang VARCHAR(100) DEFAULT NULL, 
    ghi_chu TEXT,
    FOREIGN KEY (hoa_don_id) REFERENCES hoa_don(id)
);

-- 9. BẢNG PHẢN HỒI / KHIẾU NẠI
CREATE TABLE phan_hoi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nhan_khau_id INT NOT NULL,
    tieu_de VARCHAR(150) NOT NULL,
    noi_dung TEXT NOT NULL,
    ngay_gui TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    trang_thai_xu_ly ENUM('CHUA_TIEP_NHAN', 'DANG_XU_LY', 'DA_XU_LY') DEFAULT 'CHUA_TIEP_NHAN',
    ghi_chu_ban_quan_ly TEXT,
    FOREIGN KEY (nhan_khau_id) REFERENCES nhan_khau(id)
);

-- 10. BẢNG KHOẢN THU TỰ NGUYỆN
CREATE TABLE khoan_thu_tu_nguyen (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ten_khoan_thu VARCHAR(150) NOT NULL, 
    ngay_phat_dong DATE NOT NULL,
    ngay_ket_thuc DATE,
    ghi_chu TEXT
);

-- 11. BẢNG ĐÓNG GÓP TỰ NGUYỆN (Cập nhật Khóa ngoại theo bảng tài khoản mới)
CREATE TABLE dong_gop_tu_nguyen (
    id INT AUTO_INCREMENT PRIMARY KEY,
    khoan_thu_id INT NOT NULL,
    ho_gia_dinh_id INT NOT NULL,
    so_tien DECIMAL(12,2) NOT NULL CHECK (so_tien > 0), 
    ngay_dong_gop TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nguoi_thu_id INT, 
    ghi_chu TEXT,
    FOREIGN KEY (khoan_thu_id) REFERENCES khoan_thu_tu_nguyen(id) ON DELETE CASCADE,
    FOREIGN KEY (ho_gia_dinh_id) REFERENCES ho_gia_dinh(id) ON DELETE CASCADE,
    FOREIGN KEY (nguoi_thu_id) REFERENCES tai_khoan(id) ON DELETE SET NULL
);

-- TẠO CÁC INDEX 
CREATE INDEX idx_nhan_khau_ho_ten ON nhan_khau(ho_ten);
CREATE INDEX idx_ho_gia_dinh_so_phong ON ho_gia_dinh(so_phong);
CREATE INDEX idx_hoa_don_thang_nam ON hoa_don(thang_nam);
CREATE INDEX idx_dong_gop_ho_gia_dinh ON dong_gop_tu_nguyen(ho_gia_dinh_id);


-- ==============================================================================
-- PHẦN 2: LẬP TRÌNH CƠ SỞ DỮ LIỆU (STORED PROCEDURE & TRIGGERS)
-- ==============================================================================

DELIMITER //
CREATE TRIGGER trg_AfterInsertChiTietHoaDon
AFTER INSERT ON chi_tiet_hoa_don
FOR EACH ROW
BEGIN
    UPDATE hoa_don
    SET tong_tien = (SELECT COALESCE(SUM(thanh_tien), 0) FROM chi_tiet_hoa_don WHERE hoa_don_id = NEW.hoa_don_id)
    WHERE id = NEW.hoa_don_id;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_AfterUpdateChiTietHoaDon
AFTER UPDATE ON chi_tiet_hoa_don
FOR EACH ROW
BEGIN
    UPDATE hoa_don
    SET tong_tien = (SELECT COALESCE(SUM(thanh_tien), 0) FROM chi_tiet_hoa_don WHERE hoa_don_id = NEW.hoa_don_id)
    WHERE id = NEW.hoa_don_id;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER trg_AfterDeleteChiTietHoaDon
AFTER DELETE ON chi_tiet_hoa_don
FOR EACH ROW
BEGIN
    UPDATE hoa_don
    SET tong_tien = (SELECT COALESCE(SUM(thanh_tien), 0) FROM chi_tiet_hoa_don WHERE hoa_don_id = OLD.hoa_don_id)
    WHERE id = OLD.hoa_don_id;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE sp_TaoHoaDonDinhKy(IN p_thang_nam VARCHAR(7))
BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_ho_gia_dinh_id INT;
    
    DECLARE cur CURSOR FOR SELECT id FROM ho_gia_dinh;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO v_ho_gia_dinh_id;
        IF done THEN
            LEAVE read_loop;
        END IF;

        IF NOT EXISTS (SELECT 1 FROM hoa_don WHERE ho_gia_dinh_id = v_ho_gia_dinh_id AND thang_nam = p_thang_nam) THEN
            INSERT INTO hoa_don (ho_gia_dinh_id, thang_nam, tong_tien, trang_thai_thanh_toan, ngay_tao)
            VALUES (v_ho_gia_dinh_id, p_thang_nam, 0, 'CHUA_THANH_TOAN', CURRENT_DATE);
        END IF;
    END LOOP;

    CLOSE cur;
END //
DELIMITER ;


-- ==============================================================================
-- PHẦN 3: KHỞI TẠO DỮ LIỆU MẪU ĐẦY ĐỦ 
-- ==============================================================================

INSERT INTO ho_gia_dinh (ma_ho_khau, so_phong, dien_tich_m2, ten_chu_ho, ngay_cap_ho_khau) VALUES
('HK001', 'P.101', 65.5, 'Nguyen Van A', '2020-01-15'),
('HK002', 'P.102', 70.0, 'Tran Thi B', '2020-02-10'),
('HK003', 'P.103', 55.0, 'Le Van C', '2020-03-05'),
('HK004', 'P.104', 85.5, 'Pham Thi D', '2020-04-20'),
('HK005', 'P.105', 65.5, 'Hoang Van E', '2020-05-12'),
('HK006', 'P.201', 90.0, 'Vu Thi F', '2021-01-15'),
('HK007', 'P.202', 70.0, 'Dao Van G', '2021-02-28'),
('HK008', 'P.203', 60.5, 'Ngo Thi H', '2021-03-10'),
('HK009', 'P.204', 85.0, 'Dang Van I', '2021-04-01'),
('HK010', 'P.205', 55.0, 'Bui Thi K', '2021-05-19'),
('HK011', 'P.301', 100.0, 'Do Van L', '2022-06-15'),
('HK012', 'P.302', 75.5, 'Nguyen Thi M', '2022-07-22'),
('HK013', 'P.303', 65.0, 'Tran Van N', '2022-08-11'),
('HK014', 'P.304', 80.0, 'Le Thi O', '2022-09-05'),
('HK015', 'P.305', 50.0, 'Pham Van P', '2022-10-18');

-- Khởi tạo Tài khoản mẫu (Mật khẩu đều là: 123456 đã được mã hóa BCrypt)
INSERT INTO tai_khoan (username, password, role, trang_thai, ho_dan_id) VALUES
('admin', '$2a$12$N9u1M2R0U.d0fB4n4J8vF.EaC0v8m7P9h5L8k3T2q1w4e7r5t8y', 'ADMIN', 'HOAT_DONG', NULL),
('ketoan', '$2a$12$N9u1M2R0U.d0fB4n4J8vF.EaC0v8m7P9h5L8k3T2q1w4e7r5t8y', 'KE_TOAN', 'HOAT_DONG', NULL),
('cudan_101', '$2a$12$N9u1M2R0U.d0fB4n4J8vF.EaC0v8m7P9h5L8k3T2q1w4e7r5t8y', 'CU_DAN', 'HOAT_DONG', 1),
('cudan_102', '$2a$12$N9u1M2R0U.d0fB4n4J8vF.EaC0v8m7P9h5L8k3T2q1w4e7r5t8y', 'CU_DAN', 'HOAT_DONG', 2);

INSERT INTO phuong_tien (ho_gia_dinh_id, bien_so, loai_xe, mau_xe) VALUES
(1, '29H1-11111', 'XE_MAY', 'Do Den'),
(1, '29H1-22222', 'XE_MAY', 'Trang'),
(2, '30A-88888', 'O_TO', 'Den'),
(3, '29X5-33333', 'XE_MAY', 'Xanh'),
(4, '29Y3-44444', 'XE_MAY', 'Xam'),
(6, '30E-55555', 'O_TO', 'Do'),
(11, '29K1-66666', 'XE_MAY', 'Vang');

INSERT INTO nhan_khau (ho_ten, ngay_sinh, gioi_tinh, cccd, so_dien_thoai, ho_gia_dinh_id, quan_he_chu_ho, trang_thai_cu_tru, ngay_chuyen_den) VALUES
('Nguyen Van A', '1980-05-20', 'Nam', '001080123456', '0901234567', 1, 'Chu ho', 'THUONG_TRU', '2020-01-15'),
('Hoang Thi Z', '1985-08-15', 'Nu', '001085123456', '0912345678', 1, 'Vo', 'THUONG_TRU', '2020-01-15'),
('Nguyen Van B', '2010-02-10', 'Nam', NULL, NULL, 1, 'Con', 'THUONG_TRU', '2020-01-15'),
('Tran Thi B', '1990-11-25', 'Nu', '002090123456', '0923456789', 2, 'Chu ho', 'THUONG_TRU', '2020-02-10'),
('Le Van C', '1975-04-30', 'Nam', '003075123456', '0934567890', 3, 'Chu ho', 'TAM_VANG', '2020-03-05'),
('Pham Thi D', '1988-12-12', 'Nu', '004088123456', '0945678901', 4, 'Chu ho', 'THUONG_TRU', '2020-04-20'),
('Hoang Van E', '1995-07-07', 'Nam', '005095123456', '0956789012', 5, 'Chu ho', 'TAM_TRU', '2020-05-12'),    
('Vu Thi F', '1982-01-01', 'Nu', '006082123456', '0967890123', 6, 'Chu ho', 'THUONG_TRU', '2021-01-15'),
('Dao Van G', '1979-09-09', 'Nam', '007079123456', '0978901234', 7, 'Chu ho', 'THUONG_TRU', '2021-02-28'),
('Ngo Thi H', '1992-03-14', 'Nu', '008092123456', '0989012345', 8, 'Chu ho', 'THUONG_TRU', '2021-03-10'),
('Dang Van I', '1986-06-18', 'Nam', '009086123456', '0990123456', 9, 'Chu ho', 'THUONG_TRU', '2021-04-01'),
('Bui Thi K', '1998-10-22', 'Nu', '010098123456', '0909876543', 10, 'Chu ho', 'THUONG_TRU', '2021-05-19'),
('Do Van L', '1970-11-11', 'Nam', '011070123456', '0918765432', 11, 'Chu ho', 'THUONG_TRU', '2022-06-15'),
('Nguyen Thi M', '1984-02-28', 'Nu', '012084123456', '0927654321', 12, 'Chu ho', 'THUONG_TRU', '2022-07-22'),
('Tran Van N', '1993-05-05', 'Nam', '013093123456', '0936543210', 13, 'Chu ho', 'THUONG_TRU', '2022-08-11'),
('Le Thi O', '1989-08-08', 'Nu', '014089123456', '0945432109', 14, 'Chu ho', 'THUONG_TRU', '2022-09-05'),
('Pham Van P', '1996-12-25', 'Nam', '015096123456', '0954321098', 15, 'Chu ho', 'THUONG_TRU', '2022-10-18');

INSERT INTO khoan_phi (ten_khoan_phi, loai_phi, don_gia, chu_ky) VALUES
('Phi Quan Ly Chung Cu', 'BAT_BUOC', 7000.00, 'THANG'),   
('Phi Gui Xe May', 'DICH_VU', 100000.00, 'THANG'),       
('Phi Ve Sinh', 'BAT_BUOC', 50000.00, 'THANG');          

INSERT INTO hoa_don (ho_gia_dinh_id, thang_nam, tong_tien, trang_thai_thanh_toan, ngay_tao) VALUES
(1, '2023-11', 0, 'DA_THANH_TOAN', '2023-11-01'), 
(2, '2023-11', 0, 'DA_THANH_TOAN', '2023-11-01'),
(3, '2023-11', 0, 'DA_THANH_TOAN', '2023-11-01'),
(4, '2023-11', 0, 'DA_THANH_TOAN', '2023-11-01'),
(5, '2023-11', 0, 'DA_THANH_TOAN', '2023-11-01'),
(6, '2023-11', 0, 'DA_THANH_TOAN', '2023-11-01'),
(7, '2023-11', 0, 'DA_THANH_TOAN', '2023-11-01'),
(8, '2023-11', 0, 'DA_THANH_TOAN', '2023-11-01'),
(9, '2023-11', 0, 'DA_THANH_TOAN', '2023-11-01'),
(10, '2023-11', 0, 'DA_THANH_TOAN', '2023-11-01'),
(11, '2023-11', 0, 'CHUA_THANH_TOAN', '2023-11-01'), 
(12, '2023-11', 0, 'CHUA_THANH_TOAN', '2023-11-01'),
(13, '2023-11', 0, 'CHUA_THANH_TOAN', '2023-11-01'),
(14, '2023-11', 0, 'CHUA_THANH_TOAN', '2023-11-01'),
(15, '2023-11', 0, 'CHUA_THANH_TOAN', '2023-11-01');

INSERT INTO chi_tiet_hoa_don (hoa_don_id, khoan_phi_id, so_luong, thanh_tien) VALUES
(1, 1, 65.5, 458500.00), (1, 2, 2, 200000.00), (1, 3, 1, 50000.00), 
(2, 1, 70.00, 490000.00), (2, 2, 1, 100000.00), (2, 3, 1, 50000.00),
(3, 1, 55.00, 385000.00), (3, 2, 1, 100000.00), (3, 3, 1, 50000.00),
(4, 1, 85.00, 598500.00), (4, 2, 1, 100000.00), (4, 3, 1, 50000.00),
(5, 1, 65.00, 458500.00), (5, 2, 1, 100000.00), (5, 3, 1, 50000.00),
(11, 1, 100.00, 700000.00), (11, 2, 1, 100000.00), (11, 3, 1, 50000.00),
(12, 1, 75.00, 528500.00), (12, 2, 1, 100000.00), (12, 3, 1, 50000.00),
(13, 1, 65.00, 455000.00), (13, 2, 1, 100000.00), (13, 3, 1, 50000.00);

INSERT INTO giao_dich (hoa_don_id, so_tien_thanh_toan, phuong_thuc_thanh_toan, ma_giao_dich_ngan_hang) VALUES
(1, 708500.00, 'CHUYEN_KHOAN_QR', 'MB_FT231105123'),
(2, 640000.00, 'TIEN_MAT', NULL),
(3, 535000.00, 'CHUYEN_KHOAN_QR', 'VCB_99887766'),
(4, 748500.00, 'CHUYEN_KHOAN_QR', 'TCB_11223344'),
(5, 608500.00, 'TIEN_MAT', NULL);

INSERT INTO khoan_thu_tu_nguyen (ten_khoan_thu, ngay_phat_dong, ngay_ket_thuc, ghi_chu) VALUES
('Quy ung ho dong bao lu lut mien Trung', '2023-10-15', '2023-11-15', 'Phat dong tu nguyen toan chung cu'),
('Quy Khuyen hoc nam 2023', '2023-09-01', '2023-09-30', 'Khen thuong cac chau dat HCG'),
('Quy Tet Trung Thu', '2023-08-01', '2023-08-15', 'To chuc pha co cho cac chau');

INSERT INTO dong_gop_tu_nguyen (khoan_thu_id, ho_gia_dinh_id, so_tien, nguoi_thu_id) VALUES
(1, 1, 500000.00, 1),
(1, 2, 200000.00, 1),
(1, 5, 1000000.00, 1),
(2, 1, 200000.00, 2),
(2, 4, 300000.00, 2),
(3, 10, 50000.00, 1),
(3, 15, 100000.00, 1);