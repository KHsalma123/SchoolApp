package com.schoolapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.schoolapp.R;
import com.schoolapp.adapters.DocumentsAdapter;
import com.schoolapp.database.DatabaseHelper;
import com.schoolapp.models.Document;
import com.schoolapp.utils.PrefsManager;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DocumentActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA    = 201;
    private static final int REQUEST_GALLERY   = 202;
    private static final int REQUEST_CAM_PERM  = 203;

    private ImageView ivPreview;
    private TextInputEditText etDocName;
    private MaterialButton btnCamera, btnGallery, btnSave;
    private RecyclerView rvDocuments;
    private ImageButton btnBack;
    private View cardPreview;

    private Uri photoUri;
    private String currentPhotoPath;
    private List<Document> documents = new ArrayList<>();
    private DocumentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        ivPreview  = findViewById(R.id.iv_preview);
        etDocName  = findViewById(R.id.et_doc_name);
        btnCamera  = findViewById(R.id.btn_camera);
        btnGallery = findViewById(R.id.btn_gallery);
        btnSave    = findViewById(R.id.btn_save);
        rvDocuments= findViewById(R.id.rv_documents);
        btnBack    = findViewById(R.id.btn_back);
        cardPreview= findViewById(R.id.card_preview);

        btnBack.setOnClickListener(v -> finish());
        btnCamera.setOnClickListener(v -> openCamera());
        btnGallery.setOnClickListener(v -> openGallery());
        btnSave.setOnClickListener(v -> saveDocument());

        rvDocuments.setLayoutManager(new GridLayoutManager(this, 2));
        loadDocuments();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAM_PERM);
            return;
        }
        // Explicit Intent → Camera app
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try { photoFile = createImageFile(); } catch (IOException e) { e.printStackTrace(); }
        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    private void openGallery() {
        // Implicit Intent → gallery picker
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Choisir une image"), REQUEST_GALLERY);
    }

    private File createImageFile() throws IOException {
        String stamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRENCH).format(new Date());
        String name  = "BULLETIN_" + stamp;
        File dir     = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img     = File.createTempFile(name, ".jpg", dir);
        currentPhotoPath = img.getAbsolutePath();
        return img;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CAMERA) {
            ivPreview.setImageURI(photoUri);
            cardPreview.setVisibility(View.VISIBLE);
        } else if (requestCode == REQUEST_GALLERY && data != null) {
            photoUri = data.getData();
            ivPreview.setImageURI(photoUri);
            cardPreview.setVisibility(View.VISIBLE);
            currentPhotoPath = photoUri != null ? photoUri.getPath() : "";
        }
    }

    private void saveDocument() {
        if (photoUri == null) {
            Toast.makeText(this, "Prenez ou choisissez une photo d'abord", Toast.LENGTH_SHORT).show();
            return;
        }
        String docName = etDocName.getText() != null ? etDocName.getText().toString().trim() : "";
        if (docName.isEmpty()) docName = "Document_" +
                new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH).format(new Date());

        Document doc = new Document();
        doc.setStudentId(PrefsManager.getInstance(this).getSelectedChildId());
        doc.setName(docName);
        doc.setType(Document.TYPE_BULLETIN);
        doc.setFilePath(currentPhotoPath);
        doc.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH).format(new Date()));
        doc.setFileSize(new File(currentPhotoPath != null ? currentPhotoPath : "").length());

        DatabaseHelper.getInstance(this).insertDocument(doc);
        Toast.makeText(this, "Document sauvegardé", Toast.LENGTH_SHORT).show();
        photoUri = null;
        cardPreview.setVisibility(View.GONE);
        etDocName.setText("");
        loadDocuments();
    }

    private void loadDocuments() {
        new Thread(() -> {
            int studentId = PrefsManager.getInstance(this).getSelectedChildId();
            documents = DatabaseHelper.getInstance(this).getDocumentsForStudent(studentId);
            runOnUiThread(() -> {
                adapter = new DocumentsAdapter(documents);
                rvDocuments.setAdapter(adapter);
            });
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int rc, String[] perms, int[] results) {
        super.onRequestPermissionsResult(rc, perms, results);
        if (rc == REQUEST_CAM_PERM && results.length > 0
                && results[0] == PackageManager.PERMISSION_GRANTED) openCamera();
    }
}
