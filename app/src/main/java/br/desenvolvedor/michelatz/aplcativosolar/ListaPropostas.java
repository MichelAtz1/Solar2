package br.desenvolvedor.michelatz.aplcativosolar;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ListaPropostas extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ListView.OnItemClickListener {

    SQLiteDatabase db, db2;
    String BANCO = "banco.db";
    String TABELAPROPOSTA = "proposta", TABELACONSUMOMES = "consumomes", TABELAVALORES = "valores", TABELAVALORESINICIAIS = "valoresiniciais";
    String TABELADADOSTELHADO = "telhado";
    public static final String MyPREFERENCES = "MinhasPreferencias";
    public static final String idPropostaPref = "idPropostaKey";
    SharedPreferences sharedpreferences;
    private ListView listaPropostasFinalizadas;

    String numeroProposta, nomeCliente, dataProposta, dtaStringEditada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_propostas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("HCC SOLAR");
        toolbar.setSubtitle("Lista de Propostas");
        setSupportActionBar(toolbar);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        listaPropostasFinalizadas = (ListView) findViewById(R.id.listaPropostasFinalizadas);
        listaPropostasFinalizadas.setOnItemClickListener(this);
        preencheLista();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newbuilder.build());
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
            values.put("REPRESENTANTE", "");
            values.put("COMISSAO", "");
            values.put("DESCONTO", "");
            values.put("VERIFICACAO", "");
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

        } /* else if (id == R.id.nav_3) {
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

        } */ else if (id == R.id.nav_sair) {
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

    // Busca no banco de dados os dados
    public void preencheLista() {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        Cursor linhas = db.rawQuery("select * from " + TABELAPROPOSTA + " WHERE VERIFICACAO = '1' ORDER BY ID DESC;", null);
        if (linhas.moveToFirst()) {
            do {
                String id = linhas.getString(0);
                String data = linhas.getString(1);
                String proposta = linhas.getString(2);
                String cliente = linhas.getString(3);

                HashMap<String, String> servicos = new HashMap<>();
                servicos.put("ID", id);
                servicos.put("PROPOSTA", proposta);
                servicos.put("CLIENTE", cliente);
                servicos.put("DATA", data);
                list.add(servicos);
            }
            while (linhas.moveToNext());
        }
        db.close();
        ListAdapter adapter = new SimpleAdapter(
                ListaPropostas.this, list, R.layout.list_item_relatorio,
                new String[]{"CLIENTE", "PROPOSTA", "DATA"},
                new int[]{R.id.txtCliente, R.id.txtCodigo, R.id.txtData});

        listaPropostasFinalizadas.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        HashMap<String, String> map = (HashMap) parent.getItemAtPosition(position);
        final String idLocacaoSeleciona = map.get("ID").toString();
        numeroProposta = map.get("PROPOSTA").toString();
        nomeCliente = map.get("CLIENTE").toString();
        dataProposta = map.get("DATA").toString();
        //preencheDados(idLocacaoSeleciona);

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("O que você deseja fazer com a Proposta " + numeroProposta + " ?");
        alertDialogBuilder.setPositiveButton("Relatórios",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        enviaDados();
                    }
                });

        alertDialogBuilder.setNegativeButton("Gerenciar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        gerenciarLocacao(idLocacaoSeleciona);
                    }
                });


        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void enviaDados() {

        dtaStringEditada = dataProposta.replace('/', '-');

        //Log.d("mensagem","Nova Data: "+data_editada);

        //Intent itEmail = new Intent(Intent.ACTION_SEND);
        //itEmail.setType("application/");

        //ArrayList<Uri> arquivosUris = new ArrayList<Uri>();
        //Uri arquivo1 = Uri.parse("file:/mnt/sdcard/HCC SOLAR/"+nomeCliente+"_"+numeroProposta+"_"+data_editada+"_Proposta.pdf");
        //.add(arquivo1);


        //itEmail.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arquivosUris);
        //itEmail.putExtra(Intent.EXTRA_SUBJECT, "Proposta HCC SOLAR");
        //itEmail.putExtra(Intent.EXTRA_TEXT, "Proposta realizada em " + dataProposta + ", referente ao cliente " + nomeCliente + ", e o numero da nota é: " + numeroProposta);
        //itEmail.putExtra(Intent.EXTRA_EMAIL, "michelatz1@gmail.com");

        //startActivity(Intent.createChooser(itEmail, "Escolha a App para envio do e-mail..."));

        Intent itEmail = new Intent(Intent.ACTION_SEND);
        itEmail.setType("file/*");
        //File Arq = new File("/data/data/virtualsistemas.controlepedidos/databases/VirtualPDF.pdf");
        itEmail.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:/mnt/sdcard/HCC SOLAR/"+nomeCliente+"_"+numeroProposta+"_"+dtaStringEditada+"_Proposta.pdf"));
        itEmail.putExtra(Intent.EXTRA_SUBJECT, "Proposta HCC SOLAR");
        itEmail.putExtra(Intent.EXTRA_TEXT, "Proposta realizada em " + dataProposta + ", referente ao cliente " + nomeCliente + ", e o numero da nota é: " + numeroProposta);
        itEmail.putExtra(Intent.EXTRA_EMAIL, "michelatz1@gmail.com");
        startActivity(Intent.createChooser(itEmail,"Escolha a App para envio do e-mail..."));
    }

    // Abre um Alert para seleção de um tipo de edição
    public void gerenciarLocacao(final String idLocal) {

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("O que você deseja Gerenciar na Proposta " + numeroProposta + " ?");
        alertDialogBuilder.setPositiveButton("Excluir",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        confirmarDeleteProposta(idLocal);
                        //Toast.makeText(ListaPropostas.this, "Esta Aba esta em desenvolvimento", Toast.LENGTH_SHORT).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("Clonar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dtaStringEditada = dataProposta.replace('/', '-');
                        long ultimoId = clonaPropostaBanco(idLocal);
                        clonaDados(ultimoId, idLocal);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(idPropostaPref, String.valueOf(ultimoId));
                        editor.commit();

                        Intent it = new Intent(ListaPropostas.this, Proposta1.class);
                        startActivity(it);
                        finish();
                        /*
                        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString("idLocacaoKey", idLocal);
                        editor.putString("numeroNotaKey", notaLocacaoSelecionada);
                        editor.commit();

                        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
                        ContentValues values = new ContentValues();
                        values.put("STATUS", "0");
                        db.update(TABELALOCACAO, values, "ID=" + idLocal, null);
                        db.close();

                        Intent intent = new Intent(ListaPropostas.this, Proposta1.class);
                        ListaPropostas.this.startActivity(intent);
                        finish();
                        */
                        //Toast.makeText(ListaPropostas.this, "Esta Aba esta em desenvolvimento", Toast.LENGTH_SHORT).show();
                    }
                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    // Alert para confirmação de exclusão de uma locação
    private void confirmarDeleteProposta(final String idNotaLocac) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Tem certeza que deseja deletar esta Proposta?");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletarDadosProposta(idNotaLocac);
                        Toast.makeText(ListaPropostas.this, "Excluido com Sucesso!", Toast.LENGTH_SHORT).show();
                        Intent it = new Intent(ListaPropostas.this, ListaPropostas.class);
                        startActivity(it);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public long clonaPropostaBanco(String idClone) {
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);

        String clonaProposta = null, clonaCliente = null, clonaCidade = null, clonaDeslocamento = null, clonaMedicao = null, clonaMacroRegiao = null;
        String clonaMicroRegiao = null, clonaKwH = null, clonaValor = null, clonaRepresentante = null, clonaComicao = null, clonaDesconto = null;
        String clonaFrete = null;
        String clonaSombreamento = null;

        Cursor linhas2 = db.rawQuery("SELECT * FROM " + TABELAPROPOSTA + " WHERE ID = " + idClone + ";", null);
        if (linhas2.moveToFirst()) {
            do {
                clonaProposta  = linhas2.getString(2);
                clonaCliente = linhas2.getString(3);
                clonaCidade = linhas2.getString(4);
                clonaDeslocamento = linhas2.getString(5);
                clonaMedicao = linhas2.getString(6);
                clonaMacroRegiao = linhas2.getString(7);
                clonaMicroRegiao = linhas2.getString(8);
                clonaKwH = linhas2.getString(9);
                clonaValor = linhas2.getString(10);
                clonaRepresentante = linhas2.getString(11);
                clonaComicao = linhas2.getString(12);
                clonaDesconto = linhas2.getString(13);
                clonaFrete = linhas2.getString(15);
                clonaSombreamento = linhas2.getString(16);
            }
            while (linhas2.moveToNext());
            linhas2.close();
        }

        ContentValues values = new ContentValues();
        values.put("DATA", "");
        values.put("PROPOSTA", clonaProposta);
        values.put("CLIENTE", clonaCliente);
        values.put("CIDADE", clonaCidade);
        values.put("DESLOCAMENTO", clonaDeslocamento);
        values.put("MEDICAO", clonaMedicao);
        values.put("MACROREGIAO", clonaMacroRegiao);
        values.put("MICROREGIAO", clonaMicroRegiao);
        values.put("KWH", clonaKwH);
        values.put("VALOR", clonaValor);
        values.put("REPRESENTANTE", clonaRepresentante);
        values.put("COMISSAO", clonaComicao);
        values.put("DESCONTO", clonaDesconto);
        values.put("FRETE", clonaFrete);
        values.put("SOMBREAMENTO", clonaSombreamento);
        values.put("VERIFICACAO", "0");

        long ultimoId = db.insert(TABELAPROPOSTA, null, values);
        db.close();
        return ultimoId;
    }

    public void clonaDados(long idClone, String idInicial) {
        String idString = String.valueOf( idClone);
        String totalMes1 = null, totalMes2 = null, totalMes3 = null, totalMes4 = null, totalMes5 = null, totalMes6 = null, totalMes7 = null, totalMes8 = null, totalMes9 = null, totalMes10 = null, totalMes11 = null, totalMes12 = null;
        String medicao = null, unidade = null;
        String preencheArea = null, preencheSombreamento = null, preencheInclinacao = null, preencheOrientacao = null;

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        Cursor linhas = db.rawQuery("SELECT *  FROM " + TABELACONSUMOMES+ " WHERE IDPROPOSTA = "+ idInicial +";", null);
        if (linhas.moveToFirst()) {
            do {
                totalMes1 = linhas.getString(1);
                totalMes2 = linhas.getString(2);
                totalMes3 = linhas.getString(3);
                totalMes4 = linhas.getString(4);
                totalMes5 = linhas.getString(5);
                totalMes6 = linhas.getString(6);
                totalMes7 = linhas.getString(7);
                totalMes8 = linhas.getString(8);
                totalMes9 = linhas.getString(9);
                totalMes10 = linhas.getString(10);
                totalMes11 = linhas.getString(11);
                totalMes12 = linhas.getString(12);
                medicao = linhas.getString(13);
                unidade = linhas.getString(14);
                salvaDados(totalMes1, totalMes2, totalMes3, totalMes4, totalMes5, totalMes6, totalMes7, totalMes8, totalMes9, totalMes10, totalMes11, totalMes12, medicao, unidade, idString);
            }
            while (linhas.moveToNext());
        }
        linhas.close();
        db.close();

        db2 = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        Cursor linhas2 = db2.rawQuery("SELECT *  FROM " + TABELADADOSTELHADO + " WHERE IDPROPOSTA = "+ idInicial +";", null);
        if (linhas2.moveToFirst()) {
            do {
                preencheArea = linhas2.getString(1);
                preencheSombreamento = "";
                preencheInclinacao = linhas2.getString(3);
                preencheOrientacao = linhas2.getString(4);
                salvaDados2(preencheArea, preencheSombreamento, preencheInclinacao, preencheOrientacao, idString);
            }
            while (linhas2.moveToNext());
        }
        linhas2.close();


        db2.close();
    }

    private void salvaDados2(String preencheArea, String preencheSombreamento, String preencheInclinacao, String preencheOrientacao, String idString) {

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("AREA", preencheArea);
        values.put("SOMBREAMENTO", preencheSombreamento);
        values.put("INCLINACAO", preencheInclinacao);
        values.put("ORIENTACAO", preencheOrientacao);
        values.put("IDPROPOSTA", idString);

        db.insert(TABELADADOSTELHADO, null, values);
        db.close();
    }

    private void salvaDados(String totalMes1, String totalMes2, String totalMes3, String totalMes4, String totalMes5, String totalMes6, String totalMes7, String totalMes8, String totalMes9, String totalMes10, String totalMes11, String totalMes12, String medicao, String unidade,String idDoClone) {

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("JANEIRO", totalMes1);
        values.put("FEVEREIRO", totalMes2);
        values.put("MARCO", totalMes3);
        values.put("ABRIL", totalMes4);
        values.put("MAIO", totalMes5);
        values.put("JUNHO", totalMes6);
        values.put("JULHO", totalMes7);
        values.put("AGOSTO", totalMes8);
        values.put("SETEMBRO", totalMes9);
        values.put("OUTUBRO", totalMes10);
        values.put("NOVEMBRO", totalMes11);
        values.put("DEZEMBRO", totalMes12);
        values.put("MEDICAO", medicao);
        values.put("UNIDADE", unidade);
        values.put("IDPROPOSTA", idDoClone);

        db.insert(TABELACONSUMOMES, null, values);
        db.close();
    }

    // Exclui pastas do celular, utilizando o id da Nota como condição
    private void deletarDadosProposta(String idNotaLocac) {

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        db.execSQL("DELETE FROM " + TABELAPROPOSTA + " WHERE ID = " + idNotaLocac + "");
        db.execSQL("DELETE FROM " + TABELACONSUMOMES + " WHERE IDPROPOSTA = " + idNotaLocac + "");

        db.close();
        deletarProposta();
    }

    private void deletarProposta() {
        //File docsFolder = new File(Environment.getExternalStorageDirectory() + "/HCC SOLAR/");
        //File dir = new File(docsFolder.getAbsolutePath(), nomeCliente+"_"+numeroProposta+"_"+dtaStringEditada+"_Proposta.pdf");
        //File dir = new File("file:/mnt/sdcard/HCC SOLAR/"+nomeCliente+"_"+numeroProposta+"_"+dtaStringEditada+"_Proposta.pdf");
        //dir.delete();
        //File root = new File(Environment.getExternalStorageDirectory(), "HCC SOLAR/"+nomeCliente+"_"+numeroProposta+"_"+dtaStringEditada+"_Proposta.pdf");
        //root.delete();
        //numeroProposta = map.get("PROPOSTA").toString();
        //nomeCliente = map.get("CLIENTE").toString();
        //dataProposta = map.get("DATA").toString();
        dtaStringEditada = dataProposta.replace('/', '-');

        File root = new File(Environment.getExternalStorageDirectory(), "HCC SOLAR/");
        if (!root.exists()) {

        } else {
            File files = new File(root.getAbsolutePath(), nomeCliente+"_"+numeroProposta+"_"+dtaStringEditada+"_Proposta.pdf");
            Log.d("Arquivo:","vaiplaneta:"+files);
            //for (File fInDir : files) {
                //fInDir.delete();
            files.delete();
           // }
            //excluidoComSucesso();
            //preencheLista();
        }

    }

    /*
    // Método que busca no banco os dados da locação no banco e seta seus valores nos respectivos locais
    private void preencheDados(String idLocacaoSeleciona) {
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        Cursor linhas = db.rawQuery("SELECT * FROM " + TABELALOCACAO + " WHERE ID = " + idLocacaoSeleciona + ";", null);

        if (linhas.moveToFirst()) {
            do {
                nomeEmpresa = (linhas.getString(3));
                notaEmpresa = (linhas.getString(1));
                dataLocacao = (linhas.getString(4));
            }
            while (linhas.moveToNext());
            linhas.close();
        }
        db.close();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // Envia os relatórios para email ou Watts
    public String enviaEmail() {

        progress2 = ProgressDialog.show(ListaLocacoesFinalizadas.this, "Selecioanndo relatórios",
                "Os Arquivos estão sendo selecionados para envio!,\n Por Favor. Aguarde!", true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent itEmail = new Intent(Intent.ACTION_SEND_MULTIPLE);
                itEmail.setType("application/");

                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                String nomeUsuario = sharedpreferences.getString("nomeKey", null);
                String emailUsuario = sharedpreferences.getString("emailKey", null);

                ArrayList<Uri> arquivosUris = new ArrayList<Uri>();

                Uri arquivo1 = Uri.parse("file:/mnt/sdcard/HCC/" + notaLocacaoSelecionada + "/" + notaLocacaoSelecionada + "_Postes.txt");
                Uri arquivo2 = Uri.parse("file:/mnt/sdcard/HCC/" + notaLocacaoSelecionada + "/" + notaLocacaoSelecionada + "_Consumidores.txt");
                Uri arquivo3 = Uri.parse("file:/mnt/sdcard/HCC/" + notaLocacaoSelecionada + "/" + notaLocacaoSelecionada + "_Equipamento.pdf");
                Uri arquivo4 = Uri.parse("file:/mnt/sdcard/HCC/" + notaLocacaoSelecionada + "/" + notaLocacaoSelecionada + "_Imagens.pdf");
                Uri arquivo5 = Uri.parse("file:/mnt/sdcard/HCC/" + notaLocacaoSelecionada + "/" + notaLocacaoSelecionada + "_Queda.txt");

                arquivosUris.add(arquivo1);
                arquivosUris.add(arquivo2);
                arquivosUris.add(arquivo3);
                arquivosUris.add(arquivo4);
                arquivosUris.add(arquivo5);

                itEmail.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arquivosUris);
                itEmail.putExtra(Intent.EXTRA_SUBJECT, "Relatório HCC - " + nomeEmpresa + " - " + notaEmpresa);
                itEmail.putExtra(Intent.EXTRA_TEXT, "Relatório realizado em " + dataLocacao + " pelo colaborador " + nomeUsuario + ", de e-mail: " + emailUsuario + ". \n\nReferente ao cliente " + nomeEmpresa + ", e o numero da nota é: " + notaEmpresa);
                itEmail.putExtra(Intent.EXTRA_EMAIL, "michelatz1@gmail.com");
                startActivity(Intent.createChooser(itEmail, "Escolha a App para envio do e-mail..."));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress2.dismiss();

                    }
                });
            }
        }).start();
        return "ok";
    }

    // Verifica Conexão com a Internet
    public boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

    // Alert com resultado de falta de Conecção
    public void naoConectou() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF0000'>Sem acesso a Internet!</font>"));
        alertDialogBuilder.setMessage(Html.fromHtml("<font color='#FF0000'>Para enviar estes relatórios, seu celular precisa estar conectado a internet.\n\n Por Favor!\n Conecte-se a internet e tente novamente!</font>"));


        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Verifica Conexão com a Internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ListaLocacoesFinalizadas.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    // Verifica Tranferencia de dados
    private boolean hasInternetAccess() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    // Cria um Alert, com resposta a falta de conectividade do smartfone
    public void falhaAcessoServidor() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#FF0000'>Falha no Envio de Dados!</font>"));
        alertDialogBuilder.setMessage(Html.fromHtml("<font color='#FF0000'>O envio de dados ao servidor falhou!\n\n Por Favor!\n Verifique seu pacotes de dados ou seu modem de distribuição Wi-Fi e tente novamente!</font>"));

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });


        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Cria um Alert, com a confirmação da exclusão
    public void excluidoComSucesso() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exclusão de Dados!");
        alertDialogBuilder.setMessage("Todos os dados foram Excluídos com Sucesso!");

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(ListaLocacoesFinalizadas.this, ListaLocacoesFinalizadas.class);
                        ListaLocacoesFinalizadas.this.startActivity(intent);
                        finish();
                    }
                });


        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

*/
}
