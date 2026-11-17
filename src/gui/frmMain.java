package gui;

import java.awt.Dimension;
import java.sql.SQLException;

import javax.swing.*;

import connectDB.ConnectDB;

public class FrmMain extends JFrame {

    public FrmMain() {
        setTitle("Quản lý bán hàng");
        setSize(1000, 600);
        setMinimumSize(new Dimension(900, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Khách hàng", new FrmKhachHang());
        tabs.addTab("Nhân viên", new FrmNhanVien());
        tabs.addTab("Hóa đơn", new FrmHoaDon());
        add(tabs);
    }

    public static void main(String[] args) {
        try {
            ConnectDB.getInstance().connect();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Không kết nối được CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(() -> new FrmMain().setVisible(true));
    }
}