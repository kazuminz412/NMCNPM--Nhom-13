package com.bluemoon.service;

import com.bluemoon.model.DanhMucPhi;
import com.bluemoon.repository.DanhMucPhiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DanhMucPhiService {
    
    private final DanhMucPhiRepository repository;

    // 1. Lấy danh sách hiển thị lên bảng
    public List<DanhMucPhi> findAll() {
        return repository.findAll();
    }

    // 2. Thêm mới khoản phí
    @Transactional
    public DanhMucPhi save(DanhMucPhi danhMucPhi) {
        // Validation: Chặn tạo trùng tên khoản phí
        if (repository.existsByTenPhi(danhMucPhi.getTenPhi())) {
            throw new IllegalArgumentException("Tên khoản phí này đã tồn tại trong hệ thống!");
        }
        return repository.save(danhMucPhi);
    }

    // 3. Cập nhật thông tin (Dành cho nút Sửa)
    @Transactional
    public DanhMucPhi update(Long id, DanhMucPhi danhMucPhiMoi) {
        // Tìm khoản phí cũ dưới Database
        DanhMucPhi existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khoản phí có ID: " + id));

        // Cập nhật các trường thông tin từ Frontend gửi xuống
        existing.setTenPhi(danhMucPhiMoi.getTenPhi());
        existing.setLoaiPhi(danhMucPhiMoi.getLoaiPhi());
        existing.setDonGia(danhMucPhiMoi.getDonGia());
        existing.setDonViTinh(danhMucPhiMoi.getDonViTinh());
        existing.setTrangThai(danhMucPhiMoi.getTrangThai());
        existing.setMoTa(danhMucPhiMoi.getMoTa());

        // Lưu lại bản ghi đã sửa
        return repository.save(existing);
    }

    // 4. Xóa khoản phí
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy khoản phí để xóa!");
        }
        repository.deleteById(id);
    }
}
