package qlks.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import qlks.exception.QLKSException;
import qlks.model.KhuyenMai;
import qlks.util.KetNoiCSDL;

public class KhuyenMaiDAO implements IDAO<KhuyenMai> {

    @Override
    public List<KhuyenMai> layTatCa() throws QLKSException {
        List<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM khuyenmai ORDER BY maKM";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(doc(rs));
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải khuyến mãi: " + e.getMessage());
        }
        return ds;
    }

    private KhuyenMai doc(ResultSet rs) throws java.sql.SQLException {
        KhuyenMai km = new KhuyenMai();
        km.setMaKM(rs.getString("maKM"));
        km.setTenKM(rs.getString("tenKM"));
        km.setPhanTramGiam(rs.getDouble("phanTramGiam"));
        km.setNgayBatDau(rs.getDate("ngayBatDau"));
        km.setNgayKetThuc(rs.getDate("ngayKetThuc"));
        km.setDieuKien(rs.getString("dieuKien"));
        return km;
    }

    @Override
    public boolean them(KhuyenMai km) throws QLKSException {
        String sql = "INSERT INTO khuyenmai VALUES(?,?,?,?,?,?)";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setDouble(3, km.getPhanTramGiam());
            ps.setDate(4, km.getNgayBatDau() == null ? null : new Date(km.getNgayBatDau().getTime()));
            ps.setDate(5, km.getNgayKetThuc() == null ? null : new Date(km.getNgayKetThuc().getTime()));
            ps.setString(6, km.getDieuKien());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi thêm khuyến mãi: " + e.getMessage());
        }
    }

    @Override
    public boolean capNhat(KhuyenMai km) throws QLKSException {
        String sql = "UPDATE khuyenmai SET tenKM=?,phanTramGiam=?,ngayBatDau=?,ngayKetThuc=?,dieuKien=? WHERE maKM=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, km.getTenKM());
            ps.setDouble(2, km.getPhanTramGiam());
            ps.setDate(3, km.getNgayBatDau() == null ? null : new Date(km.getNgayBatDau().getTime()));
            ps.setDate(4, km.getNgayKetThuc() == null ? null : new Date(km.getNgayKetThuc().getTime()));
            ps.setString(5, km.getDieuKien());
            ps.setString(6, km.getMaKM());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi cập nhật khuyến mãi: " + e.getMessage());
        }
    }

    @Override
    public boolean xoa(String ma) throws QLKSException {
        String sql = "DELETE FROM khuyenmai WHERE maKM=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Không thể xoá khuyến mãi đang được áp dụng.");
        }
    }

    public String sinhMa() {
        String sql = "SELECT MAX(maKM) FROM khuyenmai";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int so = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("KM%02d", so);
            }
        } catch (Exception ignored) {
        }
        return "KM01";
    }
}
