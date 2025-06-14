package com.example.bobfriend;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bobfriend.adapters.OrderAdapter;
import com.example.bobfriend.database.DatabaseHelper;
import com.example.bobfriend.database.SharedPrefManager;
import com.example.bobfriend.models.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerMenus;
    private TextView tvTotalPrice;
    private Button btnOrder;
    private OrderAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Order> orderList = new ArrayList<>();
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        dbHelper = new DatabaseHelper(this);
        initViews();
        loadMenus();
        setupClickListeners();
    }

    private void initViews() {
        recyclerMenus = findViewById(R.id.recyclerMenus);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnOrder = findViewById(R.id.btnOrder);

        recyclerMenus.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMenus() {
        // 더미 메뉴 데이터
        orderList.add(new Order("김치찌개", 8000, 0));
        orderList.add(new Order("제육볶음", 9000, 0));
        orderList.add(new Order("된장찌개", 7000, 0));
        orderList.add(new Order("공기밥", 1000, 0));

        adapter = new OrderAdapter(orderList, this::updateTotalPrice);
        recyclerMenus.setAdapter(adapter);
    }

    private void updateTotalPrice() {
        totalPrice = 0;
        for (Order order : orderList) {
            totalPrice += order.getPrice() * order.getQuantity();
        }
        tvTotalPrice.setText("총 금액: " + totalPrice + "원");
    }

    private void setupClickListeners() {
        btnOrder.setOnClickListener(v -> {
            if (totalPrice > 0) {
                String username = SharedPrefManager.getCurrentUserNickname(this);
                Toast.makeText(this, username + "님의 주문이 완료되었습니다!\n총 " + totalPrice + "원",
                        Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "메뉴를 선택하세요", Toast.LENGTH_SHORT).show();
            }
        });
    }
}