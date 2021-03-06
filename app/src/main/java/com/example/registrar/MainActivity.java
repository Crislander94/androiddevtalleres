package com.example.registrar;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

public class MainActivity extends AppCompatActivity {
    EditText TXT_US,TXT_PASS, TXT_EMAIL, TXT_PHONE, txtCedula;
    Button BTN_REGIS;
    RadioButton RDFemenino, RDMasculino;
    Calendar calendar;
    EditText date;


    //Variables notifiaciones
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    MainActivity.this, (view, year1, month1, dayOfMonth) -> {
                        month1 = month1 +1;
                        String date_nacimiento = dayOfMonth+"/"+ month1 +"/"+ year1;
                        date.setText(date_nacimiento);
                    },year, month, day);
            datePickerDialog.show();
        });
        BTN_REGIS.setOnClickListener(v -> {
            String V_USR= TXT_US.getText().toString();
            String V_PASS= TXT_PASS.getText().toString();
            String V_EMAIL= TXT_EMAIL.getText().toString();
            String sexo = "";
            String ciudad_selected = mis_ciudades.getSelectedItem().toString();
            String V_PHONE= TXT_PHONE.getText().toString();
            String fecha_nacimiento  = date.getText().toString();
            String cedula = txtCedula.getText().toString();

            if(RDFemenino.isChecked()){
                sexo = "Femenino";
            }else if(RDMasculino.isChecked()) {
                sexo = "Masculino";
            }
            if (V_USR.equals("")){
                Toast.makeText(getApplicationContext(),"falta usuario",Toast.LENGTH_SHORT).show();
            }else if (cedula.equals("")) {
                Toast.makeText(getApplicationContext(), "Falta Cedula", Toast.LENGTH_SHORT).show();
            }else if (V_PASS.equals("")){
                Toast.makeText(getApplicationContext(),"Falta contrase??a",Toast.LENGTH_SHORT).show();
            }else if (V_EMAIL.equals("")){
                Toast.makeText(getApplicationContext(),"Falta correo",Toast.LENGTH_SHORT).show();
            }else if (V_PHONE.equals("")){
                Toast.makeText(getApplicationContext(),"Falta correo",Toast.LENGTH_SHORT).show();
            }else if(fecha_nacimiento.equals("")){
                Toast.makeText(getApplicationContext(),"Seleccione su fecha de nacimiento",Toast.LENGTH_SHORT).show();
            }else if(sexo.equals("")){
                Toast.makeText(getApplicationContext(),"seleccione sexo",Toast.LENGTH_SHORT).show();
            }
            else {
                // Creacion de la conexion con la base de datos
                AdminSqliteOpenHelper usuarios = new AdminSqliteOpenHelper(this, "prueba_user", null, 1);
                // Poner db en modo lectura y escritura
                SQLiteDatabase db = usuarios.getWritableDatabase();
                //Objeto para guardar data que ser?? almacenada en la DB
                ContentValues data_user = new ContentValues();
                data_user.put("username", V_USR );
                data_user.put("password", V_PASS );
                data_user.put("email", V_EMAIL );
                data_user.put("telefono", V_PHONE );
                data_user.put("fecha_nac", fecha_nacimiento );
                data_user.put("ciudad", ciudad_selected );
                data_user.put("sexo", sexo);
                data_user.put("cedula", cedula );
                // Insertamos objeto en la tabla con los datos
                db.insert("users", null, data_user);
                // Cerramos la base de datos
                db.close();
                //Mostramos mensaje de que esta bien OK y mandamos al login Activity
                Intent i = new Intent(this, LoginActivity.class);
                Toast.makeText(getApplicationContext(),"Registro en la base de datos Exitoso "+ "Usuario:"+V_USR+"\nContrase??a:"+V_PASS+"\nEmail: "+V_EMAIL+"\nTelefono:"+V_PHONE+"\nCiudad:"+ciudad_selected+"\nFecha Nacimiento:"+fecha_nacimiento+"\nSexo: "+sexo,Toast.LENGTH_SHORT).show();
                //Se llama a la funci??n
                createNotification(V_USR);
                //Se crea la notifiaci??n del canal
                createNotificationChannel();
                startActivity(i);
            }
        });
    }
    public void LoginActivity(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    private void createNotificationChannel() {
        //Crear las notificaciones de canal, pero solo para API 26 + porque
        //Las clases de notificaciones de canal son nuevas y no los soporta la libreria
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificacion";
            String description = "Se ha a??adido un usuario";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            //Registar el canal con el sistema, no se puede cambiar la importancia
            // u otro comportamiento de la notifiaciones despu??s de esto.

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createNotification(String nombre){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.success_register);
        builder.setContentTitle("Registro Exitoso");
        builder.setContentText("Se ha registrado un nuevo usuario: "+ nombre);
        builder.setColor(Color.WHITE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Toast.makeText(this, "OnStart", Toast.LENGTH_SHORT).show();
//        // La actividad est?? a punto de hacerse visible.
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(this, "OnResume", Toast.LENGTH_SHORT).show();
//        // La actividad se ha vuelto visible (ahora se "reanuda").
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(this, "OnPause", Toast.LENGTH_SHORT).show();
//        // Enfocarse en otra actividad  (esta actividad est?? a punto de ser "detenida").
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(this, "OnStop", Toast.LENGTH_SHORT).show();
//        // La actividad ya no es visible (ahora est?? "detenida")
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(this, "OnDestroy", Toast.LENGTH_SHORT).show();
//        // La actividad est?? a punto de ser destruida.
//    }


}