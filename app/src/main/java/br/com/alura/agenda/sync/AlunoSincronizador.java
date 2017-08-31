package br.com.alura.agenda.sync;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.dto.AlunoSync;
import br.com.alura.agenda.event.AtualizaListaAlunoEvent;
import br.com.alura.agenda.preferences.AlunoPreferences;
import br.com.alura.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlunoSincronizador {

    private final Context context;
    private EventBus eventBus = EventBus.getDefault();

    public AlunoSincronizador(Context context) {
        this.context = context;
    }

    public void buscaAlunos() {
        Call<AlunoSync> call = new RetrofitInicializador().getAlunoService().lista();

        call.enqueue(new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();

                String versao = alunoSync.getMomentoDaUltimaModificacao();
                AlunoPreferences preferences = new AlunoPreferences(context);
                preferences.salvarVersao(versao);

                AlunoDAO dao = new AlunoDAO(context);
                dao.sincroniza(alunoSync.getAlunos());
                dao.close();
                eventBus.post(new AtualizaListaAlunoEvent());

                Log.i("Versao", preferences.getVersao());
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                Log.e("onFailure chamado", t.getMessage());
                eventBus.post(new AtualizaListaAlunoEvent());
            }
        });
    }
}