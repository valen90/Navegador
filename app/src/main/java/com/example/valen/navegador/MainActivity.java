package com.example.valen.navegador;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.BundleCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView wv;
    TextView tv;
    ProgressBar progressBar;
    int nRegistro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wv = (WebView) findViewById(R.id.webView);
        tv = (TextView) findViewById(R.id.tfDireccion);

        wv.loadUrl("http://www.google.es");
        wv.setWebViewClient(new WebViewClient());
        try {
            Bundle bundle = getIntent().getExtras();
            if (!bundle.isEmpty()) {
                String pag = bundle.getString("dir");
                if(pag!=null)
                    wv.loadUrl("http://" + pag);
            }
        }catch(Exception e){}
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        wv.setWebChromeClient(new WebChromeClient() {
            private int progress;
            public void setProgress(int progress) {
                this.progress = progress;
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                this.setProgress(progress * 1000);
                progressBar.incrementProgressBy(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        tv.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    visitarWeb();
                    return true;
                }
                return false;
            }
        });
    }

    public void pulsarBuscar(View view){
        visitarWeb();
    }

    public void pulsarHistorial(View view){
        Intent i = new Intent(this,HistorialActivity.class);
        startActivity(i);
        finish();
    }

    public void alta(String url){
        AdminSQLite admin = new AdminSQLite(this,"administracion",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("direccion",url);
        bd.insert("paginas",null,registro);
        bd.close();
        Toast.makeText(this,"Pagina guardada",Toast.LENGTH_SHORT).show();
    }

    public void visitarWeb(){
        wv.setWebViewClient(new WebViewClient());
        String s = tv.getText().toString();
        if(s.contains("http://")){
            s = s.substring(7);
        }
        wv.loadUrl("http://"+s);
        alta(s);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
    }

}
