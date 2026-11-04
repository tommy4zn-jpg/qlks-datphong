DROP DATABASE IF EXISTS qlks_datphong;
CREATE DATABASE qlks_datphong CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE qlks_datphong;

CREATE TABLE khachhang (
    maKH VARCHAR(10) PRIMARY KEY,
    hoTen VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    soDienThoai VARCHAR(15),
    tenDangNhap VARCHAR(50) UNIQUE NOT NULL,
    matKhau VARCHAR(100) NOT NULL,
    trangThai TINYINT(1) DEFAULT 1,
    cccd VARCHAR(20),
    diaChi VARCHAR(200),
    diemTichLuy INT DEFAULT 0
);

CREATE TABLE nhanvien (
    maNV VARCHAR(10) PRIMARY KEY,
    hoTen VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    soDienThoai VARCHAR(15),
    tenDangNhap VARCHAR(50) UNIQUE NOT NULL,
    matKhau VARCHAR(100) NOT NULL,
    trangThai TINYINT(1) DEFAULT 1,
    chucVu VARCHAR(20) NOT NULL,
    ngayVaoLam DATE,
    caLamViec VARCHAR(30),
    phongBan VARCHAR(50)
);

CREATE TABLE loaiphong (
    maLoaiPhong VARCHAR(10) PRIMARY KEY,
    tenLoai VARCHAR(50) NOT NULL,
    moTa VARCHAR(255),
    giaCoBan DOUBLE NOT NULL,
    sucChua INT DEFAULT 2,
    tienNghi VARCHAR(255)
);

CREATE TABLE phong (
    maPhong VARCHAR(10) PRIMARY KEY,
    soPhong VARCHAR(10) NOT NULL,
    tang INT,
    trangThai VARCHAR(20) DEFAULT 'TRONG',
    maLoaiPhong VARCHAR(10),
    FOREIGN KEY (maLoaiPhong) REFERENCES loaiphong(maLoaiPhong)
);

CREATE TABLE khuyenmai (
    maKM VARCHAR(10) PRIMARY KEY,
    tenKM VARCHAR(100) NOT NULL,
    phanTramGiam DOUBLE DEFAULT 0,
    ngayBatDau DATE,
    ngayKetThuc DATE,
    dieuKien VARCHAR(255)
);

CREATE TABLE dichvu (
    maDV VARCHAR(10) PRIMARY KEY,
    tenDV VARCHAR(100) NOT NULL,
    donGia DOUBLE NOT NULL,
    moTa VARCHAR(255)
);

CREATE TABLE datphong (
    maDatPhong VARCHAR(10) PRIMARY KEY,
    maKH VARCHAR(10),
    maNV VARCHAR(10),
    ngayDat DATETIME,
    ngayNhan DATE,
    ngayTra DATE,
    soKhach INT DEFAULT 1,
    tongTien DOUBLE DEFAULT 0,
    trangThai VARCHAR(20) DEFAULT 'CHO_XAC_NHAN',
    ghiChu VARCHAR(255),
    maKM VARCHAR(10),
    FOREIGN KEY (maKH) REFERENCES khachhang(maKH),
    FOREIGN KEY (maNV) REFERENCES nhanvien(maNV),
    FOREIGN KEY (maKM) REFERENCES khuyenmai(maKM)
);

CREATE TABLE chitietdatphong (
    maCT VARCHAR(10) PRIMARY KEY,
    maDatPhong VARCHAR(10),
    maPhong VARCHAR(10),
    giaPhong DOUBLE,
    soDem INT,
    thanhTien DOUBLE,
    FOREIGN KEY (maDatPhong) REFERENCES datphong(maDatPhong),
    FOREIGN KEY (maPhong) REFERENCES phong(maPhong)
);

CREATE TABLE sudungdichvu (
    maSD VARCHAR(10) PRIMARY KEY,
    maDatPhong VARCHAR(10),
    maDV VARCHAR(10),
    soLuong INT DEFAULT 1,
    ngaySuDung DATE,
    thanhTien DOUBLE,
    FOREIGN KEY (maDatPhong) REFERENCES datphong(maDatPhong),
    FOREIGN KEY (maDV) REFERENCES dichvu(maDV)
);

CREATE TABLE hoadon (
    maHD VARCHAR(10) PRIMARY KEY,
    maDatPhong VARCHAR(10),
    ngayLap DATETIME,
    tongTienPhong DOUBLE DEFAULT 0,
    tongTienDV DOUBLE DEFAULT 0,
    giamGia DOUBLE DEFAULT 0,
    thue DOUBLE DEFAULT 0,
    tongThanhToan DOUBLE DEFAULT 0,
    FOREIGN KEY (maDatPhong) REFERENCES datphong(maDatPhong)
);

CREATE TABLE thanhtoan (
    maTT VARCHAR(10) PRIMARY KEY,
    maHD VARCHAR(10),
    soTien DOUBLE,
    phuongThuc VARCHAR(30),
    ngayThanhToan DATETIME,
    maGiaoDich VARCHAR(50),
    trangThai VARCHAR(20) DEFAULT 'THANH_CONG',
    FOREIGN KEY (maHD) REFERENCES hoadon(maHD)
);

