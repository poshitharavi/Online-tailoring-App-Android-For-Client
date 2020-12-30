package com.example.tailorar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQ_CODE = 102;
    private static final int GALLERY_REQ_CODE = 105;
    private RequestQueue mRequestQueue;
    ImageView selectedImage;
    Button cameraBtn, galleryBtn, submitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        selectedImage = (ImageView) findViewById(R.id.imageViewer);
        cameraBtn = (Button) findViewById(R.id.btnCamera);
        galleryBtn = (Button) findViewById(R.id.btnGallery);
        submitBtn = (Button) findViewById(R.id.btnSubmit);
        mRequestQueue = Volley.newRequestQueue(this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitDetailsToApi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQ_CODE);
            }
        });
    }

    private void askCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera opening request", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQ_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            selectedImage.setImageBitmap(image);

        } else if (requestCode == GALLERY_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("CameraACtivity", "onActivityResult : Gallery Image Uri" + imageFileName);
                selectedImage.setImageURI(contentUri);

            }
        }
    }

    private String getFileExt(Uri contentUri) {

        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    public static String encodedBase64New(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;


    }

    public void submitDetailsToApi() throws JSONException {

        submitBtn.setEnabled(false);
        galleryBtn.setEnabled(false);
        cameraBtn.setEnabled(false);


//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("height", 178);
//        jsonObject.put("chest", 128);
//        jsonObject.put("wasitWidth", 108);
//        jsonObject.put("hip", 94);
//        jsonObject.put("legLength", 72);
//        jsonObject.put("shoulderWidth", 49);
//
//        Intent intent = new Intent(getApplicationContext(), EditMesurmentActivity.class);
//        intent.putExtra(EditMesurmentActivity.KEY_EXTRA, jsonObject.toString());
//        startActivity(intent);

        SharedPreferences loginPreferences = getSharedPreferences("tailorUserDetails", MODE_PRIVATE);
        String userId = loginPreferences.getString("user_id", "0");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapDrawable drawable = (BitmapDrawable) selectedImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        Log.d("bites", String.valueOf(imageBytes));
        String imageString = Base64.encodeToString(imageBytes, Base64.NO_WRAP);


        JSONObject reqObj = new JSONObject();
        reqObj.put("userId", userId);
        reqObj.put("baseCode", imageString);

        System.out.print(reqObj.get("baseCode"));
        Log.d("tag", reqObj.toString());

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, getResources().getString(R.string.api_url)+"mesurment/predict", reqObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                    Log.d("Data: ", jsonObject.toString());
                    Intent intent = new Intent(getApplicationContext(), EditMesurmentActivity.class);
                    intent.putExtra(EditMesurmentActivity.KEY_EXTRA, jsonObject.toString());
                    startActivity(intent);
                    finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error: ", volleyError.toString());
                submitBtn.setEnabled(true);
                galleryBtn.setEnabled(true);
                cameraBtn.setEnabled(true);
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(req);
    }
}
