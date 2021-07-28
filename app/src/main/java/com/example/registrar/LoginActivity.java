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
        TXT_PASS =findViewById(R.id.editTextTextPassword);

        cargarCredenciales();
        if(TXT_USR.getText().toString().equals("admin") && TXT_PASS.getText().toString().equals("admin")){
            Intent intent = new Intent(this,  AcercadeActivity.class);
            startActivity(intent);
        }
        BTN_NuevoRegistro = findViewById(R.id.btn_nuevo_usuario);
        BTN_ING = findViewById(R.id.button);
        BTN_ING.setOnClickListener(v -> {
            String V_USR = TXT_USR.getText().toString();
            String V_PASS = TXT_PASS.getText().toString();
            if (V_USR.equals("admin") && V_PASS.equals("admin")){
                Toast.makeText(getApplicationContext(),"Datos correcto",Toast.LENGTH_SHORT).show();
                guardarCredenciales();
            }else {
                Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void guardarCredenciales(){
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String user_name = TXT_USR.getText().toString();
        String password_user = TXT_PASS.getText().toString();

        SharedPreferences.Editor Obj_editor = preferences.edit();
        Obj_editor.putString("user",user_name);
        Obj_editor.putString("password",password_user);
        Obj_editor.commit();
    }
    public void cargarCredenciales(){
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        String user_name = preferences.getString("user", "");
        String password_user = preferences.getString("password", "");

        TXT_USR.setText(user_name);
        TXT_PASS.setText(password_user);
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