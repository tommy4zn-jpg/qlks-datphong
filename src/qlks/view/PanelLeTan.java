package qlks.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import qlks.dao.DatPhongDAO;
import qlks.dao.DichVuDAO;
import qlks.dao.HoaDonDAO;
import qlks.dao.KhachHangDAO;
import qlks.dao.PhongDAO;
import qlks.dao.SuDungDichVuDAO;
import qlks.model.ChiTietDatPhong;
import qlks.model.DatPhong;
import qlks.model.DichVu;
import qlks.model.HoaDon;
import qlks.model.LeTan;
import qlks.model.SuDungDichVu;
import qlks.model.ThanhToan;

public class PanelLeTan extends JPanel {
    private final LeTan leTan;
    private final DatPhongDAO datPhongDAO = new DatPhongDAO();
    private final SuDungDichVuDAO suDungDichVuDAO = new SuDungDichVuDAO();
    private final DichVuDAO dichVuDAO = new DichVuDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final PhongDAO phongDAO = new PhongDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã ĐP", "Khách hàng", "Ngày nhận", "Ngày trả", "Số khách", "Tổng tiền", "Trạng thái"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable bang = new JTable(model);
    private List<DatPhong> danhSach;

    public PanelLeTan(LeTan leTan) {
        this.leTan = leTan;
        setLayout(new BorderLayout(0, 16));
        setBackground(GiaoDien.NEN);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        top.setBackground(GiaoDien.TRANG);
        top.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        top.add(GiaoDien.nhan("Quản lý đặt phòng & tiếp nhận khách", GiaoDien.FONT_LON, GiaoDien.CHU));
        add(top, BorderLayout.NORTH);

        GiaoDien.dinhDangBang(bang);
        JScrollPane sp = new JScrollPane(bang);
        sp.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        bottom.setBackground(GiaoDien.TRANG);
        bottom.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        JButton btnLamMoi = GiaoDien.nutChinh("  Làm mới", GiaoDien.CHINH);
        JButton btnXacNhan = GiaoDien.nutChinh("  Xác nhận", GiaoDien.XANH_LA);
        JButton btnCheckIn = GiaoDien.nutChinh("  Check-in", GiaoDien.CAM);
        JButton btnCheckOut = GiaoDien.nutChinh("  Check-out & Hoá đơn", GiaoDien.CHINH_DAM);
        btnLamMoi.addActionListener(e -> napDuLieu());
        btnXacNhan.addActionListener(e -> xacNhan());
        btnCheckIn.addActionListener(e -> checkIn());
        btnCheckOut.addActionListener(e -> checkOut());
        bottom.add(btnLamMoi);
        bottom.add(btnXacNhan);
        bottom.add(btnCheckIn);
        bottom.add(btnCheckOut);
        add(bottom, BorderLayout.SOUTH);

        napDuLieu();
    }

