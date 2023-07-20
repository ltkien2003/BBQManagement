package com.poly.kienltpc04639.assignment_final;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.ParseException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Kienltpc04639
 */
public class QuanLyNhaHang extends javax.swing.JFrame {

    /**
     * Creates new form QuanLyNhaHang
     */
    String url = "jdbc:sqlserver://localhost:1433;database=QuanLyNhaHang;trustServerCertificate=true;";
    Connection con;
    Statement st;
    ResultSet rs;
    PreparedStatement ps;
    DefaultTableModel modelThongTinMonAn; // tạo model lưu trữ dữ liệu bảng
    DefaultTableModel modelPhanPhoiSanPham; // tạo model lưu trữ dữ liệu bảng
    List<ThongTinMonAn> list = new ArrayList<>();
    List<PhanPhoiSanPham> danhSach = new ArrayList<>();
    int viTriThongTinMonAn = 0;
    int viTriPhanPhoiSanPham = 0;
    String diaChiHinhMonAn = "";
    String diaChiHinhAnh = "";
    String anhMacDinh = "src\\com\\poly\\icon\\photo.png";
    String vaiTro;
    String sqlSelectThongTinMonAn = "SELECT * FROM ThongTinMonAn";
    String sqlInsertThongTinMonAn = "INSERT INTO ThongTinMonAn VALUES(?, ?, ?, ?, ?, ?, ?)";
    String sqlUpdateThongTinMonAn = "UPDATE dbo.ThongTinMonAn SET TenMonAn = ?, Gia = ?, NgayCapNhat = ?, SoLuong = ?, MoTa = ?, IconHinhAnh = ? WHERE MaMonAn = ?";
    String sqlDeleteThongTinMonAn = "DELETE FROM dbo.ThongTinMonAn WHERE MaMonAn = ?";
    String sqlSelectPhanPhoiSanPham = "SELECT ID, ThongTinMonAn.MaMonAn, TenMonAn, TenPhanPhoi, GiaNhap,ThongTinNhaPhanPhoi.SoLuong, DiaChi, ThongTinNhaPhanPhoi.IconHinhAnh FROM ThongTinNhaPhanPhoi INNER JOIN ThongTinMonAn ON ThongTinMonAn.MaMonAn = ThongTinNhaPhanPhoi.MaMonAn";
    String sqlUpdateThongTinNhaPhanPhoi = "UPDATE dbo.ThongTinNhaPhanPhoi SET MaMonAn = ?, TenPhanPhoi = ?, GiaNhap = ?, SoLuong = ?, DiaChi = ?, IconHinhAnh = ? WHERE ID = ?";
    String sqlDeleteThongTinNhaPhanPhoi = "DELETE FROM dbo.ThongTinNhaPhanPhoi WHERE MaMonAn = ? AND TenPhanPhoi = ?";
    String sqlInsertThongTinNhaPhanPhoi = "INSERT INTO ThongTinNhaPhanPhoi VALUES(?, ?, ?, ?, ?, ?)";

    public QuanLyNhaHang() {
        try {
            initComponents();
            con = DriverManager.getConnection(url, "sa", "120903");
            st = con.createStatement();
            lblBannerHome.setIcon(new ImageIcon(new ImageIcon("src\\com\\poly\\icon\\banner_home.jpg").getImage().getScaledInstance(lblBannerHome.getWidth(), lblBannerHome.getHeight(), Image.SCALE_DEFAULT)));
            lblAnhHome1.setIcon(new ImageIcon(new ImageIcon("src\\com\\poly\\icon\\home1.jpg").getImage().getScaledInstance(lblAnhHome1.getWidth(), lblAnhHome1.getHeight(), Image.SCALE_DEFAULT)));
            lblAnhHome2.setIcon(new ImageIcon(new ImageIcon("src\\com\\poly\\icon\\home2.jpg").getImage().getScaledInstance(lblAnhHome2.getWidth(), lblAnhHome2.getHeight(), Image.SCALE_DEFAULT)));
            loadDuLieuThongTinMonAn();
            loadDuLieuPhanPhoiSanPham();
            canGiuaBangThongTinMonAn();
            canGiuaBangPhanPhoiSanPham();
            tblThongTinMonAn.setDefaultEditor(Object.class, null);
            tblPhanPhoiSanPham.setDefaultEditor(Object.class, null);
            tblThongTinMonAn.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 14));
            tblPhanPhoiSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public QuanLyNhaHang(String vt) {
        initComponents();
        lblBannerHome.setIcon(new ImageIcon(new ImageIcon("src\\com\\poly\\icon\\banner_home.jpg").getImage().getScaledInstance(lblBannerHome.getWidth(), lblBannerHome.getHeight(), Image.SCALE_DEFAULT)));
        lblAnhHome1.setIcon(new ImageIcon(new ImageIcon("src\\com\\poly\\icon\\home1.jpg").getImage().getScaledInstance(lblAnhHome1.getWidth(), lblAnhHome1.getHeight(), Image.SCALE_DEFAULT)));
        lblAnhHome2.setIcon(new ImageIcon(new ImageIcon("src\\com\\poly\\icon\\home2.jpg").getImage().getScaledInstance(lblAnhHome2.getWidth(), lblAnhHome2.getHeight(), Image.SCALE_DEFAULT)));
        loadDuLieuThongTinMonAn();
        loadDuLieuPhanPhoiSanPham();
        canGiuaBangThongTinMonAn();
        canGiuaBangPhanPhoiSanPham();
        tblThongTinMonAn.setDefaultEditor(Object.class, null);
        tblPhanPhoiSanPham.setDefaultEditor(Object.class, null);
        tblThongTinMonAn.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblPhanPhoiSanPham.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (vt.equals("User")) {
            btnCapNhat.setEnabled(false);
            btnCapNhat1.setEnabled(false);
        }
    }

