package br.desenvolvedor.michelatz.aplcativosolar;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Proposta3 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private RadioGroup grupRepresentante;
    //private RadioButton radioNao, radioSim;
    private EditText edtKWH, edtValor, edtAreaTelhadoDisponivel;//edtValorComissao, edtValorDesconto
    private TextView txtAreaTelhadoDisponivel;

    SQLiteDatabase db;
    String BANCO = "banco.db";
    String TABELAPROPOSTA = "proposta";
    public static final String idPropostaPref = "idPropostaKey";
    private String idProposta;
    ArrayAdapter adapter;

    private RadioButton radioSombriamentoNenhum, radioSombriamentoPouco, radioSombriamentoMuito;
    private RadioGroup grupSombriamento;

    private Spinner spnTipoTelhado;
    ArrayList<String> tipoTelhado = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposta3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("HCC SOLAR");
        toolbar.setSubtitle("Proposta - Passo 3");
        setSupportActionBar(toolbar);

        tipoTelhado.add("Selecione o Telhado");
        tipoTelhado.add("Metálico");
        tipoTelhado.add("Cerâmico");
        tipoTelhado.add("Solo");

        SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
        idProposta = sharedpreferences.getString("idPropostaKey", null);

        edtKWH = (EditText) findViewById(R.id.edtKWH);
        edtValor = (EditText) findViewById(R.id.edtValor);
        edtAreaTelhadoDisponivel = (EditText) findViewById(R.id.edtAreaTelhadoDisponivel);
        txtAreaTelhadoDisponivel = (TextView) findViewById(R.id.txtAreaTelhadoDisponivel);

        edtAreaTelhadoDisponivel.setVisibility(View.GONE);
        txtAreaTelhadoDisponivel.setVisibility(View.GONE);

        radioSombriamentoNenhum = (RadioButton) findViewById(R.id.radioSombriamentoNenhum);
        radioSombriamentoPouco = (RadioButton) findViewById(R.id.radioSombriamentoPouco);
        radioSombriamentoMuito = (RadioButton) findViewById(R.id.radioSombriamentoMuito);

        grupSombriamento = (RadioGroup) findViewById(R.id.grupSombriamento);

        //edtValorComissao = (EditText) findViewById(R.id.edtValorComissao);
        //edtValorDesconto = (EditText) findViewById(R.id.edtValorDesconto);

        //grupRepresentante = (RadioGroup) findViewById(R.id.grupRepresentante);

        //radioNao = (RadioButton) findViewById(R.id.radioNao);
        //radioSim = (RadioButton) findViewById(R.id.radioSim);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        spnTipoTelhado = (Spinner) findViewById(R.id.spnTipoTelhado);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoTelhado);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoTelhado.setAdapter(adapter);

        preencheDados();
    }

    public void preencheDados() {
        String KWH = null;
        String Valor = null;
        //String ValorComissao = null;
        String telhado = null;
        String ValorDesconto = null;
        String Representante = null;
        String preencheSombreamento = null;



        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        Cursor linhas = db.rawQuery("SELECT * FROM " + TABELAPROPOSTA + " WHERE ID = " + idProposta + ";", null);
        if (linhas.moveToFirst()) {
            do {
                KWH = linhas.getString(9);
                Valor = linhas.getString(10);
                Representante = linhas.getString(11);
                telhado = linhas.getString(12);
                ValorDesconto = linhas.getString(13);
                preencheSombreamento = linhas.getString(16);
            }
            while (linhas.moveToNext());
            linhas.close();
        }
        db.close();

        if (preencheSombreamento != null) {
            if(preencheSombreamento.equals("Nenhum")){
                radioSombriamentoNenhum.setChecked(true);

            }else if(preencheSombreamento.equals("Pouco")){
                radioSombriamentoPouco.setChecked(true);

            }else if(preencheSombreamento.equals("Muito")){
                radioSombriamentoMuito.setChecked(true);

            }
        }

        if (KWH != null) {
            edtKWH.setText(KWH);
        }

        if (Valor != null) {
            edtValor.setText(Valor);
        }

        if (telhado != null) {
                int spinnerPosition = adapter.getPosition(telhado);
                spnTipoTelhado.setSelection(spinnerPosition);
        }

        if (ValorDesconto != null) {
            edtAreaTelhadoDisponivel.setText(ValorDesconto);
        }
/*
        if (Representante != null) {
            if (Representante.equals("Sim")) {
                grupRepresentante.check(R.id.radioSim);
            } else if (Representante.equals("Não")) {
                grupRepresentante.check(R.id.radioNao);
            }
        }
*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent it = new Intent(this, Proposta2.class);
            startActivity(it);
            finish();
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

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
            values.put("REPRESENTANTE", "");
            values.put("COMISSAO", "");
            values.put("DESCONTO", "0,0");
            values.put("VERIFICACAO", "0");
            values.put("SOMBREAMENTO", "");

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
            Intent it = new Intent(this, Proposta2.class);
            startActivity(it);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void irNovaProposta4(View v) {
        //emDesenvolvimento();

        String KWH = "";
        String Valor = "";
        //String ValorComissao = "";
        String tipoTelhado = "";
        String ValorDesconto = "";
        String Representante = "0";
        String Sombreamento = "";

        Sombreamento =((RadioButton)findViewById(grupSombriamento.getCheckedRadioButtonId())).getText().toString();

        Log.d("sombres","Dentro do 2 "+Sombreamento);

        KWH = edtKWH.getText().toString();
        Valor = edtValor.getText().toString();
        ValorDesconto = edtAreaTelhadoDisponivel.getText().toString();
        //ValorComissao = edtValorComissao.getText().toString();

        if (spnTipoTelhado.getSelectedItem().toString().equals("Selecione o Telhado")) {
            tipoTelhado = "";
        } else {
            tipoTelhado = spnTipoTelhado.getSelectedItem().toString();
        }
    /*

        ValorDesconto = edtValorDesconto.getText().toString();

        int selectedIdIluminacao = grupRepresentante.getCheckedRadioButtonId();
        if (selectedIdIluminacao == radioSim.getId()) {
            Representante = "Sim";
        } else if (selectedIdIluminacao == radioNao.getId()) {
            Representante = "Não";
        }

*/
        if (KWH.equals("") || Sombreamento.equals("") || Valor.equals("") || tipoTelhado.equals("") || ValorDesconto.equals("")) {
            Toast.makeText(getApplicationContext(), "Todos os dados são obrigatórios!!!", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("sombres","Dentro do update 2 "+Sombreamento);
            db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("KWH", KWH);
            values.put("VALOR", Valor);
            values.put("REPRESENTANTE", Representante);
            values.put("COMISSAO", tipoTelhado);
            values.put("DESCONTO", ValorDesconto);
            values.put("SOMBREAMENTO", Sombreamento);
            //values.put("VERIFICACAO", "1");
            db.update(TABELAPROPOSTA, values, "ID=" + idProposta, null);
            db.close();

            Intent it = new Intent(this, Proposta4.class);
            startActivity(it);
            finish();

        }

    }

    public void emDesenvolvimento() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#000000'>Em desenvolvimento!</font>"));
        alertDialogBuilder.setMessage(Html.fromHtml("<font color='#000000'>Esta Aba está em desenvolvimento. Logo estará disponível!</font>"));

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });


        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void visualisarProposta(View v) {
        String KWH = "";
        String Valor = "";
       // String ValorComissao = "";
        String tipoTelhado = "";
        String ValorDesconto = "0";
        String Representante = "0";
        String Sombreamento = "";

        Sombreamento =((RadioButton)findViewById(grupSombriamento.getCheckedRadioButtonId())).getText().toString();

        Log.d("sombres","Dentro do 1"+Sombreamento);

        KWH = edtKWH.getText().toString();
        Valor = edtValor.getText().toString();
        ValorDesconto = edtAreaTelhadoDisponivel.getText().toString();

        if (spnTipoTelhado.getSelectedItem().toString().equals("Selecione o Telhado")) {
            tipoTelhado = "";
        } else {
            tipoTelhado = spnTipoTelhado.getSelectedItem().toString();
        }
/*
        ValorDesconto = edtValorDesconto.getText().toString();

        if(ValorDesconto.equals("")){
            ValorDesconto="0";
        }

        int selectedIdIluminacao = grupRepresentante.getCheckedRadioButtonId();
        if (selectedIdIluminacao == radioSim.getId()) {
            Representante = "Sim";
        } else if (selectedIdIluminacao == radioNao.getId()) {
            Representante = "Não";
        }
*/
        if (KWH.equals("") || Valor.equals("") || tipoTelhado.equals("")) {
            Toast.makeText(getApplicationContext(), "Os campos kWh, Valor e Tipo de Telhado são obrigatórios!", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("sombres","Dentro do update"+Sombreamento);
            db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("KWH", KWH);
            values.put("VALOR", Valor);
            values.put("REPRESENTANTE", Representante);
            values.put("COMISSAO", tipoTelhado);
            values.put("DESCONTO", ValorDesconto);
            values.put("SOMBREAMENTO", Sombreamento);
            db.update(TABELAPROPOSTA, values, "ID=" + idProposta, null);
            db.close();

            Intent it = new Intent(this, VisualizaProposta.class);
            startActivity(it);
            finish();
        }
    }

}
