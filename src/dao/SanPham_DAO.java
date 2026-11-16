package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.SanPham;
import entity.NhaCungCap;
import entity.NhomSanPham;

/**
 * CRUD va tim kiem SanPham; khong phu thuoc GUI.
 * Truy van tra danh sach moi, rong neu khong tim thay (khong tra null).
 * create/update/delete tra true khi thay doi 1 dong; false neu khong co dong.
 * Du lieu dau vao sai: IllegalArgumentException.
 * Loi SQL: IllegalStateException, SQLException goc nam trong cause.
 * Ket noi/du lieu doc khong hop le cung duoc bao bang IllegalStateException.
 * Khong dung chung mot DAO/Connection dong thoi giua nhieu luong.
 */
public class SanPham_DAO {
    private static final String SELECT_SQL = "SELECT sp.maSanPham, sp.tenSanPham, sp.donViTinh, sp.gia, n.maNhom, n.tenNhom, "
                + "cc.maNCC, cc.tenNCC, cc.diaChi AS nccDiaChi, cc.soDienThoai AS nccSoDienThoai FROM "
                + "SanPham sp INNER JOIN NhomSanPham n ON sp.maNhomSanPham = n.maNhom INNER JOIN "
                + "NhaCungCap cc ON sp.maNCC = cc.maNCC";

    public ArrayList<SanPham> getAllSanPham() {
        return docDanhSach(SELECT_SQL + " ORDER BY sp.maSanPham");
    }

    /** Tim chinh xac theo ma; ket qua gom 0 hoac 1 phan tu. */
    public ArrayList<SanPham> getSanPhamTheoMa(String ma) {
        return docDanhSach(SELECT_SQL + " WHERE sp.maSanPham = ?",
                kiemTraChuoi(ma, "Ma", 10, true));
    }

    /** Tim ten co chua tu khoa. Chuoi rong tai lai tat ca ban ghi. */
    public ArrayList<SanPham> getSanPhamTheoTen(String ten) {
        if (ten != null && ten.trim().isEmpty()) {
            return getAllSanPham();
        }
        return docDanhSach(SELECT_SQL + " WHERE sp.tenSanPham LIKE ? ESCAPE '!' ORDER BY sp.maSanPham",
                taoMauTim(ten));
    }

    public ArrayList<SanPham> getSanPhamTheoNhomSanPham(String ma) {
        return docDanhSach(SELECT_SQL + " WHERE sp.maNhomSanPham = ? ORDER BY sp.maSanPham",
                kiemTraChuoi(ma, "Ma tham chieu", 10, true));
    }

    public ArrayList<SanPham> getSanPhamTheoNhaCungCap(String ma) {
        return docDanhSach(SELECT_SQL + " WHERE sp.maNCC = ? ORDER BY sp.maSanPham",
                kiemTraChuoi(ma, "Ma tham chieu", 10, true));
    }

