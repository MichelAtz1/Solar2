package br.desenvolvedor.michelatz.aplcativosolar;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class AdicionaConsumoMes extends AppCompatActivity {

    private EditText edtJaneiro, edtFevereiro, edtMarco, edtAbril, edtMaio, edtJunho;
    private EditText edtJulho, edtAgosto, edtSetembro, edtOutubro, edtNovembro, edtDezembro;
    private String idConsumoEditavel;

    private CheckBox radiouUnidadeGeradora;

    private Spinner spnTipoMedicao;
    ArrayList<String> tipoMedicao = new ArrayList<String>();
    ArrayAdapter adapter;

    SQLiteDatabase db;
    String BANCO = "banco.db";
    String TABELACONSUMOMES = "consumomes";
    String vaiEditar = "0";

    String valorSpinner = null;
    String unidadeGeradora = null;
    String jan = null;
    String fev = null;
    String mar = null;
    String abr = null;
    String mai = null;
    String jun = null;
    String jul = null;
    String ago = null;
    String set = null;
    String out = null;
    String nov = null;
    String dez = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_consumo_mes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("HCC SOLAR");
        toolbar.setSubtitle("Inserção de consumo mês");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        radiouUnidadeGeradora = (CheckBox) findViewById(R.id.radiouUnidadeGeradora);

        tipoMedicao.add("Selecione a Medição");
        tipoMedicao.add("Monofásica");
        tipoMedicao.add("Bifásica");
        tipoMedicao.add("Trifásica");

        edtJaneiro = (EditText) findViewById(R.id.edtJaneiro);
        edtFevereiro = (EditText) findViewById(R.id.edtFevereiro);
        edtMarco = (EditText) findViewById(R.id.edtMarco);
        edtAbril = (EditText) findViewById(R.id.edtAbril);
        edtMaio = (EditText) findViewById(R.id.edtMaio);
        edtJunho = (EditText) findViewById(R.id.edtJunho);
        edtJulho = (EditText) findViewById(R.id.edtJulho);
        edtAgosto = (EditText) findViewById(R.id.edtAgosto);
        edtSetembro = (EditText) findViewById(R.id.edtSetembro);
        edtOutubro = (EditText) findViewById(R.id.edtOutubro);
        edtNovembro = (EditText) findViewById(R.id.edtNovembro);
        edtDezembro = (EditText) findViewById(R.id.edtDezembro);

        spnTipoMedicao = (Spinner) findViewById(R.id.spnTipoMedicao);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoMedicao);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoMedicao.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // Verifica se existe dados enviados da pagina anterior
        if (getIntent().getStringExtra("ACAO") != null) {
            // Verifica se veio para edição, se sim: variavel Vaieditar recebe 1 e é pego o id do Consumo que será editado
            if (getIntent().getStringExtra("ACAO").equals("EDITAR")) {
                vaiEditar = "1";
                idConsumoEditavel = bundle.getString("IDCONSUMO");
                preencheDadosEdicaoConsumo();
            }
        }

    }

    //Metodo responsavel em buscar no banco os dados do consumo de meses e inseri-los nos seus devidos campos.
    public void preencheDadosEdicaoConsumo() {
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);

        Cursor linhas = db.rawQuery("SELECT * FROM " + TABELACONSUMOMES + " WHERE ID = " + idConsumoEditavel + ";", null);
        if (linhas.moveToFirst()) {
            do {
                jan = linhas.getString(1);
                fev = linhas.getString(2);
                mar = linhas.getString(3);
                abr = linhas.getString(4);
                mai = linhas.getString(5);
                jun = linhas.getString(6);
                jul = linhas.getString(7);
                ago = linhas.getString(8);
                set = linhas.getString(9);
                out = linhas.getString(10);
                nov = linhas.getString(11);
                dez = linhas.getString(12);
                valorSpinner = linhas.getString(13);
                unidadeGeradora = linhas.getString(14);

                //Log.d("mensagem ", "Adiciona consumo: Spinner: "+ valorSpinner);
                Log.d("mensagem","1: "+ linhas.getString(1));
                Log.d("mensagem","2: "+ linhas.getString(2));
                Log.d("mensagem","3: "+ linhas.getString(3));
                Log.d("mensagem","4: "+ linhas.getString(4));
                Log.d("mensagem","5: "+ linhas.getString(5));
                Log.d("mensagem","6: "+ linhas.getString(6));
                Log.d("mensagem","7: "+ linhas.getString(7));
                Log.d("mensagem","8: "+ linhas.getString(8));
                Log.d("mensagem","9: "+ linhas.getString(9));
                Log.d("mensagem","10: "+ linhas.getString(10));
                Log.d("mensagem","11: "+ linhas.getString(11));
                Log.d("mensagem","12: "+ linhas.getString(12));
                Log.d("mensagem","Fase: "+ linhas.getString(13));

            }
            while (linhas.moveToNext());
            linhas.close();
        }
        db.close();

        if (valorSpinner != null) {
            int spinnerPosition = adapter.getPosition(valorSpinner);
            spnTipoMedicao.setSelection(spinnerPosition);
        }

        if (unidadeGeradora != null) {
            if (unidadeGeradora.equals("1")) {
                radiouUnidadeGeradora.setChecked(true);
            }
        }

        if (jan != null) {
            edtJaneiro.setText(jan);
        }
        if (fev != null) {
            edtFevereiro.setText(fev);
        }
        if (mar != null) {
            edtMarco.setText(mar);

        }
        if (abr != null) {
            edtAbril.setText(abr);

        }
        if (mai != null) {
            edtMaio.setText(mai);

        }
        if (jun != null) {
            edtJunho.setText(jun);

        }
        if (jul != null) {
            edtJulho.setText(jul);

        }
        if (ago != null) {
            edtAgosto.setText(ago);

        }
        if (set != null) {
            edtSetembro.setText(set);

        }
        if (out != null) {
            edtOutubro.setText(out);

        }
        if (nov != null) {
            edtNovembro.setText(nov);

        }
        if (dez != null) {
            edtDezembro.setText(dez);

        }
    }

    //Metodo que pega ação do botão voltar no toolbar bem em cima da tela
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent it = new Intent(this, Proposta2.class);
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
        Intent it = new Intent(this, Proposta2.class);
        startActivity(it);
        finish();
    }

    public void clicouAutomatico(View v) {
        final Dialog dialog = new Dialog(AdicionaConsumoMes.this);
        dialog.setContentView(R.layout.radiocustomizado);
        dialog.setTitle("Consumo");
        dialog.setCancelable(true);

        Button dialogButton = (Button) dialog.findViewById(R.id.alertOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valorMeses = "";
                EditText textDescricaoEstrutura = (EditText)  dialog.findViewById(R.id.textDescricaoEstrutura);
                valorMeses = textDescricaoEstrutura.getText().toString();

                if(valorMeses.equals("")){
                    Toast.makeText(AdicionaConsumoMes.this, "Por Favor! Insira um valor", Toast.LENGTH_SHORT).show();
                }else {
                    edtJaneiro.setText(valorMeses);
                    edtFevereiro.setText(valorMeses);
                    edtMarco.setText(valorMeses);
                    edtAbril.setText(valorMeses);
                    edtMaio.setText(valorMeses);
                    edtJunho.setText(valorMeses);
                    edtJulho.setText(valorMeses);
                    edtAgosto.setText(valorMeses);
                    edtSetembro.setText(valorMeses);
                    edtOutubro.setText(valorMeses);
                    edtNovembro.setText(valorMeses);
                    edtDezembro.setText(valorMeses);
                    dialog.dismiss();
                }
            }
        });

        Button dialogButtonCancela = (Button) dialog.findViewById(R.id.alertCancelar);
        dialogButtonCancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void clicouManual(View v) {
        if (vaiEditar.equals("1")) {
            preencheDadosEdicaoConsumo();

        }else {
            edtJaneiro.setText("");
            edtFevereiro.setText("");
            edtMarco.setText("");
            edtAbril.setText("");
            edtMaio.setText("");
            edtJunho.setText("");
            edtJulho.setText("");
            edtAgosto.setText("");
            edtSetembro.setText("");
            edtOutubro.setText("");
            edtNovembro.setText("");
            edtDezembro.setText("");
        }
    }

    public void salvarDados(View v) {
        SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
        String idProposta = sharedpreferences.getString("idPropostaKey", null);
        String medicao = "";
        String unidade = "";

        if(radiouUnidadeGeradora.isChecked()){
            unidade = "1";
        }else{
            unidade = "0";
        }

        if (spnTipoMedicao.getSelectedItem().toString().equals("Selecione a Medição")) {
            medicao = "";
        } else {
            medicao = spnTipoMedicao.getSelectedItem().toString();
        }

        //Verifica os itens obrigatórios
        if (medicao.equals("") ||  edtJaneiro.getText().toString().equals("") || edtFevereiro.getText().toString().equals("") || edtMarco.getText().toString().equals("") || edtMaio.getText().toString().equals("") || edtAbril.getText().toString().equals("") || edtJulho.getText().toString().equals("") || edtJunho.getText().toString().equals("") || edtAgosto.getText().toString().equals("") || edtSetembro.getText().toString().equals("") || edtOutubro.getText().toString().equals("") || edtNovembro.getText().toString().equals("") || edtDezembro.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Por Favor! Insira os dados obrigatórios!!", Toast.LENGTH_SHORT).show();
        } else {
            //Verifica se é uma edição
            if (vaiEditar.equals("1")) { //se for, faz update
                db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
                ContentValues values = new ContentValues();
                values.put("JANEIRO", edtJaneiro.getText().toString());
                values.put("FEVEREIRO", edtFevereiro.getText().toString());
                values.put("MARCO", edtMarco.getText().toString());
                values.put("ABRIL", edtAbril.getText().toString());
                values.put("MAIO", edtMaio.getText().toString());
                values.put("JUNHO", edtJunho.getText().toString());
                values.put("JULHO", edtJulho.getText().toString());
                values.put("AGOSTO", edtAgosto.getText().toString());
                values.put("SETEMBRO", edtSetembro.getText().toString());
                values.put("OUTUBRO", edtOutubro.getText().toString());
                values.put("NOVEMBRO", edtNovembro.getText().toString());
                values.put("DEZEMBRO", edtDezembro.getText().toString());
                values.put("MEDICAO", medicao);
                values.put("UNIDADE", unidade);

                Log.d("Banco ","Aquiiiii  11111: "+ medicao);

                db.update(TABELACONSUMOMES, values, "ID=" + idConsumoEditavel, null);
                db.close();
            } else { //se não for, faz um insert
                db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
                ContentValues values = new ContentValues();
                values.put("JANEIRO", edtJaneiro.getText().toString());
                values.put("FEVEREIRO", edtFevereiro.getText().toString());
                values.put("MARCO", edtMarco.getText().toString());
                values.put("ABRIL", edtAbril.getText().toString());
                values.put("MAIO", edtMaio.getText().toString());
                values.put("JUNHO", edtJunho.getText().toString());
                values.put("JULHO", edtJulho.getText().toString());
                values.put("AGOSTO", edtAgosto.getText().toString());
                values.put("SETEMBRO", edtSetembro.getText().toString());
                values.put("OUTUBRO", edtOutubro.getText().toString());
                values.put("NOVEMBRO", edtNovembro.getText().toString());
                values.put("DEZEMBRO", edtDezembro.getText().toString());
                values.put("MEDICAO", medicao);
                values.put("UNIDADE", unidade);
                values.put("IDPROPOSTA", idProposta);

                Log.d("Banco ","Aquiiiii  2222: "+ medicao);

                db.insert(TABELACONSUMOMES, null, values);
                db.close();
            }
            Intent it = new Intent(this, Proposta2.class);
            startActivity(it);
            finish();
        }
    }
}
