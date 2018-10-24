package br.desenvolvedor.michelatz.aplcativosolar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Proposta2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView ;
    private ArrayList<DadosGerais> itens;
    private AdapterListViewGeral adapterListViewConsumo;

    SQLiteDatabase db;
    String BANCO = "banco.db";
    String TABELACONSUMOMES = "consumomes";
    String TABELAPROPOSTA = "proposta";
    private String idProposta;
    public static final String idPropostaPref = "idPropostaKey";
    int valoresKWHToatal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposta2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("HCC SOLAR");
        toolbar.setSubtitle("Proposta - Passo 2");

        SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
        idProposta = sharedpreferences.getString("idPropostaKey", null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.listViewDadosUnidade);

        inflaListaConsumoMes();
    }

    private List<Dados> gerarZombies() {
        List<Dados> zombies = new ArrayList<Dados>();
        zombies.add(criarZombie("Dados 1"));
        zombies.add(criarZombie("Dados 2"));
        zombies.add(criarZombie("Dados 3"));

        return zombies;
    }

    private Dados criarZombie(String nome) {
        Dados zombie = new Dados(nome);
        return zombie;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent it = new Intent(this, Proposta1.class);
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
            values.put("REPRESENTANTE", "");
            values.put("COMISSAO", "");
            values.put("DESCONTO", "");
            values.put("VERIFICACAO", "");
            values.put("FRETE", "");
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
            Intent it = new Intent(this, Proposta1.class);
            startActivity(it);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void adicionarConsumoMes(View v) {
        Intent it = new Intent(this, AdicionaConsumoMes.class);
        startActivity(it);
        finish();
    }

    public void irNovaProposta3(View v) {
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        Cursor linhas = db.rawQuery("SELECT ID FROM " + TABELACONSUMOMES+ " WHERE IDPROPOSTA = "+ idProposta +";", null);

        Cursor linhas2 = db.rawQuery("SELECT ID FROM " + TABELACONSUMOMES+ " WHERE UNIDADE = '1' AND IDPROPOSTA = "+ idProposta +";", null);

        int totalDB = linhas2.getCount();
        int contador = totalDB;

        if (linhas.moveToFirst() && linhas.getCount() > 0) {
            if(valoresKWHToatal > 34992){
                buildDialog(Proposta2.this).show();
            }else{
                if(contador == 0){
                    Toast.makeText(getApplicationContext(), "Pelo menos 1 Consumo deve ser marcado como unidade geradora!", Toast.LENGTH_SHORT).show();
                }else if(contador == 1){
                    Intent it = new Intent(this, Proposta3.class);
                    startActivity(it);
                    finish();
                }else if(contador > 1){
                    Toast.makeText(getApplicationContext(), "Apenas 1 Consumo deve ser marcado como unidade geradora!", Toast.LENGTH_SHORT).show();
                }
            }

        } else{
            Toast.makeText(getApplicationContext(), "Preencha pelo menos 1 Consumo de Mês!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Restrição");
        builder.setMessage("Este Aplicativo esta condicionado a executar somente com  valores menores que 34992 KW/h!");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }

    private void inflaListaConsumoMes() {
        itens = new ArrayList<DadosGerais>();

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);

        Cursor linhas = db.rawQuery("SELECT *  FROM " + TABELACONSUMOMES+ " WHERE IDPROPOSTA = "+ idProposta +";", null);
        int contador = 0;
        if (linhas.moveToFirst()) {
            do {
                contador++;
                String idConsumo = linhas.getString(0);
                int valores = Integer.parseInt(linhas.getString(1))+Integer.parseInt(linhas.getString(2))+ Integer.parseInt(linhas.getString(3))+Integer.parseInt(linhas.getString(4))+Integer.parseInt(linhas.getString(5))+Integer.parseInt(linhas.getString(6))+Integer.parseInt(linhas.getString(7))+Integer.parseInt(linhas.getString(8))+Integer.parseInt(linhas.getString(9))+Integer.parseInt(linhas.getString(10))+Integer.parseInt(linhas.getString(11))+Integer.parseInt(linhas.getString(12));
                String unidade = " ";
                if(linhas.getString(14).equals("1")){
                    unidade = " - UG";
                }


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

                NumberFormat formatoComPontoFinal = NumberFormat.getNumberInstance();

                String texto = "Consumo " + contador+" - " +formatoComPontoFinal.format(valores)+" kW/h"+unidade; //valores
                DadosGerais item1 = new DadosGerais(idConsumo, texto);
                itens.add(item1);
                valoresKWHToatal = valoresKWHToatal+valores;
            }
            while (linhas.moveToNext());
        }
        adapterListViewConsumo = new AdapterListViewGeral(this, itens);
        listView.setAdapter(adapterListViewConsumo);
        listView.setCacheColorHint(Color.TRANSPARENT);

        linhas.close();
        Helper.getListViewSize(listView);
        db.close();
    }

    public void deletaItem(View v) {
        adapterListViewConsumo.removeItem((Integer) v.getTag());
        adapterListViewConsumo.notifyDataSetChanged();
        String idMensagem = adapterListViewConsumo.idSelecionado;
        confirmarDelete(idMensagem);
    }

    public void editaItem(View v) {
        adapterListViewConsumo.editaItem((Integer) v.getTag());
        final String idMensagem = adapterListViewConsumo.idSelecionado;

        Intent intent = new Intent(this, AdicionaConsumoMes.class);
        intent.putExtra("ACAO", "EDITAR");
        intent.putExtra("IDCONSUMO", idMensagem);
        this.startActivity(intent);
        finish();

    }

    private void confirmarDelete(final String idMensagem) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Tem certeza que deseja deletar estes dados de Consumo?");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletarMensagem(idMensagem);
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deletarMensagem(final String idMens) {
        int idExcluido = Integer.parseInt(idMens.toString());
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        db.execSQL("DELETE FROM " + TABELACONSUMOMES + " WHERE ID = " + idExcluido + "");
        db.close();

        inflaListaConsumoMes();
    }

}
