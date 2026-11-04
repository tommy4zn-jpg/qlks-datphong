package qlks.model;

public abstract class NguoiDung {
    private String maNguoiDung;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String tenDangNhap;
    private String matKhau;
    private boolean trangThai;

    public NguoiDung() {
    }

    public NguoiDung(String maNguoiDung, String hoTen, String tenDangNhap, String matKhau) {
        this.maNguoiDung = maNguoiDung;
        this.hoTen = hoTen;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.trangThai = true;
    }

    public abstract String getVaiTro();

    public abstract String moTaQuyen();

    public boolean dangNhap(String tenDangNhap, String matKhau) {
        return this.tenDangNhap != null && this.tenDangNhap.equals(tenDangNhap)
                && this.matKhau != null && this.matKhau.equals(matKhau) && this.trangThai;
    }

    public void dangXuat() {
    }

    public boolean doiMatKhau(String matKhauCu, String matKhauMoi) {
        if (this.matKhau != null && this.matKhau.equals(matKhauCu)) {
            this.matKhau = matKhauMoi;
            return true;
        }
        return false;
    }

    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public boolean isTrangThai() { return trangThai; }
    public void setTrangThai(boolean trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return hoTen + " (" + getVaiTro() + ")";
    }
}
