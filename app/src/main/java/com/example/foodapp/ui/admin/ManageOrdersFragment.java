package com.example.foodapp.ui.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;
import com.example.foodapp.R;
import com.example.foodapp.datas.OrdersDAO;
import com.example.foodapp.datas.dbConnect;
import com.example.foodapp.models.OrderDetails;
import com.example.foodapp.models.Orders;
import com.example.foodapp.models.Users;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;
import java.util.stream.Collectors;

public class ManageOrdersFragment extends Fragment {

    private OrdersDAO ordersDAO;
    private ListView lvOrders;
    private List<Orders> ordersList;
    private String currentStatus = "Chưa xác nhận";

    private Button btnPending, btnApproved, btnDelivering, btnDelivered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_orders, container, false);

        lvOrders = view.findViewById(R.id.lvOrders);
        btnPending = view.findViewById(R.id.btnPending);
        btnApproved = view.findViewById(R.id.btnApproved);
        btnDelivering = view.findViewById(R.id.btnDelivering);
        btnDelivered = view.findViewById(R.id.btnDelivered);

        ordersDAO = new OrdersDAO(requireContext());

        // Mặc định hiển thị "Chưa xác nhận"
        loadOrdersByStatus(currentStatus);

        btnPending.setOnClickListener(v -> {
            currentStatus = "Chưa xác nhận";
            loadOrdersByStatus(currentStatus);
        });

        btnApproved.setOnClickListener(v -> {
            currentStatus = "Đã xác nhận";
            loadOrdersByStatus(currentStatus);
        });

        btnDelivering.setOnClickListener(v -> {
            currentStatus = "Đang giao hàng";
            loadOrdersByStatus(currentStatus);
        });

        btnDelivered.setOnClickListener(v -> {
            currentStatus = "Đã giao hàng";
            loadOrdersByStatus(currentStatus);
        });

        lvOrders.setOnItemLongClickListener((parent, v, position, id) -> {
            Orders selectedOrder = ordersList.get(position);
            showOrderDetails(selectedOrder);
            return true;
        });

        return view;
    }

    private void loadOrdersByStatus(String status) {
        ordersList = ordersDAO.getAllOrders(status);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                ordersList.stream()
                        .map(o -> "Order #" + o.getId() + " - " + o.getStatus() + " - $" + o.getSubTotal())
                        .collect(Collectors.toList())
        );
        lvOrders.setAdapter(adapter);
    }

    private void showOrderDetails(Orders order) {
        // Lấy thông tin người dùng từ bảng Users
        dbConnect dbHelper = new dbConnect(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String userName = "Không có dữ liệu";
        String userPhone = "Không có dữ liệu";
        Cursor cursor = db.rawQuery("SELECT name, phone FROM Users WHERE id = ?", new String[]{String.valueOf(order.getUserId())});
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            userPhone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
        }
        cursor.close();

        // Lấy danh sách món ăn từ bảng OrderDetails
        List<OrderDetails> orderDetails = ordersDAO.getOrderDetails(order.getId());
        StringBuilder details = new StringBuilder();
        for (OrderDetails od : orderDetails) {
            details.append("- ")
                    .append(od.getProductName())
                    .append(" x").append(od.getQuantity())
                    .append(" ($").append(od.getPrice()).append(")\n");
        }

        String message = "👤 Tên: " + userName +
                "\n📞 SĐT: " + userPhone +
                "\n🏠 Địa chỉ: " + order.getAddress() +
                "\n💰 Tổng tiền: $" + order.getSubTotal() +
                "\n📅 Ngày đặt: " + order.getDateOrdered() +
                "\n\n🍽️ Món ăn:\n" + details;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Đơn hàng #" + order.getId())
                .setMessage(message);

        // Logic nút theo trạng thái
        switch (order.getStatus()) {
            case "Chưa xác nhận":
                builder.setPositiveButton("Xác nhận", (d, w) -> {
                    ordersDAO.updateOrderStatus(order.getId(), "Đã xác nhận");
                    loadOrdersByStatus(currentStatus);
                    Toast.makeText(requireContext(), "✅ Đơn hàng đã được xác nhận!", Toast.LENGTH_SHORT).show();
                });
                break;

            case "Đã xác nhận":
                builder.setPositiveButton("Giao hàng", (d, w) -> {
                    ordersDAO.updateOrderStatus(order.getId(), "Đang giao hàng");
                    loadOrdersByStatus(currentStatus);
                    Toast.makeText(requireContext(), "🚚 Đơn hàng đang giao!", Toast.LENGTH_SHORT).show();
                });
                break;

            case "Đang giao hàng":
                builder.setPositiveButton("Xác nhận đã giao", (d, w) -> {
                    ordersDAO.updateOrderStatus(order.getId(), "Đã giao hàng");
                    loadOrdersByStatus(currentStatus);
                    Toast.makeText(requireContext(), "🎉 Đơn hàng đã giao thành công!", Toast.LENGTH_SHORT).show();
                });
                break;
        }

        // Nếu là đơn hàng đã giao thì chỉ còn nút xóa
        builder.setNegativeButton("Xóa", (d, w) -> {
            ordersDAO.deleteOrder(order.getId());
            loadOrdersByStatus(currentStatus);
            Toast.makeText(requireContext(), "🗑️ Đã xóa đơn hàng!", Toast.LENGTH_SHORT).show();
        });

        builder.setNeutralButton("Đóng", null);
        builder.show();
    }
}
