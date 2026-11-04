package qlks.model;

public class ChiTietDatPhong {
    private String maCT;
    private String maDatPhong;
    private String maPhong;
    private double giaPhong;
    private int soDem;
    private double thanhTien;
    private String soPhong;

    public ChiTietDatPhong() {
    }

    public double tinhThanhTien() {
        this.thanhTien = giaPhong * soDem;
        return this.thanhTien;
    }

    public String getMaCT() { return maCT; }
    public void setMaCT(String maCT) { this.maCT = maCT; }
    public String getMaDatPhong() { return maDatPhong; }
    public void setMaDatPhong(String maDatPhong) { this.maDatPhong = maDatPhong; }
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public double getGiaPhong() { return giaPhong; }
    public void setGiaPhong(double giaPhong) { this.giaPhong = giaPhong; }
    public int getSoDem() { return soDem; }
    public void setSoDem(int soDem) { this.soDem = soDem; }
    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }
}
