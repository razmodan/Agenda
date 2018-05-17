package br.com.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.agenda.modelo.Aluno;

public class AlunoDAO extends SQLiteOpenHelper{
    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos(id CHAR(36) PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "caminhoFoto TEXT, " +
                "nota REAL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";

        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE Alunos ADD COLUMN caminhoFoto TEXT;";
                db.execSQL(sql);
            case 2:
                String criandoTabelaNova = "CREATE TABLE Alunos_novo " +
                        "(id CHAR(36) PRIMARY KEY," +
                        "nome TEXT NOT NULL, " +
                        "endereco TEXT, " +
                        "telefone TEXT, " +
                        "site TEXT, " +
                        "nota REAL, " +
                        "caminhoFoto TEXT);";
                db.execSQL(criandoTabelaNova);

                String inserindoAlunosNaTabelaNova = "INSERT INTO Alunos_novo " +
                        "(id, nome, endereco, telefone, site, nota, caminhoFoto) " +
                        "SELECT id, nome, endereco, telefone, site, nota, caminhoFoto " +
                        "FROM Alunos";
                db.execSQL(inserindoAlunosNaTabelaNova);

                String removendoTabelaAntiga = "DROP TABLE Alunos";
                db.execSQL(removendoTabelaAntiga);

                String alterandoNomeDaTabelaNova = "ALTER TABLE Alunos_novo RENAME TO Alunos";
                db.execSQL(alterandoNomeDaTabelaNova);

        }
    }

    public void insere(Aluno aluno){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = new ContentValues();
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("caminhoFoto", aluno.getCaminhoFoto());
        dados.put("nota", aluno.getNota());

        long id = db.insert("Alunos", null, dados);
        aluno.setId(id);
    }

    public List<Aluno> buscaAlunos(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Alunos;", null);
        List<Aluno> alunos = new ArrayList<Aluno>();

        while (c.moveToNext()){
            Aluno aluno = new Aluno();

            aluno.setId(c.getLong(c.getColumnIndex("id")));
            aluno.setNome(c.getString(c.getColumnIndex("nome")));
            aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
            aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
            aluno.setSite(c.getString(c.getColumnIndex("site")));
            aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
            aluno.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);
        }

        c.close();
        return alunos;
    }

    public void deleta(Aluno aluno){
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {aluno.getId().toString()};

        db.delete("Alunos", "id = ?", params);
    }

    public void altera(Aluno aluno){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues dados = new ContentValues();
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("caminhoFoto", aluno.getCaminhoFoto());
        dados.put("nota", aluno.getNota());

        String[] params = {aluno.getId().toString()};
        db.update("Alunos", dados, "id = ?", params);
    }

    public boolean isAluno(String telefone){
        String sql = "SELECT * FROM Alunos WHERE telefone = ?";
        Cursor cursor = getReadableDatabase().rawQuery(sql, new String[]{telefone});

        int count = cursor.getCount();

        return count > 0;
    }


}
