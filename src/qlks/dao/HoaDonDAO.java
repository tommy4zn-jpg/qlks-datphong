package qlks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import qlks.exception.QLKSException;
import qlks.model.HoaDon;
import qlks.model.ThanhToan;
import qlks.util.KetNoiCSDL;

public class HoaDonDAO {

    public HoaDon layTheoDatPhong(String maDatPhong) throws QLKSException {
        String sql = "SELECT * FROM hoadon WHERE maDatPhong=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return doc(rs);
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải hoá đơn: " + e.getMessage());
        }
        return null;
    }

    private HoaDon doc(ResultSet rs) throws java.sql.SQLException {
        HoaDon hd = new HoaDon();
        hd.setMaHD(rs.getString("maHD"));
        hd.setMaDatPhong(rs.getString("maDatPhong"));
        hd.setNgayLap(rs.getTimestamp("ngayLap"));
        hd.setTongTienPhong(rs.getDouble("tongTienPhong"));
        hd.setTongTienDV(rs.getDouble("tongTienDV"));
        hd.setGiamGia(rs.getDouble("giamGia"));
        hd.setThue(rs.getDouble("thue"));
        hd.setTongThanhToan(rs.getDouble("tongThanhToan"));
        return hd;
    }

    public HoaDon lapHoaDon(HoaDon hd, ThanhToan tt) throws QLKSException {
        Connection cn = null;
        try {
            cn = KetNoiCSDL.getConnection();
            cn.setAutoCommit(false);

            String maHD = sinhMa(cn, "maHD", "hoadon", "HD");
            hd.setMaHD(maHD);
            String sqlHD = "INSERT INTO hoadon VALUES(?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = cn.prepareStatement(sqlHD)) {
                ps.setString(1, maHD);
                ps.setString(2, hd.getMaDatPhong());
                ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                ps.setDouble(4, hd.getTongTienPhong());
                ps.setDouble(5, hd.getTongTienDV());
                ps.setDouble(6, hd.getGiamGia());
                ps.setDouble(7, hd.getThue());
                ps.setDouble(8, hd.getTongThanhToan());
                ps.executeUpdate();
            }

            String maTT = sinhMa(cn, "maTT", "thanhtoan", "TT");
            String sqlTT = "INSERT INTO thanhtoan VALUES(?,?,?,?,?,?,?)";
            try (PreparedStatement ps = cn.prepareStatement(sqlTT)) {
                ps.setString(1, maTT);
                ps.setString(2, maHD);
                ps.setDouble(3, tt.getSoTien());
                ps.setString(4, tt.getPhuongThuc());
                ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                ps.setString(6, "GD" + System.currentTimeMillis());
                ps.setString(7, "THANH_CONG");
                ps.executeUpdate();
            }

            try (PreparedStatement ps = cn.prepareStatement("UPDATE datphong SET trangThai='DA_TRA_PHONG' WHERE maDatPhong=?")) {
                ps.setString(1, hd.getMaDatPhong());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE phong SET trangThai='TRONG' WHERE maPhong IN (SELECT maPhong FROM chitietdatphong WHERE maDatPhong=?)")) {
                ps.setString(1, hd.getMaDatPhong());
                ps.executeUpdate();
            }

            cn.commit();
            return hd;
        } catch (Exception e) {
            if (cn != null) try { cn.rollback(); } catch (Exception ignored) {}
            throw new QLKSException("Lỗi lập hoá đơn: " + e.getMessage());
        } finally {
            if (cn != null) try { cn.setAutoCommit(true); } catch (Exception ignored) {}
            KetNoiCSDL.dong(cn);
        }
    }

    private String sinhMa(Connection cn, String cot, String bang, String prefix) throws java.sql.SQLException {
        try (PreparedStatement ps = cn.prepareStatement("SELECT MAX(" + cot + ") FROM " + bang);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int so = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format(prefix + "%02d", so);
            }
        }
        return prefix + "01";
    }
}
