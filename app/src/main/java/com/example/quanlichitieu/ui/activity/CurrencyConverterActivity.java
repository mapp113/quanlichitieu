package com.example.quanlichitieu.ui.activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quanlichitieu.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CurrencyConverterActivity extends AppCompatActivity{
    private TextInputEditText amountInput1, amountInput2;
    private TextView currencyPicker1, currencyPicker2;
    ImageView swapButton;

    private final List<String> currencyList = Arrays.asList("VND", "USD", "EUR", "JPY");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.currency_exchange_tool); // đổi thành tên file XML thật, ví dụ: activity_main

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Khởi tạo view
        amountInput1 = findViewById(R.id.amount_input1);
        amountInput2 = findViewById(R.id.amount_input2);
        currencyPicker1 = findViewById(R.id.currency_picker1);
        currencyPicker2 = findViewById(R.id.currency_picker2);
        swapButton = findViewById(R.id.swap_icon);

        // Gắn dropdown cho mỗi TextView
        setupCurrencyPicker(currencyPicker1);
        setupCurrencyPicker(currencyPicker2);

        // Gợi ý xử lý thêm: chuyển đổi tiền tệ khi người dùng nhập
        // Hoặc bạn có thể thêm nút chuyển đổi nếu muốn
        amountInput1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performConversion(); // gọi hàm chuyển đổi mỗi khi người dùng gõ
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        swapButton.setOnClickListener(v -> {
            CharSequence from = currencyPicker1.getText();
            CharSequence to = currencyPicker2.getText();

            currencyPicker1.setText(to);
            currencyPicker2.setText(from);

            // Đổi giá trị nhập vào giữa hai ô
//            CharSequence input = amountInput1.getText();
//            CharSequence output = amountInput2.getText();
//
//            amountInput1.setText(output);
//            amountInput2.setText(input);

            performConversion(); // cập nhật lại kết quả sau khi đổi đơn vị
        });
        toolbar.setNavigationOnClickListener(v -> finish());

    }

    private void setupCurrencyPicker(TextView picker) {
        picker.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CurrencyConverterActivity.this, picker);
            for (int i = 0; i < currencyList.size(); i++) {
                popup.getMenu().add(0, i, i, currencyList.get(i));
            }
            popup.setOnMenuItemClickListener(item -> {
                picker.setText(item.getTitle());
                return true;
            });
            popup.show();
        });
    }

    // Lấy giá trị nhập vào khi cần xử lý
    private void getInputValues() {
        String amountStr1 = amountInput1.getText() != null ? amountInput1.getText().toString().trim() : "";
        String currency1 = currencyPicker1.getText().toString();

        String amountStr2 = amountInput2.getText() != null ? amountInput2.getText().toString().trim() : "";
        String currency2 = currencyPicker2.getText().toString();

        double amount1 = amountStr1.isEmpty() ? 0.0 : parseSafeDouble(amountStr1);
        double amount2 = amountStr2.isEmpty() ? 0.0 : parseSafeDouble(amountStr2);

        Toast.makeText(this, "Input 1: " + amount1 + " " + currency1 + "\nInput 2: " + amount2 + " " + currency2, Toast.LENGTH_SHORT).show();
    }

    private double parseSafeDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    private void performConversion() {
        String inputStr = amountInput1.getText() != null ? amountInput1.getText().toString().trim() : "";
        String fromCurrency = currencyPicker1.getText().toString();
        String toCurrency = currencyPicker2.getText().toString();

        double inputAmount = inputStr.isEmpty() ? 0.0 : parseSafeDouble(inputStr);

        // Tỉ giá mẫu, bạn có thể thay bằng API thực
        double rate = getExchangeRate(fromCurrency, toCurrency);

        double converted = inputAmount * rate;

        amountInput2.setText(String.format(Locale.ENGLISH,"%.2f", converted));
    }

    private double getExchangeRate(String from, String to) {
        if (from.equals(to)) return 1.0;
        if (from.equals("USD") && to.equals("VND")) return 24000;
        if (from.equals("VND") && to.equals("USD")) return 1.0 / 24000;
        if (from.equals("EUR") && to.equals("VND")) return 26000;
        if (from.equals("VND") && to.equals("EUR")) return 1.0 / 26000;
        // mặc định nếu không có dữ liệu
        return 1.0;
    }

}
