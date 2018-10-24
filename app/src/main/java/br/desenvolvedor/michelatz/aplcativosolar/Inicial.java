package br.desenvolvedor.michelatz.aplcativosolar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Inicial extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SQLiteDatabase db;
    ObtainGPS gps;
    String BANCO = "banco.db";
    String TABELAPROPOSTA = "proposta", TABELACONSUMOMES = "consumomes", TABELAVALORES = "valores", TABELAVALORESINICIAIS = "valoresiniciais";

    public static final String MyPREFERENCES = "MinhasPreferencias";
    public static final String idPropostaPref = "idPropostaKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("HCC SOLAR");
        toolbar.setSubtitle("Início");
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
/*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Inicial.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 221);

            return;
        }
 */
        gps = new ObtainGPS(Inicial.this);

        if (gps.canGetLocation()) {

        } else {
            gps.showSettingsAlert();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Inicial.this);

        alertDialog.setTitle("GPS");
        alertDialog.setMessage("GPS não está habilitado. Você deseja configura-lo?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                Inicial.this.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        alertDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_1) {
            criarPropostaBanco();
            Intent it = new Intent(this, Proposta1.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_2) {
            Intent it = new Intent(this, ListaPropostas.class);
            startActivity(it);
            finish();

        } /* else if (id == R.id.nav_3) {

        } else if (id == R.id.nav_4) {

        } */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void novaProposta(View v) {
        criarPropostaBanco();
        Intent it = new Intent(this, Proposta1.class);
        startActivity(it);
        finish();
    }

    public void criarPropostaBanco() {
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("DATA", "");
        values.put("PROPOSTA", "");
        values.put("CLIENTE", "");
        values.put("CIDADE", "");
        values.put("DESLOCAMENTO", "");
        values.put("MEDICAO", "");
        values.put("MACROREGIAO", "");
        values.put("MICROREGIAO", "");
        values.put("KWH", "");
        values.put("VALOR", "");
        values.put("REPRESENTANTE", "");
        values.put("COMISSAO", "");
        values.put("DESCONTO", "");
        values.put("VERIFICACAO", "");
        values.put("FRETE", "");
        values.put("SOMBREAMENTO", "");

        long ultimoId = db.insert(TABELAPROPOSTA, null, values);
        String retorno = String.valueOf(ultimoId);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(idPropostaPref, retorno);
        editor.commit();
        db.close();
    }

    public void ListarProposta(View v) {
        criarPropostaBanco();
        Intent it = new Intent(this, ListaPropostas.class);
        startActivity(it);
        finish();
    }

}
