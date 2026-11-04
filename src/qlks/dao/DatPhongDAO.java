package qlks.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import qlks.exception.QLKSException;
import qlks.model.ChiTietDatPhong;
import qlks.model.DatPhong;
import qlks.util.KetNoiCSDL;

public class DatPhongDAO {

    public String taoDatPhong(DatPhong dp, ChiTietDatPhong ct) throws QLKSException {
        Connection cn = null;
        try {
            cn = KetNoiCSDL.getConnection();
            cn.setAutoCommit(false);

            String maDP = sinhMa(cn);
            dp.setMaDatPhong(maDP);

            String sqlDP = "INSERT INTO datphong(maDatPhong,maKH,maNV,ngayDat,ngayNhan,ngayTra,soKhach,tongTien,trangThai,ghiChu,maKM) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = cn.prepareStatement(sqlDP)) {
                ps.setString(1, maDP);
                ps.setString(2, dp.getMaKH());
                ps.setString(3, dp.getMaNV());
                ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                ps.setDate(5, new Date(dp.getNgayNhan().getTime()));
                ps.setDate(6, new Date(dp.getNgayTra().getTime()));
                ps.setInt(7, dp.getSoKhach());
                ps.setDouble(8, dp.getTongTien());
                ps.setString(9, dp.getTrangThai());
                ps.setString(10, dp.getGhiChu());
                ps.setString(11, dp.getMaKM());
                ps.executeUpdate();
            }

            String maCT = "CT" + maDP.substring(2);
            String sqlCT = "INSERT INTO chitietdatphong VALUES(?,?,?,?,?,?)";
            try (PreparedStatement ps = cn.prepareStatement(sqlCT)) {
                ps.setString(1, maCT);
                ps.setString(2, maDP);
                ps.setString(3, ct.getMaPhong());
                ps.setDouble(4, ct.getGiaPhong());
                ps.setInt(5, ct.getSoDem());
                ps.setDouble(6, ct.getThanhTien());
                ps.executeUpdate();
            }

            try (PreparedStatement ps = cn.prepareStatement("UPDATE phong SET trangThai='DA_DAT' WHERE maPhong=?")) {
                ps.setString(1, ct.getMaPhong());
                ps.executeUpdate();
            }

            cn.commit();
            return maDP;
        } catch (Exception e) {
            if (cn != null) try { cn.rollback(); } catch (Exception ignored) {}
            throw new QLKSException("Lỗi tạo đặt phòng: " + e.getMessage());
        } finally {
            if (cn != null) try { cn.setAutoCommit(true); } catch (Exception ignored) {}
            KetNoiCSDL.dong(cn);
        }
    }

    private String sinhMa(Connection cn) throws java.sql.SQLException {
        try (PreparedStatement ps = cn.prepareStatement("SELECT MAX(maDatPhong) FROM datphong");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int so = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("DP%02d", so);
            }
        }
        return "DP01";
    }

    public List<DatPhong> layTatCa() throws QLKSException {
        return truyVan("SELECT d.*, k.hoTen FROM datphong d LEFT JOIN khachhang k ON d.maKH=k.maKH ORDER BY d.ngayDat DESC", null);
    }

    public List<DatPhong> layTheoKhachHang(String maKH) throws QLKSException {
        return truyVan("SELECT d.*, k.hoTen FROM datphong d LEFT JOIN khachhang k ON d.maKH=k.maKH WHERE d.maKH=? ORDER BY d.ngayDat DESC", maKH);
    }

    private List<DatPhong> truyVan(String sql, String thamSo) throws QLKSException {
        List<DatPhong> ds = new ArrayList<>();
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            if (thamSo != null) ps.setString(1, thamSo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DatPhong d = new DatPhong();
                    d.setMaDatPhong(rs.getString("maDatPhong"));
                    d.setMaKH(rs.getString("maKH"));
                    d.setMaNV(rs.getString("maNV"));
                    d.setNgayDat(rs.getTimestamp("ngayDat"));
                    d.setNgayNhan(rs.getDate("ngayNhan"));
                    d.setNgayTra(rs.getDate("ngayTra"));
                    d.setSoKhach(rs.getInt("soKhach"));
                    d.setTongTien(rs.getDouble("tongTien"));
                    d.setTrangThai(rs.getString("trangThai"));
                    d.setGhiChu(rs.getString("ghiChu"));
                    d.setMaKM(rs.getString("maKM"));
                    d.setTenKhachHang(rs.getString("hoTen"));
                    ds.add(d);
                }
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải đặt phòng: " + e.getMessage());
        }
        return ds;
    }

    public ChiTietDatPhong layChiTiet(String maDatPhong) throws QLKSException {
        String sql = "SELECT c.*, p.soPhong FROM chitietdatphong c LEFT JOIN phong p ON c.maPhong=p.maPhong WHERE c.maDatPhong=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChiTietDatPhong ct = new ChiTietDatPhong();
                    ct.setMaCT(rs.getString("maCT"));
                    ct.setMaDatPhong(rs.getString("maDatPhong"));
                    ct.setMaPhong(rs.getString("maPhong"));
                    ct.setGiaPhong(rs.getDouble("giaPhong"));
                    ct.setSoDem(rs.getInt("soDem"));
                    ct.setThanhTien(rs.getDouble("thanhTien"));
                    ct.setSoPhong(rs.getString("soPhong"));
                    return ct;
                }
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải chi tiết đặt phòng: " + e.getMessage());
        }
        return null;
    }

    public boolean capNhatTrangThai(String maDatPhong, String trangThai) throws QLKSException {
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE datphong SET trangThai=? WHERE maDatPhong=?")) {
            ps.setString(1, trangThai);
            ps.setString(2, maDatPhong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi cập nhật trạng thái: " + e.getMessage());
        }
    }

    public boolean ganNhanVien(String maDatPhong, String maNV) throws QLKSException {
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE datphong SET maNV=? WHERE maDatPhong=?")) {
            ps.setString(1, maNV);
            ps.setString(2, maDatPhong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi gán nhân viên: " + e.getMessage());
        }
    }

    public boolean huyDatPhong(String maDatPhong) throws QLKSException {
        Connection cn = null;
        try {
            cn = KetNoiCSDL.getConnection();
            cn.setAutoCommit(false);
            String maPhong = null;
            try (PreparedStatement ps = cn.prepareStatement("SELECT maPhong FROM chitietdatphong WHERE maDatPhong=?")) {
                ps.setString(1, maDatPhong);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) maPhong = rs.getString(1);
                }
            }
            try (PreparedStatement ps = cn.prepareStatement("UPDATE datphong SET trangThai='DA_HUY' WHERE maDatPhong=?")) {
                ps.setString(1, maDatPhong);
                ps.executeUpdate();
            }
            if (maPhong != null) {
                try (PreparedStatement ps = cn.prepareStatement("UPDATE phong SET trangThai='TRONG' WHERE maPhong=?")) {
                    ps.setString(1, maPhong);
                    ps.executeUpdate();
                }
            }
            cn.commit();
            return true;
        } catch (Exception e) {
            if (cn != null) try { cn.rollback(); } catch (Exception ignored) {}
            throw new QLKSException("Lỗi huỷ đặt phòng: " + e.getMessage());
        } finally {
            if (cn != null) try { cn.setAutoCommit(true); } catch (Exception ignored) {}
            KetNoiCSDL.dong(cn);
        }
    }
}
