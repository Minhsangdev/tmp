
IF DB_ID(N'QLBH') IS NULL
    EXEC(N'CREATE DATABASE QLBH');
GO

USE QLBH;
GO

-- 1. BANG NHOM SAN PHAM
CREATE TABLE NhomSanPham (
    maNhom VARCHAR(10) PRIMARY KEY,
    tenNhom NVARCHAR(100)
);

-- 2. BANG NHA CUNG CAP
CREATE TABLE NhaCungCap (
    maNCC VARCHAR(10) PRIMARY KEY,
    tenNCC NVARCHAR(100),
    diaChi NVARCHAR(200),
    soDienThoai VARCHAR(15)
);

-- 3. BANG KHACH HANG
CREATE TABLE KhachHang (
    maKhachHang VARCHAR(10) PRIMARY KEY,
    tenKhachHang NVARCHAR(100),
    soDienThoai VARCHAR(15),
    email VARCHAR(100)
);

-- 4. BANG NHAN VIEN
CREATE TABLE NhanVien (
    maNV VARCHAR(10) PRIMARY KEY,
    tenNV NVARCHAR(100),
    soDienThoai VARCHAR(15),
    email VARCHAR(100),
    luong FLOAT,
    ngayLamViec DATE
);

-- 5. BANG SAN PHAM
CREATE TABLE SanPham (
    maSanPham VARCHAR(10) PRIMARY KEY,
    tenSanPham NVARCHAR(100),
    donViTinh NVARCHAR(50),
    gia FLOAT,
    maNhomSanPham VARCHAR(10),
    maNCC VARCHAR(10)
);

-- 6. BANG HOA DON
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(10) PRIMARY KEY,
    ngayLapHD NVARCHAR(20),
    donGia FLOAT,
    maKhachHang VARCHAR(10),
    maNhanVien VARCHAR(10)
);

-- 7. BANG CHI TIET HOA DON
CREATE TABLE ChiTietHoaDon (
    maHoaDon VARCHAR(10),
    maSanPham VARCHAR(10),
    ngayLapHoaDon DATE,
    PRIMARY KEY (maHoaDon, maSanPham)
);
GO

-- TAO KHOA NGOAI

-- SanPham -> NhomSanPham
ALTER TABLE SanPham
ADD CONSTRAINT FK_SP_Nhom
FOREIGN KEY (maNhomSanPham)
REFERENCES NhomSanPham(maNhom);

-- SanPham -> NhaCungCap
ALTER TABLE SanPham
ADD CONSTRAINT FK_SP_NCC
FOREIGN KEY (maNCC)
REFERENCES NhaCungCap(maNCC);

-- HoaDon -> KhachHang
ALTER TABLE HoaDon
ADD CONSTRAINT FK_HD_KH
FOREIGN KEY (maKhachHang)
REFERENCES KhachHang(maKhachHang);

-- HoaDon -> NhanVien
ALTER TABLE HoaDon
ADD CONSTRAINT FK_HD_NV
FOREIGN KEY (maNhanVien)
REFERENCES NhanVien(maNV);

-- ChiTietHoaDon -> HoaDon
ALTER TABLE ChiTietHoaDon
ADD CONSTRAINT FK_CTHD_HD
FOREIGN KEY (maHoaDon)
REFERENCES HoaDon(maHoaDon);

-- ChiTietHoaDon -> SanPham
ALTER TABLE ChiTietHoaDon
ADD CONSTRAINT FK_CTHD_SP
FOREIGN KEY (maSanPham)
REFERENCES SanPham(maSanPham);
GO
