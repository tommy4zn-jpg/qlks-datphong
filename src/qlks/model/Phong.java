package qlks.model;

public class Phong {
    private String maPhong;
    private String soPhong;
    private int tang;
    private String trangThai;
    private String maLoaiPhong;
    private LoaiPhong loaiPhong;

    public Phong() {
    }

    public Phong(String maPhong, String soPhong, int tang, String trangThai) {
        this.maPhong = maPhong;
        this.soPhong = soPhong;
        this.tang = tang;
        this.trangThai = trangThai;
    }

    public void capNhatTrangThai(String tt) {
        this.trangThai = tt;
    }

    public boolean kiemTraTrong() {
        return "TRONG".equalsIgnoreCase(trangThai);
    }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }
    public int getTang() { return tang; }
    public void setTang(int tang) { this.tang = tang; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getMaLoaiPhong() { return maLoaiPhong; }
    public void setMaLoaiPhong(String maLoaiPhong) { this.maLoaiPhong = maLoaiPhong; }
    public LoaiPhong getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(LoaiPhong loaiPhong) { this.loaiPhong = loaiPhong; }

    @Override
    public String toString() {
        return "Phòng " + soPhong + (loaiPhong != null ? " - " + loaiPhong.getTenLoai() : "");
    }
}
