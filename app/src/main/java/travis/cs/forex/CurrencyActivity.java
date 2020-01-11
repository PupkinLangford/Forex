package travis.cs.forex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

public class CurrencyActivity extends AppCompatActivity {

    private TextView symbolTextView;
    private TextView rateTextView;
    private TextInputEditText usdInput;
    private String symbol;
    private double rate;
    private double usdAmount;
    private double convertedTotal;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        symbolTextView = findViewById(R.id.currency_symbol);
        rateTextView = findViewById(R.id.currency_rate);
        usdInput = findViewById(R.id.usd_amount);
        symbolTextView.setText(symbol);
        usdInput.setText("1.00", TextView.BufferType.EDITABLE);
        symbol = getIntent().getStringExtra("symbol");
        rate = getIntent().getDoubleExtra("rate", 0);
        updateOutput();

        usdInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateOutput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    protected void updateOutput(){
        try{
                usdAmount = Double.parseDouble(usdInput.getText().toString());
                convertedTotal = rate * usdAmount;
                rateTextView.setText(String.format("%.6f%n", convertedTotal));
        } catch (Exception e){
            rateTextView.setText("0.000000");
        }
    }


}
