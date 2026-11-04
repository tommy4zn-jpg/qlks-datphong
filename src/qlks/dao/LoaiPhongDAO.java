package qlks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import qlks.exception.QLKSException;
import qlks.model.LoaiPhong;
import qlks.util.KetNoiCSDL;

public class LoaiPhongDAO implements IDAO<LoaiPhong> {

    @Override
    public List<LoaiPhong> layTatCa() throws QLKSException {
        List<LoaiPhong> ds = new ArrayList<>();
        String sql = "SELECT * FROM loaiphong ORDER BY maLoaiPhong";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(doc(rs));
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải loại phòng: " + e.getMessage());
        }
        return ds;
    }

    private LoaiPhong doc(ResultSet rs) throws java.sql.SQLException {
        LoaiPhong lp = new LoaiPhong();
        lp.setMaLoaiPhong(rs.getString("maLoaiPhong"));
        lp.setTenLoai(rs.getString("tenLoai"));
        lp.setMoTa(rs.getString("moTa"));
        lp.setGiaCoBan(rs.getDouble("giaCoBan"));
        lp.setSucChua(rs.getInt("sucChua"));
        lp.setTienNghi(rs.getString("tienNghi"));
        return lp;
    }

    @Override
    public boolean them(LoaiPhong lp) throws QLKSException {
        String sql = "INSERT INTO loaiphong VALUES(?,?,?,?,?,?)";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, lp.getMaLoaiPhong());
            ps.setString(2, lp.getTenLoai());
            ps.setString(3, lp.getMoTa());
            ps.setDouble(4, lp.getGiaCoBan());
            ps.setInt(5, lp.getSucChua());
            ps.setString(6, lp.getTienNghi());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi thêm loại phòng: " + e.getMessage());
        }
    }

    @Override
    public boolean capNhat(LoaiPhong lp) throws QLKSException {
        String sql = "UPDATE loaiphong SET tenLoai=?,moTa=?,giaCoBan=?,sucChua=?,tienNghi=? WHERE maLoaiPhong=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, lp.getTenLoai());
            ps.setString(2, lp.getMoTa());
            ps.setDouble(3, lp.getGiaCoBan());
            ps.setInt(4, lp.getSucChua());
            ps.setString(5, lp.getTienNghi());
            ps.setString(6, lp.getMaLoaiPhong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi cập nhật loại phòng: " + e.getMessage());
        }
    }

    @Override
    public boolean xoa(String ma) throws QLKSException {
        String sql = "DELETE FROM loaiphong WHERE maLoaiPhong=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Không thể xoá (loại phòng đang được sử dụng).");
        }
    }

    public String sinhMa() {
        String sql = "SELECT MAX(maLoaiPhong) FROM loaiphong";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                int so = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("LP%02d", so);
            }
        } catch (Exception ignored) {
        }
        return "LP01";
    }
}
