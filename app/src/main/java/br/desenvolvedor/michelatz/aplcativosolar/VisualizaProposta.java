package br.desenvolvedor.michelatz.aplcativosolar;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class VisualizaProposta extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtMediaMes, txtConsumoAno, txtEconomiaAno, txtUltimoValorMedio, txtKWinstalado, txtValor, txtPlaca1, txtNumero, txtArea, txtAno, txtSistema;
    private TextView txtPrimeiraParcelaHCC, txtSegundaParcelaHCC, txtTerceiraParcelaHCC, txtParcelasSicred;
    SQLiteDatabase db;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    ProgressDialog progress;
    private File pdfFile;
    String BANCO = "banco.db";
    String TABELACONSUMOMES = "consumomes";
    String TABELAPROPOSTA = "proposta";
    String TABELAVALORES = "valores";
    String TABELAVALORESINICIAIS = "valoresiniciais";
    String TABELAANALISEECONIMICA = "analiseeconomica";
    double mediaGrafico = 0.0;
    private String idProposta;
    public static final String idPropostaPref = "idPropostaKey";
    private String nomeCliente="", data="", proposta="", endereco="", tipoTelhado="";
    private String consumoAnoFinal="", sistemaFinal="", anoFinal="", areaFinal="", numeroFinal="", placa1Final=""; //mediaMesFinal=""
    private String placa2Final="", valor1Final="", pernoiteFinal="", startObraFinal="", inversorFinal="", conexaoFinal="", freteFinal="";
    private String projetoFinal="", sistema2Final="", economiaAnoFinal="", ultimoValorMedioFinal="", KWinstaladoFinal="", valorFinal="";
    private double valorPernoite, valorKM, valorStart, precoPlaca, valorPlaca, valorTaxaSicred, valorPernoiteTotal, valorKMTotal, valorStartTotal, valorPlacaTotal, valorMaoObra, instalacaoPlaca, maoObraTotal, custoObra , custoResultado, resultado, porcentagemDesconto, desconto, propostaMenosDesconto, acrescimo, propostaMaisComissao, deslocamento, comissao, KWinstalado, sistemaDouble, diferencaConsumoGeracao, pagamentoHCC40, pagamentoHCC30, pagamentoProprio35, pagamentoProprio65, parcelaSicred;
    private double mediaMedicaoAno, calculoEconomia;
    double parcelaAno1;
    double economiaMensalAno1;
    double economiaMensalAno2;
    double economiaMensalAno3;
    double economiaMensalAno4;
    double economiaMensalAno5;

    String textoUnidadeConsumidora="";

    double economiaMesAno1;
    double economiaMesAno2;
    double economiaMesAno3;
    double economiaMesAno4;
    double economiaMesAno5;

    int carregamentoGrafico1 = 0, carregamentoGrafico2 = 0, carregamentoGrafico3 = 0, carregamentoGrafico4 = 0;

    double saldoAcumuladoAno1, saldoAcumuladoAno2, saldoAcumuladoAno3, saldoAcumuladoAno4, saldoAcumuladoAno5, saldoAcumuladoAno6;
    double saldoAcumuladoAno7, saldoAcumuladoAno8, saldoAcumuladoAno9, saldoAcumuladoAno10, saldoAcumuladoAno11, saldoAcumuladoAno12;
    double saldoAcumuladoAno13, saldoAcumuladoAno14, saldoAcumuladoAno15, saldoAcumuladoAno16, saldoAcumuladoAno17, saldoAcumuladoAno18;
    double saldoAcumuladoAno19, saldoAcumuladoAno20 , saldoAcumuladoAno21, saldoAcumuladoAno22, saldoAcumuladoAno23 , saldoAcumuladoAno24;

    int inteiroEnergiaProduzida25Anos;
    Double EconomiaDeEnergiaProduzida25Anos;

    int contadorPayback = 0;
    int contadorPayback2 = 0;

    private String quantidadeInversores;

    private int mediaMes1, mediaMes2, mediaMes3, mediaMes4, mediaMes5, mediaMes6, mediaMes7, mediaMes8, mediaMes9, mediaMes10, mediaMes11, mediaMes12;
    private int totalMeses, mediaAnual, consumoXgeracao, deslocamentoKM;

    private double custoMO, lucroResultado, valorComissao, taxaSicred;

    ProgressDialog dialog2;
    double saldoAcumuladoAno25;
    double saldoAcumulado2Ano25;
    double calculoGraficoLaranja = 0.0;
    double calculoGraficoAzul = 0.0;

    BarChart barChart;
    private ProgressDialog pd = null;
    //PieChart piechart;
    WebView wvGrafico, wvGrafico2, wvGrafico3, wvGrafico4, wvGrafico5;
    String strURL, strURL2, strURL3, strURL4, strURL5;
    public static final String ASSET_PATH = "file:///android_asset/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //conditional to check network
        if(!isConnected(VisualizaProposta.this)) {
            buildDialog(VisualizaProposta.this).show();
        }
        setContentView(R.layout.activity_visualiza_proposta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("HCC SOLAR");
        toolbar.setSubtitle("Proposta");
        setSupportActionBar(toolbar);

        SharedPreferences sharedpreferences = getSharedPreferences(Inicial.MyPREFERENCES, Context.MODE_PRIVATE);
        idProposta = sharedpreferences.getString("idPropostaKey", null);

        txtMediaMes = (TextView) findViewById(R.id.txtMediaMes);
        txtConsumoAno = (TextView) findViewById(R.id.txtConsumoAno);
        txtEconomiaAno = (TextView) findViewById(R.id.txtEconomiaAno);
        txtUltimoValorMedio = (TextView) findViewById(R.id.txtUltimoValorMedio);
        txtKWinstalado = (TextView) findViewById(R.id.txtKWinstalado);
        txtValor = (TextView) findViewById(R.id.txtValor);
        txtPlaca1 = (TextView) findViewById(R.id.txtPlaca1);
        txtNumero = (TextView) findViewById(R.id.txtNumero);
        txtArea = (TextView) findViewById(R.id.txtArea);
        txtAno = (TextView) findViewById(R.id.txtAno);
        txtSistema = (TextView) findViewById(R.id.txtSistema);

        txtParcelasSicred = (TextView) findViewById(R.id.txtParcelasSicred);

        //txtSegundaParcelaPropria = (TextView) findViewById(R.id.txtSegundaParcelaPropria);
        //txtPrimeiraParcelaPropria = (TextView) findViewById(R.id.txtPrimeiraParcelaPropria);

        txtPrimeiraParcelaHCC = (TextView) findViewById(R.id.txtPrimeiraParcelaHCC);
        txtSegundaParcelaHCC = (TextView) findViewById(R.id.txtSegundaParcelaHCC);
        txtTerceiraParcelaHCC = (TextView) findViewById(R.id.txtTerceiraParcelaHCC);

        wvGrafico = (WebView)findViewById(R.id.wvGrafico);
        WebSettings webSettings = wvGrafico.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvGrafico2 = (WebView)findViewById(R.id.wvGrafico2);
        wvGrafico3 = (WebView)findViewById(R.id.wvGrafico3);
        wvGrafico4 = (WebView)findViewById(R.id.wvGrafico4);

        wvGrafico.clearFocus();
        wvGrafico2.clearFocus();
        wvGrafico3.clearFocus();
        wvGrafico4.clearFocus();

        //new ConsultaXML().execute();

        calculaMediaMes();
        calculaTabelaAnaliseEconomicaHCC();
        geraGrafico1();
        geraGrafico2();
        geraGrafico3();
        geraGrafico4();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //Check INTERNET connectivity
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Sem conexão de Internet");
        builder.setMessage("Ative os Dados móveis ou acesse a rede WIFI para continuar!");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(VisualizaProposta.this, Proposta3.class);
                startActivity(it);
                finish();
            }
        });

        return builder;
    }

    private void geraGrafico1(){
        double taxaEnergiaGerada1 = 10.675763482781, taxaEnergiaGerada2 = 9.07053642336293, taxaEnergiaGerada3 = 9.34185257382138, taxaEnergiaGerada4 = 7.873655331745, taxaEnergiaGerada5 = 5.80463504440113, taxaEnergiaGerada6 = 4.49859215941087;
        double taxaEnergiaGerada7 = 4.95068948090391, taxaEnergiaGerada8 = 7.11493754963541, taxaEnergiaGerada9 = 7.84708685293481, taxaEnergiaGerada10 = 9.57237744567179, taxaEnergiaGerada11 = 11.7327268789257, taxaEnergiaGerada12 = 11.5217673814165;

        Log.d("mensagem"," media do mes: "+ mediaMes1);
        Log.d("mensagem"," media do mes 2: "+ mediaMes2);
        Log.d("mensagem"," media do mes 3: "+ mediaMes3);
        Log.d("mensagem"," media do mes 4: "+ mediaMes4);
        Log.d("mensagem"," media do mes 5: "+ mediaMes5);
        Log.d("mensagem"," media do mes 6: "+ mediaMes6);
        Log.d("mensagem"," media do mes 7: "+ mediaMes7);
        Log.d("mensagem"," media do mes 8: "+ mediaMes8);
        Log.d("mensagem"," media do mes 9: "+ mediaMes9);
        Log.d("mensagem"," media do mes 10: "+ mediaMes10);
        Log.d("mensagem"," media do mes 11: "+ mediaMes11);
        Log.d("mensagem"," media do mes 12: "+ mediaMes12);

        Log.d("mensagem"," Ano final: "+ anoFinal);
        Log.d("mensagem"," Grafico: "+ Double.parseDouble(anoFinal)*taxaEnergiaGerada1);


        String content = "<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
                + "      google.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "        var data = google.visualization.arrayToDataTable(["
                + "          ['Year', 'Consumo (kWh)', 'Energia Gerada (kWh)'],"
                + "          [' Jan',  "+mediaMes1+" ,      "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada1/100)+"]," //(anoFinal*taxaEnergiaGerada1)
                + "          [' Fev',  "+mediaMes2+" ,      "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada2/100)+"],"
                + "          [' Mar',  "+mediaMes3+" ,       "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada3/100)+"],"
                + "          [' Abr',  "+mediaMes4+" ,      "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada4/100)+"],"
                + "          [' Mai',  "+mediaMes5+" ,      "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada5/100)+"],"
                + "          [' Jun',  "+mediaMes6+" ,       "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada6/100)+"],"
                + "          [' Jul',  "+mediaMes7+" ,      "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada7/100)+"],"
                + "          [' Ago',  "+mediaMes8+" ,      "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada8/100)+"],"
                + "          [' Set',  "+mediaMes9+" ,       "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada9/100)+"],"
                + "          [' Out',  "+mediaMes10+" ,      "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada10/100)+"],"
                + "          [' Nov',  "+mediaMes11+" ,      "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada11/100)+"],"
                + "          [' Dez',  "+mediaMes12+" ,     "+Double.parseDouble(anoFinal)*(taxaEnergiaGerada12/100)+"]"
                + "        ]);"
                + "        var options = {"
                + "          legend: { position: 'top', alignment: 'center', textStyle: {fontSize: 7, color: '#5a9bd5', bold:'true'} },"
                +"           colors: ['#5a9bd5', '#e97f35'],"
                //+ "          hAxis: {textStyle: {fontSize: 4}}," //slantedText: true, slantedTextAngle: 30,
                + "          hAxis: {title: 'Análise Comparativa Consumo X Geração',titleTextStyle: {fontSize: 8, color: '#5a9bd5', bold:'true'},textStyle: {fontSize: 4}}," //, titleTextStyle: {color: 'red'}
                + "          vAxis: {textStyle: {fontSize: 6}},"
                + "          chartArea:{left:40, right:5, bottom:19, top:10.5}" // bottom:10, top:11
                + "        };"
                + "        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "    <div id=\"chart_div\" style=\"width: 100%; height: 130px;\"></div>"
                + "  </body>"
                + "</html>";

        //wvGrafico.requestFocusFromTouch();
        wvGrafico.loadDataWithBaseURL( "file:///android_asset/", content, "text/html", "utf-8", null );
        //wvGrafico.setVisibility(View.GONE);

        wvGrafico.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                carregamentoGrafico1 = 1;
            }
        });

    }

    private void geraGrafico2(){
        Log.d("mensagem","Grafico Azul: "+ calculoGraficoAzul);
        Log.d("mensagem","Grafico Laranja: "+ calculoGraficoLaranja);

        BigDecimal valorAzul = new BigDecimal(calculoGraficoAzul).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal valorLaranja = new BigDecimal(calculoGraficoLaranja).setScale(2, RoundingMode.HALF_EVEN);

        DecimalFormat formatacao = new DecimalFormat("###,###,###.00");

        String content2 = "<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
                + "      google.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "      var data = google.visualization.arrayToDataTable([\n" +
                "         ['Element', 'Density', { role: 'style' }, { role: 'annotation' } ],\n" +
                "         ['', "+calculoGraficoAzul+", '#5a9bd5', 'R$ "+formatacao.format(calculoGraficoAzul)+"' ],\n" +
                //"         ['Silver', 10.49, 'silver', 'Ag' ],\n" +
                //"         ['Gold', 19.30, 'gold', 'Au' ],\n" +
                "         ['', "+calculoGraficoLaranja+", 'color: #e97f35', 'R$ "+formatacao.format(calculoGraficoLaranja)+"' ]\n" +
                "      ]);"
                //+ "        var data = google.visualization.arrayToDataTable(["
                //+ "          ['Year', 'Valor conta de energia','Valor após a instalação'],"
                //+ "          ['',  "+calculoGraficoAzul+",   "+calculoGraficoLaranja+"],"
                //+ "        ]);"
                + "        var options = {"
                + "          legend: { position: 'none', alignment: 'center', textStyle: {fontSize: 7, color: '#5a9bd5', bold:'true'} },"
                + "          colors: ['#5a9bd5',  '#e97f35'],"
                + "          vAxis: {title: 'R$',titleTextStyle: {color: '#5a9bd5', bold:'true'}},"
                /*
                + "              vAxis: {"
                + "                 title: '', titleTextStyle: {color: '#5a9bd5', bold:'true'},"
                + "              },"
                */
                + "          hAxis: {title: 'Estimativa de Economia',titleTextStyle: {color: '#5a9bd5', bold:'true'}},"
                + "          chartArea:{left:50, right:5, top:5, bottom:15}" // bottom:10, top:11
                + "        };"
                + "        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "    <div id=\"chart_div\" style=\"width: 100%; height: 100px;\"></div>"
                + "  </body>"
                + "</html>";
        WebSettings webSettings3 = wvGrafico2.getSettings();
        webSettings3.setJavaScriptEnabled(true);
        //wvGrafico2.requestFocusFromTouch();
        wvGrafico2.loadDataWithBaseURL( "file:///android_asset/", content2, "text/html", "utf-8", null );

        wvGrafico2.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                carregamentoGrafico2 = 1;
            }
        });

    }

    private void geraGrafico3(){
        Calendar cal = Calendar.getInstance();
        int ano, ano2, ano3, ano4, ano5;
        ano = cal.get(Calendar.YEAR);
        ano2 = ano+1;
        ano3 = ano+2;
        ano4 = ano+3;
        ano5 = ano+4;

        Long economiamensal1 = (long) economiaMensalAno1;
        Long economiamensal2 = (long) economiaMensalAno2;
        Long economiamensal3 = (long) economiaMensalAno3;
        Long economiamensal4 = (long) economiaMensalAno4;
        Long economiamensal5 = (long) economiaMensalAno5;

        Long parcela = (long) parcelaAno1;

        Long economiames1 = (long) economiaMesAno1;
        Long economiames2 = (long) economiaMesAno2;
        Long economiames3 = (long) economiaMesAno3;
        Long economiames4 = (long) economiaMesAno4;
        Long economiames5 = (long) economiaMesAno5;

        Log.d("mensagem","Economia Mensal 1: "+ economiamensal1);
        Log.d("mensagem","Economia Mensal 2: "+ economiamensal2);
        Log.d("mensagem","Economia Mensal 3: "+ economiamensal3);
        Log.d("mensagem","Economia Mensal 4: "+ economiamensal4);
        Log.d("mensagem","Economia Mensal 5: "+ economiamensal5);

        Log.d("mensagem","Parcela Ano 1: "+ parcela);

        Log.d("mensagem","Economia mensal 1: "+ economiames1);
        Log.d("mensagem","Economia mensal 2: "+ economiames2);
        Log.d("mensagem","Economia mensal 3: "+ economiames3);
        Log.d("mensagem","Economia mensal 4: "+ economiames4);
        Log.d("mensagem","Economia mensal 5: "+ economiames5);

        //DecimalFormat formatoDuasCasasDecimais = new DecimalFormat("#######.##");
        //formatoDuasCasasDecimais.format(saldo2Ano1).replace(",", ".")
/*
        double parcela = Double.parseDouble(NumberFormat.getNumberInstance().format(parcelaAno1));

        double economiames1 = Double.parseDouble(NumberFormat.getNumberInstance().format(economiaMesAno1));
        double economiames2 = Double.parseDouble(NumberFormat.getNumberInstance().format(economiaMesAno2));
        double economiames3 = Double.parseDouble(NumberFormat.getNumberInstance().format(economiaMesAno3));
        double economiames4 = Double.parseDouble(NumberFormat.getNumberInstance().format(economiaMesAno4));
        double economiames5 = Double.parseDouble(NumberFormat.getNumberInstance().format(economiaMesAno5));

        Log.d("mensagem","Ano: "+ ano);
        Log.d("mensagem","Economia Mensal 1: "+ economiaMensalAno1);
        Log.d("mensagem","Economia Mensal 2: "+ economiaMensalAno2);
        Log.d("mensagem","Economia Mensal 3: "+ economiaMensalAno3);
        Log.d("mensagem","Economia Mensal 4: "+ economiaMensalAno4);
        Log.d("mensagem","Economia Mensal 5: "+ economiaMensalAno5);

        Log.d("mensagem","Parcela Ano 1: "+ parcelaAno1);

        Log.d("mensagem","Economia mensal 1: "+ economiaMesAno1);
        Log.d("mensagem","Economia mensal 2: "+ economiaMesAno1);
        Log.d("mensagem","Economia mensal 3: "+ economiaMesAno1);
        Log.d("mensagem","Economia mensal 4: "+ economiaMesAno1);
        Log.d("mensagem","Economia mensal 5: "+ economiaMesAno1);

        DecimalFormat formatoDuasCasasDecimais = new DecimalFormat("#######.##");
        formatoDuasCasasDecimais.setRoundingMode(RoundingMode.UP);
        Long a = Long.parseLong("999999999999999999");
        //System.out.println(NumberFormat.getCurrencyInstance().format(a));
        Log.d("mensagem","Planetaaaaaaaaa: "+ NumberFormat.getCurrencyInstance().format((int)economiaMensalAno1));
        Log.d("mensagem","Planetaaaaaaaaa2: "+ NumberFormat.getIntegerInstance().format((int)economiaMensalAno1));
        Log.d("mensagem","Planetaaaaaaaaa3: "+ NumberFormat.getIntegerInstance().format(economiaMensalAno1));
        Log.d("mensagem","Planetaaaaaaaaa4: "+ NumberFormat.getNumberInstance().format(economiaMensalAno1));
*/
/*
        String content3 = " <html>\n" +
                "  <head>\n" +
                "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      google.charts.load('current', {'packages':['corechart']});" +
                "      google.charts.setOnLoadCallback(drawVisualization);" +
                "      function drawVisualization() {\n" +
                "        // Some raw data (not necessarily accurate)\n" +
                "        var data = google.visualization.arrayToDataTable([\n" +
                "         ['Month', 'Receita mensal', 'Parcela Financiamento',  'Saldo Mensal'],\n" +
                /*
                "         ['"+ano+"',  "+economiamensal1+",      "+parcela+",        "+economiames1+"],\n" +
                "         ['"+ano2+"',  "+economiamensal2+",      "+parcela+",       "+economiames2+"],\n" +
                "         ['"+ano3+"',  "+economiamensal3+",      "+parcela+",       "+economiames3+"],\n" +
                "         ['"+ano4+"',  "+economiamensal4+",      "+parcela+",       "+economiames4+"],\n" +
                "         ['"+ano5+"',  "+economiamensal5+",      "+parcela+",       "+economiames5+"]\n" +
                */
/*
                "         ['2018',  1020,      450,        520],\n" +
                "         ['2019',  980,      780,       954],\n" +
                "         ['2020',  1200,      1040,       1400],\n" +
                "         ['2021',  700,      1200,       1020],\n" +
                "         ['2022',  1020,      908,       943]\n" +
                "      ]);\n" +
                "\n" +
                "    var options = {\n" +
                "          legend: { position: 'top', alignment: 'center', textStyle: {fontSize: 6, color: '#5a9bd5'} }," //, bold:'true'
                +"          colors: ['#5a9bd5', '#e97f35', '#FFCC00'],"
                + "          hAxis: {textStyle: {fontSize: 6}}," //slantedText: true, slantedTextAngle: 30,
                + "          vAxis: {title: 'R$',titleTextStyle: {color: '#5a9bd5', bold:'true'}},"
                + "          chartArea:{left:50, right:6}," + //left:30,
                //"      title : 'Monthly Coffee Production by Country',\n" +
                // "      vAxis: {title: 'Cups'},\n" +
                //"      hAxis: {title: 'Month'},\n" +
                "      seriesType: 'bars',\n" +
                "      series: {2: {type: 'line'}}\n" +
                "    };\n" +
                "\n" +
                "    var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));\n" +
                "    chart.draw(data, options);\n" +
                "  }\n" +
                "    </script>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id=\"chart_div\" style=\"width: 100%; height: 120px;\"></div>\n" +
                "  </body>\n" +
                "</html>";
*/
        String content3 = " <html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "  <head>\n" +
                "    <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n" +
                "    <script type=\"text/javascript\" src=\"http://www.google.com/jsapi\"></script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      google.load('visualization', '1', {packages: ['corechart']});\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "      function drawVisualization() {\n" +
                "        var data = google.visualization.arrayToDataTable([\n" +
                "            [\"X\", \"Receita mensal\", \"Parcela Financiamento\", \"Saldo Mensal\"],\n" +
                "         ['"+ano+"',  "+economiamensal1+",      "+parcela+",        "+economiames1+"],\n" +
                "         ['"+ano2+"',  "+economiamensal2+",      "+parcela+",       "+economiames2+"],\n" +
                "         ['"+ano3+"',  "+economiamensal3+",      "+parcela+",       "+economiames3+"],\n" +
                "         ['"+ano4+"',  "+economiamensal4+",      "+parcela+",       "+economiames4+"],\n" +
                "         ['"+ano5+"',  "+economiamensal5+",      "+parcela+",       "+economiames5+"]\n" +
                "        ]);\t\t\n" +
                "\t\tvar options = {\n" +
                //"     \t\thAxis: {textStyle: {fontSize: 6}},\n" +
                 "          hAxis: {textStyle: {fontSize: 5}, title: 'Fluxo de Caixa do mês de cada ano do financiamento',titleTextStyle: {fontSize: 9, color: '#5a9bd5', bold:'true'}}," //, titleTextStyle: {color: 'red'}
                +"\t\t\tvAxis: {title: 'R$', textStyle: {fontSize: 7}, titleTextStyle: {color: '#5a9bd5', bold:'true'}},\n" +
                "\t\t\tchartArea:{left:50, right:6, top:10, bottom:20},\n" +
                "    \t\tlegend: { position: 'top', alignment: 'center', textStyle: {fontSize: 6, color: '#5a9bd5'} },\n" +
                "\t\t\tseriesType: \"bars\",\n" +
                "\t\t\t\tseries: {\n" +
                "\t\t\t\t0: {\n" +
                "\t\t\t\t\tcolor: \"#5a9bd5\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t1: {\n" +
                "\t\t\t\t\tcolor: \"#e97f35\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t2: {\n" +
                "\t\t\t\t\ttype: \"line\",\n" +
                "\t\t\t\t\tcolor: \"#FFCC00\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t};    \n" +
                "\t\tvar chart = new google.visualization.ComboChart(document.getElementById(\"visualization\"));\n" +
                "\t\tchart.draw(data, options);\n" +
                "      }\n" +
                "      google.setOnLoadCallback(drawVisualization);\n" +
                "    </script>\n" +
                "  </head>\n" +
                "  <body style=\"font-family: Arial;border: 0 none;\">\n" +
                "    <div id=\"visualization\" style=\"width: 100%; height: 120px;\"></div>\n" +
                "  </body>\n" +
                "</html> ";


        WebSettings webSettings4 = wvGrafico3.getSettings();
        webSettings4.setJavaScriptEnabled(true);
        //wvGrafico3.requestFocusFromTouch();
        wvGrafico3.loadDataWithBaseURL( "file:///android_asset/", content3, "text/html", "utf-8", null );

        wvGrafico3.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                carregamentoGrafico3 = 1;
            }
        });
    }

    private void geraGrafico4(){
        Calendar cal = Calendar.getInstance();
        int ano1, ano4, ano7, ano10, ano13, ano16, ano19, ano22, ano25;
        ano1 = cal.get(Calendar.YEAR);
        ano4 = ano1+3;
        ano7 = ano1+6;
        ano10 = ano1+9;
        ano13 = ano1+12;
        ano16 = ano1+15;
        ano19 = ano1+18;
        ano22 = ano1+21;
        ano25 = ano1+24;

        Log.d("mensagem"," media do mes: "+ saldoAcumuladoAno1);
        Log.d("mensagem"," media do mes 2: "+ saldoAcumuladoAno2);
        Log.d("mensagem"," media do mes 3: "+ saldoAcumuladoAno3);
        Log.d("mensagem"," media do mes 4: "+ saldoAcumuladoAno4);
        Log.d("mensagem"," media do mes 5: "+ saldoAcumuladoAno5);
        Log.d("mensagem"," media do mes 6: "+ saldoAcumuladoAno6);
        Log.d("mensagem"," media do mes 7: "+ saldoAcumuladoAno7);
        Log.d("mensagem"," media do mes 8: "+ saldoAcumuladoAno8);
        Log.d("mensagem"," media do mes 9: "+ saldoAcumuladoAno9);
        Log.d("mensagem"," media do mes 10: "+ saldoAcumuladoAno10);
        Log.d("mensagem"," media do mes 11: "+ saldoAcumuladoAno11);
        Log.d("mensagem"," media do mes 12: "+ saldoAcumuladoAno12);

        String content3 = "<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
                + "      google.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "        var data = google.visualization.arrayToDataTable(["
                + "          ['Year', 'Capital próprio [R$]'],"
                + "          ['"+ano1+"', "+saldoAcumuladoAno1+"],"
                + "          [, "+saldoAcumuladoAno2+"],"
                + "          [, "+saldoAcumuladoAno3+"],"
                + "          ['"+ano4+"', "+saldoAcumuladoAno4+"],"
                + "          [, "+saldoAcumuladoAno5+"],"
                + "          [, "+saldoAcumuladoAno6+"],"
                + "          ['"+ano7+"', "+saldoAcumuladoAno7+"],"
                + "          [, "+saldoAcumuladoAno8+"],"
                + "          [, "+saldoAcumuladoAno9+"],"
                + "          ['"+ano10+"', "+saldoAcumuladoAno10+"],"
                + "          [, "+saldoAcumuladoAno11+"],"
                + "          [, "+saldoAcumuladoAno12+"],"
                + "          ['"+ano13+"', "+saldoAcumuladoAno13+"],"
                + "          [, "+saldoAcumuladoAno14+"],"
                + "          [, "+saldoAcumuladoAno15+"],"
                + "          ['"+ano16+"', "+saldoAcumuladoAno16+"],"
                + "          [, "+saldoAcumuladoAno17+"],"
                + "          [, "+saldoAcumuladoAno18+"],"
                + "          ['"+ano19+"', "+saldoAcumuladoAno19+"],"
                + "          [, "+saldoAcumuladoAno20+"],"
                + "          [, "+saldoAcumuladoAno21+"],"
                + "          ['"+ano22+"', "+saldoAcumuladoAno22+"],"
                + "          [, "+saldoAcumuladoAno23+"],"
                + "          [, "+saldoAcumuladoAno24+"],"
                + "          ['"+ano25+"', "+saldoAcumuladoAno25+"]"
                + "        ]);"
                + "        var options = {"
                + "          legend: { position: 'top', alignment: 'center', textStyle: {fontSize: 7, color: '#5a9bd5', bold:'true'} },"
                +"           colors: ['#5a9bd5'],"
                //+ "          hAxis: {textStyle: {fontSize: 4}}," //slantedText: true, slantedTextAngle: 30,
                + "          hAxis: {title: 'Fluxo de caixa acumulado',titleTextStyle: {fontSize: 8, color: '#5a9bd5', bold:'true'},textStyle: {fontSize: 4}}," //, titleTextStyle: {color: 'red'}
                + "          vAxis: {textStyle: {fontSize: 6}, title: 'R$', titleTextStyle: {color: '#5a9bd5', bold:'true'}},"
                + "          chartArea:{left:60, right:5, bottom:13, top:13}" // bottom:10, top:11
                + "        };"
                + "        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "    <div id=\"chart_div\" style=\"width: 100%; height: 120px;\"></div>"
                + "  </body>"
                + "</html>";

        WebSettings webSettings3 = wvGrafico4.getSettings();
        webSettings3.setJavaScriptEnabled(true);
        //wvGrafico2.requestFocusFromTouch();
        wvGrafico4.loadDataWithBaseURL( "file:///android_asset/", content3, "text/html", "utf-8", null );
        wvGrafico4.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                carregamentoGrafico4 = 1;
            }
        });

    }

    private void calculaTabelaAnaliseEconomicaHCC(){

        Double perdaAno1 = 0.0, perdaAno2 = 0.0, perdaAno3 = 0.0, perdaAno4 = 0.0, perdaAno5 = 0.0, perdaAno6 = 0.0, perdaAno7 = 0.0, perdaAno8 = 0.0, perdaAno9 = 0.0, perdaAno10 = 0.0, perdaAno11 = 0.0, perdaAno12 = 0.0;
        Double perdaAno13 = 0.0, perdaAno14 = 0.0, perdaAno15 = 0.0, perdaAno16 = 0.0, perdaAno17 = 0.0, perdaAno18 = 0.0, perdaAno19 = 0.0, perdaAno20 = 0.0, perdaAno21 = 0.0, perdaAno22 = 0.0, perdaAno23 = 0.0, perdaAno24 = 0.0, perdaAno25 = 0.0;

        double valorMultiplicacaoKWh =  0.0;
        double percenteValorManutencaoInicial = 0.0;
        double valorManutencao = 0.0;
        double valorMultiplicacaoEconomia =  0.0;

        Cursor linhas4 = db.rawQuery("SELECT * FROM " + TABELAANALISEECONIMICA + " ;", null);
        if (linhas4.moveToFirst()) {
            do {
                perdaAno2 = linhas4.getDouble(2);
                perdaAno3 = linhas4.getDouble(3);
                perdaAno4 = linhas4.getDouble(4);
                perdaAno5 = linhas4.getDouble(5);
                perdaAno6 = linhas4.getDouble(6);
                perdaAno7 = linhas4.getDouble(7);
                perdaAno8 = linhas4.getDouble(8);
                perdaAno9 = linhas4.getDouble(9);
                perdaAno10 = linhas4.getDouble(10);
                perdaAno11 = linhas4.getDouble(11);
                perdaAno12 = linhas4.getDouble(12);
                perdaAno13 = linhas4.getDouble(13);
                perdaAno14 = linhas4.getDouble(14);
                perdaAno15 = linhas4.getDouble(15);
                perdaAno16 = linhas4.getDouble(16);
                perdaAno17 = linhas4.getDouble(17);
                perdaAno18 = linhas4.getDouble(18);
                perdaAno19 = linhas4.getDouble(19);
                perdaAno20 = linhas4.getDouble(20);
                perdaAno21 = linhas4.getDouble(21);
                perdaAno22 = linhas4.getDouble(22);
                perdaAno23 = linhas4.getDouble(23);
                perdaAno24 = linhas4.getDouble(24);
                perdaAno25 = linhas4.getDouble(25);
                valorMultiplicacaoKWh = linhas4.getDouble(26);
                percenteValorManutencaoInicial = linhas4.getDouble(27);
                valorManutencao = linhas4.getDouble(28);
                valorMultiplicacaoEconomia = linhas4.getDouble(29);
                // = linhas4.getDouble(5); //valorPlaca

            }
            while (linhas4.moveToNext());
            linhas4.close();

            Log.d("tag34","perdaAno1: "+perdaAno1);
            Log.d("tag34","perdaAno2: "+perdaAno2);
            Log.d("tag34","perdaAno3: "+perdaAno3);
            Log.d("tag34","perdaAno4: "+perdaAno4);
            Log.d("tag34","perdaAno5: "+perdaAno5);
            Log.d("tag34","perdaAno6: "+perdaAno6);
            Log.d("tag34","perdaAno7: "+perdaAno7);
            Log.d("tag34","perdaAno8: "+perdaAno8);
            Log.d("tag34","perdaAno9: "+perdaAno9);
            Log.d("tag34","perdaAno10: "+perdaAno10);
            Log.d("tag34","perdaAno11: "+perdaAno11);
            Log.d("tag34","perdaAno12: "+perdaAno12);
            Log.d("tag34","perdaAno13: "+perdaAno13);
            Log.d("tag34","perdaAno14: "+perdaAno14);
            Log.d("tag34","perdaAno15: "+perdaAno15);
            Log.d("tag34","perdaAno16: "+perdaAno16);
            Log.d("tag34","perdaAno17: "+perdaAno17);
            Log.d("tag34","perdaAno18: "+perdaAno18);
            Log.d("tag34","perdaAno19: "+perdaAno19);
            Log.d("tag34","perdaAno20: "+perdaAno20);
            Log.d("tag34","perdaAno21: "+perdaAno21);
            Log.d("tag34","perdaAno22: "+perdaAno22);
            Log.d("tag34","perdaAno23: "+perdaAno23);
            Log.d("tag34","perdaAno24: "+perdaAno24);
            Log.d("tag34","perdaAno25: "+perdaAno25);
            Log.d("tag34","100: "+valorMultiplicacaoKWh);
            Log.d("tag34","200: "+percenteValorManutencaoInicial);
            Log.d("tag34","300: "+valorManutencao);
            Log.d("tag34","400: "+valorMultiplicacaoEconomia);

        }

        int kwhAno = Integer.parseInt(anoFinal);

        DecimalFormat formatoCincoCasasDecimais = new DecimalFormat("#######.#####");
        formatoCincoCasasDecimais.setRoundingMode(RoundingMode.UP);

        DecimalFormat formatoDuasCasasDecimais = new DecimalFormat("#######.##");
        formatoDuasCasasDecimais.setRoundingMode(RoundingMode.UP);



        Double geracaoAno1 = (double)kwhAno;
        Double geracaoAno2 = geracaoAno1;

        //Double geracaoAno3 = Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno2 * (1-(perdaAno3/100))).replace(",", "."));

        //BigDecimal geracaoAno3Teste = new BigDecimal(geracaoAno2).setScale(3, RoundingMode.HALF_EVEN);

        Double geracaoAno3 = (new BigDecimal(geracaoAno2).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno3/100));
        Double geracaoAno4 = (new BigDecimal(geracaoAno3).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno4/100));
        Double geracaoAno5 = (new BigDecimal(geracaoAno4).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno5/100));
        Double geracaoAno6 = (new BigDecimal(geracaoAno5).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno6/100));
        Double geracaoAno7 = (new BigDecimal(geracaoAno6).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno7/100));
        Double geracaoAno8 = (new BigDecimal(geracaoAno7).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno8/100));
        Double geracaoAno9 = (new BigDecimal(geracaoAno8).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno9/100));
        Double geracaoAno10 = (new BigDecimal(geracaoAno9).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno10/100));
        Double geracaoAno11 = (new BigDecimal(geracaoAno10).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno11/100));
        Double geracaoAno12 = (new BigDecimal(geracaoAno11).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno12/100));
        Double geracaoAno13 = (new BigDecimal(geracaoAno12).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno13/100));
        Double geracaoAno14 = (new BigDecimal(geracaoAno13).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno14/100));
        Double geracaoAno15 = (new BigDecimal(geracaoAno14).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno15/100));
        Double geracaoAno16 = (new BigDecimal(geracaoAno15).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno16/100));
        Double geracaoAno17 = (new BigDecimal(geracaoAno16).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno17/100));
        Double geracaoAno18 = (new BigDecimal(geracaoAno17).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno18/100));
        Double geracaoAno19 = (new BigDecimal(geracaoAno18).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno19/100));
        Double geracaoAno20 = (new BigDecimal(geracaoAno19).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno20/100));
        Double geracaoAno21 = (new BigDecimal(geracaoAno20).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno21/100));
        Double geracaoAno22 = (new BigDecimal(geracaoAno21).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno22/100));
        Double geracaoAno23 = (new BigDecimal(geracaoAno22).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno23/100));
        Double geracaoAno24 = (new BigDecimal(geracaoAno23).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno24/100));
        Double geracaoAno25 = (new BigDecimal(geracaoAno24).setScale(7, RoundingMode.HALF_EVEN).doubleValue()) *(1-(perdaAno25/100));

