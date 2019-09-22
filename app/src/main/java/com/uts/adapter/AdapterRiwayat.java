package com.uts.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.uts.MainActivity;
import com.uts.R;
import com.uts.RiwayatActivity;
import com.uts.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterRiwayat extends RecyclerView.Adapter<AdapterRiwayat.ViewHolder>{

    Context context;
    ArrayList<HashMap<String, String>> list_data;

    public AdapterRiwayat(RiwayatActivity riwayatActivity, ArrayList<HashMap<String, String>> list_data) {
        this.context = riwayatActivity;
        this.list_data = list_data;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.riwayat, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final String id_pinjam,status,tanggal,pemimjam,buku;

        id_pinjam = list_data.get(position).get("id_pinjam");
        status = list_data.get(position).get("status");
        tanggal = list_data.get(position).get("tanggal");
        pemimjam = list_data.get(position).get("pemimjam");
        buku = list_data.get(position).get("buku");

        holder.txtStatus.setText(status);
        holder.txtPeminjam.setText(pemimjam);
        holder.txtTanggal.setText(tanggal);
        holder.txtID.setText(buku);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(status.equals("dipinjam")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("pengembalian buku");
                    builder.setMessage("apa anda ingin mengembalikan buku ini ?");

                    builder.setPositiveButton("kembalikan", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            kembali(id_pinjam,buku);
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("nanti", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }else{


                }


            }
        });






    }

    private void kembali(final String id_pinjam, final String buku) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.kembali ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Toast.makeText(context, "buku di kembalikan",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }else{
                            Toast.makeText(context, "Gagal mengembalikan ",Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,String.valueOf(error),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_buku", buku);
                params.put("id_pinjam", id_pinjam);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTanggal,txtPeminjam,txtStatus,txtID;
        RelativeLayout root;


        public ViewHolder(View itemView) {
            super(itemView);


            root = itemView.findViewById(R.id.rootRiwayat);
            txtTanggal = itemView.findViewById(R.id.txtRiwayatTanggal);
            txtPeminjam = itemView.findViewById(R.id.txtRiwayatPeminjam);
            txtStatus = itemView.findViewById(R.id.txtRiwayatStatus);
            txtID = itemView.findViewById(R.id.txtRiwayatID);

        }
    }


}
