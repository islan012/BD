package br.senac.rn.bancodados;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.app.AlertDialog.Builder;



public class MainActivity extends AppCompatActivity {
    EditText editNome, editContato, editTipo;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.editNome);
        editContato = findViewById(R.id.editContato);
        editTipo = findViewById(R.id.editTipo);

        db = openOrCreateDatabase("ContatosDB", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS contatos(Nome VARCHAR, Contato VARCHAR, Tipo VARCHAR);");
    }

    public void btAdc(View view) {
        if (editNome.getText().toString().trim().length() == 0 || editContato.getText().toString().trim().length() == 0 || editTipo.getText().toString().trim().length() == 0) {
            showMessage("Erro", "Preencha Corretamente os valores");
            return;
        }

        db.execSQL("INSERT INTO contatos VALUES('" + editNome.getText() + "','" + editContato.getText() + "','" + editTipo.getText() + "')");
        showMessage("Ok", "Dados Gravados");
        clearText();
    }

    public void btDel(View view) {
        if (editNome.getText().toString().trim().length() == 0) {
            showMessage("Erro", "Entre com o Nome");
            return;
        }
        Cursor c = db.rawQuery("SELECT * FROM contatos WHERE Nome= '" + editNome.getText() + "'", null);
        if (c.moveToFirst()) {
            db.execSQL("DELETE FROM contatos WHERE Nome='" + editNome.getText() + "'");
            showMessage("Sucesso", "Dados Deletados");
        } else {
            showMessage("Erro", "Nome inválido");
        }
        clearText();
    }

    public void btSalvarEdit(View view) {
        if (editNome.getText().toString().trim().length() == 0) {
            showMessage("Erro", "Favor entrar com o nome");
            return;
        }
        Cursor c = db.rawQuery("SELECT * FROM contatos WHERE Nome= '" + editNome.getText() + "'", null);
        if (c.moveToFirst()) {
            db.execSQL("UPDATE contatos SET Contato='" + editContato.getText() + "',Tipo='" + editTipo.getText() + "' WHERE Nome='" + editNome.getText() + "'");
            showMessage("Sucesso", "Dados Editados");
        } else {
            showMessage("Erro", "Faça uma busca primeiro");
            clearText();
        }
    }

    public void btBuscarContato(View view) {
        if (editNome.getText().toString().trim().length() == 0) {
            showMessage("Erro", "Favor entrar com o nome");
            return;
        }
        Cursor c = db.rawQuery("SELECT * FROM contatos WHERE Nome= ?", new String[]{editNome.getText().toString()});
        if (c.moveToFirst()) {
            editNome.setText(c.getString(c.getColumnIndex("Nome")));
            editContato.setText(c.getString(c.getColumnIndex("Contato")));
            editTipo.setText(c.getString(c.getColumnIndex("Tipo")));
        } else {
            showMessage("Erro", "Nenhum contato encontrado com esse nome");
            clearText();
        }
    }


    public void btListaContato(View view) {
        Cursor c = db.rawQuery("SELECT * FROM contatos", null);
        if (c.getCount() == 0) {
            showMessage("Erro", "Nada encontrado");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            buffer.append("Nome: " + c.getString(c.getColumnIndex("Nome")) + "\n");
        }
        showMessage("Detalhes dos Contatos", buffer.toString());
    }

    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        editNome.setText("");
        editContato.setText("");
        editTipo.setText("");
    }

    public void btLimparContatos(View view) {
        editNome.setText("");
        editContato.setText("");
        editTipo.setText("");
    }
}
