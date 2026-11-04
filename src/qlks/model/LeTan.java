package qlks.model;

public class LeTan extends NhanVien {
    private String caLamViec;

    public LeTan() {
    }

    public LeTan(String maNguoiDung, String hoTen, String tenDangNhap, String matKhau) {
        super(maNguoiDung, hoTen, tenDangNhap, matKhau);
    }

    @Override
    public String getVaiTro() {
        return "Lễ tân";
    }

    @Override
    public String moTaQuyen() {
        return "Xác nhận đặt phòng, check-in, check-out, lập hoá đơn, đặt phòng trực tiếp";
    }

    public String getCaLamViec() { return caLamViec; }
    public void setCaLamViec(String caLamViec) { this.caLamViec = caLamViec; }
}