CREATE TABLE danhgia (
    maDG VARCHAR(10) PRIMARY KEY,
    maKH VARCHAR(10),
    maPhong VARCHAR(10),
    diem INT,
    binhLuan VARCHAR(255),
    ngayDanhGia DATE,
    FOREIGN KEY (maKH) REFERENCES khachhang(maKH),
    FOREIGN KEY (maPhong) REFERENCES phong(maPhong)
);

INSERT INTO nhanvien VALUES
('NV01','Trần Quản Lý','ql@khachsan.vn','0901000001','admin','123',1,'QUANLY','2022-01-10',NULL,'Điều hành'),
('NV02','Nguyễn Thị Lễ Tân','letan@khachsan.vn','0901000002','letan','123',1,'LETAN','2023-05-20','Ca sáng',NULL),
('NV03','Phạm Văn Tiếp Tân','letan2@khachsan.vn','0901000003','letan2','123',1,'LETAN','2023-08-15','Ca chiều',NULL);

INSERT INTO khachhang VALUES
('KH01','Lê Văn An','an.le@gmail.com','0911111111','an','123',1,'079201001234','12 Lê Lợi, Q1, TP.HCM',120),
('KH02','Trần Thị Bình','binh.tran@gmail.com','0922222222','binh','123',1,'079201005678','45 Trần Hưng Đạo, Q5, TP.HCM',50),
('KH03','Hoàng Minh Châu','chau.hm@gmail.com','0933333333','chau','123',1,'079201009999','78 Nguyễn Huệ, Q1, TP.HCM',0);

INSERT INTO loaiphong VALUES
('LP01','Standard','Phòng tiêu chuẩn 1 giường đôi',500000,2,'Điều hòa, TV, WiFi, Tủ lạnh mini'),
('LP02','Deluxe','Phòng cao cấp hướng thành phố',900000,3,'Điều hòa, Smart TV, WiFi, Bồn tắm, Minibar'),
('LP03','Suite','Phòng hạng sang có phòng khách riêng',1800000,4,'Điều hòa, Smart TV, WiFi, Bồn tắm, Bếp nhỏ, Ban công'),
('LP04','Family','Phòng gia đình 2 giường đôi',1300000,5,'Điều hòa, TV, WiFi, Tủ lạnh, Sofa');

INSERT INTO phong VALUES
('P101','101',1,'TRONG','LP01'),
('P102','102',1,'TRONG','LP01'),
('P103','103',1,'TRONG','LP02'),
('P201','201',2,'TRONG','LP02'),
('P202','202',2,'TRONG','LP03'),
('P203','203',2,'DANG_O','LP01'),
('P301','301',3,'TRONG','LP03'),
('P302','302',3,'TRONG','LP04'),
('P303','303',3,'BAO_TRI','LP04'),
('P401','401',4,'TRONG','LP04');

INSERT INTO khuyenmai VALUES
('KM01','Chào hè 2026',15,'2026-06-01','2026-08-31','Đặt từ 2 đêm trở lên'),
('KM02','Khách thân thiết',10,'2026-01-01','2026-12-31','Điểm tích lũy trên 100'),
('KM03','Đặt sớm',20,'2026-01-01','2026-12-31','Đặt trước 15 ngày');

INSERT INTO dichvu VALUES
('DV01','Ăn sáng buffet',120000,'Buffet sáng tại nhà hàng tầng 1'),
('DV02','Giặt ủi',80000,'Dịch vụ giặt ủi theo kg'),
('DV03','Thuê xe sân bay',350000,'Đưa đón sân bay Tân Sơn Nhất'),
('DV04','Spa & Massage',450000,'Gói thư giãn 60 phút'),
('DV05','Minibar',150000,'Đồ uống và snack trong phòng');

INSERT INTO datphong VALUES
('DP01','KH01','NV02','2026-10-01 09:30:00','2026-10-05','2026-10-07',2,1000000,'DA_TRA_PHONG','Khách quen',NULL),
('DP02','KH02','NV02','2026-11-02 14:00:00','2026-11-10','2026-11-12',2,1800000,'DA_XAC_NHAN',NULL,'KM01');

INSERT INTO chitietdatphong VALUES
('CT01','DP01','P101',500000,2,1000000),
('CT02','DP02','P103',900000,2,1800000);

INSERT INTO sudungdichvu VALUES
('SD01','DP01','DV01',2,'2026-10-06',240000);

INSERT INTO hoadon VALUES
('HD01','DP01','2026-10-07 11:00:00',1000000,240000,0,124000,1364000);

INSERT INTO thanhtoan VALUES
('TT01','HD01',1364000,'Tiền mặt','2026-10-07 11:05:00','GD20261007001','THANH_CONG');

INSERT INTO danhgia VALUES
('DG01','KH01','P101',5,'Phòng sạch, nhân viên thân thiện','2026-10-07');
