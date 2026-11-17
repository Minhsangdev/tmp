package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.NhanVien_DAO;
import entity.NhanVien;

public class frmHoaDon extends JPanel implements ActionListener, MouseListener {
    private JTextField txtMa, txtTen, txtSdt, txtEmail, txtLuong, txtNgay, txtTim;
    private JButton btnThem, btnSua, btnXoa, btnXoaTrang, btnTim, btnTatCa, btnTongLuong;
    private JTable table;
    private DefaultTableModel model;
    private NhanVien_DAO nvDAO = new NhanVien_DAO();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat DF = new DecimalFormat("#,##0");

    public frmHoaDon() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.BLUE);
        add(lblTitle, BorderLayout.NORTH);

        // ===== Form =====
        JPanel pForm = new JPanel(new GridBagLayout());
        pForm.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;

        txtMa = new JTextField(15);
        txtTen = new JTextField(15);
        txtSdt = new JTextField(15);
        txtEmail = new JTextField(15);
        txtLuong = new JTextField(15);
        txtNgay = new JTextField(15);
        txtNgay.setToolTipText("Định dạng dd/MM/yyyy");

        addRow(pForm, gc, 0, "Mã nhân viên:", txtMa, "Tên nhân viên:", txtTen);
        addRow(pForm, gc, 1, "Số điện thoại:", txtSdt, "Email:", txtEmail);
        addRow(pForm, gc, 2, "Lương:", txtLuong, "Ngày làm việc (dd/MM/yyyy):", txtNgay);

        // ===== Nút =====
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnXoaTrang = new JButton("Xóa trắng");
        btnTongLuong = new JButton("Tính tổng lương");
        txtTim = new JTextField(10);
        btnTim = new JButton("Tìm theo mã");
        btnTatCa = new JButton("Tất cả");
        pBtn.add(btnThem);
        pBtn.add(btnSua);
        pBtn.add(btnXoa);
        pBtn.add(btnXoaTrang);
        pBtn.add(btnTongLuong);
        pBtn.add(new JLabel("  Mã NV:"));
        pBtn.add(txtTim);
        pBtn.add(btnTim);
        pBtn.add(btnTatCa);

        JPanel pTop = new JPanel(new BorderLayout());
        pTop.add(pForm, BorderLayout.CENTER);
        pTop.add(pBtn, BorderLayout.SOUTH);

        // ===== Bảng =====
        String[] cols = { "Mã NV", "Tên NV", "Số điện thoại", "Email", "Lương", "Ngày làm việc" };
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
        sp.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));

        JPanel pMain = new JPanel(new BorderLayout(5, 5));
        pMain.add(pTop, BorderLayout.NORTH);
        pMain.add(sp, BorderLayout.CENTER);
        add(pMain, BorderLayout.CENTER);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnTongLuong.addActionListener(this);
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
        for (NhanVien nv : nvDAO.getAllNhanVien()) {
            themDongVaoTable(nv);
        }
    }

    private Object[] toRow(NhanVien nv) {
        return new Object[] { nv.getMaNV(), nv.getTenNV(), nv.getSoDienThoai(), nv.getEmail(),
                DF.format(nv.getLuong()),
                nv.getNgayLamViec() == null ? "" : nv.getNgayLamViec().format(FMT) };
    }

    private void themDongVaoTable(NhanVien nv) {
        model.addRow(toRow(nv));
    }

    private void xoaTrang() {
        txtMa.setText("");
        txtTen.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtLuong.setText("");
        txtNgay.setText("");
        txtTim.setText("");
        txtMa.setEditable(true);
        table.clearSelection();
        txtMa.requestFocus();
    }

    private void baoLoi(String msg, JTextField txt) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
        txt.requestFocus();
        txt.selectAll();
    }

    private boolean validData() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String sdt = txtSdt.getText().trim();
        String email = txtEmail.getText().trim();
        String luong = txtLuong.getText().trim();
        String ngay = txtNgay.getText().trim();

        if (!ma.matches("^NV\\d{3,}$")) {
            baoLoi("Mã nhân viên phải có dạng NV + ít nhất 3 chữ số (VD: NV001)", txtMa);
            return false;
        }
        if (ten.isEmpty()) {
            baoLoi("Tên nhân viên không được để trống", txtTen);
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
        try {
            double l = Double.parseDouble(luong.replace(",", ""));
            if (l <= 0) {
                baoLoi("Lương phải lớn hơn 0", txtLuong);
                return false;
            }
        } catch (NumberFormatException ex) {
            baoLoi("Lương phải là số", txtLuong);
            return false;
        }
        try {
            LocalDate d = LocalDate.parse(ngay, FMT);
            if (d.isAfter(LocalDate.now())) {
                baoLoi("Ngày làm việc không được sau ngày hiện tại", txtNgay);
                return false;
            }
        } catch (DateTimeParseException ex) {
            baoLoi("Ngày làm việc phải đúng định dạng dd/MM/yyyy", txtNgay);
            return false;
        }
        return true;
    }

    private NhanVien layTuForm() {
        return new NhanVien(txtMa.getText().trim(), txtTen.getText().trim(),
                txtSdt.getText().trim(), txtEmail.getText().trim(),
                Double.parseDouble(txtLuong.getText().trim().replace(",", "")),
                LocalDate.parse(txtNgay.getText().trim(), FMT));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == btnThem) {
            if (validData()) {
                NhanVien nv = layTuForm();
                if (nvDAO.timNhanVien(nv.getMaNV()) != null) {
                    baoLoi("Mã nhân viên đã tồn tại", txtMa);
                    return;
                }
                if (nvDAO.themNhanVien(nv)) {
                    themDongVaoTable(nv);
                    JOptionPane.showMessageDialog(this, "Thêm thành công");
                    xoaTrang();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (o == btnSua) {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần sửa");
                return;
            }
            if (validData()) {
                NhanVien nv = layTuForm();
                if (nvDAO.suaNhanVien(nv)) {
                    Object[] row = toRow(nv);
                    for (int i = 0; i < row.length; i++) {
                        model.setValueAt(row[i], r, i);
                    }
                    JOptionPane.showMessageDialog(this, "Sửa thành công");
                } else {
                    JOptionPane.showMessageDialog(this, "Sửa thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (o == btnXoa) {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần xóa");
                return;
            }
            int cf = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (cf == JOptionPane.YES_OPTION) {
                String ma = model.getValueAt(r, 0).toString();
                if (nvDAO.xoaNhanVien(ma)) {
                    model.removeRow(r);
                    xoaTrang();
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa (nhân viên có thể đã lập hóa đơn)", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (o == btnXoaTrang) {
            xoaTrang();
        } else if (o == btnTongLuong) {
            JOptionPane.showMessageDialog(this,
                    "Tổng lương nhân viên: " + DF.format(nvDAO.tinhLuong()) + " VNĐ");
        } else if (o == btnTim) {
            String ma = txtTim.getText().trim();
            if (ma.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập mã nhân viên cần tìm");
                return;
            }
            NhanVien nv = nvDAO.timNhanVien(ma);
            model.setRowCount(0);
            if (nv != null) {
                themDongVaoTable(nv);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên");
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
        txtLuong.setText(model.getValueAt(r, 4).toString().replace(",", ""));
        txtNgay.setText(model.getValueAt(r, 5).toString());
        txtMa.setEditable(false);
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}