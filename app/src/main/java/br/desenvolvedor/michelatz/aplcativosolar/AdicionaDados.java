package br.desenvolvedor.michelatz.aplcativosolar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AdicionaDados extends AppCompatActivity {

    private RadioButton radio0Graus, radio5Graus, radio10Graus, radio15Graus, radio20Graus, radio25Graus, radio30Graus;
    private RadioButton radioNorte, radioSul, radioLeste, radioOeste;

    private EditText edtAreaLado, edtOrientacao;

    String inclinacao, orientacao, idConsumoEditavel, valorOrientacao;

    SQLiteDatabase db;
    String BANCO = "banco.db";
    String TABELADADOSTELHADO = "telhado";
    String vaiEditar = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_dados);

        radioNorte = (RadioButton) findViewById(R.id.radioNorte);
        radioSul = (RadioButton) findViewById(R.id.radioSul);
        radioLeste = (RadioButton) findViewById(R.id.radioLeste);
        radioOeste = (RadioButton) findViewById(R.id.radioOeste);

        radioNorte.setVisibility(View.GONE);
        radioSul.setVisibility(View.GONE);
        radioLeste.setVisibility(View.GONE);
        radioOeste.setVisibility(View.GONE);

        radio0Graus = (RadioButton) findViewById(R.id.radio0Graus);
        radio5Graus = (RadioButton) findViewById(R.id.radio5Graus);
        radio10Graus = (RadioButton) findViewById(R.id.radio10Graus);
        radio15Graus = (RadioButton) findViewById(R.id.radio15Graus);
        radio20Graus = (RadioButton) findViewById(R.id.radio20Graus);
        radio25Graus = (RadioButton) findViewById(R.id.radio25Graus);
        radio30Graus = (RadioButton) findViewById(R.id.radio30Graus);

        edtAreaLado = (EditText) findViewById(R.id.edtAreaLado);
        edtOrientacao = (EditText) findViewById(R.id.edtOrientacao);

        edtOrientacao.setText("Norte");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("HCC SOLAR");
        toolbar.setSubtitle("Adiciona Dados da Unidade");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // Verifica se existe dados enviados da pagina anterior
        if (getIntent().getStringExtra("ACAO") != null) {
            // Verifica se veio para edição, se sim: variavel Vaieditar recebe 1 e é pego o id do Consumo que será editado
            if (getIntent().getStringExtra("ACAO").equals("EDITAR")) {
                vaiEditar = "1";
                idConsumoEditavel = bundle.getString("IDCONSUMO");
                preencheDadosEdicao();
            }
        }
    }

    private void preencheDadosEdicao() {

        String preencheArea = null,  preencheInclinacao = null, preencheOrientacao = null;
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);

        Cursor linhas = db.rawQuery("SELECT * FROM " + TABELADADOSTELHADO + " WHERE ID = " + idConsumoEditavel + ";", null);
        if (linhas.moveToFirst()) {
            do {
                preencheArea = linhas.getString(1);
                //preencheSombreamento = linhas.getString(2);
                preencheInclinacao = linhas.getString(3);
                preencheOrientacao = linhas.getString(4);

                Log.d("mensagem","1: "+ linhas.getString(1));
                Log.d("mensagem","2: "+ linhas.getString(2));
                Log.d("mensagem","3: "+ linhas.getString(3));
                Log.d("mensagem","4: "+ linhas.getString(4));

            }
            while (linhas.moveToNext());
            linhas.close();
        }
        db.close();

        if (preencheArea != null) {
            edtAreaLado.setText(preencheArea);
        }
        /*
        if (preencheSombreamento != null) {
            if(preencheSombreamento.equals("Nenhum")){
                radioSombriamentoNenhum.setChecked(true);

            }else if(preencheSombreamento.equals("Pouco")){
                radioSombriamentoPouco.setChecked(true);

            }else if(preencheSombreamento.equals("Muito")){
                radioSombriamentoMuito.setChecked(true);

            }
        }
        */
        if (preencheInclinacao != null) {
            if(preencheInclinacao.equals("0")){
                radio0Graus.setChecked(true);

            }else if(preencheInclinacao.equals("5")){
                radio5Graus.setChecked(true);

            }else if(preencheInclinacao.equals("10")){
                radio10Graus.setChecked(true);

            }else if(preencheInclinacao.equals("15")){
                radio15Graus.setChecked(true);

            }else if(preencheInclinacao.equals("20")){
                radio20Graus.setChecked(true);

            }else if(preencheInclinacao.equals("25")){
                radio25Graus.setChecked(true);

            }else if(preencheInclinacao.equals("30")){
                radio30Graus.setChecked(true);

            }
            inclinacao = preencheInclinacao;

        }
        if (preencheOrientacao != null) {
            /*
            if(preencheOrientacao.equals("Norte")){
                radioNorte.setChecked(true);

            }else if(preencheOrientacao.equals("Sul")){
                radioSul.setChecked(true);

            }else if(preencheOrientacao.equals("Oeste")){
                radioOeste.setChecked(true);

            }else if(preencheOrientacao.equals("Leste")){
                radioLeste.setChecked(true);

            }
            orientacao = preencheOrientacao;
            */
            //valorOrientacao = preencheOrientacao;
            edtOrientacao.setText(preencheOrientacao);
        }
    }

    public void finalizaInsercao(View v) {
        SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
        String idProposta = sharedpreferences.getString("idPropostaKey", null);
        String Area = "";
        String orientacaoVal = "";
        //String Sombreamento = "";

        Area = edtAreaLado.getText().toString();
        orientacaoVal = edtOrientacao.getText().toString();

        //Sombreamento =((RadioButton)findViewById(grupSombriamento.getCheckedRadioButtonId())).getText().toString();


        Log.d("resposta","Area: "+Area);
        //Log.d("resposta","Sombreamento: "+Sombreamento);
        Log.d("resposta","inclinacao: "+inclinacao);
        //Log.d("resposta","orientacao: "+orientacao);

        if (Area.equals("") ||  inclinacao.equals("") || orientacaoVal.equals("")) {
            Toast.makeText(getApplicationContext(), "Todos os dados obrigatórios!!", Toast.LENGTH_SHORT).show();
        } else {
            //Verifica se é uma edição
            if (vaiEditar.equals("1")) { //se for, faz update
                db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
                ContentValues values = new ContentValues();
                values.put("AREA", Area);
                //values.put("SOMBREAMENTO", Sombreamento);
                values.put("INCLINACAO", inclinacao);
                values.put("ORIENTACAO", orientacaoVal);
                Log.d("Banco ","Aquiiiii  11111: ");

                db.update(TABELADADOSTELHADO, values, "ID=" + idConsumoEditavel, null);
                db.close();
            } else { //se não for, faz um insert
                db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
                ContentValues values = new ContentValues();
                values.put("AREA", Area);
                //values.put("SOMBREAMENTO", Sombreamento);
                values.put("INCLINACAO", inclinacao);
                values.put("ORIENTACAO", orientacaoVal);
                values.put("IDPROPOSTA", idProposta);

                Log.d("Banco ","Aquiiiii  2222: ");

                db.insert(TABELADADOSTELHADO, null, values);
                db.close();
            }
            Intent it = new Intent(this, Proposta4.class);
            startActivity(it);
            finish();
        }
    }

    //Metodo que pega ação do botão voltar no toolbar bem em cima da tela
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent it = new Intent(this, Proposta4.class);
                startActivity(it);
                finish();

                break;
            default:break;
        }
        return true;
    }

    //Metodo responsavel por pegar ação do botão nativo "Voltar" do smartfone
    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, Proposta4.class);
        startActivity(it);
        finish();
    }

    public void trocaOrientaca(View v){
        inseriNorte();
    }

    private void inseriNorte() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("A Orientação é NORTE?");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        edtOrientacao.setText("Norte");
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        inseriOeste();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void inseriOeste() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("A Orientação é OESTE?");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        edtOrientacao.setText("Oeste");
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        inseriLeste();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void inseriLeste() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("A Orientação é LESTE?");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        edtOrientacao.setText("Leste");
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        inseriSul();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void inseriSul() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("A Orientação é SUL?");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        edtOrientacao.setText("Sul");
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        edtOrientacao.setText("Norte");
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void clicouOeste(View v){
        orientacao = "Oeste";
        radioSul.setChecked(false);
        radioLeste.setChecked(false);
        radioNorte.setChecked(false);

    }

    public void clicouSul(View v){
        orientacao = "Sul";
        radioOeste.setChecked(false);
        radioLeste.setChecked(false);
        radioNorte.setChecked(false);

    }

    public void clicouLeste(View v){
        orientacao = "Leste";
        radioSul.setChecked(false);
        radioOeste.setChecked(false);
        radioNorte.setChecked(false);

    }

    public void clicouNorte(View v){
        orientacao = "Norte";
        radioSul.setChecked(false);
        radioLeste.setChecked(false);
        radioOeste.setChecked(false);

    }

    public void clicou0Graus(View v){
        inclinacao = "0";
        radio5Graus.setChecked(false);
        radio10Graus.setChecked(false);
        radio15Graus.setChecked(false);
        radio20Graus.setChecked(false);
        radio25Graus.setChecked(false);
        radio30Graus.setChecked(false);
    }

    public void clicou5Graus(View v){
        inclinacao = "5";
        radio0Graus.setChecked(false);
        radio10Graus.setChecked(false);
        radio15Graus.setChecked(false);
        radio20Graus.setChecked(false);
        radio25Graus.setChecked(false);
        radio30Graus.setChecked(false);
    }

    public void clicou10Graus(View v){
        inclinacao = "10";
        radio5Graus.setChecked(false);
        radio0Graus.setChecked(false);
        radio15Graus.setChecked(false);
        radio20Graus.setChecked(false);
        radio25Graus.setChecked(false);
        radio30Graus.setChecked(false);
    }

    public void clicou15Graus(View v){
        inclinacao = "15";
        radio5Graus.setChecked(false);
        radio10Graus.setChecked(false);
        radio0Graus.setChecked(false);
        radio20Graus.setChecked(false);
        radio25Graus.setChecked(false);
        radio30Graus.setChecked(false);
    }

    public void clicou20Graus(View v){
        inclinacao = "20";
        radio5Graus.setChecked(false);
        radio10Graus.setChecked(false);
        radio15Graus.setChecked(false);
        radio0Graus.setChecked(false);
        radio25Graus.setChecked(false);
        radio30Graus.setChecked(false);
    }

    public void clicou25Graus(View v){
        inclinacao = "25";
        radio5Graus.setChecked(false);
        radio10Graus.setChecked(false);
        radio15Graus.setChecked(false);
        radio20Graus.setChecked(false);
        radio0Graus.setChecked(false);
        radio30Graus.setChecked(false);
    }

    public void clicou30Graus(View v){
        inclinacao = "30";
        radio5Graus.setChecked(false);
        radio10Graus.setChecked(false);
        radio15Graus.setChecked(false);
        radio20Graus.setChecked(false);
        radio25Graus.setChecked(false);
        radio0Graus.setChecked(false);
    }

}
