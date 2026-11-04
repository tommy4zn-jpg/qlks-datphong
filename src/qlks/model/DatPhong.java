package qlks.model;

import java.util.Date;

public class DatPhong {
    private String maDatPhong;
    private String maKH;
    private String maNV;
    private Date ngayDat;
    private Date ngayNhan;
    private Date ngayTra;
    private int soKhach;
    private double tongTien;
    private String trangThai;
    private String ghiChu;
    private String maKM;
    private String tenKhachHang;

    public DatPhong() {
    }

    public double tinhTongTien(double giaPhong, int soDem, double phanTramGiam) {
        double thanhTien = giaPhong * soDem;
        double giam = thanhTien * phanTramGiam / 100.0;
        this.tongTien = thanhTien - giam;
        return this.tongTien;
    }

    public void capNhatTrangThai(String tt) {
        this.trangThai = tt;
    }

    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public Date getNgayDat() { return ngayDat; }
    public void setNgayDat(Date ngayDat) { this.ngayDat = ngayDat; }
    public Date getNgayNhan() { return ngayNhan; }
    public void setNgayNhan(Date ngayNhan) { this.ngayNhan = ngayNhan; }
    public Date getNgayTra() { return ngayTra; }
    public void setNgayTra(Date ngayTra) { this.ngayTra = ngayTra; }
    public int getSoKhach() { return soKhach; }
    public void setSoKhach(int soKhach) { this.soKhach = soKhach; }
    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
}