    private void napDuLieu() {
        model.setRowCount(0);
        try {
            danhSach = datPhongDAO.layTatCa();
            for (DatPhong d : danhSach) {
                model.addRow(new Object[]{
                        d.getMaDatPhong(), d.getTenKhachHang(), sdf.format(d.getNgayNhan()),
                        sdf.format(d.getNgayTra()), d.getSoKhach(), GiaoDien.tien(d.getTongTien()),
                        docTrangThai(d.getTrangThai())});
            }
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private String docTrangThai(String tt) {
        switch (tt) {
            case "CHO_XAC_NHAN": return "Chờ xác nhận";
            case "DA_XAC_NHAN": return "Đã xác nhận";
            case "DA_NHAN_PHONG": return "Đang ở";
            case "DA_TRA_PHONG": return "Đã trả phòng";
            case "DA_HUY": return "Đã huỷ";
            default: return tt;
        }
    }

    private DatPhong chon() {
        int dong = bang.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đặt phòng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return danhSach.get(dong);
    }

    private void xacNhan() {
        DatPhong d = chon();
        if (d == null) return;
        if (!d.getTrangThai().equals("CHO_XAC_NHAN")) {
            JOptionPane.showMessageDialog(this, "Chỉ xác nhận được đặt phòng đang chờ.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            datPhongDAO.capNhatTrangThai(d.getMaDatPhong(), "DA_XAC_NHAN");
            datPhongDAO.ganNhanVien(d.getMaDatPhong(), leTan.getMaNguoiDung());
            JOptionPane.showMessageDialog(this, "Đã xác nhận đặt phòng " + d.getMaDatPhong(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
            napDuLieu();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void checkIn() {
        DatPhong d = chon();
        if (d == null) return;
        if (!d.getTrangThai().equals("DA_XAC_NHAN")) {
            JOptionPane.showMessageDialog(this, "Chỉ check-in được đặt phòng đã xác nhận.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            ChiTietDatPhong ct = datPhongDAO.layChiTiet(d.getMaDatPhong());
            datPhongDAO.capNhatTrangThai(d.getMaDatPhong(), "DA_NHAN_PHONG");
            phongDAO.capNhatTrangThai(ct.getMaPhong(), "DANG_O");
            JOptionPane.showMessageDialog(this, "Đã check-in. Phòng " + ct.getSoPhong() + " chuyển sang trạng thái đang ở.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            napDuLieu();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void checkOut() {
        DatPhong d = chon();
        if (d == null) return;
        if (!d.getTrangThai().equals("DA_NHAN_PHONG")) {
            JOptionPane.showMessageDialog(this, "Chỉ check-out được phòng đang ở.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            themDichVu(d);
            ChiTietDatPhong ct = datPhongDAO.layChiTiet(d.getMaDatPhong());
            double tienPhong = ct.getThanhTien();
            double tienDV = suDungDichVuDAO.tongTienDichVu(d.getMaDatPhong());
            double giamGia = tienPhong - d.getTongTien();
            if (giamGia < 0) giamGia = 0;

            HoaDon hd = new HoaDon();
            hd.setMaDatPhong(d.getMaDatPhong());
            hd.setTongTienPhong(tienPhong);
            hd.setTongTienDV(tienDV);
            hd.setGiamGia(giamGia);
            hd.tinhTongThanhToan();

            String[] phuongThuc = {"Tiền mặt", "Chuyển khoản", "Thẻ tín dụng"};
            JComboBox<String> cbo = new JComboBox<>(phuongThuc);
            StringBuilder sb = new StringBuilder();
            sb.append("HOÁ ĐƠN THANH TOÁN\n");
            sb.append("Phòng: ").append(ct.getSoPhong()).append("  (").append(ct.getSoDem()).append(" đêm)\n");
            sb.append("Tiền phòng: ").append(GiaoDien.tien(tienPhong)).append("\n");
            sb.append("Tiền dịch vụ: ").append(GiaoDien.tien(tienDV)).append("\n");
            sb.append("Giảm giá (khuyến mãi): ").append(GiaoDien.tien(giamGia)).append("\n");
            sb.append("Thuế VAT 10%: ").append(GiaoDien.tien(hd.getThue())).append("\n");
            sb.append("TỔNG THANH TOÁN: ").append(GiaoDien.tien(hd.getTongThanhToan())).append("\n\n");
            sb.append("Chọn phương thức thanh toán:");

            JPanel form = new JPanel(new BorderLayout(0, 8));
            javax.swing.JTextArea ta = new javax.swing.JTextArea(sb.toString());
            ta.setEditable(false);
            ta.setFont(GiaoDien.FONT_THUONG);
            ta.setBackground(form.getBackground());
            form.add(ta, BorderLayout.CENTER);
            form.add(cbo, BorderLayout.SOUTH);
            int ok = JOptionPane.showConfirmDialog(this, form, "Check-out & Thanh toán", JOptionPane.OK_CANCEL_OPTION);
            if (ok != JOptionPane.OK_OPTION) return;

            ThanhToan tt = new ThanhToan();
            tt.setSoTien(hd.getTongThanhToan());
            tt.setPhuongThuc((String) cbo.getSelectedItem());
            hoaDonDAO.lapHoaDon(hd, tt);
            khachHangDAO.congDiemTichLuy(d.getMaKH(), (int) (hd.getTongThanhToan() / 100000));

            JOptionPane.showMessageDialog(this, "Thanh toán thành công! Mã hoá đơn: " + hd.getMaHD() +
                    "\nTổng thu: " + GiaoDien.tien(hd.getTongThanhToan()), "Hoàn tất", JOptionPane.INFORMATION_MESSAGE);
            napDuLieu();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void themDichVu(DatPhong d) throws Exception {
        int c = JOptionPane.showConfirmDialog(this, "Khách có sử dụng thêm dịch vụ không?", "Dịch vụ phát sinh", JOptionPane.YES_NO_OPTION);
        while (c == JOptionPane.YES_OPTION) {
            JComboBox<DichVu> cbo = new JComboBox<>();
            for (DichVu dv : dichVuDAO.layTatCa()) cbo.addItem(dv);
            JSpinner sp = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
            JPanel form = new JPanel(new java.awt.GridLayout(0, 1, 6, 6));
            form.add(GiaoDien.nhan("Dịch vụ:", GiaoDien.FONT_DAM, GiaoDien.CHU));
            form.add(cbo);
            form.add(GiaoDien.nhan("Số lượng:", GiaoDien.FONT_DAM, GiaoDien.CHU));
            form.add(sp);
            int ok = JOptionPane.showConfirmDialog(this, form, "Thêm dịch vụ", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                DichVu dv = (DichVu) cbo.getSelectedItem();
                int sl = (int) sp.getValue();
                SuDungDichVu s = new SuDungDichVu();
                s.setMaDatPhong(d.getMaDatPhong());
                s.setMaDV(dv.getMaDV());
                s.setSoLuong(sl);
                s.setThanhTien(dv.getDonGia() * sl);
                suDungDichVuDAO.them(s);
            }
            c = JOptionPane.showConfirmDialog(this, "Thêm dịch vụ khác?", "Dịch vụ phát sinh", JOptionPane.YES_NO_OPTION);
        }
    }

    private void baoLoi(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
