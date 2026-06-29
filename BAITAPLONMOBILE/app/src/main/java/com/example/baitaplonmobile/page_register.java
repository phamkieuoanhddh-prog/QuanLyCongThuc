package com.example.baitaplonmobile;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class page_register extends AppCompatActivity {

    page_database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_register);

        db = new page_database(this);
    }

    public void OnClick_NutRegister(View v) {
        EditText edtEmail, edtPassword, edtConfirmPassword;
        edtEmail = (EditText) findViewById(R.id.EditText_Email);
        edtPassword = (EditText) findViewById(R.id.EditText_Password);
        edtConfirmPassword = (EditText) findViewById(R.id.EditText_ConfirmPassword);

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.KiemTraEmailDaTonTai(email)) {
            Toast.makeText(this, "Email này đã được đăng ký!", Toast.LENGTH_SHORT).show();
            return;
        }

        db.ThemTaiKhoan(email, password);
        Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(page_register.this, page_login.class);
        startActivity(intent);
        finish();
    }

    public void OnClick_ChuyenLogin(View v) {
        Intent intent = new Intent(page_register.this, page_login.class);
        startActivity(intent);
        finish();
    }
}