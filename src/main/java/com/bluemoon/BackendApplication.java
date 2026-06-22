package com.bluemoon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.bluemoon.repository.NguoiDungRepository;
import com.bluemoon.repository.HoDanRepository;
import com.bluemoon.repository.NhanKhauRepository;
import com.bluemoon.model.NguoiDung;
import com.bluemoon.model.HoDan;
import com.bluemoon.model.NhanKhau;
import java.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(NguoiDungRepository repository, HoDanRepository hoDanRepository,
            NhanKhauRepository nhanKhauRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!repository.existsByUsername("admin")) {
                NguoiDung admin = new NguoiDung();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setHoTen("Quản Trị Viên");
                admin.setRole("ADMIN");
                admin.setTrangThai(true);
                repository.save(admin);
                System.out.println("====== TẠO THÀNH CÔNG TÀI KHOẢN ADMIN MẶC ĐỊNH ======");
            }
            if (!repository.existsByUsername("ketoan")) {
                NguoiDung ketoan = new NguoiDung();
                ketoan.setUsername("ketoan");
                ketoan.setPassword(passwordEncoder.encode("123456"));
                ketoan.setHoTen("Kế Toán Trưởng");
                ketoan.setRole("KE_TOAN");
                ketoan.setTrangThai(true);
                repository.save(ketoan);
            }
            if (!repository.existsByUsername("cudan01")) {
                NguoiDung cudan = new NguoiDung();
                cudan.setUsername("cudan01");
                cudan.setPassword(passwordEncoder.encode("123456"));
                cudan.setHoTen("Nguyễn Văn An");
                cudan.setRole("CU_DAN");
                cudan.setTrangThai(true);
                cudan.setHoDanId(1L); // Liên kết với hộ dân số 1 để test
                repository.save(cudan);
            }

            if (hoDanRepository.count() == 0) {
                HoDan hd1 = new HoDan();
                hd1.setMaHoKhau("HK001");
                hd1.setSoPhong("A1-204");
                hd1.setTenChuHo("Nguyễn Văn An");
                hd1.setToaNha("Blue Moon");
                hd1.setSoDienThoai("0912345678");
                hd1.setDienTichM2(75.5);
                hd1.setTrangThai("da_dong");
                hoDanRepository.save(hd1);

                HoDan hd2 = new HoDan();
                hd2.setMaHoKhau("HK002");
                hd2.setSoPhong("B2-108");
                hd2.setTenChuHo("Lê Minh Châu");
                hd2.setToaNha("Blue Moon");
                hd2.setSoDienThoai("0901111222");
                hd2.setDienTichM2(60.0);
                hd2.setTrangThai("mot_phan");
                hoDanRepository.save(hd2);

                HoDan hd3 = new HoDan();
                hd3.setMaHoKhau("HK003");
                hd3.setSoPhong("C3-402");
                hd3.setTenChuHo("Hoàng Thị Lan");
                hd3.setToaNha("Blue Moon");
                hd3.setSoDienThoai("0966333444");
                hd3.setDienTichM2(85.0);
                hd3.setTrangThai("chua_dong");
                hoDanRepository.save(hd3);
                System.out.println("====== TẠO DỮ LIỆU HỘ DÂN MẪU THÀNH CÔNG ======");

                // Thêm Nhân Khẩu mẫu
                NhanKhau nk1 = new NhanKhau();
                nk1.setHoTen("Nguyễn Văn An");
                nk1.setSoCCCD("001085012345");
                nk1.setNgaySinh(LocalDate.of(1985, 3, 15));
                nk1.setGioiTinh("nam");
                nk1.setSoDienThoai("0912345678");
                nk1.setQuanHe("chu_ho");
                nk1.setTrangThai("thuong_tru");
                nk1.setHoDan(hd1);
                nhanKhauRepository.save(nk1);

                NhanKhau nk2 = new NhanKhau();
                nk2.setHoTen("Trần Thị Bình");
                nk2.setSoCCCD("001090023456");
                nk2.setNgaySinh(LocalDate.of(1990, 7, 22));
                nk2.setGioiTinh("nu");
                nk2.setSoDienThoai("0987654321");
                nk2.setQuanHe("vo_chong");
                nk2.setTrangThai("thuong_tru");
                nk2.setHoDan(hd1);
                nhanKhauRepository.save(nk2);

                NhanKhau nk3 = new NhanKhau();
                nk3.setHoTen("Nguyễn Văn Bé");
                nk3.setNgaySinh(LocalDate.of(2015, 1, 10));
                nk3.setGioiTinh("nam");
                nk3.setQuanHe("con");
                nk3.setTrangThai("thuong_tru");
                nk3.setHoDan(hd1);
                nhanKhauRepository.save(nk3);

                NhanKhau nk4 = new NhanKhau();
                nk4.setHoTen("Lê Minh Châu");
                nk4.setSoCCCD("001078034567");
                nk4.setNgaySinh(LocalDate.of(1978, 11, 5));
                nk4.setGioiTinh("nam");
                nk4.setSoDienThoai("0901111222");
                nk4.setQuanHe("chu_ho");
                nk4.setTrangThai("tam_tru");
                nk4.setHoDan(hd2);
                nhanKhauRepository.save(nk4);

                NhanKhau nk5 = new NhanKhau();
                nk5.setHoTen("Hoàng Thị Lan");
                nk5.setSoCCCD("001095045678");
                nk5.setNgaySinh(LocalDate.of(1995, 6, 18));
                nk5.setGioiTinh("nu");
                nk5.setSoDienThoai("0966333444");
                nk5.setQuanHe("chu_ho");
                nk5.setTrangThai("thuong_tru");
                nk5.setHoDan(hd3);
                nhanKhauRepository.save(nk5);

                System.out.println("====== TẠO DỮ LIỆU NHÂN KHẨU MẪU THÀNH CÔNG ======");
            }

            // Theo yêu cầu của User: Thêm NGUYÊN 1 LÔ NHÂN KHẨU MỚI (khoảng 10 người) để
            // test hiển thị
            if (nhanKhauRepository.count() < 30) {
                String[] hoFams = { "Đỗ", "Lý", "Ngô", "Bùi", "Phan", "Vũ", "Trịnh", "Hồ", "Đinh", "Đoàn" };
                for (int i = 0; i < 10; i++) {
                    HoDan hd = new HoDan();
                    hd.setMaHoKhau("HK00" + (i + 10));
                    hd.setSoPhong("D" + i + "-10" + i);
                    hd.setTenChuHo(hoFams[i] + " Văn " + (char) ('A' + i));
                    hd.setToaNha("Blue Moon");
                    hd.setSoDienThoai("0988" + i + i + i + "123");
                    hd.setDienTichM2(60.0 + i);
                    hoDanRepository.save(hd);

                    NhanKhau nk = new NhanKhau();
                    nk.setHoTen(hd.getTenChuHo());
                    nk.setNgaySinh(LocalDate.of(1980 + i, 1 + i, 10 + i));
                    nk.setGioiTinh("nam");
                    nk.setQuanHe("chu_ho");
                    nk.setTrangThai("thuong_tru");
                    nk.setHoDan(hd);
                    nhanKhauRepository.save(nk);

                    // Thêm vợ/con
                    NhanKhau nkVo = new NhanKhau();
                    nkVo.setHoTen("Thị " + (char) ('A' + i + 1));
                    nkVo.setNgaySinh(LocalDate.of(1985 + i, 2 + i, 12 + i));
                    nkVo.setGioiTinh("nu");
                    nkVo.setQuanHe("vo_chong");
                    nkVo.setTrangThai("thuong_tru");
                    nkVo.setHoDan(hd);
                    nhanKhauRepository.save(nkVo);
                }
                System.out.println("====== ĐÃ THÊM LÔ 20 NHÂN KHẨU VÀ 10 HỘ DÂN MỚI ======");
            }
        };
    }
}
