package travis.cs.forex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class CurrencyActivity extends AppCompatActivity {

    private TextView symbolTextView;
    private TextView rateTextView;
    private String symbol;
    private double rate;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        symbolTextView = findViewById(R.id.currency_symbol);
        rateTextView = findViewById(R.id.currency_rate);
        symbol = getIntent().getStringExtra("symbol");
        rate = getIntent().getDoubleExtra("rate", 0);

        symbolTextView.setText(symbol);
        rateTextView.setText(String.format("%.6f%n", rate));

    }


}
