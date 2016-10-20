package com.example.valen.navegador;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class HistorialActivity extends AppCompatActivity {
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        String web = "hay";
        AdminSQLite admin = new AdminSQLite(this,"administracion",null,1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        Cursor c = bd.query("paginas", new String[]{"direccion"},null,null,null,null,null);
        layout = (LinearLayout) findViewById(R.id.linScroLay);
        if(c.moveToFirst()) {

            while(c.moveToNext()) {
                web = c.getString(0) + "";
                Button buyButton = new Button(this);
                buyButton.setText(web);
                buyButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                layout.addView(buyButton);
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        met(((Button) v).getText().toString());
                    }
                });
            }
        }
        bd.close();
    }

    private void met(String s){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("dir",s);
        startActivity(intent);
        finish();

    }
}
