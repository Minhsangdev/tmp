package entity;

/**
 * Mo hinh SanPham theo CSDL QLBH.
 * Kiem tra du lieu truoc khi ghi duoc thuc hien trong DAO.
 */
public class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private String donViTinh;
    private double gia;
    private NhomSanPham nhomSanPham;
    private NhaCungCap nhaCungCap;

    public SanPham() {
    }

    public SanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public SanPham(String maSanPham, String tenSanPham, String donViTinh, double gia,
            NhomSanPham nhomSanPham, NhaCungCap nhaCungCap) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.donViTinh = donViTinh;
        this.gia = gia;
        this.nhomSanPham = nhomSanPham;
        this.nhaCungCap = nhaCungCap;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public NhomSanPham getNhomSanPham() {
        return nhomSanPham;
    }

    public void setNhomSanPham(NhomSanPham nhomSanPham) {
        this.nhomSanPham = nhomSanPham;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    @Override
    public String toString() {
        return "SanPham [maSanPham=" + maSanPham
                + ", tenSanPham=" + tenSanPham
                + ", donViTinh=" + donViTinh
                + ", gia=" + gia
                + ", nhomSanPham=" + nhomSanPham
                + ", nhaCungCap=" + nhaCungCap
                + "]";
    }
}
