package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhaCungCap;

/**
 * CRUD va tim kiem NhaCungCap; khong phu thuoc GUI.
 * Truy van tra danh sach moi, rong neu khong tim thay (khong tra null).
 * create/update/delete tra true khi thay doi 1 dong; false neu khong co dong.
 * Du lieu dau vao sai: IllegalArgumentException.
 * Loi SQL: IllegalStateException, SQLException goc nam trong cause.
 * Ket noi/du lieu doc khong hop le cung duoc bao bang IllegalStateException.
 * Khong dung chung mot DAO/Connection dong thoi giua nhieu luong.
 */
public class NhaCungCap_DAO {
    private static final String SELECT_SQL = "SELECT maNCC, tenNCC, diaChi, soDienThoai FROM NhaCungCap";

    public ArrayList<NhaCungCap> getAllNhaCungCap() {
        return docDanhSach(SELECT_SQL + " ORDER BY maNCC");
    }

    /** Tim chinh xac theo ma; ket qua gom 0 hoac 1 phan tu. */
    public ArrayList<NhaCungCap> getNhaCungCapTheoMa(String ma) {
        return docDanhSach(SELECT_SQL + " WHERE maNCC = ?",
                kiemTraChuoi(ma, "Ma", 10, true));
    }

    /** Tim ten co chua tu khoa. Chuoi rong tai lai tat ca ban ghi. */
    public ArrayList<NhaCungCap> getNhaCungCapTheoTen(String ten) {
        if (ten != null && ten.trim().isEmpty()) {
            return getAllNhaCungCap();
        }
        return docDanhSach(SELECT_SQL + " WHERE tenNCC LIKE ? ESCAPE '!' ORDER BY maNCC",
                taoMauTim(ten));
    }

    public boolean create(NhaCungCap ncc) {
        if (ncc == null) {
            throw new IllegalArgumentException("NhaCungCap khong duoc null.");
        }
        String maNCC = kiemTraChuoi(ncc.getMaNCC(), "Ma nha cung cap", 10, true);
        String tenNCC = kiemTraChuoi(ncc.getTenNCC(), "Ten nha cung cap", 20, true);
        String diaChi = kiemTraChuoi(ncc.getDiaChi(), "Dia chi", 20, false);
        String soDienThoai = kiemTraChuoi(ncc.getSoDienThoai(), "So dien thoai", 10, false);
        if (soDienThoai != null && !soDienThoai.matches("[0-9]{1,10}")) {
            throw new IllegalArgumentException("So dien thoai chi duoc chua toi da 10 chu so.");
        }
        String sql = "INSERT INTO NhaCungCap (maNCC, tenNCC, diaChi, soDienThoai) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = layKetNoi().prepareStatement(sql)) {
            stmt.setString(1, maNCC);
            stmt.setString(2, tenNCC);
            stmt.setString(3, diaChi);
            stmt.setString(4, soDienThoai);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw loiCSDL("Them NhaCungCap", e);
        }
    }

    public boolean update(NhaCungCap ncc) {
        if (ncc == null) {
            throw new IllegalArgumentException("NhaCungCap khong duoc null.");
        }
        String maNCC = kiemTraChuoi(ncc.getMaNCC(), "Ma nha cung cap", 10, true);
        String tenNCC = kiemTraChuoi(ncc.getTenNCC(), "Ten nha cung cap", 20, true);
        String diaChi = kiemTraChuoi(ncc.getDiaChi(), "Dia chi", 20, false);
        String soDienThoai = kiemTraChuoi(ncc.getSoDienThoai(), "So dien thoai", 10, false);
        if (soDienThoai != null && !soDienThoai.matches("[0-9]{1,10}")) {
            throw new IllegalArgumentException("So dien thoai chi duoc chua toi da 10 chu so.");
        }
        String sql = "UPDATE NhaCungCap SET tenNCC = ?, diaChi = ?, soDienThoai = ? WHERE maNCC = ?";
        try (PreparedStatement stmt = layKetNoi().prepareStatement(sql)) {
            stmt.setString(1, tenNCC);
            stmt.setString(2, diaChi);
            stmt.setString(3, soDienThoai);
            stmt.setString(4, maNCC);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw loiCSDL("Cap nhat NhaCungCap", e);
        }
    }

    /** Chi xoa ban ghi nay; de khoa ngoai ngan xoa khi dang duoc tham chieu. */
    public boolean delete(String ma) {
        String id = kiemTraChuoi(ma, "Ma", 10, true);
        String sql = "DELETE FROM NhaCungCap WHERE maNCC = ?";
        try (PreparedStatement stmt = layKetNoi().prepareStatement(sql)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw loiCSDL("Xoa NhaCungCap", e);
        }
    }

    private ArrayList<NhaCungCap> docDanhSach(String sql, String... thamSo) {
        ArrayList<NhaCungCap> ds = new ArrayList<NhaCungCap>();
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
            throw loiCSDL("Doc NhaCungCap", e);
        }
        return ds;
    }

    private NhaCungCap docBanGhi(ResultSet rs) throws SQLException {
        return new NhaCungCap(docChuoiChar(rs.getString("maNCC")), rs.getString("tenNCC"),
                rs.getString("diaChi"), docChuoiChar(rs.getString("soDienThoai")));
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
