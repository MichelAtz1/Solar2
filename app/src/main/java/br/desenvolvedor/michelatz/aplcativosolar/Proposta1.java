package br.desenvolvedor.michelatz.aplcativosolar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.maps.android.SphericalUtil;

//import com.google.android.gms.maps.model.LatLng;
//import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Proposta1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private Spinner spnTipoMedicao;
    //ArrayList<String> tipoMedicao = new ArrayList<String>();
    //ArrayAdapter adapter;

    public static final String idPropostaPref = "idPropostaKey";

    private RadioGroup grupMacro;
    private RadioButton radioMacroRS, radioMacroBA;
    private RadioButton radioMicroRegiao1RS, radioMicroRegiao2RS, radioMicroRegiao3RS;//radioMicroRegiao4RS, radioMicroRegiao5RS, radioMicroRegiao6RS
    private RadioButton radioMicroRegiao1BA, radioMicroRegiao2BA, radioMicroRegiao3BA, radioMicroRegiao4BA;
    private TextView txtData, txtMicroReg;
    private EditText edtNumProposta, edtNomeCliente, edtCidade, edtDeslocamento, edtDesFrete;
    private LinearLayout tableRS, tableBA;
    private ImageButton btmapRS, btmapBA;
    private String macroRegiao = "", microRegiao = "";
    private String idProposta;

    SQLiteDatabase db;
    String BANCO = "banco.db";
    String TABELAPROPOSTA = "proposta";

    private Address endereco;
    ObtainGPS gps;
    //GPSTracker gpsTracker;
    String cidadeFoto = "inicial";
    String estadoFoto = "inicial";
    String distanciaKM = "inicial";
    String longitude = "inicial";
    String latitude = "inicial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposta1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("HCC SOLAR");
        toolbar.setSubtitle("Proposta - Passo 1");
        setSupportActionBar(toolbar);

        SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
        idProposta = sharedpreferences.getString("idPropostaKey", null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*
        tipoMedicao.add("Selecione a Medição");
        tipoMedicao.add("Monofásica");
        tipoMedicao.add("Bifásica");
        tipoMedicao.add("Trifásica");
        */

        edtNumProposta = (EditText) findViewById(R.id.edtNumProposta);
        edtNomeCliente = (EditText) findViewById(R.id.edtNomeCliente);
        edtCidade = (EditText) findViewById(R.id.edtCidade);
        edtDeslocamento = (EditText) findViewById(R.id.edtDes);
        edtDesFrete = (EditText) findViewById(R.id.edtDesFrete);

        radioMacroRS = (RadioButton) findViewById(R.id.radioMacroRS);
        radioMacroBA = (RadioButton) findViewById(R.id.radioMacroBA);

        radioMicroRegiao1RS = (RadioButton) findViewById(R.id.radioMicroRegiao1RS);
        radioMicroRegiao2RS = (RadioButton) findViewById(R.id.radioMicroRegiao2RS);
        radioMicroRegiao3RS = (RadioButton) findViewById(R.id.radioMicroRegiao3RS);
        //radioMicroRegiao4RS = (RadioButton) findViewById(R.id.radioMicroRegiao4RS);
        //radioMicroRegiao5RS = (RadioButton) findViewById(R.id.radioMicroRegiao5RS);
        //radioMicroRegiao6RS = (RadioButton) findViewById(R.id.radioMicroRegiao6RS);

        radioMicroRegiao1BA = (RadioButton) findViewById(R.id.radioMicroRegiao1BA);
        radioMicroRegiao2BA = (RadioButton) findViewById(R.id.radioMicroRegiao2BA);
        radioMicroRegiao3BA = (RadioButton) findViewById(R.id.radioMicroRegiao3BA);
        radioMicroRegiao4BA = (RadioButton) findViewById(R.id.radioMicroRegiao4BA);

        tableBA = (LinearLayout) findViewById(R.id.tableBA);
        tableRS = (LinearLayout) findViewById(R.id.tableRS);
        btmapRS = (ImageButton) findViewById(R.id.btmapRS);
        btmapBA = (ImageButton) findViewById(R.id.btmapBA);
        txtData = (TextView) findViewById(R.id.txtData);
        txtMicroReg = (TextView) findViewById(R.id.txtMicroReg);

        grupMacro = (RadioGroup) findViewById(R.id.grupMacro);
        /*
        spnTipoMedicao = (Spinner) findViewById(R.id.spnTipoMedicao);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoMedicao);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoMedicao.setAdapter(adapter);
        */
        geraData();
        escondeCombos();
        preencheDados();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove("idPropostaKey");
            editor.commit();
            editor.clear();

            Intent it = new Intent(this, Inicial.class);
            startActivity(it);
            finish();
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_1) {
            SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove("idPropostaKey");
            editor.commit();
            editor.clear();

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
            values.put("FRETE", "");
            values.put("SOMBREAMENTO", "");
            values.put("REPRESENTANTE", "");
            values.put("COMISSAO", "");
            values.put("DESCONTO", "");
            values.put("VERIFICACAO", "");

            long ultimoId = db.insert(TABELAPROPOSTA, null, values);
            String retorno = String.valueOf(ultimoId);
            SharedPreferences.Editor editor2 = sharedpreferences.edit();
            editor2.putString(idPropostaPref, retorno);
            editor2.commit();
            db.close();

            Intent it = new Intent(this, Proposta1.class);
            startActivity(it);
            finish();
        } else if (id == R.id.nav_2) {
            SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove("idPropostaKey");
            editor.commit();
            editor.clear();

            Intent it = new Intent(this, ListaPropostas.class);
            startActivity(it);
            finish();

        }/* else if (id == R.id.nav_3) {
            SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove("idPropostaKey");
            editor.commit();
            editor.clear();

        } else if (id == R.id.nav_4) {
            SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove("idPropostaKey");
            editor.commit();
            editor.clear();

        }*/ else if (id == R.id.nav_sair) {
            SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove("idPropostaKey");
            editor.commit();
            editor.clear();

            Intent it = new Intent(this, Inicial.class);
            startActivity(it);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void escondeCombos(){
        txtMicroReg.setVisibility(View.GONE);
        tableRS.setVisibility(View.GONE);
        tableBA.setVisibility(View.GONE);
        btmapRS.setVisibility(View.GONE);
        btmapBA.setVisibility(View.GONE);
    }

    private void geraData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        txtData.setText(data_completa);
    }

    public void clicouMacroRS(View v){
        txtMicroReg.setVisibility(View.VISIBLE);
        tableRS.setVisibility(View.VISIBLE);
        tableBA.setVisibility(View.GONE);
        btmapRS.setVisibility(View.VISIBLE);
        btmapBA.setVisibility(View.GONE);

        radioMicroRegiao1BA.setChecked(false);
        radioMicroRegiao2BA.setChecked(false);
        radioMicroRegiao3BA.setChecked(false);
        radioMicroRegiao4BA.setChecked(false);

        macroRegiao = "RS";
        microRegiao = "";
    }

    public void clicouMacroBA(View v){
        txtMicroReg.setVisibility(View.VISIBLE);
        tableRS.setVisibility(View.GONE);
        tableBA.setVisibility(View.VISIBLE);
        btmapRS.setVisibility(View.GONE);
        btmapBA.setVisibility(View.VISIBLE);

        radioMicroRegiao1RS.setChecked(false);
        radioMicroRegiao2RS.setChecked(false);
        radioMicroRegiao3RS.setChecked(false);
        //radioMicroRegiao4RS.setChecked(false);
        //radioMicroRegiao5RS.setChecked(false);
        //radioMicroRegiao6RS.setChecked(false);

        macroRegiao = "BA";
        microRegiao = "";
    }

    public void clicouMicroRegiao1RS(View v){
        microRegiao = "Região 1 RS";
        radioMicroRegiao2RS.setChecked(false);
        radioMicroRegiao3RS.setChecked(false);
        //radioMicroRegiao4RS.setChecked(false);
        //radioMicroRegiao5RS.setChecked(false);
        //radioMicroRegiao6RS.setChecked(false);

        radioMicroRegiao1BA.setChecked(false);
        radioMicroRegiao2BA.setChecked(false);
        radioMicroRegiao3BA.setChecked(false);
        radioMicroRegiao4BA.setChecked(false);
    }

    public void clicouMicroRegiao2RS(View v){
        microRegiao = "Região 2 RS";
        radioMicroRegiao1RS.setChecked(false);
        radioMicroRegiao3RS.setChecked(false);
        //radioMicroRegiao4RS.setChecked(false);
        //radioMicroRegiao5RS.setChecked(false);
        //radioMicroRegiao6RS.setChecked(false);

        radioMicroRegiao1BA.setChecked(false);
        radioMicroRegiao2BA.setChecked(false);
        radioMicroRegiao3BA.setChecked(false);
        radioMicroRegiao4BA.setChecked(false);
        //radioMicroRegiao5BA.setChecked(false);
    }

    public void clicouMicroRegiao3RS(View v){
        microRegiao = "Região 3 RS";
        radioMicroRegiao2RS.setChecked(false);
        radioMicroRegiao1RS.setChecked(false);
        //radioMicroRegiao4RS.setChecked(false);
        //radioMicroRegiao5RS.setChecked(false);
        //radioMicroRegiao6RS.setChecked(false);

        radioMicroRegiao1BA.setChecked(false);
        radioMicroRegiao2BA.setChecked(false);
        radioMicroRegiao3BA.setChecked(false);
        radioMicroRegiao4BA.setChecked(false);
        //radioMicroRegiao5BA.setChecked(false);
    }
