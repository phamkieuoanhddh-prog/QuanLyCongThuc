package com.example.baitaplonmobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class page_manager_congthuc extends AppCompatActivity {

    TextView TextView_TieuDeForm;
    EditText EditText_TenCongThuc, EditText_NguyenLieu, EditText_CachLam;
    RadioGroup RadioGroup_DoKho;
    ImageView ImageView_AnhCongThuc;
    Button Button_XoaCongThuc;

    page_database db;
    int maCongThuc = -1;
    String duongDanAnh = "";

    ActivityResultLauncher<Intent> launcherChonAnh = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        duongDanAnh = uri.toString();
                        ImageView_AnhCongThuc.setImageURI(uri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_congthuc);

        db = new page_database(this);

        TextView_TieuDeForm = (TextView) findViewById(R.id.TextView_TieuDeForm);
        EditText_TenCongThuc = (EditText) findViewById(R.id.EditText_TenCongThuc);
        EditText_NguyenLieu = (EditText) findViewById(R.id.EditText_NguyenLieu);
        EditText_CachLam = (EditText) findViewById(R.id.EditText_CachLam);
        RadioGroup_DoKho = (RadioGroup) findViewById(R.id.RadioGroup_DoKho);
        ImageView_AnhCongThuc = (ImageView) findViewById(R.id.ImageView_AnhCongThuc);
        Button_XoaCongThuc = (Button) findViewById(R.id.Button_XoaCongThuc);

        maCongThuc = getIntent().getIntExtra("MaCongThuc", -1);

        if (maCongThuc != -1) {
            TextView_TieuDeForm.setText("Sửa Công Thức");
            Button_XoaCongThuc.setVisibility(View.VISIBLE);
            LoadDuLieuCongThuc();
        }
    }

    public void LoadDuLieuCongThuc() {
        String sql = "select * from CongThuc where MaCongThuc = " + maCongThuc;
        Cursor cur = db.getCursor(sql);
        if (cur.moveToFirst()) {
            EditText_TenCongThuc.setText(cur.getString(1));
            String doKho = cur.getString(2);
            EditText_NguyenLieu.setText(cur.getString(3));
            EditText_CachLam.setText(cur.getString(4));
            duongDanAnh = cur.getString(5) != null ? cur.getString(5) : "";

            if (doKho.equals("Trung bình")) {
                RadioGroup_DoKho.check(R.id.RadioButton_TrungBinh);
            } else if (doKho.equals("Khó")) {
                RadioGroup_DoKho.check(R.id.RadioButton_Kho);
            } else {
                RadioGroup_DoKho.check(R.id.RadioButton_De);
            }

            if (!duongDanAnh.isEmpty()) {
                ImageView_AnhCongThuc.setImageURI(Uri.parse(duongDanAnh));
            }
        }
        cur.close();
    }

    public void OnClick_ChonAnh(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcherChonAnh.launch(intent);
    }

    public void OnClick_LuuCongThuc(View v) {
        String ten = EditText_TenCongThuc.getText().toString().trim();
        String nguyenLieu = EditText_NguyenLieu.getText().toString().trim();
        String cachLam = EditText_CachLam.getText().toString().trim();

        if (ten.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên công thức!", Toast.LENGTH_SHORT).show();
            return;
        }

        int idDoKho = RadioGroup_DoKho.getCheckedRadioButtonId();
        String doKho = "Dễ";
        if (idDoKho == R.id.RadioButton_TrungBinh) {
            doKho = "Trung bình";
        } else if (idDoKho == R.id.RadioButton_Kho) {
            doKho = "Khó";
        }

        if (maCongThuc == -1) {
            String sql = "insert into CongThuc(TenCongThuc, DoKho, NguyenLieu, CachLam, DuongDanAnh, YeuThich) values('"
                    + ten + "','" + doKho + "','" + nguyenLieu + "','" + cachLam + "','" + duongDanAnh + "',0)";
            db.ExecuteSQL(sql);
            Toast.makeText(this, "Đã thêm công thức!", Toast.LENGTH_SHORT).show();
        } else {
            String sql = "update CongThuc set TenCongThuc='" + ten + "', DoKho='" + doKho
                    + "', NguyenLieu='" + nguyenLieu + "', CachLam='" + cachLam
                    + "', DuongDanAnh='" + duongDanAnh + "' where MaCongThuc=" + maCongThuc;
            db.ExecuteSQL(sql);
            Toast.makeText(this, "Đã cập nhật công thức!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    public void OnClick_XoaCongThuc(View v) {
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Xác nhận xóa");
        al.setMessage("Bạn có chắc muốn xóa công thức này không?");
        al.setPositiveButton("Xóa", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                db.XoaCongThuc(maCongThuc);
                Toast.makeText(page_manager_congthuc.this, "Đã xóa công thức!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        al.setNegativeButton("Hủy", null);
        al.create().show();
    }

    public void OnClick_QuayLai(View v) {
        finish();
    }
}