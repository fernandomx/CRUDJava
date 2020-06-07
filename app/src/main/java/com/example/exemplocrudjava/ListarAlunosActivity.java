package com.example.exemplocrudjava;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ListarAlunosActivity extends AppCompatActivity {


    private ListView listView;
    private AlunoDAO  dao;
    private List<Aluno> alunos;
    private List<Aluno> alunosFiltrados = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_alunos);

        listView = findViewById(R.id.lista_alunos);
        dao = new AlunoDAO(this);

        alunos = dao.obterTodos(); // recebe a lista de alunos
        alunosFiltrados.addAll(alunos);
        //criar adaptador do android para passar para listview da tela
        //ArrayAdapter<Aluno> adaptador = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunosFiltrados);
        //alterado para um adapter personalizado
        AlunoAdapter adaptador = new AlunoAdapter(this,alunosFiltrados);


        listView.setAdapter(adaptador);

        //Registrar menu de contexto com a listview
        registerForContextMenu(listView);

    }

    //sobreescrever metodo createoptionsmenu
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal,menu);

        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                procurarAluno(newText);
                return false;
            }
        });


        return true;
    }

    //menu de contexto (auxiliar)
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);

        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto,menu);

    }


    public void procurarAluno(String nome) {
        alunosFiltrados.clear();
        for(Aluno a: alunos) {
            if (a.getNome().toLowerCase().contains(nome.toLowerCase())) {
                alunosFiltrados.add(a);
            }
        }
        listView.invalidateViews();
    }

    //metodo excluir
    public void excluir(MenuItem item) {

        //obter a posição do item na lista
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Aluno alunoExcluir = alunosFiltrados.get(menuInfo.position);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Deseja excluir o aluno?")
                .setNegativeButton("Não",null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alunosFiltrados.remove(alunoExcluir);
                        alunos.remove(alunoExcluir);
                        dao.excluir(alunoExcluir);
                        listView.invalidateViews();
                    }
                }).create();
        dialog.show();


    }


    public void atualizar(MenuItem item){

        //obter a posição do item na lista
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Aluno alunoAtualizar = alunosFiltrados.get(menuInfo.position);
        Intent it = new Intent(this, CadastroAlunoActivity.class);
        it.putExtra("aluno", alunoAtualizar);
        startActivity(it);


    }

    //metodo para vincular com XML do menu
    public void cadastrar(MenuItem item) {
        //intent para abrir a tela de cadastro do Aluno
        Intent it = new Intent(this, CadastroAlunoActivity.class);
        startActivity(it);
    }

    //sobreescrever metodo onResume para listar os dados novamente
    @Override
    public void onResume(){
        super.onResume();
        alunos = dao.obterTodos();
        alunosFiltrados.clear(); //limpar lista filtrada
        alunosFiltrados.addAll(alunos); //adicionar a listagem novamente em filtrados
        listView.invalidateViews(); //invalidar os dados da listview
    }


}