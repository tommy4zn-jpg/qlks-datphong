package qlks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import qlks.exception.QLKSException;
import qlks.model.DichVu;
import qlks.util.KetNoiCSDL;

public class DichVuDAO implements IDAO<DichVu> {

    @Override
    public List<DichVu> layTatCa() throws QLKSException {
        List<DichVu> ds = new ArrayList<>();
        String sql = "SELECT * FROM dichvu ORDER BY maDV";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DichVu dv = new DichVu();
                dv.setMaDV(rs.getString("maDV"));
                dv.setTenDV(rs.getString("tenDV"));
                dv.setDonGia(rs.getDouble("donGia"));
                dv.setMoTa(rs.getString("moTa"));
                ds.add(dv);
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải dịch vụ: " + e.getMessage());
        }
        return ds;
    }

    @Override
    public boolean them(DichVu dv) throws QLKSException {
        String sql = "INSERT INTO dichvu VALUES(?,?,?,?)";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, dv.getMaDV());
            ps.setString(2, dv.getTenDV());
            ps.setDouble(3, dv.getDonGia());
            ps.setString(4, dv.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi thêm dịch vụ: " + e.getMessage());
        }
    }

    @Override
    public boolean capNhat(DichVu dv) throws QLKSException {
        String sql = "UPDATE dichvu SET tenDV=?,donGia=?,moTa=? WHERE maDV=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, dv.getTenDV());
            ps.setDouble(2, dv.getDonGia());
            ps.setString(3, dv.getMoTa());
            ps.setString(4, dv.getMaDV());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi cập nhật dịch vụ: " + e.getMessage());
        }
    }

    @Override
    public boolean xoa(String ma) throws QLKSException {
        String sql = "DELETE FROM dichvu WHERE maDV=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Không thể xoá dịch vụ đang được sử dụng.");
        }
    }

    public String sinhMa() {
        String sql = "SELECT MAX(maDV) FROM dichvu";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int so = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("DV%02d", so);
            }
        } catch (Exception ignored) {
        }
        return "DV01";
    }
}
