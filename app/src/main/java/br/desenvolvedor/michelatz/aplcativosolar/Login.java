package br.desenvolvedor.michelatz.aplcativosolar;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Login extends AppCompatActivity {
    private EditText edtCodigoSeguranca;

    private String JSON_STRING;
    private String JSON_STRING2;
    private String JSON_STRING3;
    private String JSON_STRING4;
    private String JSON_STRING5;
    SQLiteDatabase db;
    String BANCO = "banco.db";
    String TABELAPROPOSTA = "proposta", TABELACONSUMOMES = "consumomes", TABELAVALORES = "valores", TABELAVALORESINICIAIS = "valoresiniciais", TABELAANALISEECONIMICA = "analiseeconomica", TABELADADOSTELHADO = "telhado" , TABELAVALORESSECUNDARIOS = "valoressecundarios", TABELADAPRECOS = "precos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtCodigoSeguranca = (EditText) findViewById(R.id.edtCodigoSeguranca);
        criarBanco();

        final int REQUEST_CODE_ASK_PERMISSIONS = 111;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);

            int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        return;
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
                return;
            }
            return;
        }
    }

    public void criarBanco() {

        /*Metodo openOrCreateDatabase tenta criar o banco caso ele nao exista. Caso ja exista, simplesmente abre o banco */
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);

        //CRIANDO TABELA PROPOSTA
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELAPROPOSTA + " (" + "ID INTEGER PRIMARY KEY, " +
                "DATA TEXT, " +
                "PROPOSTA TEXT, " +
                "CLIENTE TEXT, " +
                "CIDADE TEXT, " +
                "DESLOCAMENTO TEXT, " +
                "MEDICAO TEXT, " +
                "MACROREGIAO TEXT, " +
                "MICROREGIAO TEXT, " +
                "KWH TEXT, " +
                "VALOR TEXT, " +
                "REPRESENTANTE TEXT, " +
                "COMISSAO TEXT, " +
                "DESCONTO TEXT, " +
                "VERIFICACAO TEXT, " +
                "FRETE TEXT, " +
                "SOMBREAMENTO TEXT);");

        //CRIANDO TABELA CONSUMO MES
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELACONSUMOMES + " (" + "ID INTEGER PRIMARY KEY, " +
                "JANEIRO TEXT, " +
                "FEVEREIRO TEXT, " +
                "MARCO TEXT, " +
                "ABRIL TEXT, " +
                "MAIO TEXT, " +
                "JUNHO TEXT, " +
                "JULHO TEXT, " +
                "AGOSTO TEXT, " +
                "SETEMBRO TEXT, " +
                "OUTUBRO TEXT, " +
                "NOVEMBRO TEXT, " +
                "DEZEMBRO TEXT, " +
                "MEDICAO TEXT, " +
                "UNIDADE TEXT, " +
                "IDPROPOSTA INTEGER);");


        //CRIANDO TABELA VALORES
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELAVALORES + " (" + "ID INTEGER PRIMARY KEY, " +
                "SISTEMA TEXT, " +
                "ANO INTEGER, " +
                "AREA TEXT, " +
                "NUMERO TEXT, " +
                "PLACA1 TEXT, " +
                "PLACA2 TEXT, " +
                "VALOR1 TEXT, " +
                "PERNOITES TEXT, " +
                "STARTOBRA TEXT, " +
                "INVERSOR TEXT, " +
                "CONEXAO TEXT, " +
                "FRETE TEXT, " +
                "PROJETO TEXT, " +
                "SISTEMAFINAL TEXT, " +
                "QNT TEXT);");

        //CRIANDO TABELA VALORES
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELAVALORESINICIAIS + " (" + "ID INTEGER PRIMARY KEY, " +
                "CUSTOMO REAL, " +
                "LUCRORESULTADO REAL, " +
                "COMICAO REAL, " +
                "SICRED REAL, " +
                "VALORPERNOITE REAL, " +
                "VALORKM REAL, " +
                "VALORSTART REAL, " +
                "VALORPLACA REAL);");

        //CRIANDO TABELA VALORES 2
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELAVALORESSECUNDARIOS + " (" + "ID INTEGER PRIMARY KEY, " +
                "FDSUJ REAL, " +
                "FDSOMBRAPOUCA REAL, " +
                "FDSOMBRAMUITO REAL, " +
                "FDTELHADOSOLO REAL, " +
                "FDTELHADOCERMETAL REAL, " +
                "MODULODEFAULT REAL, " +
                "MODULOMENOR REAL, " +
                "MODULOMAIOR REAL);");


        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELADADOSTELHADO + " (" + "ID INTEGER PRIMARY KEY, " +
                "AREA TEXT, " +
                "SOMBREAMENTO TEXT, " +
                "INCLINACAO TEXT, " +
                "ORIENTACAO TEXT, " +
                "IDPROPOSTA INTEGER);");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELADAPRECOS + " (" + "ID INTEGER PRIMARY KEY, " +
                "MATERIAL TEXT, " +
                "CUSTO TEXT);");

        //CRIANDO TABELA VALORES
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABELAANALISEECONIMICA + " (" + "ID INTEGER PRIMARY KEY, " +
                "PERDA1 REAL, " +
                "PERDA2 REAL, " +
                "PERDA3 REAL, " +
                "PERDA4 REAL, " +
                "PERDA5 REAL, " +
                "PERDA6 REAL, " +
                "PERDA7 REAL, " +
                "PERDA8 REAL, " +
                "PERDA9 REAL, " +
                "PERDA10 REAL, " +
                "PERDA11 REAL, " +
                "PERDA12 REAL, " +
                "PERDA13 REAL, " +
                "PERDA14 REAL, " +
                "PERDA15 REAL, " +
                "PERDA16 REAL, " +
                "PERDA17 REAL, " +
                "PERDA18 REAL, " +
                "PERDA19 REAL, " +
                "PERDA20 REAL, " +
                "PERDA21 REAL, " +
                "PERDA22 REAL, " +
                "PERDA23 REAL, " +
                "PERDA24 REAL, " +
                "PERDA25 REAL, " +
                "MULTIPLICACAOKWH REAL, " +
                "MANUTENCAOINICIAL REAL, " +
                "MANUTENCAO REAL, " +
                "ECONOMIA REAL);");

        db.close();

        verificaBanco();
    }

    public void verificaBanco() {

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + TABELAVALORES + " ;", null);

        if (cur != null) {
            cur.moveToFirst();
            if (cur.getInt (0) == 0) {
                preencheDados();
                preencheDadosIniciais();
                preencheDadosSecundarios();
                preencheDadosPrecos();
            } else {
                Log.d("mensagem","Entrou 1");
            }
        }
        db.close();

    }
    public void preencheDadosPrecos() {
        for(int i=1;i<=27;i++){
            insere_registro_precos("","");
        }
    }

    private void insere_registro_precos(String material, String custo) {
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("MATERIAL", material);
            values.put("CUSTO", custo);

            db.insert(TABELADAPRECOS, null, values);
            db.close();
    }


    public void preencheDadosIniciais() {
        insere_registro_iniciais(0.0,0.0, 0.0,0.0,0.0,0.0, 0.0,0.0);//2333.0
    }

    public void preencheDadosSecundarios() {
        insere_registro_secundario(0.0,0.0, 0.0,0.0,0.0,0.0, 0.0,0.0);//2333.0
    }

    private void insere_registro_secundario(double FDSUJ, double FDSOMBRAPOUCA, double FDSOMBRAMUITO, double FDTELHADOSOLO, double FDTELHADOCERMETAL, double MODULODEFAULT, double MODULOMENOR, double MODULOMAIOR) {
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("FDSUJ", FDSUJ);
        values.put("FDSOMBRAPOUCA", FDSOMBRAPOUCA);
        values.put("FDSOMBRAMUITO", FDSOMBRAMUITO);
        values.put("FDTELHADOSOLO", FDTELHADOSOLO);
        values.put("FDTELHADOCERMETAL", FDTELHADOCERMETAL);
        values.put("MODULODEFAULT", MODULODEFAULT);
        values.put("MODULOMENOR", MODULOMENOR);
        values.put("MODULOMAIOR", MODULOMAIOR);

        db.insert(TABELAVALORESSECUNDARIOS, null, values);

        db.close();

    }

    public void preencheDados() {
        for(int i=1;i<=36;i++){
            insere_registro("",0, "", "", "", "", "","","","","","","","");
        }
        }

    public void insere_registro_iniciais(Double FATORMO, Double LUCRORESULTADO , Double COMICAO , Double taxaSicred, Double valorpernoite, Double valorkm , Double valorstart , Double valorplaca) {

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("CUSTOMO", FATORMO);
        values.put("LUCRORESULTADO", LUCRORESULTADO);
        values.put("COMICAO", COMICAO);
        values.put("SICRED", taxaSicred);
        values.put("VALORPERNOITE", valorpernoite);
        values.put("VALORKM", valorkm);
        values.put("VALORSTART", valorstart);
        values.put("VALORPLACA", valorplaca);

        db.insert(TABELAVALORESINICIAIS, null, values);

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values2 = new ContentValues();
        values2.put("PERDA1", 0.0);
        values2.put("PERDA2", 0.0);
        values2.put("PERDA3", 0.0);
        values2.put("PERDA4", 0.0);
        values2.put("PERDA5", 0.0);
        values2.put("PERDA6", 0.0);
        values2.put("PERDA7", 0.0);
        values2.put("PERDA8", 0.0);
        values2.put("PERDA9", 0.0);
        values2.put("PERDA10", 0.0);
        values2.put("PERDA11", 0.0);
        values2.put("PERDA12", 0.0);
        values2.put("PERDA13", 0.0);
        values2.put("PERDA14", 0.0);
        values2.put("PERDA15", 0.0);
        values2.put("PERDA16", 0.0);
        values2.put("PERDA17", 0.0);
        values2.put("PERDA18", 0.0);
        values2.put("PERDA19", 0.0);
        values2.put("PERDA20", 0.0);
        values2.put("PERDA21", 0.0);
        values2.put("PERDA22", 0.0);
        values2.put("PERDA23", 0.0);
        values2.put("PERDA24", 0.0);
        values2.put("PERDA25", 0.0);
        values2.put("MULTIPLICACAOKWH", 0.0);
        values2.put("MANUTENCAOINICIAL", 0.0);
        values2.put("MANUTENCAO", 0.0);
        values2.put("ECONOMIA", 0.0);

        db.insert(TABELAANALISEECONIMICA, null, values2);

        db.close();
    }

    public void insere_registro(String sistema, Integer ano, String area, String numero, String placa1, String placa2, String valor1, String pernoites, String startObra, String inversor, String conexao, String frete, String projeto, String sistemaFinal) {

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("SISTEMA", sistema);
        values.put("ANO", ano);
        values.put("AREA", area);
        values.put("NUMERO", numero);
        values.put("PLACA1", placa1);
        values.put("PLACA2", placa2);
        values.put("VALOR1", valor1);
        values.put("PERNOITES", pernoites);
        values.put("STARTOBRA", startObra);
        values.put("INVERSOR", inversor);
        values.put("CONEXAO", conexao);
        values.put("FRETE", frete);
        values.put("PROJETO", projeto);
        values.put("SISTEMAFINAL", sistemaFinal);

        db.insert(TABELAVALORES, null, values);
        db.close();
    }

    // Metodo que paga  a ação do botão "Logar"
    public void entrarAplicativo(View v) {
        final String codigo = edtCodigoSeguranca.getText().toString();

        if (codigo.equals("")) { // Verifica se os campos foram preenchidos
            Toast.makeText(getApplicationContext(), "Por favor, informe o código de segurança para login!", Toast.LENGTH_SHORT).show();
        } else { // se os dados foram preenchidos, chama o metodo que ira verificar se e-mail e senha exixtem no banco de dados
            if (verificaConexao()) {
                //if (hasInternetAccess() != false) {
                    getJSONLogin(codigo);
                //}else {
                    //falhaAcessoServidor();
               // }
            } else {
                naoConectou();
            }
        }
    }

    private void getJSONLogin(final String codigo){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Login.this,"Verificando Código de Segurança","Aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //JSON_STRING = s;
                //showEmployee();
                if(s.trim().equals("1")){
                    Intent it = new Intent(Login.this, Inicial.class);
                    startActivity(it);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Erro no código de acesso! Tente novamente!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                String dadosTabela1 = rh.sendGetRequest(Config.URL_GET_TABELA1);
                JSON_STRING  = dadosTabela1;
                gravaTabelaCelular();

                String dadosValores = rh.sendGetRequest(Config.URL_GET_VALORES);
                JSON_STRING2  = dadosValores;
                gravaValoresCelular();

                String dadosAnaliseEconomica = rh.sendGetRequest(Config.URL_GET_ANALISE_ECONOMICA);
                JSON_STRING3  = dadosAnaliseEconomica;
                gravaValoresAnaliseEconomica();

                String confereCodigoServidor = rh.sendGetRequestParam(Config.URL_VERIFICA_LOGIN,codigo);
                Log.d("mensagem","Aquiiiiiiii:"+confereCodigoServidor);

                String dadosValoresSecundarios= rh.sendGetRequest(Config.URL_GET_VALORES_SECUNDARIOS);
                JSON_STRING4  = dadosValoresSecundarios;
                //atualiza_valores_secundarios();
                gravaValoresSecundarios();

                String dadosPrecos = rh.sendGetRequest(Config.URL_GET_PRECOS);
                JSON_STRING5  = dadosPrecos;
                gravaPrecos();

                return confereCodigoServidor;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void gravaPrecos(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING5);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY5);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                String id = jo.getString(Config.TAG_ID);
                String material = jo.getString(Config.TAG_MATERIAL);
                String custo = jo.getString(Config.TAG_CUSTO);


                Integer intId = Integer.parseInt(id);

                atualiza_precos(material,custo, intId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void atualiza_precos(String material, String custo, Integer intId) {

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values2 = new ContentValues();
        values2.put("MATERIAL", material);
        values2.put("CUSTO", custo);

        Log.d("retorno","Material: "+material);
        Log.d("retorno","Custo: "+custo);

        db.update(TABELADAPRECOS, values2, "ID=" + intId, null);
        db.close();
    }

    private void gravaTabelaCelular(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                String id = jo.getString(Config.TAG_ID);
                String sistema = jo.getString(Config.TAG_SISTEMA);
                String ano = jo.getString(Config.TAG_ANO);
                String area = jo.getString(Config.TAG_AREA);
                String numero = jo.getString(Config.TAG_NUMERO);
                String placa1 = jo.getString(Config.TAG_PLACA1);
                String placa2 = jo.getString(Config.TAG_PLACA2);
                String valor1 = jo.getString(Config.TAG_VALOR1);
                String pernoites = jo.getString(Config.TAG_PERNOITES);
                String starobra = jo.getString(Config.TAG_STARTOBRA);
                String inversor = jo.getString(Config.TAG_INVERSOR);
                String conexao = jo.getString(Config.TAG_CONEXAO);
                String frete = jo.getString(Config.TAG_FRETE);
                String projeto = jo.getString(Config.TAG_PROJETO);
                String sistemafinal = jo.getString(Config.TAG_SISTEMAFINAL);
                String quntInversores = jo.getString(Config.TAG_QNTINVERSORES);

                Integer intAno = Integer.parseInt(ano);
                Integer intId = Integer.parseInt(id);

                Log.d("mensagem","Ano 1: "+intId);
                Log.d("mensagem","Ano 2: "+sistema);
                Log.d("mensagem","Ano 3: "+intAno);
                Log.d("mensagem","Ano 4: "+area);
                Log.d("mensagem","Ano 5: "+numero);
                Log.d("mensagem","Ano 6: "+placa2);
                Log.d("mensagem","Ano 7: "+placa1);
                Log.d("mensagem","Ano 8: "+valor1);
                Log.d("mensagem","Ano 9: "+pernoites);
                Log.d("mensagem","Ano 10: "+starobra);
                Log.d("mensagem","Ano 11: "+inversor);
                Log.d("mensagem","Ano 12: "+conexao);
                Log.d("mensagem","Ano 13: "+frete);
                Log.d("mensagem","Ano 14: "+projeto);
                Log.d("mensagem","Ano 15: "+sistemafinal);
                Log.d("mensagem","Inversores: "+quntInversores);

                atualiza_tabela1(sistema,intAno, area, numero, placa1, placa2, valor1,pernoites,starobra,inversor,conexao,frete,projeto,sistemafinal,quntInversores,intId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void gravaValoresSecundarios(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING4);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY4);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                String id = jo.getString(Config.TAG_ID);
                Double fdsuj = jo.getDouble(Config.TAG_FDSUJ);
                Double fdpoucasombra = jo.getDouble(Config.TAG_FDSOMBRAPOUCA);
                Double fdmuitasombra = jo.getDouble(Config.TAG_FDSOMBRAMUITO);
                Double fdtelhadosolo = jo.getDouble(Config.TAG_FDTELHADOSOLO);

                Double fdtelhadometal = jo.getDouble(Config.TAG_FDTELHADOCERMETAL);
                Double modulodefault = jo.getDouble(Config.TAG_MODULODEFAULT);
                Double medulomenor = jo.getDouble(Config.TAG_MODULOMENOR);
                Double modulomaior = jo.getDouble(Config.TAG_MODULOMAIOR);

                Integer intId = Integer.parseInt(id);
                atualiza_valores_secundarios(fdsuj,fdpoucasombra,fdmuitasombra,fdtelhadosolo,fdtelhadometal,modulodefault,medulomenor,modulomaior,intId);
                //atualiza_valores(customo,lucroresultado, comicao, sicred, valorpernoite, valorkm, valorstart, valorplaca, intId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void gravaValoresCelular(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING2);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY2);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                String id = jo.getString(Config.TAG_ID);
                Double customo = jo.getDouble(Config.TAG_CUSTOMO);
                Double lucroresultado = jo.getDouble(Config.TAG_LUCRORESULTADO);
                Double comicao = jo.getDouble(Config.TAG_COMICAO);
                Double sicred = jo.getDouble(Config.TAG_SICRED);

                Double valorpernoite = jo.getDouble(Config.TAG_VALORPERNOITE);
                Double valorkm = jo.getDouble(Config.TAG_VALORKM);
                Double valorstart = jo.getDouble(Config.TAG_VALORSTART);
                Double valorplaca = jo.getDouble(Config.TAG_VALORPLCA);

                Integer intId = Integer.parseInt(id);

                Log.d("mensagem","Ano 1: "+intId);
                Log.d("mensagem","Ano 2: "+customo);
                Log.d("mensagem","Ano 3: "+lucroresultado);
                Log.d("mensagem","Ano 4: "+comicao);
                Log.d("mensagem","Ano 5: "+sicred);

                Log.d("mensagem","Ano 6: "+valorpernoite);
                Log.d("mensagem","Ano 7: "+valorkm);
                Log.d("mensagem","Ano 8: "+valorstart);
                Log.d("mensagem","Ano 9: "+valorplaca);

                atualiza_valores(customo,lucroresultado, comicao, sicred, valorpernoite, valorkm, valorstart, valorplaca, intId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void gravaValoresAnaliseEconomica(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(JSON_STRING3);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY3);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);

                String id = jo.getString(Config.TAG_ID);
                Double perda2 = jo.getDouble(Config.TAG_PERDA2);
                Double perda3 = jo.getDouble(Config.TAG_PERDA3);
                Double perda4 = jo.getDouble(Config.TAG_PERDA4);
                Double perda5 = jo.getDouble(Config.TAG_PERDA5);
                Double perda6 = jo.getDouble(Config.TAG_PERDA6);
                Double perda7 = jo.getDouble(Config.TAG_PERDA7);
                Double perda8 = jo.getDouble(Config.TAG_PERDA8);
                Double perda9 = jo.getDouble(Config.TAG_PERDA9);
                Double perda10 = jo.getDouble(Config.TAG_PERDA10);
                Double perda11 = jo.getDouble(Config.TAG_PERDA11);
                Double perda12 = jo.getDouble(Config.TAG_PERDA12);
                Double perda13 = jo.getDouble(Config.TAG_PERDA13);
                Double perda14 = jo.getDouble(Config.TAG_PERDA14);
                Double perda15 = jo.getDouble(Config.TAG_PERDA15);
                Double perda16 = jo.getDouble(Config.TAG_PERDA16);
                Double perda17 = jo.getDouble(Config.TAG_PERDA17);
                Double perda18 = jo.getDouble(Config.TAG_PERDA18);
                Double perda19 = jo.getDouble(Config.TAG_PERDA19);
                Double perda20 = jo.getDouble(Config.TAG_PERDA20);
                Double perda21 = jo.getDouble(Config.TAG_PERDA21);
                Double perda22 = jo.getDouble(Config.TAG_PERDA22);
                Double perda23 = jo.getDouble(Config.TAG_PERDA23);
                Double perda24 = jo.getDouble(Config.TAG_PERDA24);
                Double perda25 = jo.getDouble(Config.TAG_PERDA25);


                Double kwh = jo.getDouble(Config.TAG_KWH);
                Double manutIniciail = jo.getDouble(Config.TAG_MANUTENCAO_INICIAL);
                Double manut = jo.getDouble(Config.TAG_MANUTENCAO);
                Double economia = jo.getDouble(Config.TAG_ECONOMIA);

                Integer intId = Integer.parseInt(id);

                Log.d("mensagem","Perda ID: "+intId);
                Log.d("mensagem","Perda 2: "+perda2);
                Log.d("mensagem","Perda 3: "+perda2);
                Log.d("mensagem","Perda 4: "+perda2);
                Log.d("mensagem","Perda 5: "+perda2);
                Log.d("mensagem","Perda 6: "+perda2);
                Log.d("mensagem","Perda 7: "+perda2);
                Log.d("mensagem","Perda 8: "+perda2);
                Log.d("mensagem","Perda 9: "+perda2);
                Log.d("mensagem","Perda 10: "+perda2);
                Log.d("mensagem","Perda 11: "+perda2);
                Log.d("mensagem","Perda 12: "+perda2);
                Log.d("mensagem","Perda 13: "+perda2);
                Log.d("mensagem","Perda 14: "+perda2);
                Log.d("mensagem","Perda 15: "+perda2);
                Log.d("mensagem","Perda 16: "+perda2);
                Log.d("mensagem","Perda 17: "+perda2);
                Log.d("mensagem","Perda 18: "+perda2);
                Log.d("mensagem","Perda 19: "+perda2);
                Log.d("mensagem","Perda 20: "+perda2);
                Log.d("mensagem","Perda 21: "+perda2);
                Log.d("mensagem","Perda 22: "+perda2);
                Log.d("mensagem","Perda 23: "+perda2);
                Log.d("mensagem","Perda 24: "+perda2);
                Log.d("mensagem","Perda 25: "+perda2);

                Log.d("mensagem","KWh: "+kwh);
                Log.d("mensagem","Inicial: "+manutIniciail);
                Log.d("mensagem","Manutenção: "+manut);
                Log.d("mensagem","Economia: "+economia);

                atualiza_analise_economica(perda2, perda3, perda4, perda5, perda6, perda7, perda8, perda9,perda10,perda11, perda12, perda13, perda14, perda15, perda16, perda17, perda18, perda19, perda20, perda21, perda22,perda23,perda24, perda25, kwh, manutIniciail, manut, economia,intId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void atualiza_analise_economica(Double perda2, Double perda3, Double perda4, Double perda5, Double perda6, Double perda7, Double perda8, Double perda9, Double perda10, Double perda11, Double perda12, Double perda13, Double perda14, Double perda15, Double perda16, Double perda17, Double perda18, Double perda19, Double perda20, Double perda21, Double perda22, Double perda23, Double perda24, Double perda25, Double kwh, Double manutIniciail, Double manut, Double economia, Integer intId) {
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values2 = new ContentValues();
        values2.put("PERDA2", perda2);
        values2.put("PERDA3", perda3);
        values2.put("PERDA4", perda4);
        values2.put("PERDA5", perda5);
        values2.put("PERDA6", perda6);
        values2.put("PERDA7", perda7);
        values2.put("PERDA8", perda8);
        values2.put("PERDA9", perda9);
        values2.put("PERDA10", perda10);
        values2.put("PERDA11", perda11);
        values2.put("PERDA12", perda12);
        values2.put("PERDA13", perda13);
        values2.put("PERDA14", perda14);
        values2.put("PERDA15", perda15);
        values2.put("PERDA16", perda16);
        values2.put("PERDA17", perda17);
        values2.put("PERDA18", perda18);
        values2.put("PERDA19", perda19);
        values2.put("PERDA20", perda20);
        values2.put("PERDA21", perda21);
        values2.put("PERDA22", perda22);
        values2.put("PERDA23", perda23);
        values2.put("PERDA24", perda24);
        values2.put("PERDA25", perda25);
        values2.put("MULTIPLICACAOKWH", kwh);
        values2.put("MANUTENCAOINICIAL", manutIniciail);
        values2.put("MANUTENCAO", manut);
        values2.put("ECONOMIA", economia);

        db.update(TABELAANALISEECONIMICA, values2, "ID=" + intId, null);
        db.close();
    }

    public void atualiza_tabela1(String sistema, Integer ano, String area, String numero, String placa1, String placa2, String valor1, String pernoites, String startObra, String inversor, String conexao, String frete, String projeto, String sistemaFinal, String quntInversores, Integer id) {

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("SISTEMA", sistema);
        values.put("ANO", ano);
        values.put("AREA", area);
        values.put("NUMERO", numero);
        values.put("PLACA1", placa1);
        values.put("PLACA2", placa2);
        values.put("VALOR1", valor1);
        values.put("PERNOITES", pernoites);
        values.put("STARTOBRA", startObra);
        values.put("INVERSOR", inversor);
        values.put("CONEXAO", conexao);
        values.put("FRETE", frete);
        values.put("PROJETO", projeto);
        values.put("SISTEMAFINAL", sistemaFinal);
        values.put("QNT", quntInversores);

        db.update(TABELAVALORES, values, "ID=" + id, null);
        db.close();
    }

    public void atualiza_valores(Double customo, Double lucroresultado, Double comicao, Double sicred, Double valorpernoite, Double valorkm, Double valorstart, Double valorplaca, Integer id) {

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("CUSTOMO", customo);
        values.put("LUCRORESULTADO", lucroresultado);
        values.put("COMICAO", comicao);
        values.put("SICRED", sicred);
        values.put("VALORPERNOITE", valorpernoite);
        values.put("VALORKM", valorkm);
        values.put("VALORSTART", valorstart);
        values.put("VALORPLACA", valorplaca);

        db.update(TABELAVALORESINICIAIS, values, "ID=" + id, null);
        db.close();
    }

    public void atualiza_valores_secundarios(Double fdsuj, Double fdpoucasombra, Double fdmuitasombra, Double fdtelhadosolo, Double fdtelhadometal, Double modulodefault, Double medulomenor, Double modulomaior, Integer intId) {
        //

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("FDSUJ", fdsuj);
        values.put("FDSOMBRAPOUCA", fdpoucasombra);
        values.put("FDSOMBRAMUITO", fdmuitasombra);
        values.put("FDTELHADOSOLO", fdtelhadosolo);
        values.put("FDTELHADOCERMETAL", fdtelhadometal);
        values.put("MODULODEFAULT", modulodefault);
        values.put("MODULOMENOR", medulomenor);
        values.put("MODULOMAIOR", modulomaior);

        db.update(TABELAVALORESSECUNDARIOS, values, "ID=" + intId, null);
        //db.insert(TABELAVALORESSECUNDARIOS, null, values);

        db.close();
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
        alertDialogBuilder.setMessage(Html.fromHtml("<font color='#FF0000'>Para logar neste aplicativo, seu celular precisa estar conectado a internet.\n\n Por Favor!\n Conecte-se a internet e tente novamente!</font>"));


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
        ConnectivityManager connectivityManager = (ConnectivityManager) Login.this.getSystemService(CONNECTIVITY_SERVICE);
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


}