/*
        Log.d("mensagem","Eitaaaa noisss 1: "+bd);
        Log.d("mensagem","Eitaaaa noisss 2: "+bd2);
        Log.d("mensagem","Eitaaaa noisss 3: "+bd3);
        Log.d("mensagem","Eitaaaa noisss 4: "+bd4);
        Log.d("mensagem","Eitaaaa noisss 5: "+bd5);
        Log.d("mensagem","Eitaaaa noisss 6: "+bd6);
        Log.d("mensagem","Eitaaaa noisss 7: "+bd7);
        Log.d("mensagem","Eitaaaa noisss 8: "+bd8);
        Log.d("mensagem","Eitaaaa noisss 9: "+bd9);
        Log.d("mensagem","Eitaaaa noisss 10: "+bd10);
        Log.d("mensagem","Eitaaaa noisss 11: "+bd11);
        Log.d("mensagem","Eitaaaa noisss 12: "+bd12);
        Log.d("mensagem","Eitaaaa noisss 13: "+bd13);
        Log.d("mensagem","Eitaaaa noisss 14: "+bd14);
        Log.d("mensagem","Eitaaaa noisss 15: "+bd15);
        Log.d("mensagem","Eitaaaa noisss 16: "+bd16);
        Log.d("mensagem","Eitaaaa noisss 17: "+bd17);
        Log.d("mensagem","Eitaaaa noisss 18: "+bd18);
        Log.d("mensagem","Eitaaaa noisss 19: "+bd19);
        Log.d("mensagem","Eitaaaa noisss 20: "+bd20);
        Log.d("mensagem","Eitaaaa noisss 21: "+bd21);
        Log.d("mensagem","Eitaaaa noisss 22: "+bd22);
        Log.d("mensagem","Eitaaaa noisss 23: "+bd23);
       // Log.d("mensagem","Eitaaaa noisss 24: "+bd24);
        //Log.d("mensagem","Eitaaaa noisss 25: "+bd25);


        Double geracaoAno4 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno3 * (1-(perdaAno4/100))).replace(",", "."));
        Double geracaoAno5 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno4 * (1-(perdaAno5/100))).replace(",", "."));
        Double geracaoAno6 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno5 * (1-perdaAno6/100)).replace(",", "."));
        Double geracaoAno7 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno6 * (1-perdaAno7/100)).replace(",", "."));
        Double geracaoAno8 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno7 * (1-perdaAno8/100)).replace(",", "."));
        Double geracaoAno9 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno8 * (1-perdaAno9/100)).replace(",", "."));
        Double geracaoAno10 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno9 * (1-perdaAno10/100)).replace(",", "."));
        Double geracaoAno11 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno10 * (1-perdaAno11/100)).replace(",", "."));
        Double geracaoAno12 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno11 * (1-perdaAno12/100)).replace(",", "."));
        Double geracaoAno13 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno12 * (1-perdaAno13/100)).replace(",", "."));
        Double geracaoAno14 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno13 * (1-perdaAno14/100)).replace(",", "."));
        Double geracaoAno15 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno14 * (1-perdaAno15/100)).replace(",", "."));
        Double geracaoAno16 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno15 * (1-perdaAno16/100)).replace(",", "."));
        Double geracaoAno17 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno16 * (1-perdaAno17/100)).replace(",", "."));
        Double geracaoAno18 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno17 * (1-perdaAno18/100)).replace(",", "."));
        Double geracaoAno19 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno18 * (1-perdaAno19/100)).replace(",", "."));
        Double geracaoAno20 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno19 * (1-perdaAno20/100)).replace(",", "."));
        Double geracaoAno21 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno20 * (1-perdaAno21/100)).replace(",", "."));
        Double geracaoAno22 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno21 * (1-perdaAno22/100)).replace(",", "."));
        Double geracaoAno23 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno22 * (1-perdaAno23/100)).replace(",", "."));
        Double geracaoAno24 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno23 * (1-perdaAno24/100)).replace(",", "."));
        Double geracaoAno25 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno24 * (1-perdaAno25/100)).replace(",", "."));
*/
        //Double geracaoAno25 =  Double.parseDouble(formatoCincoCasasDecimais.format(geracaoAno24 * (1-perdaAno25/100)).replace(",", "."));


        Log.d("mensagem","Energia Ano 1: "+geracaoAno1);
        Log.d("mensagem","Energia Ano 2: "+geracaoAno2);
        Log.d("mensagem","Energia Ano 3: "+geracaoAno3);
        Log.d("mensagem","Energia Ano 4: "+geracaoAno4);
        Log.d("mensagem","Energia Ano 5: "+geracaoAno5);
        Log.d("mensagem","Energia Ano 6: "+geracaoAno6);
        Log.d("mensagem","Energia Ano 7: "+geracaoAno7);
        Log.d("mensagem","Energia Ano 8: "+geracaoAno8);
        Log.d("mensagem","Energia Ano 9: "+geracaoAno9);
        Log.d("mensagem","Energia Ano 10: "+geracaoAno10);
        Log.d("mensagem","Energia Ano 11: "+geracaoAno11);
        Log.d("mensagem","Energia Ano 12: "+geracaoAno12);
        Log.d("mensagem","Energia Ano 13: "+geracaoAno13);
        Log.d("mensagem","Energia Ano 14: "+geracaoAno14);
        Log.d("mensagem","Energia Ano 15: "+geracaoAno15);
        Log.d("mensagem","Energia Ano 16: "+geracaoAno16);
        Log.d("mensagem","Energia Ano 17: "+geracaoAno17);
        Log.d("mensagem","Energia Ano 18: "+geracaoAno18);
        Log.d("mensagem","Energia Ano 19: "+geracaoAno19);
        Log.d("mensagem","Energia Ano 20: "+geracaoAno20);
        Log.d("mensagem","Energia Ano 21: "+geracaoAno21);
        Log.d("mensagem","Energia Ano 22: "+geracaoAno22);
        Log.d("mensagem","Energia Ano 23: "+geracaoAno23);
        Log.d("mensagem","Energia Ano 24: "+geracaoAno24);
        Log.d("mensagem","Energia Ano 25: "+geracaoAno25);

        //Log.d("mensagem","Valor kwh: "+ valorMultiplicacaoKWh);
        double valorKWhAno1 = Double.parseDouble(KWinstaladoFinal);
        double valorKWhAno2 = valorKWhAno1 * valorMultiplicacaoKWh;
        double valorKWhAno3 = valorKWhAno2 * valorMultiplicacaoKWh;
        double valorKWhAno4 = valorKWhAno3 * valorMultiplicacaoKWh;
        double valorKWhAno5 = valorKWhAno4 * valorMultiplicacaoKWh;
        double valorKWhAno6 = valorKWhAno5 * valorMultiplicacaoKWh;
        double valorKWhAno7 = valorKWhAno6 * valorMultiplicacaoKWh;
        double valorKWhAno8 = valorKWhAno7 * valorMultiplicacaoKWh;
        double valorKWhAno9 = valorKWhAno8 * valorMultiplicacaoKWh;
        double valorKWhAno10 = valorKWhAno9 * valorMultiplicacaoKWh;
        double valorKWhAno11 = valorKWhAno10 * valorMultiplicacaoKWh;
        double valorKWhAno12 = valorKWhAno11 * valorMultiplicacaoKWh;
        double valorKWhAno13 = valorKWhAno12 * valorMultiplicacaoKWh;
        double valorKWhAno14 = valorKWhAno13 * valorMultiplicacaoKWh;
        double valorKWhAno15 = valorKWhAno14 * valorMultiplicacaoKWh;
        double valorKWhAno16 = valorKWhAno15 * valorMultiplicacaoKWh;
        double valorKWhAno17 = valorKWhAno16 * valorMultiplicacaoKWh;
        double valorKWhAno18 = valorKWhAno17 * valorMultiplicacaoKWh;
        double valorKWhAno19 = valorKWhAno18 * valorMultiplicacaoKWh;
        double valorKWhAno20 = valorKWhAno19 * valorMultiplicacaoKWh;
        double valorKWhAno21 = valorKWhAno20 * valorMultiplicacaoKWh;
        double valorKWhAno22 = valorKWhAno21 * valorMultiplicacaoKWh;
        double valorKWhAno23 = valorKWhAno22 * valorMultiplicacaoKWh;
        double valorKWhAno24 = valorKWhAno23 * valorMultiplicacaoKWh;
        double valorKWhAno25 = valorKWhAno24 * valorMultiplicacaoKWh;

        Log.d("mensagem","Valor Km Ano 1: "+valorKWhAno1);
        Log.d("mensagem","Valor Km Ano 2: "+valorKWhAno2);
        Log.d("mensagem","Valor Km Ano 3: "+valorKWhAno3);
        Log.d("mensagem","Valor Km Ano 4: "+valorKWhAno4);
        Log.d("mensagem","Valor Km Ano 5: "+valorKWhAno5);
        Log.d("mensagem","Valor Km Ano 6: "+valorKWhAno6);
        Log.d("mensagem","Valor Km Ano 7: "+valorKWhAno7);
        Log.d("mensagem","Valor Km Ano 8: "+valorKWhAno8);
        Log.d("mensagem","Valor Km Ano 9: "+valorKWhAno9);
        Log.d("mensagem","Valor Km Ano 10: "+valorKWhAno10);
        Log.d("mensagem","Valor Km Ano 11: "+valorKWhAno11);
        Log.d("mensagem","Valor Km Ano 12: "+valorKWhAno12);
        Log.d("mensagem","Valor Km Ano 13: "+valorKWhAno13);
        Log.d("mensagem","Valor Km Ano 14: "+valorKWhAno14);
        Log.d("mensagem","Valor Km Ano 15: "+valorKWhAno15);
        Log.d("mensagem","Valor Km Ano 16: "+valorKWhAno16);
        Log.d("mensagem","Valor Km Ano 17: "+valorKWhAno17);
        Log.d("mensagem","Valor Km Ano 18: "+valorKWhAno18);
        Log.d("mensagem","Valor Km Ano 19: "+valorKWhAno19);
        Log.d("mensagem","Valor Km Ano 20: "+valorKWhAno20);
        Log.d("mensagem","Valor Km Ano 21: "+valorKWhAno21);
        Log.d("mensagem","Valor Km Ano 22: "+valorKWhAno22);
        Log.d("mensagem","Valor Km Ano 23: "+valorKWhAno23);
        Log.d("mensagem","Valor Km Ano 24: "+valorKWhAno24);
        Log.d("mensagem","Valor Km Ano 25: "+valorKWhAno25);


        //int consumo = consumoXgeracao;
        double investimento = -1 * propostaMaisComissao; ///sistemaDouble

        double manutencaoAno1 = 0.0;
        double manutencaoAno2 = 0.0;
        double manutencaoAno3 = 0.0;
        double manutencaoAno4 = 0.0;
        double manutencaoAno5 = 0.0;

        double manutencaoAno6 = investimento * (percenteValorManutencaoInicial/100);
        double manutencaoAno7 = manutencaoAno6 * valorManutencao;
        double manutencaoAno8 = manutencaoAno7 * valorManutencao;
        double manutencaoAno9 = manutencaoAno8 * valorManutencao;
        double manutencaoAno10 = manutencaoAno9 * valorManutencao;
        double manutencaoAno11 = manutencaoAno10 * valorManutencao +(investimento * 0.2);
        double manutencaoAno12 = (manutencaoAno10 * valorManutencao)*valorManutencao;
        double manutencaoAno13 = manutencaoAno12 * valorManutencao;
        double manutencaoAno14 = manutencaoAno13 * valorManutencao;
        double manutencaoAno15 = manutencaoAno14 * valorManutencao;
        double manutencaoAno16 = manutencaoAno15 * valorManutencao;
        double manutencaoAno17 = manutencaoAno16 * valorManutencao;
        double manutencaoAno18 = manutencaoAno17 * valorManutencao;
        double manutencaoAno19 = manutencaoAno18 * valorManutencao;
        double manutencaoAno20 = manutencaoAno19 * valorManutencao;
        double manutencaoAno21 = manutencaoAno20 * valorManutencao +(investimento * 0.2);
        double manutencaoAno22 = (manutencaoAno20 * valorManutencao)*valorManutencao;
        double manutencaoAno23 = manutencaoAno22 * valorManutencao;
        double manutencaoAno24 = manutencaoAno23 * valorManutencao;
        double manutencaoAno25 = manutencaoAno24 * valorManutencao;

        //Log.d("mensagem","Manutenção Ano 1: "+valorKWhAno1);
        //Log.d("mensagem","Manutenção Ano 2: "+valorKWhAno2);
        //Log.d("mensagem","Manutenção Ano 3: "+valorKWhAno3);
        //Log.d("mensagem","Manutenção 4: "+valorKWhAno4);
        //Log.d("mensagem","Manutenção Ano 5: "+valorKWhAno5);
        Log.d("mensagem","Manutenção Ano 6: "+manutencaoAno6);
        Log.d("mensagem","Manutenção Ano 7: "+manutencaoAno7);
        Log.d("mensagem","Manutenção Ano 8: "+manutencaoAno8);
        Log.d("mensagem","Manutenção Ano 9: "+manutencaoAno9);
        Log.d("mensagem","Manutenção Ano 10: "+manutencaoAno10);
        Log.d("mensagem","Manutenção Ano 11: "+manutencaoAno11);
        Log.d("mensagem","Manutenção Ano 12: "+manutencaoAno12);
        Log.d("mensagem","Manutenção Ano 13: "+manutencaoAno13);
        Log.d("mensagem","Manutenção Ano 14: "+manutencaoAno14);
        Log.d("mensagem","Manutenção Ano 15: "+manutencaoAno15);
        Log.d("mensagem","Manutenção Ano 16: "+manutencaoAno16);
        Log.d("mensagem","Manutenção Ano 17: "+manutencaoAno17);
        Log.d("mensagem","VManutenção Ano 18: "+manutencaoAno18);
        Log.d("mensagem","Manutenção Ano 19: "+manutencaoAno19);
        Log.d("mensagem","Manutenção Ano 20: "+manutencaoAno20);
        Log.d("mensagem","Manutenção Ano 21: "+manutencaoAno21);
        Log.d("mensagem","Manutenção Ano 22: "+manutencaoAno22);
        Log.d("mensagem","Manutenção Ano 23: "+manutencaoAno23);
        Log.d("mensagem","Manutenção Ano 24: "+manutencaoAno24);
        Log.d("mensagem","Manutenção Ano 25: "+manutencaoAno25);

        Log.d("mensagem","Grafico Azul: "+calculoGraficoAzul);
        Log.d("mensagem","Grafico Laranja: "+calculoGraficoLaranja);

        double valorEconomiaAno1 = calculoGraficoAzul - calculoGraficoLaranja;//calculoEconomia;//Double.parseDouble(KWinstaladoFinal) * kwhAno;//kwhAno * txtUltimoValorMedio;
        double valorEconomiaAno2 = valorEconomiaAno1 * valorMultiplicacaoEconomia;
        double valorEconomiaAno3 = valorEconomiaAno2 * valorMultiplicacaoEconomia;
        double valorEconomiaAno4 = valorEconomiaAno3 * valorMultiplicacaoEconomia;
        double valorEconomiaAno5 = valorEconomiaAno4 * valorMultiplicacaoEconomia;
        double valorEconomiaAno6 = valorEconomiaAno5 * valorMultiplicacaoEconomia;
        double valorEconomiaAno7 = valorEconomiaAno6 * valorMultiplicacaoEconomia;
        double valorEconomiaAno8 = valorEconomiaAno7 * valorMultiplicacaoEconomia;
        double valorEconomiaAno9 = valorEconomiaAno8 * valorMultiplicacaoEconomia;
        double valorEconomiaAno10 = valorEconomiaAno9 * valorMultiplicacaoEconomia;
        double valorEconomiaAno11 = valorEconomiaAno10 * valorMultiplicacaoEconomia;
        double valorEconomiaAno12 = valorEconomiaAno11 * valorMultiplicacaoEconomia;
        double valorEconomiaAno13 = valorEconomiaAno12 * valorMultiplicacaoEconomia;
        double valorEconomiaAno14 = valorEconomiaAno13 * valorMultiplicacaoEconomia;
        double valorEconomiaAno15 = valorEconomiaAno14 * valorMultiplicacaoEconomia;
        double valorEconomiaAno16 = valorEconomiaAno15 * valorMultiplicacaoEconomia;
        double valorEconomiaAno17 = valorEconomiaAno16 * valorMultiplicacaoEconomia;
        double valorEconomiaAno18 = valorEconomiaAno17 * valorMultiplicacaoEconomia;
        double valorEconomiaAno19 = valorEconomiaAno18 * valorMultiplicacaoEconomia;
        double valorEconomiaAno20 = valorEconomiaAno19 * valorMultiplicacaoEconomia;
        double valorEconomiaAno21 = valorEconomiaAno20 * valorMultiplicacaoEconomia;
        double valorEconomiaAno22 = valorEconomiaAno21 * valorMultiplicacaoEconomia;
        double valorEconomiaAno23 = valorEconomiaAno22 * valorMultiplicacaoEconomia;
        double valorEconomiaAno24 = valorEconomiaAno23 * valorMultiplicacaoEconomia;
        double valorEconomiaAno25 = valorEconomiaAno24 * valorMultiplicacaoEconomia;

        Log.d("mensagem","Valor Economia Ano 1: "+valorEconomiaAno1);
        Log.d("mensagem","Valor Economia Ano 2: "+valorEconomiaAno2);
        Log.d("mensagem","Valor Economia Ano 3: "+valorEconomiaAno3);
        Log.d("mensagem","Valor Economia Ano 4: "+valorEconomiaAno4);
        Log.d("mensagem","Valor Economia Ano 5: "+valorEconomiaAno5);
        Log.d("mensagem","Valor Economia Ano 6: "+valorEconomiaAno6);
        Log.d("mensagem","Valor Economia Ano 7: "+valorEconomiaAno7);
        Log.d("mensagem","Valor Economia Ano 8: "+valorEconomiaAno8);
        Log.d("mensagem","Valor Economia Ano 9: "+valorEconomiaAno9);
        Log.d("mensagem","Valor Economia Ano 10: "+valorEconomiaAno10);
        Log.d("mensagem","Valor Economia Ano 11: "+valorEconomiaAno11);
        Log.d("mensagem","Valor Economia Ano 12: "+valorEconomiaAno12);
        Log.d("mensagem","Valor Economia Ano 13: "+valorEconomiaAno13);
        Log.d("mensagem","Valor Economia Ano 14: "+valorEconomiaAno14);
        Log.d("mensagem","Valor Economia Ano 15: "+valorEconomiaAno15);
        Log.d("mensagem","Valor Economia Ano 16: "+valorEconomiaAno16);
        Log.d("mensagem","Valor Economia Ano 17: "+valorEconomiaAno17);
        Log.d("mensagem","Valor Economia Ano 18: "+valorEconomiaAno18);
        Log.d("mensagem","Valor Economia Ano 19: "+valorEconomiaAno19);
        Log.d("mensagem","Valor Economia Ano 20: "+valorEconomiaAno20);
        Log.d("mensagem","Valor Economia Ano 21: "+valorEconomiaAno21);
        Log.d("mensagem","Valor Economia Ano 22: "+valorEconomiaAno22);
        Log.d("mensagem","Valor Economia Ano 23: "+valorEconomiaAno23);
        Log.d("mensagem","Valor Economia Ano 24: "+valorEconomiaAno24);
        Log.d("mensagem","Valor Economia Ano 25: "+valorEconomiaAno25);


        Log.d("mensagem","Investimento: "+ investimento);
        Log.d("mensagem","Valor Economia: "+ valorEconomiaAno1);

        double saldoAno1 = investimento+valorEconomiaAno1;
        double saldoAno2 = manutencaoAno2+valorEconomiaAno2;
        double saldoAno3 = manutencaoAno3+valorEconomiaAno3;
        double saldoAno4 = manutencaoAno4+valorEconomiaAno4;
        double saldoAno5 = manutencaoAno5+valorEconomiaAno5;
        double saldoAno6 = manutencaoAno6+valorEconomiaAno6;
        double saldoAno7 = manutencaoAno7+valorEconomiaAno7;
        double saldoAno8 = manutencaoAno8+valorEconomiaAno8;
        double saldoAno9 = manutencaoAno9+valorEconomiaAno9;
        double saldoAno10 = manutencaoAno10+valorEconomiaAno10;
        double saldoAno11 = manutencaoAno11+valorEconomiaAno11;
        double saldoAno12 = manutencaoAno12+valorEconomiaAno12;
        double saldoAno13 = manutencaoAno13+valorEconomiaAno13;
        double saldoAno14 = manutencaoAno14+valorEconomiaAno14;
        double saldoAno15 = manutencaoAno15+valorEconomiaAno15;
        double saldoAno16 = manutencaoAno16+valorEconomiaAno16;
        double saldoAno17 = manutencaoAno17+valorEconomiaAno17;
        double saldoAno18 = manutencaoAno18+valorEconomiaAno18;
        double saldoAno19 = manutencaoAno19+valorEconomiaAno19;
        double saldoAno20 = manutencaoAno20+valorEconomiaAno20;
        double saldoAno21 = manutencaoAno21+valorEconomiaAno21;
        double saldoAno22 = manutencaoAno22+valorEconomiaAno22;
        double saldoAno23 = manutencaoAno23+valorEconomiaAno23;
        double saldoAno24 = manutencaoAno24+valorEconomiaAno24;
        double saldoAno25 = manutencaoAno25+valorEconomiaAno25;

        Log.d("mensagem","Saldo Ano 1: "+saldoAno1);
        Log.d("mensagem","Saldo Ano 2: "+saldoAno2);
        Log.d("mensagem","Saldo Ano 3: "+saldoAno3);
        Log.d("mensagem","Saldo Ano 4: "+saldoAno4);
        Log.d("mensagem","Saldo Ano 5: "+saldoAno5);
        Log.d("mensagem","Saldo Ano 6: "+saldoAno6);
        Log.d("mensagem","Saldo Ano 7: "+saldoAno7);
        Log.d("mensagem","Saldo Ano 8: "+saldoAno8);
        Log.d("mensagem","Saldo Ano 9: "+saldoAno9);
        Log.d("mensagem","Saldo Ano 10: "+saldoAno10);
        Log.d("mensagem","Saldo Ano 11: "+saldoAno11);
        Log.d("mensagem","Saldo Ano 12: "+saldoAno12);
        Log.d("mensagem","Saldo Ano 13: "+saldoAno13);
        Log.d("mensagem","Saldo Ano 14: "+saldoAno14);
        Log.d("mensagem","Saldo Ano 15: "+saldoAno15);
        Log.d("mensagem","Saldo Ano 16: "+saldoAno16);
        Log.d("mensagem","Saldo Ano 17: "+saldoAno17);
        Log.d("mensagem","Saldo Ano 18: "+saldoAno18);
        Log.d("mensagem","Saldo Ano 19: "+saldoAno19);
        Log.d("mensagem","Saldo Ano 20: "+saldoAno20);
        Log.d("mensagem","Saldo Ano 21: "+saldoAno21);
        Log.d("mensagem","Saldo Ano 22: "+saldoAno22);
        Log.d("mensagem","Saldo Ano 23: "+saldoAno23);
        Log.d("mensagem","Saldo Ano 24: "+saldoAno24);
        Log.d("mensagem","Saldo Ano 25: "+saldoAno25);

        saldoAcumuladoAno1 = investimento+manutencaoAno1+valorEconomiaAno1;
        saldoAcumuladoAno2 = manutencaoAno2+valorEconomiaAno2+saldoAcumuladoAno1;
        saldoAcumuladoAno3 = manutencaoAno3+valorEconomiaAno3+saldoAcumuladoAno2;
        saldoAcumuladoAno4 = manutencaoAno4+valorEconomiaAno4+saldoAcumuladoAno3;
        saldoAcumuladoAno5 = manutencaoAno5+valorEconomiaAno5+saldoAcumuladoAno4;
         saldoAcumuladoAno6 = manutencaoAno6+valorEconomiaAno6+saldoAcumuladoAno5;
         saldoAcumuladoAno7 = manutencaoAno7+valorEconomiaAno7+saldoAcumuladoAno6;
         saldoAcumuladoAno8 = manutencaoAno8+valorEconomiaAno8+saldoAcumuladoAno7;
         saldoAcumuladoAno9 = manutencaoAno9+valorEconomiaAno9+saldoAcumuladoAno8;
         saldoAcumuladoAno10 = manutencaoAno10+valorEconomiaAno10+saldoAcumuladoAno9;
         saldoAcumuladoAno11 = manutencaoAno11+valorEconomiaAno11+saldoAcumuladoAno10;
         saldoAcumuladoAno12 = manutencaoAno12+valorEconomiaAno12+saldoAcumuladoAno11;
         saldoAcumuladoAno13 = manutencaoAno13+valorEconomiaAno13+saldoAcumuladoAno12;
         saldoAcumuladoAno14 = manutencaoAno14+valorEconomiaAno14+saldoAcumuladoAno13;
         saldoAcumuladoAno15 = manutencaoAno15+valorEconomiaAno15+saldoAcumuladoAno14;
         saldoAcumuladoAno16 = manutencaoAno16+valorEconomiaAno16+saldoAcumuladoAno15;
         saldoAcumuladoAno17 = manutencaoAno17+valorEconomiaAno17+saldoAcumuladoAno16;
         saldoAcumuladoAno18 = manutencaoAno18+valorEconomiaAno18+saldoAcumuladoAno17;
         saldoAcumuladoAno19 = manutencaoAno19+valorEconomiaAno19+saldoAcumuladoAno18;
         saldoAcumuladoAno20 = manutencaoAno20+valorEconomiaAno20+saldoAcumuladoAno19;
         saldoAcumuladoAno21 = manutencaoAno21+valorEconomiaAno21+saldoAcumuladoAno20;
         saldoAcumuladoAno22 = manutencaoAno22+valorEconomiaAno22+saldoAcumuladoAno21;
         saldoAcumuladoAno23 = manutencaoAno23+valorEconomiaAno23+saldoAcumuladoAno22;
         saldoAcumuladoAno24 = manutencaoAno24+valorEconomiaAno24+saldoAcumuladoAno23;
        saldoAcumuladoAno25 = manutencaoAno25+valorEconomiaAno25+saldoAcumuladoAno24;


        contadorPayback = 0;
        if(saldoAcumuladoAno1 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno2 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno3 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno4 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno5 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno6 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno7 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno8 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno9 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno10 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno11 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno12 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno13 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno14 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno15 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno16 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno17 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno18 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno19 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno20 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno21 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno22 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno23 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno24 < 0){
            contadorPayback = contadorPayback+1;
        }
        if(saldoAcumuladoAno25 < 0){
            contadorPayback = contadorPayback+1;
        }

        Double EnergiaProduzida25Anos = geracaoAno1+geracaoAno2+geracaoAno3+geracaoAno4+geracaoAno5+geracaoAno6+geracaoAno7+geracaoAno8+geracaoAno9+geracaoAno10+geracaoAno11+geracaoAno12+geracaoAno13+geracaoAno14+geracaoAno15+geracaoAno16+geracaoAno17+geracaoAno18+geracaoAno19+geracaoAno20+geracaoAno21+geracaoAno22+geracaoAno23+geracaoAno24+geracaoAno25;
        EconomiaDeEnergiaProduzida25Anos = valorEconomiaAno1+valorEconomiaAno2+valorEconomiaAno3+valorEconomiaAno4+valorEconomiaAno5+valorEconomiaAno6+valorEconomiaAno7+valorEconomiaAno8+valorEconomiaAno9+valorEconomiaAno10+valorEconomiaAno11+valorEconomiaAno12+valorEconomiaAno13+valorEconomiaAno14+valorEconomiaAno15+valorEconomiaAno16+valorEconomiaAno17+valorEconomiaAno18+valorEconomiaAno19+valorEconomiaAno20+valorEconomiaAno21+valorEconomiaAno22+valorEconomiaAno23+valorEconomiaAno24+valorEconomiaAno25;
        Double VPL = saldoAcumuladoAno1+saldoAcumuladoAno2+saldoAcumuladoAno3+saldoAcumuladoAno4+saldoAcumuladoAno5+saldoAcumuladoAno6+saldoAcumuladoAno7+saldoAcumuladoAno8+saldoAcumuladoAno9+saldoAcumuladoAno10+saldoAcumuladoAno11+saldoAcumuladoAno12+saldoAcumuladoAno13+saldoAcumuladoAno14+saldoAcumuladoAno15+saldoAcumuladoAno16+saldoAcumuladoAno17+saldoAcumuladoAno18+saldoAcumuladoAno19+saldoAcumuladoAno20+saldoAcumuladoAno21+saldoAcumuladoAno22+saldoAcumuladoAno23+saldoAcumuladoAno24+saldoAcumuladoAno25;

        DecimalFormat df = new DecimalFormat("#######.##");
        df.setRoundingMode(RoundingMode.UP);

        inteiroEnergiaProduzida25Anos = (int) Math.round(EnergiaProduzida25Anos);

        Log.d("mensagem","Energia 25 Anos: "+inteiroEnergiaProduzida25Anos);
        Log.d("mensagem","Economia de Energia em 25 Anos: "+df.format(EconomiaDeEnergiaProduzida25Anos));
        Log.d("mensagem","Fluxo de Caixa Acumulado: "+df.format(saldoAcumuladoAno25));
        Log.d("mensagem","VPL: "+df.format(VPL));
        Log.d("mensagem","TIR: ");
        Log.d("mensagem","PAYBACK: "+contadorPayback);

        //TABELA 2

        //double valor = -1 * (investimento * 0.65);
       // double taxa = 0.95/100;
        //int parcelas = 60;
        //double capital = valor / parcelas;
        double prestacao = investimento * taxaSicred;
        double capital = prestacao * 12;

        Log.d("mensagem","Prestação: "+prestacao);
        Log.d("mensagem","Capital: "+capital);

        double saldo2Ano1 = manutencaoAno1+capital+valorEconomiaAno1;
        double saldo2Ano2 = manutencaoAno2+capital+valorEconomiaAno2;
        double saldo2Ano3 = manutencaoAno3+capital+valorEconomiaAno3;
        double saldo2Ano4 = manutencaoAno4+capital+valorEconomiaAno4;
        double saldo2Ano5 = manutencaoAno5+capital+valorEconomiaAno5;
        double saldo2Ano6 = manutencaoAno6+valorEconomiaAno6;
        double saldo2Ano7 = manutencaoAno7+valorEconomiaAno7;
        double saldo2Ano8 = manutencaoAno8+valorEconomiaAno8;
        double saldo2Ano9 = manutencaoAno9+valorEconomiaAno9;
        double saldo2Ano10 = manutencaoAno10+valorEconomiaAno10;
        double saldo2Ano11 = manutencaoAno11+valorEconomiaAno11;
        double saldo2Ano12 = manutencaoAno12+valorEconomiaAno12;
        double saldo2Ano13 = manutencaoAno13+valorEconomiaAno13;
        double saldo2Ano14 = manutencaoAno14+valorEconomiaAno14;
        double saldo2Ano15 = manutencaoAno15+valorEconomiaAno15;
        double saldo2Ano16 = manutencaoAno16+valorEconomiaAno16;
        double saldo2Ano17 = manutencaoAno17+valorEconomiaAno17;
        double saldo2Ano18 = manutencaoAno18+valorEconomiaAno18;
        double saldo2Ano19 = manutencaoAno19+valorEconomiaAno19;
        double saldo2Ano20 = manutencaoAno20+valorEconomiaAno20;
        double saldo2Ano21 = manutencaoAno21+valorEconomiaAno21;
        double saldo2Ano22 = manutencaoAno22+valorEconomiaAno22;
        double saldo2Ano23 = manutencaoAno23+valorEconomiaAno23;
        double saldo2Ano24 = manutencaoAno24+valorEconomiaAno24;
        double saldo2Ano25 = manutencaoAno25+valorEconomiaAno25;

        Log.d("mensagem","Saldo Ano 1: "+saldo2Ano1);
        Log.d("mensagem","Saldo Ano 2: "+saldo2Ano2);
        Log.d("mensagem","Saldo Ano 3: "+saldo2Ano3);
        Log.d("mensagem","Saldo Ano 4: "+saldo2Ano4);
        Log.d("mensagem","Saldo Ano 5: "+saldo2Ano5);
        Log.d("mensagem","Saldo Ano 6: "+saldo2Ano6);
        Log.d("mensagem","Saldo Ano 7: "+saldo2Ano7);
        Log.d("mensagem","Saldo Ano 8: "+saldo2Ano8);
        Log.d("mensagem","Saldo Ano 9: "+saldo2Ano9);
        Log.d("mensagem","Saldo Ano 10: "+saldo2Ano10);
        Log.d("mensagem","Saldo Ano 11: "+saldo2Ano11);
        Log.d("mensagem","Saldo Ano 12: "+saldo2Ano12);
        Log.d("mensagem","Saldo Ano 13: "+saldo2Ano13);
        Log.d("mensagem","Saldo Ano 14: "+saldo2Ano14);
        Log.d("mensagem","Saldo Ano 15: "+saldo2Ano15);
        Log.d("mensagem","Saldo Ano 16: "+saldo2Ano16);
        Log.d("mensagem","Saldo Ano 17: "+saldo2Ano17);
        Log.d("mensagem","Saldo Ano 18: "+saldo2Ano18);
        Log.d("mensagem","Saldo Ano 19: "+saldo2Ano19);
        Log.d("mensagem","Saldo Ano 20: "+saldo2Ano20);
        Log.d("mensagem","Saldo Ano 21: "+saldo2Ano21);
        Log.d("mensagem","Saldo Ano 22: "+saldo2Ano22);
        Log.d("mensagem","Saldo Ano 23: "+saldo2Ano23);
        Log.d("mensagem","Saldo Ano 24: "+saldo2Ano24);
        Log.d("mensagem","Saldo Ano 25: "+saldo2Ano25);

        double saldoAcumulado2Ano1 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano1).replace(",", "."));
        double saldoAcumulado2Ano2 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano2+saldoAcumulado2Ano1).replace(",", "."));
        double saldoAcumulado2Ano3 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano3+saldoAcumulado2Ano2).replace(",", "."));
        double saldoAcumulado2Ano4 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano4+saldoAcumulado2Ano3).replace(",", "."));
        double saldoAcumulado2Ano5 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano5+saldoAcumulado2Ano4).replace(",", "."));
        double saldoAcumulado2Ano6 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano6+saldoAcumulado2Ano5).replace(",", "."));
        double saldoAcumulado2Ano7 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano7+saldoAcumulado2Ano6).replace(",", "."));
        double saldoAcumulado2Ano8 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano8+saldoAcumulado2Ano7).replace(",", "."));
        double saldoAcumulado2Ano9 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano9+saldoAcumulado2Ano8).replace(",", "."));
        double saldoAcumulado2Ano10 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano10+saldoAcumulado2Ano9).replace(",", "."));
        double saldoAcumulado2Ano11 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano11+saldoAcumulado2Ano10).replace(",", "."));
        double saldoAcumulado2Ano12 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano12+saldoAcumulado2Ano11).replace(",", "."));
        double saldoAcumulado2Ano13 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano13+saldoAcumulado2Ano12).replace(",", "."));
        double saldoAcumulado2Ano14 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano14+saldoAcumulado2Ano13).replace(",", "."));
        double saldoAcumulado2Ano15 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano15+saldoAcumulado2Ano14).replace(",", "."));
        double saldoAcumulado2Ano16 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano16+saldoAcumulado2Ano15).replace(",", "."));
        double saldoAcumulado2Ano17 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano17+saldoAcumulado2Ano16).replace(",", "."));
        double saldoAcumulado2Ano18 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano18+saldoAcumulado2Ano17).replace(",", "."));
        double saldoAcumulado2Ano19 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano19+saldoAcumulado2Ano18).replace(",", "."));
        double saldoAcumulado2Ano20 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano20+saldoAcumulado2Ano19).replace(",", "."));
        double saldoAcumulado2Ano21 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano21+saldoAcumulado2Ano20).replace(",", "."));
        double saldoAcumulado2Ano22 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano22+saldoAcumulado2Ano21).replace(",", "."));
        double saldoAcumulado2Ano23 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano23+saldoAcumulado2Ano22).replace(",", "."));
        double saldoAcumulado2Ano24 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano24+saldoAcumulado2Ano23).replace(",", "."));
        saldoAcumulado2Ano25 = Double.parseDouble(formatoDuasCasasDecimais.format(saldo2Ano25+saldoAcumulado2Ano24).replace(",", "."));

        Log.d("mensagem","Acumulado Ano 1: "+saldoAcumulado2Ano1);
        Log.d("mensagem","Acumulado Ano 2: "+saldoAcumulado2Ano2);
        Log.d("mensagem","Acumulado Ano 3: "+saldoAcumulado2Ano3);
        Log.d("mensagem","Acumulado Ano 4: "+saldoAcumulado2Ano4);
        Log.d("mensagem","Acumulado Ano 5: "+saldoAcumulado2Ano5);
        Log.d("mensagem","Acumulado Ano 6: "+saldoAcumulado2Ano6);
        Log.d("mensagem","Acumulado Ano 7: "+saldoAcumulado2Ano7);
        Log.d("mensagem","Acumulado Ano 8: "+saldoAcumulado2Ano8);
        Log.d("mensagem","Acumulado Ano 9: "+saldoAcumulado2Ano9);
        Log.d("mensagem","Acumulado Ano 10: "+saldoAcumulado2Ano10);
        Log.d("mensagem","Acumulado Ano 11: "+saldoAcumulado2Ano11);
        Log.d("mensagem","Acumulado Ano 12: "+saldoAcumulado2Ano12);
        Log.d("mensagem","Acumulado Ano 13: "+saldoAcumulado2Ano13);
        Log.d("mensagem","Acumulado Ano 14: "+saldoAcumulado2Ano14);
        Log.d("mensagem","Acumulado Ano 15: "+saldoAcumulado2Ano15);
        Log.d("mensagem","Acumulado Ano 16: "+saldoAcumulado2Ano16);
        Log.d("mensagem","Acumulado Ano 17: "+saldoAcumulado2Ano17);
        Log.d("mensagem","Acumulado Ano 18: "+saldoAcumulado2Ano18);
        Log.d("mensagem","Acumulado Ano 19: "+saldoAcumulado2Ano19);
        Log.d("mensagem","Acumulado Ano 20: "+saldoAcumulado2Ano20);
        Log.d("mensagem","Acumulado Ano 21: "+saldoAcumulado2Ano21);
        Log.d("mensagem","Acumulado Ano 22: "+saldoAcumulado2Ano22);
        Log.d("mensagem","Acumulado Ano 23: "+saldoAcumulado2Ano22);
        Log.d("mensagem","Acumulado Ano 24: "+saldoAcumulado2Ano23);
        Log.d("mensagem","Acumulado Ano 25: "+saldoAcumulado2Ano24);


        contadorPayback2 = 0;
        if(saldoAcumulado2Ano1 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano2 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano3 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano4 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano5 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano6 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano7 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano8 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano9 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano10 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano11 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano12 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano13 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano14 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano15 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano16 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano17 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano18 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano19 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano20 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano21 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano22 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano23 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano24 < 0){
            contadorPayback2 = contadorPayback2+1;
        }
        if(saldoAcumulado2Ano25 < 0){
            contadorPayback2 = contadorPayback2+1;
        }

        parcelaAno1 = capital/12;
        economiaMensalAno1 = valorEconomiaAno1 / 12;
        economiaMensalAno2 = valorEconomiaAno2 / 12;
        economiaMensalAno3 = valorEconomiaAno3 / 12;
        economiaMensalAno4 = valorEconomiaAno4 / 12;
        economiaMensalAno5 = valorEconomiaAno5 / 12;

        economiaMesAno1 = economiaMensalAno1 + parcelaAno1;
        economiaMesAno2 = economiaMensalAno2 + parcelaAno1;
        economiaMesAno3 = economiaMensalAno3 + parcelaAno1;
        economiaMesAno4 = economiaMensalAno4 + parcelaAno1;
        economiaMesAno5 = economiaMensalAno5 + parcelaAno1;


        Log.d("mensagem","Parcela Ano: "+parcelaAno1);
        Log.d("mensagem","economiaMensalAno1: "+economiaMensalAno1);
        Log.d("mensagem","economiaMensalAno2: "+economiaMensalAno2);
        Log.d("mensagem","economiaMensalAno3: "+economiaMensalAno3);
        Log.d("mensagem","economiaMensalAno4: "+economiaMensalAno4);
        Log.d("mensagem","economiaMensalAno5: "+economiaMensalAno5);

        Log.d("mensagem","economiaMesAno1: "+economiaMesAno1);
        Log.d("mensagem","economiaMesAno2: "+economiaMesAno2);
        Log.d("mensagem","economiaMesAno3: "+economiaMesAno3);
        Log.d("mensagem","economiaMesAno4: "+economiaMesAno4);
        Log.d("mensagem","economiaMesAno5: "+economiaMesAno5);

        Log.d("mensagem","VPL: ");//+df.format(VPL)
        Log.d("mensagem","TIR: ");
        Log.d("mensagem","PAYBACK 2: "+contadorPayback2);


        Log.d("mensagem","Energia 25 Anos 2: "+inteiroEnergiaProduzida25Anos);
        Log.d("mensagem","Economia de Energia em 25 Anos 2: "+df.format(EconomiaDeEnergiaProduzida25Anos));
        Log.d("mensagem","Fluxo de Caixa Acumulado 2: "+df.format(saldoAcumulado2Ano25));
        Log.d("mensagem","VPL: ");//+df.format(VPL)
        Log.d("mensagem","TIR: ");
        Log.d("mensagem","PAYBACK 2: "+contadorPayback2);

    }

    public static double getIRR(final double[] cashFlows)
    {
        final int MAX_ITER = 24;
        double EXCEL_EPSILON = 0.0000001;

        double x = 0.1;
        int iter = 0;
        while (iter++ < MAX_ITER) {

            final double x1 = 1.0 + x;
            double fx = 0.0;
            double dfx = 0.0;
            for (int i = 0; i < cashFlows.length; i++) {
                final double v = cashFlows[ i ];
                final double x1_i = Math.pow( x1, i );
                fx += v / x1_i;
                final double x1_i1 = x1_i * x1;
                dfx += -i * v / x1_i1;
            }
            final double new_x = x - fx / dfx;
            final double epsilon = Math.abs( new_x - x );

            if (epsilon <= EXCEL_EPSILON) {
                if (x == 0.0 && Math.abs( new_x ) <= EXCEL_EPSILON) {
                    return 0.0; // OpenOffice calc does this
                }
                else {
                    return new_x*100;
                }
            }
            x = new_x;
        }
        return x;
    }

    private void calculaMediaMes(){
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        Cursor linhas = db.rawQuery("SELECT *  FROM " + TABELACONSUMOMES+ " WHERE IDPROPOSTA = "+ idProposta +";", null);
        int totalMes1 = 0, totalMes2 = 0, totalMes3 = 0, totalMes4 = 0, totalMes5 = 0, totalMes6 = 0, totalMes7 = 0, totalMes8 = 0, totalMes9 = 0, totalMes10 = 0, totalMes11 = 0, totalMes12 = 0;
        int contador = 0;
        int valores = 0;
        int totalValores = 0;
        int valorMedicao = 0;


        Cursor linhas2 = db.rawQuery("SELECT * FROM " + TABELAPROPOSTA + " WHERE ID = " + idProposta + ";", null);
        if (linhas2.moveToFirst()) {
            do {
                nomeCliente = linhas2.getString(3);
                proposta = linhas2.getString(2);
                data = linhas2.getString(1);
                endereco = linhas2.getString(4);
                tipoTelhado = linhas2.getString(12);
                porcentagemDesconto  = 0.0;
                deslocamentoKM = Integer.parseInt(linhas2.getString(5));
                Double KWH  = Double.parseDouble(linhas2.getString(9));
                Double valor = Double.parseDouble(linhas2.getString(10));
                KWinstaladoFinal = String.valueOf(valor/KWH);
                //Log.d("mesc","1"+valorMedicao);
            }
            while (linhas2.moveToNext());
            linhas2.close();
            //consumoAnoFinal = String.valueOf(totalValores - (valorMedicao*12));

            mediaMedicaoAno = valorMedicao*12;
            //Log.d("mesc","2"+valorMedicao);
        }

        int contMono=0, contBi=0, contTri=0;

        if (linhas.moveToFirst()) {
            do {
                contador = contador + 1;

                totalMes1 = totalMes1 + Integer.parseInt(linhas.getString(1));
                totalMes2 = totalMes2 + Integer.parseInt(linhas.getString(2));
                totalMes3 = totalMes3 + Integer.parseInt(linhas.getString(3));
                totalMes4 = totalMes4 + Integer.parseInt(linhas.getString(4));
                totalMes5 = totalMes5 + Integer.parseInt(linhas.getString(5));
                totalMes6 = totalMes6 + Integer.parseInt(linhas.getString(6));
                totalMes7 = totalMes7 + Integer.parseInt(linhas.getString(7));
                totalMes8 = totalMes8 + Integer.parseInt(linhas.getString(8));
                totalMes9 = totalMes9 + Integer.parseInt(linhas.getString(9));
                totalMes10 = totalMes10 + Integer.parseInt(linhas.getString(10));
                totalMes11 = totalMes11 + Integer.parseInt(linhas.getString(11));
                totalMes12 = totalMes12 + Integer.parseInt(linhas.getString(12));

                Log.d("Banco ","Visualizacao: "+ linhas.getString(13));

                if (linhas.getString(13).equals("Monofásica")){
                    valorMedicao = 30;
                    contMono = contMono+1;

                } else if (linhas.getString(13).equals("Bifásica")){
                    valorMedicao = 50;
                    contBi = contBi+1;

                } else if (linhas.getString(13).equals("Trifásica")){
                    valorMedicao = 100;
                    contTri = contTri+1;

                }
                Log.d("Brasilllllllll",KWinstaladoFinal);
                Log.d("Brasil - Valor medicao",""+valorMedicao);
                Log.d("Brasil -Parte 1",""+((valorMedicao*Double.parseDouble(KWinstaladoFinal))*12));
                mediaGrafico = mediaGrafico + ((valorMedicao*Double.parseDouble(KWinstaladoFinal))*12);
                Log.d("Brasilll - Media",""+mediaGrafico);
                valores = Integer.parseInt(linhas.getString(1))+ Integer.parseInt(linhas.getString(2))+ Integer.parseInt(linhas.getString(3))+Integer.parseInt(linhas.getString(4))+Integer.parseInt(linhas.getString(5))+Integer.parseInt(linhas.getString(6))+Integer.parseInt(linhas.getString(7))+Integer.parseInt(linhas.getString(8))+Integer.parseInt(linhas.getString(9))+Integer.parseInt(linhas.getString(10))+Integer.parseInt(linhas.getString(11))+Integer.parseInt(linhas.getString(12));
                totalValores = valores + totalValores;
            }
            while (linhas.moveToNext());

            textoUnidadeConsumidora = "Monofásica: "+contMono+", Bifásica: "+contBi+", Trifásica: "+contTri;

            calculoGraficoLaranja = calculoGraficoLaranja + mediaGrafico;
            Log.d("Brasilllllllll222222",""+calculoGraficoLaranja);
            mediaMes1 = totalMes1; // contador
            mediaMes2 = totalMes2; // contador
            mediaMes3 = totalMes3; // contador
            mediaMes4 = totalMes4; // contador
            mediaMes5 = totalMes5; // contador
            mediaMes6 = totalMes6; // contador
            mediaMes7 = totalMes7; // contador
            mediaMes8 = totalMes8; // contador
            mediaMes9 = totalMes9; // contador
            mediaMes10 = totalMes10; // contador
            mediaMes11 = totalMes11; // contador
            mediaMes12 = totalMes12; // contador
            totalMeses = mediaMes1+mediaMes2+mediaMes3+mediaMes4+mediaMes5+mediaMes6+mediaMes7+mediaMes8+mediaMes9+mediaMes10+mediaMes11+mediaMes12;
            consumoXgeracao = totalMeses;
            calculoGraficoAzul = consumoXgeracao * Double.parseDouble(KWinstaladoFinal);

            Log.d("eitaaa-1",""+consumoXgeracao);
            Log.d("eitaaa-2",""+Double.parseDouble(KWinstaladoFinal));
            Log.d("eitaaa-3",""+calculoGraficoAzul);
            mediaAnual = totalMeses/12;
        }

        Cursor linhas3 = db.rawQuery("SELECT * FROM " + TABELAVALORES + " WHERE ANO > " + consumoXgeracao + " ORDER BY ANO LIMIT 1;", null);// ORDER BY ANO desc LIMIT 1
        if (linhas3.moveToFirst()) {
            do {
                sistemaFinal = linhas3.getString(1);
                anoFinal = linhas3.getString(2);

                areaFinal = linhas3.getString(3);
                numeroFinal = linhas3.getString(4);
                placa1Final = linhas3.getString(5);
                placa2Final = linhas3.getString(6);
                valor1Final = linhas3.getString(7);
                pernoiteFinal = linhas3.getString(8);
                startObraFinal = linhas3.getString(9);
                inversorFinal = linhas3.getString(10);
                conexaoFinal = linhas3.getString(11);
                freteFinal = linhas3.getString(12);
                projetoFinal = linhas3.getString(13);
                sistema2Final = linhas3.getString(14);
                quantidadeInversores = linhas3.getString(15);

                sistemaDouble  = Double.parseDouble(sistemaFinal.replace(",", "."));

            }
            while (linhas3.moveToNext());
            linhas3.close();
        }else{
            Log.d("mensagem","ERRRooUUUUUUUUUUUUUUUUUU");

            Cursor pegaMenor = db.rawQuery("SELECT * FROM " + TABELAVALORES + " WHERE ANO < " + consumoXgeracao + " ORDER BY ANO DESC LIMIT 1;", null);// ORDER BY ANO desc LIMIT 1
            if (pegaMenor.moveToFirst()) {
                do {
                    sistemaFinal = pegaMenor.getString(1);
                    anoFinal = pegaMenor.getString(2);

                    areaFinal = pegaMenor.getString(3);
                    numeroFinal = pegaMenor.getString(4);
                    placa1Final = pegaMenor.getString(5);
                    placa2Final = pegaMenor.getString(6);
                    valor1Final = pegaMenor.getString(7);
                    pernoiteFinal = pegaMenor.getString(8);
                    startObraFinal = pegaMenor.getString(9);
                    inversorFinal = pegaMenor.getString(10);
                    conexaoFinal = pegaMenor.getString(11);
                    freteFinal = pegaMenor.getString(12);
                    projetoFinal = pegaMenor.getString(13);
                    sistema2Final = pegaMenor.getString(14);
                    quantidadeInversores = pegaMenor.getString(15);

                    sistemaDouble  = Double.parseDouble(sistemaFinal.replace(",", "."));

                }
                while (linhas3.moveToNext());
                linhas3.close();
            }
        }

        DecimalFormat decimal = new DecimalFormat("0.00");
        DecimalFormat df = new DecimalFormat("###,###,###,###,###.00");
        //DecimalFormat numericoDouble = new DecimalFormat("###.###.###.###.###,00");

        Cursor linhas4 = db.rawQuery("SELECT * FROM " + TABELAVALORESINICIAIS + " ;", null);
        if (linhas4.moveToFirst()) {
            do {

                custoMO = linhas4.getDouble(1);
                lucroResultado = linhas4.getDouble(2);
                valorComissao = linhas4.getDouble(3);
                taxaSicred = linhas4.getDouble(4);
                valorPernoite = linhas4.getDouble(5);
                valorKM = linhas4.getDouble(6);
                valorStart = linhas4.getDouble(7);
                precoPlaca = linhas4.getDouble(8);
                // = linhas4.getDouble(5); //valorPlaca

            }
            while (linhas4.moveToNext());
            linhas4.close();

            valorPernoiteTotal = valorPernoite * Double.parseDouble(pernoiteFinal);
            Log.d("tag34","valorPernoiteTotal: "+valorPernoiteTotal);
            valorKMTotal = valorKM * deslocamentoKM; // este 25 tem que mudar para deslocamento
            Log.d("tag34","valorKMTotal: "+valorKMTotal);
            deslocamento = valorPernoiteTotal + valorKMTotal;
            Log.d("tag34","deslocamento: "+deslocamento);
            valorMaoObra = valorStart * Double.parseDouble(startObraFinal.replace(",", "."));
            Log.d("tag34","valorMaoObra: "+valorMaoObra);
            instalacaoPlaca = precoPlaca * Double.parseDouble(numeroFinal);
            Log.d("tag34","valorPlaca: "+valorPlaca);
            Log.d("tag34","numeroFinal: "+numeroFinal);
            Log.d("tag34","instalacaoPlaca: "+instalacaoPlaca);
            maoObraTotal = (instalacaoPlaca + valorMaoObra + deslocamento)*custoMO;
            Log.d("tag34","maoObraTotal: "+maoObraTotal);
            custoObra = Double.parseDouble(valor1Final.replace(".", "").replace(",", "."))+ maoObraTotal;
            Log.d("tag34","custoObra: "+custoObra);

            custoResultado = custoObra/lucroResultado;
            Log.d("tag34","custoResultado: "+custoResultado);
            desconto = custoResultado*(porcentagemDesconto/100);
            Log.d("tag34","desconto: "+desconto);
            resultado = custoResultado - custoObra - desconto;
            propostaMenosDesconto = custoResultado - desconto;
            acrescimo = propostaMenosDesconto * 0.0; //25283.59
            //ver comissõ venda interna.
            comissao = propostaMenosDesconto*(valorComissao/100);
            propostaMaisComissao = propostaMenosDesconto + acrescimo + comissao;

        }

        NumberFormat formatoComPontoFinal = NumberFormat.getNumberInstance();

        //BigDecimal valor1 = new BigDecimal(propostaMaisComissao).setScale(2, RoundingMode.HALF_EVEN);
        //BigDecimal valor2 = new BigDecimal(propostaMaisComissao/sistemaDouble).setScale(2, RoundingMode.HALF_EVEN);

        DecimalFormat formatacao = new DecimalFormat("###,###,###.00");

        txtMediaMes.setText(""+formatoComPontoFinal.format(mediaAnual)+" kW/h");
        txtConsumoAno.setText(""+formatoComPontoFinal.format(consumoXgeracao)+" kW/h");

        txtSistema.setText(sistemaFinal+" kWp");
        txtAno.setText(""+formatoComPontoFinal.format(Integer.parseInt(anoFinal))+" kW/h Ano"); //df.format(Double.parseDouble(anoFinal))+" KW/h Ano"
        txtArea.setText(areaFinal+" m² Área ");
        txtNumero.setText(numeroFinal+" módulos");
        txtPlaca1.setText("Potência "+placa1Final);

        //BigDecimal valor1 = new BigDecimal(propostaMaisComissao).setScale(2, RoundingMode.HALF_EVEN);
        //BigDecimal valor2 = new BigDecimal(propostaMaisComissao/sistemaDouble).setScale(2, RoundingMode.HALF_EVEN);

        Locale ptBr = new Locale("pt", "BR");

        txtValor.setText("R$ "+formatacao.format(propostaMaisComissao));
        txtKWinstalado.setText("R$ "+formatacao.format(propostaMaisComissao/sistemaDouble));

        BigDecimal valor3 = new BigDecimal(Double.parseDouble(KWinstaladoFinal)).setScale(2, RoundingMode.HALF_EVEN);
        Double consumoMenosGeracao = Double.parseDouble(String.valueOf(consumoXgeracao)); //- mediaMedicaoAno

        BigDecimal valor4 = new BigDecimal(consumoMenosGeracao/Double.parseDouble(KWinstaladoFinal)).setScale(2, RoundingMode.HALF_EVEN);
        calculoEconomia = Double.parseDouble(String.valueOf(valor4));
        Log.d("tag34","consumoXgeracao: "+totalMeses);
        Log.d("tag34","KWinstaladoFinal: "+KWinstaladoFinal);

        //CALCULO GRAFICO LARANJA

        double tarifa = Double.parseDouble(KWinstaladoFinal);
        double geracaoAnualKWH = Integer.parseInt(anoFinal);
        double consumoAnualKWH = consumoXgeracao;
        int diferencaKWH = (int) (geracaoAnualKWH - consumoAnualKWH);
        double valorSemGeracao = calculoGraficoAzul;
        double valorComMedicao = mediaGrafico;
        double valorDaDiferenca = valorComMedicao+((-1 * diferencaKWH)*tarifa);

        BigDecimal bd = new BigDecimal(valorDaDiferenca).setScale(2, RoundingMode.HALF_EVEN);
        valorDaDiferenca = (bd.doubleValue());

        if(diferencaKWH < 0){
            calculoGraficoLaranja = valorDaDiferenca;
        }

        double teste = valorDaDiferenca * 1;


        Log.d("Vai Planeta","teste: "+teste);
        Log.d("Vai Planeta","tarifa: "+tarifa);
        Log.d("Vai Planeta","geracaoAnualKWH: "+geracaoAnualKWH);
        Log.d("Vai Planeta","consumoAnualKWH: "+consumoAnualKWH);
        Log.d("Vai Planeta","diferencaKWH: "+diferencaKWH);
        Log.d("Vai Planeta","valorSemGeracao: "+valorSemGeracao);
        Log.d("Vai Planeta","valorComMedicao: "+valorComMedicao);
        Log.d("Vai Planeta","valorDaDiferenca: "+valorDaDiferenca);
        Log.d("Vai Planeta","calculoGraficoAzul: "+calculoGraficoAzul);
        Log.d("Vai Planeta","calculoGraficoLaranja: "+calculoGraficoLaranja);

        BigDecimal valorEconomiaAno = new BigDecimal(calculoGraficoAzul-calculoGraficoLaranja).setScale(2, RoundingMode.HALF_EVEN);
        txtUltimoValorMedio.setText("R$ "+NumberFormat.getNumberInstance().format(valor3.doubleValue()));
        txtEconomiaAno.setText("R$ "+NumberFormat.getNumberInstance().format(valorEconomiaAno.doubleValue()));

        pagamentoHCC40 =  propostaMaisComissao * 0.40;
        pagamentoHCC30 =  propostaMaisComissao * 0.30;

        BigDecimal valor5 = new BigDecimal(pagamentoHCC40).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal valor6 = new BigDecimal(pagamentoHCC30).setScale(2, RoundingMode.HALF_EVEN);
        txtPrimeiraParcelaHCC.setText("R$ "+formatacao.format(pagamentoHCC40));
        txtSegundaParcelaHCC.setText("R$ "+formatacao.format(pagamentoHCC30));
        txtTerceiraParcelaHCC.setText("R$ "+formatacao.format(pagamentoHCC30));

        parcelaSicred = propostaMaisComissao * taxaSicred;


        txtParcelasSicred.setText("R$ "+formatacao.format(parcelaSicred));

        /*
        pagamentoProprio35 =  propostaMaisComissao * 0.35;
        pagamentoProprio65 =  propostaMaisComissao * 0.65;
        BigDecimal valor7 = new BigDecimal(pagamentoProprio35).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal valor8 = new BigDecimal(pagamentoProprio65).setScale(2, RoundingMode.HALF_EVEN);
        txtPrimeiraParcelaPropria.setText(NumberFormat.getCurrencyInstance(ptBr).format(valor7.doubleValue()));
        txtSegundaParcelaPropria.setText(NumberFormat.getCurrencyInstance(ptBr).format(valor8.doubleValue()));
        */
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent it = new Intent(this, Proposta3.class);
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
            values.put("MEDICAO", "");
            values.put("MACROREGIAO", "");
            values.put("MICROREGIAO", "");
            values.put("KWH", "");
            values.put("VALOR", "");
            values.put("REPRESENTANTE", "");
            values.put("COMISSAO", "");
            values.put("FRETE", "");
            values.put("SOMBREAMENTO", "");
            values.put("DESCONTO", "0,0");
            values.put("VERIFICACAO", "0");

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
            Intent it = new Intent(this, Proposta3.class);
            startActivity(it);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void geraProposta(View v) {
        if(carregamentoGrafico1 != 1 || carregamentoGrafico2 != 1 || carregamentoGrafico3 != 1 || carregamentoGrafico4 != 1){
            Toast.makeText(this, "Aguarde os Gráficos carregarem, para prosseguir!", Toast.LENGTH_SHORT).show();
        }else{
            getJSONGeraPdf();
        }

    }

    private void getJSONGeraPdf(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(VisualizaProposta.this,"Gerando Proposta","Por Favor, aguarde...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
                ContentValues values = new ContentValues();
                values.put("VERIFICACAO", "1");
                db.update(TABELAPROPOSTA, values, "ID=" + idProposta, null);
                db.close();

                Toast.makeText(VisualizaProposta.this, "Proposta Gerada com Sucesso!", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(VisualizaProposta.this, Inicial.class);
                startActivity(it);
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                return "1";
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    // Metodo responsavel por verificar se as permissões foram aceitas
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // Se a permissão foi aceita, chama o metodo para criar o PDF
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Permissão Negada", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // verifica permissões
    public void createPdfWrapper() throws FileNotFoundException, DocumentException {
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("Você precisa permitir o acesso ao Armazenamento",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            try {
                createPdf();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Metodo do Alert
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void createPdf() throws IOException, DocumentException {

        // Verifica se a pasta HCC já existe no smartfone, se não existir, cria a pasta
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/HCC SOLAR");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }
        // Criando o arquivo PDF, com o n da nota como nome do arquivo
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String data_completa = dateFormat.format(data);
        pdfFile = new File(docsFolder.getAbsolutePath(), nomeCliente+"_"+proposta+"_"+data_completa+"_Proposta.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Rectangle pagesize = new Rectangle(589f, 840f);
        Document document = new Document(pagesize, 0, 0, 0, 0);
        PdfWriter.getInstance(document, output);

        document.addTitle("Proposta");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int largura = size.x;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width3 = metrics.widthPixels;
        int height = metrics.heightPixels;

        if (width3 > 2000){
            //Toast.makeText(this, "Tela Grande", Toast.LENGTH_SHORT).show();
            Log.d("Larguraaaaaa", "caiu if: ");
        }else{
            //Toast.makeText(this, "Tela Pequena", Toast.LENGTH_SHORT).show();
            Log.d("Larguraaaaaa", "caiu else: ");
        }

        Log.d("Larguraaaaaa", "Largura: "+largura);
        Log.d("Larguraaaaaa222", "Largura2: "+width3);

        // cria a pagina em PDF
        document.open();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap imagemCapa = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.fundo1);
        Bitmap tamanhoCapa = Bitmap.createScaledBitmap(imagemCapa, 589, 840, true);
        tamanhoCapa.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Image imagemFundoCapa = Image.getInstance(stream.toByteArray());

        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        Bitmap imagemFundo = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.fundo2);
        Bitmap tamanhoImagemFundo = Bitmap.createScaledBitmap(imagemFundo, 589, 840, true);
        tamanhoImagemFundo.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
        Image marcaDagua = Image.getInstance(stream2.toByteArray());

        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        Bitmap imagemCantoInferior = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.embaixo);
        //Bitmap tamanhoImagemCantoInferior = Bitmap.createScaledBitmap(imagemCantoInferior, 92, 51, true);
        imagemCantoInferior.compress(Bitmap.CompressFormat.JPEG, 100, stream3);

        //int Width1 = imagemCantoInferior.getWidth();
        //Log.d("mensagem","Imagem Canto: "+ Width1);
        Image imgCantoInferior = Image.getInstance(stream3.toByteArray());

        imgCantoInferior.scalePercent(9);
        int Width1 = (int) imgCantoInferior.getScaledWidth();
        Log.d("mensagem","Width1: "+ Width1);

        if(Width1 >= 124){
            imgCantoInferior.scalePercent(9);

        }else if(Width1 < 124 && Width1 >= 99){
            imgCantoInferior.scalePercent(11);

        }else if(Width1 < 99 && Width1 >= 74){
            imgCantoInferior.scalePercent(13);

        }else if(Width1 < 74 && Width1 >= 50){
            imgCantoInferior.scalePercent(15);

        }else if(Width1 < 50){
            imgCantoInferior.scalePercent(18);
        }

/*
        if (width3 > 2000){
            imgCantoInferior.scalePercent(15);
        }else{
        */
            //imgCantoInferior.scalePercent(9);
            //float Width10 = imgCantoInferior.getWidth();
            //float Width11 = imgCantoInferior.getScaledWidth();
            //Log.d("mensagem","Imagem Canto: "+ Width10);
            //Log.d("mensagem","Imagem Canto2: "+ Width11);
       // }

        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL);
        fontTitulo.setColor(255,255,0);
        Font fontTextoCapa = new Font(Font.FontFamily.HELVETICA, 18, Font.NORMAL, BaseColor.WHITE);
        Font fontNumeroProposta = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.GREEN);
        //Font fontTextoNormal = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLUE);
        //fontTextoNormal.setSize(18);

        Font textoNormalAzul = new Font(Font.FontFamily.HELVETICA, 25, Font.NORMAL);
        textoNormalAzul.setSize(15);
        textoNormalAzul.setColor(70,130,180);
/*
        BaseFont futura_bold = BaseFont.createFont("resources/fonts/Futura Bold font.ttf", "CP1251", BaseFont.EMBEDDED);
        BaseFont futura_BK_BT = BaseFont.createFont("resources/fonts/futura medium bt.ttf", "CP1251", BaseFont.EMBEDDED);

        Font base11Azul20= new Font(futura_bold, 20);
        Font base22Azul20= new Font(futura_BK_BT, 20);
        Font base1Branco22= new Font(futura_BK_BT, 22);
        Font base2Branco12= new Font(futura_BK_BT, 12);
        base11Azul20.setColor(70,130,180);
        base22Azul20.setColor(70,130,180);
        base1Branco22.setColor(255,255,255);
        base2Branco12.setColor(255,255,255);
*/
        //Font textoNormalAzul = new Font(bf_russian, 15);
        //textoNormalAzul.setColor(70,130,180);

        //Primeira Pagina
        imagemFundoCapa.setAbsolutePosition(0, 0);
        document.add(imagemFundoCapa);

        Font tituloBranco = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.WHITE);
        tituloBranco.setSize(18);
        Paragraph pTitulo = new Paragraph(new Phrase(30F , "PROPOSTA", tituloBranco));
        pTitulo.setAlignment(Element.ALIGN_CENTER);

        Font textoAzulTitulos = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
        textoAzulTitulos.setSize(17);
        textoAzulTitulos.setColor(70,130,180);

        Font tituloBranco2 = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.WHITE);
        tituloBranco2.setSize(18);
        Paragraph pTitulo2 = new Paragraph(new Phrase(30F , "COMERCIAL/TÉCNICA", tituloBranco2));
        pTitulo2.setAlignment(Element.ALIGN_CENTER);

        Font textoBranco = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD, BaseColor.WHITE);
        textoBranco.setSize(15);
        Paragraph txtBranco = new Paragraph(new Phrase(30F , "ID "+proposta, textoBranco));
        txtBranco.setAlignment(Element.ALIGN_CENTER);

        Paragraph espaco = new Paragraph("\n\n\n\n\n\n\n");
        document.add(espaco);

        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{ 589, 80 });
        table.setLockedWidth(true);

        List list = new List();
        list.add(new ListItem(pTitulo));
        list.add(new ListItem(pTitulo2));
        list.add(new ListItem(txtBranco));

        PdfPCell cell = new PdfPCell(new Phrase());
        cell.addElement(list);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(110);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBackgroundColor(new BaseColor(91,155,213));
        cell.setColspan(2);
        table.addCell(cell);
        document.add(table);

        Paragraph espaco2 = new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        document.add(espaco2);

        Font textoAzul = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
        textoAzul.setSize(22);
        textoAzul.setColor(70,130,180);
        Paragraph txtAzul = new Paragraph(new Phrase(30F , "SISTEMA FOTOVOLTAICO DE "+sistemaFinal+" kWp", textoAzul));
        txtAzul.setAlignment(Element.ALIGN_CENTER);
        document.add(txtAzul);

        Paragraph espaco3 = new Paragraph("\n");
        document.add(espaco3);

        Paragraph txtAzul2 = new Paragraph(new Phrase(30F , "             Cliente: "+nomeCliente, textoAzul));
        txtAzul2.setAlignment(Element.ALIGN_LEFT);
        document.add(txtAzul2);

        Paragraph txtAzul3 = new Paragraph(new Phrase(30F , "             Cidade: "+endereco, textoAzul));
        txtAzul3.setAlignment(Element.ALIGN_LEFT);
        document.add(txtAzul3);

        Paragraph espaco4 = new Paragraph("\n\n\n");
        document.add(espaco4);

        //Date data =  new Date();
        Locale local = new Locale("pt","BR");
        DateFormat formatoData = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy",local);

        Font dataAzul = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
        dataAzul.setSize(15);
        dataAzul.setColor(70,130,180);
        Paragraph txtData = new Paragraph(new Phrase(30F , "Santa Maria, "+formatoData.format(data), dataAzul));
        txtData.setAlignment(Element.ALIGN_CENTER);
        document.add(txtData);


        document.newPage();

        // Segunda Página
        marcaDagua.setAbsolutePosition(0, 0);
        document.add(marcaDagua);

        imgCantoInferior.setAbsolutePosition(230, 3);
        document.add(imgCantoInferior);

        Font tituloBranco3 = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.WHITE);
        tituloBranco3.setSize(18);
        Paragraph titulo1pg2 = new Paragraph(new Phrase(30F , "Apresentação da Empresa", tituloBranco3));
        titulo1pg2.setAlignment(Element.ALIGN_CENTER);

        Font textoBrancoPg2 = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.WHITE);
        textoBrancoPg2.setSize(13);
        Paragraph paragrafo1pg2 = new Paragraph(new Phrase(10F , "O grupo HCC Engenharia atento ao movimento de geração de energia elétrica de forma limpa e sustentável, apresenta a CELIBI SERVIÇOS LTDA ME empresa do grupo que será a responsável pela execução e fornecimento de materiais, inscrita no CNPJ 11.197.873/0001-52. A empresa atua há 13 anos no setor elétrico e é uma das 20 maiores integradores de energia solar no Brasil, segundo dados da Greener.Conheça alguns de nossos cases de sucesso:", textoBrancoPg2));
        paragrafo1pg2.setAlignment(Element.ALIGN_JUSTIFIED);
        paragrafo1pg2.setLeading(80);
        paragrafo1pg2.setIndentationLeft(40);

        Paragraph espaco1Pg2 = new Paragraph("\n\n");
        document.add(espaco1Pg2);

        // primeira linha
        PdfPTable table34 = new PdfPTable(2);
        table34.setTotalWidth(new float[]{ 500, 130 });
        table34.setLockedWidth(true);
        PdfPCell cell34 = new PdfPCell(new Phrase(titulo1pg2));
        cell34.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell34.setFixedHeight(25);
        cell34.setBackgroundColor(new BaseColor(91,155,213));
        cell34.setBorder(Rectangle.NO_BORDER);
        cell34.setColspan(2);
        table34.addCell(cell34);

        // segunda linha
        PdfPCell cell35 = new PdfPCell(new Phrase(paragrafo1pg2));
        cell35.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell35.setFixedHeight(100);
        cell35.setPaddingRight(35);
        cell35.setPaddingLeft(35);
        cell35.setLeading(0f, 1.2f);
        cell35.setBackgroundColor(new BaseColor(91,155,213));
        cell35.setBorder(Rectangle.NO_BORDER);
        cell35.setColspan(2);
        table34.addCell(cell35);
        document.add(table34);

        ByteArrayOutputStream streamImagem1 = new ByteArrayOutputStream();
        Bitmap imagem1 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.pg2img1);
        //Bitmap tamanhoImg1 = Bitmap.createScaledBitmap(imagem1, 210, 210, true);
        imagem1.compress(Bitmap.CompressFormat.JPEG, 100, streamImagem1);
        Image img1pa2 = Image.getInstance(streamImagem1.toByteArray());

        //int Width2 = tamanhoImg1.getWidth();

        //Log.d("mensagem","tamanhoImg1: "+ Width2);

        img1pa2.scalePercent(15);
        int Width2 = (int) img1pa2.getScaledWidth();
        Log.d("mensagem","Width2: "+ Width2);
