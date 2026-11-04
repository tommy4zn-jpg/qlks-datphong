package qlks.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import qlks.exception.QLKSException;
import qlks.model.SuDungDichVu;
import qlks.util.KetNoiCSDL;

public class SuDungDichVuDAO {

    public List<SuDungDichVu> layTheoDatPhong(String maDatPhong) throws QLKSException {
        List<SuDungDichVu> ds = new ArrayList<>();
        String sql = "SELECT s.*, d.tenDV, d.donGia FROM sudungdichvu s LEFT JOIN dichvu d ON s.maDV=d.maDV WHERE s.maDatPhong=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SuDungDichVu s = new SuDungDichVu();
                    s.setMaSD(rs.getString("maSD"));
                    s.setMaDatPhong(rs.getString("maDatPhong"));
                    s.setMaDV(rs.getString("maDV"));
                    s.setSoLuong(rs.getInt("soLuong"));
                    s.setNgaySuDung(rs.getDate("ngaySuDung"));
                    s.setThanhTien(rs.getDouble("thanhTien"));
                    s.setTenDV(rs.getString("tenDV"));
                    s.setDonGia(rs.getDouble("donGia"));
                    ds.add(s);
                }
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải dịch vụ đã dùng: " + e.getMessage());
        }
        return ds;
    }

    public boolean them(SuDungDichVu s) throws QLKSException {
        String sql = "INSERT INTO sudungdichvu VALUES(?,?,?,?,?,?)";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, sinhMa());
            ps.setString(2, s.getMaDatPhong());
            ps.setString(3, s.getMaDV());
            ps.setInt(4, s.getSoLuong());
            ps.setDate(5, new Date(System.currentTimeMillis()));
            ps.setDouble(6, s.getThanhTien());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi thêm dịch vụ: " + e.getMessage());
        }
    }

    private String sinhMa() {
        String sql = "SELECT MAX(maSD) FROM sudungdichvu";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int so = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("SD%02d", so);
            }
        } catch (Exception ignored) {
        }
        return "SD01";
    }

    public double tongTienDichVu(String maDatPhong) throws QLKSException {
        String sql = "SELECT COALESCE(SUM(thanhTien),0) FROM sudungdichvu WHERE maDatPhong=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, maDatPhong);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tính tiền dịch vụ: " + e.getMessage());
        }
        return 0;
    }
}