/*
    public void clicouMicroRegiao4RS(View v){
        microRegiao = "Região 4 RS";
        radioMicroRegiao2RS.setChecked(false);
        radioMicroRegiao3RS.setChecked(false);
        radioMicroRegiao1RS.setChecked(false);
        radioMicroRegiao5RS.setChecked(false);
        radioMicroRegiao6RS.setChecked(false);

        radioMicroRegiao1BA.setChecked(false);
        radioMicroRegiao2BA.setChecked(false);
        radioMicroRegiao3BA.setChecked(false);
        radioMicroRegiao4BA.setChecked(false);
        //radioMicroRegiao5BA.setChecked(false);
    }

    public void clicouMicroRegiao5RS(View v){
        microRegiao = "Região 5 RS";
        radioMicroRegiao2RS.setChecked(false);
        radioMicroRegiao3RS.setChecked(false);
        radioMicroRegiao4RS.setChecked(false);
        radioMicroRegiao1RS.setChecked(false);
        radioMicroRegiao6RS.setChecked(false);

        radioMicroRegiao1BA.setChecked(false);
        radioMicroRegiao2BA.setChecked(false);
        radioMicroRegiao3BA.setChecked(false);
        radioMicroRegiao4BA.setChecked(false);
        //radioMicroRegiao5BA.setChecked(false);
    }

    public void clicouMicroRegiao6RS(View v){
        microRegiao = "Região 6 RS";
        radioMicroRegiao2RS.setChecked(false);
        radioMicroRegiao3RS.setChecked(false);
        radioMicroRegiao4RS.setChecked(false);
        radioMicroRegiao1RS.setChecked(false);
        radioMicroRegiao5RS.setChecked(false);

        radioMicroRegiao1BA.setChecked(false);
        radioMicroRegiao2BA.setChecked(false);
        radioMicroRegiao3BA.setChecked(false);
        radioMicroRegiao4BA.setChecked(false);
        //radioMicroRegiao5BA.setChecked(false);
    }
    */

    public void clicouMicroRegiao1BA(View v){
        microRegiao = "Região 1 BA";
        radioMicroRegiao1RS.setChecked(false);
        radioMicroRegiao2RS.setChecked(false);
        radioMicroRegiao3RS.setChecked(false);
        //radioMicroRegiao4RS.setChecked(false);
        //radioMicroRegiao5RS.setChecked(false);
        //radioMicroRegiao6RS.setChecked(false);

        radioMicroRegiao2BA.setChecked(false);
        radioMicroRegiao3BA.setChecked(false);
        radioMicroRegiao4BA.setChecked(false);
        //radioMicroRegiao5BA.setChecked(false);
    }

    public void clicouMicroRegiao2BA(View v){
        microRegiao = "Região 2 BA";
        radioMicroRegiao1RS.setChecked(false);
        radioMicroRegiao2RS.setChecked(false);
        radioMicroRegiao3RS.setChecked(false);
        //radioMicroRegiao4RS.setChecked(false);
        //radioMicroRegiao5RS.setChecked(false);
        //radioMicroRegiao6RS.setChecked(false);

        radioMicroRegiao1BA.setChecked(false);
        radioMicroRegiao3BA.setChecked(false);
        radioMicroRegiao4BA.setChecked(false);
        //radioMicroRegiao5BA.setChecked(false);
    }

    public void clicouMicroRegiao3BA(View v){
        microRegiao = "Região 3 BA";
        radioMicroRegiao1RS.setChecked(false);
        radioMicroRegiao2RS.setChecked(false);
        radioMicroRegiao3RS.setChecked(false);
        //radioMicroRegiao4RS.setChecked(false);
        //radioMicroRegiao5RS.setChecked(false);
        //radioMicroRegiao6RS.setChecked(false);

        radioMicroRegiao2BA.setChecked(false);
        radioMicroRegiao1BA.setChecked(false);
        radioMicroRegiao4BA.setChecked(false);
        //radioMicroRegiao5BA.setChecked(false);
    }

    public void clicouMicroRegiao4BA(View v){
        microRegiao = "Região 4 BA";
        radioMicroRegiao1RS.setChecked(false);
        radioMicroRegiao2RS.setChecked(false);
        radioMicroRegiao3RS.setChecked(false);
        //radioMicroRegiao4RS.setChecked(false);
        //radioMicroRegiao5RS.setChecked(false);
        //radioMicroRegiao6RS.setChecked(false);

        radioMicroRegiao2BA.setChecked(false);
        radioMicroRegiao3BA.setChecked(false);
        radioMicroRegiao1BA.setChecked(false);
        //radioMicroRegiao5BA.setChecked(false);
    }

    public void irNovaProposta2(View v) {
        String data = "";
        String numeroProposta = "";
        String nomeCliente = "";
        String cidade = "";
        String deslocamento = "";
        String frete = "";
/*
        String medicao = "";

        if (spnTipoMedicao.getSelectedItem().toString().equals("Selecione a Medição")) {
            medicao = "";
        } else {
            medicao = spnTipoMedicao.getSelectedItem().toString();
        }
*/
        data = txtData.getText().toString();
        numeroProposta = edtNumProposta.getText().toString();
        nomeCliente = edtNomeCliente.getText().toString();
        cidade = edtCidade.getText().toString();
        deslocamento = edtDeslocamento.getText().toString();
        frete = edtDesFrete.getText().toString();

        if (frete.equals("") || deslocamento.equals("") || numeroProposta.equals("") || nomeCliente.equals("") || cidade.equals("") || macroRegiao.equals("") || microRegiao.equals("")) {
            Toast.makeText(getApplicationContext(), "Todos os campos são obrigatórios!!!", Toast.LENGTH_SHORT).show();
        } else {

            String s = "Hello World";
            if(nomeCliente.indexOf("/") != -1){ //Se for diferente de -1 é pq existe o caracter.
                Toast.makeText(getApplicationContext(), "A tecla ' / ' não pode ser inserida no nome do cliente!!!", Toast.LENGTH_SHORT).show();
            }else{

                db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
                ContentValues values = new ContentValues();
                values.put("DATA", data);
                values.put("PROPOSTA", numeroProposta);
                values.put("CLIENTE", nomeCliente);
                values.put("CIDADE", cidade);
                values.put("DESLOCAMENTO", deslocamento);
                values.put("FRETE", frete);
                values.put("MACROREGIAO", macroRegiao);
                values.put("MICROREGIAO", microRegiao);
                db.update(TABELAPROPOSTA, values, "ID=" + idProposta, null);
                db.close();

                Intent it = new Intent(this, Proposta2.class);
                startActivity(it);
                finish();

            }
        }
    }

    public void mapRS(View v) {
        final Dialog dialog = new Dialog(Proposta1.this);
        dialog.setContentView(R.layout.mapa_rs);
        dialog.setTitle("Mapa RS");
        dialog.setCancelable(true);

        Button dialogButton = (Button) dialog.findViewById(R.id.btRS);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void mapBA(View v) {
        final Dialog dialog = new Dialog(Proposta1.this);
        dialog.setContentView(R.layout.mapa_ba);
        dialog.setTitle("Mapa BA");
        dialog.setCancelable(true);

        Button dialogButton = (Button) dialog.findViewById(R.id.btBA);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void preencheDados() {
        String numeroProposta = null;
        String nomeCliente = null;
        String cidade = null;
        String deslocamento = null;
        String frete = null;
        //String medicao = null;
        String macroRegiao2 = null;
        String microRegiao2 = null;

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        Cursor linhas = db.rawQuery("SELECT * FROM " + TABELAPROPOSTA + " WHERE ID = " + idProposta + ";", null);
        if (linhas.moveToFirst()) {
            do {
                numeroProposta = linhas.getString(2);
                nomeCliente = linhas.getString(3);
                cidade = linhas.getString(4);
                deslocamento = linhas.getString(5);
                //medicao = linhas.getString(6);
                frete = linhas.getString(15);
                macroRegiao2 = linhas.getString(7);
                microRegiao2 = linhas.getString(8);
            }
            while (linhas.moveToNext());
            linhas.close();
        }
        db.close();

        if (numeroProposta != null) {
            edtNumProposta.setText(numeroProposta);
        }

        if (nomeCliente != null) {
            edtNomeCliente.setText(nomeCliente);
        }

        if (cidade != null) {
            edtCidade.setText(cidade);
        }

        if (deslocamento != null) {
            edtDeslocamento.setText(deslocamento);
        }

        if (frete != null) {
            edtDesFrete.setText(frete);
        }

        if (macroRegiao2 != null) {
            if (macroRegiao2.equals("RS")) {
                txtMicroReg.setVisibility(View.VISIBLE);
                tableRS.setVisibility(View.VISIBLE);
                tableBA.setVisibility(View.GONE);
                radioMacroRS.setChecked(true);
                radioMacroBA.setChecked(false);
                btmapRS.setVisibility(View.VISIBLE);
                btmapBA.setVisibility(View.GONE);
                macroRegiao = "RS";

                if (microRegiao2 != null) {
                    if (microRegiao2.equals("Região 1 RS")) {
                        microRegiao = "Região 1 RS";
                        radioMicroRegiao1RS.setChecked(true);
                        radioMicroRegiao2RS.setChecked(false);
                        radioMicroRegiao3RS.setChecked(false);
                        //radioMicroRegiao4RS.setChecked(false);
                        //radioMicroRegiao5RS.setChecked(false);
                        //radioMicroRegiao6RS.setChecked(false);

                    }else if(microRegiao2.equals("Região 2 RS")){
                        microRegiao = "Região 2 RS";
                        radioMicroRegiao1RS.setChecked(false);
                        radioMicroRegiao2RS.setChecked(true);
                        radioMicroRegiao3RS.setChecked(false);
                        //radioMicroRegiao4RS.setChecked(false);
                        //radioMicroRegiao5RS.setChecked(false);
                        //radioMicroRegiao6RS.setChecked(false);

                    } else if(microRegiao2.equals("Região 3 RS")){
                        microRegiao = "Região 3 RS";
                        radioMicroRegiao1RS.setChecked(false);
                        radioMicroRegiao2RS.setChecked(false);
                        radioMicroRegiao3RS.setChecked(true);
                        //radioMicroRegiao4RS.setChecked(false);
                        //radioMicroRegiao5RS.setChecked(false);
                        //radioMicroRegiao6RS.setChecked(false);

                    }
                    /*
                    else if(microRegiao2.equals("Região 4 RS")){
                        microRegiao = "Região 4 RS";
                        radioMicroRegiao1RS.setChecked(false);
                        radioMicroRegiao2RS.setChecked(false);
                        radioMicroRegiao3RS.setChecked(false);
                        radioMicroRegiao4RS.setChecked(true);
                        radioMicroRegiao5RS.setChecked(false);
                        radioMicroRegiao6RS.setChecked(false);

                    } else if(microRegiao2.equals("Região 5 RS")){
                        microRegiao = "Região 5 RS";
                        radioMicroRegiao1RS.setChecked(false);
                        radioMicroRegiao2RS.setChecked(false);
                        radioMicroRegiao3RS.setChecked(false);
                        radioMicroRegiao4RS.setChecked(false);
                        radioMicroRegiao5RS.setChecked(true);
                        radioMicroRegiao6RS.setChecked(false);

                    } else if(microRegiao2.equals("Região 6 RS")){
                        microRegiao = "Região 6 RS";
                        radioMicroRegiao1RS.setChecked(false);
                        radioMicroRegiao2RS.setChecked(false);
                        radioMicroRegiao3RS.setChecked(false);
                        radioMicroRegiao4RS.setChecked(false);
                        radioMicroRegiao5RS.setChecked(false);
                        radioMicroRegiao6RS.setChecked(true);

                    }
                    */
                }

            } else if (macroRegiao2.equals("BA")) {
                txtMicroReg.setVisibility(View.VISIBLE);
                tableRS.setVisibility(View.GONE);
                tableBA.setVisibility(View.VISIBLE);
                radioMacroRS.setChecked(false);
                radioMacroBA.setChecked(true);
                btmapRS.setVisibility(View.GONE);
                btmapBA.setVisibility(View.VISIBLE);
                macroRegiao = "BA";

                if (microRegiao2.equals("Região 1 BA")) {
                    microRegiao = "Região 1 BA";
                    radioMicroRegiao1BA.setChecked(true);
                    radioMicroRegiao2BA.setChecked(false);
                    radioMicroRegiao3BA.setChecked(false);
                    radioMicroRegiao4BA.setChecked(false);

                }else if(microRegiao2.equals("Região 2 BA")){
                    microRegiao = "Região 2 BA";
                    radioMicroRegiao1BA.setChecked(false);
                    radioMicroRegiao2BA.setChecked(true);
                    radioMicroRegiao3BA.setChecked(false);
                    radioMicroRegiao4BA.setChecked(false);

                } else if(microRegiao2.equals("Região 3 BA")){
                    microRegiao = "Região 3 BA";
                    radioMicroRegiao1BA.setChecked(false);
                    radioMicroRegiao2BA.setChecked(false);
                    radioMicroRegiao3BA.setChecked(true);
                    radioMicroRegiao4BA.setChecked(false);

                } else if(microRegiao2.equals("Região 4 BA")){
                    microRegiao = "Região 4 BA";
                    radioMicroRegiao1BA.setChecked(false);
                    radioMicroRegiao2BA.setChecked(false);
                    radioMicroRegiao3BA.setChecked(false);
                    radioMicroRegiao4BA.setChecked(true);

                }
            }
/*
            if (medicao != null) {
                int spinnerPosition = adapter.getPosition(medicao);
                spnTipoMedicao.setSelection(spinnerPosition);
            }
*/
        }
    }

    public void clicouAutomaticoCidade(View v) {
        try {
            getLocalization();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clicouManualCidade(View v) {
        edtCidade.setText("");
    }

    public void getLocalization() throws IOException {

        gps = new ObtainGPS(Proposta1.this);

        if(gps.canGetLocation()) {
            if(gps.getLongitude() == 0.0 || gps.getLatitude() == 0.0){
                mostraFalha();
            }else{
                longitude = String.valueOf(gps.getLongitude());
                latitude = String.valueOf(gps.getLatitude());
                endereco = buscaEndereco(gps.getLatitude(), gps.getLongitude());



                Location locationA = new Location("point A");

                locationA.setLatitude(gps.getLatitude());
                locationA.setLongitude(gps.getLongitude());

                Location locationB = new Location("point B");
                Location locationIbiruba = new Location("point C");

                locationB.setLatitude(-29.700512);
                locationB.setLongitude(-53.726328);

                locationIbiruba.setLatitude(-28.635100);
                locationIbiruba.setLongitude(-53.114711);
/*
                LatLng posicaoInicial = new LatLng(gps.getLatitude(),gps.getLongitude());
                LatLng posicaiFinal = new LatLng(-28.635100,-53.114711);

                double distanciaMetros23 = distanceMetros(gps.getLatitude(),gps.getLongitude(),-29.700512,-53.726328);
                Log.i("LOG","A Distancia 2  é planeta djhsf  = "+distanciaMetros23 / 1000);

                LatLng posicaoInicial2 = new LatLng(gps.getLatitude(),gps.getLongitude());
                LatLng posicaiFinal2 = new LatLng(-29.700512,-53.726328);

                double distanciaNova = SphericalUtil.computeDistanceBetween(posicaoInicial, posicaiFinal);
                double distanciaNova2 = SphericalUtil.computeDistanceBetween(posicaoInicial2, posicaiFinal2);
                Log.i("LOG","A Distancia é do Planeta= "+formatNumber(distanciaNova));
                Log.i("LOG","A Distancia é do Planeta2= "+formatNumber(distanciaNova2));

                double distancia = distance(gps.getLatitude(),gps.getLongitude(),-28.635100,-53.114711);
                Log.i("LOG","A Distancia é = "+distancia);


                double distanciaMetros = distanceMetros(gps.getLatitude(),gps.getLongitude(),-28.635100,-53.114711);
                Log.i("LOG","A Distancia 2  é = "+distanciaMetros / 1000);

                Location startPoint=new Location("locationA");
                startPoint.setLatitude(gps.getLatitude());
                startPoint.setLongitude(gps.getLongitude());

                Location endPoint=new Location("locationA");
                endPoint.setLatitude(-28.635100);
                endPoint.setLongitude(-53.114711);

                double distanceLoca = startPoint.distanceTo(endPoint);
                Log.i("LOG","A Distancia Loca é = "+distanceLoca / 1000);

*/
                float distance = locationA.distanceTo(locationB);
                float distanceFrete = locationA.distanceTo(locationIbiruba);
                float distanceKM = distance / 1000;
                float distanceKMFrete = distanceFrete / 1000;
                int distanciaIntKm = (int) Math.ceil(distanceKM);
                int distanciaIntKmFrete = (int) Math.ceil(distanceKMFrete);

                distanciaIntKm = distanciaIntKm * 2;
                distanciaIntKmFrete = distanciaIntKmFrete * 2;

                //if(endereco.getLocality() != null){
                cidadeFoto = endereco.getSubAdminArea();
                Log.d("cidade", "endereco: " + String.valueOf(endereco));
                /*
            Log.d("cidade", "cidade: "+String.valueOf(endereco.getLocality()));
            Log.d("cidade", "cidade 2: "+String.valueOf(endereco.get(0).getLocality()));
            Log.d("cidade", "cidade 3: "+String.valueOf(endereco.get(0).getLocality()));
            Log.d("cidade", "cidade 4: "+String.valueOf(endereco.get(0).getLocality()));
            Log.d("cidade", "cidade 5: "+String.valueOf(endereco.get(0).getLocality()));
            Log.d("cidade", "cidade 2: "+String.valueOf(endereco.get(0).getLocality()));
            Log.d("cidade", "cidade 2: "+String.valueOf(endereco.get(0).getLocality()));

*/


                estadoFoto = endereco.getAdminArea();
                //}else{

                //}
                //cidadeFoto = endereco.getLocality();
                //estadoFoto = endereco.getAdminArea();

                if (estadoFoto.equals("Rio Grande do Sul")) {
                    estadoFoto = "RS";
                } else if (estadoFoto.equals("Bahia")) {
                    estadoFoto = "BA";
                }
                if (distanciaIntKm < 50) {
                    distanciaIntKm = 50;
                }

                if (distanciaIntKmFrete < 50) {
                    distanciaIntKmFrete = 50;
                }
                if (cidadeFoto != null) {
                    edtCidade.setText(cidadeFoto + " - " + estadoFoto);
                    edtDeslocamento.setText("" + distanciaIntKm);
                    edtDesFrete.setText("" + distanciaIntKmFrete);
                } else {
                    edtCidade.setText("");
                    edtDeslocamento.setText("");
                    edtDesFrete.setText("");
                    mostraFalha();
                }
            }

       } else {
            gps.showSettingsAlert();
            SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove("idPropostaKey");
            editor.commit();
            editor.clear();

            Intent it = new Intent(this, Inicial.class);
            startActivity(it);
            finish();
        }
        /*
        gps = new ObtainGPS(Proposta1.this);

        if (GetLocalization(Proposta1.this)) {
            if (gps.canGetLocation()) {

                longitude = String.valueOf(gps.getLongitude());
                latitude = String.valueOf(gps.getLatitude());
                endereco = buscaEndereco(gps.getLatitude(),gps.getLongitude());

                Location locationA = new Location("point A");

                locationA.setLatitude(gps.getLatitude());
                locationA.setLongitude(gps.getLongitude());

                Location locationB = new Location("point B");

                //Latitude Longitude HCC
                locationB.setLatitude(-29.700512);
                locationB.setLongitude(-53.726328);

                float distance = locationA.distanceTo(locationB);
                float distanceKM = distance/1000;
                int distanciaIntKm = (int) Math.ceil(distanceKM);
                distanciaIntKm = distanciaIntKm*2;
                //distanciaKM = distanceKM;

                //Log.d("mensagem", "Distancia: " + distance);

                cidadeFoto = endereco.getLocality();
                estadoFoto = endereco.getAdminArea();

                if(estadoFoto.equals("Rio Grande do Sul")){
                    estadoFoto = "RS";
                }else if(estadoFoto.equals("Bahia")){
                    estadoFoto = "BA";
                }
                /*
                Log.d("Numero: ", "" + numeroFoto);
                Log.d("Rua: ", "" + ruaFoto);
                Log.d("Bairro :", "" + bairroFoto);
                Log.d("Cidade: ", "" + cidadeFoto);
                Log.d("Estado: ", "" + estadoFoto);
                */
        /*
                edtCidade.setText(cidadeFoto+" - "+estadoFoto);
                edtDeslocamento.setText(""+distanciaIntKm);

            } else {
                AlertDialog.Builder erroLocation = new AlertDialog.Builder(this);
                erroLocation.setTitle("Localização não encontrada");
                erroLocation.setMessage("Sua Localização não foi encontrada!! Tente novamente!");
                erroLocation.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                startActivity(new Intent(Proposta1.this, Inicial.class));
                                finish();
                            }
                        });
                AlertDialog alertDialog = erroLocation.create();
                alertDialog.show();

                gps.showSettingsAlert();
            }

        }
        */
    }
/*
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
*/
/*
    private float distanceMetros(double currentlatitude, double currentlongitude, double originLat, double originLon) {

        float[] results = new float[1];
        Location.distanceBetween(currentlatitude, currentlongitude, originLat, originLon, results);
        float distanceInMeters = results[0];

        return distanceInMeters;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);}

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);}

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);}

    private String formatNumber(double distance) {
        String unit = "m";
        //if (distance  1000) {
            distance /= 1000;
            unit = "km";
        //}

        return String.format("%4.3f%s", distance, unit);
    }
*/
    private void mostraFalha() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#000000'>Erro ao acessar GPS!</font>"));
        alertDialogBuilder.setMessage(Html.fromHtml("<font color='#000000'>Por Favor! \n Insira a Cidade e o estado manualmente!</font>"));


        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean GetLocalization(Context context) {
        int REQUEST_PERMISSION_LOCALIZATION = 221;
        boolean res = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                res = false;
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_LOCALIZATION);
            }
        }
        return res;
    }

    public Address buscaEndereco(double latitude, double longitude) throws IOException{

        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext());
        addresses = geocoder.getFromLocation(latitude,longitude,1);
        if (addresses.size()>0){
            address = addresses.get(0);
        }

        return  address;
    }

}
