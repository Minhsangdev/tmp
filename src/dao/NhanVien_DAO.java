package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVien_DAO {

    private NhanVien mapRow(ResultSet rs) throws SQLException {
        Date ngay = rs.getDate("ngayLamViec");
        return new NhanVien(
                rs.getString("maNV"),
                rs.getString("tenNV"),
                rs.getString("soDienThoai"),
                rs.getString("email"),
                rs.getDouble("luong"),
                ngay == null ? null : ngay.toLocalDate());
    }

    public ArrayList<NhanVien> getAllNhanVien() {
        ArrayList<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // TinNhanVien(maNV : String) : Object
    public NhanVien timNhanVien(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE maNV = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ThemNhanVien() : Boolean
    public boolean themNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setString(3, nv.getSoDienThoai());
            ps.setString(4, nv.getEmail());
            ps.setDouble(5, nv.getLuong());
            ps.setDate(6, Date.valueOf(nv.getNgayLamViec()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // SuaThongTinNV(maNV : String) : Boolean
    public boolean suaNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET tenNV = ?, soDienThoai = ?, email = ?, "
                   + "luong = ?, ngayLamViec = ? WHERE maNV = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getSoDienThoai());
            ps.setString(3, nv.getEmail());
            ps.setDouble(4, nv.getLuong());
            ps.setDate(5, Date.valueOf(nv.getNgayLamViec()));
            ps.setString(6, nv.getMaNV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // XoaNhanVien(maNV : String) : Boolean
    public boolean xoaNhanVien(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE maNV = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // TinhLuong() : Double - tổng lương của tất cả nhân viên
    public double tinhLuong() {
        String sql = "SELECT SUM(luong) AS tong FROM NhanVien";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("tong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}