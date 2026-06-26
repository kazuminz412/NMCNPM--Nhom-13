package com.bluemoon.controller;

import com.bluemoon.dto.CanHoDTO;
import com.bluemoon.model.HoDan;
import com.bluemoon.repository.HoDanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/can-ho")
@RequiredArgsConstructor
public class CanHoController {

    private final HoDanRepository hoDanRepo;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CanHoDTO>> getDanhSachCanHo() {
        List<HoDan> tatCaHoDan = hoDanRepo.findAll();
        List<CanHoDTO> result = new ArrayList<>();
        
        // 1. Thêm TẤT CẢ các hộ dân vào danh sách "Đã có chủ" (Đảm bảo đủ 47 hộ)
        List<String> cacPhongDaCoChu = new ArrayList<>();
        for (HoDan hd : tatCaHoDan) {
            CanHoDTO dto = new CanHoDTO();
            dto.setMaCanHo(hd.getSoPhong());
            dto.setToaNha(hd.getToaNha());
            dto.setTrangThai("da_co_chu");
            dto.setTenChuHo(hd.getTenChuHo());
            dto.setDienTich(hd.getDienTichM2());
            result.add(dto);
            cacPhongDaCoChu.add(hd.getSoPhong());
        }

        // 2. Sinh thêm các căn hộ "Chưa có chủ" (Khoảng 144 căn để tổng ~150-200)
        // Tòa: A1, B1, C1, D1 (4 tòa)
        String[] toas = {"A1", "B1", "C1", "D1"};
        // Mảng diện tích tạo sự đa dạng theo số phòng (cùng trục dọc sẽ giống nhau, đặc thù chung cư)
        double[] dienTichMau = {0, 65.5, 58.0, 72.5, 85.0, 50.5, 50.5, 85.0, 58.0, 65.5};
        
        for (String toa : toas) {
            // Tầng từ 1 đến 4, số phòng từ 1 đến 9 (vd 101...409) -> 4 * 9 = 36 phòng/tòa
            // Tổng: 4 tòa * 36 = 144 phòng
            for (int tang = 1; tang <= 4; tang++) {
                for (int phong = 1; phong <= 9; phong++) {
                    String maCanHo = String.format("%s-%d0%d", toa, tang, phong);
                    
                    // Chỉ thêm vào nếu phòng này chưa có ai ở
                    if (!cacPhongDaCoChu.contains(maCanHo)) {
                        CanHoDTO dto = new CanHoDTO();
                        dto.setMaCanHo(maCanHo);
                        dto.setToaNha(toa);
                        dto.setTrangThai("chua_co_chu");
                        dto.setTenChuHo(null);
                        
                        // Diện tích đa dạng dựa theo số phòng và cộng thêm một chút tùy theo tầng
                        double dienTich = dienTichMau[phong] + (tang * 1.5);
                        dto.setDienTich(dienTich);
                        
                        result.add(dto);
                    }
                }
            }
        }

        return ResponseEntity.ok(result);
    }
}
