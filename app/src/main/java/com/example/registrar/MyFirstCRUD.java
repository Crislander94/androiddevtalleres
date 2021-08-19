package com.example.registrar;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MyFirstCRUD extends AppCompatActivity {
    EditText TXT_US,TXT_PASS, TXT_EMAIL, TXT_PHONE, txtCedula;
    Button BTN_REGIS;
    RadioButton RDFemenino, RDMasculino;
    Calendar calendar;
    EditText date;
    Spinner mis_ciudades = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_first_crud);
        mis_ciudades =  findViewById(R.id.ciudades);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ciudades, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mis_ciudades.setAdapter(adapter);
        // Capture the layout's TextView and set the string as its text
        TXT_US= findViewById(R.id.edit_usuario);
        TXT_PASS=   findViewById(R.id.nuevapassword);
        TXT_EMAIL=  findViewById(R.id.edit_correo);
        TXT_PHONE=  findViewById(R.id.editPhone);
        BTN_REGIS=  findViewById(R.id.btn_registrar);
        RDFemenino =  findViewById(R.id.rdFemenino);
        RDMasculino =   findViewById(R.id.rdMasculino);
        txtCedula = findViewById(R.id.cedulaUser);
        date = findViewById(R.id.dateNac);
        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        //Obtener la fecha del datePicker Seleccionada
        date.setOnClickListener(v ->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MyFirstCRUD.this, (view, year1, month1, dayOfMonth) -> {
                month1 = month1 +1;
                String date_nacimiento = dayOfMonth+"/"+ month1 +"/"+ year1;
                date.setText(date_nacimiento);
            },year, month, day);
            datePickerDialog.show();
        });
    }


    public void Buscar (View view){
        AdminSqliteOpenHelper usuarios = new AdminSqliteOpenHelper(this, "prueba_user", null, 1);
        SQLiteDatabase db = usuarios.getWritableDatabase();
        TXT_US.setText(usuarios.toString());
        Toast.makeText(getApplicationContext(),usuarios.toString(),Toast.LENGTH_SHORT).show();
        TXT_EMAIL.setText("EJEMPLOOOOOOOO2");
        String valueConsult = txtCedula.getText().toString();
        if(!valueConsult.isEmpty()){
            Cursor fila = db.rawQuery("select * from users where cedula = "+valueConsult, null);
            if(fila.moveToFirst()){
                int id = fila.getInt(0);
                String username = fila.getString(1);
                String password= fila.getString(2);
                String email= fila.getString(3);
                String phone= fila.getString(4);
                String cedula = fila.getString(5);
                String fecha_nacimiento  = fila.getString(6);
                String ciudad = fila.getString(7);
                String sexo = fila.getString(8);
                TXT_US.setText(username);
            }else {
                Toast.makeText(this , "El usuario que intentas encontrar no existe", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this , "El campo cedula está vacío", Toast.LENGTH_LONG).show();
        }
        db.close();
    }
    public void Actualizar (View v){
        String V_USR= TXT_US.getText().toString();
        String V_PASS= TXT_PASS.getText().toString();
        String V_EMAIL= TXT_EMAIL.getText().toString();
        String sexo = "";
        String ciudad_selected = mis_ciudades.getSelectedItem().toString();
        String V_PHONE= TXT_PHONE.getText().toString();
        String fecha_nacimiento  = date.getText().toString();
        String cedula = txtCedula.getText().toString();
        AdminSqliteOpenHelper usuarios = new AdminSqliteOpenHelper(this, "prueba_user", null, 1);
        SQLiteDatabase db = usuarios.getWritableDatabase();
        if(!cedula.isEmpty()){
            ContentValues data_user = new ContentValues();
            data_user.put("username", V_USR );
            data_user.put("password", V_PASS );
            data_user.put("email", V_EMAIL );
            data_user.put("cedula", cedula );
            data_user.put("telefono", V_PHONE );
            data_user.put("fecha_nac", fecha_nacimiento );
            data_user.put("ciudad", ciudad_selected );
            data_user.put("sexo", sexo);
            // Insertamos objeto en la tabla con los datos
            int cantidad = db.update("users",data_user ,"cedula = "+cedula, null);
            // Cerramos la base de datos
            if(cantidad == 1){
                Toast.makeText(this, "Usuario modificado con exito", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No se pudo modificar al usuario", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this , "El campo cedula está vacío", Toast.LENGTH_LONG).show();
        }
    }
    public void Eliminar (View v){
        String cedula = txtCedula.getText().toString();
        AdminSqliteOpenHelper usuarios = new AdminSqliteOpenHelper(this, "prueba_user", null, 1);
        SQLiteDatabase db = usuarios.getWritableDatabase();
        if(!cedula.isEmpty()){
            // Insertamos objeto en la tabla con los datos
            int cantidad = db.delete("users" ,"cedula = "+cedula, null);
            // Cerramos la base de datos
            if(cantidad == 1){
                Toast.makeText(this, "Usuario eliminado con exito", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No se pudo eliminado al usuario", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this , "El campo cedula está vacío", Toast.LENGTH_LONG).show();
        }

        db.close();
    }
}