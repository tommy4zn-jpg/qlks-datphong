package qlks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import qlks.exception.DangNhapException;
import qlks.model.KhachHang;
import qlks.model.LeTan;
import qlks.model.NguoiDung;
import qlks.model.QuanLy;
import qlks.util.KetNoiCSDL;

public class XacThucDAO {

    public NguoiDung dangNhap(String tenDangNhap, String matKhau) throws DangNhapException {
        NguoiDung nd = timNhanVien(tenDangNhap, matKhau);
        if (nd != null) return nd;
        nd = timKhachHang(tenDangNhap, matKhau);
        if (nd != null) return nd;
        throw new DangNhapException("Tên đăng nhập hoặc mật khẩu không đúng!");
    }

    private NguoiDung timNhanVien(String tenDangNhap, String matKhau) throws DangNhapException {
        String sql = "SELECT * FROM nhanvien WHERE tenDangNhap=? AND matKhau=? AND trangThai=1";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = KetNoiCSDL.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);
            rs = ps.executeQuery();
            if (rs.next()) {
                String chucVu = rs.getString("chucVu");
                if ("QUANLY".equals(chucVu)) {
                    QuanLy ql = new QuanLy();
                    ganThongTinChung(ql, rs);
                    ql.setPhongBan(rs.getString("phongBan"));
                    return ql;
                } else {
                    LeTan lt = new LeTan();
                    ganThongTinChung(lt, rs);
                    lt.setCaLamViec(rs.getString("caLamViec"));
                    return lt;
                }
            }
            return null;
        } catch (Exception e) {
            throw new DangNhapException("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        } finally {
            KetNoiCSDL.dong(rs, ps, cn);
        }
    }

    private void ganThongTinChung(NguoiDung nd, ResultSet rs) throws java.sql.SQLException {
        nd.setMaNguoiDung(rs.getString("maNV"));
        nd.setHoTen(rs.getString("hoTen"));
        nd.setEmail(rs.getString("email"));
        nd.setSoDienThoai(rs.getString("soDienThoai"));
        nd.setTenDangNhap(rs.getString("tenDangNhap"));
        nd.setMatKhau(rs.getString("matKhau"));
        nd.setTrangThai(rs.getBoolean("trangThai"));
    }

    private NguoiDung timKhachHang(String tenDangNhap, String matKhau) throws DangNhapException {
        String sql = "SELECT * FROM khachhang WHERE tenDangNhap=? AND matKhau=? AND trangThai=1";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = KetNoiCSDL.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setString(1, tenDangNhap);
            ps.setString(2, matKhau);
            rs = ps.executeQuery();
            if (rs.next()) {
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
            return null;
        } catch (Exception e) {
            throw new DangNhapException("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
        } finally {
            KetNoiCSDL.dong(rs, ps, cn);
        }
    }

    public boolean dangKy(KhachHang kh) throws DangNhapException {
        String sql = "INSERT INTO khachhang(maKH,hoTen,email,soDienThoai,tenDangNhap,matKhau,trangThai,cccd,diaChi,diemTichLuy) VALUES(?,?,?,?,?,?,1,?,?,0)";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = KetNoiCSDL.getConnection();
            ps = cn.prepareStatement(sql);
            ps.setString(1, kh.getMaNguoiDung());
            ps.setString(2, kh.getHoTen());
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getSoDienThoai());
            ps.setString(5, kh.getTenDangNhap());
            ps.setString(6, kh.getMatKhau());
            ps.setString(7, kh.getCccd());
            ps.setString(8, kh.getDiaChi());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new DangNhapException("Đăng ký thất bại: " + e.getMessage());
        } finally {
            KetNoiCSDL.dong(ps, cn);
        }
    }

    public boolean tonTaiTenDangNhap(String tenDangNhap) {
        String sql = "SELECT 1 FROM khachhang WHERE tenDangNhap=? UNION SELECT 1 FROM nhanvien WHERE tenDangNhap=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            ps.setString(2, tenDangNhap);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            return true;
        }
    }

    public String sinhMaKhachHang() {
        String sql = "SELECT MAX(maKH) FROM khachhang";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int so = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("KH%02d", so);
            }
        } catch (Exception ignored) {
        }
        return "KH01";
    }
}
