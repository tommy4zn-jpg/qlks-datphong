package qlks.model;

public class LoaiPhong {
    private String maLoaiPhong;
    private String tenLoai;
    private String moTa;
    private double giaCoBan;
    private int sucChua;
    private String tienNghi;

    public LoaiPhong() {
    }

    public LoaiPhong(String maLoaiPhong, String tenLoai, double giaCoBan, int sucChua) {
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoai = tenLoai;
        this.giaCoBan = giaCoBan;
        this.sucChua = sucChua;
    }

    public void capNhatGia(double gia) {
        this.giaCoBan = gia;
    }

    public String getMaLoaiPhong() { return maLoaiPhong; }
    public void setMaLoaiPhong(String maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public double getGiaCoBan() { return giaCoBan; }
    public void setGiaCoBan(double giaCoBan) { this.giaCoBan = giaCoBan; }
    public int getSucChua() { return sucChua; }
    public void setSucChua(int sucChua) { this.sucChua = sucChua; }
    public String getTienNghi() { return tienNghi; }
    public void setTienNghi(String tienNghi) { this.tienNghi = tienNghi; }

    @Override
    public String toString() {
        return tenLoai;
    }
}