/*
        if(Width2 != 202){
            img1pa2.scalePercent(40);
        }
*/

        if(Width2 >= 202){
            img1pa2.scalePercent(14);

        }else if(Width2 < 202 && Width2 >= 162){
            img1pa2.scalePercent(18);

        }else if(Width2 < 162 && Width2 >= 122){
            img1pa2.scalePercent(22);

        }else if(Width2 < 122 && Width2 >= 82){
            img1pa2.scalePercent(26);

        }else if(Width2 < 82){
            img1pa2.scalePercent(30);
        }





        //if (width3 > 2000){
            //img1pa2.scalePercent(32);
       // }else{
            //img1pa2.scalePercent(15);
        //}

        img1pa2.setAlignment(Element.ALIGN_LEFT);

        ByteArrayOutputStream streamImagem2 = new ByteArrayOutputStream();
        Bitmap imagem2 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.pg2img2);
        //Bitmap tamanhoImg2 = Bitmap.createScaledBitmap(imagem2, 210, 210, true);
        imagem2.compress(Bitmap.CompressFormat.JPEG, 100, streamImagem2);
        Image img2pa2 = Image.getInstance(streamImagem2.toByteArray());

        //int Width3 = tamanhoImg2.getWidth();
        //Log.d("mensagem","tamanhoImg2: "+ Width3);

        img2pa2.scalePercent(15);
        int Width3 = (int) img2pa2.getScaledWidth();
        Log.d("mensagem","Width3: "+ Width3);


