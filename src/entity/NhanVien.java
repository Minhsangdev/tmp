package entity;

import java.time.LocalDate;
import java.util.Objects;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String soDienThoai;
    private String email;
    private double luong;
    private LocalDate ngayLamViec;

    public NhanVien() {
    }

    public NhanVien(String maNV) {
        this.maNV = maNV;
    }

    public NhanVien(String maNV, String tenNV, String soDienThoai, String email,
                    double luong, LocalDate ngayLamViec) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.luong = luong;
        this.ngayLamViec = ngayLamViec;
    }

    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    public LocalDate getNgayLamViec() { return ngayLamViec; }
    public void setNgayLamViec(LocalDate ngayLamViec) { this.ngayLamViec = ngayLamViec; }

    @Override
    public int hashCode() {
        return Objects.hash(maNV);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NhanVien other = (NhanVien) obj;
        return Objects.equals(maNV, other.maNV);
    }

    @Override
    public String toString() {
        return "NhanVien [maNV=" + maNV + ", tenNV=" + tenNV + ", soDienThoai=" + soDienThoai
                + ", email=" + email + ", luong=" + luong + ", ngayLamViec=" + ngayLamViec + "]";
    }
}