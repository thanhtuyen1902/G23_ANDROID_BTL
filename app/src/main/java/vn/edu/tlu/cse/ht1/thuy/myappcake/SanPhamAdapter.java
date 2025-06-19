package vn.edu.tlu.cse.ht1.thuy.myappcake;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {

    private Context context;
    private List<SanPham> sanPhamList;
    private NumberFormat currencyFormat;

    public SanPhamAdapter(Context context, List<SanPham> sanPhamList) {
        this.context = context;
        this.sanPhamList = sanPhamList;
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        currencyFormat.setMinimumFractionDigits(0);
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        SanPham sanPham = sanPhamList.get(position);

        // Tải và hiển thị hình ảnh
        int imageResId = context.getResources().getIdentifier(
                sanPham.getHinhAnh(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.imgHinhAnh.setImageResource(imageResId);
        } else {
            holder.imgHinhAnh.setImageResource(R.drawable.ic_launcher_background); // Ảnh placeholder nếu không tìm thấy
        }

        // Gán các Text
        holder.tvMaSP.setText("Mã bánh: " + sanPham.getMaSP());
        holder.tvTenSP.setText(sanPham.getTenSP());
        holder.tvGiaBan.setText("Giá bánh: " + currencyFormat.format(sanPham.getGiaBan()));
        holder.tvMoTa.setText("Mô tả: " + sanPham.getMoTa());

        // Các TextView này được set visibility="gone" trong XML, nhưng nếu muốn hiển thị
        // thì bạn có thể bỏ visibility="gone" và set text ở đây
        // holder.tvLoaiBanh.setText("Loại: " + sanPham.getLoaiBanh());
        // holder.tvSoLuongDaBan.setText("Đã bán: " + sanPham.getSoLuongDaBan());
        // holder.tvHinhThucBan.setText("Hình thức: " + sanPham.getHinhThucBan());

        // TODO: Thêm sự kiện click cho các icon hành động nếu cần
        // holder.iconView.setOnClickListener(...);
        // holder.iconEdit.setOnClickListener(...);
        // holder.iconDelete.setOnClickListener(...);
    }

    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }

    public static class SanPhamViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHinhAnh;
        TextView tvMaSP, tvTenSP, tvGiaBan, tvLoaiBanh, tvMoTa, tvSoLuongDaBan, tvHinhThucBan;
        ImageView iconView, iconEdit, iconDelete; // Khai báo các ImageView cho icon hành động

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhAnh = itemView.findViewById(R.id.imgHinhAnh);
            tvMaSP = itemView.findViewById(R.id.tvMaSP);
            tvTenSP = itemView.findViewById(R.id.tvTenSP);
            tvGiaBan = itemView.findViewById(R.id.tvGiaBan);
            tvLoaiBanh = itemView.findViewById(R.id.tvLoaiBanh); // Có thể ẩn trong XML
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            tvSoLuongDaBan = itemView.findViewById(R.id.tvSoLuongDaBan); // Có thể ẩn trong XML
            tvHinhThucBan = itemView.findViewById(R.id.tvHinhThucBan); // Có thể ẩn trong XML

            iconView = itemView.findViewById(R.id.iconView);
            iconEdit = itemView.findViewById(R.id.iconEdit);
            iconDelete = itemView.findViewById(R.id.iconDelete);
        }
    }
}