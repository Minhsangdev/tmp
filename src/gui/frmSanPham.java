package gui;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.function.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.NhaCungCap_DAO;
import dao.NhomSanPham_DAO;
import dao.SanPham_DAO;
import entity.NhaCungCap;
import entity.NhomSanPham;
import entity.SanPham;

public class FrmSanPham extends JPanel implements ActionListener, MouseListener {
    private JTextField txtMa, txtTen, txtDonVi, txtGia, txtTim;
    private JComboBox<NhomSanPham> cboNhom;
    private JComboBox<NhaCungCap> cboNCC;
    private JButton btnThem, btnSua, btnXoa, btnXoaTrang, btnLamMoiDS;
    private JButton btnTimMa, btnTimTen, btnLocNhom, btnLocNCC, btnTatCa;
    private JTable table;
    private DefaultTableModel model;

    private SanPham_DAO spDAO = new SanPham_DAO();
    private NhomSanPham_DAO nhomDAO = new NhomSanPham_DAO();
    private NhaCungCap_DAO nccDAO = new NhaCungCap_DAO();

    private static final DecimalFormat DF = new DecimalFormat("#,##0");

    public FrmSanPham() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.BLUE);
        add(lblTitle, BorderLayout.NORTH);

        // ===== Form =====
        JPanel pForm = new JPanel(new GridBagLayout());
        pForm.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.fill = GridBagConstraints.HORIZONTAL;

        txtMa = new JTextField(15);
        txtTen = new JTextField(15);
        txtDonVi = new JTextField(15);
        txtGia = new JTextField(15);
        cboNhom = new JComboBox<>();
        cboNCC = new JComboBox<>();
        caiRenderer();
        napComboBox();

        addRow(pForm, gc, 0, "Mã sản phẩm:", txtMa, "Tên sản phẩm:", txtTen);
        addRow(pForm, gc, 1, "Đơn vị tính:", txtDonVi, "Giá:", txtGia);
        addRow(pForm, gc, 2, "Nhóm sản phẩm:", cboNhom, "Nhà cung cấp:", cboNCC);

        // ===== Nút =====
        JPanel pBtn1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnXoaTrang = new JButton("Xóa trắng");
        btnLamMoiDS = new JButton("Làm mới nhóm/NCC");
        pBtn1.add(btnThem);
        pBtn1.add(btnSua);
        pBtn1.add(btnXoa);
        pBtn1.add(btnXoaTrang);
        pBtn1.add(btnLamMoiDS);

        JPanel pBtn2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtTim = new JTextField(12);
        btnTimMa = new JButton("Tìm theo mã");
        btnTimTen = new JButton("Tìm theo tên");
        btnLocNhom = new JButton("Lọc theo nhóm");
        btnLocNCC = new JButton("Lọc theo NCC");
        btnTatCa = new JButton("Tất cả");
        pBtn2.add(new JLabel("Từ khóa:"));
        pBtn2.add(txtTim);
        pBtn2.add(btnTimMa);
        pBtn2.add(btnTimTen);
        pBtn2.add(btnLocNhom);
        pBtn2.add(btnLocNCC);
        pBtn2.add(btnTatCa);

        JPanel pBtn = new JPanel(new GridLayout(2, 1));
        pBtn.add(pBtn1);
        pBtn.add(pBtn2);

        JPanel pTop = new JPanel(new BorderLayout());
        pTop.add(pForm, BorderLayout.CENTER);
        pTop.add(pBtn, BorderLayout.SOUTH);

        // ===== Bảng =====
        String[] cols = { "Mã SP", "Tên sản phẩm", "Đơn vị tính", "Giá", "Nhóm", "Nhà cung cấp" };
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
        sp.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        JPanel pMain = new JPanel(new BorderLayout(5, 5));
        pMain.add(pTop, BorderLayout.NORTH);
        pMain.add(sp, BorderLayout.CENTER);
        add(pMain, BorderLayout.CENTER);

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnLamMoiDS.addActionListener(this);
        btnTimMa.addActionListener(this);
        btnTimTen.addActionListener(this);
        btnLocNhom.addActionListener(this);
        btnLocNCC.addActionListener(this);
        btnTatCa.addActionListener(this);
        table.addMouseListener(this);

        taiDanhSach(() -> spDAO.getAllSanPham());
    }

    private void addRow(JPanel p, GridBagConstraints gc, int row,
                        String l1, JComponent c1, String l2, JComponent c2) {
        gc.gridy = row;
        gc.gridx = 0; gc.weightx = 0; p.add(new JLabel(l1), gc);
        gc.gridx = 1; gc.weightx = 1; p.add(c1, gc);
        gc.gridx = 2; gc.weightx = 0; p.add(new JLabel(l2), gc);
        gc.gridx = 3; gc.weightx = 1; p.add(c2, gc);
    }

    // toString() của entity không còn dạng "mã - tên" nên hiển thị bằng renderer
    private void caiRenderer() {
        cboNhom.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof NhomSanPham) {
                    NhomSanPham n = (NhomSanPham) value;
                    setText(n.getMaNhom() + " - " + n.getTenNhom());
                }
                return this;
            }
        });
        cboNCC.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof NhaCungCap) {
                    NhaCungCap c = (NhaCungCap) value;
                    setText(c.getMaNCC() + " - " + c.getTenNCC());
                }
                return this;
            }
        });
    }

    private void napComboBox() {
        try {
            cboNhom.removeAllItems();
            for (NhomSanPham n : nhomDAO.getAllNhomSanPham()) {
                cboNhom.addItem(n);
            }
            cboNCC.removeAllItems();
            for (NhaCungCap c : nccDAO.getAllNhaCungCap()) {
                cboNCC.addItem(c);
            }
        } catch (RuntimeException ex) {
            hienLoi(ex);
        }
    }

    // Tải danh sách lên bảng; gom xử lý lỗi của DAO vào một chỗ
    private void taiDanhSach(Supplier<ArrayList<SanPham>> nguon) {
        try {
            ArrayList<SanPham> ds = nguon.get();
            model.setRowCount(0);
            for (SanPham sp : ds) {
                model.addRow(toRow(sp));
            }
        } catch (RuntimeException ex) {
            hienLoi(ex);
        }
    }

    private void hienLoi(RuntimeException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private Object[] toRow(SanPham sp) {
        NhomSanPham n = sp.getNhomSanPham();
        NhaCungCap c = sp.getNhaCungCap();
        return new Object[] { sp.getMaSanPham(), sp.getTenSanPham(), sp.getDonViTinh(),
                DF.format(sp.getGia()),
                n == null ? "" : n.getMaNhom() + " - " + n.getTenNhom(),
                c == null ? "" : c.getMaNCC() + " - " + c.getTenNCC() };
    }

    private void xoaTrang() {
        txtMa.setText("");
        txtTen.setText("");
        txtDonVi.setText("");
        txtGia.setText("");
        txtTim.setText("");
        if (cboNhom.getItemCount() > 0) cboNhom.setSelectedIndex(0);
        if (cboNCC.getItemCount() > 0) cboNCC.setSelectedIndex(0);
        txtMa.setEditable(true);
        table.clearSelection();
        txtMa.requestFocus();
    }

    private void baoLoi(String msg, JTextField txt) {
        JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.ERROR_MESSAGE);
        if (txt != null) {
            txt.requestFocus();
            txt.selectAll();
        }
    }

    // Giới hạn độ dài khớp với DAO: mã 10, tên 20, đơn vị tính 20; giá >= 0
    private boolean validData() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String donVi = txtDonVi.getText().trim();
        String gia = txtGia.getText().trim();

        if (!ma.matches("^SP\\d{3,8}$")) {
            baoLoi("Mã sản phẩm phải có dạng SP + 3 đến 8 chữ số (VD: SP001), tối đa 10 ký tự", txtMa);
            return false;
        }
        if (ten.isEmpty()) {
            baoLoi("Tên sản phẩm không được để trống", txtTen);
            return false;
        }
        if (ten.length() > 20) {
            baoLoi("Tên sản phẩm tối đa 20 ký tự", txtTen);
            return false;
        }
        if (donVi.isEmpty()) {
            baoLoi("Đơn vị tính không được để trống", txtDonVi);
            return false;
        }
        if (donVi.length() > 20) {
            baoLoi("Đơn vị tính tối đa 20 ký tự", txtDonVi);
            return false;
        }
        try {
            double g = Double.parseDouble(gia.replace(",", ""));
            if (g < 0) {
                baoLoi("Giá không được âm", txtGia);
                return false;
            }
        } catch (NumberFormatException ex) {
            baoLoi("Giá phải là số", txtGia);
            return false;
        }
        if (cboNhom.getSelectedItem() == null) {
            baoLoi("Chưa có nhóm sản phẩm để chọn", null);
            return false;
        }
        if (cboNCC.getSelectedItem() == null) {
            baoLoi("Chưa có nhà cung cấp để chọn", null);
            return false;
        }
        return true;
    }

    private SanPham layTuForm() {
        return new SanPham(txtMa.getText().trim(), txtTen.getText().trim(),
                txtDonVi.getText().trim(),
                Double.parseDouble(txtGia.getText().trim().replace(",", "")),
                (NhomSanPham) cboNhom.getSelectedItem(),
                (NhaCungCap) cboNCC.getSelectedItem());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        try {
            if (o == btnThem) {
                xuLyThem();
            } else if (o == btnSua) {
                xuLySua();
            } else if (o == btnXoa) {
                xuLyXoa();
            } else if (o == btnXoaTrang) {
                xoaTrang();
            } else if (o == btnLamMoiDS) {
                napComboBox();
            } else if (o == btnTimMa) {
                String ma = txtTim.getText().trim();
                if (ma.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nhập mã sản phẩm cần tìm");
                    return;
                }
                ArrayList<SanPham> kq = spDAO.getSanPhamTheoMa(ma);
                hienKetQuaTim(kq);
            } else if (o == btnTimTen) {
                // Từ khóa rỗng: DAO tự trả về toàn bộ danh sách
                hienKetQuaTim(spDAO.getSanPhamTheoTen(txtTim.getText()));
            } else if (o == btnLocNhom) {
                NhomSanPham n = (NhomSanPham) cboNhom.getSelectedItem();
                if (n == null) return;
                taiDanhSach(() -> spDAO.getSanPhamTheoNhomSanPham(n.getMaNhom()));
            } else if (o == btnLocNCC) {
                NhaCungCap c = (NhaCungCap) cboNCC.getSelectedItem();
                if (c == null) return;
                taiDanhSach(() -> spDAO.getSanPhamTheoNhaCungCap(c.getMaNCC()));
            } else if (o == btnTatCa) {
                txtTim.setText("");
                taiDanhSach(() -> spDAO.getAllSanPham());
            }
        } catch (RuntimeException ex) {
            // IllegalArgumentException / IllegalStateException từ DAO
            hienLoi(ex);
        }
    }

    private void hienKetQuaTim(ArrayList<SanPham> kq) {
        model.setRowCount(0);
        if (kq.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm");
            return;
        }
        for (SanPham sp : kq) {
            model.addRow(toRow(sp));
        }
    }

    private void xuLyThem() {
        if (!validData()) return;
        SanPham sp = layTuForm();
        if (!spDAO.getSanPhamTheoMa(sp.getMaSanPham()).isEmpty()) {
            baoLoi("Mã sản phẩm đã tồn tại", txtMa);
            return;
        }
        if (spDAO.create(sp)) {
            model.addRow(toRow(sp));
            JOptionPane.showMessageDialog(this, "Thêm thành công");
            xoaTrang();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuLySua() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm cần sửa");
            return;
        }
        if (!validData()) return;
        SanPham sp = layTuForm();
        if (spDAO.update(sp)) {
            Object[] row = toRow(sp);
            for (int i = 0; i < row.length; i++) {
                model.setValueAt(row[i], r, i);
            }
            JOptionPane.showMessageDialog(this, "Sửa thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Sửa thất bại (không tìm thấy sản phẩm)",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuLyXoa() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm cần xóa");
            return;
        }
        int cf = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (cf != JOptionPane.YES_OPTION) return;

        String ma = model.getValueAt(r, 0).toString();
        if (spDAO.delete(ma)) {
            model.removeRow(r);
            xoaTrang();
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại (không tìm thấy sản phẩm)",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int r = table.getSelectedRow();
        if (r == -1) return;
        txtMa.setText(model.getValueAt(r, 0).toString());
        txtTen.setText(model.getValueAt(r, 1).toString());
        txtDonVi.setText(model.getValueAt(r, 2).toString());
        txtGia.setText(model.getValueAt(r, 3).toString().replace(",", ""));
        chonNhom(model.getValueAt(r, 4).toString().split(" - ")[0]);
        chonNCC(model.getValueAt(r, 5).toString().split(" - ")[0]);
        txtMa.setEditable(false);
    }

    private void chonNhom(String ma) {
        for (int i = 0; i < cboNhom.getItemCount(); i++) {
            if (cboNhom.getItemAt(i).getMaNhom().equals(ma)) {
                cboNhom.setSelectedIndex(i);
                return;
            }
        }
    }

    private void chonNCC(String ma) {
        for (int i = 0; i < cboNCC.getItemCount(); i++) {
            if (cboNCC.getItemAt(i).getMaNCC().equals(ma)) {
                cboNCC.setSelectedIndex(i);
                return;
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}