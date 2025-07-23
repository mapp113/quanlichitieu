package com.example.quanlichitieu.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import com.example.quanlichitieu.R;
import com.example.quanlichitieu.data.local.entity.Category;
import com.example.quanlichitieu.data.local.entity.Transaction;
import com.example.quanlichitieu.data.local.entity.Type;
import com.example.quanlichitieu.viewmodel.TransactionViewModel;
import com.example.quanlichitieu.viewmodel.CategoryViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import java.util.ArrayList;
import android.view.View;
import java.text.ParseException;

public class AddEditActivity extends AppCompatActivity {

    private EditText etAmount, etDesc, etDate, etAddress;
    private RadioGroup rgType;
    private RadioButton rbIncome, rbExpense;
    private Button btnSave;
    private TransactionViewModel transactionViewModel;
    private int transactionId = -1;
    private Spinner spCategory;
    private List<Category> categoryList;
    private int selectedCategoryId = -1;
    private EditText etUserOwnerId;
    private CategoryViewModel categoryViewModel;
    private static final int REQUEST_CODE_PICK_LOCATION = 1001;
    private Button btnChooseLocation;
    private double selectedLat = 0, selectedLng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit);
        etAmount = findViewById(R.id.etAmount);
        etDesc = findViewById(R.id.etDesc);
        etDate = findViewById(R.id.etDate);
        if (transactionId == -1) {
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            etDate.setHint(today);
        }
        rgType = findViewById(R.id.rgType);
        rbIncome = findViewById(R.id.rbIncome);
        rbExpense = findViewById(R.id.rbExpense);
        btnSave = findViewById(R.id.btnSave);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        spCategory = findViewById(R.id.spCategory);
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryList = categories;
            List<String> names = new ArrayList<>();
            for (Category c : categories) names.add(c.getName());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCategory.setAdapter(adapter);
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categoryList != null && position < categoryList.size()) {
                    selectedCategoryId = categoryList.get(position).getId();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryId = -1;
            }
        });
        etAddress = findViewById(R.id.etAddress);
//        etUserOwnerId = findViewById(R.id.etUserOwnerId);
        btnChooseLocation = findViewById(R.id.btnChooseLocation);
        btnChooseLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapPickerActivity.class);
            if (selectedLat != 0 && selectedLng != 0) {
                intent.putExtra("lat", selectedLat);
                intent.putExtra("lng", selectedLng);
            }
            startActivityForResult(intent, REQUEST_CODE_PICK_LOCATION);
        });
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("transaction_id")) {
            transactionId = intent.getIntExtra("transaction_id", -1);
            if (transactionId != -1) {
                transactionViewModel.getTransactionById(transactionId).observe(this, this::populateForm);
            }
        }
        btnSave.setOnClickListener(v -> saveTransaction());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void populateForm(Transaction transaction) {
        if (transaction == null) return;
        etAmount.setText(String.valueOf(transaction.amount));
        etDesc.setText(transaction.title);
        // Format lại ngày khi hiển thị
        try {
            Date parsed = new SimpleDateFormat("dd/MM/yyyy").parse(transaction.date);
            etDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(parsed));
        } catch (Exception e) {
            etDate.setText(transaction.date != null ? transaction.date : "");
        }
        if (transaction.type == Type.INCOME) rbIncome.setChecked(true);
        else rbExpense.setChecked(true);
        etAddress.setText("");
        if (categoryList != null) {
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getId() == transaction.categoryId) {
                    spCategory.setSelection(i);
                    break;
                }
            }
        }
    }

    private void saveTransaction() {
        String amountStr = etAmount.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String dateStr = etDate.getText().toString().trim();
        if (amountStr.isEmpty() || desc.isEmpty() || rgType.getCheckedRadioButtonId() == -1 || selectedCategoryId == -1) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        double amount = Double.parseDouble(amountStr);
        String date;
        if (transactionId == -1) {
            date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        } else {
            if (dateStr.isEmpty()) {
                date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            } else {
                try {
                    Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                    date = new SimpleDateFormat("dd/MM/yyyy").format(parsed);
                } catch (ParseException e) {
                    date = dateStr;
                }
            }
        }
        Type type = rbIncome.isChecked() ? Type.INCOME : Type.EXPENSE;
        String address = etAddress.getText().toString().trim();
        int userOwnerId = 1; // TODO: Lấy userId thực tế từ session/login
        saveTransactionWithCategoryId(desc, amount, type, date, address, selectedCategoryId, userOwnerId);
    }
    private void saveTransactionWithCategoryId(String desc, double amount, Type type, String date, String address, int categoryId, int userOwnerId) {
        String addressToSave = (selectedLat != 0 && selectedLng != 0) ? (selectedLat + "," + selectedLng) : address;
        Transaction transaction = new Transaction(desc, amount, type, date, addressToSave, categoryId, userOwnerId);
        if (transactionId == -1) {
            transactionViewModel.insert(transaction);
        } else {
            transaction.id = transactionId;
            transactionViewModel.update(transaction);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_LOCATION && resultCode == Activity.RESULT_OK && data != null) {
            selectedLat = data.getDoubleExtra("lat", 0);
            selectedLng = data.getDoubleExtra("lng", 0);
            etAddress.setText(selectedLat + ", " + selectedLng);
        }
    }
}