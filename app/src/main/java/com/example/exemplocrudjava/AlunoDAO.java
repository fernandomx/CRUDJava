package com.example.exemplocrudjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    private Conexao conexao;
    private SQLiteDatabase banco;

    //Construtor com conexao criada, iniciar banco com a conexao para gravacao
    public AlunoDAO(Context context){
        conexao = new Conexao(context);
        banco = conexao.getWritableDatabase();

    }

    //metodo para inserir aluno com os valores para tabela aluno
    // metodo de retorno é longo, banco retorno o ID do aluno criado
    public long inserir(Aluno aluno){
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("cpf",aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        //tabela, se haverá colunas vazias, e os valores
        return banco.insert("aluno",null, values);
     }

     //metodo que retorna uma lista de alunos
    public List<Aluno> obterTodos(){
        List<Aluno> alunos = new ArrayList<>();

        //faz a consulta no banco e popula o cursor, ponteiro
        Cursor cursor = banco.query("aluno",new String[]{"id","nome","cpf","telefone"},null,null,null,null,null,null);

        //percorre o cursor até encontrar o próximo e adiciona aluno na listagem
        while (cursor.moveToNext()){

            Aluno a = new Aluno();
            a.setId(cursor.getInt(0));
            a.setNome(cursor.getString(1));
            a.setCpf(cursor.getString(2));
            a.setTelefone(cursor.getString(3));
            alunos.add(a);
        }
        return alunos;
    }

    public void excluir(Aluno a) {
        banco.delete("aluno", "id= ?", new String[]{a.getId().toString()});

    }

    public void atualizar(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("cpf",aluno.getCpf());
        values.put("telefone", aluno.getTelefone());
        banco.update("aluno",values,"id= ?", new String[]{aluno.getId().toString()});
    }

}
