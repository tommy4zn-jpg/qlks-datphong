package qlks.model;

import java.util.Date;

public class DanhGia {
    private String maDG;
    private String maKH;
    private String maPhong;
    private int diem;
    private String binhLuan;
    private Date ngayDanhGia;
    private String tenKhachHang;

    public DanhGia() {
    }

    public String getMaDG() { return maDG; }
    public void setMaDG(String maDG) { this.maDG = maDG; }
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public int getDiem() { return diem; }
    public void setDiem(int diem) { this.diem = diem; }
    public String getBinhLuan() { return binhLuan; }
    public void setBinhLuan(String binhLuan) { this.binhLuan = binhLuan; }
    public Date getNgayDanhGia() { return ngayDanhGia; }
    public void setNgayDanhGia(Date ngayDanhGia) { this.ngayDanhGia = ngayDanhGia; }
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
}
