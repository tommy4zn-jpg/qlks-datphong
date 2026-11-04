package qlks.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import qlks.exception.QLKSException;
import qlks.model.LoaiPhong;
import qlks.model.Phong;
import qlks.util.KetNoiCSDL;

public class PhongDAO implements IDAO<Phong> {

    private static final String SELECT_BASE =
        "SELECT p.*, l.tenLoai, l.giaCoBan, l.sucChua, l.moTa AS moTaLoai, l.tienNghi " +
        "FROM phong p LEFT JOIN loaiphong l ON p.maLoaiPhong=l.maLoaiPhong";

    @Override
    public List<Phong> layTatCa() throws QLKSException {
        List<Phong> ds = new ArrayList<>();
        String sql = SELECT_BASE + " ORDER BY p.maPhong";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(doc(rs));
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tải danh sách phòng: " + e.getMessage());
        }
        return ds;
    }

    public List<Phong> timPhongTrong(int soKhach) throws QLKSException {
        List<Phong> ds = new ArrayList<>();
        String sql = SELECT_BASE + " WHERE p.trangThai='TRONG' AND l.sucChua>=? ORDER BY l.giaCoBan";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, soKhach);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(doc(rs));
                }
            }
        } catch (Exception e) {
            throw new QLKSException("Lỗi tìm phòng trống: " + e.getMessage());
        }
        return ds;
    }

    private Phong doc(ResultSet rs) throws java.sql.SQLException {
        Phong p = new Phong();
        p.setMaPhong(rs.getString("maPhong"));
        p.setSoPhong(rs.getString("soPhong"));
        p.setTang(rs.getInt("tang"));
        p.setTrangThai(rs.getString("trangThai"));
        p.setMaLoaiPhong(rs.getString("maLoaiPhong"));
        LoaiPhong lp = new LoaiPhong();
        lp.setMaLoaiPhong(rs.getString("maLoaiPhong"));
        lp.setTenLoai(rs.getString("tenLoai"));
        lp.setGiaCoBan(rs.getDouble("giaCoBan"));
        lp.setSucChua(rs.getInt("sucChua"));
        lp.setMoTa(rs.getString("moTaLoai"));
        lp.setTienNghi(rs.getString("tienNghi"));
        p.setLoaiPhong(lp);
        return p;
    }

    @Override
    public boolean them(Phong p) throws QLKSException {
        String sql = "INSERT INTO phong(maPhong,soPhong,tang,trangThai,maLoaiPhong) VALUES(?,?,?,?,?)";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getSoPhong());
            ps.setInt(3, p.getTang());
            ps.setString(4, p.getTrangThai());
            ps.setString(5, p.getMaLoaiPhong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi thêm phòng: " + e.getMessage());
        }
    }

    @Override
    public boolean capNhat(Phong p) throws QLKSException {
        String sql = "UPDATE phong SET soPhong=?,tang=?,trangThai=?,maLoaiPhong=? WHERE maPhong=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getSoPhong());
            ps.setInt(2, p.getTang());
            ps.setString(3, p.getTrangThai());
            ps.setString(4, p.getMaLoaiPhong());
            ps.setString(5, p.getMaPhong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi cập nhật phòng: " + e.getMessage());
        }
    }

    public boolean capNhatTrangThai(String maPhong, String trangThai) throws QLKSException {
        String sql = "UPDATE phong SET trangThai=? WHERE maPhong=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setString(2, maPhong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Lỗi cập nhật trạng thái phòng: " + e.getMessage());
        }
    }

    @Override
    public boolean xoa(String ma) throws QLKSException {
        String sql = "DELETE FROM phong WHERE maPhong=?";
        try (Connection cn = KetNoiCSDL.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, ma);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            throw new QLKSException("Không thể xoá (phòng đã có dữ liệu đặt).");
        }
    }
}
