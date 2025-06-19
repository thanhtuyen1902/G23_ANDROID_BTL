package vn.edu.tlu.cse.ht1.thuy.myappcake;

public class SanPham {
    private String hinhAnh; // Tên file ảnh (ví dụ: "sample_choco") - Lưu ý phải có trong res/drawable
    private String maSP;
    private String tenSP;
    private int giaBan;
    private String loaiBanh;
    private String moTa;
    private int soLuongDaBan;
    private String hinhThucBan;

    public SanPham() {
        // Constructor rỗng (cần thiết cho một số thao tác)
    }

    // Constructor đầy đủ để dễ dàng tạo đối tượng SanPham
    public SanPham(String hinhAnh, String maSP, String tenSP, int giaBan,
                   String loaiBanh, String moTa, int soLuongDaBan, String hinhThucBan) {
        this.hinhAnh = hinhAnh;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.loaiBanh = loaiBanh;
        this.moTa = moTa;
        this.soLuongDaBan = soLuongDaBan;
        this.hinhThucBan = hinhThucBan;
    }

    // --- Getters và Setters cho tất cả các thuộc tính ---
    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(int giaBan) {
        this.giaBan = giaBan;
    }

    public String getLoaiBanh() {
        return loaiBanh;
    }

    public void setLoaiBanh(String loaiBanh) {
        this.loaiBanh = loaiBanh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getSoLuongDaBan() {
        return soLuongDaBan;
    }

    public void setSoLuongDaBan(int soLuongDaBan) {
        this.soLuongDaBan = soLuongDaBan;
    }

    public String getHinhThucBan() {
        return hinhThucBan;
    }

    public void setHinhThucBan(String hinhThucBan) {
        this.hinhThucBan = hinhThucBan;
    }
}