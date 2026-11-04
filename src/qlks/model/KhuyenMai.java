package qlks.model;

import java.util.Date;

public class KhuyenMai {
    private String maKM;
    private String tenKM;
    private double phanTramGiam;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String dieuKien;

    public KhuyenMai() {
    }

    public KhuyenMai(String maKM, String tenKM, double phanTramGiam) {
        this.maKM = maKM;
        this.tenKM = tenKM;
        this.phanTramGiam = phanTramGiam;
    }

    public boolean kiemTraHopLe(Date ngay) {
        if (ngay == null) return false;
        boolean sauBatDau = ngayBatDau == null || !ngay.before(ngayBatDau);
        boolean truocKetThuc = ngayKetThuc == null || !ngay.after(ngayKetThuc);
        return sauBatDau && truocKetThuc;
    }

    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }
    public String getTenKM() { return tenKM; }
    public void setTenKM(String tenKM) { this.tenKM = tenKM; }
    public double getPhanTramGiam() { return phanTramGiam; }
    public void setPhanTramGiam(double phanTramGiam) { this.phanTramGiam = phanTramGiam; }
    public Date getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(Date ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public Date getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(Date ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public String getDieuKien() { return dieuKien; }
    public void setDieuKien(String dieuKien) { this.dieuKien = dieuKien; }

    @Override
    public String toString() {
        return tenKM + " (-" + (int) phanTramGiam + "%)";
    }
}
