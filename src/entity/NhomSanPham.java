package entity;

/**
 * Mo hinh NhomSanPham theo CSDL QLBH.
 * Kiem tra du lieu truoc khi ghi duoc thuc hien trong DAO.
 */
public class NhomSanPham {
    private String maNhom;
    private String tenNhom;

    public NhomSanPham() {
    }

    public NhomSanPham(String maNhom) {
        this.maNhom = maNhom;
    }

    public NhomSanPham(String maNhom, String tenNhom) {
        this.maNhom = maNhom;
        this.tenNhom = tenNhom;
    }

    public String getMaNhom() {
        return maNhom;
    }

    public void setMaNhom(String maNhom) {
        this.maNhom = maNhom;
    }

    public String getTenNhom() {
        return tenNhom;
    }

    public void setTenNhom(String tenNhom) {
        this.tenNhom = tenNhom;
    }

    @Override
    public String toString() {
        return "NhomSanPham [maNhom=" + maNhom
                + ", tenNhom=" + tenNhom
                + "]";
    }
}
