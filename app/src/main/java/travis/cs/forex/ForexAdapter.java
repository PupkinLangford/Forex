package travis.cs.forex;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ForexAdapter extends RecyclerView.Adapter<ForexAdapter.ForexViewHolder> implements Filterable {
    public static class ForexViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout containerView;
        public TextView textView;

        ForexViewHolder(View view){
            super(view);

            containerView = view.findViewById(R.id.forex_row);
            textView = view.findViewById(R.id.forex_row_text_view);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Currency current = (Currency) containerView.getTag();
                    Intent intent = new Intent(v.getContext(), CurrencyActivity.class);
                    intent.putExtra("symbol", current.getSymbol());
                    intent.putExtra("rate", current.getRate());
                    v.getContext().startActivity(intent);
                }
            });


        }
    }

    protected List<Currency> currencyList = new ArrayList<>();
    protected RequestQueue requestQueue;
    protected List<Currency> filtered = new ArrayList<>();

    ForexAdapter(Context context){
        requestQueue = Volley.newRequestQueue(context);
        loadCurrencies();
    }


    public void loadCurrencies(){
        String url = "http://data.fixer.io/api/latest?access_key=" + Constants.API_KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject rates = response.getJSONObject("rates");
                    double usd = rates.getDouble("USD");
                    Iterator<String> iterator = rates.keys();
                    while (iterator.hasNext()){
                        String currentKey = iterator.next();
                        double currentRate = rates.getDouble(currentKey) / usd;
                        Currency currentCurrency = new Currency(currentKey, "", currentRate);
                        currencyList.add(currentCurrency);
                    }
                    filtered = new ArrayList<>(currencyList);
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("!!!!!!", "JSON Error", e);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("!!!!!!!", "currency list error", error);
            }
        });
        requestQueue.add(request);
    }

    private class CurrencyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Currency> filteredCurrency = new ArrayList<>();
            for (int i = 0; i < currencyList.size(); i++){
                if (currencyList.get(i).getSymbol().toLowerCase().contains(constraint.toString().toLowerCase())){
                    filteredCurrency.add(currencyList.get(i));
                }
            }
            results.values = filteredCurrency;
            results.count = filteredCurrency.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filtered = (List<Currency>) results.values;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ForexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.forex_row, parent, false);

        return new ForexViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForexViewHolder holder, int position) {
        Currency current = filtered.get(position);
        holder.textView.setText(current.toString());
        holder.containerView.setTag(current);

    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    public Filter getFilter() {
        return new CurrencyFilter();
    }
}


