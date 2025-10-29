package com.example.jalgo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.security.MessageDigest;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "jalgo.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context ctx){ super(ctx, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "user TEXT NOT NULL UNIQUE," +
                "pass TEXT NOT NULL)");
    }

    @Override public void onUpgrade(SQLiteDatabase db,int oldV,int newV){
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // Registrar
    public boolean register(String name, String user, String plainPass){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name.trim());
        cv.put("user", user.trim().toLowerCase());
        cv.put("pass", sha256(plainPass));
        long res = db.insert("users", null, cv);
        return res != -1;
    }

    // Login: retorna id o -1
    public long login(String user,String plainPass){
        SQLiteDatabase db = getReadableDatabase();
        String[] cols = {"id"};
        String[] args = { user.trim().toLowerCase(), sha256(plainPass) };
        Cursor c = db.query("users", cols, "user=? AND pass=?", args, null, null, null);
        long id = -1;
        if (c.moveToFirst()) id = c.getLong(0);
        c.close();
        return id;
    }

    // Obtener nombre por id
    public String getName(long id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM users WHERE id=?", new String[]{String.valueOf(id)});
        String name = null; if (c.moveToFirst()) name = c.getString(0);
        c.close(); return name;
    }

    private static String sha256(String s){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(s.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte x: b) sb.append(String.format("%02x", x));
            return sb.toString();
        }catch(Exception e){ return ""; }
    }
}
