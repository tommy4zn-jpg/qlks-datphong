package qlks.model;

import java.util.Date;

public class SuDungDichVu {
    private String maSD;
    private String maDatPhong;
    private String maDV;
    private int soLuong;
    private Date ngaySuDung;
    private double thanhTien;
    private String tenDV;
    private double donGia;

    public SuDungDichVu() {
    }

    public double tinhThanhTien() {
        this.thanhTien = donGia * soLuong;
        return this.thanhTien;
    }

    public String getMaSD() { return maSD; }
    public void setMaSD(String maSD) { this.maSD = maSD; }
    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }
    public String getMaDV() { return maDV; }
    public void setMaDV(String maDV) { this.maDV = maDV; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public Date getNgaySuDung() { return ngaySuDung; }
    public void setNgaySuDung(Date ngaySuDung) { this.ngaySuDung = ngaySuDung; }
    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
    public String getTenDV() { return tenDV; }
    public void setTenDV(String tenDV) { this.tenDV = tenDV; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
}