/*
        if(Width3 != 202){
            img2pa2.scalePercent(40);
        }
*/
        if(Width3 >= 202){
            img2pa2.scalePercent(14);

        }else if(Width3 < 202 && Width3 >= 162){
            img2pa2.scalePercent(18);

        }else if(Width3 < 162 && Width3 >= 122){
            img2pa2.scalePercent(22);

        }else if(Width3 < 122 && Width3 >= 82){
            img2pa2.scalePercent(26);

        }else if(Width3 < 82){
            img2pa2.scalePercent(30);
        }

        /*
        if (width3 > 2000){
            img2pa2.scalePercent(32);
        }else{
            img2pa2.scalePercent(15);
        }
        */
        img2pa2.setAlignment(Element.ALIGN_RIGHT);

        Paragraph espaco2Pg2 = new Paragraph("\n");
        document.add(espaco2Pg2);

        PdfPTable tablewer = new PdfPTable(2);
        tablewer.setTotalWidth(new float[]{ 290, 230 });
        tablewer.setLockedWidth(true);
        Paragraph p = new Paragraph();
        p.add("    ");
        p.add(new Chunk(img1pa2, 0, 0, true));
        p.add("               ");
        p.add(new Chunk(img2pa2, 0, 0, true));
        PdfPCell cellsdsd = new PdfPCell();
        cellsdsd.addElement(p);
        cellsdsd.setColspan(2);
        cellsdsd.setBorder(Rectangle.NO_BORDER);
        tablewer.addCell(cellsdsd);
        document.add(tablewer);

        document.add(espaco2Pg2);

        ByteArrayOutputStream streamImagem3 = new ByteArrayOutputStream();
        Bitmap imagem3 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.pg2img3);
        //Bitmap tamanhoImg3 = Bitmap.createScaledBitmap(imagem3, 210, 210, true);
        imagem3.compress(Bitmap.CompressFormat.JPEG, 100, streamImagem3);
        Image img3pa2 = Image.getInstance(streamImagem3.toByteArray());


        //int Width4 = tamanhoImg3.getWidth();
        //Log.d("mensagem","tamanhoImg3: "+ Width4);

        img3pa2.scalePercent(15);
        int Width4 = (int) img3pa2.getScaledWidth();
        Log.d("mensagem","Width4: "+ Width4);
