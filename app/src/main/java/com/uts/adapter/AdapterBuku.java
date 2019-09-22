package com.uts.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
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
import com.uts.UpdateActivity;
import com.uts.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterBuku extends RecyclerView.Adapter<AdapterBuku.ViewHolder>{

    Context context;
    ArrayList<HashMap<String, String>> list_data;


    public AdapterBuku(MainActivity mainActivity, ArrayList<HashMap<String, String>> list_data) {
        this.context = mainActivity;
        this.list_data = list_data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buku, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final String gambar = list_data.get(position).get("gambar");

        final String url = Server.URL_DATA+"/buku/"+gambar;

        final String id_buku,judul,pengarang,pemimjam,status,jenis,riwayat,keterangan,user;
        id_buku = list_data.get(position).get("id_buku");
        judul = list_data.get(position).get("judul");
        pengarang = list_data.get(position).get("pengarang");
        pemimjam = list_data.get(position).get("pemimjam");
        status = list_data.get(position).get("status");
        jenis = list_data.get(position).get("jenis");
        riwayat = list_data.get(position).get("riwayat");
        keterangan = list_data.get(position).get("keterangan");
        user = list_data.get(position).get("user");

        holder.txtJudul.setText(judul);
        holder.txtPengaran.setText(pengarang);
        Glide.with(context).load(url).into(holder.gbrBuku);
        holder.txtKeterangan.setText(keterangan);

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusBuku(id_buku);
            }
        });
        holder.btnPinjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinjamBuku(user,id_buku);
               // pinjam(id_buku);

            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id_buku",id_buku);
                intent.putExtra("judul",judul);
                intent.putExtra("pengarang",pengarang);
                intent.putExtra("pemimjam",pemimjam);
                intent.putExtra("status",status);
                intent.putExtra("jenis",jenis);
                intent.putExtra("riwayat",riwayat);
                intent.putExtra("keterangan",keterangan);
                intent.putExtra("gambar",url);
                context.startActivity(intent);


            }
        });


    }

    private void pinjam(final String id_buku) {





        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("masukkan nama pemimjam");


        final EditText input = new EditText(context);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);

        builder.setPositiveButton("simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String peminjam = input.getText().toString();

                pinjamBuku(peminjam,id_buku);


            }
        });
        builder.setNegativeButton("batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        builder.show();


    }

    private void pinjamBuku(final String pemimjam, final String id_buku) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.pinjambuku ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Toast.makeText(context, "Berhasil Meminjam",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        }else{
                            Toast.makeText(context, "Gagal",Toast.LENGTH_LONG).show();

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
                params.put("pemimjam", pemimjam);
                params.put("id_buku", id_buku);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);



    }

    private void hapusBuku(final String id_buku) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.hapusBuku ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Toast.makeText(context, "di hapus",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(context,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);


                        }else{
                            Toast.makeText(context, "Gagal",Toast.LENGTH_LONG).show();
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
                params.put("id_buku", id_buku);

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
        TextView txtJudul,txtPengaran,txtKeterangan;
        ImageView gbrBuku;
        LinearLayout root;
        Button btnEdit,btnHapus,btnPinjam;


        public ViewHolder(View itemView) {
            super(itemView);

            txtJudul = itemView.findViewById(R.id.txtBukuJudul);
            gbrBuku =  itemView.findViewById(R.id.gbrBukuPriview);
            txtPengaran = itemView.findViewById(R.id.txtBukuPengarang);
            txtKeterangan = itemView.findViewById(R.id.txtBukuKeterangan);
            root = itemView.findViewById(R.id.rootBuku);
            btnEdit = itemView.findViewById(R.id.btnBukuEdit);
            btnHapus = itemView.findViewById(R.id.btnBukuHapus);
            btnPinjam = itemView.findViewById(R.id.btnBukuPinjam);

        }
    }


}