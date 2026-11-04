package qlks.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import qlks.dao.XacThucDAO;
import qlks.model.KhachHang;

public class DangKyDialog extends JDialog {
    private final JTextField txtHoTen = GiaoDien.oVanBan();
    private final JTextField txtEmail = GiaoDien.oVanBan();
    private final JTextField txtSdt = GiaoDien.oVanBan();
    private final JTextField txtCccd = GiaoDien.oVanBan();
    private final JTextField txtDiaChi = GiaoDien.oVanBan();
    private final JTextField txtTenDangNhap = GiaoDien.oVanBan();
    private final JPasswordField txtMatKhau = GiaoDien.oMatKhau();
    private final XacThucDAO xacThucDAO = new XacThucDAO();

    public DangKyDialog(Frame chu) {
        super(chu, "Đăng ký tài khoản khách hàng", true);
        setSize(460, 560);
        setLocationRelativeTo(chu);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(GiaoDien.TRANG);
        p.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(5, 0, 5, 0);

        JLabel tieuDe = GiaoDien.nhan("Tạo tài khoản mới", GiaoDien.FONT_TIEU_DE, GiaoDien.CHU);
        gc.gridy = 0;
        p.add(tieuDe, gc);

        int y = 1;
        y = themDong(p, gc, y, "Họ và tên", txtHoTen);
        y = themDong(p, gc, y, "Email", txtEmail);
        y = themDong(p, gc, y, "Số điện thoại", txtSdt);
        y = themDong(p, gc, y, "CCCD", txtCccd);
        y = themDong(p, gc, y, "Địa chỉ", txtDiaChi);
        y = themDong(p, gc, y, "Tên đăng nhập", txtTenDangNhap);
        y = themDong(p, gc, y, "Mật khẩu", txtMatKhau);

        JButton btn = GiaoDien.nutChinh("ĐĂNG KÝ", GiaoDien.XANH_LA);
        btn.setPreferredSize(new Dimension(380, 44));
        gc.gridy = y;
        gc.insets = new Insets(18, 0, 5, 0);
        p.add(btn, gc);
        btn.addActionListener(e -> xuLyDangKy());

        add(p);
    }

    private int themDong(JPanel p, GridBagConstraints gc, int y, String nhan, JTextField field) {
        gc.gridy = y;
        gc.insets = new Insets(8, 0, 2, 0);
        p.add(GiaoDien.nhan(nhan, GiaoDien.FONT_DAM, GiaoDien.CHU), gc);
        field.setPreferredSize(new Dimension(380, 40));
        gc.gridy = y + 1;
        gc.insets = new Insets(0, 0, 4, 0);
        p.add(field, gc);
        return y + 2;
    }

    private void xuLyDangKy() {
        String hoTen = txtHoTen.getText().trim();
        String tdn = txtTenDangNhap.getText().trim();
        String mk = new String(txtMatKhau.getPassword());
        if (hoTen.isEmpty() || tdn.isEmpty() || mk.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên, tên đăng nhập và mật khẩu.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (xacThucDAO.tonTaiTenDangNhap(tdn)) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            KhachHang kh = new KhachHang();
            kh.setMaNguoiDung(xacThucDAO.sinhMaKhachHang());
            kh.setHoTen(hoTen);
            kh.setEmail(txtEmail.getText().trim());
            kh.setSoDienThoai(txtSdt.getText().trim());
            kh.setCccd(txtCccd.getText().trim());
            kh.setDiaChi(txtDiaChi.getText().trim());
            kh.setTenDangNhap(tdn);
            kh.setMatKhau(mk);
            if (xacThucDAO.dangKy(kh)) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Bạn có thể đăng nhập ngay.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