/*
        if(Width4 != 202){
            img3pa2.scalePercent(40);
        }
*/

        if(Width4 >= 202){
            img3pa2.scalePercent(14);

        }else if(Width4 < 202 && Width4 >= 162){
            img3pa2.scalePercent(18);

        }else if(Width4 < 162 && Width4 >= 122){
            img3pa2.scalePercent(22);

        }else if(Width4 < 122 && Width4 >= 82){
            img3pa2.scalePercent(26);

        }else if(Width4 < 82){
            img3pa2.scalePercent(30);
        }

        /*
        if (width3 > 2000){
            img3pa2.scalePercent(32);
        }else{
            img3pa2.scalePercent(15);
        }
        */
        img3pa2.setAlignment(Element.ALIGN_CENTER);
        document.add(img3pa2);

        document.add(espaco2Pg2);

        Font tituloBranco4 = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.WHITE);
        tituloBranco4.setSize(18);
        Paragraph titulo2pg2 = new Paragraph(new Phrase(30F , "Objetivo", tituloBranco4));
        titulo2pg2.setAlignment(Element.ALIGN_CENTER);

        Font textoBranco2Pg2 = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.WHITE);
        textoBranco2Pg2.setSize(14);
        Paragraph paragrafo2pg2 = new Paragraph(new Phrase(10F , "A presente proposta tem como principal objetivo o Fornecimento de Materiais e Mão de Obra para construção da Microgeração Distribuída Fotovoltaica, bem como a Elaboração de Projetos Elétricos e Liberação do mesmo junto a concessionária. Denominado Microgeração Distribuída Fotovoltaica de "+sistemaFinal+" kWp", textoBranco2Pg2));
        paragrafo2pg2.setAlignment(Element.ALIGN_JUSTIFIED);
        paragrafo2pg2.setLeading(5);

        // primeira linha
        PdfPTable table2pg2 = new PdfPTable(2);
        table2pg2.setTotalWidth(new float[]{ 500, 130 });
        table2pg2.setLockedWidth(true);
        PdfPCell cell1tabela2 = new PdfPCell(new Phrase(titulo2pg2));
        cell1tabela2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1tabela2.setFixedHeight(25);
        cell1tabela2.setBackgroundColor(new BaseColor(91,155,213));
        cell1tabela2.setBorder(Rectangle.NO_BORDER);
        cell1tabela2.setColspan(2);
        table2pg2.addCell(cell1tabela2);

        // segunda linha
        PdfPCell cell2tabela2 = new PdfPCell(new Phrase(paragrafo2pg2));
        cell2tabela2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cell2tabela2.setFixedHeight(85);
        cell2tabela2.setPaddingRight(35);
        cell2tabela2.setPaddingLeft(35);
        cell2tabela2.setLeading(0f, 1.2f);
        cell2tabela2.setBackgroundColor(new BaseColor(91,155,213));
        cell2tabela2.setBorder(Rectangle.NO_BORDER);
        cell2tabela2.setColspan(2);
        table2pg2.addCell(cell2tabela2);
        document.add(table2pg2);

        document.newPage();

        // Terceira Página
        marcaDagua.setAbsolutePosition(0, 0);
        document.add(marcaDagua);

        imgCantoInferior.setAbsolutePosition(230, 3);
        document.add(imgCantoInferior);

        document.add(espaco1Pg2);


        // primeira linha
        Paragraph titulo1pg3 = new Paragraph(new Phrase(30F , "Módulos Fotovoltaicos", tituloBranco4));
        titulo1pg3.setAlignment(Element.ALIGN_CENTER);

        PdfPTable table1pg3 = new PdfPTable(2);
        table1pg3.setTotalWidth(new float[]{ 500, 130 });
        table1pg3.setLockedWidth(true);
        PdfPCell cell1tabela1pg3 = new PdfPCell(new Phrase(titulo1pg3));
        cell1tabela1pg3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1tabela1pg3.setFixedHeight(25);
        cell1tabela1pg3.setBackgroundColor(new BaseColor(91,155,213));
        cell1tabela1pg3.setBorder(Rectangle.NO_BORDER);
        cell1tabela1pg3.setColspan(2);
        table1pg3.addCell(cell1tabela1pg3);
        document.add(table1pg3);

        document.add(espaco1Pg2);

        ByteArrayOutputStream streamImagemModulo = new ByteArrayOutputStream();
        Bitmap imagemModuloFoto = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imgmodulo);
        //Bitmap tamanhoImg4 = Bitmap.createScaledBitmap(imagemModuloFoto, 199, 89, true);
        imagemModuloFoto.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemModulo);
        Image imgModuloFoto = Image.getInstance(streamImagemModulo.toByteArray());

        //imgModuloFoto.scalePercent(16);
        //int Width12 = (int) imgModuloFoto.getScaledWidth();
        //Log.d("mensagem","Width12: "+ Width12);

        imgModuloFoto.scalePercent(16);
        int Width5 = (int) imgModuloFoto.getScaledWidth();
        Log.d("mensagem","Width5: "+ Width5);
/*
        if(Width5 != 227){
            imgModuloFoto.scalePercent(32);
        }
*/

        if(Width5 >= 227){
            imgModuloFoto.scalePercent(16);

        }else if(Width5 < 227 && Width5 >= 182){
            imgModuloFoto.scalePercent(20);

        }else if(Width5 < 182 && Width5 >= 137){
            imgModuloFoto.scalePercent(24);

        }else if(Width5 < 137 && Width5 >= 92){
            imgModuloFoto.scalePercent(28);

        }else if(Width5 < 92){
            imgModuloFoto.scalePercent(32);
        }

        /*
        if (width3 > 2000){
            imgModuloFoto.scalePercent(22);
        }else{
            imgModuloFoto.scalePercent(16);
        }
        */

        Font textoNormalAzulNovo = new Font(Font.FontFamily.HELVETICA, 25, Font.NORMAL);
        textoNormalAzulNovo.setSize(14);
        textoNormalAzulNovo.setColor(70,130,180);

        PdfPTable table1Pg3 = new PdfPTable(2);
        table1Pg3.setTotalWidth(new float[]{ 300, 230 });
        table1Pg3.setLockedWidth(true);
        Paragraph p1pg3 = new Paragraph();
        Paragraph p2pg3 = new Paragraph();

        p1pg3.add(" Quantidade de módulos fotovoltaicos: "+numeroFinal+" \n Marca: Canadian Solar ou Yingli Solar \n Modelo: Poli cristalino \n Potência: "+placa1Final+" \n Garantia: 10 anos \n Vida útil: 25 anos \n Distribuídos em uma área de "+areaFinal+" m².");
        p1pg3.setFont(textoNormalAzulNovo);
        p2pg3.add(new Chunk(imgModuloFoto, 0, 0, true));
        PdfPCell celula1Table1Pg3 = new PdfPCell();
        PdfPCell celula2Table1Pg3 = new PdfPCell();
        celula1Table1Pg3.addElement(p1pg3);
        celula2Table1Pg3.addElement(p2pg3);
        celula1Table1Pg3.setPaddingLeft(15);
        celula2Table1Pg3.setPaddingTop(20);
        celula1Table1Pg3.setColspan(1);
        celula2Table1Pg3.setColspan(1);
        celula1Table1Pg3.setBorder(Rectangle.NO_BORDER);
        celula2Table1Pg3.setBorder(Rectangle.NO_BORDER);
        table1Pg3.addCell(celula1Table1Pg3);
        table1Pg3.addCell(celula2Table1Pg3);
        document.add(table1Pg3);

        Font textoNormalAzulNovoMenor = new Font(Font.FontFamily.HELVETICA, 25, Font.NORMAL);
        textoNormalAzulNovoMenor.setSize(13);
        textoNormalAzulNovoMenor.setColor(70,130,180);

        Paragraph textoNovo = new Paragraph(new Phrase(30F , "\n* Poderão ser utilizados módulos fotovoltaicos com potência diferente, porém a potência total do sistema será ajustada de forma a não prejudicar o rendimento do sistema.", textoNormalAzulNovoMenor));
        textoNovo.setAlignment(Element.ALIGN_JUSTIFIED);
        textoNovo.setLeading(16);
        textoNovo.setPaddingTop(20);
        textoNovo.setIndentationLeft(50);
        textoNovo.setIndentationRight(37);
        document.add(textoNovo);

        document.add(espaco1Pg2);

        // primeira linha
        Paragraph titulo2pg3 = new Paragraph(new Phrase(30F , "Inversores", tituloBranco4));
        titulo2pg3.setAlignment(Element.ALIGN_CENTER);

        PdfPTable table2pg3 = new PdfPTable(2);
        table2pg3.setTotalWidth(new float[]{ 500, 130 });
        table2pg3.setLockedWidth(true);
        PdfPCell cell1tabela2pg3 = new PdfPCell(new Phrase(titulo2pg3));
        cell1tabela2pg3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1tabela2pg3.setFixedHeight(25);
        cell1tabela2pg3.setBackgroundColor(new BaseColor(91,155,213));
        cell1tabela2pg3.setBorder(Rectangle.NO_BORDER);
        cell1tabela2pg3.setColspan(2);
        table2pg3.addCell(cell1tabela2pg3);
        document.add(table2pg3);

        document.add(espaco1Pg2);

        ByteArrayOutputStream streamImagemInversor = new ByteArrayOutputStream();
        Bitmap imagemInversorFoto = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imginversor);
        //Bitmap tamanhoImg5 = Bitmap.createScaledBitmap(imagemInversorFoto, 90, 114, true);
        imagemInversorFoto.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemInversor);
        Image imgInversorFoto = Image.getInstance(streamImagemInversor.toByteArray());

        //tamanhoImg5.getWidth();
        //Log.d("mensagem","tamanhoImg5: "+ tamanhoImg5);

        imgInversorFoto.scalePercent(16);
        int Width6 = (int) imgInversorFoto.getScaledWidth();
        Log.d("mensagem","Width6: "+ Width6);
/*
        if(Width6 != 113){
            imgInversorFoto.scalePercent(32);
        }
*/
        if(Width6 >= 113){
            imgInversorFoto.scalePercent(16);

        }else if(Width6 < 113 && Width6 >= 90){
            imgInversorFoto.scalePercent(20);

        }else if(Width6 < 90 && Width6 >= 67){
            imgInversorFoto.scalePercent(24);

        }else if(Width6 < 67 && Width6 >= 46){
            imgInversorFoto.scalePercent(28);

        }else if(Width6 <= 45){
            imgInversorFoto.scalePercent(32);
        }


        /*
        if (width3 > 2000){
            imgInversorFoto.scalePercent(22);
        }else{
            imgInversorFoto.scalePercent(16);
        }
*/
        PdfPTable table3pg3 = new PdfPTable(2);
        table3pg3.setTotalWidth(new float[]{ 300, 230 });
        table3pg3.setLockedWidth(true);
        Paragraph p3pg3 = new Paragraph();
        Paragraph p4pg3 = new Paragraph();
        Paragraph p5pg3 = new Paragraph();
        p3pg3.add(" Quantidade de inversores: "+quantidadeInversores+" \n Marca: Fronius \n Garantia: 7 anos.");
        p3pg3.setFont(textoNormalAzul);
        //p5pg3.add("*Pode-se ser utilizados inversores similares, de marcas como ABB, NHS, mantendo as especificações técnicas.");
       // p5pg3.setFont(textoNormalAzul);
        p4pg3.add(new Chunk(imgInversorFoto, 0, 0, true));
        PdfPCell celula1Table2Pg3 = new PdfPCell();
        PdfPCell celula2Table2Pg3 = new PdfPCell();
        PdfPCell celula3Table2Pg3 = new PdfPCell();
        celula1Table2Pg3.addElement(p3pg3);
        celula3Table2Pg3.addElement(p5pg3);
        celula2Table2Pg3.addElement(p4pg3);
        celula2Table2Pg3.setPaddingLeft(40);
        celula1Table2Pg3.setColspan(1);
        celula2Table2Pg3.setColspan(1);
        celula1Table2Pg3.setPaddingLeft(15);
        celula3Table2Pg3.setColspan(2);
        celula1Table2Pg3.setBorder(Rectangle.NO_BORDER);
        celula2Table2Pg3.setBorder(Rectangle.NO_BORDER);
        celula3Table2Pg3.setBorder(Rectangle.NO_BORDER);
        table3pg3.addCell(celula1Table2Pg3);
        table3pg3.addCell(celula2Table2Pg3);
        table3pg3.addCell(celula3Table2Pg3);
        document.add(table3pg3);
        document.add(espaco2Pg2);

        // primeira linha
        Paragraph titulo3pg3 = new Paragraph(new Phrase(30F , "Estruturas", tituloBranco4));
        titulo3pg3.setAlignment(Element.ALIGN_CENTER);

        PdfPTable table5pg3 = new PdfPTable(2);
        table5pg3.setTotalWidth(new float[]{ 500, 130 });
        table5pg3.setLockedWidth(true);
        PdfPCell cell1tabela5pg3 = new PdfPCell(new Phrase(titulo3pg3));
        cell1tabela5pg3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1tabela5pg3.setFixedHeight(25);
        cell1tabela5pg3.setBackgroundColor(new BaseColor(91,155,213));
        cell1tabela5pg3.setBorder(Rectangle.NO_BORDER);
        cell1tabela5pg3.setColspan(2);
        table5pg3.addCell(cell1tabela5pg3);
        document.add(table5pg3);

        document.add(espaco1Pg2);

        ByteArrayOutputStream  streamImagemEstrutura= new ByteArrayOutputStream();
        Bitmap imagemEstruturaFoto = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imgestrutura);
        //Bitmap tamanhoImg6 = Bitmap.createScaledBitmap(imagemEstruturaFoto, 210, 98, true);
        imagemEstruturaFoto.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemEstrutura);
        Image imgEstruturaFoto = Image.getInstance(streamImagemEstrutura.toByteArray());


        imgEstruturaFoto.scalePercent(16);
        int Width7 = (int) imgEstruturaFoto.getScaledWidth();
        Log.d("mensagem","Width7: "+ Width7);
