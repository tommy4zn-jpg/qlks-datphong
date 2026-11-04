package qlks.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import qlks.model.KhachHang;
import qlks.model.NguoiDung;
import qlks.model.QuanLy;
import qlks.model.LeTan;

public class TrangChuView extends JFrame {
    private final NguoiDung nguoiDung;
    private final JPanel noiDung = new JPanel(new CardLayout());
    private final JPanel sidebarMenu = new JPanel();
    private JButton nutDangChon;

    public TrangChuView(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
        setTitle("Gia Dinh Hotel - " + nguoiDung.getVaiTro());
        setSize(1180, 720);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(taoSidebar(), BorderLayout.WEST);
        add(taoNoiDung(), BorderLayout.CENTER);

        napMenuTheoVaiTro();
    }

    private JPanel taoSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(GiaoDien.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(248, 0));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(GiaoDien.SIDEBAR);
        header.setBorder(BorderFactory.createEmptyBorder(26, 20, 20, 20));
        JLabel logo = new JLabel(" GIA DINH HOTEL");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel vaiTro = new JLabel(nguoiDung.getVaiTro().toUpperCase());
        vaiTro.setFont(GiaoDien.FONT_NHO);
        vaiTro.setForeground(new Color(0x7B8BB0));
        vaiTro.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(logo);
        header.add(Box.createVerticalStrut(6));
        header.add(vaiTro);
        sidebar.add(header, BorderLayout.NORTH);

        sidebarMenu.setLayout(new BoxLayout(sidebarMenu, BoxLayout.Y_AXIS));
        sidebarMenu.setBackground(GiaoDien.SIDEBAR);
        sidebarMenu.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        sidebar.add(sidebarMenu, BorderLayout.CENTER);

        JButton btnDangXuat = new JButton("  Đăng xuất");
        btnDangXuat.setFont(GiaoDien.FONT_DAM);
        btnDangXuat.setForeground(Color.WHITE);
        btnDangXuat.setBackground(GiaoDien.DO);
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        btnDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangXuat.addActionListener(e -> dangXuat());
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(GiaoDien.SIDEBAR);
        wrap.setBorder(BorderFactory.createEmptyBorder(10, 14, 20, 14));
        wrap.add(btnDangXuat, BorderLayout.CENTER);
        sidebar.add(wrap, BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel taoNoiDung() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(GiaoDien.NEN);

        JPanel topbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        topbar.setBackground(GiaoDien.TRANG);
        topbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GiaoDien.VIEN));
        JLabel chao = new JLabel("Xin chào, " + nguoiDung.getHoTen() + "  ");
        chao.setFont(GiaoDien.FONT_DAM);
        chao.setForeground(GiaoDien.CHU);
        JLabel avatar = new JLabel(nguoiDung.getHoTen().substring(0, 1).toUpperCase(), SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(GiaoDien.CHINH);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(GiaoDien.FONT_LON);
        avatar.setPreferredSize(new Dimension(38, 38));
        avatar.setBorder(BorderFactory.createEmptyBorder());
        topbar.add(chao);
        topbar.add(avatar);
        center.add(topbar, BorderLayout.NORTH);

        noiDung.setBackground(GiaoDien.NEN);
        noiDung.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        center.add(noiDung, BorderLayout.CENTER);
        return center;
    }

    private void napMenuTheoVaiTro() {
        if (nguoiDung instanceof KhachHang) {
            KhachHang kh = (KhachHang) nguoiDung;
            themMuc("  Tìm & Đặt phòng", "datphong", new PanelDatPhong(kh));
            themMuc("  Lịch sử đặt phòng", "lichsu", new PanelLichSu(kh));
            themMuc("  Hồ sơ cá nhân", "hoso", new PanelHoSo(kh));
        } else if (nguoiDung instanceof LeTan) {
            themMuc("  Quản lý đặt phòng", "quanlydp", new PanelLeTan((LeTan) nguoiDung));
            themMuc("  Danh mục phòng", "phong", PanelDanhMuc.phong());
        } else if (nguoiDung instanceof QuanLy) {
            themMuc("  Tổng quan & Thống kê", "thongke", new PanelThongKe());
            themMuc("  Quản lý phòng", "phong", PanelDanhMuc.phong());
            themMuc("  Loại phòng", "loaiphong", PanelDanhMuc.loaiPhong());
            themMuc("  Dịch vụ", "dichvu", PanelDanhMuc.dichVu());
            themMuc("  Khuyến mãi", "khuyenmai", PanelDanhMuc.khuyenMai());
            themMuc("  Nhân viên", "nhanvien", PanelDanhMuc.nhanVien());
            themMuc("  Đánh giá", "danhgia", PanelDanhMuc.danhGia());
        }
    }

    private void themMuc(String nhan, String key, JPanel panel) {
        noiDung.add(panel, key);
        JButton btn = new JButton(nhan);
        btn.setFont(GiaoDien.FONT_THUONG);
        btn.setForeground(new Color(0xC9D4EE));
        btn.setBackground(GiaoDien.SIDEBAR);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.addActionListener(e -> {
            ((CardLayout) noiDung.getLayout()).show(noiDung, key);
            chonMuc(btn);
        });
        sidebarMenu.add(btn);
        sidebarMenu.add(Box.createVerticalStrut(6));
        if (nutDangChon == null) {
            ((CardLayout) noiDung.getLayout()).show(noiDung, key);
            chonMuc(btn);
        }
    }

    private void chonMuc(JButton btn) {
        if (nutDangChon != null) {
            nutDangChon.setBackground(GiaoDien.SIDEBAR);
            nutDangChon.setForeground(new Color(0xC9D4EE));
        }
        btn.setBackground(GiaoDien.CHINH);
        btn.setForeground(Color.WHITE);
        nutDangChon = btn;
    }

    private void dangXuat() {
        int c = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            nguoiDung.dangXuat();
            dispose();
            new DangNhapView().setVisible(true);
        }
    }
}
