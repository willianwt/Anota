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

public class AjudaActivity extends AppCompatActivity {
    private RecyclerView recyclerViewAjuda;
    private List<Ajuda> ajudaList = new ArrayList<>();
    private  AdapterAjuda adapterAjuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);
        Toolbar toolbar = findViewById(R.id.toolbarAjuda);
        toolbar.setTitle("Tutorial");
        setSupportActionBar(toolbar);

        recyclerViewAjuda = findViewById(R.id.recyclerViewAjuda);

        tutorial();

        adapterAjuda = new AdapterAjuda(ajudaList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewAjuda.setLayoutManager(layoutManager);
        recyclerViewAjuda.setHasFixedSize(true);
        recyclerViewAjuda.setAdapter(adapterAjuda);
        recyclerViewAjuda.addItemDecoration(new DividerItemDecoration(recyclerViewAjuda.getContext(), DividerItemDecoration.VERTICAL));

    }

    public void tutorial(){
        ajudaList.add(new Ajuda(
                "Curso",
                "Incluir > Clique no botão localizado no canto superior esquerdo>Seleciona a opção “ + Adicionar Curso”> Preencha os dados solicitados e clique em “→”.\n" +
                        "\n" +
                        "Excluir >Deslize o curso que deseja excluir  para direita ou esquerda >Aparecerá uma mensagem de confirmação para exclusão> Pressione ”Confirmar”.  OBS: todos as informações contidas no curso serão excluídas juntamente.\n"
        ));
        ajudaList.add(new Ajuda(
                "Semestre",
                "Incluir > Clique no curso que deseja realizar a inclusão> Seleciona o botão “+” localizado no canto inferior direito da tela > Preencha os dados solicitados e clique em “→”.\n" +
                        "\n" +
                        "Excluir >Deslize o semestre que deseja excluir  para direita ou esquerda >Aparecerá uma mensagem de confirmação para exclusão> Pressione ”Confirmar”.  OBS: todos as informações contidas no semestre serão excluídas juntamente.\n"
        ));
        ajudaList.add(new Ajuda(
                "Disciplina",
                "Incluir > Clique no curso que deseja realizar a inclusão>  Selecione o semestre que deseja incluir a disciplina> Seleciona o botão “+” localizado no canto inferior direito da tela > Preencha os dados solicitados e clique em “Adicionar Disciplina”> Em seguida clique no ícone localizado no canto superior direito da tela.\n" +
                        "\n" +
                        "Excluir >Deslize a disciplina que deseja excluir  para direita ou esquerda >Aparecerá uma mensagem de confirmação para exclusão> Pressione ”Confirmar”.  OBS: todos as informações contidas na disciplina será excluída.\n"
        ));
        ajudaList.add(new Ajuda(
                "Lembrete",
                "Incluir > Clique no botão localizado no canto superior esquerdo>Seleciona a opção “ Lembretes”>  Em seguida clique no ícone “+” localizado no canto superior direito da tela >Preencha os dados solicitados > Em seguida clique no ícone localizado no canto superior direito da tela.\n" +
                        "\n" +
                        "Excluir >Deslize o lembrete que deseja excluir  para direita ou esquerda >Aparecerá uma mensagem de confirmação para exclusão> Pressione ”Confirmar”. \n" +
                        "\n" +
                        "Alterar > Clique no lembrete que deseja alterar >Realize as alterações> Faça a alteração desejada > Em seguida clique no ícone localizado no canto superior direito da tela.\n"
        ));
        ajudaList.add(new Ajuda(
                "Perfil",
                "Alterar >  Clique no botão localizado no canto superior esquerdo>Seleciona a opção “ Meu Perfil”> Preencha os dados solicitados e clique em “Atualizar dados”.\n"
        ));
        ajudaList.add(new Ajuda(
                "Nota",
                "Incluir > Dentro da matéria desejada clique sobre o botão “NOTA” >Em seguida clique sobre o ícone localizado no lado inferior direito “+” > Preencha os dados solicitados > Clique em “Salvar”.\n" +
                        "ou\n" +
                        "Dentro da matéria que deseja incluir a nota clique no ícone “+” localizado no canto inferior direito da tela > Em seguida clique na plavra “nota”>Preencha os dados solicitados > Clique em “Salvar”.\n" +
                        "\n" +
                        "Excluir > Deslize o nota que deseja excluir  para direita ou esquerda > Aparecerá uma mensagem de confirmação para exclusão> Pressione ”Confirmar”\n"
        ));
        ajudaList.add(new Ajuda(
                "Falta",
                "Incluir > Dentro da matéria desejada clique sobre o botão “FALTA” >Em seguida clique sobre o ícone localizado no lado inferior direito “+” > Preencha os dados solicitados > Clique em “Salvar”.\n" +
                        "ou\n" +
                        "Dentro da matéria que deseja incluir a falta clique no ícone “+” localizado no canto inferior direito da tela > Em seguida clique na palavra “falta”>Preencha os dados solicitados > Clique em “Salvar”.\n" +
                        "\n" +
                        "Excluir > Deslize o falta que deseja excluir  para direita ou esquerda > Aparecerá uma mensagem de confirmação para exclusão> Pressione ”Confirmar”.\n"
        ));
        ajudaList.add(new Ajuda(
                "Resumo",
                "O resumo pode ser por texto ou foto. Dentro da disciplina selecionada clique no “+” e em seguida escolha o desejado.\n" +
                        "\n"+
                        "Caso seja foto, tire a foto e salve com um nome. No caso de texto, digite o titulo, o conteudo e salve.\n"+
                        "As fotos podem demorar um pouco para aparecer. Tenha paciência."


        ));

    }
}
