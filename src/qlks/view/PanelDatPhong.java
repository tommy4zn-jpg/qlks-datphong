package qlks.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import qlks.dao.DatPhongDAO;
import qlks.dao.KhuyenMaiDAO;
import qlks.dao.PhongDAO;
import qlks.model.ChiTietDatPhong;
import qlks.model.DatPhong;
import qlks.model.KhachHang;
import qlks.model.KhuyenMai;
import qlks.model.Phong;

public class PanelDatPhong extends JPanel {
    private final KhachHang khachHang;
    private final PhongDAO phongDAO = new PhongDAO();
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private final DatPhongDAO datPhongDAO = new DatPhongDAO();

    private final JSpinner spSoKhach = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    private final JSpinner spNgayNhan = new JSpinner(new SpinnerDateModel());
    private final JSpinner spNgayTra = new JSpinner(new SpinnerDateModel());
    private final JComboBox<KhuyenMai> cboKhuyenMai = new JComboBox<>();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Mã phòng", "Số phòng", "Tầng", "Loại phòng", "Sức chứa", "Giá/đêm", "Tiện nghi"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable bang = new JTable(model);
    private List<Phong> danhSach;

    public PanelDatPhong(KhachHang khachHang) {
        this.khachHang = khachHang;
        setLayout(new BorderLayout(0, 16));
        setBackground(GiaoDien.NEN);

        add(taoThanhTim(), BorderLayout.NORTH);

        GiaoDien.dinhDangBang(bang);
        JScrollPane sp = new JScrollPane(bang);
        sp.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        add(sp, BorderLayout.CENTER);

        add(taoThanhDat(), BorderLayout.SOUTH);

        napKhuyenMai();
        timPhong();
    }

    private JPanel taoThanhTim() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 14));
        p.setBackground(GiaoDien.TRANG);
        p.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));

        spNgayNhan.setEditor(new DateEditor(spNgayNhan, "dd/MM/yyyy"));
        spNgayTra.setEditor(new DateEditor(spNgayTra, "dd/MM/yyyy"));
        spNgayTra.setValue(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)));
        thietLapKichThuoc(spNgayNhan, spNgayTra, spSoKhach);

        p.add(GiaoDien.nhan("Ngày nhận:", GiaoDien.FONT_DAM, GiaoDien.CHU));
        p.add(spNgayNhan);
        p.add(GiaoDien.nhan("Ngày trả:", GiaoDien.FONT_DAM, GiaoDien.CHU));
        p.add(spNgayTra);
        p.add(GiaoDien.nhan("Số khách:", GiaoDien.FONT_DAM, GiaoDien.CHU));
        p.add(spSoKhach);

        JButton btnTim = GiaoDien.nutChinh("  Tìm phòng trống");
        btnTim.addActionListener(e -> timPhong());
        p.add(btnTim);
        return p;
    }

    private void thietLapKichThuoc(JSpinner... ds) {
        for (JSpinner s : ds) {
            s.setPreferredSize(new Dimension(130, 36));
        }
        spSoKhach.setPreferredSize(new Dimension(70, 36));
    }

    private JPanel taoThanhDat() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        p.setBackground(GiaoDien.TRANG);
        p.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        p.add(GiaoDien.nhan("Khuyến mãi:", GiaoDien.FONT_DAM, GiaoDien.CHU));
        cboKhuyenMai.setPreferredSize(new Dimension(220, 36));
        p.add(cboKhuyenMai);
        JButton btnDat = GiaoDien.nutChinh("  Đặt phòng đã chọn", GiaoDien.XANH_LA);
        btnDat.setPreferredSize(new Dimension(200, 42));
        btnDat.addActionListener(e -> datPhong());
        p.add(btnDat);
        return p;
    }

    private void napKhuyenMai() {
        cboKhuyenMai.removeAllItems();
        cboKhuyenMai.addItem(null);
        try {
            for (KhuyenMai km : khuyenMaiDAO.layTatCa()) {
                cboKhuyenMai.addItem(km);
            }
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void timPhong() {
        model.setRowCount(0);
        try {
            int soKhach = (int) spSoKhach.getValue();
            danhSach = phongDAO.timPhongTrong(soKhach);
            danhSach.forEach(p -> model.addRow(new Object[]{
                    p.getMaPhong(), p.getSoPhong(), p.getTang(),
                    p.getLoaiPhong().getTenLoai(), p.getLoaiPhong().getSucChua(),
                    GiaoDien.tien(p.getLoaiPhong().getGiaCoBan()), p.getLoaiPhong().getTienNghi()}));
            if (danhSach.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không còn phòng trống phù hợp với số khách đã chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void datPhong() {
        int dong = bang.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng trong danh sách.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Date ngayNhan = (Date) spNgayNhan.getValue();
        Date ngayTra = (Date) spNgayTra.getValue();
        long soDem = TimeUnit.MILLISECONDS.toDays(ngayTra.getTime() - ngayNhan.getTime());
        if (soDem <= 0) {
            JOptionPane.showMessageDialog(this, "Ngày trả phải sau ngày nhận ít nhất 1 đêm.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Phong phong = danhSach.get(dong);
        double gia = phong.getLoaiPhong().getGiaCoBan();
        KhuyenMai km = (KhuyenMai) cboKhuyenMai.getSelectedItem();
        double phanTram = km == null ? 0 : km.getPhanTramGiam();

        DatPhong dp = new DatPhong();
        dp.setMaKH(khachHang.getMaNguoiDung());
        dp.setNgayNhan(ngayNhan);
        dp.setNgayTra(ngayTra);
        dp.setSoKhach((int) spSoKhach.getValue());
        dp.setTrangThai("CHO_XAC_NHAN");
        dp.setMaKM(km == null ? null : km.getMaKM());
        double tong = dp.tinhTongTien(gia, (int) soDem, phanTram);

        ChiTietDatPhong ct = new ChiTietDatPhong();
        ct.setMaPhong(phong.getMaPhong());
        ct.setGiaPhong(gia);
        ct.setSoDem((int) soDem);
        ct.setThanhTien(gia * soDem);

        String thongTin = String.format(
                "Phòng: %s (%s)\nSố đêm: %d\nGiá/đêm: %s\nKhuyến mãi: %s\nTổng tiền: %s\n\nXác nhận đặt phòng?",
                phong.getSoPhong(), phong.getLoaiPhong().getTenLoai(), soDem,
                GiaoDien.tien(gia), km == null ? "Không" : km.getTenKM(), GiaoDien.tien(tong));
        int c = JOptionPane.showConfirmDialog(this, thongTin, "Xác nhận đặt phòng", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;

        try {
            String ma = datPhongDAO.taoDatPhong(dp, ct);
            JOptionPane.showMessageDialog(this, "Đặt phòng thành công! Mã đặt phòng: " + ma +
                    "\nVui lòng chờ lễ tân xác nhận.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            timPhong();
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    private void baoLoi(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
