package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.KhachHang_DAO;
import entity.KhachHang;

public class frmKhachHang extends JPanel implements ActionListener, MouseListener {
    private JTextField txtMa, txtTen, txtSdt, txtEmail, txtTim;
    private JButton btnThem, btnSua, btnXoa, btnXoaTrang, btnTim, btnTatCa;
    private JTable table;
    private DefaultTableModel model;
    private KhachHang_DAO khDAO = new KhachHang_DAO();

    public frmKhachHang() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Tiêu đề =====
        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.BLUE);
        add(lblTitle, BorderLayout.NORTH);

        // ===== Form nhập liệu =====
        JPanel pForm = new JPanel(new GridBagLayout());
        pForm.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;

        txtMa = new JTextField(15);
        txtTen = new JTextField(15);
        txtSdt = new JTextField(15);
        txtEmail = new JTextField(15);

        addRow(pForm, gc, 0, "Mã khách hàng:", txtMa, "Tên khách hàng:", txtTen);
        addRow(pForm, gc, 1, "Số điện thoại:", txtSdt, "Email:", txtEmail);

        // ===== Nút chức năng =====
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnXoaTrang = new JButton("Xóa trắng");
        txtTim = new JTextField(12);
        btnTim = new JButton("Tìm theo mã");
        btnTatCa = new JButton("Tất cả");
        pBtn.add(btnThem);
        pBtn.add(btnSua);
        pBtn.add(btnXoa);
        pBtn.add(btnXoaTrang);
        pBtn.add(new JLabel("   Mã KH:"));
        pBtn.add(txtTim);
        pBtn.add(btnTim);
        pBtn.add(btnTatCa);

        JPanel pTop = new JPanel(new BorderLayout());
        pTop.add(pForm, BorderLayout.CENTER);
        pTop.add(pBtn, BorderLayout.SOUTH);

        // ===== Bảng =====
        String[] cols = { "Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Email" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(22);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));

        JPanel pMain = new JPanel(new BorderLayout(5, 5));
        pMain.add(pTop, BorderLayout.NORTH);
        pMain.add(sp, BorderLayout.CENTER);
        add(pMain, BorderLayout.CENTER);

        // ===== Sự kiện =====
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnTim.addActionListener(this);
        btnTatCa.addActionListener(this);
        table.addMouseListener(this);

        docDuLieuLenTable();
    }

    private void addRow(JPanel p, GridBagConstraints gc, int row,
                        String l1, JComponent c1, String l2, JComponent c2) {
        gc.gridy = row;
        gc.gridx = 0; gc.weightx = 0; p.add(new JLabel(l1), gc);
        gc.gridx = 1; gc.weightx = 1; p.add(c1, gc);
        gc.gridx = 2; gc.weightx = 0; p.add(new JLabel(l2), gc);
        gc.gridx = 3; gc.weightx = 1; p.add(c2, gc);
    }

    private void docDuLieuLenTable() {
        model.setRowCount(0);
        for (KhachHang kh : khDAO.getAllKhachHang()) {
            themDongVaoTable(kh);
        }
    }

    private void themDongVaoTable(KhachHang kh) {
        model.addRow(new Object[] { kh.getMaKhachHang(), kh.getTenKhachHang(),
                kh.getSoDienThoai(), kh.getEmail() });
    }

    private void xoaTrang() {
        txtMa.setText("");
        txtTen.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtTim.setText("");
        txtMa.setEditable(true);
        table.clearSelection();
        txtMa.requestFocus();
    }

    // ===== Kiểm tra dữ liệu =====
    private boolean validData() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String sdt = txtSdt.getText().trim();
        String email = txtEmail.getText().trim();

        if (!ma.matches("^KH\\d{3,}$")) {
            baoLoi("Mã khách hàng phải có dạng KH + ít nhất 3 chữ số (VD: KH001)", txtMa);
            return false;
        }
        if (ten.isEmpty()) {
            baoLoi("Tên khách hàng không được để trống", txtTen);
            return false;
        }
        if (!sdt.matches("^0\\d{9}$")) {
            baoLoi("Số điện thoại gồm 10 chữ số, bắt đầu bằng 0", txtSdt);
            return false;
        }
        if (!email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            baoLoi("Email không hợp lệ", txtEmail);
            return false;
        }
        return true;
    }

    private void baoLoi(String msg, JTextField txt) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
        txt.requestFocus();
        txt.selectAll();
    }

    private KhachHang layTuForm() {
        return new KhachHang(txtMa.getText().trim(), txtTen.getText().trim(),
                txtSdt.getText().trim(), txtEmail.getText().trim());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == btnThem) {
            if (validData()) {
                KhachHang kh = layTuForm();
                if (khDAO.timKhachHang(kh.getMaKhachHang()) != null) {
                    baoLoi("Mã khách hàng đã tồn tại", txtMa);
                    return;
                }
                if (khDAO.themKhachHang(kh)) {
                    themDongVaoTable(kh);
                    JOptionPane.showMessageDialog(this, "Thêm thành công");
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (o == btnSua) {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Hãy chọn khách hàng cần sửa");
                return;
            }
            if (validData()) {
                KhachHang kh = layTuForm();
                if (khDAO.suaKhachHang(kh)) {
                    model.setValueAt(kh.getTenKhachHang(), r, 1);
                    model.setValueAt(kh.getSoDienThoai(), r, 2);
                    model.setValueAt(kh.getEmail(), r, 3);
                    JOptionPane.showMessageDialog(this, "Sửa thành công");
                } else {
                    JOptionPane.showMessageDialog(this, "Sửa thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (o == btnXoa) {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Hãy chọn khách hàng cần xóa");
                return;
            }
            int cf = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khách hàng này?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (cf == JOptionPane.YES_OPTION) {
                String ma = model.getValueAt(r, 0).toString();
                if (khDAO.xoaKhachHang(ma)) {
                    model.removeRow(r);
                    xoaTrang();
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa (khách hàng có thể đã có hóa đơn)", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (o == btnXoaTrang) {
            xoaTrang();
        } else if (o == btnTim) {
            String ma = txtTim.getText().trim();
            if (ma.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập mã khách hàng cần tìm");
                return;
            }
            KhachHang kh = khDAO.timKhachHang(ma);
            model.setRowCount(0);
            if (kh != null) {
                themDongVaoTable(kh);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng");
            }
        } else if (o == btnTatCa) {
            txtTim.setText("");
            docDuLieuLenTable();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int r = table.getSelectedRow();
        if (r == -1) return;
        txtMa.setText(model.getValueAt(r, 0).toString());
        txtTen.setText(model.getValueAt(r, 1).toString());
        txtSdt.setText(model.getValueAt(r, 2).toString());
        txtEmail.setText(model.getValueAt(r, 3).toString());
        txtMa.setEditable(false);
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}