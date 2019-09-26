package com.williantaiguara.anota.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.williantaiguara.anota.R;
import com.williantaiguara.anota.adapter.AdapterAjuda;
import com.williantaiguara.anota.model.Ajuda;

import java.util.ArrayList;
import java.util.List;

public class SobreActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSobre;
    private List<Ajuda> sobreList = new ArrayList<>();
    private AdapterAjuda adapterSobre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        Toolbar toolbar = findViewById(R.id.toolbarSobre);
        toolbar.setTitle("Sobre");
        setSupportActionBar(toolbar);

        recyclerViewSobre = findViewById(R.id.recyclerViewSobre);

        faq();

        adapterSobre = new AdapterAjuda(sobreList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewSobre.setLayoutManager(layoutManager);
        recyclerViewSobre.setHasFixedSize(true);
        recyclerViewSobre.setAdapter(adapterSobre);
        recyclerViewSobre.addItemDecoration(new DividerItemDecoration(recyclerViewSobre.getContext(), DividerItemDecoration.VERTICAL));
    }

    public void faq(){
        sobreList.add(new Ajuda(
                "O que é o Anota?",
                "O Anota foi criado e pensado para ajudar os alunos a reunir todas as informações que o estudante irá precisar durante o curso, desde o calendário com as matérias do semestre até desenvolver resumos para auxiliar nos estudos para as provas. Além de estar servindo para montar um histórico de notas e faltas, armazenando todas as informações que forem cadastradas, do semestre de calouro até o último semestre, sendo possível o aluno acessar todas as informações de todas as matérias cadastradas no decorrer do tempo.\n"
        ));
        sobreList.add(new Ajuda(
                "Por que usar o Anota ?",
                "Com design simples e intuitivo, o Anota é capaz de agradar dos mais simples usuários até os mais exigentes na hora de fazer anotações sobre o curso, sejam elas anotações sobre notas, faltas, resumos escolares, lembretes e até mesmo armazenamento de imagens importantes.\n"
        ));
        sobreList.add(new Ajuda(
                "O aplicativo é pago ?",
                "O app é totalmente gratuito."
        ));
        sobreList.add(new Ajuda(
                "Gostaria de sugerir algo ?",
                "O feedback do Cliente é uma experiência no Anota que permite aos usuários compartilharem com a nossa equipe suas opiniões após a interação com o nosso aplicativo.\n"
        ));
        sobreList.add(new Ajuda(
                "Como atualizar o Anota?",
                "Nós recomendamos que você sempre utilize a última versão do Anota disponível. As últimas versões têm os recursos mais recentes e correções de erros. para isso vá na para a  Play Store e toque em Menu  > Meus apps e jogos. Toque no botão ATUALIZAR que aparece ao lado do app Anota. Você também pode abrir a Play Store e pesquisar pelo aplicativo Anota e toque em ATUALIZAR .\n"
        ));
        sobreList.add(new Ajuda(
                "Esqueci a senha. Como recupero?",
                "Acesse o botão ENTRAR localizado na tela inicial. Clique no botão ??? e preencha com os dados solicitados. Você receberá em alguns instantes um link em seu email para efetuar o cadastramento da sua nova senha.\n"
        ));
        sobreList.add(new Ajuda(
                "Qual a forma de Autenticação ?",
                "Existe apenas uma forma de autenticação:\n" +
                        "Usar o seu login de usuário e senha. O usuário será o email informado na criação da conta \n"
        ));
    }
}
