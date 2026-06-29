package com.example.baitaplonmobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class page_list_congthuc extends AppCompatActivity {

    GridView GridView_DanhSachCongThuc;
    TextView TextView_RongDanhSach;
    EditText EditText_TimKiem;

    page_database db;
    ArrayList<Integer> dsMaCongThuc;
    ArrayList<String> dsTenCongThuc;
    ArrayList<String> dsDoKho;
    ArrayList<String> dsDuongDanAnh;
    ArrayList<Integer> dsYeuThich;

    Adapter_CongThuc adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_congthuc);

        db = new page_database(this);

        GridView_DanhSachCongThuc = (GridView) findViewById(R.id.GridView_DanhSachCongThuc);
        TextView_RongDanhSach = (TextView) findViewById(R.id.TextView_RongDanhSach);
        EditText_TimKiem = (EditText) findViewById(R.id.EditText_TimKiem);

        dsMaCongThuc = new ArrayList<Integer>();
        dsTenCongThuc = new ArrayList<String>();
        dsDoKho = new ArrayList<String>();
        dsDuongDanAnh = new ArrayList<String>();
        dsYeuThich = new ArrayList<Integer>();

        adapter = new Adapter_CongThuc();
        GridView_DanhSachCongThuc.setAdapter(adapter);

        LoadDanhSach("");

        EditText_TimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LoadDanhSach(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void LoadDanhSach(String tuKhoa) {
        dsMaCongThuc.clear();
        dsTenCongThuc.clear();
        dsDoKho.clear();
        dsDuongDanAnh.clear();
        dsYeuThich.clear();

        String sql = "select * from CongThuc where TenCongThuc like '%" + tuKhoa + "%'";
        Cursor cur = db.getCursor(sql);

        if (cur.moveToFirst()) {
            do {
                dsMaCongThuc.add(cur.getInt(0));
                dsTenCongThuc.add(cur.getString(1));
                dsDoKho.add(cur.getString(2));
                dsDuongDanAnh.add(cur.getString(5));
                dsYeuThich.add(cur.getInt(6));
            } while (cur.moveToNext());
        }
        cur.close();

        adapter.notifyDataSetChanged();

        if (dsMaCongThuc.size() == 0) {
            TextView_RongDanhSach.setVisibility(View.VISIBLE);
        } else {
            TextView_RongDanhSach.setVisibility(View.GONE);
        }
    }

    public void OnClick_DangXuat(View v) {
        Intent intent = new Intent(page_list_congthuc.this, page_login.class);
        startActivity(intent);
        finish();
    }

    public void OnClick_ThemCongThuc(View v) {
        Intent intent = new Intent(page_list_congthuc.this, page_manager_congthuc.class);
        startActivity(intent);
    }

    public void OnClick_XemYeuThich(View v) {
        Intent intent = new Intent(page_list_congthuc.this, page_list_yeuthich.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadDanhSach(EditText_TimKiem.getText().toString());
    }

    class Adapter_CongThuc extends BaseAdapter {

        @Override
        public int getCount() {
            return dsMaCongThuc.size();
        }

        @Override
        public Object getItem(int position) {
            return dsMaCongThuc.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.activity_item_congthuc, parent, false);
            }

            TextView txtTen = (TextView) view.findViewById(R.id.TextView_TenCongThuc);
            TextView txtDoKho = (TextView) view.findViewById(R.id.TextView_DoKho);
            ImageView imgAnh = (ImageView) view.findViewById(R.id.ImageView_AnhCongThuc);
            ImageView imgYeuThich = (ImageView) view.findViewById(R.id.ImageView_YeuThich);

            txtTen.setText(dsTenCongThuc.get(position));
            txtDoKho.setText(dsDoKho.get(position));

            String duongDan = dsDuongDanAnh.get(position);
            if (duongDan != null && !duongDan.isEmpty()) {
                imgAnh.setImageURI(Uri.parse(duongDan));
            } else {
                imgAnh.setImageResource(R.drawable.hinhnen1);
            }

            if (dsYeuThich.get(position) == 1) {
                imgYeuThich.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                imgYeuThich.setImageResource(android.R.drawable.btn_star_big_off);
            }

            final int maCongThuc = dsMaCongThuc.get(position);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(page_list_congthuc.this, page_manager_congthuc.class);
                    intent.putExtra("MaCongThuc", maCongThuc);
                    startActivity(intent);
                }
            });

            imgYeuThich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int trangThaiMoi = (dsYeuThich.get(position) == 1) ? 0 : 1;
                    db.DoiTrangThaiYeuThich(maCongThuc, trangThaiMoi);
                    LoadDanhSach(EditText_TimKiem.getText().toString());
                }
            });

            return view;
        }
    }
}