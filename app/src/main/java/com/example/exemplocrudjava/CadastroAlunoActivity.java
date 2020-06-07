package com.example.exemplocrudjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroAlunoActivity extends AppCompatActivity {

    private EditText nome;
    private EditText cpf;
    private EditText telefone;
    private AlunoDAO dao;
    private Aluno aluno=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_aluno);

        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCPF);
        telefone = findViewById(R.id.editTelefone);

        //instanciar a classe AlunoDAO com context this
        dao = new AlunoDAO(this);

        Intent it = getIntent();
        if (it.hasExtra("aluno")){
            aluno = (Aluno) it.getSerializableExtra("aluno");
            nome.setText(aluno.getNome());
            cpf.setText(aluno.getCpf());
            telefone.setText(aluno.getTelefone());
        }

    }
    public void Salvar(View view) {

        if (aluno == null) {

            aluno = new Aluno();

            aluno.setNome(nome.getText().toString());
            aluno.setCpf(cpf.getText().toString());
            aluno.setTelefone(telefone.getText().toString());

            //metodo inserir classe DAO para inserir o aluno
            long id = dao.inserir(aluno);
            Toast.makeText(this, "Aluno inserido com id" + id,Toast.LENGTH_SHORT).show();
        }else {
            aluno.setNome(nome.getText().toString());
            aluno.setCpf(cpf.getText().toString());
            aluno.setTelefone(telefone.getText().toString());
            dao.atualizar(aluno);
            Toast.makeText(this, "Aluno foi atualizado",Toast.LENGTH_SHORT).show();
        }

    }
}