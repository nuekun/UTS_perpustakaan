package com.uts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uts.server.Server;

import java.util.HashMap;
import java.util.Map;

public class LoginRegisterActivity extends AppCompatActivity {
    private EditText txtEmail,txtPassword;
    private Button btnDaftar,btnLogin;
    private SharedPreferences user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        txtEmail=findViewById(R.id.txtLogEmail);
        txtPassword=findViewById(R.id.txtLogPass);
        btnDaftar=findViewById(R.id.btnLogDaftar);
        btnLogin=findViewById(R.id.btnLogLogin);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilDaftar();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                login(email,password);

            }
        });


    }

    private void login(final String email, final String password) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.loginPengguna, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("1")) {
                    Toast.makeText(LoginRegisterActivity.this, "Berhasil",Toast.LENGTH_LONG).show();
                    user = getSharedPreferences("user", MODE_PRIVATE);
                    user.edit().putString("uid", email).commit();
                    Intent intent = new Intent(LoginRegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginRegisterActivity.this, "Gagal",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginRegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("email", email);
                map.put("password", password);

                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void tampilDaftar() {


        final EditText txtNama,txtEmail,txtPassword;
        final Button btnBatal,btnSimpan;

        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginRegisterActivity.this);
        View view = getLayoutInflater().inflate(R.layout.daftar_user,null);

        txtNama=view.findViewById(R.id.txtDaftarNama);
        txtEmail=view.findViewById(R.id.txtDaftarEmail);
        txtPassword=view.findViewById(R.id.txtDaftarPassword);
        btnBatal=view.findViewById(R.id.btnDafBatal);
        btnSimpan=view.findViewById(R.id.btnDaftarSimpan);

        builder.setCancelable(false);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama , email , password  ;

                nama = txtNama.getText().toString();
                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();

                daftarPenguna(nama,email,password);


            }
        });


        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginRegisterActivity.this,LoginRegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });





    }

    private void daftarPenguna(final String nama, final String email, final String password) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.daftarPengguna ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Toast.makeText(LoginRegisterActivity.this, "Berhasil, Silahkan Login !",Toast.LENGTH_LONG).show();
//                            user = getSharedPreferences("user", MODE_PRIVATE);
//                            user.edit().putString("uid", email).commit();
//                            Intent intent = new Intent(LoginRegisterActivity.this,MainActivity.class);
//                            startActivity(intent);
//                            finish();

                            Intent intent = new Intent(LoginRegisterActivity.this,LoginRegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }else{
                            Toast.makeText(LoginRegisterActivity.this, "Gagal",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginRegisterActivity.this,String.valueOf(error),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("nama", nama);
                params.put("password", password);
                params.put("status","0");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
