package vn.edu.tlu.group23.mybakeryapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.tlu.group23.mybakeryapp.R;
import vn.edu.tlu.group23.mybakeryapp.database.TaskDAO;
import vn.edu.tlu.group23.mybakeryapp.models.Task;

public class DeleteConfirmDialog extends Dialog {
    private final Context context;
    private final String itemName;
    private final DeleteAction deleteAction;
    private final Runnable onDeleted;
    TextView dialogMessage;
    ImageView closeButton;
    Button buttonDelete, buttonCancel;
    public interface DeleteAction {
        boolean delete();
    }
    public DeleteConfirmDialog(Context context, String itemName, DeleteAction deleteAction, Runnable onDeleted) {
        super(context);
        this.context = context;
        this.itemName = itemName;
        this.deleteAction = deleteAction;
        this.onDeleted = onDeleted;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_delete_taskshift);

        if (getWindow() != null) {
            getWindow().setLayout(
                    (int)(context.getResources().getDisplayMetrics().widthPixels * 0.9),
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialogMessage = findViewById(R.id.dialogMessage);
        closeButton = findViewById(R.id.closeButton);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Hiển thị tên công việc trong thông báo
        dialogMessage.setText("Bạn có chắc chắn muốn xóa \"" + itemName + "\" khỏi danh sách không?");

        // Đóng dialog khi nhấn close hoặc cancel
        closeButton.setOnClickListener(v -> dismiss());
        buttonCancel.setOnClickListener(v -> dismiss());

        // Xử lý xóa công việc
        buttonDelete.setOnClickListener(v -> {
            boolean success = deleteAction.delete();
            if (success) {
                Toast.makeText(context, "Đã xóa " + itemName + " khỏi danh sách!", Toast.LENGTH_SHORT).show();
                onDeleted.run();
            } else {
                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });
    }

}
