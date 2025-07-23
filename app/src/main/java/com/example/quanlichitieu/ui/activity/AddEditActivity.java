package com.example.quanlichitieu.ui.activity;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.widget.ArrayAdapter;

public class AddEditActivity extends AppCompatActivity {

    private EditText etAmount, etDesc, etDate, etAddress;
    private RadioGroup rgType;
    private RadioButton rbIncome, rbExpense;
    private Button btnSave;
    private TransactionViewModel transactionViewModel;
    private int transactionId = -1;
    // private Spinner spCategory;
    // private List<Category> categoryList;
    private EditText etUserOwnerId;
    // private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit);
        etAmount = findViewById(R.id.etAmount);
        etDesc = findViewById(R.id.etDesc);
        etDate = findViewById(R.id.etDate);
        // Nếu là thêm mới, set hint ngày hiện tại cho etDate
        if (transactionId == -1) {
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            etDate.setHint(today);
        }
        rgType = findViewById(R.id.rgType);
        rbIncome = findViewById(R.id.rbIncome);
        rbExpense = findViewById(R.id.rbExpense);
        btnSave = findViewById(R.id.btnSave);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        // categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        // spCategory = findViewById(R.id.spCategory);
        etAddress = findViewById(R.id.etAddress);
//        etUserOwnerId = findViewById(R.id.etUserOwnerId);
        // categoryViewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
        //     @Override
        //     public void onChanged(List<Category> categories) {
        //         categoryList = categories;
        //         ArrayAdapter<String> adapter = new ArrayAdapter<>(AddEditActivity.this, android.R.layout.simple_spinner_item);
        //         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //         for (Category c : categories) adapter.add(c.getName());
        //         spCategory.setAdapter(adapter);
        //     }
        // });
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
        etDate.setText(transaction.date != null ? transaction.date : "");
        if (transaction.type == Type.INCOME) rbIncome.setChecked(true);
        else rbExpense.setChecked(true);
        etAddress.setText("");
        // set category spinner selection
        // if (categoryList != null) {
        //     for (int i = 0; i < categoryList.size(); i++) {
        //         if (categoryList.get(i).getId() == transaction.category) {
        //             spCategory.setSelection(i);
        //             break;
        //         }
        //     }
        // }
    }

    private void saveTransaction() {
        String amountStr = etAmount.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String dateStr = etDate.getText().toString().trim();
        if (amountStr.isEmpty() || desc.isEmpty() || rgType.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        double amount = Double.parseDouble(amountStr);
        String date;
        if (transactionId == -1) {
            // Thêm mới: date là ngày hiện tại
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        } else {
            // Sửa: cho phép nhập date
            date = dateStr.isEmpty() ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : dateStr;
        }
        Type type = rbIncome.isChecked() ? Type.INCOME : Type.EXPENSE;
        // int categoryId = 0;
        // if (categoryList != null && spCategory.getSelectedItemPosition() >= 0 && spCategory.getSelectedItemPosition() < categoryList.size()) {
        //     categoryId = categoryList.get(spCategory.getSelectedItemPosition()).getId();
        // }
        String address = etAddress.getText().toString().trim();
        // int userOwnerId = 0;
        // try { userOwnerId = Integer.parseInt(etUserOwnerId.getText().toString().trim()); } catch (Exception ignored) {}
        Transaction transaction = new Transaction(desc, amount, type, date, address.hashCode());
        if (transactionId == -1) {
            transactionViewModel.insert(transaction);
        } else {
            transaction.id = transactionId;
            transactionViewModel.update(transaction);
        }
        setResult(RESULT_OK);
        finish();
    }
}