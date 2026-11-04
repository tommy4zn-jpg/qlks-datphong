package qlks.model;

public class KhachHang extends NguoiDung {
    private String cccd;
    private String diaChi;
    private int diemTichLuy;

    public KhachHang() {
    }

    public KhachHang(String maNguoiDung, String hoTen, String tenDangNhap, String matKhau) {
        super(maNguoiDung, hoTen, tenDangNhap, matKhau);
    }

    @Override
    public String getVaiTro() {
        return "Khách hàng";
    }

    @Override
    public String moTaQuyen() {
        return "Tìm phòng, đặt phòng, huỷ đặt, xem lịch sử, đánh giá";
    }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }
}
