package com.example.baitaplonmobile;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class page_list_yeuthich extends AppCompatActivity {

    ListView ListView_DanhSachYeuThich;
    TextView TextView_RongYeuThich;
    EditText EditText_TimKiemYeuThich;

    page_database db;
    ArrayList<Integer> dsMaCongThuc;
    ArrayList<String> dsTenCongThuc;
    ArrayList<String> dsDoKho;
    ArrayList<String> dsDuongDanAnh;

    Adapter_YeuThich adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_yeuthich);

        db = new page_database(this);

        ListView_DanhSachYeuThich = (ListView) findViewById(R.id.ListView_DanhSachYeuThich);
        TextView_RongYeuThich = (TextView) findViewById(R.id.TextView_RongYeuThich);
        EditText_TimKiemYeuThich = (EditText) findViewById(R.id.EditText_TimKiemYeuThich);

        dsMaCongThuc = new ArrayList<Integer>();
        dsTenCongThuc = new ArrayList<String>();
        dsDoKho = new ArrayList<String>();
        dsDuongDanAnh = new ArrayList<String>();

        adapter = new Adapter_YeuThich();
        ListView_DanhSachYeuThich.setAdapter(adapter);

        LoadDanhSach("");

        EditText_TimKiemYeuThich.addTextChangedListener(new TextWatcher() {
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

        String sql = "select * from CongThuc where YeuThich = 1 and TenCongThuc like '%" + tuKhoa + "%'";
        Cursor cur = db.getCursor(sql);

        if (cur.moveToFirst()) {
            do {
                dsMaCongThuc.add(cur.getInt(0));
                dsTenCongThuc.add(cur.getString(1));
                dsDoKho.add(cur.getString(2));
                dsDuongDanAnh.add(cur.getString(5));
            } while (cur.moveToNext());
        }
        cur.close();

        adapter.notifyDataSetChanged();

        if (dsMaCongThuc.size() == 0) {
            TextView_RongYeuThich.setVisibility(View.VISIBLE);
        } else {
            TextView_RongYeuThich.setVisibility(View.GONE);
        }
    }

    public void OnClick_QuayLaiYeuThich(View v) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadDanhSach(EditText_TimKiemYeuThich.getText().toString());
    }

    class Adapter_YeuThich extends BaseAdapter {

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
            imgYeuThich.setImageResource(android.R.drawable.btn_star_big_on);

            String duongDan = dsDuongDanAnh.get(position);
            if (duongDan != null && !duongDan.isEmpty()) {
                imgAnh.setImageURI(Uri.parse(duongDan));
            } else {
                imgAnh.setImageResource(R.drawable.hinhnen1);
            }

            final int maCongThuc = dsMaCongThuc.get(position);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(page_list_yeuthich.this, page_manager_congthuc.class);
                    intent.putExtra("MaCongThuc", maCongThuc);
                    startActivity(intent);
                }
            });

            imgYeuThich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.DoiTrangThaiYeuThich(maCongThuc, 0);
                    LoadDanhSach(EditText_TimKiemYeuThich.getText().toString());
                }
            });

            return view;
        }
    }
}