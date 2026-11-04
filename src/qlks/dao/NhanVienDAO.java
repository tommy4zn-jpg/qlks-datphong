package qlks.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import qlks.exception.QLKSException;
import qlks.model.LeTan;
import qlks.model.NhanVien;
import qlks.model.QuanLy;
import qlks.util.KetNoiCSDL;

public class NhanVienDAO implements IDAO<NhanVien> {

    @Override
    public List<NhanVien> layTatCa() throws QLKSException {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien ORDER BY maNV";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(doc(rs));
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải nhân viên: " + e.getMessage());
        }
        return ds;
    }

    private NhanVien doc(ResultSet rs) throws java.sql.SQLException {
        NhanVien nv;
        if ("QUANLY".equals(rs.getString("chucVu"))) {
            QuanLy ql = new QuanLy();
            ql.setPhongBan(rs.getString("phongBan"));
            nv = ql;
        } else {
            LeTan lt = new LeTan();
            lt.setCaLamViec(rs.getString("caLamViec"));
            nv = lt;
        }
        nv.setMaNguoiDung(rs.getString("maNV"));
        nv.setHoTen(rs.getString("hoTen"));
        nv.setEmail(rs.getString("email"));
        nv.setSoDienThoai(rs.getString("soDienThoai"));
        nv.setTenDangNhap(rs.getString("tenDangNhap"));
        nv.setMatKhau(rs.getString("matKhau"));
        nv.setTrangThai(rs.getBoolean("trangThai"));
        nv.setChucVu(rs.getString("chucVu"));
        nv.setNgayVaoLam(rs.getDate("ngayVaoLam"));
        return nv;
    }

    @Override
    public boolean them(NhanVien nv) throws QLKSException {
        String sql = "INSERT INTO nhanvien(maNV,hoTen,email,soDienThoai,tenDangNhap,matKhau,trangThai,chucVu,ngayVaoLam,caLamViec,phongBan) VALUES(?,?,?,?,?,?,1,?,?,?,?)";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNguoiDung());
            ps.setString(2, nv.getHoTen());
            ps.setString(3, nv.getEmail());
            ps.setString(4, nv.getSoDienThoai());
            ps.setString(5, nv.getTenDangNhap());
            ps.setString(6, nv.getMatKhau());
            ps.setString(7, nv.getChucVu());
            ps.setDate(8, nv.getNgayVaoLam() == null ? null : new Date(nv.getNgayVaoLam().getTime()));
            ps.setString(9, nv instanceof LeTan ? ((LeTan) nv).getCaLamViec() : null);
            ps.setString(10, nv instanceof QuanLy ? ((QuanLy) nv).getPhongBan() : null);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi thêm nhân viên: " + e.getMessage());
        }
    }

    @Override
    public boolean capNhat(NhanVien nv) throws QLKSException {
        String sql = "UPDATE nhanvien SET hoTen=?,email=?,soDienThoai=?,chucVu=?,caLamViec=?,phongBan=? WHERE maNV=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getEmail());
            ps.setString(3, nv.getSoDienThoai());
            ps.setString(4, nv.getChucVu());
            ps.setString(5, nv instanceof LeTan ? ((LeTan) nv).getCaLamViec() : null);
            ps.setString(6, nv instanceof QuanLy ? ((QuanLy) nv).getPhongBan() : null);
            ps.setString(7, nv.getMaNguoiDung());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi cập nhật nhân viên: " + e.getMessage());
        }
    }

    @Override
    public boolean xoa(String ma) throws QLKSException {
        String sql = "DELETE FROM nhanvien WHERE maNV=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Không thể xoá nhân viên đã xử lý đặt phòng.");
        }
    }

    public String sinhMa() {
        String sql = "SELECT MAX(maNV) FROM nhanvien";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int so = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("NV%02d", so);
            }
        } catch (Exception ignored) {
        }
        return "NV01";
    }
}
