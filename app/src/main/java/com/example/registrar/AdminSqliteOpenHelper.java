package com.example.registrar;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

    public class AdminSqliteOpenHelper extends SQLiteOpenHelper {
    public AdminSqliteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "CREATE TABLE users(" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "username TEXT, " +
                                "password TEXT, " +
                                "email TEXT, " +
                                "telefono TEXT, " +
                                "cedula TEXT, " +
                                "fecha_nac Text, " +
                                "ciudad Text, " +
                                "sexo TEXT )";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
