package qlks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import qlks.exception.QLKSException;
import qlks.model.KhachHang;
import qlks.util.KetNoiCSDL;

public class KhachHangDAO implements IDAO<KhachHang> {

    @Override
    public List<KhachHang> layTatCa() throws QLKSException {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM khachhang ORDER BY maKH";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(doc(rs));
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải khách hàng: " + e.getMessage());
        }
        return ds;
    }

    public KhachHang timTheoMa(String maKH) throws QLKSException {
        String sql = "SELECT * FROM khachhang WHERE maKH=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return doc(rs);
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tìm khách hàng: " + e.getMessage());
        }
        return null;
    }

    private KhachHang doc(ResultSet rs) throws java.sql.SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaNguoiDung(rs.getString("maKH"));
        kh.setHoTen(rs.getString("hoTen"));
        kh.setEmail(rs.getString("email"));
        kh.setSoDienThoai(rs.getString("soDienThoai"));
        kh.setTenDangNhap(rs.getString("tenDangNhap"));
        kh.setMatKhau(rs.getString("matKhau"));
        kh.setTrangThai(rs.getBoolean("trangThai"));
        kh.setCccd(rs.getString("cccd"));
        kh.setDiaChi(rs.getString("diaChi"));
        kh.setDiemTichLuy(rs.getInt("diemTichLuy"));
        return kh;
    }

    @Override
    public boolean them(KhachHang kh) throws QLKSException {
        return false;
    }

    @Override
    public boolean capNhat(KhachHang kh) throws QLKSException {
        String sql = "UPDATE khachhang SET hoTen=?,email=?,soDienThoai=?,cccd=?,diaChi=? WHERE maKH=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getEmail());
            ps.setString(3, kh.getSoDienThoai());
            ps.setString(4, kh.getCccd());
            ps.setString(5, kh.getDiaChi());
            ps.setString(6, kh.getMaNguoiDung());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi cập nhật khách hàng: " + e.getMessage());
        }
    }

    public boolean doiMatKhau(String maKH, String matKhauMoi) throws QLKSException {
        String sql = "UPDATE khachhang SET matKhau=? WHERE maKH=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, matKhauMoi);
            ps.setString(2, maKH);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi đổi mật khẩu: " + e.getMessage());
        }
    }

    public void congDiemTichLuy(String maKH, int diem) throws QLKSException {
        String sql = "UPDATE khachhang SET diemTichLuy=diemTichLuy+? WHERE maKH=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, diem);
            ps.setString(2, maKH);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new QLKSException("Lỗi cộng điểm: " + e.getMessage());
        }
    }

    @Override
    public boolean xoa(String ma) throws QLKSException {
        String sql = "DELETE FROM khachhang WHERE maKH=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Không thể xoá khách hàng đã có lịch sử đặt phòng.");
        }
    }
}
