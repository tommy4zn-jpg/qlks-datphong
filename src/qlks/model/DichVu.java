package qlks.model;

public class DichVu {
    private String maDV;
    private String tenDV;
    private double donGia;
    private String moTa;

    public DichVu() {
    }

    public DichVu(String maDV, String tenDV, double donGia) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.donGia = donGia;
    }

    public String getMaDV() { return maDV; }
    public void setMaDV(String maDV) { this.maDV = maDV; }
    public String getTenDV() { return tenDV; }
    public void setTenDV(String tenDV) { this.tenDV = tenDV; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @Override
    public String toString() {
        return tenDV;
    }
}
