package qlks.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import qlks.exception.QLKSException;
import qlks.model.DanhGia;
import qlks.util.KetNoiCSDL;

public class DanhGiaDAO {

    public List<DanhGia> layTatCa() throws QLKSException {
        List<DanhGia> ds = new ArrayList<>();
        String sql = "SELECT g.*, k.hoTen FROM danhgia g LEFT JOIN khachhang k ON g.maKH=k.maKH ORDER BY g.ngayDanhGia DESC";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DanhGia g = new DanhGia();
                g.setMaDG(rs.getString("maDG"));
                g.setMaKH(rs.getString("maKH"));
                g.setMaPhong(rs.getString("maPhong"));
                g.setDiem(rs.getInt("diem"));
                g.setBinhLuan(rs.getString("binhLuan"));
                g.setNgayDanhGia(rs.getDate("ngayDanhGia"));
                g.setTenKhachHang(rs.getString("hoTen"));
                ds.add(g);
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải đánh giá: " + e.getMessage());
        }
        return ds;
    }

    public boolean guiDanhGia(DanhGia g) throws QLKSException {
        String sql = "INSERT INTO danhgia VALUES(?,?,?,?,?,?)";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, sinhMa());
            ps.setString(2, g.getMaKH());
            ps.setString(3, g.getMaPhong());
            ps.setInt(4, g.getDiem());
            ps.setString(5, g.getBinhLuan());
            ps.setDate(6, new Date(System.currentTimeMillis()));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi gửi đánh giá: " + e.getMessage());
        }
    }

    private String sinhMa() {
        String sql = "SELECT MAX(maDG) FROM danhgia";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int so = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("DG%02d", so);
            }
        } catch (Exception ignored) {
        }
        return "DG01";
    }
}