    public boolean create(SanPham sp) {
        if (sp == null) {
            throw new IllegalArgumentException("SanPham khong duoc null.");
        }
        if (sp.getNhomSanPham() == null || sp.getNhaCungCap() == null) {
            throw new IllegalArgumentException("San pham phai co nhom va nha cung cap.");
        }
        if (!Double.isFinite(sp.getGia()) || sp.getGia() < 0) {
            throw new IllegalArgumentException("Gia phai la so huu han va khong am.");
        }
        String maSanPham = kiemTraChuoi(sp.getMaSanPham(), "Ma san pham", 10, true);
        String tenSanPham = kiemTraChuoi(sp.getTenSanPham(), "Ten san pham", 20, true);
        String donViTinh = kiemTraChuoi(sp.getDonViTinh(), "Don vi tinh", 20, true);
        double gia = sp.getGia();
        String maNhomSanPham = kiemTraChuoi(sp.getNhomSanPham().getMaNhom(), "Ma nhom", 10, true);
        String maNCC = kiemTraChuoi(sp.getNhaCungCap().getMaNCC(), "Ma nha cung cap", 10, true);
        String sql = "INSERT INTO SanPham (maSanPham, tenSanPham, donViTinh, gia, maNhomSanPham, maNCC) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = layKetNoi().prepareStatement(sql)) {
            stmt.setString(1, maSanPham);
            stmt.setString(2, tenSanPham);
            stmt.setString(3, donViTinh);
            stmt.setDouble(4, gia);
            stmt.setString(5, maNhomSanPham);
            stmt.setString(6, maNCC);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw loiCSDL("Them SanPham", e);
        }
    }

    public boolean update(SanPham sp) {
        if (sp == null) {
            throw new IllegalArgumentException("SanPham khong duoc null.");
        }
        if (sp.getNhomSanPham() == null || sp.getNhaCungCap() == null) {
            throw new IllegalArgumentException("San pham phai co nhom va nha cung cap.");
        }
        if (!Double.isFinite(sp.getGia()) || sp.getGia() < 0) {
            throw new IllegalArgumentException("Gia phai la so huu han va khong am.");
        }
        String maSanPham = kiemTraChuoi(sp.getMaSanPham(), "Ma san pham", 10, true);
        String tenSanPham = kiemTraChuoi(sp.getTenSanPham(), "Ten san pham", 20, true);
        String donViTinh = kiemTraChuoi(sp.getDonViTinh(), "Don vi tinh", 20, true);
        double gia = sp.getGia();
        String maNhomSanPham = kiemTraChuoi(sp.getNhomSanPham().getMaNhom(), "Ma nhom", 10, true);
        String maNCC = kiemTraChuoi(sp.getNhaCungCap().getMaNCC(), "Ma nha cung cap", 10, true);
        String sql = "UPDATE SanPham SET tenSanPham = ?, donViTinh = ?, gia = ?, maNhomSanPham = ?, maNCC = "
                + "? WHERE maSanPham = ?";
        try (PreparedStatement stmt = layKetNoi().prepareStatement(sql)) {
            stmt.setString(1, tenSanPham);
            stmt.setString(2, donViTinh);
            stmt.setDouble(3, gia);
            stmt.setString(4, maNhomSanPham);
            stmt.setString(5, maNCC);
            stmt.setString(6, maSanPham);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw loiCSDL("Cap nhat SanPham", e);
        }
    }

    /** Chi xoa ban ghi nay; de khoa ngoai ngan xoa khi dang duoc tham chieu. */
    public boolean delete(String ma) {
        String id = kiemTraChuoi(ma, "Ma", 10, true);
        String sql = "DELETE FROM SanPham WHERE maSanPham = ?";
        try (PreparedStatement stmt = layKetNoi().prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw loiCSDL("Xoa SanPham", e);
        }
    }

    private ArrayList<SanPham> docDanhSach(String sql, String... thamSo) {
        ArrayList<SanPham> ds = new ArrayList<SanPham>();
        try (PreparedStatement stmt = layKetNoi().prepareStatement(sql)) {
            for (int i = 0; i < thamSo.length; i++) {
                stmt.setString(i + 1, thamSo[i]);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ds.add(docBanGhi(rs));
                }
            }
        } catch (SQLException e) {
            throw loiCSDL("Doc SanPham", e);
        }
        return ds;
    }

    private SanPham docBanGhi(ResultSet rs) throws SQLException {
        NhomSanPham nhom = new NhomSanPham(docChuoiChar(rs.getString("maNhom")),
                rs.getString("tenNhom"));
        NhaCungCap ncc = new NhaCungCap(docChuoiChar(rs.getString("maNCC")), rs.getString("tenNCC"),
                rs.getString("nccDiaChi"), docChuoiChar(rs.getString("nccSoDienThoai")));
        double gia = rs.getDouble("gia");
        // Gia trong SQL dang nullable, nhung double khong bieu dien duoc null.
        // Bao loi ro rang thay vi am tham coi NULL la gia 0.
        if (rs.wasNull() || !Double.isFinite(gia) || gia < 0) {
            throw new IllegalStateException("San pham " + docChuoiChar(rs.getString("maSanPham"))
                    + " co gia NULL/khong hop le. Can chinh lai du lieu.");
        }
        return new SanPham(docChuoiChar(rs.getString("maSanPham")), rs.getString("tenSanPham"),
                rs.getString("donViTinh"), gia, nhom, ncc);
    }

    // Connection thuoc ConnectDB: DAO khong close, commit hay rollback no.
    private Connection layKetNoi() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            throw new IllegalStateException(
                    "Chua co ket noi CSDL hop le. Hay kiem tra ConnectDB va JDBC driver.");
        }
        return con;
    }

    private String kiemTraChuoi(String value, String tenTruong, int doDai, boolean batBuoc) {
        String s = value == null ? null : value.trim();
        if (s == null || s.isEmpty()) {
            if (batBuoc) {
                throw new IllegalArgumentException(tenTruong + " khong duoc de trong.");
            }
            return null;
        }
        if (s.length() > doDai) {
            throw new IllegalArgumentException(tenTruong + " toi da " + doDai + " ky tu.");
        }
        return s;
    }

    // Kieu CHAR(10) co the duoc SQL Server dem them khoang trang.
    private String docChuoiChar(String s) {
        return s == null ? null : s.trim();
    }

    // Tim chua chuoi theo nghia den: %, _, [ va ! khong thanh ky tu dai dien.
    private String taoMauTim(String ten) {
        if (ten == null) {
            throw new IllegalArgumentException("Tu khoa tim kiem khong duoc null.");
        }
        return "%" + ten.trim().replace("!", "!!").replace("%", "!%")
                .replace("_", "!_").replace("[", "![") + "%";
    }

    private IllegalStateException loiCSDL(String thaoTac, SQLException e) {
        String chiTiet = "Khong the truy cap CSDL. Kiem tra ket noi va cau truc bang.";
        // Ma loi SQL Server; van giu SQLException goc trong getCause().
        for (SQLException loi = e; loi != null; loi = loi.getNextException()) {
            int maLoi = loi.getErrorCode();
            if (maLoi == 2601 || maLoi == 2627) {
                chiTiet = "Ma da ton tai, khong the them trung.";
                break;
            }
            if (maLoi == 547) {
                chiTiet = "Vi pham khoa ngoai/rang buoc: ban ghi dang duoc su dung"
                        + " hoac ma tham chieu khong ton tai. Khong tu dong xoa du lieu lien quan.";
                break;
            }
            if (maLoi == 8152 || maLoi == 2628) {
                chiTiet = "Du lieu vuot qua do dai cot trong CSDL.";
                break;
            }
        }
        return new IllegalStateException(thaoTac + ": " + chiTiet, e);
    }
}
