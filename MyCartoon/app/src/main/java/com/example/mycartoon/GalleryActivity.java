package com.example.mycartoon;

import static android.net.Uri.withAppendedPath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.Manifest;
import java.util.ArrayList;
import java.util.List;
import com.example.mycartoon.MyImage;


public class GalleryActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 100;
    private static final int REQUEST_EDIT_IMAGE = 400;
    private static final int REQUEST_PICK_IMAGE = 200;
    private static final int REQUEST_CAPTURE_IMAGE = 300;

    private GridView mGridView;
    private GalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Kiểm tra quyền truy cập bộ nhớ
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            showImageList();
        }

        // Xử lý sự kiện click vào nút Thư viện
        Button btnGallery = findViewById(R.id.btn_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_IMAGE);
            }
        });

        // Xử lý sự kiện click vào nút Camera
        Button btnCamera = findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
            }
        });
    }

    private void showImageList() {
        // Lấy danh sách ảnh từ bộ nhớ
        List<MyImage> imageList = getImageList(this);

        // Hiển thị danh sách ảnh lên GridView
        mGridView = findViewById(R.id.grid_view);
        mAdapter = new GalleryAdapter(this, (ArrayList<MyImage>) imageList);
        mGridView.setAdapter(mAdapter);

        // Xử lý sự kiện click vào item trong GridView
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đường dẫn của ảnh khi người dùng click vào item
                String imagePath = mAdapter.getItemPath(position);

                // Chuyển đến màn hình chỉnh sửa ảnh
                Intent intent = new Intent(GalleryActivity.this, LaravaActivity.class);
                intent.putExtra(LaravaActivity.EXTRA_IMAGE_PATH, imagePath);
                startActivityForResult(intent, REQUEST_EDIT_IMAGE);
            }
        });
    }
    private ArrayList<MyImage> getImageList(Context context) {
        ArrayList<MyImage> imageList = new ArrayList<>();

        // Lấy Uri của thư mục ảnh
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Các trường cần lấy của ảnh
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};

        // Sắp xếp kết quả theo thời gian tạo
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        // Query danh sách ảnh
        Cursor cursor = context.getContentResolver().query(imageUri, projection, null, null, sortOrder);

        if (cursor != null && cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int nameColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int pathColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            do {
                // Lấy thông tin ảnh từ cursor
                long id = cursor.getLong(idColumnIndex);
                String displayName = cursor.getString(nameColumnIndex);
                String path = cursor.getString(pathColumnIndex);

                // Tạo đối tượng MyImage và thêm vào danh sách
                MyImage image = new MyImage(displayName, path, ContentUris.withAppendedId(imageUri, id));
                imageList.add(image);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return imageList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_IMAGE && resultCode == RESULT_OK) {
            // Xử lý kết quả trả về ở đây
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageList();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}