package qlks.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import qlks.dao.ThongKeDAO;

public class PanelThongKe extends JPanel {
    private final ThongKeDAO thongKeDAO = new ThongKeDAO();

    public PanelThongKe() {
        setLayout(new BorderLayout(0, 18));
        setBackground(GiaoDien.NEN);
        try {
            add(taoThe(), BorderLayout.NORTH);
            add(taoBieuDo(), BorderLayout.CENTER);
            add(taoBang(), BorderLayout.SOUTH);
        } catch (Exception ex) {
            add(GiaoDien.nhan("Lỗi tải thống kê: " + ex.getMessage(), GiaoDien.FONT_THUONG, GiaoDien.DO), BorderLayout.NORTH);
        }
    }

    private JPanel taoThe() throws Exception {
        JPanel p = new JPanel(new GridLayout(1, 4, 16, 0));
        p.setBackground(GiaoDien.NEN);
        p.add(vien(GiaoDien.the("Tổng doanh thu", GiaoDien.tien(thongKeDAO.tongDoanhThu()), GiaoDien.XANH_LA)));
        p.add(vien(GiaoDien.the("Lượt đặt phòng", String.valueOf(thongKeDAO.soDatPhong()), GiaoDien.CHINH)));
        p.add(vien(GiaoDien.the("Phòng trống", String.valueOf(thongKeDAO.soPhongTrong()), GiaoDien.CAM)));
        p.add(vien(GiaoDien.the("Khách hàng", String.valueOf(thongKeDAO.soKhachHang()), GiaoDien.CHINH_DAM)));
        return p;
    }

    private JPanel vien(JPanel the) {
        the.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GiaoDien.VIEN),
                the.getBorder()));
        return the;
    }

    private JPanel taoBieuDo() throws Exception {
        Map<String, Double> dl = thongKeDAO.doanhThuTheoLoaiPhong();
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(GiaoDien.TRANG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GiaoDien.VIEN),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        p.add(GiaoDien.nhan("Doanh thu theo loại phòng", GiaoDien.FONT_LON, GiaoDien.CHU), BorderLayout.NORTH);
        p.add(new BieuDoCot(dl), BorderLayout.CENTER);
        return p;
    }

    private JScrollPane taoBang() throws Exception {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Trạng thái phòng", "Số lượng"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        for (Map.Entry<String, Integer> e : thongKeDAO.soPhongTheoTrangThai().entrySet()) {
            model.addRow(new Object[]{docTrangThai(e.getKey()), e.getValue()});
        }
        JTable bang = new JTable(model);
        GiaoDien.dinhDangBang(bang);
        JScrollPane sp = new JScrollPane(bang);
        sp.setPreferredSize(new Dimension(0, 150));
        sp.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        return sp;
    }

    private String docTrangThai(String tt) {
        switch (tt) {
            case "TRONG": return "Trống";
            case "DA_DAT": return "Đã đặt";
            case "DANG_O": return "Đang ở";
            case "BAO_TRI": return "Bảo trì";
            default: return tt;
        }
    }

    private static class BieuDoCot extends JPanel {
        private final Map<String, Double> dl;

        BieuDoCot(Map<String, Double> dl) {
            this.dl = dl;
            setBackground(GiaoDien.TRANG);
            setPreferredSize(new Dimension(0, 240));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (dl.isEmpty()) return;
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            int le = 60, ph = 40, tr = 20, du = 40;
            double max = dl.values().stream().mapToDouble(Double::doubleValue).max().orElse(1);
            if (max <= 0) max = 1;
            int n = dl.size();
            int vungRong = w - le - tr;
            int coCot = Math.min(80, vungRong / (n * 2));
            int khoang = vungRong / n;
            g2.setColor(GiaoDien.VIEN);
            g2.drawLine(le, h - du, w - tr, h - du);
            int i = 0;
            Color[] mau = {GiaoDien.CHINH, GiaoDien.XANH_LA, GiaoDien.CAM, GiaoDien.CHINH_DAM, new Color(0x9673A6)};
            for (Map.Entry<String, Double> e : dl.entrySet()) {
                int x = le + i * khoang + (khoang - coCot) / 2;
                int chieuCao = (int) ((h - du - ph) * (e.getValue() / max));
                int y = h - du - chieuCao;
                g2.setColor(mau[i % mau.length]);
                g2.fillRoundRect(x, y, coCot, chieuCao, 10, 10);
                g2.setColor(GiaoDien.CHU);
                g2.setFont(GiaoDien.FONT_NHO);
                g2.drawString(e.getKey(), x, h - du + 16);
                String gt = (long) (e.getValue() / 1000) + "K";
                g2.drawString(gt, x, y - 6);
                i++;
            }
        }
    }
}
