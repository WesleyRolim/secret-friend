package br.com.friendsecretandroid.wrstecnology.friendsecret;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import br.com.friendsecretandroid.wrstecnology.model.Participantes;
import br.com.whatsappandroid.cursoandroid.whatsapp.helper.Permissao;

public class MainActivity extends AppCompatActivity {

    private EditText nome;
    private EditText codArea;
    private EditText ddd;
    private EditText telefone;
    private EditText sugestao;
    private Button adicionar;
    private Button limpar;
    private Button sortear;
    private Button newArray;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS
    };
    private String todosUsuários = "";
    private String[] participantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permissao.valirdarPermisao(1, this, permissoesNecessarias);

        nome = (EditText) findViewById(R.id.nomeText);
        codArea = (EditText) findViewById(R.id.codAreaText);
        ddd = (EditText) findViewById(R.id.dddText);
        telefone = (EditText) findViewById(R.id.telefoneText);
        sugestao = (EditText) findViewById(R.id.sugestaoText);
        adicionar = (Button) findViewById(R.id.addButton);
        limpar = (Button) findViewById(R.id.limparButton);
        sortear = (Button) findViewById(R.id.jogarButton);
        newArray = (Button) findViewById(R.id.newButton);
        final ArrayList<Participantes> participantes = new ArrayList<Participantes>();

        SimpleMaskFormatter simpleMaskCodArea = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskCodArea = new MaskTextWatcher(codArea, simpleMaskCodArea);
        codArea.addTextChangedListener(maskCodArea);

        SimpleMaskFormatter simpleMaskDDD = new SimpleMaskFormatter("(NN)");
        MaskTextWatcher maskDDD = new MaskTextWatcher(ddd, simpleMaskDDD);
        ddd.addTextChangedListener(maskDDD);

        SimpleMaskFormatter simpleTelefone = new SimpleMaskFormatter("N.NNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleTelefone);
        telefone.addTextChangedListener(maskTelefone);

        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camposVazios()) {
                    String participante = nome.getText().toString();
                    String telefoneCompleto =
                            codArea.getText().toString() +
                                    ddd.getText().toString() +
                                    telefone.getText().toString();

                    String telefoneSemFormatacao = telefoneCompleto.replace("(", "");
                    telefoneSemFormatacao = telefoneSemFormatacao.replace(")", "");
                    telefoneSemFormatacao = telefoneSemFormatacao.replace(".", "");
                    telefoneSemFormatacao = telefoneSemFormatacao.replace("-", "");

                    //Salvando os dados no array;
                    Participantes dado = new Participantes(nome.getText().toString(), telefoneSemFormatacao, sugestao.getText().toString());
                    participantes.add(dado);

                    limparCampos();

                    //todosUsuários = todosUsuários + nome.getText().toString() + telefoneSemFormatacao + "@";

                    //Log.i("Telefone", "Tudo: "+todosUsuários);
                }
            }
        });

        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
            }
        });

        sortear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Integer> numero = null;
                ArrayList<String> amigos = new ArrayList<String>();
                if (participantes.size() > 2){
                    while (numero == null){
                        numero = sortearCorretamente(participantes.size());
                    }
                    Log.i("JOGO","ARRAY: "+ numero );
                    for (int i = 0 ; i < numero.size() ; i ++){
                        Log.i("AMIGO", participantes.get(i).getNome() +" seu amigo é " +
                                participantes.get(numero.get(i)).getNome() +
                                " numero dele é: "+ participantes.get(numero.get(i)).getTelefone() +
                                " sugestão de presente dele é: " + participantes.get(numero.get(i)).getSugestao());
                        boolean smsEnviado = enviarSMS(participantes.get(numero.get(i)).getTelefone(),
                                "Seu amigo secreto é: "+ participantes.get(i).getNome() +
                                        " e a sugestão de presente dele é: " + participantes.get(numero.get(i)).getSugestao());
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Cadastrar mais participantes", Toast.LENGTH_LONG).show();
                }

            }
        });

        newArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                participantes.clear();
            }
        });

    }

    private boolean enviarSMS(String telefone, String mensagem){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);

            return true;
        }catch (Exception e ){
            e.printStackTrace();
            return false;
        }

    }

    public void onRequestPermissionsResult (int requestCode, String[] permission, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permission, grantResults);

        for (int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao (){
        AlertDialog.Builder builder = new AlertDialog.Builder( this);
        builder.setTitle("Permissoes Negadas");
        builder.setMessage("Para utilizar esse aplicativo é necessário aceitar todas as permissões");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void limparCampos (){
        nome.setText("");
        codArea.setText("");
        ddd.setText("");
        telefone.setText("");
        sugestao.setText("");
    }

    private boolean camposVazios(){
        // O campo presente não é obrigatório
        if (nome.getText().toString().isEmpty() || codArea.getText().toString().isEmpty() || ddd.getText().toString().isEmpty()|| telefone.getText().toString().isEmpty()){
            Toast.makeText(MainActivity.this, "Entre com todo os campos", Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    private static ArrayList<Integer> sortear (int tamanhoVetor){
        ArrayList<Integer> numero = new ArrayList<Integer>();
        for (int i = 0 ; i < tamanhoVetor ; i++){
            numero.add(i);
        }
        Collections.shuffle(numero);
        return numero;
    }

    private static ArrayList<Integer> sortearCorretamente (int tamanhoVetor){
        ArrayList<Integer> numero;
        numero = sortear(tamanhoVetor);
        boolean validador = false;

        for (int i = 0 ; i < numero.size() ; i ++){
            if (numero.get(i) != i){
                validador = true;
            }else{
                validador = false;
                i = numero.size();
            }
        }
        if(validador){
            return numero;
        }else{
            sortearCorretamente(tamanhoVetor);
            return null;
        }

    }

}
