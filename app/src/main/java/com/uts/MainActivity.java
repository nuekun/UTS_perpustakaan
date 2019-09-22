package com.uts;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uts.adapter.AdapterBuku;
import com.uts.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btnRiwayat,btnTambah,btnCari,btnLogout;
    private EditText txtCari;
    private Spinner spinJenis;
    private RecyclerView recBuku;
    private ArrayList<HashMap<String, String>> list_data;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private Bitmap bitmap = null;
    Bitmap priview ;
    private Uri imageUri;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    ImageView gbrPriview;
    private SharedPreferences user;


    private ArrayList<String> kategori,kategoriTambah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTambah = findViewById(R.id.btnMainTambah);
        btnLogout=findViewById(R.id.btnMainLogout);
        txtCari = findViewById(R.id.txtMainCari);
        txtCari.setFocusable(false);
        btnCari=findViewById(R.id.btnMainCari);
        btnRiwayat = findViewById(R.id.btnMainRiwayat);
        recBuku = findViewById(R.id.recBuku);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recBuku.setLayoutManager(llm);
        spinJenis=findViewById(R.id.spinJenis);


        user = getSharedPreferences("user", MODE_PRIVATE);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.edit().putString("uid", "").commit();
                cekUser("");
            }
        });

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cari , jenis ;
                cari = txtCari.getText().toString();
                jenis = spinJenis.getSelectedItem().toString();

                if(jenis.equals("semua")){

                    cariData(cari,"");

                }else{

                    cariData(cari,jenis);
                }

            }
        });

        btnRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RiwayatActivity.class);
                startActivity(intent);
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tampilTambah();

            }
        });

        txtCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCari.setFocusable(true);
                txtCari.setFocusableInTouchMode(true);
            }
        });

        kategoriTambah = new ArrayList<String>();
        kategori = new ArrayList<String>();
        kategori.add("semua");

        spinJenis.setOnItemSelectedListener(this);

        cariData("","");
        ambilKategori();
        Permissions();


    }

    @Override
    protected void onStart() {
        super.onStart();


        String uid = user.getString("uid", "");
        cekUser(uid);

    }

    private void cekUser(String uid) {
        if (uid.equals("")){
            Intent login = new Intent(this,LoginRegisterActivity.class);
            startActivity(login);

        }
    }

    private void tampilTambah() {

        final EditText txtjudul,txtpengarang,txtKeterangan;
        final Spinner spinJenis ;
        Button btnJenis,btnGambar,btnSimpan,btnBatal;


        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.tambah_buku,null);
        txtjudul = view.findViewById(R.id.txtTambahBukuJudul);
        txtpengarang = view.findViewById(R.id.txtTambahBukuPengaran);
        txtKeterangan = view.findViewById(R.id.txtTambahBukuKeterangan);
        spinJenis = view.findViewById(R.id.spinTambahBukuJenis);
        gbrPriview = view.findViewById(R.id.gbrTambahBukuPriview);



        spinJenis.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item, kategoriTambah));



        btnJenis = view.findViewById(R.id.btnTambahBukuJenis);
        btnGambar = view.findViewById(R.id.btnTambahBukuGambar);
        btnSimpan = view.findViewById(R.id.btnTambahBukuSImpan);
        btnBatal = view.findViewById(R.id.btnTambahBukuBatal);
        builder.setView(view);
        builder.show();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String judul , pengarang , jenis , keterangan ;

                judul = txtjudul.getText().toString();
                pengarang = txtpengarang.getText().toString();
                jenis = spinJenis.getSelectedItem().toString();
                keterangan = txtKeterangan.getText().toString();



                tambahBuku(judul,pengarang,jenis,keterangan);
            }
        });

        btnJenis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("masukkan jenis buku ");


                final EditText input = new EditText(MainActivity.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                builder.setView(input);

                builder.setPositiveButton("simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String jenis = input.getText().toString();
                        tambahKatagori(jenis);


                        spinJenis.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, kategoriTambah));





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
        });

        btnGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ambilFoto();
              //  gbrPriview.setImageBitmap(getBitmap());
                bitmap = getPriview();
                gbrPriview.setImageBitmap(bitmap);



            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });



    }


    private void tambahBuku(final String judul, final String pengarang, final String jenis, final String keterangan) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.tambahBuku ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Toast.makeText(MainActivity.this, "Berhasil",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);




                        }else{
                            Toast.makeText(MainActivity.this, "Gagal",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,String.valueOf(error),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("judul", judul);
                params.put("pengarang",pengarang);
                params.put("katagori",jenis);
                params.put("keterangan",keterangan);
                params.put("gambar",getStringImage(bitmap));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void tambahKatagori(final String jenis) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.tambahJenis ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Toast.makeText(MainActivity.this, "Berhasil",Toast.LENGTH_LONG).show();
                            ambilKategori();

                        }else{
                            Toast.makeText(MainActivity.this, "Gagal",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,String.valueOf(error),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("jenis", jenis);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void ambilFoto() {

        final CharSequence[] options = {"Ambil Foto", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(
                MainActivity.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Ambil Foto")) {
                    String fileName = "new-photo-name.jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(intent, PICK_Camera_IMAGE);
                } else if (options[item].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, PICK_IMAGE);
                }
            }
        });
        builder.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();
                }
                break;
            case PICK_Camera_IMAGE:
                if (resultCode == RESULT_OK) {
                    selectedImageUri = imageUri;
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if (selectedImageUri != null) {
            try {
                String filemanagerstring = selectedImageUri.getPath();
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {
                    filePath = selectedImagePath;
                } else if (filemanagerstring != null) {
                    filePath = filemanagerstring;
                } else {
                    Toast.makeText(MainActivity.this, "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Internal error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);
        //setPriview(bitmap);
        gbrPriview.setImageBitmap(bitmap);

    }

    public Bitmap getPriview() {
        return priview;
    }

    public void setPriview(Bitmap priview) {
        this.priview = priview;
    }

    private void Permissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA},
                99);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 99: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Permissions();
                }
                return;
            }
        }
    }




    private void cariData(final String cari,final String jenis) {
        recBuku.setAdapter(null);
        requestQueue = Volley.newRequestQueue(this);
        list_data = new ArrayList<HashMap<String, String>>();
        list_data.clear();
        stringRequest = new StringRequest(Request.Method.POST, Server.CariBuku
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
                        map.put("id_buku", json.getString("id_buku"));
                        map.put("judul", json.getString("judul"));
                        map.put("pengarang", json.getString("pengarang"));
                        map.put("pemimjam", json.getString("pemimjam"));
                        map.put("status", json.getString("status"));
                        map.put("gambar", json.getString("gambar"));
                        map.put("jenis", json.getString("jenis"));
                        map.put("riwayat", json.getString("riwayat"));
                        map.put("keterangan", json.getString("keterangan"));
                        map.put("user",user.getString("uid", ""));
                        list_data.add(map);
                        AdapterBuku adapter = new AdapterBuku(MainActivity.this, list_data);
                        recBuku.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cari",cari);
                params.put("jenis",jenis);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void ambilKategori() {

        requestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, Server.ambilKategori
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response ", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Hasil");
                    for (int a = 0; a < jsonArray.length(); a++) {
                        JSONObject json = jsonArray.getJSONObject(a);
                        kategori.add(json.getString("jenis"));
                        kategoriTambah.add(json.getString("jenis"));

                    }
                    spinJenis.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, kategori));




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (spinJenis.getSelectedItem().toString().equals("semua")){
            cariData("","");
        }
        else {

            String jenis = spinJenis.getSelectedItem().toString();
            cariData("",jenis);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
