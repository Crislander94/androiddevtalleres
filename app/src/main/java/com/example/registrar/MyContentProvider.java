package com.example.registrar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyContentProvider extends AppCompatActivity {
    private static final int CODIGO_SOLICITUD_PERMISO = 1;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_content_provider);

        activity = this;
    }
    public void getContacts(View v) {
        if (chequearStatusPermiso()) {
            getUriContacts();
        } else {
            solicitarPermiso();
        }
    }
    public void solicitarPermiso(){
        boolean solicitarPermisoContactos = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS);
        if(solicitarPermisoContactos){
            Toast.makeText(MyContentProvider.this, "Los permisos fueron otorgados", Toast.LENGTH_SHORT).show();
        }else{
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, CODIGO_SOLICITUD_PERMISO);
        }
    }
    public boolean chequearStatusPermiso(){
        boolean permisoContactos = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        return permisoContactos;
    }
    /**
     * Función que me permite Obtener los Conactos de mi telefono
     */
    public void getUriContacts(){
        TextView txtRenderContactos = findViewById(R.id.txtRenderContactos);
        String[] dataContactsConf = new String[]{ContactsContract.Data._ID,ContactsContract.Data.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.TYPE};
        String selectionClause =
                ContactsContract.Data.MIMETYPE+"='"+ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                +"' AND "+ContactsContract.CommonDataKinds.Phone.NUMBER + " IS NOT NULL";
        String sortOrder = ContactsContract.Data.DISPLAY_NAME +" ASC";
        Toast.makeText(this, "Se cargaron los contactos", Toast.LENGTH_SHORT).show();
        Cursor c = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                dataContactsConf,
                selectionClause,
                null,
                sortOrder);

        txtRenderContactos.setText("");

        while(c.moveToNext()){
            txtRenderContactos.append("Identificador : "+c.getString(0)+"\nNombre: "+c.getString(1)+"\nNumero: "+c.getString(2)+"\nTipo: "+c.getString(3)+"\n\n");
        }

        c.close();
    }

    public void Retroceder(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}