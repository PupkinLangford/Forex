package travis.cs.forex;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ForexFavAdapter extends ForexAdapter{


    ForexFavAdapter(Context context) {
        super(context);
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
                        if (!currentKey.equals("MXN") && !currentKey.equals("BRL") && !currentKey.equals("PEN")) {
                            continue;
                        }
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


}
