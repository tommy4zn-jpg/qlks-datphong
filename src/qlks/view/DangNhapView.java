package qlks.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import qlks.dao.XacThucDAO;
import qlks.exception.DangNhapException;
import qlks.model.NguoiDung;

public class DangNhapView extends JFrame {
    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private final XacThucDAO xacThucDAO = new XacThucDAO();

    public DangNhapView() {
        setTitle("Hệ thống Quản lý Đặt phòng Khách sạn");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(taoPanelTrai(), BorderLayout.WEST);
        add(taoPanelPhai(), BorderLayout.CENTER);
    }

    private JPanel taoPanelTrai() {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, GiaoDien.SIDEBAR_SANG, 0, getHeight(), GiaoDien.SIDEBAR));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setPreferredSize(new Dimension(380, 560));
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.insets = new Insets(8, 20, 8, 20);

        JLabel logo = new JLabel("🏨", SwingConstants.CENTER);
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        gc.gridy = 0;
        p.add(logo, gc);

        JLabel ten = new JLabel("GIA DINH HOTEL");
        ten.setFont(new Font("Segoe UI", Font.BOLD, 26));
        ten.setForeground(Color.WHITE);
        gc.gridy = 1;
        p.add(ten, gc);

        JLabel slogan = new JLabel("<html><div style='text-align:center;width:300px'>Hệ thống quản lý đặt phòng khách sạn thông minh - tiện lợi - chuyên nghiệp</div></html>");
        slogan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        slogan.setForeground(new Color(0xC9D4EE));
        slogan.setHorizontalAlignment(SwingConstants.CENTER);
        gc.gridy = 2;
        p.add(slogan, gc);

        return p;
    }

    private JPanel taoPanelPhai() {
        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(GiaoDien.TRANG);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(GiaoDien.TRANG);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 0, 6, 0);

        JLabel tieuDe = new JLabel("Đăng nhập hệ thống");
        tieuDe.setFont(GiaoDien.FONT_TIEU_DE);
        tieuDe.setForeground(GiaoDien.CHU);
        gc.gridy = 0;
        form.add(tieuDe, gc);

        JLabel phu = new JLabel("Vui lòng nhập thông tin tài khoản của bạn");
        phu.setFont(GiaoDien.FONT_NHO);
        phu.setForeground(GiaoDien.CHU_NHAT);
        gc.gridy = 1;
        gc.insets = new Insets(0, 0, 18, 0);
        form.add(phu, gc);

        gc.insets = new Insets(6, 0, 2, 0);
        gc.gridy = 2;
        form.add(GiaoDien.nhan("Tên đăng nhập", GiaoDien.FONT_DAM, GiaoDien.CHU), gc);
        txtTenDangNhap = GiaoDien.oVanBan();
        txtTenDangNhap.setPreferredSize(new Dimension(320, 42));
        gc.gridy = 3;
        form.add(txtTenDangNhap, gc);

        gc.gridy = 4;
        form.add(GiaoDien.nhan("Mật khẩu", GiaoDien.FONT_DAM, GiaoDien.CHU), gc);
        txtMatKhau = GiaoDien.oMatKhau();
        txtMatKhau.setPreferredSize(new Dimension(320, 42));
        gc.gridy = 5;
        form.add(txtMatKhau, gc);

        JButton btnDangNhap = GiaoDien.nutChinh("ĐĂNG NHẬP");
        btnDangNhap.setPreferredSize(new Dimension(320, 46));
        gc.gridy = 6;
        gc.insets = new Insets(20, 0, 6, 0);
        form.add(btnDangNhap, gc);

        JButton btnDangKy = new JButton("Chưa có tài khoản? Đăng ký ngay");
        btnDangKy.setFont(GiaoDien.FONT_THUONG);
        btnDangKy.setForeground(GiaoDien.CHINH);
        btnDangKy.setBorder(BorderFactory.createEmptyBorder());
        btnDangKy.setContentAreaFilled(false);
        btnDangKy.setFocusPainted(false);
        btnDangKy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        gc.gridy = 7;
        gc.insets = new Insets(0, 0, 6, 0);
        form.add(btnDangKy, gc);

        JLabel goiY = new JLabel("<html><div style='text-align:center;color:#9AA3B2;font-size:11px'>admin/123 &nbsp;·&nbsp; letan/123 &nbsp;·&nbsp; an/123</div></html>");
        goiY.setHorizontalAlignment(SwingConstants.CENTER);
        gc.gridy = 8;
        gc.insets = new Insets(14, 0, 0, 0);
        form.add(goiY, gc);

        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        txtMatKhau.addActionListener(e -> xuLyDangNhap());
        btnDangKy.addActionListener(e -> {
            DangKyDialog dlg = new DangKyDialog(this);
            dlg.setVisible(true);
        });

        wrap.add(form);
        return wrap;
    }

    private void xuLyDangNhap() {
        String ten = txtTenDangNhap.getText().trim();
        String mk = new String(txtMatKhau.getPassword());
        if (ten.isEmpty() || mk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            NguoiDung nd = xacThucDAO.dangNhap(ten, mk);
            JOptionPane.showMessageDialog(this, "Xin chào " + nd.getHoTen() + "!\nBạn đăng nhập với vai trò: " + nd.getVaiTro(), "Đăng nhập thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new TrangChuView(nd).setVisible(true);
        } catch (DangNhapException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        }
    }
}
