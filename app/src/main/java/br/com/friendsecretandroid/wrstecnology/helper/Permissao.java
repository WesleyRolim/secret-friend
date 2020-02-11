package br.com.whatsappandroid.cursoandroid.whatsapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean valirdarPermisao (int requestCode, Activity activity, String [] permissoes){
        if (Build.VERSION.SDK_INT >= 23){

            List<String> listaPermissao = new ArrayList<String>();

            for (String permissao : permissoes){
                boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!validaPermissao){
                    listaPermissao.add(permissao);
                }
            }

            if (listaPermissao.isEmpty()){
                return true;
            }

            String[] novasPermissoes = new String[listaPermissao.size()];
            listaPermissao.toArray(novasPermissoes);

            // Solicitar permissão
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }

        return true;
    }

}
