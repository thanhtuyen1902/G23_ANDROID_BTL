package vn.edu.tlu.group23.mybakeryapp.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Xóa import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private List<Product> productList;
    private final OnItemActionListener listener;

    public interface OnItemActionListener {
        void onViewClick(Product product);
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> productList, OnItemActionListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    public void setProductList(List<Product> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Thay đổi logic tải ảnh từ Glide sang tải từ resource drawable
        if (product.getHinhAnh() != null && !product.getHinhAnh().isEmpty()) {
            int imageResId = context.getResources().getIdentifier(
                    product.getHinhAnh(), "drawable", context.getPackageName());
            if (imageResId != 0) {
                holder.ivProductImage.setImageResource(imageResId);
            } else {
                // Nếu không tìm thấy resource, dùng ảnh placeholder mặc định
                holder.ivProductImage.setImageResource(R.drawable.ic_image);
                android.util.Log.e("ProductAdapter", "Image resource not found for: " + product.getHinhAnh() + ". Using default.");
            }
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_image);
            android.util.Log.e("ProductAdapter", "Image name is null or empty for product ID: " + product.getMaSP() + ". Using default.");
        }

        holder.tvProductCode.setText("Mã bánh: " + product.getMaSP());
        holder.tvProductName.setText(product.getTenSP());

        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getGiaBan());
        holder.tvProductPrice.setText("Giá: " + formattedPrice + " VNĐ");

        holder.tvProductDescription.setText(product.getMoTa());

        holder.iconView.setOnClickListener(v -> listener.onViewClick(product));
        holder.iconEdit.setOnClickListener(v -> listener.onEditClick(product));
        holder.iconDelete.setOnClickListener(v -> listener.onDeleteClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductCode;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductDescription;
        ImageView iconView;
        ImageView iconEdit;
        ImageView iconDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.imgHinhAnh);
            tvProductCode = itemView.findViewById(R.id.tvMaSP);
            tvProductName = itemView.findViewById(R.id.tvTenSP);
            tvProductPrice = itemView.findViewById(R.id.tvGiaBan);
            tvProductDescription = itemView.findViewById(R.id.tvMoTa);
            iconView = itemView.findViewById(R.id.iconView);
            iconEdit = itemView.findViewById(R.id.iconEdit);
            iconDelete = itemView.findViewById(R.id.iconDelete);
        }
    }
}