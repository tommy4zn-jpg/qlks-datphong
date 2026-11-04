package qlks.model;

public class QuanLy extends NhanVien {
    private String phongBan;

    public QuanLy() {
    }

    public QuanLy(String maNguoiDung, String hoTen, String tenDangNhap, String matKhau) {
        super(maNguoiDung, hoTen, tenDangNhap, matKhau);
    }

    @Override
    public String getVaiTro() {
        return "Quản lý";
    }

    @Override
    public String moTaQuyen() {
        return "Quản lý phòng, loại phòng, dịch vụ, khuyến mãi, nhân viên và xem báo cáo";
    }

    public String getPhongBan() { return phongBan; }
    public void setPhongBan(String phongBan) { this.phongBan = phongBan; }
}
