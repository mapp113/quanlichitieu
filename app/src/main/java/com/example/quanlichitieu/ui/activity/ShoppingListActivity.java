package com.example.quanlichitieu.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.SessionManager;
import com.example.quanlichitieu.data.local.database.appDatabase;
import com.example.quanlichitieu.data.local.entity.ShoppingItem;
import com.example.quanlichitieu.ui.adapter.ShoppingListAdapter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;
import java.util.stream.Collectors;

public class ShoppingListActivity extends AppCompatActivity {

    private ShoppingListAdapter uncheckedAdapter, checkedAdapter;
    private List<ShoppingItem> allItems;
    private appDatabase db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        userId = SessionManager.getUserId(this);

        RecyclerView uncheckedList = findViewById(R.id.uncheckedItems);
        RecyclerView checkedList = findViewById(R.id.checkedItems);
        EditText etItemName = findViewById(R.id.etItemName);
        Button btnAddItem = findViewById(R.id.btnAddItem);

        db = appDatabase.getInstance(this);
        allItems = db.shoppingItemDao().getShoppingItemsByUserId(userId);

        uncheckedAdapter = new ShoppingListAdapter(getFilteredItems(false), itemActionListener);
        checkedAdapter = new ShoppingListAdapter(getFilteredItems(true), itemActionListener);

        uncheckedList.setLayoutManager(new LinearLayoutManager(this));
        checkedList.setLayoutManager(new LinearLayoutManager(this));
        uncheckedList.setAdapter(uncheckedAdapter);
        checkedList.setAdapter(checkedAdapter);

        btnAddItem.setOnClickListener(v -> {
            String name = etItemName.getText().toString().trim();
            if (!name.isEmpty()) {
                ShoppingItem item = new ShoppingItem( name, false, userId);
                db.shoppingItemDao().insertShoppingItem(item);
                refreshItems();
                etItemName.setText("");
            }
        });
    }

    private void refreshItems() {
        allItems = db.shoppingItemDao().getShoppingItemsByUserId(userId);
        uncheckedAdapter.updateList(getFilteredItems(false));
        checkedAdapter.updateList(getFilteredItems(true));
    }

    private List<ShoppingItem> getFilteredItems(boolean isChecked) {
        return allItems.stream()
                .filter(item -> item.isChecked() == isChecked)
                .collect(Collectors.toList());
    }

    private final ShoppingListAdapter.OnItemActionListener itemActionListener = new ShoppingListAdapter.OnItemActionListener() {
        @Override
        public void onItemCheckedChanged(ShoppingItem item, boolean isChecked) {
            item.setChecked(isChecked);
            db.shoppingItemDao().updateShoppingItem(item);
            new android.os.Handler().post(() -> refreshItems());
        }

        @Override
        public void onItemDeleted(ShoppingItem item) {
            db.shoppingItemDao().deleteShoppingItem(item);
            new android.os.Handler().post(() -> refreshItems());
        }
    };
}