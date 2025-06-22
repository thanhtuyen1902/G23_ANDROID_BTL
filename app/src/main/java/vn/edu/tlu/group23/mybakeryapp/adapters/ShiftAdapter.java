package vn.edu.tlu.group23.mybakeryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.models.Shift;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder>{
    private List<Shift> shiftList;
    private Context context;
    private OnShiftClickListener listener;

    public interface OnShiftClickListener {
        void onShiftClick(Shift shift);
    }
    public ShiftAdapter(Context context, List<Shift> shiftList, OnShiftClickListener listener) {
        this.context = context;
        this.shiftList = shiftList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shift, parent, false);
        return new ShiftViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ShiftViewHolder holder, int position) {
        Shift shift = shiftList.get(position);
        holder.tvShiftName.setText(shift.getName());
        holder.tvShiftTime.setText(shift.getStartTime() + " - " + shift.getEndTime());
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onShiftClick(shift);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    public void updateData(List<Shift> newList) {
        shiftList = newList;
        notifyDataSetChanged();
    }
    static class ShiftViewHolder extends RecyclerView.ViewHolder {
        TextView tvShiftName, tvShiftTime;

        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShiftName = itemView.findViewById(R.id.tvShiftName);
            tvShiftTime = itemView.findViewById(R.id.tvShiftTime);
        }
    }
}
