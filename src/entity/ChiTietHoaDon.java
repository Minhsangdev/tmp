package entity;

import java.time.LocalDate;

public class ChiTietHoaDon {
	private HoaDon maHoaDon;
	private SanPham maSanPham;
	private LocalDate ngayLapHoaDon;
	public HoaDon getMaHoaDon() {
		return maHoaDon;
	}
	public void setMaHoaDon(HoaDon maHoaDon) {
		this.maHoaDon = maHoaDon;
	}
	public SanPham getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(SanPham maSanPham) {
		this.maSanPham = maSanPham;
	}
	public LocalDate getNgayLapHoaDon() {
		return ngayLapHoaDon;
	}
	public void setNgayLapHoaDon(LocalDate ngayLapHoaDon) {
		this.ngayLapHoaDon = ngayLapHoaDon;
	}
	public ChiTietHoaDon(HoaDon maHoaDon, SanPham maSanPham, LocalDate ngayLapHoaDon) {
		super();
		this.maHoaDon = maHoaDon;
		this.maSanPham = maSanPham;
		this.ngayLapHoaDon = ngayLapHoaDon;
	}
	public ChiTietHoaDon(HoaDon maHoaDon) {
		super();
		this.maHoaDon = maHoaDon;
	}
	
	
	
}
