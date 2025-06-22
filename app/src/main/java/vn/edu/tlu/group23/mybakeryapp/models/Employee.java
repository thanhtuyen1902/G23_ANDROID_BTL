//package vn.edu.tlu.group23.mybakeryapp.models;
//
//public class Employee {
//}

package vn.edu.tlu.group23.mybakeryapp.models;

public class Employee {
    private String maNV;
    private String tenNV;
    private String soDienThoai;
    private String chucVu;
    private String userName;
    private String passWord;

    private String role;

    // Constructor đầy đủ
    public Employee(String maNV, String tenNV, String soDienThoai, String chucVu, String userName, String passWord, String role) {

        this.maNV = maNV;
        this.tenNV = tenNV;
        this.soDienThoai = soDienThoai;
        this.chucVu = chucVu;
        this.userName = userName;
        this.passWord = passWord;
        this.role = role;
    }

    // Constructor rỗng (bắt buộc nếu dùng Firebase, Gson,...)
    public Employee() {}

    // Getter & Setter
    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public String getUserName() {
        return userName;
    }
    public String getPassWord() {
        return passWord;
    }


    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getChucVu() {
        return chucVu;
    }

    public String getRole() {
        return role;
    }


    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return tenNV + " (" + maNV + ")";
    }
}

