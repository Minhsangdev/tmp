package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;

public class HoaDon_DAO {

    private KhachHang_DAO khDAO = new KhachHang_DAO();
    private NhanVien_DAO nvDAO = new NhanVien_DAO();

    private HoaDon mapRow(ResultSet rs) throws SQLException {
        Date ngay = rs.getDate("ngayLapHD");
        KhachHang kh = khDAO.timKhachHang(rs.getString("maKhachHang"));
        NhanVien nv = nvDAO.timNhanVien(rs.getString("maNhanVien"));
        return new HoaDon(
                rs.getString("maHoaDon"),
                ngay == null ? null : ngay.toLocalDate(),
                rs.getDouble("donGia"),
                kh,
                nv);
    }

    public ArrayList<HoaDon> getAllHoaDon() {
        ArrayList<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";
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

    public HoaDon timHoaDon(String maHD) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
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

    public ArrayList<HoaDon> timHoaDonTheoKhachHang(String maKH) {
        ArrayList<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon WHERE maKhachHang = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean themHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon VALUES (?, ?, ?, ?, ?)";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHoaDon());
            ps.setDate(2, Date.valueOf(hd.getNgayLapHD()));
            ps.setDouble(3, hd.getDonGia());
            ps.setString(4, hd.getKhachHang().getMaKhachHang());
            ps.setString(5, hd.getNhanVien().getMaNV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaHoaDon(HoaDon hd) {
        String sql = "UPDATE HoaDon SET ngayLapHD = ?, donGia = ?, maKhachHang = ?, "
                   + "maNhanVien = ? WHERE maHoaDon = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(hd.getNgayLapHD()));
            ps.setDouble(2, hd.getDonGia());
            ps.setString(3, hd.getKhachHang().getMaKhachHang());
            ps.setString(4, hd.getNhanVien().getMaNV());
            ps.setString(5, hd.getMaHoaDon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaHoaDon(String maHD) {
        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}