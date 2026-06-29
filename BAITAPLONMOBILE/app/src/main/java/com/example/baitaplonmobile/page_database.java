package com.example.baitaplonmobile;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class page_database extends SQLiteOpenHelper {

    private static final String dbName = "QuanLyCongThuc.sqlite";
    private static final int dbVersion = 1;

    public page_database(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TaiKhoan (MaTaiKhoan INTEGER PRIMARY KEY AUTOINCREMENT, Email TEXT, Password TEXT)");
        db.execSQL("CREATE TABLE CongThuc (MaCongThuc INTEGER PRIMARY KEY AUTOINCREMENT, TenCongThuc TEXT, DoKho TEXT, NguyenLieu TEXT, CachLam TEXT, DuongDanAnh TEXT, YeuThich INTEGER, MaTaiKhoan INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CongThuc");
        db.execSQL("DROP TABLE IF EXISTS TaiKhoan");
        onCreate(db);
    }

    public void ExecuteSQL(String sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
    }

    public Cursor getCursor(String sql) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public boolean KiemTraDangNhap(String email, String password) {
        String sql = "select * from TaiKhoan where Email = '" + email + "' and Password = '" + password + "'";
        Cursor cur = getCursor(sql);
        boolean ketQua = cur.moveToFirst();
        cur.close();
        return ketQua;
    }

    public boolean KiemTraEmailDaTonTai(String email) {
        String sql = "select * from TaiKhoan where Email = '" + email + "'";
        Cursor cur = getCursor(sql);
        boolean ketQua = cur.moveToFirst();
        cur.close();
        return ketQua;
    }

    public void ThemTaiKhoan(String email, String password) {
        String sql = "insert into TaiKhoan(Email, Password) values('" + email + "','" + password + "')";
        ExecuteSQL(sql);
    }

    public void DoiTrangThaiYeuThich(int maCongThuc, int trangThaiMoi) {
        String sql = "update CongThuc set YeuThich = " + trangThaiMoi + " where MaCongThuc = " + maCongThuc;
        ExecuteSQL(sql);
    }

    public void XoaCongThuc(int maCongThuc) {
        String sql = "delete from CongThuc where MaCongThuc = " + maCongThuc;
        ExecuteSQL(sql);
    }
}