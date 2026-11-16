package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHang_DAO {

    public ArrayList<KhachHang> getAllKhachHang() {
        ArrayList<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new KhachHang(
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getString("soDienThoai"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public KhachHang timKhachHang(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("maKhachHang"),
                            rs.getString("tenKhachHang"),
                            rs.getString("soDienThoai"),
                            rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang VALUES (?, ?, ?, ?)";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKhachHang());
            ps.setString(2, kh.getTenKhachHang());
            ps.setString(3, kh.getSoDienThoai());
            ps.setString(4, kh.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKhachHang = ?, soDienThoai = ?, email = ? "
                   + "WHERE maKhachHang = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKhachHang());
            ps.setString(2, kh.getSoDienThoai());
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getMaKhachHang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKhachHang = ?";
        Connection con = ConnectDB.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}