package com.example.arop;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private ImageButton actionButton;
    private ImageButton refreshButton;
    private final String correctPassword = "PHBP_2024"; // Altere para sua senha
    private final String PREFERENCES_NAME = "com.example.arop.PREFERENCES";
    private final String KEY_URL = "currentUrl";
    private String currentUrl;
    private final String defaultUrl = "https://forms.office.com/Pages/ResponsePage.aspx?id=DQSIkWdsW0yxEjajBLZtrQAAAAAAAAAAAAN__4EFobVUMlc0MDFTNTNPQ1E3TVhKV1lRRkU2RkhQOC4u";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        actionButton = findViewById(R.id.actionButton);
        refreshButton = findViewById(R.id.refreshButton);

        // Carregar a URL salva do SharedPreferences ou usar uma URL padrão
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        currentUrl = preferences.getString(KEY_URL, defaultUrl);

        if (currentUrl.equals(defaultUrl)) {
            showToastForThreeSeconds("O URL do Forms precisa de ser atualizado");
        }

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);

        // Carregar a URL atual
        webView.loadUrl(currentUrl);

        // Configurar o ImageButton para abrir o diálogo de senha
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordDialog();
            }
        });

        // Configurar o botão de atualização
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.reload(); // Recarregar a página atual
            }
        });
    }

    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        final EditText passwordInput = new EditText(this);
        passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(passwordInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredPassword = passwordInput.getText().toString();
                if (enteredPassword.equals(correctPassword)) {
                    showUrlDialog();
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showUrlDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter New URL");

        final EditText urlInput = new EditText(this);
        urlInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_URI);
        builder.setView(urlInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUrl = urlInput.getText().toString();
                if (!newUrl.isEmpty()) {
                    currentUrl = newUrl;
                    // Salvar o novo URL no SharedPreferences
                    SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(KEY_URL, currentUrl);
                    editor.apply();

                    // Atualizar o WebView com o novo URL
                    webView.loadUrl(currentUrl);
                } else {
                    Toast.makeText(MainActivity.this, "URL cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showToastForThreeSeconds(final String message) {
        final Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);

        // Show the Toast
        toast.show();

        // Use a Handler to cancel the Toast after 3 seconds (3000 milliseconds)
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 3000);
    }
}
