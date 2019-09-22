package com.uts;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.uts.server.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {

    private Button btnKatagori,btnUpdate,btnFoto;
    private EditText txtJudul,txtPengarang,txtKeterangan;
    private Spinner spinKatagori;
    private ImageView gbrPriview;
    private Bitmap bitmap = null;
    private Uri imageUri;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_Camera_IMAGE = 2;
    private ArrayList<String> kategori;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent = getIntent();
        final String id_buku = intent.getStringExtra("id_buku");
        final String judul = intent.getStringExtra("judul");
        final String pengarang = intent.getStringExtra("pengarang");
        final String jenis = intent.getStringExtra("jenis");
        final String keterangan = intent.getStringExtra("keterangan");
        final String gambar = intent.getStringExtra("gambar");

        btnKatagori=findViewById(R.id.btnEditBukuJenis);
        btnFoto=findViewById(R.id.btnEditBukuGambar);
        btnUpdate=findViewById(R.id.btnEditBukuSImpan);
        txtJudul=findViewById(R.id.txtEditBukuJudul);
        txtKeterangan=findViewById(R.id.txtEditBukuKeterangan);
        txtPengarang=findViewById(R.id.txtEditBukuPengaran);
        spinKatagori=findViewById(R.id.spinEditBukuJenis);
        gbrPriview=findViewById(R.id.gbrEditBukuPriview);

        txtJudul.setText(judul);
        txtPengarang.setText(pengarang);
        txtKeterangan.setText(keterangan);
        Glide.with(UpdateActivity.this).load(gambar).into(gbrPriview);



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bitmap = ((BitmapDrawable)gbrPriview.getDrawable()).getBitmap();
                setBitmap(bitmap);
                String judul = txtJudul.getText().toString();
                String pengarang = txtPengarang.getText().toString();
                String kategori = spinKatagori.getSelectedItem().toString();
                String keterangan = txtKeterangan.getText().toString();
                updateBuku(id_buku,judul,pengarang,kategori,keterangan);
            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilFoto();
            }
        });


        btnKatagori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogJenis();

            }
        });





        ambilKatagori();

    }

    private void updateBuku(final String id_buku, final String judul, final String pengarang, final String kategori, final String keterangan) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.editBuku ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("1")) {
                            Toast.makeText(UpdateActivity.this, "Berhasil",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(UpdateActivity.this, "Gagal",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateActivity.this,String.valueOf(error),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_buku",id_buku);
                params.put("judul",judul);
                params.put("pengarang",pengarang);
                params.put("katagori",kategori);
                params.put("keterangan",keterangan);
                params.put("gambar",getStringImage(bitmap));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void dialogJenis() {


        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setTitle("masukkan jenis buku ");


        final EditText input = new EditText(UpdateActivity.this);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        builder.setView(input);

        builder.setPositiveButton("simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String jenis = input.getText().toString();
                tambahKatagori(jenis);


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


    private void ambilKatagori() {

        kategori = new ArrayList<String>();
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

                    }

                    spinKatagori.setAdapter(new ArrayAdapter<String>(UpdateActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, kategori));



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
                            Toast.makeText(UpdateActivity.this, "Berhasil",Toast.LENGTH_LONG).show();
                            ambilKatagori();

                        }else{
                            Toast.makeText(UpdateActivity.this, "Gagal",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateActivity.this,String.valueOf(error),Toast.LENGTH_LONG).show();
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
                UpdateActivity.this);
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
                    Toast.makeText(UpdateActivity.this, "Unknown path",
                            Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {
                    decodeFile(filePath);
                } else {
                    bitmap = null;
                }
            } catch (Exception e) {
                Toast.makeText(UpdateActivity.this, "Internal error",
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
        gbrPriview.setImageBitmap(bitmap);

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
