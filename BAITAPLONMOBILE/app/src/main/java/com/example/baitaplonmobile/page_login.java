package com.example.baitaplonmobile;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class page_login extends AppCompatActivity {

    page_database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_login);

        db = new page_database(this);
    }

    public void OnClick_NutLogin(View v) {
        EditText edtEmail, edtPassword;
        edtEmail = (EditText) findViewById(R.id.EditText_Email);
        edtPassword = (EditText) findViewById(R.id.EditText_Password);

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (email.equals("") || password.equals("")) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.KiemTraDangNhap(email, password)) {
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(page_login.this, page_list_congthuc.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
        }
    }

    public void OnClick_ChuyenRegister(View v) {
        Intent intent = new Intent(page_login.this, page_register.class);
        startActivity(intent);
    }
}