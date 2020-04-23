package travis.cs.forex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

public class CurrencyActivity extends AppCompatActivity {

    private TextView symbolTextView;
    private TextView symbolTextView2;
    private TextView rateTextView;
    private TextInputEditText usdInput;
    private TextInputEditText otherInput;
    private String symbol;
    private double rate;
    private RequestQueue requestQueue;
    private TextWatcher usdTextWatcher;
    private TextWatcher foreignTextWatcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        symbolTextView = findViewById(R.id.currency_symbol);
        symbolTextView2 = findViewById(R.id.currency_symbol2);
        rateTextView = findViewById(R.id.currency_rate);
        usdInput = findViewById(R.id.usd_amount);
        otherInput = findViewById(R.id.other_amount);

        usdInput.setText("1.00", TextView.BufferType.EDITABLE);
        symbol = getIntent().getStringExtra("symbol");
        rate = getIntent().getDoubleExtra("rate", 0);
        symbolTextView.setText(symbol);
        symbolTextView2.setText(symbol);
        rateTextView.setText(String.format("%.6f%n", rate));
        otherInput.setText(String.format("%.6f%n", rate), TextView.BufferType.EDITABLE);

        usdTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                otherInput.removeTextChangedListener(foreignTextWatcher);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateOutput(usdInput, otherInput, rate);
            }

            @Override
            public void afterTextChanged(Editable s) {
                otherInput.addTextChangedListener(foreignTextWatcher);
            }
        };

        foreignTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                usdInput.removeTextChangedListener(usdTextWatcher);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateOutput(otherInput, usdInput, 1/rate);
            }

            @Override
            public void afterTextChanged(Editable s) {
                usdInput.addTextChangedListener(usdTextWatcher);
            }
        };

        usdInput.addTextChangedListener(usdTextWatcher);
        otherInput.addTextChangedListener(foreignTextWatcher);


    }

    protected void updateOutput(TextInputEditText editText, TextInputEditText target, double conversionRate){
        try{
            double entryAmount = Double.parseDouble(editText.getText().toString());
            double convertedTotal = conversionRate * entryAmount;
            target.setText(String.format("%.2f%n", convertedTotal));
        } catch (Exception e){
            target.setText("0.000000");
        }
    }


}