/*
        if(Width7 != 251){
            imgEstruturaFoto.scalePercent(32);
        }
*/
        if(Width7 >= 251){
            imgEstruturaFoto.scalePercent(16);

        }else if(Width7 < 251 && Width7 >= 201){
            imgEstruturaFoto.scalePercent(20);

        }else if(Width7 < 201 && Width7 >= 151){
            imgEstruturaFoto.scalePercent(24);

        }else if(Width7 < 151 && Width7 >= 101){
            imgEstruturaFoto.scalePercent(28);

        }else if(Width7 < 101){
            imgEstruturaFoto.scalePercent(32);
        }

        /*
        if (width3 > 2000){
            imgEstruturaFoto.scalePercent(22);
        }else{
            imgEstruturaFoto.scalePercent(16);
        }
*/
        PdfPTable table4pg3 = new PdfPTable(2);
        table4pg3.setTotalWidth(new float[]{ 300, 230 });
        table4pg3.setLockedWidth(true);
        Paragraph p6pg3 = new Paragraph();
        Paragraph p7pg3 = new Paragraph();
        p6pg3.add(" Fabricante: Solar Group \n Material: Alumínio \n Vida útil: 50 anos.");
        p6pg3.setFont(textoNormalAzul);
        p7pg3.add(new Chunk(imgEstruturaFoto, 0, 0, true));
        PdfPCell celula1Table3Pg3 = new PdfPCell();
        PdfPCell celula2Table3Pg3 = new PdfPCell();
        celula1Table3Pg3.addElement(p6pg3);
        celula2Table3Pg3.addElement(p7pg3);
        celula1Table3Pg3.setPaddingLeft(15);
        celula1Table3Pg3.setColspan(1);
        celula2Table3Pg3.setColspan(1);
        celula1Table3Pg3.setBorder(Rectangle.NO_BORDER);
        celula2Table3Pg3.setBorder(Rectangle.NO_BORDER);
        table4pg3.addCell(celula1Table3Pg3);
        table4pg3.addCell(celula2Table3Pg3);
        document.add(table4pg3);
        document.add(espaco2Pg2);


        document.newPage();

        // Quarta Página
        marcaDagua.setAbsolutePosition(0, 0);
        document.add(marcaDagua);

        imgCantoInferior.setAbsolutePosition(230, 3);
        document.add(imgCantoInferior);

        Paragraph titulo1pg4 = new Paragraph(new Phrase(30F , "Como Funciona um Sistema Fotovoltaico ON – GRID", tituloBranco3));
        titulo1pg4.setAlignment(Element.ALIGN_CENTER);

        NumberFormat formatoComPontoTetxo = NumberFormat.getNumberInstance();

        Paragraph paragrafo1pg4 = new Paragraph(new Phrase(10F , "Um sistema on-grid funciona em conjunto com a rede da concessionária, a energia gerada pelos painéis fotovoltaicos é convertida em energia CA pelo inversor, parte dessa energia alimenta de maneira instantânea as cargas locais e o excedente volta para a rede da concessionária. Através do medidor bidirecional é registrada essa entrega de "+formatoComPontoTetxo.format(Integer.parseInt(anoFinal))+" kWh sob forma de crédito, que é abatida nos períodos onde não há geração e é necessário buscar a energia da rede. A resolução 482/ 687 da ANEEL regulamenta o sistema de compensação de créditos e viabiliza economicamente a operação.", textoBrancoPg2));
        paragrafo1pg4.setAlignment(Element.ALIGN_JUSTIFIED);
        paragrafo1pg4.setLeading(5);

        document.add(espaco1Pg2);

        // primeira linha
        PdfPTable table1Pg4 = new PdfPTable(2);
        table1Pg4.setTotalWidth(new float[]{ 500, 130 });
        table1Pg4.setLockedWidth(true);
        PdfPCell celula1Tabela1Pagina4 = new PdfPCell(new Phrase(titulo1pg4));
        celula1Tabela1Pagina4.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela1Pagina4.setFixedHeight(25);
        celula1Tabela1Pagina4.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela1Pagina4.setBorder(Rectangle.NO_BORDER);
        celula1Tabela1Pagina4.setColspan(2);
        table1Pg4.addCell(celula1Tabela1Pagina4);

        // segunda linha
        PdfPCell celula2Tabela1Pagina4 = new PdfPCell(new Phrase(paragrafo1pg4));
        celula2Tabela1Pagina4.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        celula2Tabela1Pagina4.setFixedHeight(130);
        celula2Tabela1Pagina4.setPaddingRight(35);
        celula2Tabela1Pagina4.setLeading(0f, 1.2f);
        celula2Tabela1Pagina4.setPaddingLeft(35);
        celula2Tabela1Pagina4.setBackgroundColor(new BaseColor(91,155,213));
        celula2Tabela1Pagina4.setBorder(Rectangle.NO_BORDER);
        celula2Tabela1Pagina4.setColspan(2);
        table1Pg4.addCell(celula2Tabela1Pagina4);
        document.add(table1Pg4);

        //document.add(espaco2Pg2);

        ByteArrayOutputStream streamImagemGrid = new ByteArrayOutputStream();
        Bitmap imagemGrid = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imggrid);
        //Bitmap tamanhoImg7 = Bitmap.createScaledBitmap(imagemGrid, 409, 207, true);
        imagemGrid.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemGrid);
        Image imgGrid = Image.getInstance(streamImagemGrid.toByteArray());
        imgGrid.setAlignment(Element.ALIGN_CENTER);

        imgGrid.scalePercent(12);
        int Width8 = (int) imgGrid.getScaledWidth();
        Log.d("mensagem","Width8: "+ Width8);
/*
        if(Width8 != 420){
            imgGrid.scalePercent(24);
        }
*/
        if(Width8 >= 420){
            imgGrid.scalePercent(12);

        }else if(Width8 < 420 && Width8 >= 337){
            imgGrid.scalePercent(15);

        }else if(Width8 < 337 && Width8 >= 254){
            imgGrid.scalePercent(18);

        }else if(Width8 < 254 && Width8 >= 171){
            imgGrid.scalePercent(21);

        }else if(Width8 < 171){
            imgGrid.scalePercent(24);
        }


        /*
        if (width3 > 2000){
            imgGrid.scalePercent(18);
        }else{
            imgGrid.scalePercent(12);
        }
        */
        document.add(imgGrid);

        //document.add(espaco2Pg2);


        Paragraph titulo2pg4 = new Paragraph(new Phrase(30F , "Vantagens de Gerar sua Própria Energia", tituloBranco3));
        titulo2pg4.setAlignment(Element.ALIGN_CENTER);

        Paragraph paragrafo2pg4 = new Paragraph(new Phrase(10F , " 1. Investir em energia solar é uma atitude sustentável; \n 2. Rapidez na instalação do sistema é um diferencial; \n 3. O investimento é recuperado em poucos anos; \n 4. Economia na conta de luz é imediata e duradoura; \n 5. Gerar energia para outras unidades é simples e viável; \n 6. A valorização imobiliária é comprovada; \n 7. Independência energética, livre de aumentos e impostos;", textoBrancoPg2));
        paragrafo2pg4.setAlignment(Element.ALIGN_JUSTIFIED);
        paragrafo2pg4.setLeading(5);

        //document.add(espaco1Pg2);

        // primeira linha
        PdfPTable table2Pg4 = new PdfPTable(2);
        table2Pg4.setTotalWidth(new float[]{ 500, 130 });
        table2Pg4.setLockedWidth(true);
        PdfPCell celula1Tabela2Pagina4 = new PdfPCell(new Phrase(titulo2pg4));
        celula1Tabela2Pagina4.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela2Pagina4.setFixedHeight(25);
        celula1Tabela2Pagina4.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela2Pagina4.setBorder(Rectangle.NO_BORDER);
        celula1Tabela2Pagina4.setColspan(2);
        table2Pg4.addCell(celula1Tabela2Pagina4);

        // segunda linha
        PdfPCell celula2Tabela2Pagina4 = new PdfPCell(new Phrase(paragrafo2pg4));
        celula2Tabela2Pagina4.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        celula2Tabela2Pagina4.setFixedHeight(140);
        celula2Tabela2Pagina4.setPaddingRight(35);
        celula2Tabela2Pagina4.setPaddingLeft(50);
        celula2Tabela2Pagina4.setLeading(0f, 1.4f);
        celula2Tabela2Pagina4.setBackgroundColor(new BaseColor(91,155,213));
        celula2Tabela2Pagina4.setBorder(Rectangle.NO_BORDER);
        celula2Tabela2Pagina4.setColspan(2);
        table2Pg4.addCell(celula2Tabela2Pagina4);
        document.add(table2Pg4);


        Paragraph titulo1pg5 = new Paragraph(new Phrase(30F , "Análise de Consumo Anual", tituloBranco3));
        titulo1pg5.setAlignment(Element.ALIGN_CENTER);

        document.add(espaco2Pg2);

        // primeira linha
        PdfPTable table1Pg5 = new PdfPTable(2);
        table1Pg5.setTotalWidth(new float[]{ 500, 130 });
        table1Pg5.setLockedWidth(true);
        PdfPCell celula1Tabela1Pagina5 = new PdfPCell(new Phrase(titulo1pg5));
        celula1Tabela1Pagina5.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela1Pagina5.setFixedHeight(25);
        celula1Tabela1Pagina5.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela1Pagina5.setBorder(Rectangle.NO_BORDER);
        celula1Tabela1Pagina5.setColspan(2);
        table1Pg5.addCell(celula1Tabela1Pagina5);
        document.add(table1Pg5);

        document.add(espaco2Pg2);


        Font textoBrancoMeses = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.WHITE);
        textoBrancoMeses.setSize(16);
        Font textoAzulMeses = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.BLUE);
        textoAzulMeses.setSize(14);
        txtAzul.setAlignment(Element.ALIGN_CENTER);
        textoAzulMeses.setColor(70,130,180);

        Font textoAzulMeses2 = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.BLUE);
        textoAzulMeses2.setSize(12);
        textoAzulMeses2.setColor(70,130,180);

        PdfPTable tabelaMes = new PdfPTable(12);
        tabelaMes.setTotalWidth(new float[]{ 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40 });
        tabelaMes.setLockedWidth(true);
        PdfPCell cellMeses;
        cellMeses = new PdfPCell(new Phrase(30F , "Consumo da unidade consumidora", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellMeses.setColspan(12);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Quantidade de unidades: "+textoUnidadeConsumidora, textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellMeses.setColspan(12);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Jan", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Fev", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Mar", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Abr", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Mai", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Jun", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Jul", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Ago", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Set", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Out", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Nov", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , "Dez", textoBrancoMeses));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setBackgroundColor(new BaseColor(91,155,213));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);

        NumberFormat formatoComPonto = NumberFormat.getNumberInstance();
        //System.out.println(f.format(123456789));
        //Log.d("Planeta","eitaaaa"+f.format(mediaMes1));

        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes1), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes2), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes3), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes4), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes5), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes6), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes7), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes8), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes9), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes10), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes11), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);
        cellMeses = new PdfPCell(new Phrase(30F , ""+formatoComPonto.format(mediaMes12), textoAzulMeses2));
        cellMeses.setBorderColor(new BaseColor(70,130,180));
        cellMeses.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabelaMes.addCell(cellMeses);

        BigDecimal valor3 = new BigDecimal(Double.parseDouble(KWinstaladoFinal)).setScale(2, RoundingMode.HALF_EVEN);
        Double consumoMenosGeracao = consumoXgeracao - mediaMedicaoAno;
        Locale ptBr = new Locale("pt", "BR");

        document.add(tabelaMes);
        Font textoBrancoMeses2 = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.WHITE);
        textoBrancoMeses2.setSize(13);
        Paragraph titulo2pg5 = new Paragraph(new Phrase(30F , "Valor kWh: kWh + PIS + COFINS + ICMS + BANDEIRA = R$ "+NumberFormat.getNumberInstance().format(valor3.doubleValue()), textoBrancoMeses2));
        titulo2pg5.setAlignment(Element.ALIGN_CENTER);

        document.add(espaco2Pg2);

        // primeira linha
        PdfPTable table2Pg5 = new PdfPTable(2);
        table2Pg5.setTotalWidth(new float[]{ 500, 130 });
        table2Pg5.setLockedWidth(true);
        PdfPCell celula1Tabela2Pagina5 = new PdfPCell(new Phrase(titulo2pg5));
        celula1Tabela2Pagina5.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela2Pagina5.setFixedHeight(25);
        celula1Tabela2Pagina5.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela2Pagina5.setBorder(Rectangle.NO_BORDER);
        celula1Tabela2Pagina5.setColspan(2);
        table2Pg5.addCell(celula1Tabela2Pagina5);
        document.add(table2Pg5);


        document.newPage();

        // Quinta Página
        marcaDagua.setAbsolutePosition(0, 0);
        document.add(marcaDagua);

        imgCantoInferior.setAbsolutePosition(230, 3);
        document.add(imgCantoInferior);

        document.add(espaco1Pg2);

        Paragraph titulo20pg4 = new Paragraph(new Phrase(30F , "Pré-Projeto", tituloBranco3));
        titulo20pg4.setAlignment(Element.ALIGN_CENTER);

        Paragraph paragrafo20pg4 = new Paragraph(new Phrase(10F , "Local: "+endereco+"\n" +
                //"Inclinação: XXX\n" +
                //"Orientação: XXX\n" +
                "Tipo de Telhado: "+tipoTelhado, textoBrancoPg2));
        paragrafo20pg4.setAlignment(Element.ALIGN_JUSTIFIED);
        paragrafo20pg4.setLeading(5);

        //document.add(espaco1Pg2);

        // primeira linha
        PdfPTable table20Pg4 = new PdfPTable(2);
        table20Pg4.setTotalWidth(new float[]{ 500, 130 });
        table20Pg4.setLockedWidth(true);
        PdfPCell celula1Tabela20Pagina4 = new PdfPCell(new Phrase(titulo20pg4));
        celula1Tabela20Pagina4.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela20Pagina4.setFixedHeight(25);
        celula1Tabela20Pagina4.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela20Pagina4.setBorder(Rectangle.NO_BORDER);
        celula1Tabela20Pagina4.setColspan(2);
        table20Pg4.addCell(celula1Tabela20Pagina4);

        // segunda linha
        PdfPCell celula20Tabela2Pagina4 = new PdfPCell(new Phrase(paragrafo20pg4));
        celula20Tabela2Pagina4.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        celula20Tabela2Pagina4.setFixedHeight(60);
        celula20Tabela2Pagina4.setPaddingRight(35);
        celula20Tabela2Pagina4.setPaddingLeft(50);
        celula20Tabela2Pagina4.setLeading(0f, 1.4f);
        celula20Tabela2Pagina4.setBackgroundColor(new BaseColor(91,155,213));
        celula20Tabela2Pagina4.setBorder(Rectangle.NO_BORDER);
        celula20Tabela2Pagina4.setColspan(2);
        table20Pg4.addCell(celula20Tabela2Pagina4);
        document.add(table20Pg4);

        Paragraph titulo20pg5 = new Paragraph(new Phrase(30F , "Fator de Performance do Sistema:", tituloBranco3));
        titulo20pg5.setAlignment(Element.ALIGN_CENTER);

        document.add(espaco2Pg2);

        // primeira linha
        PdfPTable table20Pg5 = new PdfPTable(2);
        table20Pg5.setTotalWidth(new float[]{ 500, 130 });
        table20Pg5.setLockedWidth(true);
        PdfPCell celula1Tabela20Pagina5 = new PdfPCell(new Phrase(titulo20pg5));
        celula1Tabela20Pagina5.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela20Pagina5.setFixedHeight(25);
        celula1Tabela20Pagina5.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela20Pagina5.setBorder(Rectangle.NO_BORDER);
        celula1Tabela20Pagina5.setColspan(2);
        table20Pg5.addCell(celula1Tabela20Pagina5);
        document.add(table20Pg5);

        Paragraph tituloRelacaoFoto = new Paragraph(new Phrase(30F , "Relação de desempenho fotovoltaica", textoAzulTitulos));
        tituloRelacaoFoto.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloRelacaoFoto);

        document.add(espaco2Pg2);

        ByteArrayOutputStream streamImagemDesempenhoFoto = new ByteArrayOutputStream();
        Bitmap imagemDesempenho = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.desempenhofoto);
        //Bitmap tamanhoImg8 = Bitmap.createScaledBitmap(imagemDesempenho, 292, 140, true);
        imagemDesempenho.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemDesempenhoFoto);
        Image imgDesempenho = Image.getInstance(streamImagemDesempenhoFoto.toByteArray());
        imgDesempenho.setAlignment(Element.ALIGN_CENTER);


        imgDesempenho.scalePercent(14);
        int Width9 = (int) imgDesempenho.getScaledWidth();
        Log.d("mensagem","Width9: "+ Width9);
/*
        if(Width9 != 349){
            imgDesempenho.scalePercent(28);
        }
*/
        if(Width9 >= 349){
            imgDesempenho.scalePercent(14);

        }else if(Width9 < 349 && Width9 >= 279){
            imgDesempenho.scalePercent(17);

        }else if(Width9 < 279 && Width9 >= 209){
            imgDesempenho.scalePercent(22);

        }else if(Width9 < 209 && Width9 >= 140){
            imgDesempenho.scalePercent(25);

        }else if(Width9 < 140){
            imgDesempenho.scalePercent(28);
        }

        /*
        if (width3 > 2000){
            imgDesempenho.scalePercent(26);
        }else{
            imgDesempenho.scalePercent(14);
        }
        */
        document.add(imgDesempenho);

        document.add(espaco2Pg2);

        Font textoAzul1 = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.WHITE);
        textoAzul1.setSize(15);
        Font textoAzul2 = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.BLUE);
        textoAzul2.setSize(17);
        txtAzul.setAlignment(Element.ALIGN_CENTER);
        textoAzul2.setColor(70,130,180);

        PdfPTable table3pg5 = new PdfPTable(2);
        table3pg5.setTotalWidth(new float[]{ 220, 220 });
        table3pg5.setLockedWidth(true);
        PdfPCell cellTable5;
        cellTable5 = new PdfPCell(new Phrase(30F , "Sistema Ideal (kWp)", textoAzul1));
        cellTable5.setBorderColor(new BaseColor(70,130,180));
        cellTable5.setBackgroundColor(new BaseColor(91,155,213));
        cellTable5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3pg5.addCell(cellTable5);
        cellTable5 = new PdfPCell(new Phrase(30F , "Geração estimada \n Ano (kWh)", textoAzul1));
        cellTable5.setBorderColor(new BaseColor(70,130,180));
        cellTable5.setBackgroundColor(new BaseColor(91,155,213));
        cellTable5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3pg5.addCell(cellTable5);
        //cellTable5 = new PdfPCell(new Phrase(30F , "% de geração em \n Relação ao consumo", textoAzul1));
        //cellTable5.setBackgroundColor(new BaseColor(91,155,213));
        //cellTable5.setHorizontalAlignment(Element.ALIGN_CENTER);
        //table3pg5.addCell(cellTable5);
        cellTable5 = new PdfPCell(new Phrase(30F , sistemaFinal, textoAzul2));
        cellTable5.setBorderColor(new BaseColor(70,130,180));
        cellTable5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3pg5.addCell(cellTable5);
        cellTable5 = new PdfPCell(new Phrase(30F , formatoComPonto.format(Integer.parseInt(anoFinal)), textoAzul2));
        cellTable5.setBorderColor(new BaseColor(70,130,180));
        cellTable5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3pg5.addCell(cellTable5);
        cellTable5 = new PdfPCell(new Phrase(30F , "100%", textoAzul2));
        cellTable5.setBorderColor(new BaseColor(70,130,180));
        cellTable5.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3pg5.addCell(cellTable5);

        document.add(table3pg5);

        document.add(espaco2Pg2);

        Paragraph tituloEnergiaGerada = new Paragraph(new Phrase(30F , "Energia Gerada", textoAzulTitulos));
        tituloEnergiaGerada.setAlignment(Element.ALIGN_CENTER);
        tituloEnergiaGerada.setSpacingAfter(-15);
        document.add(tituloEnergiaGerada);
/*
        ByteArrayOutputStream streamImagemEnergiaGerada= new ByteArrayOutputStream();
        Bitmap imagemEnergia = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imgeenergia);
        imagemEnergia.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemEnergiaGerada);
        Image imgEnergia = Image.getInstance(streamImagemEnergiaGerada.toByteArray());
        imgEnergia.setAlignment(Element.ALIGN_CENTER);
        imgEnergia.scalePercent(15);
        document.add(imgEnergia);

*/

        Font textoPreto = new Font(Font.FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.BLACK);
        textoPreto.setSize(12);

        ByteArrayOutputStream streamGrafico = new ByteArrayOutputStream();
        Bitmap bitmapGrafico = Bitmap.createBitmap(wvGrafico.getWidth(), wvGrafico.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapGrafico);
        wvGrafico.draw(canvas);

        //Bitmap tamanhoImg9 = Bitmap.createScaledBitmap(bitmapGrafico, 530, 220, true);
        bitmapGrafico.compress(Bitmap.CompressFormat.JPEG, 100, streamGrafico);
        Image myImgGrafico = Image.getInstance(streamGrafico.toByteArray());
        myImgGrafico.setAlignment(Image.MIDDLE);

        myImgGrafico.scalePercent(50);
        int Width10 = (int) myImgGrafico.getScaledWidth();
        Log.d("mensagem","Width10: "+ Width10);
/*
        if(Width10 != 495){
            myImgGrafico.scalePercent(100);
        }
*/
        if(Width10 >= 495){
            myImgGrafico.scalePercent(50);

        }else if(Width10 < 495 && Width10 >= 397){
            myImgGrafico.scalePercent(64);

        }else if(Width10 < 397 && Width10 >= 299){
            myImgGrafico.scalePercent(77);

        }else if(Width10 < 299 && Width10 >= 201){
            myImgGrafico.scalePercent(89);

        }else if(Width10 < 201){
            myImgGrafico.scalePercent(100);
        }

        //myImgGrafico.scalePercent(50);
        /*
        if (width3 > 2000){
            myImgGrafico.scalePercent(90);
        }else{
            myImgGrafico.scalePercent(50);
        }
        */
        myImgGrafico.setSpacingBefore(-15);
        document.add(myImgGrafico);
