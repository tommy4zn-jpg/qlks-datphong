package qlks.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import qlks.dao.DanhGiaDAO;
import qlks.dao.DichVuDAO;
import qlks.dao.KhuyenMaiDAO;
import qlks.dao.LoaiPhongDAO;
import qlks.dao.NhanVienDAO;
import qlks.dao.PhongDAO;
import qlks.model.DichVu;
import qlks.model.KhuyenMai;
import qlks.model.LeTan;
import qlks.model.LoaiPhong;
import qlks.model.NhanVien;
import qlks.model.Phong;
import qlks.model.QuanLy;

public class PanelDanhMuc extends JPanel {

    private interface Nap {
        List<Object[]> nap() throws Exception;
    }

    private interface HanhDong {
        void thucHien(String ma) throws Exception;
    }

    private final String[] cols;
    private final Nap nap;
    private final HanhDong them;
    private final HanhDong sua;
    private final HanhDong xoa;
    private final boolean choPhepSua;

    private final DefaultTableModel model;
    private final JTable bang;

    private PanelDanhMuc(String tieuDe, String[] cols, Nap nap, HanhDong them, HanhDong sua, HanhDong xoa) {
        this.cols = cols;
        this.nap = nap;
        this.them = them;
        this.sua = sua;
        this.xoa = xoa;
        this.choPhepSua = them != null;

        setLayout(new BorderLayout(0, 16));
        setBackground(GiaoDien.NEN);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        top.setBackground(GiaoDien.TRANG);
        top.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        top.add(GiaoDien.nhan(tieuDe, GiaoDien.FONT_LON, GiaoDien.CHU));
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        bang = new JTable(model);
        GiaoDien.dinhDangBang(bang);
        JScrollPane sp = new JScrollPane(bang);
        sp.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        bottom.setBackground(GiaoDien.TRANG);
        bottom.setBorder(BorderFactory.createLineBorder(GiaoDien.VIEN));
        JButton btnLamMoi = GiaoDien.nutChinh("  Làm mới", GiaoDien.CHINH);
        btnLamMoi.addActionListener(e -> napBang());
        bottom.add(btnLamMoi);
        if (choPhepSua) {
            JButton btnThem = GiaoDien.nutChinh("  Thêm", GiaoDien.XANH_LA);
            JButton btnSua = GiaoDien.nutChinh("  Sửa", GiaoDien.CAM);
            JButton btnXoa = GiaoDien.nutChinh("  Xoá", GiaoDien.DO);
            btnThem.addActionListener(e -> chay(them, null));
            btnSua.addActionListener(e -> chay(sua, maDangChon()));
            btnXoa.addActionListener(e -> xoaDong());
            bottom.add(btnThem);
            bottom.add(btnSua);
            bottom.add(btnXoa);
        }
        add(bottom, BorderLayout.SOUTH);

        napBang();
    }