    private void canGiuaBangThongTinMonAn() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblThongTinMonAn.getColumnModel().getColumn(0).setHeaderRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(1).setHeaderRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(2).setHeaderRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(3).setHeaderRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(4).setHeaderRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(5).setHeaderRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblThongTinMonAn.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
    }

    private void canGiuaBangPhanPhoiSanPham() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblPhanPhoiSanPham.getColumnModel().getColumn(0).setHeaderRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(1).setHeaderRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(2).setHeaderRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(3).setHeaderRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(4).setHeaderRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(5).setHeaderRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblPhanPhoiSanPham.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
    }

    public void timKiemThongTinMonAn() {
        boolean kiemTra = false;
        int viTri = 0;
        if (txtTimKiemThongTinMonAn.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã món ăn để tìm kiếm thông tin");
            txtTimKiemThongTinMonAn.requestFocus();
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).maMonAn.equalsIgnoreCase(txtTimKiemThongTinMonAn.getText())) {
                    kiemTra = true;
                    viTri = i;
                }
            }
            if (kiemTra == true) {
                JOptionPane.showMessageDialog(this, "Thông tin món ăn đã được tìm thấy");
                tblThongTinMonAn.setRowSelectionInterval(viTri, viTri);
                Rectangle cell = tblThongTinMonAn.getCellRect(viTri, 0, true);
                tblThongTinMonAn.scrollRectToVisible(cell);
                hienThiThongTinMonAn();
            } else {
                JOptionPane.showMessageDialog(this, "Thông tin món ăn không có trong bảng");
            }
        }
    }

    public void resetFormThongTinMonAn() {
        txtMaMonAn.setText("");
        txtTenMonAn.setText("");
        txtGia.setText("");
        txtNgayCapNhat.setText("");
        txtSoLuong.setText("");
        txtMoTa.setText("");
        lblIconThongTinMonAn.setIcon(new ImageIcon("src\\com\\poly\\icon\\photo.png"));
        lblIconThongTinMonAn.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        txtMaMonAn.requestFocus();
        tblThongTinMonAn.clearSelection();
        viTriThongTinMonAn = 0;
    }

    public void hienThiThongTinMonAn() {
        int viTri = tblThongTinMonAn.getSelectedRow();
        ThongTinMonAn ttma = list.get(viTri);
        if (ttma.iconHinhAnh.equals("") || ttma.iconHinhAnh.equals(anhMacDinh)) {
            ttma.iconHinhAnh = anhMacDinh;
            lblIconThongTinMonAn.setIcon(new ImageIcon(ttma.iconHinhAnh));
        } else {
            lblIconThongTinMonAn.setIcon(new ImageIcon(new ImageIcon(ttma.iconHinhAnh).getImage().getScaledInstance(lblIconThongTinMonAn.getWidth(), lblIconThongTinMonAn.getHeight(), Image.SCALE_DEFAULT)));
        }
        txtMaMonAn.setText(tblThongTinMonAn.getValueAt(viTri, 0).toString());
        txtTenMonAn.setText(tblThongTinMonAn.getValueAt(viTri, 1).toString());
        txtGia.setText(tblThongTinMonAn.getValueAt(viTri, 2).toString());
        txtNgayCapNhat.setText(tblThongTinMonAn.getValueAt(viTri, 3).toString());
        txtSoLuong.setText(tblThongTinMonAn.getValueAt(viTri, 4).toString());
        txtMoTa.setText(tblThongTinMonAn.getValueAt(viTri, 5).toString());
    }

    public void fillThongTinMonAn() {
        try {
            rs = st.executeQuery(sqlSelectThongTinMonAn);
            list.clear();
            while (rs.next()) {
                String maMonAn = rs.getString(1);
                String tenMonAn = rs.getString(2);
                int gia = rs.getInt(3);
                Date ngay = rs.getDate(4);
                String ngayCapNhat = (new SimpleDateFormat("dd-MM-yyyy")).format(ngay);
                int soLuong = rs.getInt(5);
                String moTa = rs.getString(6);
                String srcHinhAnh = rs.getString(7);
                list.add(new ThongTinMonAn(maMonAn, tenMonAn, gia, ngayCapNhat, soLuong, moTa, srcHinhAnh));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e);
        }
        modelThongTinMonAn.setRowCount(0);
        for (ThongTinMonAn nh : list) {
            modelThongTinMonAn.addRow(new Object[]{nh.maMonAn, nh.tenMonAn, nh.gia, nh.ngayCapNhat, nh.soLuong, nh.moTa});
        }
    }

    private void loadDuLieuThongTinMonAn() {
        modelThongTinMonAn = new DefaultTableModel(); // tạo model mới
        modelThongTinMonAn.addColumn("MÃ MÓN ĂN"); // tạo cột cho bảng
        modelThongTinMonAn.addColumn("TÊN MÓN ĂN"); // tạo cột cho bảng
        modelThongTinMonAn.addColumn("GIÁ"); // tạo cột cho bảng
        modelThongTinMonAn.addColumn("NGÀY CẬP NHẬT");// tạo cột cho bảng
        modelThongTinMonAn.addColumn("SỐ LƯỢNG");// tạo cột cho bảng
        modelThongTinMonAn.addColumn("MÔ TẢ");// tạo cột cho bảng
        tblThongTinMonAn.setModel(modelThongTinMonAn);
        //Thêm dữ liệu vào danh sách
        try {
            rs = st.executeQuery(sqlSelectThongTinMonAn);
            list.clear();
            while (rs.next()) {
                String maMonAn = rs.getString(1);
                String tenMonAn = rs.getString(2);
                int gia = rs.getInt(3);
                Date ngay = rs.getDate(4);
                String ngayCapNhat = (new SimpleDateFormat("dd-MM-yyyy")).format(ngay);
                int soLuong = rs.getInt(5);
                String moTa = rs.getString(6);
                String srcHinhAnh = rs.getString(7);
                list.add(new ThongTinMonAn(maMonAn, tenMonAn, gia, ngayCapNhat, soLuong, moTa, srcHinhAnh));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu trong file");
        }
        //Thêm các dòng dữ liệu vào model
        for (ThongTinMonAn nh : list) {
            modelThongTinMonAn.addRow(new Object[]{nh.maMonAn, nh.tenMonAn, nh.gia, nh.ngayCapNhat, nh.soLuong, nh.moTa});
            tblThongTinMonAn.setModel(modelThongTinMonAn);
        }
    }

    public boolean kiemTraLuuThongTinMonAn() {
        boolean kiemTra = false;
        boolean kiemTraGia = true;
        boolean kiemTraNgayCapNhat = true;
        boolean kiemTraSoLuong = true;
        String kiemTraMaMonAn = "^MA+\\d+(\\d+)+";
        String kiemTraDinhDangNgayCapNhat = "([0-9]{2})-([0-9]{2})-([0-9]{4})";
        for (ThongTinMonAn ttma : list) {
            if (ttma.maMonAn.equalsIgnoreCase(txtMaMonAn.getText())) {
                kiemTra = true;
            }
        }
        try {
            Integer.parseInt(txtGia.getText());
        } catch (NumberFormatException e) {
            kiemTraGia = false;
        }
        try {
            SimpleDateFormat dinhDang = new SimpleDateFormat();
            dinhDang.applyPattern("dd-MM-yyyy");
            Date ngayThangNam;
            ngayThangNam = dinhDang.parse(txtNgayCapNhat.getText());
        } catch (ParseException e) {
            kiemTraNgayCapNhat = false;
        }
        try {
            Integer.parseInt(txtSoLuong.getText());
        } catch (NumberFormatException e) {
            kiemTraSoLuong = false;
        }
        if (txtMaMonAn.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Mã món ăn không được để trống");
            txtMaMonAn.requestFocus();
        } else if (txtTenMonAn.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên món ăn không được để trống");
            txtTenMonAn.requestFocus();
        } else if (txtGia.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Giá không được để trống");
            txtGia.requestFocus();
        } else if (txtNgayCapNhat.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Ngày cập nhật không được để trống");
            txtNgayCapNhat.requestFocus();
        } else if (txtSoLuong.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Số lượng không được để trống");
            txtSoLuong.requestFocus();
        } else if (txtMoTa.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Mô tả không được để trống");
            txtMoTa.requestFocus();
        } else if (!txtMaMonAn.getText().matches(kiemTraMaMonAn)) {
            JOptionPane.showMessageDialog(this, "Mã món ăn không đúng định dạng (VD: MA01)", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaMonAn.requestFocus();
        } else if (kiemTraGia == false) {
            JOptionPane.showMessageDialog(this, "Giá phải là số", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtGia.requestFocus();
        } else if (kiemTraNgayCapNhat == false || !txtNgayCapNhat.getText().matches(kiemTraDinhDangNgayCapNhat)) {
            JOptionPane.showMessageDialog(this, "Ngày cập nhật chưa đúng định dạng dd-MM-yyyy", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtNgayCapNhat.requestFocus();
        } else if (kiemTraSoLuong == false) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoLuong.requestFocus();
        } else if (Integer.parseInt(txtGia.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "Giá phải lớn hơn 0", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtGia.requestFocus();
        } else if (Integer.parseInt(txtSoLuong.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoLuong.requestFocus();
        } else if (kiemTra == true) {
            JOptionPane.showMessageDialog(this, "Thông tin món ăn đã tồn tại");
            txtMaMonAn.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    public boolean kiemTraCapNhatThongTinMonAn() {
        int viTri = tblThongTinMonAn.getSelectedRow();
        if (tblThongTinMonAn.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else if (viTri < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng cần cập nhật");
        } else if (tblThongTinMonAn.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chỉ chọn 1 hàng để cập nhật");
        } else if (!tblThongTinMonAn.getValueAt(viTri, 0).toString().equalsIgnoreCase(txtMaMonAn.getText())) {
            JOptionPane.showMessageDialog(this, "Bạn không được phép chỉnh sửa mã món ăn");
            txtMaMonAn.setText(tblThongTinMonAn.getValueAt(viTri, 0).toString());
        } else if (!kiemTraLuuThongTinMonAn()) {

        } else {
            return true;
        }
        return false;
    }

    public boolean kiemTraXoaThongTinMonAn() {
        int viTri = tblThongTinMonAn.getSelectedRow();
        if (tblThongTinMonAn.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else if (viTri < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng cần xóa");
        } else if (tblThongTinMonAn.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chỉ chọn 1 hàng để xóa");
        } else {
            return true;
        }
        return false;
    }

    public void timKiemPhanPhoiSanPham() {
        boolean kiemTra = false;
        int viTri = 0;
        tblPhanPhoiSanPham.clearSelection();
        if (txtTimKiemThongTinNhaPhanPhoi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã món ăn để tìm kiếm thông tin");
            txtTimKiemThongTinNhaPhanPhoi.requestFocus();
        } else {
            for (int i = 0; i < danhSach.size(); i++) {
                if (danhSach.get(i).maMonAn.equalsIgnoreCase(txtTimKiemThongTinNhaPhanPhoi.getText())) {
                    kiemTra = true;
                    viTri = i;
                    tblPhanPhoiSanPham.addRowSelectionInterval(i, i);
                }
            }
            if (kiemTra == true) {
                JOptionPane.showMessageDialog(this, "Thông tin nhà phân phối đã được tìm thấy");
                Rectangle cell = tblPhanPhoiSanPham.getCellRect(viTri, 0, true);
                tblPhanPhoiSanPham.scrollRectToVisible(cell);
                hienThiPhanPhoiSanPham();
            } else {
                JOptionPane.showMessageDialog(this, "Thông tin nhà phân phối không có trong bảng");
            }
        }
    }

    public void resetFormPhanPhoiSanPham() {
        txtMaMonAnPhanPhoi.setText("");
        txtTenMonAnPhanPhoi.setText("");
        txtTenNhaPhanPhoi.setText("");
        txtGiaNhap.setText("");
        txtSoLuongPhanPhoi.setText("");
        txtDiaChi.setText("");
        lblIconMonAn.setIcon(null);
        tblPhanPhoiSanPham.clearSelection();
        lblIconMonAn.setIcon(new ImageIcon("src\\com\\poly\\icon\\photo.png"));
        lblIconMonAn.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        txtMaMonAnPhanPhoi.requestFocus();
        viTriPhanPhoiSanPham = 0;
    }

    public void hienThiPhanPhoiSanPham() {
        int viTri = tblPhanPhoiSanPham.getSelectedRow();
        PhanPhoiSanPham ppsp = danhSach.get(viTri);
        if (ppsp.iconHinhAnh.equals("") || ppsp.iconHinhAnh.equals(anhMacDinh)) {
            ppsp.iconHinhAnh = anhMacDinh;
            lblIconMonAn.setIcon(new ImageIcon(ppsp.iconHinhAnh));
        } else {
            lblIconMonAn.setIcon(new ImageIcon(new ImageIcon(ppsp.iconHinhAnh).getImage().getScaledInstance(lblIconMonAn.getWidth(), lblIconMonAn.getHeight(), Image.SCALE_DEFAULT)));
        }
        txtMaMonAnPhanPhoi.setText(tblPhanPhoiSanPham.getValueAt(viTri, 0).toString());
        txtTenMonAnPhanPhoi.setText(tblPhanPhoiSanPham.getValueAt(viTri, 1).toString());
        txtTenNhaPhanPhoi.setText(tblPhanPhoiSanPham.getValueAt(viTri, 2).toString());
        txtGiaNhap.setText(tblPhanPhoiSanPham.getValueAt(viTri, 3).toString());
        txtSoLuongPhanPhoi.setText(tblPhanPhoiSanPham.getValueAt(viTri, 4).toString());
        txtDiaChi.setText(tblPhanPhoiSanPham.getValueAt(viTri, 5).toString());
    }

    public void fillPhanPhoiSanPham() {
        try {
            rs = st.executeQuery(sqlSelectPhanPhoiSanPham);
            danhSach.clear();
            while (rs.next()) {
                String ID = rs.getString(1);
                String maMonAn = rs.getString(2);
                String tenMonAn = rs.getString(3);
                String tenNhaPhanPhoi = rs.getString(4);
                int giaNhap = rs.getInt(5);
                int soLuong = rs.getInt(6);
                String diaChi = rs.getString(7);
                String srcHinhAnh = rs.getString(8);
                danhSach.add(new PhanPhoiSanPham(ID, maMonAn, tenMonAn, tenNhaPhanPhoi, giaNhap, soLuong, diaChi, srcHinhAnh));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu trong file");
        }
        modelPhanPhoiSanPham.setRowCount(0);
        for (PhanPhoiSanPham ppsp : danhSach) {
            modelPhanPhoiSanPham.addRow(new Object[]{ppsp.maMonAn, ppsp.tenMonAn, ppsp.tenNhaPhanPhoi, ppsp.giaNhap, ppsp.soLuong, ppsp.diaChi});
        }
    }

    private void loadDuLieuPhanPhoiSanPham() {
        modelPhanPhoiSanPham = new DefaultTableModel(); // tạo model mới
        modelPhanPhoiSanPham.addColumn("MÃ MÓN ĂN"); // tạo cột cho bảng
        modelPhanPhoiSanPham.addColumn("TÊN MÓN ĂN"); // tạo cột cho bảng
        modelPhanPhoiSanPham.addColumn("TÊN PP"); // tạo cột cho bảng
        modelPhanPhoiSanPham.addColumn("GIÁ NHẬP");// tạo cột cho bảng
        modelPhanPhoiSanPham.addColumn("SỐ LƯỢNG");// tạo cột cho bảng
        modelPhanPhoiSanPham.addColumn("ĐỊA CHỈ");// tạo cột cho bảng
        tblPhanPhoiSanPham.setModel(modelPhanPhoiSanPham);
        //Thêm dữ liệu vào danh sách
        try {
            rs = st.executeQuery(sqlSelectPhanPhoiSanPham);
            danhSach.clear();
            while (rs.next()) {
                String ID = rs.getString(1);
                String maMonAn = rs.getString(2);
                String tenMonAn = rs.getString(3);
                String tenNhaPhanPhoi = rs.getString(4);
                int giaNhap = rs.getInt(5);
                int soLuong = rs.getInt(6);
                String diaChi = rs.getString(7);
                String srcHinhAnh = rs.getString(8);
                danhSach.add(new PhanPhoiSanPham(ID, maMonAn, tenMonAn, tenNhaPhanPhoi, giaNhap, soLuong, diaChi, srcHinhAnh));
            }
        } catch (SQLException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "Không có dữ liệu trong file");
        }
        //Thêm các dòng dữ liệu vào model
        for (PhanPhoiSanPham ppsp : danhSach) {
            modelPhanPhoiSanPham.addRow(new Object[]{ppsp.maMonAn, ppsp.tenMonAn, ppsp.tenNhaPhanPhoi, ppsp.giaNhap, ppsp.soLuong, ppsp.diaChi});
            tblPhanPhoiSanPham.setModel(modelPhanPhoiSanPham);
        }
    }

    public boolean kiemTraLuuPhanPhoiSanPham() {
        boolean kiemTra = false;
        boolean kiemTraThongTin = false;
        boolean kiemTraGia = true;
        boolean kiemTraSoLuong = true;
        int soLuongKho = 0;
        int soLuongPhanPhoi = 0;
        String kiemTraMaMonAn = "^MA+\\d+(\\d+)+";
        try {
            Integer.parseInt(txtGiaNhap.getText());
        } catch (NumberFormatException e) {
            kiemTraGia = false;
        }
        try {
            Integer.parseInt(txtSoLuongPhanPhoi.getText());
        } catch (NumberFormatException e) {
            kiemTraSoLuong = false;
        }
        for (ThongTinMonAn ttma : list) {
            if (ttma.maMonAn.equalsIgnoreCase(txtMaMonAnPhanPhoi.getText())) {
                kiemTraThongTin = true;
                soLuongKho = soLuongKho + ttma.soLuong;
            }
        }
        for (PhanPhoiSanPham ppsp : danhSach) {
            if (ppsp.maMonAn.equalsIgnoreCase(txtMaMonAnPhanPhoi.getText()) && ppsp.tenNhaPhanPhoi.equalsIgnoreCase(txtTenNhaPhanPhoi.getText())) {
                kiemTra = true;
            }
        }
        for (PhanPhoiSanPham ppsp : danhSach) {
            if (ppsp.maMonAn.equalsIgnoreCase(txtMaMonAnPhanPhoi.getText()) && kiemTraSoLuong == true) {
                soLuongPhanPhoi = soLuongPhanPhoi + ppsp.soLuong + Integer.parseInt(txtSoLuongPhanPhoi.getText().trim());
            }
        }
        if (txtMaMonAnPhanPhoi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Mã món ăn không được để trống");
            txtMaMonAnPhanPhoi.requestFocus();
        } else if (txtTenMonAnPhanPhoi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên món ăn không được để trống");
            txtTenMonAnPhanPhoi.requestFocus();
        } else if (txtTenNhaPhanPhoi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên nhà phân phối không được để trống");
            txtTenNhaPhanPhoi.requestFocus();
        } else if (txtGiaNhap.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Giá nhập không được để trống");
            txtGiaNhap.requestFocus();
        } else if (txtSoLuongPhanPhoi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Số lượng không được để trống");
            txtSoLuongPhanPhoi.requestFocus();
        } else if (txtDiaChi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống");
            txtDiaChi.requestFocus();
        } else if (!txtMaMonAnPhanPhoi.getText().matches(kiemTraMaMonAn)) {
            JOptionPane.showMessageDialog(this, "Mã món ăn không đúng định dạng (VD: MA01)", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaMonAnPhanPhoi.requestFocus();
        } else if (kiemTraGia == false) {
            JOptionPane.showMessageDialog(this, "Giá phải là số", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaNhap.requestFocus();
        } else if (kiemTraSoLuong == false) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoLuongPhanPhoi.requestFocus();
        } else if (Integer.parseInt(txtGiaNhap.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "Giá phải lớn hơn 0", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaNhap.requestFocus();
        } else if (Integer.parseInt(txtSoLuongPhanPhoi.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoLuongPhanPhoi.requestFocus();
        } else if (kiemTra == true) {
            JOptionPane.showMessageDialog(this, "Thông tin nhà phân phối đã tồn tại");
            txtMaMonAnPhanPhoi.requestFocus();
        } else if (kiemTraThongTin == false) {
            JOptionPane.showMessageDialog(this, "Thông tin món ăn không có trong danh sách");
            txtMaMonAnPhanPhoi.requestFocus();
        } else if (soLuongKho < soLuongPhanPhoi) {
            JOptionPane.showMessageDialog(this, "Sản phẩm đã hết hàng trong kho");
        } else {
            return true;
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        btnTrangChu = new javax.swing.JButton();
        btnThongTinMonAn = new javax.swing.JButton();
        btnThoatChuongTrinh = new javax.swing.JButton();
        btnPhanPhoiSanPham = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblAnhHome1 = new javax.swing.JLabel();
        lblBannerHome = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblAnhHome2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnTimKiemThongTinMonAn = new javax.swing.JButton();
        txtTimKiemThongTinMonAn = new javax.swing.JTextField();
        txtMaMonAn = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblMaMonAn = new javax.swing.JLabel();
        lblTenMonAn = new javax.swing.JLabel();
        txtTenMonAn = new javax.swing.JTextField();
        lblGia = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        txtNgayCapNhat = new javax.swing.JTextField();
        lblNgayCapNhat = new javax.swing.JLabel();
        lblSoLuong = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblThongTinMonAn = new javax.swing.JTable();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLuuDuLieu = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrevious = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtMoTa = new javax.swing.JTextArea();
        lblIconThongTinMonAn = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnTimKiemNhaPhanPhoi = new javax.swing.JButton();
        txtTimKiemThongTinNhaPhanPhoi = new javax.swing.JTextField();
        txtMaMonAnPhanPhoi = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        lblMaMonAnPhanPhoi = new javax.swing.JLabel();
        lblTenMonAnPhanPhoi = new javax.swing.JLabel();
        txtTenMonAnPhanPhoi = new javax.swing.JTextField();
        lblTenNhaPhanPhoi = new javax.swing.JLabel();
        txtTenNhaPhanPhoi = new javax.swing.JTextField();
        txtGiaNhap = new javax.swing.JTextField();
        lblGiaNhap = new javax.swing.JLabel();
        lblSoLuongPhanPhoi = new javax.swing.JLabel();
        txtSoLuongPhanPhoi = new javax.swing.JTextField();
        btnThem1 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblPhanPhoiSanPham = new javax.swing.JTable();
        btnCapNhat1 = new javax.swing.JButton();
        btnXoa1 = new javax.swing.JButton();
        btnLuuDuLieu1 = new javax.swing.JButton();
        btnFirst1 = new javax.swing.JButton();
        btnPrevious1 = new javax.swing.JButton();
        btnNext1 = new javax.swing.JButton();
        btnLast1 = new javax.swing.JButton();
        lblDiaChi = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        lblIconMonAn = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/logo.png"))); // NOI18N

        btnTrangChu.setBackground(new java.awt.Color(255, 255, 255));
        btnTrangChu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnTrangChu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/trangchu.png"))); // NOI18N
        btnTrangChu.setText("TRANG GIỚI THIỆU");
        btnTrangChu.setBorder(null);
        btnTrangChu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTrangChuMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTrangChuMouseExited(evt);
            }
        });
        btnTrangChu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrangChuActionPerformed(evt);
            }
        });

        btnThongTinMonAn.setBackground(new java.awt.Color(255, 255, 255));
        btnThongTinMonAn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThongTinMonAn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/food-cart.png"))); // NOI18N
        btnThongTinMonAn.setText("THÔNG TIN MÓN ĂN");
        btnThongTinMonAn.setBorder(null);
        btnThongTinMonAn.setSelected(true);
        btnThongTinMonAn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnThongTinMonAnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnThongTinMonAnMouseExited(evt);
            }
        });
        btnThongTinMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongTinMonAnActionPerformed(evt);
            }
        });

        btnThoatChuongTrinh.setBackground(new java.awt.Color(255, 255, 255));
        btnThoatChuongTrinh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThoatChuongTrinh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/exit.png"))); // NOI18N
        btnThoatChuongTrinh.setText("THOÁT CHƯƠNG TRÌNH");
        btnThoatChuongTrinh.setBorder(null);
        btnThoatChuongTrinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnThoatChuongTrinhMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnThoatChuongTrinhMouseExited(evt);
            }
        });
        btnThoatChuongTrinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatChuongTrinhActionPerformed(evt);
            }
        });

        btnPhanPhoiSanPham.setBackground(new java.awt.Color(255, 255, 255));
        btnPhanPhoiSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnPhanPhoiSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/distributor.png"))); // NOI18N
        btnPhanPhoiSanPham.setText("PHÂN PHỐI SẢN PHẨM");
        btnPhanPhoiSanPham.setBorder(null);
        btnPhanPhoiSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPhanPhoiSanPhamMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPhanPhoiSanPhamMouseExited(evt);
            }
        });
        btnPhanPhoiSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhanPhoiSanPhamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnTrangChu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnThongTinMonAn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLogo)
                .addGap(59, 59, 59))
            .addComponent(btnThoatChuongTrinh, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
            .addComponent(btnPhanPhoiSanPham, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThongTinMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPhanPhoiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThoatChuongTrinh, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.CardLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        lblAnhHome1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/home1.jpg"))); // NOI18N

        lblBannerHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/banner_home.jpg"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel2.setText("ALACARTE (GỌI MÓN)");

        lblAnhHome2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/home2.jpg"))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel3.setText("ALACARTE (GỌI MÓN)");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBannerHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnhHome1, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblAnhHome2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(142, 142, 142))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(lblBannerHome, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAnhHome1, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(lblAnhHome2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addContainerGap())
        );

        jPanel3.add(jPanel4, "card3");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnTimKiemThongTinMonAn.setBackground(new java.awt.Color(255, 255, 255));
        btnTimKiemThongTinMonAn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/timkiem.png"))); // NOI18N
        btnTimKiemThongTinMonAn.setBorder(null);
        btnTimKiemThongTinMonAn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTimKiemThongTinMonAnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTimKiemThongTinMonAnMouseExited(evt);
            }
        });
        btnTimKiemThongTinMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemThongTinMonAnActionPerformed(evt);
            }
        });

        txtTimKiemThongTinMonAn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTimKiemThongTinMonAn.setForeground(new java.awt.Color(204, 204, 204));
        txtTimKiemThongTinMonAn.setText("Nhập mã món ăn cần tìm kiếm");
        txtTimKiemThongTinMonAn.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));
        txtTimKiemThongTinMonAn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimKiemThongTinMonAnFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimKiemThongTinMonAnFocusLost(evt);
            }
        });
        txtTimKiemThongTinMonAn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimKiemThongTinMonAnKeyPressed(evt);
            }
        });

        txtMaMonAn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaMonAn.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));
        txtMaMonAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaMonAnActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 204, 204));
        jLabel4.setText("QUẢN LÝ THÔNG TIN MÓN ĂN");

        lblMaMonAn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaMonAn.setText("Mã món ăn");

        lblTenMonAn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenMonAn.setText("Tên món ăn");

        txtTenMonAn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTenMonAn.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));

        lblGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGia.setText("Giá");

        txtGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGia.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));

        txtNgayCapNhat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtNgayCapNhat.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));

        lblNgayCapNhat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNgayCapNhat.setText("Ngày cập nhật");

        lblSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSoLuong.setText("Số lượng");

        txtSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoLuong.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));

        btnThem.setBackground(new java.awt.Color(255, 255, 255));
        btnThem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/add.png"))); // NOI18N
        btnThem.setToolTipText("Làm mới");
        btnThem.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        tblThongTinMonAn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblThongTinMonAn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblThongTinMonAn.setFillsViewportHeight(true);
        tblThongTinMonAn.setGridColor(new java.awt.Color(0, 204, 204));
        tblThongTinMonAn.setOpaque(false);
        tblThongTinMonAn.setRowHeight(30);
        tblThongTinMonAn.setSelectionBackground(new java.awt.Color(0, 204, 204));
        tblThongTinMonAn.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblThongTinMonAn.setShowGrid(false);
        tblThongTinMonAn.setShowHorizontalLines(true);
        tblThongTinMonAn.setShowVerticalLines(true);
        tblThongTinMonAn.setSurrendersFocusOnKeystroke(true);
        tblThongTinMonAn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThongTinMonAnMouseClicked(evt);
            }
        });
        tblThongTinMonAn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblThongTinMonAnKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblThongTinMonAn);

        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/edit.png"))); // NOI18N
        btnCapNhat.setToolTipText("Cập nhật");
        btnCapNhat.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/delete.png"))); // NOI18N
        btnXoa.setToolTipText("Xóa dữ liệu");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLuuDuLieu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/save.png"))); // NOI18N
        btnLuuDuLieu.setToolTipText("Lưu dữ liệu");
        btnLuuDuLieu.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnLuuDuLieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuDuLieuActionPerformed(evt);
            }
        });

        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/first.png"))); // NOI18N
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/previous.png"))); // NOI18N
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/next.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/last.png"))); // NOI18N
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        jSeparator2.setBackground(new java.awt.Color(0, 204, 204));
        jSeparator2.setForeground(new java.awt.Color(0, 204, 204));
        jSeparator2.setOpaque(true);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Mô tả");

        jScrollPane5.setToolTipText("");
        jScrollPane5.setAutoscrolls(true);

        txtMoTa.setColumns(20);
        txtMoTa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMoTa.setLineWrap(true);
        txtMoTa.setRows(5);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setAutoscrolls(false);
        jScrollPane5.setViewportView(txtMoTa);

        lblIconThongTinMonAn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconThongTinMonAn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/photo.png"))); // NOI18N
        lblIconThongTinMonAn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblIconThongTinMonAn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblIconThongTinMonAnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jSeparator2)
                        .addContainerGap())
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtTimKiemThongTinMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTimKiemThongTinMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLuuDuLieu)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(253, 253, 253))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtMaMonAn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                .addComponent(txtTenMonAn)
                                .addComponent(txtGia, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNgayCapNhat, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addComponent(lblSoLuong)
                            .addComponent(jLabel1)
                            .addComponent(lblMaMonAn)
                            .addComponent(lblGia)
                            .addComponent(lblNgayCapNhat)
                            .addComponent(lblTenMonAn))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jScrollPane1))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblIconThongTinMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 828, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 2, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(378, 378, 378))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKiemThongTinMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnTimKiemThongTinMonAn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblMaMonAn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTenMonAn)
                        .addGap(7, 7, 7)
                        .addComponent(txtTenMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblNgayCapNhat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNgayCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblIconThongTinMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblSoLuong)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnThem)
                    .addComponent(btnCapNhat)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLuuDuLieu))
                .addContainerGap())
        );

        jPanel3.add(jPanel5, "card2");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        btnTimKiemNhaPhanPhoi.setBackground(new java.awt.Color(255, 255, 255));
        btnTimKiemNhaPhanPhoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/timkiem.png"))); // NOI18N
        btnTimKiemNhaPhanPhoi.setBorder(null);
        btnTimKiemNhaPhanPhoi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnTimKiemNhaPhanPhoiMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnTimKiemNhaPhanPhoiMouseExited(evt);
            }
        });
        btnTimKiemNhaPhanPhoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemNhaPhanPhoiActionPerformed(evt);
            }
        });

        txtTimKiemThongTinNhaPhanPhoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTimKiemThongTinNhaPhanPhoi.setForeground(new java.awt.Color(204, 204, 204));
        txtTimKiemThongTinNhaPhanPhoi.setText("Nhập mã món ăn cần tìm kiếm");
        txtTimKiemThongTinNhaPhanPhoi.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));
        txtTimKiemThongTinNhaPhanPhoi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimKiemThongTinNhaPhanPhoiFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimKiemThongTinNhaPhanPhoiFocusLost(evt);
            }
        });
        txtTimKiemThongTinNhaPhanPhoi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimKiemThongTinNhaPhanPhoiKeyPressed(evt);
            }
        });

        txtMaMonAnPhanPhoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaMonAnPhanPhoi.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));
        txtMaMonAnPhanPhoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaMonAnPhanPhoiActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 204, 204));
        jLabel8.setText("QUẢN LÝ THÔNG TIN PHÂN PHỐI SẢN PHẨM");

        lblMaMonAnPhanPhoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaMonAnPhanPhoi.setText("Mã món ăn");

        lblTenMonAnPhanPhoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenMonAnPhanPhoi.setText("Tên món ăn");

        txtTenMonAnPhanPhoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTenMonAnPhanPhoi.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));

        lblTenNhaPhanPhoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenNhaPhanPhoi.setText("Tên nhà phân phối");

        txtTenNhaPhanPhoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTenNhaPhanPhoi.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));

        txtGiaNhap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtGiaNhap.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));

        lblGiaNhap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGiaNhap.setText("Giá nhập");

        lblSoLuongPhanPhoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSoLuongPhanPhoi.setText("Số lượng");

        txtSoLuongPhanPhoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtSoLuongPhanPhoi.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 204, 204)));

        btnThem1.setBackground(new java.awt.Color(255, 255, 255));
        btnThem1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/add.png"))); // NOI18N
        btnThem1.setToolTipText("Làm mới");
        btnThem1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnThem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem1ActionPerformed(evt);
            }
        });

        tblPhanPhoiSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblPhanPhoiSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPhanPhoiSanPham.setFillsViewportHeight(true);
        tblPhanPhoiSanPham.setGridColor(new java.awt.Color(0, 204, 204));
        tblPhanPhoiSanPham.setOpaque(false);
        tblPhanPhoiSanPham.setRowHeight(30);
        tblPhanPhoiSanPham.setSelectionBackground(new java.awt.Color(0, 204, 204));
        tblPhanPhoiSanPham.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tblPhanPhoiSanPham.setShowGrid(false);
        tblPhanPhoiSanPham.setShowHorizontalLines(true);
        tblPhanPhoiSanPham.setShowVerticalLines(true);
        tblPhanPhoiSanPham.setSurrendersFocusOnKeystroke(true);
        tblPhanPhoiSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPhanPhoiSanPhamMouseClicked(evt);
            }
        });
        tblPhanPhoiSanPham.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPhanPhoiSanPhamKeyPressed(evt);
            }
        });
        jScrollPane7.setViewportView(tblPhanPhoiSanPham);

        btnCapNhat1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/edit.png"))); // NOI18N
        btnCapNhat1.setToolTipText("Cập nhật");
        btnCapNhat1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCapNhat1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhat1ActionPerformed(evt);
            }
        });

        btnXoa1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/delete.png"))); // NOI18N
        btnXoa1.setToolTipText("Xóa dữ liệu");
        btnXoa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoa1ActionPerformed(evt);
            }
        });

        btnLuuDuLieu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/save.png"))); // NOI18N
        btnLuuDuLieu1.setToolTipText("Lưu dữ liệu");
        btnLuuDuLieu1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnLuuDuLieu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuDuLieu1ActionPerformed(evt);
            }
        });

        btnFirst1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/first.png"))); // NOI18N
        btnFirst1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirst1ActionPerformed(evt);
            }
        });

        btnPrevious1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/previous.png"))); // NOI18N
        btnPrevious1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevious1ActionPerformed(evt);
            }
        });

        btnNext1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/next.png"))); // NOI18N
        btnNext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNext1ActionPerformed(evt);
            }
        });

        btnLast1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/last.png"))); // NOI18N
        btnLast1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLast1ActionPerformed(evt);
            }
        });

        lblDiaChi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDiaChi.setText("Địa chỉ");

        jSeparator4.setBackground(new java.awt.Color(0, 204, 204));
        jSeparator4.setForeground(new java.awt.Color(0, 204, 204));
        jSeparator4.setOpaque(true);

        jScrollPane8.setToolTipText("");
        jScrollPane8.setAutoscrolls(true);

        txtDiaChi.setColumns(20);
        txtDiaChi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtDiaChi.setLineWrap(true);
        txtDiaChi.setRows(5);
        txtDiaChi.setWrapStyleWord(true);
        txtDiaChi.setAutoscrolls(false);
        jScrollPane8.setViewportView(txtDiaChi);

        lblIconMonAn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIconMonAn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/poly/icon/photo.png"))); // NOI18N
        lblIconMonAn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblIconMonAn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblIconMonAnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jSeparator4)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtTimKiemThongTinNhaPhanPhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTimKiemNhaPhanPhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnThem1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCapNhat1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoa1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLuuDuLieu1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFirst1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnPrevious1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnLast1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(253, 253, 253))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtMaMonAnPhanPhoi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                                .addComponent(txtTenMonAnPhanPhoi)
                                .addComponent(txtTenNhaPhanPhoi, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtGiaNhap, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSoLuongPhanPhoi, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addComponent(lblSoLuongPhanPhoi)
                            .addComponent(lblDiaChi)
                            .addComponent(lblMaMonAnPhanPhoi)
                            .addComponent(lblTenNhaPhanPhoi)
                            .addComponent(lblGiaNhap)
                            .addComponent(lblTenMonAnPhanPhoi))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jScrollPane7))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblIconMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 828, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 2, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(265, 265, 265))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKiemThongTinNhaPhanPhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnTimKiemNhaPhanPhoi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lblMaMonAnPhanPhoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaMonAnPhanPhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTenMonAnPhanPhoi)
                        .addGap(7, 7, 7)
                        .addComponent(txtTenMonAnPhanPhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTenNhaPhanPhoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTenNhaPhanPhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGiaNhap)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(lblIconMonAn, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(lblSoLuongPhanPhoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoLuongPhanPhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDiaChi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnFirst1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPrevious1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNext1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLast1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnThem1)
                    .addComponent(btnCapNhat1)
                    .addComponent(btnXoa1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLuuDuLieu1))
                .addContainerGap())
        );

        jPanel3.add(jPanel6, "card5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnTrangChuMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTrangChuMouseExited
        btnTrangChu.setBackground(Color.white);
    }//GEN-LAST:event_btnTrangChuMouseExited

    private void btnTrangChuMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTrangChuMouseEntered
        btnTrangChu.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnTrangChuMouseEntered

    private void btnThongTinMonAnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThongTinMonAnMouseEntered
        btnThongTinMonAn.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnThongTinMonAnMouseEntered

    private void btnThongTinMonAnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThongTinMonAnMouseExited
        btnThongTinMonAn.setBackground(Color.white);
    }//GEN-LAST:event_btnThongTinMonAnMouseExited

    private void btnTimKiemThongTinMonAnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTimKiemThongTinMonAnMouseEntered
        btnTimKiemThongTinMonAn.setBackground(new Color(250, 250, 250));
    }//GEN-LAST:event_btnTimKiemThongTinMonAnMouseEntered

    private void btnTimKiemThongTinMonAnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTimKiemThongTinMonAnMouseExited
        btnTimKiemThongTinMonAn.setBackground(Color.white);
    }//GEN-LAST:event_btnTimKiemThongTinMonAnMouseExited

    private void btnTrangChuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrangChuActionPerformed
        CardLayout layout = (CardLayout) jPanel3.getLayout();
        layout.first(jPanel3);
    }//GEN-LAST:event_btnTrangChuActionPerformed

    private void btnThongTinMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongTinMonAnActionPerformed
        CardLayout layout = (CardLayout) jPanel3.getLayout();
        layout.show(jPanel3, "card2");
        txtMaMonAn.requestFocus();
    }//GEN-LAST:event_btnThongTinMonAnActionPerformed

    private void txtTimKiemThongTinMonAnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKiemThongTinMonAnFocusLost
        if (txtTimKiemThongTinMonAn.getText().equals("")) {
            txtTimKiemThongTinMonAn.setText("Nhập mã món ăn cần tìm kiếm");
            txtTimKiemThongTinMonAn.setForeground(new Color(204, 204, 204));
        }
    }//GEN-LAST:event_txtTimKiemThongTinMonAnFocusLost

    private void txtTimKiemThongTinMonAnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKiemThongTinMonAnFocusGained
        if (txtTimKiemThongTinMonAn.getText().equals("Nhập mã món ăn cần tìm kiếm")) {
            txtTimKiemThongTinMonAn.setText("");
            txtTimKiemThongTinMonAn.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtTimKiemThongTinMonAnFocusGained

    private void btnTimKiemThongTinMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemThongTinMonAnActionPerformed
        if (txtTimKiemThongTinMonAn.getText().equals("Nhập mã món ăn cần tìm kiếm")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã món ăn cần tìm kiếm");
            txtTimKiemThongTinMonAn.requestFocus();
        } else {
            timKiemThongTinMonAn();
        }
    }//GEN-LAST:event_btnTimKiemThongTinMonAnActionPerformed

    private void tblThongTinMonAnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThongTinMonAnMouseClicked
        if (tblThongTinMonAn.getRowCount() == 0) {

        } else {
            hienThiThongTinMonAn();
            viTriThongTinMonAn = tblThongTinMonAn.getSelectedRow();
        }
    }//GEN-LAST:event_tblThongTinMonAnMouseClicked

    private void btnLuuDuLieuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuDuLieuActionPerformed
        if (!kiemTraLuuThongTinMonAn()) {

        } else {
            int ketQua = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn thêm dòng dữ liệu mới của món ăn " + (txtMaMonAn.getText()) + " không?", "Thông báo xác nhận", JOptionPane.YES_NO_OPTION);
            if (ketQua == JOptionPane.YES_OPTION) {
                try {
                    ps = con.prepareStatement(sqlInsertThongTinMonAn);
                    ps.setString(1, txtMaMonAn.getText().trim());
                    ps.setString(2, txtTenMonAn.getText().trim());
                    ps.setInt(3, Integer.parseInt(txtGia.getText()));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    ps.setDate(4, java.sql.Date.valueOf(sdf2.format(sdf.parse(txtNgayCapNhat.getText()))));
                    ps.setInt(5, Integer.parseInt(txtSoLuong.getText()));
                    ps.setString(6, txtMoTa.getText());
                    ps.setString(7, diaChiHinhAnh);
                    ps.execute();

                } catch (SQLException e) {
                    System.out.println(e);
                } catch (ParseException e) {
                }
                JOptionPane.showMessageDialog(this, "Thêm món ăn thành công");
                fillThongTinMonAn();
                resetFormThongTinMonAn();
            }

        }
    }//GEN-LAST:event_btnLuuDuLieuActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int viTri = tblThongTinMonAn.getSelectedRow();
        if (!kiemTraXoaThongTinMonAn()) {

        } else {
            int ketQua = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa dòng dữ liệu của món ăn " + (tblThongTinMonAn.getValueAt(viTri, 0).toString()) + " không?", "Thông báo xác nhận", JOptionPane.YES_NO_OPTION);
            if (ketQua == JOptionPane.YES_OPTION) {
                try {
                    ps = con.prepareStatement(sqlDeleteThongTinMonAn);
                    ps.setString(1, tblThongTinMonAn.getValueAt(viTri, 0).toString());
                    ps.execute();

                } catch (SQLException e) {
                    System.out.println(e);
                }
                fillThongTinMonAn();
                fillPhanPhoiSanPham();
                resetFormThongTinMonAn();
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        resetFormThongTinMonAn();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        int viTri = tblThongTinMonAn.getSelectedRow();
        if (!kiemTraCapNhatThongTinMonAn()) {
        } else {
            int ketQua = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn cập nhật dòng dữ liệu của món ăn " + (tblThongTinMonAn.getValueAt(viTri, 0).toString()) + " không?", "Thông báo xác nhận", JOptionPane.YES_NO_OPTION);
            if (ketQua == JOptionPane.YES_OPTION) {
                ThongTinMonAn ttma = list.get(viTri);
                if (!diaChiHinhAnh.equals("")) {
                    ttma.iconHinhAnh = diaChiHinhAnh;
                }
                try {
                    ps = con.prepareStatement(sqlUpdateThongTinMonAn);
                    ps.setString(1, txtTenMonAn.getText().trim());
                    ps.setInt(2, Integer.parseInt(txtGia.getText().trim()));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                    ps.setDate(3, java.sql.Date.valueOf(sdf2.format(sdf.parse(txtNgayCapNhat.getText().trim()))));
                    ps.setInt(4, Integer.parseInt(txtSoLuong.getText().trim()));
                    ps.setString(5, txtMoTa.getText().trim());
                    ps.setString(6, ttma.iconHinhAnh);
                    ps.setString(7, ttma.maMonAn);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e);
                } catch (ParseException e) {
                }
                JOptionPane.showMessageDialog(this, "Cập nhật dữ liệu cho món ăn " + tblThongTinMonAn.getValueAt(viTri, 0).toString() + " thành công");
                fillThongTinMonAn();
                resetFormThongTinMonAn();
            }
        }
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        if (tblThongTinMonAn.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else {
            viTriThongTinMonAn = 0;
            tblThongTinMonAn.setRowSelectionInterval(viTriThongTinMonAn, viTriThongTinMonAn);
            hienThiThongTinMonAn();
        }
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        int viTri = tblThongTinMonAn.getSelectedRow();
        if (tblThongTinMonAn.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else if (viTri < 0) {
            tblThongTinMonAn.setRowSelectionInterval(viTriThongTinMonAn, viTriThongTinMonAn);
            hienThiThongTinMonAn();
        } else {
            viTriThongTinMonAn--;
            if (viTriThongTinMonAn < 0) {
                viTriThongTinMonAn = list.size() - 1;
            }
            tblThongTinMonAn.setRowSelectionInterval(viTriThongTinMonAn, viTriThongTinMonAn);
            hienThiThongTinMonAn();
        }
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        int viTri = tblThongTinMonAn.getSelectedRow();
        if (tblThongTinMonAn.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else if (viTri < 0) {
            tblThongTinMonAn.setRowSelectionInterval(viTriThongTinMonAn, viTriThongTinMonAn);
            hienThiThongTinMonAn();
        } else {
            viTriThongTinMonAn++;
            if (viTriThongTinMonAn >= list.size()) {
                viTriThongTinMonAn = 0;
            }
            tblThongTinMonAn.setRowSelectionInterval(viTriThongTinMonAn, viTriThongTinMonAn);
            hienThiThongTinMonAn();
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        if (tblThongTinMonAn.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else {
            viTriThongTinMonAn = list.size() - 1;
            tblThongTinMonAn.setRowSelectionInterval(viTriThongTinMonAn, viTriThongTinMonAn);
            hienThiThongTinMonAn();
        }
    }//GEN-LAST:event_btnLastActionPerformed

    private void txtTimKiemThongTinMonAnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemThongTinMonAnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnTimKiemThongTinMonAn.doClick();
        }
    }//GEN-LAST:event_txtTimKiemThongTinMonAnKeyPressed

    private void btnThoatChuongTrinhMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThoatChuongTrinhMouseEntered
        btnThoatChuongTrinh.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnThoatChuongTrinhMouseEntered

    private void btnThoatChuongTrinhMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThoatChuongTrinhMouseExited
        btnThoatChuongTrinh.setBackground(Color.white);
    }//GEN-LAST:event_btnThoatChuongTrinhMouseExited

    private void btnThoatChuongTrinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatChuongTrinhActionPerformed
        int ketQua = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn thoát khỏi chương trình không?", "Thông báo xác nhận", JOptionPane.YES_NO_OPTION);
        if (ketQua == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_btnThoatChuongTrinhActionPerformed

    private void btnPhanPhoiSanPhamMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPhanPhoiSanPhamMouseEntered
        btnPhanPhoiSanPham.setBackground(new Color(0, 204, 204));
    }//GEN-LAST:event_btnPhanPhoiSanPhamMouseEntered

    private void btnPhanPhoiSanPhamMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPhanPhoiSanPhamMouseExited
        btnPhanPhoiSanPham.setBackground(Color.white);
    }//GEN-LAST:event_btnPhanPhoiSanPhamMouseExited

    private void btnPhanPhoiSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhanPhoiSanPhamActionPerformed
        CardLayout layout = (CardLayout) jPanel3.getLayout();
        layout.last(jPanel3);
        txtMaMonAnPhanPhoi.requestFocus();
    }//GEN-LAST:event_btnPhanPhoiSanPhamActionPerformed

    private void tblThongTinMonAnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblThongTinMonAnKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            btnXoa.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            btnThem.doClick();
        }

    }//GEN-LAST:event_tblThongTinMonAnKeyPressed

    private void txtMaMonAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaMonAnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaMonAnActionPerformed

    private void lblIconThongTinMonAnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblIconThongTinMonAnMouseClicked
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("files", ImageIO.getReaderFileSuffixes());
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        diaChiHinhAnh = String.valueOf(chooser.getSelectedFile());
        try {
            BufferedImage img;
            img = ImageIO.read(new File(f.getAbsolutePath()));
            Image img1 = img.getScaledInstance(lblIconThongTinMonAn.getWidth(), lblIconThongTinMonAn.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon format = new ImageIcon(img1);
            lblIconThongTinMonAn.setIcon(format);
        } catch (IOException e) {
        }
    }//GEN-LAST:event_lblIconThongTinMonAnMouseClicked

    private void btnTimKiemNhaPhanPhoiMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTimKiemNhaPhanPhoiMouseEntered
        btnTimKiemNhaPhanPhoi.setBackground(new Color(250, 250, 250));
    }//GEN-LAST:event_btnTimKiemNhaPhanPhoiMouseEntered

    private void btnTimKiemNhaPhanPhoiMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTimKiemNhaPhanPhoiMouseExited
        btnTimKiemNhaPhanPhoi.setBackground(Color.white);
    }//GEN-LAST:event_btnTimKiemNhaPhanPhoiMouseExited

    private void btnTimKiemNhaPhanPhoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemNhaPhanPhoiActionPerformed
        if (txtTimKiemThongTinNhaPhanPhoi.getText().equals("Nhập mã món ăn cần tìm kiếm")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã món ăn cần tìm kiếm");
            txtTimKiemThongTinNhaPhanPhoi.requestFocus();
        } else {
            timKiemPhanPhoiSanPham();
        }
    }//GEN-LAST:event_btnTimKiemNhaPhanPhoiActionPerformed

    private void txtTimKiemThongTinNhaPhanPhoiFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKiemThongTinNhaPhanPhoiFocusGained
        if (txtTimKiemThongTinNhaPhanPhoi.getText().equals("Nhập mã món ăn cần tìm kiếm")) {
            txtTimKiemThongTinNhaPhanPhoi.setText("");
            txtTimKiemThongTinNhaPhanPhoi.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_txtTimKiemThongTinNhaPhanPhoiFocusGained

    private void txtTimKiemThongTinNhaPhanPhoiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimKiemThongTinNhaPhanPhoiFocusLost
        if (txtTimKiemThongTinNhaPhanPhoi.getText().equals("")) {
            txtTimKiemThongTinNhaPhanPhoi.setText("Nhập mã món ăn cần tìm kiếm");
            txtTimKiemThongTinNhaPhanPhoi.setForeground(new Color(204, 204, 204));
        }
    }//GEN-LAST:event_txtTimKiemThongTinNhaPhanPhoiFocusLost

    private void txtTimKiemThongTinNhaPhanPhoiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemThongTinNhaPhanPhoiKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnTimKiemNhaPhanPhoi.doClick();
        }
    }//GEN-LAST:event_txtTimKiemThongTinNhaPhanPhoiKeyPressed

    private void txtMaMonAnPhanPhoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaMonAnPhanPhoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaMonAnPhanPhoiActionPerformed

    private void btnThem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem1ActionPerformed
        resetFormPhanPhoiSanPham();
    }//GEN-LAST:event_btnThem1ActionPerformed

    private void tblPhanPhoiSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhanPhoiSanPhamMouseClicked
        hienThiPhanPhoiSanPham();
        viTriPhanPhoiSanPham = tblPhanPhoiSanPham.getSelectedRow();
    }//GEN-LAST:event_tblPhanPhoiSanPhamMouseClicked

    private void tblPhanPhoiSanPhamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPhanPhoiSanPhamKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            btnXoa1.doClick();
        }
        if (evt.getKeyCode() == KeyEvent.VK_F5) {
            btnThem1.doClick();
        }
    }//GEN-LAST:event_tblPhanPhoiSanPhamKeyPressed

    private void btnCapNhat1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhat1ActionPerformed
        int viTri = tblPhanPhoiSanPham.getSelectedRow();
        int soLuongKho = 0;
        int soLuongPhanPhoi = 0;
        boolean kiemTraThongTin = false;
        boolean kiemTraGia = true;
        boolean kiemTraSoLuong = true;
        String kiemTraMaMonAn = "^MA+\\d+(\\d+)+";
        for (ThongTinMonAn ttma : list) {
            if (ttma.maMonAn.equalsIgnoreCase(txtMaMonAnPhanPhoi.getText())) {
                kiemTraThongTin = true;
                soLuongKho = soLuongKho + ttma.soLuong;
            }
        }
        for (PhanPhoiSanPham ppsp : danhSach) {
            if (ppsp.maMonAn.equalsIgnoreCase(txtMaMonAnPhanPhoi.getText())) {
                kiemTraThongTin = true;
                soLuongPhanPhoi = soLuongPhanPhoi + ppsp.soLuong;
            }
        }
        if (viTri >= 0 && !txtMaMonAnPhanPhoi.getText().equals(tblPhanPhoiSanPham.getValueAt(viTri, 0).toString())) {
            soLuongPhanPhoi = 0;
            for (PhanPhoiSanPham ppsp : danhSach) {
                if (ppsp.maMonAn.equalsIgnoreCase(txtMaMonAnPhanPhoi.getText())) {
                    kiemTraThongTin = true;
                    soLuongPhanPhoi = soLuongPhanPhoi + ppsp.soLuong;
                }
            }
            soLuongPhanPhoi = soLuongPhanPhoi + Integer.parseInt(txtSoLuongPhanPhoi.getText());
        } else if (viTri >= 0 && danhSach.get(viTri).soLuong <= Integer.parseInt(txtSoLuongPhanPhoi.getText()) && viTri >= 0) {
            danhSach.get(viTri).soLuong = Integer.parseInt(txtSoLuongPhanPhoi.getText());
            soLuongPhanPhoi = 0;
            for (PhanPhoiSanPham ppsp : danhSach) {
                if (ppsp.maMonAn.equalsIgnoreCase(txtMaMonAnPhanPhoi.getText())) {
                    kiemTraThongTin = true;
                    soLuongPhanPhoi = soLuongPhanPhoi + ppsp.soLuong;
                }
            }
        } else if (viTri >= 0 && danhSach.get(viTri).soLuong >= Integer.parseInt(txtSoLuongPhanPhoi.getText()) && viTri >= 0) {
            danhSach.get(viTri).soLuong = Integer.parseInt(txtSoLuongPhanPhoi.getText());
            soLuongPhanPhoi = 0;
            for (PhanPhoiSanPham ppsp : danhSach) {
                if (ppsp.maMonAn.equalsIgnoreCase(txtMaMonAnPhanPhoi.getText())) {
                    kiemTraThongTin = true;
                    soLuongPhanPhoi = soLuongPhanPhoi + ppsp.soLuong;
                }
            }
        }
        try {
            Integer.parseInt(txtGiaNhap.getText());
        } catch (NumberFormatException e) {
            kiemTraGia = false;
        }
        try {
            Integer.parseInt(txtSoLuongPhanPhoi.getText());
        } catch (NumberFormatException e) {
            kiemTraSoLuong = false;
        }
        if (tblPhanPhoiSanPham.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else if (viTri < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng cần cập nhật");
        } else if (tblPhanPhoiSanPham.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chỉ chọn 1 hàng để cập nhật");
        } else if (txtMaMonAnPhanPhoi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Mã món ăn không được để trống");
            txtMaMonAnPhanPhoi.requestFocus();
        } else if (txtTenMonAnPhanPhoi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên món ăn không được để trống");
            txtTenMonAnPhanPhoi.requestFocus();
        } else if (txtTenNhaPhanPhoi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên nhà phân phối không được để trống");
            txtTenNhaPhanPhoi.requestFocus();
        } else if (txtGiaNhap.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Giá nhập không được để trống");
            txtGiaNhap.requestFocus();
        } else if (txtSoLuongPhanPhoi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Số lượng không được để trống");
            txtSoLuongPhanPhoi.requestFocus();
        } else if (txtDiaChi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống");
            txtDiaChi.requestFocus();
        } else if (!txtMaMonAnPhanPhoi.getText().matches(kiemTraMaMonAn)) {
            JOptionPane.showMessageDialog(this, "Mã món ăn không đúng định dạng (VD: MA01)", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtMaMonAnPhanPhoi.requestFocus();
        } else if (kiemTraGia == false) {
            JOptionPane.showMessageDialog(this, "Giá phải là số", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaNhap.requestFocus();
        } else if (kiemTraSoLuong == false) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoLuongPhanPhoi.requestFocus();
        } else if (Integer.parseInt(txtGiaNhap.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "Giá phải lớn hơn 0", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtGiaNhap.requestFocus();
        } else if (Integer.parseInt(txtSoLuongPhanPhoi.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoLuongPhanPhoi.requestFocus();
        } else if (txtDiaChi.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống");
            txtDiaChi.requestFocus();
        } else if (soLuongKho < soLuongPhanPhoi) {
            JOptionPane.showMessageDialog(this, "Sản phẩm đã hết hàng trong kho");
        } else if (kiemTraThongTin == false) {
            JOptionPane.showMessageDialog(this, "Thông tin món ăn không có trong danh sách");
            txtMaMonAnPhanPhoi.requestFocus();
        } else {
            int ketQua = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn cập nhật dòng dữ liệu của món ăn " + (tblPhanPhoiSanPham.getValueAt(viTri, 0).toString()) + " không?", "Thông báo xác nhận", JOptionPane.YES_NO_OPTION);
            if (ketQua == JOptionPane.YES_OPTION) {
                PhanPhoiSanPham ppsp = danhSach.get(viTri);
                if (!diaChiHinhMonAn.equals("")) {
                    ppsp.iconHinhAnh = diaChiHinhMonAn;
                }
                try {
                    ps = con.prepareStatement(sqlUpdateThongTinNhaPhanPhoi);
                    ps.setString(1, txtMaMonAnPhanPhoi.getText().trim());
                    ps.setString(2, txtTenNhaPhanPhoi.getText().trim());
                    ps.setInt(3, Integer.parseInt(txtGiaNhap.getText().trim()));
                    ps.setInt(4, Integer.parseInt(txtSoLuongPhanPhoi.getText().trim()));
                    ps.setString(5, txtDiaChi.getText().trim());
                    ps.setString(6, ppsp.iconHinhAnh);
                    ps.setString(7, ppsp.ID);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e);
                }
                JOptionPane.showMessageDialog(this, "Cập nhật dữ liệu cho nhà phân phối " + tblPhanPhoiSanPham.getValueAt(viTri, 0).toString() + " thành công");
                fillPhanPhoiSanPham();
                resetFormPhanPhoiSanPham();
            }
        }
    }//GEN-LAST:event_btnCapNhat1ActionPerformed

    private void btnXoa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoa1ActionPerformed
        int viTri = tblPhanPhoiSanPham.getSelectedRow();
        if (tblPhanPhoiSanPham.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else if (viTri < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hàng cần xóa");
        } else if (tblPhanPhoiSanPham.getSelectedRowCount() > 1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chỉ chọn 1 hàng để xóa");
        } else {
            int ketQua = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa dòng dữ liệu của món ăn " + (tblPhanPhoiSanPham.getValueAt(viTri, 0).toString()) + " không?", "Thông báo xác nhận", JOptionPane.YES_NO_OPTION);
            if (ketQua == JOptionPane.YES_OPTION) {
                try {
                    ps = con.prepareStatement(sqlDeleteThongTinNhaPhanPhoi);
                    ps.setString(1, tblPhanPhoiSanPham.getValueAt(viTri, 0).toString());
                    ps.setString(2, tblPhanPhoiSanPham.getValueAt(viTri, 2).toString());
                    ps.execute();

                } catch (SQLException e) {
                    System.out.println(e);
                }
                fillPhanPhoiSanPham();
                resetFormPhanPhoiSanPham();
            }
        }
    }//GEN-LAST:event_btnXoa1ActionPerformed

    private void btnLuuDuLieu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuDuLieu1ActionPerformed
        if (!kiemTraLuuPhanPhoiSanPham()) {

        } else {
            int ketQua = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn thêm dòng dữ liệu mới của món ăn " + (txtMaMonAnPhanPhoi.getText()) + " không?", "Thông báo xác nhận", JOptionPane.YES_NO_OPTION);
            if (ketQua == JOptionPane.YES_OPTION) {
                try {
                    ps = con.prepareStatement(sqlInsertThongTinNhaPhanPhoi);
                    ps.setString(1, txtMaMonAnPhanPhoi.getText().trim());
                    ps.setString(2, txtTenNhaPhanPhoi.getText().trim());
                    ps.setInt(3, Integer.parseInt(txtGiaNhap.getText()));
                    ps.setInt(4, Integer.parseInt(txtSoLuongPhanPhoi.getText()));
                    ps.setString(5, txtDiaChi.getText());
                    ps.setString(6, diaChiHinhMonAn);
                    ps.execute();

                } catch (SQLException e) {
                    System.out.println(e);
                }
                JOptionPane.showMessageDialog(this, "Thêm món ăn thành công");
                fillPhanPhoiSanPham();
                resetFormPhanPhoiSanPham();
            }

        }
    }//GEN-LAST:event_btnLuuDuLieu1ActionPerformed

    private void btnFirst1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirst1ActionPerformed
        if (tblPhanPhoiSanPham.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else {
            viTriPhanPhoiSanPham = 0;
            tblPhanPhoiSanPham.setRowSelectionInterval(viTriPhanPhoiSanPham, viTriPhanPhoiSanPham);
            hienThiPhanPhoiSanPham();
        }
    }//GEN-LAST:event_btnFirst1ActionPerformed

    private void btnPrevious1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevious1ActionPerformed
        int viTri = tblPhanPhoiSanPham.getSelectedRow();
        if (tblPhanPhoiSanPham.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else if (viTri < 0) {
            tblPhanPhoiSanPham.setRowSelectionInterval(viTriPhanPhoiSanPham, viTriPhanPhoiSanPham);
            hienThiPhanPhoiSanPham();
        } else {
            viTriPhanPhoiSanPham--;
            if (viTriPhanPhoiSanPham < 0) {
                viTriPhanPhoiSanPham = danhSach.size() - 1;
            }
            tblPhanPhoiSanPham.setRowSelectionInterval(viTriPhanPhoiSanPham, viTriPhanPhoiSanPham);
            hienThiPhanPhoiSanPham();
        }
    }//GEN-LAST:event_btnPrevious1ActionPerformed

    private void btnNext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNext1ActionPerformed
        int viTri = tblPhanPhoiSanPham.getSelectedRow();
        if (tblPhanPhoiSanPham.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else if (viTri < 0) {
            tblPhanPhoiSanPham.setRowSelectionInterval(viTriPhanPhoiSanPham, viTriPhanPhoiSanPham);
            hienThiPhanPhoiSanPham();
        } else {
            viTriPhanPhoiSanPham++;
            if (viTriPhanPhoiSanPham >= danhSach.size()) {
                viTriPhanPhoiSanPham = 0;
            }
            tblPhanPhoiSanPham.setRowSelectionInterval(viTriPhanPhoiSanPham, viTriPhanPhoiSanPham);
            hienThiPhanPhoiSanPham();
        }
    }//GEN-LAST:event_btnNext1ActionPerformed

    private void btnLast1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLast1ActionPerformed
        if (tblPhanPhoiSanPham.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Bảng đang trống");
        } else {
            viTriPhanPhoiSanPham = danhSach.size() - 1;
            tblPhanPhoiSanPham.setRowSelectionInterval(viTriPhanPhoiSanPham, viTriPhanPhoiSanPham);
            hienThiPhanPhoiSanPham();
        }
    }//GEN-LAST:event_btnLast1ActionPerformed

    private void lblIconMonAnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblIconMonAnMouseClicked
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("files", ImageIO.getReaderFileSuffixes());
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        diaChiHinhMonAn = String.valueOf(chooser.getSelectedFile());
        if (diaChiHinhMonAn.equals("")) {
        } else {
            try {
                BufferedImage img;
                img = ImageIO.read(new File(f.getAbsolutePath()));
                Image img1 = img.getScaledInstance(lblIconMonAn.getWidth(), lblIconMonAn.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon format = new ImageIcon(img1);
                lblIconMonAn.setIcon(format);
            } catch (IOException e) {
            }
        }
    }//GEN-LAST:event_lblIconMonAnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhaHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhaHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhaHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyNhaHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyNhaHang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnCapNhat1;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnFirst1;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnLast1;
    private javax.swing.JButton btnLuuDuLieu;
    private javax.swing.JButton btnLuuDuLieu1;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnNext1;
    private javax.swing.JButton btnPhanPhoiSanPham;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JButton btnPrevious1;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThem1;
    private javax.swing.JButton btnThoatChuongTrinh;
    private javax.swing.JButton btnThongTinMonAn;
    private javax.swing.JButton btnTimKiemNhaPhanPhoi;
    private javax.swing.JButton btnTimKiemThongTinMonAn;
    private javax.swing.JButton btnTrangChu;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoa1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lblAnhHome1;
    private javax.swing.JLabel lblAnhHome2;
    private javax.swing.JLabel lblBannerHome;
    private javax.swing.JLabel lblDiaChi;
    private javax.swing.JLabel lblGia;
    private javax.swing.JLabel lblGiaNhap;
    private javax.swing.JLabel lblIconMonAn;
    private javax.swing.JLabel lblIconThongTinMonAn;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblMaMonAn;
    private javax.swing.JLabel lblMaMonAnPhanPhoi;
    private javax.swing.JLabel lblNgayCapNhat;
    private javax.swing.JLabel lblSoLuong;
    private javax.swing.JLabel lblSoLuongPhanPhoi;
    private javax.swing.JLabel lblTenMonAn;
    private javax.swing.JLabel lblTenMonAnPhanPhoi;
    private javax.swing.JLabel lblTenNhaPhanPhoi;
    private javax.swing.JTable tblPhanPhoiSanPham;
    private javax.swing.JTable tblThongTinMonAn;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtGiaNhap;
    private javax.swing.JTextField txtMaMonAn;
    private javax.swing.JTextField txtMaMonAnPhanPhoi;
    private javax.swing.JTextArea txtMoTa;
    private javax.swing.JTextField txtNgayCapNhat;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtSoLuongPhanPhoi;
    private javax.swing.JTextField txtTenMonAn;
    private javax.swing.JTextField txtTenMonAnPhanPhoi;
    private javax.swing.JTextField txtTenNhaPhanPhoi;
    private javax.swing.JTextField txtTimKiemThongTinMonAn;
    private javax.swing.JTextField txtTimKiemThongTinNhaPhanPhoi;
    // End of variables declaration//GEN-END:variables
}
