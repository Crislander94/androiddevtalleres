package com.example.registrar;

import android.app.DatePickerDialog;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_first_crud);
        Spinner mis_ciudades =  findViewById(R.id.ciudades);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ciudades, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mis_ciudades.setAdapter(adapter);
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.txtRegistrar);
        textView.setText(message);
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

        String Sql = "Select * from users where cedula = " + txtCedula.getText().toString();
        Cursor fila = db.rawQuery(Sql, null);
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
            db.close();
        }else {
            Toast.makeText(this , "El usuario que intentas encontrar no existe", Toast.LENGTH_LONG).show();
            db.close();
        }
    }
    public void Actualizar (View v){}
    public void Eliminar (View v){}
}