/*
        Paragraph tituloGrafico1= new Paragraph(new Phrase(30F , "Análise Comparativa consumo (kWh) e energia gerada (kWh)", textoPreto));
        tituloGrafico1.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloGrafico1);
*/
        document.newPage();

        marcaDagua.setAbsolutePosition(0, 0);
        document.add(marcaDagua);

        imgCantoInferior.setAbsolutePosition(230, 3);
        document.add(imgCantoInferior);

        document.add(espaco1Pg2);

        Paragraph tituloContaEnergia = new Paragraph(new Phrase(30F , "Redução da conta de energia", textoAzulTitulos));
        tituloContaEnergia.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloContaEnergia);

        document.add(espaco2Pg2);
        document.add(espaco2Pg2);

        ByteArrayOutputStream streamImagemEnergiaGerada= new ByteArrayOutputStream();
        Bitmap imagemEnergia = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.legenda4);
        imagemEnergia.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemEnergiaGerada);
        Image imgEnergia = Image.getInstance(streamImagemEnergiaGerada.toByteArray());
        imgEnergia.setAlignment(Element.ALIGN_CENTER);

        //imgEnergia.scalePercent(25);



        //imgEnergia.scalePercent(25);
        /*
        if (width3 > 2000){
            imgEnergia.scalePercent(45);
        }else{
            imgEnergia.scalePercent(25);
        }
        */
        imgEnergia.scalePercent(25);
        int Width11 = (int) imgEnergia.getScaledWidth();
        Log.d("mensagem","Width11: "+ Width11);
 /*
        if(Width11 != 320){
            imgEnergia.scalePercent(50);
        }
*/
        if(Width11 >= 320){
            imgEnergia.scalePercent(25);

        }else if(Width11 < 320 && Width11 >= 257){
            imgEnergia.scalePercent(31);

        }else if(Width11 < 257 && Width11 >= 194){
            imgEnergia.scalePercent(38);

        }else if(Width11 < 194 && Width11 >= 131){
            imgEnergia.scalePercent(44);

        }else if(Width11 < 131){
            imgEnergia.scalePercent(50);
        }


        imgEnergia.setAbsolutePosition(170, 735);
        document.add(imgEnergia);

        ByteArrayOutputStream streamGrafico2 = new ByteArrayOutputStream();
        Bitmap bitmapGrafico2 = Bitmap.createBitmap(wvGrafico2.getWidth(), wvGrafico2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmapGrafico2);
        wvGrafico2.draw(canvas2);

        //Bitmap tamanhoImg10 = Bitmap.createScaledBitmap(bitmapGrafico2, 430, 130, true);
        bitmapGrafico2.compress(Bitmap.CompressFormat.JPEG, 100, streamGrafico2);
        Image myImgGrafico2 = Image.getInstance(streamGrafico2.toByteArray());
        myImgGrafico2.setAlignment(Image.MIDDLE);
        myImgGrafico2.setSpacingAfter(-15);

        myImgGrafico2.scalePercent(50);
        int Width12 = (int) myImgGrafico2.getScaledWidth();
        Log.d("mensagem","Width12: "+ Width12);
/*
        if(Width12 != 495){
            myImgGrafico2.scalePercent(100);
        }
*/
        if(Width12 >= 495){
            myImgGrafico2.scalePercent(50);

        }else if(Width12 < 495 && Width12 >= 397){
            myImgGrafico2.scalePercent(64);

        }else if(Width12 < 397 && Width12 >= 299){
            myImgGrafico2.scalePercent(77);

        }else if(Width12 < 299 && Width12 >= 201){
            myImgGrafico2.scalePercent(89);

        }else if(Width12 < 201){
            myImgGrafico2.scalePercent(100);
        }

        /*
        if (width3 > 2000){
            myImgGrafico2.scalePercent(90);
        }else{
            myImgGrafico2.scalePercent(50);
        }
        */
        document.add(myImgGrafico2);
/*
        Paragraph tituloGrafico2= new Paragraph(new Phrase(30F , "Estimativa de Econômia", textoPreto));
        tituloGrafico2.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloGrafico2);


        ByteArrayOutputStream streamImagemLegenda= new ByteArrayOutputStream();
        Bitmap imagemLegenda = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imglegenda);
        imagemLegenda.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemLegenda);
        Image imgReducaoLegenda = Image.getInstance(streamImagemLegenda.toByteArray());
        imgReducaoLegenda.setAlignment(Element.ALIGN_CENTER);
        imgReducaoLegenda.scalePercent(30);
        document.add(imgReducaoLegenda);

        ByteArrayOutputStream streamImagemReducaoEnergia = new ByteArrayOutputStream();
        Bitmap imagemReducaoEnergia = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imgrereducao);
        imagemReducaoEnergia.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemReducaoEnergia);
        Image imgReducaoEnergia = Image.getInstance(streamImagemReducaoEnergia.toByteArray());
        imgReducaoEnergia.setAlignment(Element.ALIGN_CENTER);
        imgReducaoEnergia.scalePercent(16);
        document.add(imgReducaoEnergia);
        */

        Font textoObsAzul = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLDITALIC);
        textoObsAzul.setSize(12);
        textoObsAzul.setColor(70,130,180);

        Paragraph textoObs = new Paragraph(new Phrase(30F , "*Os valores apresentados estão sujeitos a alterações devido às questões técnicas e comerciais.", textoObsAzul));
        textoObs.setAlignment(Element.ALIGN_CENTER);
        textoObs.setSpacingBefore(-15);
        document.add(textoObs);

        ByteArrayOutputStream streamImagemEnergiaGerada1= new ByteArrayOutputStream();
        Bitmap imagemEnergia1 = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imgcasinha);
        //Bitmap tamanhoImg15 = Bitmap.createScaledBitmap(imagemEnergia1, 160, 139, true);
        imagemEnergia1.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemEnergiaGerada1);
        Image imgEnergia1 = Image.getInstance(streamImagemEnergiaGerada1.toByteArray());
        imgEnergia1.setAlignment(Element.ALIGN_CENTER);

        imgEnergia1.scalePercent(25);
        int Width13 = (int) imgEnergia1.getScaledWidth();
        Log.d("mensagem","Width13: "+ Width13);
/*
        if(Width13 != 164){
            imgEnergia1.scalePercent(50);
        }
*/
        if(Width13 >= 164){
            imgEnergia1.scalePercent(25);

        }else if(Width13 < 164 && Width13 >= 131){
            imgEnergia1.scalePercent(31);

        }else if(Width13 < 131 && Width13 >= 98){
            imgEnergia1.scalePercent(38);

        }else if(Width13 < 98 && Width13 >= 66){
            imgEnergia1.scalePercent(44);

        }else if(Width13 < 66){
            imgEnergia1.scalePercent(50);
        }

        //imgEnergia1.scalePercent(25);
        /*
        if (width3 > 2000){
            imgEnergia1.scalePercent(45);
        }else{
            imgEnergia1.scalePercent(25);
        }
        */

        document.add(espaco2Pg2);

        Font textoAzulCustos = new Font(Font.FontFamily.HELVETICA, 25, Font.NORMAL);
        textoAzulCustos.setSize(13);
        textoAzulCustos.setColor(70,130,180);

        PdfPTable table1pg6 = new PdfPTable(3);
        table1pg6.setTotalWidth(new float[]{ 150, 170, 200});
        table1pg6.setLockedWidth(true);
        PdfPCell cellTable1;
        cellTable1 = new PdfPCell(new Phrase(30F , "CUSTO \n ESPECÍFICO", textoAzulCustos));
        cellTable1.setBorderColor(new BaseColor(70,130,180));
        cellTable1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1.setPadding(10);
        table1pg6.addCell(cellTable1);


        BigDecimal valorDoGrafico = new BigDecimal(propostaMaisComissao/sistemaDouble).setScale(2, RoundingMode.HALF_EVEN);


        //NumberFormat nf = NumberFormat.getCurrencyInstance();
        //String formatado = nf.format (valor);
        //NumberFormat.getCurrencyInstance(ptBr).format( )

        cellTable1 = new PdfPCell(new Phrase(30F , "R$ "+NumberFormat.getNumberInstance().format(valorDoGrafico.doubleValue()), textoAzulCustos));
        cellTable1.setBorderColor(new BaseColor(70,130,180));
        cellTable1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1.setBorderWidthRight(2.0f);
        cellTable1.setPadding(10);
        table1pg6.addCell(cellTable1);

        cellTable1 = new PdfPCell(imgEnergia1);
        cellTable1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1.setRowspan(3);
        cellTable1.setPaddingLeft(40);
        cellTable1.setPaddingTop(10);
        cellTable1.setBorder(Rectangle.NO_BORDER);
        table1pg6.addCell(cellTable1);


        cellTable1 = new PdfPCell(new Phrase(30F , "POTÊNCIA \n NOMINAL", textoAzulCustos));
        cellTable1.setBorderColor(new BaseColor(70,130,180));
        cellTable1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1.setPadding(10);
        table1pg6.addCell(cellTable1);
        cellTable1 = new PdfPCell(new Phrase(30F , sistemaFinal+" kWp", textoAzulCustos));
        cellTable1.setBorderColor(new BaseColor(70,130,180));
        cellTable1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1.setBorderWidthRight(2.0f);
        cellTable1.setPadding(10);
        table1pg6.addCell(cellTable1);
        cellTable1 = new PdfPCell(new Phrase(30F , "CUSTO TOTAL \n DO SISTEMA", textoAzulCustos));
        cellTable1.setBorderColor(new BaseColor(70,130,180));
        cellTable1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1.setPadding(10);
        table1pg6.addCell(cellTable1);

        BigDecimal valor1 = new BigDecimal(propostaMaisComissao).setScale(2, RoundingMode.HALF_EVEN);

        cellTable1 = new PdfPCell(new Phrase(30F , "R$ "+NumberFormat.getNumberInstance().format(valor1.doubleValue()), textoAzulCustos));
        cellTable1.setBorderColor(new BaseColor(70,130,180));
        cellTable1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1.setBorderWidthRight(2.0f);
        cellTable1.setPadding(10);
        table1pg6.addCell(cellTable1);
        document.add(table1pg6);

        Font textoAzulMenor = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
        textoAzulMenor.setSize(15);
        textoAzulMenor.setColor(70,130,180);

        Paragraph titulo1pg7 = new Paragraph(new Phrase(30F , "Descrição e Valores", tituloBranco3));
        titulo1pg7.setAlignment(Element.ALIGN_CENTER);

        document.add(espaco2Pg2);

        // primeira linha
        PdfPTable table1Pg7 = new PdfPTable(2);
        table1Pg7.setTotalWidth(new float[]{ 500, 130 });
        table1Pg7.setLockedWidth(true);
        PdfPCell celula1Tabela1Pagina7 = new PdfPCell(new Phrase(titulo1pg7));
        celula1Tabela1Pagina7.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela1Pagina7.setFixedHeight(25);
        celula1Tabela1Pagina7.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela1Pagina7.setBorder(Rectangle.NO_BORDER);
        celula1Tabela1Pagina7.setColspan(2);
        table1Pg7.addCell(celula1Tabela1Pagina7);
        document.add(table1Pg7);

        document.add(espaco2Pg2);

        Font textoAzuldescricao = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
        textoAzuldescricao.setSize(15);
        textoAzuldescricao.setColor(70,130,180);

        PdfPTable table1pg7 = new PdfPTable(2);
        table1pg7.setTotalWidth(new float[]{ 360, 130});
        table1pg7.setLockedWidth(true);
        PdfPCell cellTable1pg7;
        cellTable1pg7 = new PdfPCell(new Phrase(30F , "Descrição", textoAzuldescricao));
        cellTable1pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable1pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1pg7.setPadding(10);
        table1pg7.addCell(cellTable1pg7);
        cellTable1pg7 = new PdfPCell(new Phrase(30F , "Valor", textoAzuldescricao));
        cellTable1pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable1pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1pg7.setPadding(10);
        table1pg7.addCell(cellTable1pg7);
        cellTable1pg7 = new PdfPCell(new Phrase(30F , "01 – Instalação de Microgeração Distribuída Fotovoltaica de "+sistemaFinal+" kWp, com fornecimento de todo material, incluindo Módulos Fotovoltaicos, Inversor, Estruturas, Fiação, Mão de obra especializada e Projeto aprovado na Concessionária.", textoAzulCustos));
        cellTable1pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable1pg7.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cellTable1pg7.setLeading(0f, 1.2f);
        cellTable1pg7.setPadding(10);
        cellTable1pg7.setLeading(0f, 1.2f);
        table1pg7.addCell(cellTable1pg7);
        cellTable1pg7 = new PdfPCell(new Phrase(30F , "\n\n R$ "+NumberFormat.getNumberInstance().format(valor1.doubleValue()), textoAzulMenor));
        cellTable1pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable1pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1pg7.setPadding(10);
        table1pg7.addCell(cellTable1pg7);
        cellTable1pg7 = new PdfPCell(new Phrase(30F , "Total Geral", textoAzulCustos));
        cellTable1pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable1pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1pg7.setPadding(10);
        table1pg7.addCell(cellTable1pg7);

        cellTable1pg7 = new PdfPCell(new Phrase(30F ,  "R$ "+NumberFormat.getNumberInstance().format(valor1.doubleValue()), textoAzulMenor));
        cellTable1pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable1pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable1pg7.setPadding(10);
        table1pg7.addCell(cellTable1pg7);
        document.add(table1pg7);

        document.newPage();

        // Setima Página
        marcaDagua.setAbsolutePosition(0, 0);
        document.add(marcaDagua);

        imgCantoInferior.setAbsolutePosition(230, 3);
        document.add(imgCantoInferior);

        document.add(espaco1Pg2);


        Paragraph titulo2pg7 = new Paragraph(new Phrase(30F , "Condições de Pagamento", tituloBranco3));
        titulo2pg7.setAlignment(Element.ALIGN_CENTER);

        // primeira linha
        PdfPTable table2Pg7 = new PdfPTable(2);
        table2Pg7.setTotalWidth(new float[]{ 500, 130 });
        table2Pg7.setLockedWidth(true);
        PdfPCell celula1Tabela2Pagina7 = new PdfPCell(new Phrase(titulo2pg7));
        celula1Tabela2Pagina7.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela2Pagina7.setFixedHeight(25);
        celula1Tabela2Pagina7.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela2Pagina7.setBorder(Rectangle.NO_BORDER);
        celula1Tabela2Pagina7.setColspan(2);
        table2Pg7.addCell(celula1Tabela2Pagina7);
        document.add(table2Pg7);
        /*
        Paragraph paragrafo1pg7 = new Paragraph(new Phrase(10F , "\n 01 – Os serviços prestados serão pagos pelo CONTRATANTE diretamente a empresa CONTRATADA conforme acordado, o pagamento será efetuado da seguinte forma: \n 1ª Parcela de mobilização (40%): R$ 95.600,75 – boleto com vencimento para 10 dias da assinatura do contrato; \n 2ª Parcela (30%): R$ 71.700,56 – boleto com vencimento para 30 dias após a assinatura do contrato; \n 3ª Parcela (30%): R$ 71.700,56 – boleto com vencimento para 60 dias após a assinatura do contrato.\n", textoAzulCustos));
        paragrafo1pg7.setAlignment(Element.ALIGN_JUSTIFIED);
        paragrafo1pg7.setLeading(5);
        document.add(paragrafo1pg7);

        document.add(espaco1Pg2);
*/

        Paragraph paragrafo1pg7 = new Paragraph(new Phrase(30F , "01 – Os serviços prestados serão pagos pelo CONTRATANTE diretamente a empresa CONTRATADA conforme acordado, o pagamento será efetuado da seguinte forma:", textoAzulCustos));
        paragrafo1pg7.setAlignment(Element.ALIGN_JUSTIFIED);
        paragrafo1pg7.setLeading(20);
        //document.add(paragrafo1pg7);

        PdfPTable table3Pg7 = new PdfPTable(2);
        table3Pg7.setTotalWidth(new float[]{ 260, 260 });
        table3Pg7.setLockedWidth(true);
        PdfPCell celula1Tabela3Pagina7 = new PdfPCell(new Phrase(paragrafo1pg7));
        celula1Tabela3Pagina7.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        celula1Tabela3Pagina7.setBorder(Rectangle.NO_BORDER);
        celula1Tabela3Pagina7.setColspan(2);
        celula1Tabela3Pagina7.setPaddingTop(10);
        table3Pg7.addCell(celula1Tabela3Pagina7);
        document.add(table3Pg7);

        BigDecimal valor5 = new BigDecimal(pagamentoHCC40).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal valor6 = new BigDecimal(pagamentoHCC30).setScale(2, RoundingMode.HALF_EVEN);

        Paragraph paragrafo2pg7 = new Paragraph(new Phrase(30F , "\n1ª Parcela de mobilização (40%): R$ "+NumberFormat.getNumberInstance().format(valor5.doubleValue())+" – boleto com vencimento para 10 dias da assinatura do contrato;\n" +
                "\n2ª Parcela (30%): R$ "+NumberFormat.getNumberInstance().format(valor6.doubleValue())+" – boleto com vencimento para 30 dias após a assinatura do contrato;\n" +
                "\n3ª Parcela (30%): R$ "+NumberFormat.getNumberInstance().format(valor6.doubleValue())+" – boleto com vencimento para 60 dias após a assinatura do contrato.\n", textoAzulCustos));
        paragrafo2pg7.setAlignment(Element.ALIGN_JUSTIFIED);
        PdfPTable table4Pg7 = new PdfPTable(2);
        table4Pg7.setTotalWidth(new float[]{ 260, 260 });
        table4Pg7.setLockedWidth(true);
        PdfPCell celula2Tabela3Pagina7 = new PdfPCell(new Phrase(paragrafo2pg7));
        celula2Tabela3Pagina7.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        celula2Tabela3Pagina7.setBorder(Rectangle.NO_BORDER);
        celula2Tabela3Pagina7.setPaddingLeft(30);
        celula2Tabela3Pagina7.setColspan(2);
        table4Pg7.addCell(celula2Tabela3Pagina7);
        document.add(table4Pg7);

        document.add(espaco2Pg2);

        Paragraph titulo3pg7 = new Paragraph(new Phrase(30F , "Análise Financeira do Projeto", tituloBranco3));
        titulo3pg7.setAlignment(Element.ALIGN_CENTER);

        // primeira linha
        PdfPTable table5Pg7 = new PdfPTable(2);
        table5Pg7.setTotalWidth(new float[]{ 500, 130 });
        table5Pg7.setLockedWidth(true);
        PdfPCell celula1Tabela5Pagina7 = new PdfPCell(new Phrase(titulo3pg7));
        celula1Tabela5Pagina7.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela5Pagina7.setFixedHeight(25);
        celula1Tabela5Pagina7.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela5Pagina7.setBorder(Rectangle.NO_BORDER);
        celula1Tabela5Pagina7.setColspan(2);
        table5Pg7.addCell(celula1Tabela5Pagina7);
        document.add(table5Pg7);

        document.add(espaco2Pg2);

        Font textoAzulTabela = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
        textoAzulTabela.setSize(13);
        textoAzulTabela.setColor(70,130,180);

        PdfPTable table6pg7 = new PdfPTable(2);
        table6pg7.setTotalWidth(new float[]{ 360, 130});
        table6pg7.setLockedWidth(true);
        PdfPCell cellTable6pg7;
        cellTable6pg7 = new PdfPCell(new Phrase(30F , "Pagamento HCC", tituloBranco3));
        cellTable6pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setBackgroundColor(new BaseColor(91,155,213));
        cellTable6pg7.setColspan(2);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        cellTable6pg7 = new PdfPCell(new Phrase(30F , "Energia Produzida em 25 anos (kWh)", textoAzulTabela));
        cellTable6pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        //dsfsdfsdd
        table6pg7.addCell(cellTable6pg7);
        cellTable6pg7 = new PdfPCell(new Phrase(30F , ""+NumberFormat.getNumberInstance().format(inteiroEnergiaProduzida25Anos), textoAzulCustos));
        cellTable6pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        cellTable6pg7 = new PdfPCell(new Phrase(30F , "Economia de energia em 25 anos (R$)", textoAzulTabela));
        cellTable6pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);

        DecimalFormat duasCasasdecimais = new DecimalFormat("#######.##");
        duasCasasdecimais.setRoundingMode(RoundingMode.UP);
        table6pg7.addCell(cellTable6pg7);

        //NumberFormat.getNumberInstance().format(valor1.doubleValue()
        BigDecimal valorProximo = new BigDecimal(EconomiaDeEnergiaProduzida25Anos).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal valorProximo2 = new BigDecimal(saldoAcumuladoAno25).setScale(2, RoundingMode.HALF_EVEN);

        DecimalFormat formatacao = new DecimalFormat("###,###,###,###.00");

        cellTable6pg7 = new PdfPCell(new Phrase(30F , ""+formatacao.format(EconomiaDeEnergiaProduzida25Anos), textoAzulCustos));
        cellTable6pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        cellTable6pg7 = new PdfPCell(new Phrase(30F , "Fluxo de Caixa Acumulado (R$)", textoAzulTabela));
        cellTable6pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        cellTable6pg7 = new PdfPCell(new Phrase(30F , ""+formatacao.format(saldoAcumuladoAno25), textoAzulCustos));
        cellTable6pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        /*
        cellTable6pg7 = new PdfPCell(new Phrase(30F , "VPL [R$]", textoAzulTabela));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        cellTable6pg7 = new PdfPCell(new Phrase(30F , "R$ 3.903.146,09", textoAzulCustos));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        cellTable6pg7 = new PdfPCell(new Phrase(30F , "TIR [%]", textoAzulTabela));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        cellTable6pg7 = new PdfPCell(new Phrase(30F , "55%", textoAzulCustos));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        */
        cellTable6pg7 = new PdfPCell(new Phrase(30F , "PAYBACK (anos)", textoAzulTabela));
        cellTable6pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        cellTable6pg7 = new PdfPCell(new Phrase(30F , ""+contadorPayback, textoAzulCustos));
        cellTable6pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable6pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable6pg7.setPadding(4);
        table6pg7.addCell(cellTable6pg7);
        document.add(table6pg7);

        document.add(espaco2Pg2);
/*
        ByteArrayOutputStream streamImagemFluxoCaixa= new ByteArrayOutputStream();
        Bitmap imagemFluxoCaixa = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imgfluxocaixa);
        imagemFluxoCaixa.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemFluxoCaixa);
        Image imgFluxoCaixa = Image.getInstance(streamImagemFluxoCaixa.toByteArray());
        imgFluxoCaixa.setAlignment(Element.ALIGN_CENTER);
        imgFluxoCaixa.scalePercent(17);
        document.add(imgFluxoCaixa);
*/
        ByteArrayOutputStream streamGrafico4 = new ByteArrayOutputStream();
        Bitmap bitmapGrafico4 = Bitmap.createBitmap(wvGrafico4.getWidth(), wvGrafico4.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas4 = new Canvas(bitmapGrafico4);
        wvGrafico4.draw(canvas4);

        //Bitmap tamanhoImg12 = Bitmap.createScaledBitmap(bitmapGrafico4, 530, 200, true);
        bitmapGrafico4.compress(Bitmap.CompressFormat.JPEG, 100, streamGrafico4);
        Image myImgGrafico4 = Image.getInstance(streamGrafico4.toByteArray());
        myImgGrafico4.setAlignment(Image.MIDDLE);

        myImgGrafico4.scalePercent(50);
        int Width14 = (int) myImgGrafico4.getScaledWidth();
        Log.d("mensagem","Width14: "+ Width14);
/*
        if(Width14 != 495){
            myImgGrafico4.scalePercent(100);
        }
*/
        if(Width14 >= 495){
            myImgGrafico4.scalePercent(50);

        }else if(Width14 < 495 && Width14 >= 397){
            myImgGrafico4.scalePercent(64);

        }else if(Width14 < 397 && Width14 >= 299){
            myImgGrafico4.scalePercent(77);

        }else if(Width14 < 299 && Width14 >= 201){
            myImgGrafico4.scalePercent(89);

        }else if(Width14 < 201){
            myImgGrafico4.scalePercent(100);
        }



        /*
        if (width3 > 2000){
            myImgGrafico4.scalePercent(90);
        }else{
            myImgGrafico4.scalePercent(50);
        }
        */
        document.add(myImgGrafico4);


        document.newPage();

        // Oitava Página
        marcaDagua.setAbsolutePosition(0, 0);
        document.add(marcaDagua);

        imgCantoInferior.setAbsolutePosition(230, 3);
        document.add(imgCantoInferior);

        document.add(espaco1Pg2);

        Paragraph titulo1pg8 = new Paragraph(new Phrase(30F , "Convênio SICREDI", tituloBranco3));
        titulo1pg8.setAlignment(Element.ALIGN_CENTER);

        // primeira linha
        PdfPTable table1Pg8 = new PdfPTable(2);
        table1Pg8.setTotalWidth(new float[]{ 500, 130 });
        table1Pg8.setLockedWidth(true);
        PdfPCell celula1Tabela1Pagina8 = new PdfPCell(new Phrase(titulo1pg8));
        celula1Tabela1Pagina8.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela1Pagina8.setFixedHeight(25);
        celula1Tabela1Pagina8.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela1Pagina8.setBorder(Rectangle.NO_BORDER);
        celula1Tabela1Pagina8.setColspan(2);
        table1Pg8.addCell(celula1Tabela1Pagina8);
        document.add(table1Pg8);

        document.add(espaco2Pg2);

        Font textoSicredlAzul1 = new Font(Font.FontFamily.HELVETICA, 25, Font.NORMAL);
        textoSicredlAzul1.setSize(14);
        textoSicredlAzul1.setColor(70,130,180);

        Font textoSicredlAzul2 = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
        textoSicredlAzul2.setSize(10);
        textoSicredlAzul2.setColor(70,130,180);

        ByteArrayOutputStream streamImagemSicred = new ByteArrayOutputStream();
        Bitmap imagemSicred = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imgsicred);
        //Bitmap tamanhoImg13 = Bitmap.createScaledBitmap(imagemSicred, 201, 54, true);
        imagemSicred.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemSicred);
        Image imgSicred = Image.getInstance(streamImagemSicred.toByteArray());

        imgSicred.scalePercent(15);
        int Width15 = (int) imgSicred.getScaledWidth();
        Log.d("mensagem","Width15: "+ Width15);
