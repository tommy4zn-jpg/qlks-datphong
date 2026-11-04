package qlks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import qlks.exception.QLKSException;
import qlks.util.KetNoiCSDL;

public class ThongKeDAO {

    public double tongDoanhThu() throws QLKSException {
        return laySo("SELECT COALESCE(SUM(tongThanhToan),0) FROM hoadon");
    }

    public int soDatPhong() throws QLKSException {
        return (int) laySo("SELECT COUNT(*) FROM datphong");
    }

    public int soPhongTrong() throws QLKSException {
        return (int) laySo("SELECT COUNT(*) FROM phong WHERE trangThai='TRONG'");
    }

    public int soKhachHang() throws QLKSException {
        return (int) laySo("SELECT COUNT(*) FROM khachhang");
    }

    private double laySo(String sql) throws QLKSException {
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) {
            throw new QLKSException("Lỗi thống kê: " + e.getMessage());
        }
        return 0;
    }

    public Map<String, Double> doanhThuTheoLoaiPhong() throws QLKSException {
        Map<String, Double> kq = new LinkedHashMap<>();
        String sql = "SELECT l.tenLoai, COALESCE(SUM(c.thanhTien),0) AS dt " +
                "FROM loaiphong l LEFT JOIN phong p ON p.maLoaiPhong=l.maLoaiPhong " +
                "LEFT JOIN chitietdatphong c ON c.maPhong=p.maPhong " +
                "GROUP BY l.maLoaiPhong, l.tenLoai ORDER BY dt DESC";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                kq.put(rs.getString("tenLoai"), rs.getDouble("dt"));
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi thống kê doanh thu: " + e.getMessage());
        }
        return kq;
    }

    public Map<String, Integer> soPhongTheoTrangThai() throws QLKSException {
        Map<String, Integer> kq = new LinkedHashMap<>();
        String sql = "SELECT trangThai, COUNT(*) AS sl FROM phong GROUP BY trangThai";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                kq.put(rs.getString("trangThai"), rs.getInt("sl"));
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi thống kê phòng: " + e.getMessage());
        }
        return kq;
    }
}
