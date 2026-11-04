package qlks.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import qlks.dao.KhachHangDAO;
import qlks.model.KhachHang;

public class PanelHoSo extends JPanel {
    private final KhachHang khachHang;
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    private final JTextField txtHoTen = GiaoDien.oVanBan();
    private final JTextField txtEmail = GiaoDien.oVanBan();
    private final JTextField txtSdt = GiaoDien.oVanBan();
    private final JTextField txtCccd = GiaoDien.oVanBan();
    private final JTextField txtDiaChi = GiaoDien.oVanBan();
    private final JPasswordField txtMatKhauMoi = GiaoDien.oMatKhau();

    public PanelHoSo(KhachHang khachHang) {
        this.khachHang = khachHang;
        setBackground(GiaoDien.NEN);
        setLayout(new GridBagLayout());

        JPanel the = new JPanel(new GridBagLayout());
        the.setBackground(GiaoDien.TRANG);
        the.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GiaoDien.VIEN),
                BorderFactory.createEmptyBorder(26, 34, 26, 34)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 0, 2, 0);

        gc.gridy = 0;
        the.add(GiaoDien.nhan("Hồ sơ cá nhân", GiaoDien.FONT_TIEU_DE, GiaoDien.CHU), gc);
        gc.gridy = 1;
        the.add(GiaoDien.nhan("Mã KH: " + khachHang.getMaNguoiDung() + "   ·   Điểm tích luỹ: " + khachHang.getDiemTichLuy(),
                GiaoDien.FONT_NHO, GiaoDien.CHU_NHAT), gc);

        int y = 2;
        y = dong(the, gc, y, "Họ và tên", txtHoTen);
        y = dong(the, gc, y, "Email", txtEmail);
        y = dong(the, gc, y, "Số điện thoại", txtSdt);
        y = dong(the, gc, y, "CCCD", txtCccd);
        y = dong(the, gc, y, "Địa chỉ", txtDiaChi);

        JButton btnLuu = GiaoDien.nutChinh("  Lưu thông tin", GiaoDien.XANH_LA);
        btnLuu.setPreferredSize(new Dimension(430, 44));
        gc.gridy = y++;
        gc.insets = new Insets(18, 0, 6, 0);
        the.add(btnLuu, gc);

        gc.gridy = y++;
        gc.insets = new Insets(6, 0, 2, 0);
        the.add(GiaoDien.nhan("Đổi mật khẩu mới", GiaoDien.FONT_DAM, GiaoDien.CHU), gc);
        txtMatKhauMoi.setPreferredSize(new Dimension(430, 40));
        gc.gridy = y++;
        the.add(txtMatKhauMoi, gc);
        JButton btnDoiMK = GiaoDien.nutChinh("  Đổi mật khẩu", GiaoDien.CHINH);
        btnDoiMK.setPreferredSize(new Dimension(430, 44));
        gc.gridy = y++;
        gc.insets = new Insets(10, 0, 4, 0);
        the.add(btnDoiMK, gc);

        add(the);

        napDuLieu();
        btnLuu.addActionListener(e -> luu());
        btnDoiMK.addActionListener(e -> doiMatKhau());
    }

    private int dong(JPanel p, GridBagConstraints gc, int y, String nhan, JTextField field) {
        gc.gridy = y;
        gc.insets = new Insets(10, 0, 2, 0);
        p.add(GiaoDien.nhan(nhan, GiaoDien.FONT_DAM, GiaoDien.CHU), gc);
        field.setPreferredSize(new Dimension(430, 40));
        gc.gridy = y + 1;
        gc.insets = new Insets(0, 0, 4, 0);
        p.add(field, gc);
        return y + 2;
    }

    private void napDuLieu() {
        txtHoTen.setText(khachHang.getHoTen());
        txtEmail.setText(khachHang.getEmail());
        txtSdt.setText(khachHang.getSoDienThoai());
        txtCccd.setText(khachHang.getCccd());
        txtDiaChi.setText(khachHang.getDiaChi());
    }

    private void luu() {
        khachHang.setHoTen(txtHoTen.getText().trim());
        khachHang.setEmail(txtEmail.getText().trim());
        khachHang.setSoDienThoai(txtSdt.getText().trim());
        khachHang.setCccd(txtCccd.getText().trim());
        khachHang.setDiaChi(txtDiaChi.getText().trim());
        try {
            khachHangDAO.capNhat(khachHang);
            JOptionPane.showMessageDialog(this, "Cập nhật hồ sơ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doiMatKhau() {
        String mk = new String(txtMatKhauMoi.getPassword());
        if (mk.length() < 3) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới phải có ít nhất 3 ký tự.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            khachHangDAO.doiMatKhau(khachHang.getMaNguoiDung(), mk);
            khachHang.setMatKhau(mk);
            txtMatKhauMoi.setText("");
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
