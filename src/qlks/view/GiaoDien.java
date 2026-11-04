package qlks.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class GiaoDien {
    public static final Color NEN = new Color(0xF4F6FB);
    public static final Color CHINH = new Color(0x1F6FEB);
    public static final Color CHINH_DAM = new Color(0x1657B8);
    public static final Color SIDEBAR = new Color(0x16213E);
    public static final Color SIDEBAR_SANG = new Color(0x0F3460);
    public static final Color XANH_LA = new Color(0x2E9E5B);
    public static final Color CAM = new Color(0xE07B10);
    public static final Color DO = new Color(0xD64545);
    public static final Color CHU = new Color(0x1A2233);
    public static final Color CHU_NHAT = new Color(0x6B7280);
    public static final Color TRANG = Color.WHITE;
    public static final Color VIEN = new Color(0xDCE1EB);

    public static final Font FONT_TIEU_DE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_LON = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_THUONG = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_DAM = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_NHO = new Font("Segoe UI", Font.PLAIN, 12);

    private static final NumberFormat DINH_DANG_TIEN = NumberFormat.getInstance(new Locale("vi", "VN"));

    public static String tien(double gia) {
        return DINH_DANG_TIEN.format(gia) + " đ";
    }

    public static JButton nutChinh(String text, Color mau) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? mau.darker() : (getModel().isRollover() ? mau.brighter() : mau));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(TRANG);
        b.setFont(FONT_DAM);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        return b;
    }

    public static JButton nutChinh(String text) {
        return nutChinh(text, CHINH);
    }

    public static Border oNhap(String goiY) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VIEN, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    public static JTextField oVanBan() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_THUONG);
        tf.setBorder(oNhap(""));
        return tf;
    }

    public static JPasswordField oMatKhau() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_THUONG);
        pf.setBorder(oNhap(""));
        return pf;
    }

    public static JLabel nhan(String text, Font font, Color mau) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(mau);
        return l;
    }

    public static void dinhDangBang(javax.swing.JTable bang) {
        bang.setRowHeight(34);
        bang.setFont(FONT_THUONG);
        bang.setSelectionBackground(new Color(0xDCE9FF));
        bang.setSelectionForeground(CHU);
        bang.setGridColor(new Color(0xEDEFF5));
        bang.setShowVerticalLines(false);
        bang.setIntercellSpacing(new java.awt.Dimension(0, 1));
        javax.swing.table.JTableHeader header = bang.getTableHeader();
        header.setFont(FONT_DAM);
        header.setBackground(SIDEBAR_SANG);
        header.setForeground(TRANG);
        header.setPreferredSize(new java.awt.Dimension(0, 40));
        header.setReorderingAllowed(false);
        ((javax.swing.table.DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    }

    public static JPanel the(String tieuDe, String giaTri, Color mau) {
        JPanel p = new JPanel(new BorderLayoutHolder());
        p.setBackground(TRANG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, mau),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        JLabel lblGiaTri = nhan(giaTri, new Font("Segoe UI", Font.BOLD, 24), CHU);
        JLabel lblTieuDe = nhan(tieuDe, FONT_NHO, CHU_NHAT);
        JPanel trong = new JPanel();
        trong.setBackground(TRANG);
        trong.setLayout(new javax.swing.BoxLayout(trong, javax.swing.BoxLayout.Y_AXIS));
        lblGiaTri.setAlignmentX(0);
        lblTieuDe.setAlignmentX(0);
        trong.add(lblGiaTri);
        trong.add(javax.swing.Box.createVerticalStrut(4));
        trong.add(lblTieuDe);
        p.add(trong);
        return p;
    }

    private static class BorderLayoutHolder extends java.awt.BorderLayout {
    }
}
