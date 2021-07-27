package com.example.registrar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    EditText TXT_USR, TXT_PASS;
    Button BTN_ING;
    Button BTN_NuevoRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TXT_USR =findViewById(R.id.editTextTextPersonName);
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        TXT_USR.setText(preferences.getString("usuario",""));

        TXT_PASS =findViewById(R.id.editTextTextPassword);
        SharedPreferences preferences_1 = getSharedPreferences("datos1", Context.MODE_PRIVATE);
        TXT_PASS.setText(preferences.getString("password",""));
        BTN_NuevoRegistro = findViewById(R.id.btn_nuevo_usuario);
        BTN_ING = findViewById(R.id.button);
        BTN_ING.setOnClickListener(v -> {
            String V_USR = TXT_USR.getText().toString();
            String V_PASS = TXT_PASS.getText().toString();
            if (V_USR.equals("admin") && V_PASS.equals("admin")){
                Toast.makeText(getApplicationContext(),"Datos correcto",Toast.LENGTH_SHORT).show();
                SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor = preferencias.edit();
                Obj_editor.putString("usuario",TXT_USR.getText().toString());
                Obj_editor.commit();

                SharedPreferences preferencias1 = getSharedPreferences("datos1", Context.MODE_PRIVATE);
                SharedPreferences.Editor Obj_editor1 = preferencias1.edit();
                Obj_editor1.putString("password",TXT_PASS.getText().toString());
                Obj_editor1.commit();
            }else {
                Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();
            }
        });
    }
     public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this,  MainActivity.class);
        startActivity(intent);
    }
    //Create Menu
    public boolean onCreateOptionsMenu(Menu mimenu){

        getMenuInflater().inflate(R.menu.menu,mimenu);
        return true;
    }
    //Evento click cuando un item del menu es seleccionado
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.acercade:
                Intent intent = new Intent(this, AcercadeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}