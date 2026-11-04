package qlks.model;

import java.util.Date;

public abstract class NhanVien extends NguoiDung {
    private String chucVu;
    private Date ngayVaoLam;

    public NhanVien() {
    }

    public NhanVien(String maNguoiDung, String hoTen, String tenDangNhap, String matKhau) {
        super(maNguoiDung, hoTen, tenDangNhap, matKhau);
    }

    public void capNhatThongTin(String email, String sdt) {
        setEmail(email);
        setSoDienThoai(sdt);
    }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }
    public Date getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(Date ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }
}