    private void napBang() {
        model.setRowCount(0);
        try {
            for (Object[] r : nap.nap()) model.addRow(r);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String maDangChon() {
        int dong = bang.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return String.valueOf(model.getValueAt(dong, 0));
    }

    private void chay(HanhDong hd, String ma) {
        if (hd == sua && ma == null) return;
        try {
            hd.thucHien(ma);
            napBang();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaDong() {
        String ma = maDangChon();
        if (ma == null) return;
        int c = JOptionPane.showConfirmDialog(this, "Xác nhận xoá \"" + ma + "\"?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        chay(xoa, ma);
    }

    private static boolean form(JComponent cha, String tieuDe, JComponent... thanhPhan) {
        JPanel p = new JPanel(new GridLayout(0, 1, 6, 6));
        for (JComponent c : thanhPhan) p.add(c);
        int ok = JOptionPane.showConfirmDialog(cha, p, tieuDe, JOptionPane.OK_CANCEL_OPTION);
        return ok == JOptionPane.OK_OPTION;
    }

    private static JTextField o(String gt) {
        JTextField tf = new JTextField(gt);
        tf.setPreferredSize(new Dimension(260, 32));
        return tf;
    }

    public static PanelDanhMuc phong() {
        PhongDAO dao = new PhongDAO();
        LoaiPhongDAO lpDao = new LoaiPhongDAO();
        String[] cols = {"Mã phòng", "Số phòng", "Tầng", "Loại phòng", "Trạng thái", "Giá/đêm"};
        Nap nap = () -> {
            List<Object[]> rows = new ArrayList<>();
            for (Phong p : dao.layTatCa()) {
                rows.add(new Object[]{p.getMaPhong(), p.getSoPhong(), p.getTang(),
                        p.getLoaiPhong() == null ? "" : p.getLoaiPhong().getTenLoai(),
                        docTrangThaiPhong(p.getTrangThai()), GiaoDien.tien(p.getLoaiPhong() == null ? 0 : p.getLoaiPhong().getGiaCoBan())});
            }
            return rows;
        };
        PanelDanhMuc[] holder = new PanelDanhMuc[1];
        HanhDong luu = ma -> {
            List<LoaiPhong> dsLoai = lpDao.layTatCa();
            Phong p = ma == null ? null : timPhong(dao, ma);
            JTextField txtSo = o(p == null ? "" : p.getSoPhong());
            JSpinner spTang = new JSpinner(new SpinnerNumberModel(p == null ? 1 : p.getTang(), 1, 50, 1));
            JComboBox<LoaiPhong> cboLoai = new JComboBox<>();
            for (LoaiPhong lp : dsLoai) {
                cboLoai.addItem(lp);
                if (p != null && lp.getMaLoaiPhong().equals(p.getMaLoaiPhong())) cboLoai.setSelectedItem(lp);
            }
            JComboBox<String> cboTT = new JComboBox<>(new String[]{"TRONG", "DA_DAT", "DANG_O", "BAO_TRI"});
            if (p != null) cboTT.setSelectedItem(p.getTrangThai());
            if (!form(holder[0], ma == null ? "Thêm phòng" : "Sửa phòng",
                    nhan("Số phòng"), txtSo, nhan("Tầng"), spTang, nhan("Loại phòng"), cboLoai, nhan("Trạng thái"), cboTT)) return;
            Phong moi = p == null ? new Phong() : p;
            if (p == null) moi.setMaPhong("P" + txtSo.getText().trim());
            moi.setSoPhong(txtSo.getText().trim());
            moi.setTang((int) spTang.getValue());
            moi.setMaLoaiPhong(((LoaiPhong) cboLoai.getSelectedItem()).getMaLoaiPhong());
            moi.setTrangThai((String) cboTT.getSelectedItem());
            if (p == null) dao.them(moi); else dao.capNhat(moi);
        };
        PanelDanhMuc panel = new PanelDanhMuc("Quản lý phòng", cols, nap, luu, luu, dao::xoa);
        holder[0] = panel;
        return panel;
    }

    public static PanelDanhMuc loaiPhong() {
        LoaiPhongDAO dao = new LoaiPhongDAO();
        String[] cols = {"Mã loại", "Tên loại", "Giá cơ bản", "Sức chứa", "Tiện nghi"};
        Nap nap = () -> {
            List<Object[]> rows = new ArrayList<>();
            for (LoaiPhong lp : dao.layTatCa()) {
                rows.add(new Object[]{lp.getMaLoaiPhong(), lp.getTenLoai(), GiaoDien.tien(lp.getGiaCoBan()), lp.getSucChua(), lp.getTienNghi()});
            }
            return rows;
        };
        PanelDanhMuc[] holder = new PanelDanhMuc[1];
        HanhDong luu = ma -> {
            LoaiPhong lp = ma == null ? null : timLoai(dao, ma);
            JTextField txtTen = o(lp == null ? "" : lp.getTenLoai());
            JTextField txtGia = o(lp == null ? "0" : String.valueOf((long) lp.getGiaCoBan()));
            JSpinner spSucChua = new JSpinner(new SpinnerNumberModel(lp == null ? 2 : lp.getSucChua(), 1, 20, 1));
            JTextField txtMoTa = o(lp == null ? "" : lp.getMoTa());
            JTextField txtTienNghi = o(lp == null ? "" : lp.getTienNghi());
            if (!form(holder[0], ma == null ? "Thêm loại phòng" : "Sửa loại phòng",
                    nhan("Tên loại"), txtTen, nhan("Giá cơ bản"), txtGia, nhan("Sức chứa"), spSucChua,
                    nhan("Mô tả"), txtMoTa, nhan("Tiện nghi"), txtTienNghi)) return;
            LoaiPhong moi = lp == null ? new LoaiPhong() : lp;
            if (lp == null) moi.setMaLoaiPhong(dao.sinhMa());
            moi.setTenLoai(txtTen.getText().trim());
            moi.setGiaCoBan(docSo(txtGia.getText()));
            moi.setSucChua((int) spSucChua.getValue());
            moi.setMoTa(txtMoTa.getText().trim());
            moi.setTienNghi(txtTienNghi.getText().trim());
            if (lp == null) dao.them(moi); else dao.capNhat(moi);
        };
        PanelDanhMuc panel = new PanelDanhMuc("Quản lý loại phòng", cols, nap, luu, luu, dao::xoa);
        holder[0] = panel;
        return panel;
    }

    public static PanelDanhMuc dichVu() {
        DichVuDAO dao = new DichVuDAO();
        String[] cols = {"Mã DV", "Tên dịch vụ", "Đơn giá", "Mô tả"};
        Nap nap = () -> {
            List<Object[]> rows = new ArrayList<>();
            for (DichVu dv : dao.layTatCa()) {
                rows.add(new Object[]{dv.getMaDV(), dv.getTenDV(), GiaoDien.tien(dv.getDonGia()), dv.getMoTa()});
            }
            return rows;
        };
        PanelDanhMuc[] holder = new PanelDanhMuc[1];
        HanhDong luu = ma -> {
            DichVu dv = ma == null ? null : timDichVu(dao, ma);
            JTextField txtTen = o(dv == null ? "" : dv.getTenDV());
            JTextField txtGia = o(dv == null ? "0" : String.valueOf((long) dv.getDonGia()));
            JTextField txtMoTa = o(dv == null ? "" : dv.getMoTa());
            if (!form(holder[0], ma == null ? "Thêm dịch vụ" : "Sửa dịch vụ",
                    nhan("Tên dịch vụ"), txtTen, nhan("Đơn giá"), txtGia, nhan("Mô tả"), txtMoTa)) return;
            DichVu moi = dv == null ? new DichVu() : dv;
            if (dv == null) moi.setMaDV(dao.sinhMa());
            moi.setTenDV(txtTen.getText().trim());
            moi.setDonGia(docSo(txtGia.getText()));
            moi.setMoTa(txtMoTa.getText().trim());
            if (dv == null) dao.them(moi); else dao.capNhat(moi);
        };
        PanelDanhMuc panel = new PanelDanhMuc("Quản lý dịch vụ", cols, nap, luu, luu, dao::xoa);
        holder[0] = panel;
        return panel;
    }

    public static PanelDanhMuc khuyenMai() {
        KhuyenMaiDAO dao = new KhuyenMaiDAO();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String[] cols = {"Mã KM", "Tên khuyến mãi", "% Giảm", "Bắt đầu", "Kết thúc", "Điều kiện"};
        Nap nap = () -> {
            List<Object[]> rows = new ArrayList<>();
            for (KhuyenMai km : dao.layTatCa()) {
                rows.add(new Object[]{km.getMaKM(), km.getTenKM(), (int) km.getPhanTramGiam() + "%",
                        km.getNgayBatDau() == null ? "" : sdf.format(km.getNgayBatDau()),
                        km.getNgayKetThuc() == null ? "" : sdf.format(km.getNgayKetThuc()), km.getDieuKien()});
            }
            return rows;
        };
        PanelDanhMuc[] holder = new PanelDanhMuc[1];
        HanhDong luu = ma -> {
            KhuyenMai km = ma == null ? null : timKhuyenMai(dao, ma);
            JTextField txtTen = o(km == null ? "" : km.getTenKM());
            JTextField txtPhanTram = o(km == null ? "0" : String.valueOf((long) km.getPhanTramGiam()));
            JTextField txtBatDau = o(km == null || km.getNgayBatDau() == null ? "" : sdf.format(km.getNgayBatDau()));
            JTextField txtKetThuc = o(km == null || km.getNgayKetThuc() == null ? "" : sdf.format(km.getNgayKetThuc()));
            JTextField txtDieuKien = o(km == null ? "" : km.getDieuKien());
            if (!form(holder[0], ma == null ? "Thêm khuyến mãi" : "Sửa khuyến mãi",
                    nhan("Tên khuyến mãi"), txtTen, nhan("% Giảm"), txtPhanTram,
                    nhan("Bắt đầu (dd/MM/yyyy)"), txtBatDau, nhan("Kết thúc (dd/MM/yyyy)"), txtKetThuc,
                    nhan("Điều kiện"), txtDieuKien)) return;
            KhuyenMai moi = km == null ? new KhuyenMai() : km;
            if (km == null) moi.setMaKM(dao.sinhMa());
            moi.setTenKM(txtTen.getText().trim());
            moi.setPhanTramGiam(docSo(txtPhanTram.getText()));
            moi.setNgayBatDau(docNgay(sdf, txtBatDau.getText()));
            moi.setNgayKetThuc(docNgay(sdf, txtKetThuc.getText()));
            moi.setDieuKien(txtDieuKien.getText().trim());
            if (km == null) dao.them(moi); else dao.capNhat(moi);
        };
        PanelDanhMuc panel = new PanelDanhMuc("Quản lý khuyến mãi", cols, nap, luu, luu, dao::xoa);
        holder[0] = panel;
        return panel;
    }

    public static PanelDanhMuc nhanVien() {
        NhanVienDAO dao = new NhanVienDAO();
        String[] cols = {"Mã NV", "Họ tên", "Chức vụ", "Email", "SĐT", "Tên ĐN"};
        Nap nap = () -> {
            List<Object[]> rows = new ArrayList<>();
            for (NhanVien nv : dao.layTatCa()) {
                rows.add(new Object[]{nv.getMaNguoiDung(), nv.getHoTen(), nv.getVaiTro(),
                        nv.getEmail(), nv.getSoDienThoai(), nv.getTenDangNhap()});
            }
            return rows;
        };
        PanelDanhMuc[] holder = new PanelDanhMuc[1];
        HanhDong luu = ma -> {
            NhanVien nv = ma == null ? null : timNhanVien(dao, ma);
            JTextField txtTen = o(nv == null ? "" : nv.getHoTen());
            JTextField txtEmail = o(nv == null ? "" : nv.getEmail());
            JTextField txtSdt = o(nv == null ? "" : nv.getSoDienThoai());
            JTextField txtTDN = o(nv == null ? "" : nv.getTenDangNhap());
            JComboBox<String> cboChucVu = new JComboBox<>(new String[]{"LETAN", "QUANLY"});
            if (nv != null) cboChucVu.setSelectedItem(nv.getChucVu());
            if (!form(holder[0], ma == null ? "Thêm nhân viên" : "Sửa nhân viên",
                    nhan("Họ tên"), txtTen, nhan("Chức vụ"), cboChucVu, nhan("Email"), txtEmail,
                    nhan("Số điện thoại"), txtSdt, nhan("Tên đăng nhập"), txtTDN)) return;
            String chucVu = (String) cboChucVu.getSelectedItem();
            NhanVien moi;
            if ("QUANLY".equals(chucVu)) {
                QuanLy q = new QuanLy();
                q.setPhongBan("Điều hành");
                moi = q;
            } else {
                LeTan l = new LeTan();
                l.setCaLamViec("Ca sáng");
                moi = l;
            }
            moi.setMaNguoiDung(nv == null ? dao.sinhMa() : nv.getMaNguoiDung());
            moi.setHoTen(txtTen.getText().trim());
            moi.setEmail(txtEmail.getText().trim());
            moi.setSoDienThoai(txtSdt.getText().trim());
            moi.setTenDangNhap(txtTDN.getText().trim());
            moi.setMatKhau(nv == null ? "123" : nv.getMatKhau());
            moi.setChucVu(chucVu);
            moi.setNgayVaoLam(nv == null ? new java.util.Date() : nv.getNgayVaoLam());
            if (nv == null) dao.them(moi); else dao.capNhat(moi);
        };
        PanelDanhMuc panel = new PanelDanhMuc("Quản lý nhân viên", cols, nap, luu, luu, dao::xoa);
        holder[0] = panel;
        return panel;
    }

    public static PanelDanhMuc danhGia() {
        DanhGiaDAO dao = new DanhGiaDAO();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String[] cols = {"Mã ĐG", "Khách hàng", "Phòng", "Điểm", "Bình luận", "Ngày"};
        Nap nap = () -> {
            List<Object[]> rows = new ArrayList<>();
            for (qlks.model.DanhGia g : dao.layTatCa()) {
                rows.add(new Object[]{g.getMaDG(), g.getTenKhachHang(), g.getMaPhong(), g.getDiem() + " ",
                        g.getBinhLuan(), g.getNgayDanhGia() == null ? "" : sdf.format(g.getNgayDanhGia())});
            }
            return rows;
        };
        return new PanelDanhMuc("Đánh giá của khách hàng", cols, nap, null, null, null);
    }

    private static javax.swing.JLabel nhan(String t) {
        return GiaoDien.nhan(t, GiaoDien.FONT_DAM, GiaoDien.CHU);
    }

    private static double docSo(String s) {
        try {
            return Double.parseDouble(s.trim().replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private static java.util.Date docNgay(SimpleDateFormat sdf, String s) {
        try {
            return sdf.parse(s.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static String docTrangThaiPhong(String tt) {
        switch (tt) {
            case "TRONG": return "Trống";
            case "DA_DAT": return "Đã đặt";
            case "DANG_O": return "Đang ở";
            case "BAO_TRI": return "Bảo trì";
            default: return tt;
        }
    }

    private static Phong timPhong(PhongDAO dao, String ma) throws Exception {
        return dao.layTatCa().stream().filter(p -> p.getMaPhong().equals(ma)).findFirst().orElse(null);
    }

    private static LoaiPhong timLoai(LoaiPhongDAO dao, String ma) throws Exception {
        return dao.layTatCa().stream().filter(l -> l.getMaLoaiPhong().equals(ma)).findFirst().orElse(null);
    }

    private static DichVu timDichVu(DichVuDAO dao, String ma) throws Exception {
        return dao.layTatCa().stream().filter(d -> d.getMaDV().equals(ma)).findFirst().orElse(null);
    }

    private static KhuyenMai timKhuyenMai(KhuyenMaiDAO dao, String ma) throws Exception {
        return dao.layTatCa().stream().filter(k -> k.getMaKM().equals(ma)).findFirst().orElse(null);
    }

    private static NhanVien timNhanVien(NhanVienDAO dao, String ma) throws Exception {
        return dao.layTatCa().stream().filter(n -> n.getMaNguoiDung().equals(ma)).findFirst().orElse(null);
    }
}
