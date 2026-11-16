package entity;

import java.time.LocalDate;
import java.util.Objects;

public class HoaDon {
    private String maHoaDon;
    private LocalDate ngayLapHD;
    private double donGia;
    private KhachHang khachHang;  
    private NhanVien nhanVien;     

    public HoaDon() {
    }

    public HoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public HoaDon(String maHoaDon, LocalDate ngayLapHD, double donGia,
                  KhachHang khachHang, NhanVien nhanVien) {
        this.maHoaDon = maHoaDon;
        this.ngayLapHD = ngayLapHD;
        this.donGia = donGia;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
    }

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }

    public LocalDate getNgayLapHD() { return ngayLapHD; }
    public void setNgayLapHD(LocalDate ngayLapHD) { this.ngayLapHD = ngayLapHD; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }

    // ThanhToan() : Double - trả về số tiền cần thanh toán
    public double thanhToan() {
        return donGia;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHoaDon);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HoaDon other = (HoaDon) obj;
        return Objects.equals(maHoaDon, other.maHoaDon);
    }

    @Override
    public String toString() {
        return "HoaDon [maHoaDon=" + maHoaDon + ", ngayLapHD=" + ngayLapHD + ", donGia=" + donGia
                + ", khachHang=" + (khachHang == null ? null : khachHang.getMaKhachHang())
                + ", nhanVien=" + (nhanVien == null ? null : nhanVien.getMaNV()) + "]";
    }
}