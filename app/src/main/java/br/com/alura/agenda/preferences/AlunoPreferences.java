package br.com.alura.agenda.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Glauber on 31/08/2017.
 */

public class AlunoPreferences {

    private static final String ALUNO_PREFERENCES = "br.com.alura.agenda.preferences.AlunoPreferences";
    private static final String VERSAO_DO_DADO = "versao_do_dado";
    private final Context context;

    public AlunoPreferences(Context context){
        this.context = context;
    }

    public void salvarVersao(String versao) {
        SharedPreferences preferences = getSharedPreferencs();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(VERSAO_DO_DADO, versao);
        editor.commit();
    }

    private SharedPreferences getSharedPreferencs() {
        return context.getSharedPreferences(ALUNO_PREFERENCES, context.MODE_PRIVATE);
    }

    public String getVersao(){
        SharedPreferences versaoSalva = getSharedPreferencs();
        return versaoSalva.getString(VERSAO_DO_DADO, "");
    }

    public boolean temVersao() {
        return !getVersao().isEmpty();
    }
}