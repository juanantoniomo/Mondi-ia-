package com.mondi.ai;


import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class MainActivity extends Activity {
    private EditText endpoint, model, apiKey, prompt;
    private TextView transcript;
    private final JSONArray history = new JSONArray();
    private final int ink = Color.rgb(235, 240, 250);
    private final int panel = Color.rgb(18, 27, 45);


    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(10, 16, 29));
        getWindow().setNavigationBarColor(Color.rgb(10, 16, 29));
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(16, 14, 16, 10); root.setBackgroundColor(Color.rgb(10, 16, 29)); setContentView(root);
        TextView title = new TextView(this); title.setText("MONDI AI"); title.setTextColor(ink); title.setTextSize(26); root.addView(title);
        TextView subtitle = new TextView(this); subtitle.setText("Asistente conversacional independiente"); subtitle.setTextColor(0xffa9bad5); root.addView(subtitle);
        endpoint = field(root, "URL HTTPS de API compatible (termina en /chat/completions)", "https://api.openai.com/v1/chat/completions");
        model = field(root, "Modelo (el que admita tu proveedor)", "");
        apiKey = field(root, "Clave API", ""); apiKey.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Button save = button("Guardar URL y modelo", root); save.setOnClickListener(v -> {
            getPreferences(0).edit().putString("endpoint", endpoint.getText().toString()).putString("model", model.getText().toString()).apply();
            toast("URL y modelo guardados. La clave no se guarda.");
        });
        endpoint.setText(getPreferences(0).getString("endpoint", endpoint.getText().toString()));
        model.setText(getPreferences(0).getString("model", ""));
        ScrollView scroll = new ScrollView(this); transcript = new TextView(this); transcript.setTextColor(ink); transcript.setTextSize(16);
        transcript.setPadding(8, 14, 8, 14); transcript.setText("MONDI: Hola. Configura el modelo y la clave de un proveedor compatible para empezar.\n\n");
        scroll.addView(transcript); root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        prompt = field(root, "Escribe tu mensaje...", "");
        Button send = button("Enviar", root); send.setOnClickListener(v -> sendMessage());
        TextView note = new TextView(this); note.setText("La conversación usa el endpoint que configures. Requiere conexión y puede generar costes en el proveedor."); note.setTextColor(0xffa9bad5); note.setTextSize(12); root.addView(note);
    }


    private EditText field(LinearLayout root, String hint, String value) {
        EditText e = new EditText(this); e.setSingleLine(true); e.setHint(hint); e.setText(value); e.setTextColor(ink); e.setHintTextColor(0xff8494ad);
        e.setPadding(10, 6, 10, 6); rootBackground(e); root.addView(e); return e;
    }
    private void rootBackground(EditText e) { e.setBackgroundColor(panel); }
    private Button button(String text, LinearLayout root) { Button b = new Button(this); b.setText(text); root.addView(b); return b; }
    private void toast(String text) { Toast.makeText(this, text, Toast.LENGTH_LONG).show(); }
    private void append(String text) { runOnUiThread(() -> transcript.append(text + "\n\n")); }


    private void sendMessage() {
        final String question = prompt.getText().toString().trim();
        final String url = endpoint.getText().toString().trim();
        final String modelName = model.getText().toString().trim();
        final String key = apiKey.getText().toString().trim();
        if (question.isEmpty()) return;
        if (!url.startsWith("https://") || !url.endsWith("/chat/completions")) { toast("Introduce una URL HTTPS compatible que termine en /chat/completions."); return; }
        if (modelName.isEmpty() || key.isEmpty()) { toast("Configura el modelo y la clave API de tu proveedor."); return; }
        prompt.setText(""); append("TÚ: " + question);
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)new URL(url).openConnection(); connection.setRequestMethod("POST");
                connection.setConnectTimeout(20000); connection.setReadTimeout(60000); connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setRequestProperty("Authorization", "Bearer " + key);
                JSONArray messages = new JSONArray();
                messages.put(new JSONObject().put("role", "system").put("content", "Eres MONDI AI, un asistente útil y claro. Responde en español salvo que te pidan otro idioma."));
                synchronized (history) { for (int i = Math.max(0, history.length() - 16); i < history.length(); i++) messages.put(history.getJSONObject(i)); }
                messages.put(new JSONObject().put("role", "user").put("content", question));
                JSONObject body = new JSONObject().put("model", modelName).put("messages", messages);
                try (OutputStream out = connection.getOutputStream()) { out.write(body.toString().getBytes(StandardCharsets.UTF_8)); }
                int status = connection.getResponseCode(); InputStream stream = status < 400 ? connection.getInputStream() : connection.getErrorStream();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                if (stream != null) { try (InputStream in = stream) { byte[] buffer = new byte[4096]; int n; while ((n = in.read(buffer)) != -1 && bytes.size() < 1000000) bytes.write(buffer, 0, n); } }
                String raw = bytes.toString("UTF-8");
                if (status >= 400) throw new Exception("El proveedor respondió HTTP " + status + ". Revisa endpoint, modelo y clave.");
                String answer = new JSONObject(raw).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                synchronized (history) { history.put(new JSONObject().put("role", "user").put("content", question)); history.put(new JSONObject().put("role", "assistant").put("content", answer)); }
                append("MONDI: " + answer);
            } catch (Exception ex) { append("No se pudo conectar: " + ex.getMessage()); }
            finally { if (connection != null) connection.disconnect(); }
        }).start();
    }
}

