package qlks.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import qlks.dao.DanhGiaDAO;
import qlks.dao.DatPhongDAO;
import qlks.model.ChiTietDatPhong;
import qlks.model.DanhGia;
import qlks.model.DatPhong;
import qlks.model.KhachHang;

public class PanelLichSu extends JPanel {
    private final KhachHang khachHang;
    private final DatPhongDAO datPhongDAO = new DatPhongDAO();
    private final DanhGiaDAO danhGiaDAO = new DanhGiaDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã ĐP", "Ngày nhận", "Ngày trả", "Số khách", "Tổng tiền", "Trạng thái"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable bang = new JTable(model);
    private List<DatPhong> danhSach;

    public PanelLichSu(KhachHang khachHang) {
        this.khachHang = khachHang;
        setLayout(new BorderLayout(0, 16));
        setBackground(GiaoDien.NEN);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(GiaoDien.TRANG);
        top.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        top.add(GiaoDien.nhan("Lịch sử đặt phòng của bạn", GiaoDien.FONT_LON, GiaoDien.CHU));
        add(top, BorderLayout.NORTH);

        GiaoDien.dinhDangBang(bang);
        JScrollPane sp = new JScrollPane(bang);
        sp.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        bottom.setBackground(GiaoDien.TRANG);
        bottom.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        JButton btnLamMoi = GiaoDien.nutChinh("  Làm mới", GiaoDien.CHINH);
        JButton btnHuy = GiaoDien.nutChinh("  Huỷ đặt phòng", GiaoDien.DO);
        JButton btnDanhGia = GiaoDien.nutChinh("  Đánh giá", GiaoDien.CAM);
        btnLamMoi.addActionListener(e -> napDuLieu());
        btnHuy.addActionListener(e -> huy());
        btnDanhGia.addActionListener(e -> danhGia());
        bottom.add(btnLamMoi);
        bottom.add(btnHuy);
        bottom.add(btnDanhGia);
        add(bottom, BorderLayout.SOUTH);

        napDuLieu();
    }

    private void napDuLieu() {
        model.setRowCount(0);
        try {
            danhSach = datPhongDAO.layTheoKhachHang(khachHang.getMaNguoiDung());
            for (DatPhong d : danhSach) {
                model.addRow(new Object[]{
                        d.getMaDatPhong(), sdf.format(d.getNgayNhan()), sdf.format(d.getNgayTra()),
                        d.getSoKhach(), GiaoDien.tien(d.getTongTien()), docTrangThai(d.getTrangThai())});
            }
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private String docTrangThai(String tt) {
        switch (tt) {
            case "CHO_XAC_NHAN": return "Chờ xác nhận";
            case "DA_XAC_NHAN": return "Đã xác nhận";
            case "DA_NHAN_PHONG": return "Đã nhận phòng";
            case "DA_TRA_PHONG": return "Đã trả phòng";
            case "DA_HUY": return "Đã huỷ";
            default: return tt;
        }
    }

    private void huy() {
        int dong = bang.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đặt phòng cần huỷ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DatPhong d = danhSach.get(dong);
        if (!d.getTrangThai().equals("CHO_XAC_NHAN") && !d.getTrangThai().equals("DA_XAC_NHAN")) {
            JOptionPane.showMessageDialog(this, "Chỉ huỷ được đặt phòng đang chờ hoặc đã xác nhận.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int c = JOptionPane.showConfirmDialog(this, "Xác nhận huỷ đặt phòng " + d.getMaDatPhong() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
            datPhongDAO.huyDatPhong(d.getMaDatPhong());
            JOptionPane.showMessageDialog(this, "Đã huỷ đặt phòng.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            napDuLieu();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void danhGia() {
        int dong = bang.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đặt phòng đã trả phòng để đánh giá.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        DatPhong d = danhSach.get(dong);
        if (!d.getTrangThai().equals("DA_TRA_PHONG")) {
            JOptionPane.showMessageDialog(this, "Chỉ đánh giá được sau khi đã trả phòng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            ChiTietDatPhong ct = datPhongDAO.layChiTiet(d.getMaDatPhong());
            JSpinner spDiem = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));
            JTextField txtBinhLuan = new JTextField();
            JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 6, 6));
            form.add(GiaoDien.nhan("Chấm điểm (1-5):", GiaoDien.FONT_DAM, GiaoDien.CHU));
            form.add(spDiem);
            form.add(GiaoDien.nhan("Nhận xét:", GiaoDien.FONT_DAM, GiaoDien.CHU));
            form.add(txtBinhLuan);
            int ok = JOptionPane.showConfirmDialog(this, form, "Đánh giá phòng " + ct.getSoPhong(), JOptionPane.OK_CANCEL_OPTION);
            if (ok != JOptionPane.OK_OPTION) return;
            DanhGia g = new DanhGia();
            g.setMaKH(khachHang.getMaNguoiDung());
            g.setMaPhong(ct.getMaPhong());
            g.setDiem((int) spDiem.getValue());
            g.setBinhLuan(txtBinhLuan.getText().trim());
            danhGiaDAO.guiDanhGia(g);
            JOptionPane.showMessageDialog(this, "Cảm ơn bạn đã đánh giá!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void baoLoi(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
