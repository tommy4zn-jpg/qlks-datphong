package qlks.model;

import java.util.Date;

public class ThanhToan {
    private String maTT;
    private String maHD;
    private double soTien;
    private String phuongThuc;
    private Date ngayThanhToan;
    private String maGiaoDich;
    private String trangThai;

    public ThanhToan() {
    }

    public ThanhToan(String maTT, String maHD, double soTien, String phuongThuc) {
        this.maTT = maTT;
        this.maHD = maHD;
        this.soTien = soTien;
        this.phuongThuc = phuongThuc;
    }

    public String getMaTT() { return maTT; }
    public void setMaTT(String maTT) { this.maTT = maTT; }
    public String getMaHD() { return maHD; }
    public void setMaHD(String maHD) { this.maHD = maHD; }
    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }
    public String getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }
    public Date getNgayThanhToan() { return ngayThanhToan; }
    public void setNgayThanhToan(Date ngayThanhToan) { this.ngayThanhToan = ngayThanhToan; }
    public String getMaGiaoDich() { return maGiaoDich; }
    public void setMaGiaoDich(String maGiaoDich) { this.maGiaoDich = maGiaoDich; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
