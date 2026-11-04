package qlks.model;

import java.util.Date;

public class HoaDon {
    private String maHD;
    private String maDatPhong;
    private Date ngayLap;
    private double tongTienPhong;
    private double tongTienDV;
    private double giamGia;
    private double thue;
    private double tongThanhToan;

    public HoaDon() {
    }

    public double tinhTongThanhToan() {
        double truocThue = tongTienPhong + tongTienDV - giamGia;
        this.thue = truocThue * 0.1;
        this.tongThanhToan = truocThue + this.thue;
        return this.tongThanhToan;
    }

    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }
    public Date getNgayLap() { return ngayLap; }
    public void setNgayLap(Date ngayLap) { this.ngayLap = ngayLap; }
    public double getTongTienPhong() { return tongTienPhong; }
    public void setTongTienPhong(double tongTienPhong) { this.tongTienPhong = tongTienPhong; }
    public double getTongTienDV() { return tongTienDV; }
    public void setTongTienDV(double tongTienDV) { this.tongTienDV = tongTienDV; }
    public double getGiamGia() { return giamGia; }
    public void setGiamGia(double giamGia) { this.giamGia = giamGia; }
    public double getThue() { return thue; }
    public void setThue(double thue) { this.thue = thue; }
    public double getTongThanhToan() { return tongThanhToan; }
    public void setTongThanhToan(double tongThanhToan) { this.tongThanhToan = tongThanhToan; }
}
