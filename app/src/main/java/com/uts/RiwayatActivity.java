package com.uts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uts.adapter.AdapterBuku;
import com.uts.adapter.AdapterRiwayat;
import com.uts.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RiwayatActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RecyclerView recRiwayat;
    private Spinner spinRiwayat;
    private ArrayList<HashMap<String, String>> list_data;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        recRiwayat = findViewById(R.id.recRiwayat);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recRiwayat.setLayoutManager(llm);
        spinRiwayat=findViewById(R.id.spinRiwayat);

        ambilRiwayat("");
        spinRiwayat.setOnItemSelectedListener(RiwayatActivity.this);

    }

    private void ambilRiwayat(final String status) {

        recRiwayat.setAdapter(null);
        requestQueue = Volley.newRequestQueue(this);
        list_data = new ArrayList<HashMap<String, String>>();
        list_data.clear();
        stringRequest = new StringRequest(Request.Method.POST, Server.daftarRiwayat
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Hasil");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id_pinjam", json.getString("id_pinjam"));
                        map.put("tanggal", json.getString("tanggal"));
                        map.put("status", json.getString("status"));
                        map.put("buku", json.getString("buku"));
                        map.put("pemimjam", json.getString("pemimjam"));

                        list_data.add(map);
                        AdapterRiwayat adapter = new AdapterRiwayat(RiwayatActivity.this, list_data);
                        recRiwayat.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RiwayatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("status",status);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinRiwayat.getSelectedItem().toString().equals("semua")){
            ambilRiwayat("");
        }
        else {

            String status = spinRiwayat.getSelectedItem().toString();
            ambilRiwayat(status);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
