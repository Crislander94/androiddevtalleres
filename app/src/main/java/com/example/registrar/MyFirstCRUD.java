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
    EditText txtusername,txtpassword, txtmail, txtPhone, txtCedula;
    RadioButton rdF, rdM;
    Calendar calendario;
    EditText date1;
    Spinner ciudades = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_first_crud);
        ciudades =  findViewById(R.id.xciudades);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ciudades, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        ciudades.setAdapter(adapter);
        // Capture the layout's TextView and set the string as its text
        txtusername= findViewById(R.id.xedit_usuario);
        txtpassword=   findViewById(R.id.xnuevapassword);
        txtmail=  findViewById(R.id.xedit_correo);
        txtPhone=  findViewById(R.id.xeditPhone);
        rdF =  findViewById(R.id.xrdFemenino);
        rdM =   findViewById(R.id.xrdMasculino);
        txtCedula = findViewById(R.id.xcedulaUser);
        date1 = findViewById(R.id.xdateNac);
        calendario = Calendar.getInstance();
        final int year = calendario.get(Calendar.YEAR);
        final int month = calendario.get(Calendar.MONTH);
        final int day = calendario.get(Calendar.DAY_OF_MONTH);
        //Obtener la fecha del datePicker Seleccionada
        date1.setOnClickListener(v ->{
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MyFirstCRUD.this, (view, year1, month1, dayOfMonth) -> {
                month1 = month1 +1;
                String date_nacimiento = dayOfMonth+"/"+ month1 +"/"+ year1;
                date1.setText(date_nacimiento);
            },year, month, day);
            datePickerDialog.show();
        });
    }
    public void Buscar (View view){
        AdminSqliteOpenHelper usuarios = new AdminSqliteOpenHelper(this, "prueba_user", null, 1);
        SQLiteDatabase db = usuarios.getWritableDatabase();
        String valueConsult = txtCedula.getText().toString();
        if(!valueConsult.isEmpty()){
            Cursor fila = db.rawQuery("select * from users where cedula = '"+valueConsult+"'", null);
            if(fila.moveToFirst()){
                int id = fila.getInt(0);
                String username = fila.getString(1);
                String password= fila.getString(2);
                String email= fila.getString(3);
                String phone= fila.getString(4);
                String fecha_nacimiento  = fila.getString(5);
                String ciudad = fila.getString(6);
                String sexo = fila.getString(7);
                txtusername.setText(username);
                txtpassword.setText(password);
                txtmail.setText(email);
                txtPhone.setText(phone);
                date1.setText(fecha_nacimiento);
                if(sexo.equals("Femenino")){
                    rdF.setChecked(true);
                }else{
                    rdM.setChecked(true);
                }
            }else {
                Toast.makeText(this , "El usuario que intentas encontrar no existe "+valueConsult, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this , "El campo cedula está vacío", Toast.LENGTH_LONG).show();
        }
        db.close();
    }
    public void Actualizar (View v){
        String value_username= txtusername.getText().toString();
        String value_password = txtpassword.getText().toString();
        String value_mail= txtmail.getText().toString();
        String sexo = "";
        String ciudad_selected = ciudades.getSelectedItem().toString();
        String value_phone= txtPhone.getText().toString();
        String fecha_nacimiento  = date1.getText().toString();
        String cedula = txtCedula.getText().toString();
        AdminSqliteOpenHelper usuarios = new AdminSqliteOpenHelper(this, "prueba_user", null, 1);
        SQLiteDatabase db = usuarios.getWritableDatabase();
        if(!cedula.isEmpty()){
            ContentValues data_user = new ContentValues();
            data_user.put("username", value_username );
            data_user.put("password", value_password );
            data_user.put("email", value_mail );
            data_user.put("telefono", value_phone );
            data_user.put("fecha_nac", fecha_nacimiento );
            data_user.put("ciudad", ciudad_selected );
            data_user.put("sexo", sexo);
            // Insertamos objeto en la tabla con los datos
            int cantidad = db.update("users",data_user ,"cedula = '"+cedula+"'", null);
            // Cerramos la base de datos
            if(cantidad == 1){
                Toast.makeText(this, "Usuario modificado con exito", Toast.LENGTH_SHORT).show();
                txtusername.setText("");
                txtpassword.setText("");
                txtmail.setText("");
                txtPhone.setText("");
                date1.setText("");
                db.close();
            }else{
                Toast.makeText(this, "No se pudo modificar al usuario", Toast.LENGTH_SHORT).show();
                db.close();
            }
        }else{
            Toast.makeText(this , "El campo cedula está vacío", Toast.LENGTH_LONG).show();
            db.close();
        }
    }
    public void Eliminar (View v){
        String cedula = txtCedula.getText().toString();
        AdminSqliteOpenHelper usuarios = new AdminSqliteOpenHelper(this, "prueba_user", null, 1);
        SQLiteDatabase db = usuarios.getWritableDatabase();
        if(!cedula.isEmpty()){
            // Insertamos objeto en la tabla con los datos
            int cantidad = db.delete("users" ,"cedula = '"+cedula+"'", null);
            // Cerramos la base de datos
            if(cantidad == 1){
                txtusername.setText("");
                txtpassword.setText("");
                txtmail.setText("");
                txtPhone.setText("");
                date1.setText("");
                Toast.makeText(this, "Usuario eliminado con exito", Toast.LENGTH_SHORT).show();
                db.close();
            }else{
                Toast.makeText(this, "El usuario buscado no se encuentra", Toast.LENGTH_SHORT).show();
                db.close();
            }
        }else{
            Toast.makeText(this , "El campo cedula está vacío", Toast.LENGTH_LONG).show();
            db.close();
        }

        db.close();
    }
    public void Retroceder(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}