/*
        if(Width15 != 220){
            imgSicred.scalePercent(15);
        }
*/
        if(Width15 >= 220){
            imgSicred.scalePercent(15);

        }else if(Width15 < 220 && Width15 >= 177){
            imgSicred.scalePercent(17);

        }else if(Width15 < 177 && Width15 >= 134){
            imgSicred.scalePercent(19);

        }else if(Width15 < 134 && Width15 >= 93){
            imgSicred.scalePercent(22);

        }else if(Width15 < 93){
            imgSicred.scalePercent(25);
        }



        //imgSicred.scalePercent(15);
        /*
        if (width3 > 2000){
            imgSicred.scalePercent(25);
        }else{
            imgSicred.scalePercent(15);
        }
        */

        Paragraph pSicred = new Paragraph();
        pSicred.add(new Chunk(imgSicred, 0, 0, true));

        PdfPTable table2pg8 = new PdfPTable(2);
        table2pg8.setTotalWidth(new float[]{ 300, 230});
        table2pg8.setLockedWidth(true);
        PdfPCell cellTable2pg8;
        BigDecimal valorTaxaSicred = new BigDecimal(parcelaSicred).setScale(2, RoundingMode.HALF_EVEN);

        cellTable2pg8 = new PdfPCell(new Phrase(30F , "Financiamento de 100% do Projeto 60ª Parcelas financiamento bancário: R$ "+NumberFormat.getNumberInstance().format(valorTaxaSicred.doubleValue())+", Juros de 0,5% à 0,8% A.M. + CDI60 meses.", textoSicredlAzul1));
        cellTable2pg8.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cellTable2pg8.setBorder(Rectangle.NO_BORDER);
        cellTable2pg8.setPaddingRight(10);
        cellTable2pg8.setLeading(0f, 1.2f);
        cellTable2pg8.setPaddingTop(7);
        table2pg8.addCell(cellTable2pg8);
        cellTable2pg8 = new PdfPCell(new Phrase());
        cellTable2pg8.addElement(pSicred);
        cellTable2pg8.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTable2pg8.setPaddingLeft(30);
        cellTable2pg8.setBorder(Rectangle.NO_BORDER);
        table2pg8.addCell(cellTable2pg8);
        cellTable2pg8 = new PdfPCell(new Phrase(30F , "\n*As taxas de juros utilizadas como base nas simulações variam de acordo com a CDI, pois trata-se de uma taxa pós-fixada.\n" +
                "*Os valores de parcelas, número de parcelas e taxas apresentadas nesta proposta são apenas uma simulação de financiamento, podendo variar de acordo com a relação do associado com a instituição financeira.\n", textoSicredlAzul2));
        cellTable2pg8.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cellTable2pg8.setBorder(Rectangle.NO_BORDER);
        cellTable2pg8.setColspan(2);
        cellTable2pg8.setLeading(0f, 1.2f);
        table2pg8.addCell(cellTable2pg8);
        document.add(table2pg8);

        document.add(espaco1Pg2);

        Paragraph titulo3pg70 = new Paragraph(new Phrase(30F , "Análise Financeira do Projeto", tituloBranco3));
        titulo3pg70.setAlignment(Element.ALIGN_CENTER);

        // primeira linha
        PdfPTable table5Pg70 = new PdfPTable(2);
        table5Pg70.setTotalWidth(new float[]{ 500, 130 });
        table5Pg70.setLockedWidth(true);
        PdfPCell celula1Tabela5Pagina70 = new PdfPCell(new Phrase(titulo3pg70));
        celula1Tabela5Pagina70.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela5Pagina70.setFixedHeight(25);
        celula1Tabela5Pagina70.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela5Pagina70.setBorder(Rectangle.NO_BORDER);
        celula1Tabela5Pagina70.setColspan(2);
        table5Pg70.addCell(celula1Tabela5Pagina70);
        document.add(table5Pg70);

        document.add(espaco2Pg2);

        PdfPTable table7pg7 = new PdfPTable(2);
        table7pg7.setTotalWidth(new float[]{ 360, 130});
        table7pg7.setLockedWidth(true);
        PdfPCell cellTable7pg7;
        cellTable7pg7 = new PdfPCell(new Phrase(30F , "Simulação Financiamento", tituloBranco3));
        cellTable7pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setBackgroundColor(new BaseColor(91,155,213));
        cellTable7pg7.setColspan(2);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        cellTable7pg7 = new PdfPCell(new Phrase(30F , "Energia Produzida em 25 anos (kWh)", textoAzulTabela));
        cellTable7pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        cellTable7pg7 = new PdfPCell(new Phrase(30F , ""+NumberFormat.getNumberInstance().format(inteiroEnergiaProduzida25Anos), textoAzulCustos));
        cellTable7pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        cellTable7pg7 = new PdfPCell(new Phrase(30F , "Economia de energia em 25 anos (R$) ", textoAzulTabela));
        cellTable7pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        cellTable7pg7 = new PdfPCell(new Phrase(30F , ""+formatacao.format(EconomiaDeEnergiaProduzida25Anos), textoAzulCustos));
        cellTable7pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        cellTable7pg7 = new PdfPCell(new Phrase(30F , "Fluxo de Caixa Acumulado (R$)", textoAzulTabela));
        cellTable7pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);

        BigDecimal valorProximo3 = new BigDecimal(saldoAcumulado2Ano25).setScale(2, RoundingMode.HALF_EVEN);

        cellTable7pg7 = new PdfPCell(new Phrase(30F , ""+formatacao.format(saldoAcumulado2Ano25), textoAzulCustos));
        cellTable7pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        /*
        cellTable7pg7 = new PdfPCell(new Phrase(30F , "VPL [R$]", textoAzulTabela));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        cellTable7pg7 = new PdfPCell(new Phrase(30F , "R$ 3.903.146,09", textoAzulCustos));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        cellTable7pg7 = new PdfPCell(new Phrase(30F , "TIR [%]", textoAzulTabela));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        cellTable7pg7 = new PdfPCell(new Phrase(30F , "55%", textoAzulCustos));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        */
        cellTable7pg7 = new PdfPCell(new Phrase(30F , "PAYBACK (anos)", textoAzulTabela));
        cellTable7pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        cellTable7pg7 = new PdfPCell(new Phrase(30F , ""+contadorPayback2, textoAzulCustos));
        cellTable7pg7.setBorderColor(new BaseColor(70,130,180));
        cellTable7pg7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable7pg7.setPadding(4);
        table7pg7.addCell(cellTable7pg7);
        document.add(table7pg7);

        document.add(espaco2Pg2);
/*
        ByteArrayOutputStream streamImagemFluxoCaixaAno= new ByteArrayOutputStream();
        Bitmap imagemFluxoCaixaAno = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imgfluxocaixaano);
        imagemFluxoCaixaAno.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemFluxoCaixaAno);
        Image imgFluxoCaixaAno = Image.getInstance(streamImagemFluxoCaixaAno.toByteArray());
        imgFluxoCaixaAno.setAlignment(Element.ALIGN_CENTER);
        imgFluxoCaixaAno.scalePercent(17);
        document.add(imgFluxoCaixaAno);
*/
        ByteArrayOutputStream streamGrafico3 = new ByteArrayOutputStream();
        Bitmap bitmapGrafico3 = Bitmap.createBitmap(wvGrafico3.getWidth(), wvGrafico3.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas3 = new Canvas(bitmapGrafico3);
        wvGrafico3.draw(canvas3);

        //Bitmap tamanhoImg14 = Bitmap.createScaledBitmap(bitmapGrafico3, 450, 220, true);
        bitmapGrafico3.compress(Bitmap.CompressFormat.JPEG, 100, streamGrafico3);
        Image myImgGrafico3 = Image.getInstance(streamGrafico3.toByteArray());

        myImgGrafico3.setAlignment(Image.MIDDLE);

        myImgGrafico3.scalePercent(50);
        int Width16 = (int) myImgGrafico3.getScaledWidth();
        Log.d("mensagem","Width16: "+ Width16);
/*
        if(Width16 != 495){
            myImgGrafico3.scalePercent(100);
        }
*/
        if(Width16 >= 495){
            myImgGrafico3.scalePercent(50);

        }else if(Width16 < 495 && Width16 >= 397){
            myImgGrafico3.scalePercent(64);

        }else if(Width16 < 397 && Width16 >= 299){
            myImgGrafico3.scalePercent(77);

        }else if(Width16 < 299 && Width16 >= 201){
            myImgGrafico3.scalePercent(89);

        }else if(Width16 < 201){
            myImgGrafico3.scalePercent(100);
        }

        //myImgGrafico3.scalePercent(50);
        /*
        if (width3 > 2000){
            myImgGrafico3.scalePercent(90);
        }else{
            myImgGrafico3.scalePercent(50);
        }
        */
        document.add(myImgGrafico3);

/*
        Paragraph tituloGrafico3= new Paragraph(new Phrase(30F , "Fluxo de Caixa do mês para cada ano do Financiamento", textoObsAzul));
        tituloGrafico3.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloGrafico3);
*/

        Font textoAzulPG9 = new Font(Font.FontFamily.HELVETICA, 25, Font.BOLD);
        textoAzulPG9.setSize(18);
        textoAzulPG9.setColor(70,130,180);

        Font textoAzulPG92 = new Font(Font.FontFamily.HELVETICA, 25, Font.NORMAL);
        textoAzulPG92.setSize(14);
        textoAzulPG92.setColor(70,130,180);

        document.newPage();

        // Nona Página
        marcaDagua.setAbsolutePosition(0, 0);
        document.add(marcaDagua);

        //imgCantoInferior.setAbsolutePosition(230, 3);
        if(Width1 >= 124){
            imgCantoInferior.setAbsolutePosition(230, 3);

        }else if(Width1 < 124 && Width1 >= 99){
            imgCantoInferior.setAbsolutePosition(235, 3);

        }else if(Width1 < 99 && Width1 >= 74){
            imgCantoInferior.setAbsolutePosition(240, 3);

        }else if(Width1 < 74 && Width1 >= 50){
            imgCantoInferior.setAbsolutePosition(245, 3);

        }else if(Width1 < 50){
            imgCantoInferior.setAbsolutePosition(250, 3);
        }

        document.add(imgCantoInferior);

        document.add(espaco1Pg2);

        Paragraph titulo3pg700 = new Paragraph(new Phrase(30F , "Validade da Proposta", tituloBranco3));
        titulo3pg700.setAlignment(Element.ALIGN_CENTER);

        // primeira linha
        PdfPTable table5Pg700 = new PdfPTable(2);
        table5Pg700.setTotalWidth(new float[]{ 500, 130 });
        table5Pg700.setLockedWidth(true);
        PdfPCell celula1Tabela5Pagina700 = new PdfPCell(new Phrase(titulo3pg700));
        celula1Tabela5Pagina700.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela5Pagina700.setFixedHeight(25);
        celula1Tabela5Pagina700.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela5Pagina700.setBorder(Rectangle.NO_BORDER);
        celula1Tabela5Pagina700.setColspan(2);
        table5Pg700.addCell(celula1Tabela5Pagina700);
        document.add(table5Pg700);

        //data
/*
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = df.format(data);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(s);
     */


        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String s = df.format(data);

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime( new Date(String.valueOf(s)) );

        int dia = cal.getActualMaximum( Calendar.DAY_OF_MONTH );
        //int mes = cal.get(Calendar.MONDAY)-1;
        int mes = cal.get(Calendar.MONTH) +1;
        if(mes==13){
            mes = 12;
        }
        Log.d("mensagem","Data teste:"+mes);
        //mes = mes+1;
        int ano = cal.get(Calendar.YEAR);

        //System.out.println( dia+"/"+mes+"/"+ano );
        //Date dataNova;
        String DataNova = dia+"/"+mes+"/"+ano;
/*
        try {
            dataNova = (new SimpleDateFormat("dd/MM/yyyy")).parse( dia+"/"+mes+"/"+ano );
        } catch (ParseException e) {
            e.printStackTrace();
        }
*/
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(data);
        //System.out.println(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        Paragraph textoValidade = new Paragraph(new Phrase(30F , "\n      Esta proposta é válida até "+DataNova+" ou término dos estoques. Após os valores deverão ser recalculados.", textoAzulPG92));
        textoValidade.setAlignment(Element.ALIGN_JUSTIFIED);
        textoValidade.setLeading(20);
        textoValidade.setIndentationLeft(40);
        textoValidade.setIndentationRight(40);
        document.add(textoValidade);

        document.add(espaco1Pg2);

        Paragraph titulo1pg9 = new Paragraph(new Phrase(30F , "Considerações Finais", tituloBranco3));
        titulo1pg9.setAlignment(Element.ALIGN_CENTER);

        // primeira linha
        PdfPTable table1Pg9 = new PdfPTable(2);
        table1Pg9.setTotalWidth(new float[]{ 500, 130 });
        table1Pg9.setLockedWidth(true);
        PdfPCell celula1Tabela1Pagina9 = new PdfPCell(new Phrase(titulo1pg9));
        celula1Tabela1Pagina9.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela1Pagina9.setFixedHeight(25);
        celula1Tabela1Pagina9.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela1Pagina9.setBorder(Rectangle.NO_BORDER);
        celula1Tabela1Pagina9.setColspan(2);
        table1Pg9.addCell(celula1Tabela1Pagina9);
        document.add(table1Pg9);

        Paragraph paragrafo2pg9 = new Paragraph(new Phrase(30F , "\n      A proposta em questão é um estudo baseado na fatura de energia elétrica e na localização, os valores aqui pré-projetados podem sofrer alterações devido as condições específicas do local onde será realizado o projeto. Valores sujeitos à variações devido às incertezas associadas as tarifas elétricas, variabilidades do recurso solar e níveis de tensão da rede.", textoAzulPG92));
        paragrafo2pg9.setAlignment(Element.ALIGN_JUSTIFIED);
        paragrafo2pg9.setLeading(20);
        paragrafo2pg9.setIndentationLeft(40);
        paragrafo2pg9.setIndentationRight(40);
        document.add(paragrafo2pg9);

        document.add(espaco1Pg2);
        document.add(espaco1Pg2);
        document.add(espaco1Pg2);
        document.add(espaco1Pg2);
        document.add(espaco1Pg2);
        document.add(espaco1Pg2);
        document.add(espaco1Pg2);
        document.add(espaco1Pg2);

        ByteArrayOutputStream streamImagemABSolar= new ByteArrayOutputStream();
        Bitmap imagemABSolar = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.imgabsolar);
        imagemABSolar.compress(Bitmap.CompressFormat.JPEG, 100, streamImagemABSolar);
        Image imgABSolar = Image.getInstance(streamImagemABSolar.toByteArray());
        imgABSolar.setAlignment(Element.ALIGN_CENTER);
        //imgABSolar.scalePercent(25);

        imgABSolar.scalePercent(25);
        int Width17 = (int) imgABSolar.getScaledWidth();
        Log.d("mensagem","Width17: "+ Width17);
/*
        if(Width17 != 171){
            imgABSolar.scalePercent(50);
        }
*/

        if(Width17 >= 171){
            imgABSolar.scalePercent(25);

        }else if(Width17 < 171 && Width17 >= 138){
            imgABSolar.scalePercent(31);

        }else if(Width17 < 138 && Width17 >= 105){
            imgABSolar.scalePercent(38);

        }else if(Width17 < 105 && Width17 >= 72){
            imgABSolar.scalePercent(44);

        }else if(Width17 < 72){
            imgABSolar.scalePercent(50);
        }

        /*
        if (width3 > 2000){
            imgABSolar.scalePercent(40);
        }else{
            imgABSolar.scalePercent(25);
        }
        */
        document.add(imgABSolar);

        document.newPage();

        // Decima Página
        marcaDagua.setAbsolutePosition(0, 0);
        document.add(marcaDagua);

        imgCantoInferior.setAbsolutePosition(230, 3);
        document.add(imgCantoInferior);

        document.add(espaco1Pg2);

        Paragraph titulo1pg10 = new Paragraph(new Phrase(30F , "Termo de Aceitação da Proposta", tituloBranco3));
        titulo1pg10.setAlignment(Element.ALIGN_CENTER);

        // primeira linha
        PdfPTable table1Pg10 = new PdfPTable(2);
        table1Pg10.setTotalWidth(new float[]{ 500, 130 });
        table1Pg10.setLockedWidth(true);
        PdfPCell celula1Tabela1Pagina10 = new PdfPCell(new Phrase(titulo1pg10));
        celula1Tabela1Pagina10.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula1Tabela1Pagina10.setFixedHeight(25);
        celula1Tabela1Pagina10.setBackgroundColor(new BaseColor(91,155,213));
        celula1Tabela1Pagina10.setBorder(Rectangle.NO_BORDER);
        celula1Tabela1Pagina10.setColspan(2);
        table1Pg10.addCell(celula1Tabela1Pagina10);
        document.add(table1Pg10);

        Paragraph paragrafo2pg10 = new Paragraph(new Phrase(30F , "\nÀ\n" +
                "HCC Engenharia\n", textoAzulPG92));
        paragrafo2pg10.setAlignment(Element.ALIGN_CENTER);
        paragrafo2pg10.setLeading(20);
        paragrafo2pg10.setIndentationLeft(40);
        paragrafo2pg10.setIndentationRight(40);
        document.add(paragrafo2pg10);

        Paragraph paragrafo3pg10 = new Paragraph(new Phrase(30F , "\n      Ref. a proposta ID "+proposta+" – A presente proposta tem como principal objetivo o Fornecimento de Materiais e Mão de Obra para construção de Microgeração Distribuída Fotovoltaica, bem como a Elaboração de Projeto Elétrico e Liberação do mesmo junto a concessionária. Denominado Microgeração Distribuída Fotovoltaica de "+sistemaFinal+" kWp", textoAzulPG92));
        paragrafo3pg10.setAlignment(Element.ALIGN_JUSTIFIED);
        paragrafo3pg10.setLeading(20);
        paragrafo3pg10.setIndentationLeft(40);
        paragrafo3pg10.setIndentationRight(40);
        document.add(paragrafo3pg10);

        document.add(espaco1Pg2);

        PdfPTable table2pg10 = new PdfPTable(2);
        table2pg10.setTotalWidth(new float[]{ 360, 130});
        table2pg10.setLockedWidth(true);
        PdfPCell cellTable2pg10;
        cellTable2pg10 = new PdfPCell(new Phrase(30F , "Descrição", textoAzuldescricao));
        cellTable2pg10.setBorderColor(new BaseColor(70,130,180));
        cellTable2pg10.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable2pg10.setPadding(10);
        table2pg10.addCell(cellTable2pg10);
        cellTable2pg10 = new PdfPCell(new Phrase(30F , "Valor", textoAzuldescricao));
        cellTable2pg10.setBorderColor(new BaseColor(70,130,180));
        cellTable2pg10.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable2pg10.setPadding(10);
        table2pg10.addCell(cellTable2pg10);
        cellTable2pg10 = new PdfPCell(new Phrase(30F , "01 – Instalação de Microgeração Distribuída Fotovoltaica de "+sistemaFinal+" kWp, com fornecimento de todo material, incluindo Módulos Fotovoltaicos, Inversor, Estruturas, Fiação, Mão de obra especializada e Projeto aprovado na Concessionária.", textoAzulCustos));
        cellTable2pg10.setBorderColor(new BaseColor(70,130,180));
        cellTable2pg10.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        cellTable2pg10.setLeading(0f, 1.2f);
        cellTable2pg10.setPadding(10);
        table2pg10.addCell(cellTable2pg10);
        cellTable2pg10 = new PdfPCell(new Phrase(30F , "\n\n R$ "+NumberFormat.getNumberInstance().format(valor1.doubleValue()), textoAzulMenor));
        cellTable2pg10.setBorderColor(new BaseColor(70,130,180));
        cellTable2pg10.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable2pg10.setPadding(10);
        table2pg10.addCell(cellTable2pg10);
        cellTable2pg10 = new PdfPCell(new Phrase(30F , "Total Geral", textoAzulCustos));
        cellTable2pg10.setBorderColor(new BaseColor(70,130,180));
        cellTable2pg10.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable2pg10.setPadding(10);
        table2pg10.addCell(cellTable2pg10);
        cellTable2pg10 = new PdfPCell(new Phrase(30F , "R$ "+NumberFormat.getNumberInstance().format(valor1.doubleValue()), textoAzulMenor));
        cellTable2pg10.setBorderColor(new BaseColor(70,130,180));
        cellTable2pg10.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTable2pg10.setPadding(10);
        table2pg10.addCell(cellTable2pg10);
        document.add(table2pg10);

        document.add(espaco2Pg2);

        Paragraph paragrafo4pg10 = new Paragraph(new Phrase(30F , "      Manifestamos a aceitação da proposta referida. Assim sendo, solicitamos as providências necessárias para a execução do serviço comprometendo-nos em caso de desistência com pagamento de multa de 10% no valor da proposta. ", textoAzulPG92));
        paragrafo4pg10.setAlignment(Element.ALIGN_JUSTIFIED);
        paragrafo4pg10.setLeading(20);
        paragrafo4pg10.setIndentationLeft(40);
        paragrafo4pg10.setIndentationRight(40);
        document.add(paragrafo4pg10);

        document.add(espaco2Pg2);

        Paragraph paragrafo5pg10 = new Paragraph(new Phrase(30F , "\nData:_____/_____/_________ . \n\n" +
                "Assinatura: ____________________________ . \n\n" +
                "Nome: ________________________________ . \n\n", textoAzulTitulos));
        paragrafo5pg10.setAlignment(Element.ALIGN_LEFT);
        paragrafo5pg10.setLeading(20);
        paragrafo5pg10.setIndentationLeft(40);
        document.add(paragrafo5pg10);

/*
        ArrayList<BarEntry> barEntradas =  new ArrayList<>();
        barEntradas.add(new BarEntry(40f,0));
        barEntradas.add(new BarEntry(44f,1));
        barEntradas.add(new BarEntry(54f,2));
        barEntradas.add(new BarEntry(62f,3));
        barEntradas.add(new BarEntry(65f,4));
        barEntradas.add(new BarEntry(70f,5));

        BarDataSet todosDados = new BarDataSet(barEntradas,"texto");

        ArrayList<String> barDados =  new ArrayList<>();
        barDados.add("Janeiro");
        barDados.add("Fevereiro");
        barDados.add("Março");
        barDados.add("Abril");
        barDados.add("Maio");
        barDados.add("Outubro");

        BarData dados = new BarData(todosDados);
        barChart.setData(dados);
        */
        //barChart.setVisibility(View.GONE);
/*
        Bitmap bitmap = Bitmap.createBitmap(barChart.getWidth(), barChart.getHeight(), Bitmap.Config.ARGB_8888);

        ByteArrayOutputStream streamGrafico = new ByteArrayOutputStream();
        Bitmap bitmapGrafico = barChart.getChartBitmap();
        bitmapGrafico.compress(Bitmap.CompressFormat.JPEG, 100, streamGrafico);
        Image myImgGrafico = Image.getInstance(streamGrafico.toByteArray());
        myImgGrafico.setAlignment(Image.MIDDLE);
        document.add(myImgGrafico);
*/

        //Bitmap mBitmap;
        //Layout webViewContainer
        //mBitmap =  Bitmap.createBitmap(wvGrafico.getWidth(), wvGrafico.getHeight(), Bitmap.Config.ARGB_8888);
        //Canvas canvas = new Canvas(mBitmap);
        //webViewContainer.draw(canvas);
        //strURL = "https://chart.googleapis.com/chart?cht=lc&chxt=x,y&chs=300x300&chd=t:10,45,5,10,13,26&chl=Janeiro|Fevereiro|Marco|Abril|Maio|Junho&chdl=Vendas%20&chxr=1,0,50&chds=0,25&chg=0,5,0,0&chco=3D7930&chtt=Vendas+x+1000&chm=v,FF0000,0,::.10,4";
        //strURL = "http://chart.apis.google.com/chart?cht=bhg&chs=550x400&chd=t:100,50,115,80|10,20,15,30&chxt=x,y&chxl=1:|Janeiro|Fevereiro|Marco|Abril&chxr=0,0,120&chds=0,120&chco=4D89F9&chbh=35,0,15&chg=8.33,0,5,0&chco=0A8C8A,EBB671&chdl=Vendas|Compras";
        //strURL = "https://chart.googleapis.com/chart?cht=lc&chxt=x,y&chs=700x350&chd=t:10,45,5,10|30,35,30,15|10,10.5,30,35&chl=Janeiro|Fevereiro|Marco|Abril&chdl=Vendas|Compras|Outros&chxr=1,0,50&chds=0,50&chg=0,5,0,0&chco=DA3B15,3072F3,000000&chtt=grafico+de+vendas";
        //strURL = "https://chart.googleapis.com/chart?cht=p3&chs=200x90&chd=t:40,45,5,10&chl=Jan|Fev|Mar|Abr";
        //strURL = "http://chart.apis.google.com/chart?chxl=0:|Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec&chxt=x,y&chs=300x300&cht=r&chco=FF0000&chd=t:63,64,67,73,77,81,85,86,85,81,74,67,63&chls=2,4,0&chm=B,FF000080,0,0,0";
        //wvGrafico.loadUrl(strURL);

        document.close();


        //startActivity(new Intent(VisualizaProposta.this, grafico.class));
        //finish();
    }

}
