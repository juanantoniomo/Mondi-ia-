package com.mondi.ai;
import android.app.*;import android.os.*;import android.content.*;import android.speech.*;import android.speech.tts.*;import android.provider.ContactsContract;import android.database.Cursor;import android.net.Uri;import android.graphics.Color;import android.view.*;import android.widget.*;import org.json.*;import java.net.*;import java.io.*;import java.util.*;
public class MainActivity extends Activity {
 LinearLayout root; EditText input,endpoint,model,key; TextView chat; TextToSpeech tts; boolean ttsReady=false,voiceSend=false; String lastAnswer=""; JSONArray conversation=new JSONArray();
 public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(Color.rgb(13,19,34));getWindow().setNavigationBarColor(Color.rgb(13,19,34));ScrollView page=new ScrollView(this);page.setFillViewport(true);root=new LinearLayout(this);root.setOrientation(1);root.setPadding(18,20,18,12);root.setBackgroundColor(Color.rgb(13,19,34));page.addView(root);page.setOnApplyWindowInsetsListener((v,insets)->{v.setPadding(insets.getSystemWindowInsetLeft(),insets.getSystemWindowInsetTop(),insets.getSystemWindowInsetRight(),insets.getSystemWindowInsetBottom());return insets;});setContentView(page);
 TextView title=new TextView(this);title.setText("MONDI AI 2.9");title.setTextSize(26);title.setTextColor(Color.WHITE);root.addView(title);
 TextView subtitle=new TextView(this);subtitle.setText("Asistente independiente · Configura tu motor de IA");subtitle.setTextColor(0xffaabbdd);root.addView(subtitle);
 String geminiUrl="https://generativelanguage.googleapis.com/v1beta";String savedUrl=getPreferences(0).getString("url","");String savedModel=getPreferences(0).getString("model","");if(savedUrl.isEmpty()||savedUrl.contains("api.openai.com")){savedUrl=geminiUrl;savedModel="gemini-3.8-flash";getPreferences(0).edit().putString("url",savedUrl).putString("model",savedModel).apply();}endpoint=field("URL base Gemini API",savedUrl);model=field("Modelo Gemini gratuito",savedModel.isEmpty()?"gemini-3.8-flash":savedModel);key=field("Clave API gratuita de Google AI Studio (no se guarda)","");key.setInputType(android.text.InputType.TYPE_CLASS_TEXT|android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);button("🔑 Crear clave gratis de Gemini",()->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://aistudio.google.com/app/apikey"))));
 Button save=button("Guardar configuración Gemini",()->{getPreferences(0).edit().putString("url",endpoint.getText().toString().trim()).putString("model",model.getText().toString().trim()).apply();toast("Configuración Gemini guardada. La clave solo queda en memoria hasta cerrar la app.");});
 ScrollView scroll=new ScrollView(this);chat=new TextView(this);chat.setText("MONDI: Hola. Puedo conversar con un proveedor de IA configurado. También tienes voz, herramientas Android, editor y guía de Linux.\n\n");chat.setTextColor(Color.WHITE);chat.setTextSize(16);chat.setPadding(5,12,5,12);scroll.addView(chat);root.addView(scroll,new LinearLayout.LayoutParams(-1,dp(180)));
 input=field("Escribe tu mensaje...","");button("Enviar a IA",()->send());LinearLayout actions=new LinearLayout(this);root.addView(actions);small(actions,"🎙 Dictar y enviar",()->{voiceSend=true;voice();});small(actions,"🔊 Repetir voz",()->{if(ttsReady&&!lastAnswer.isEmpty())tts.speak(lastAnswer,TextToSpeech.QUEUE_FLUSH,null,"mondi-repeat");else toast("Todavía no hay una respuesta para leer.");});small(actions,"🌐 Buscar",()->{String q=input.getText().toString().trim();if(!q.isEmpty())startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/search?q="+Uri.encode(q))));else toast("Escribe primero lo que quieres buscar.");});small(actions,"📞 Llamar",()->{String number=input.getText().toString().trim();if(!number.isEmpty())startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+Uri.encode(number))));else toast("Escribe un número primero.");});small(actions,"👤 Contactos",()->contacts());button("⌨ Terminal Android",()->terminal());button("🐧 Guía Linux y Ubuntu",()->linuxSetup());button("🐧 Abrir Termux",()->openTermux());button("🧰 Comandos y programación",()->programmingToolkit());button("📝 Editor de código y archivos",()->codeEditor());button("⬆ Buscar actualizaciones",()->checkUpdates());tts=new TextToSpeech(this,s->{ttsReady=s==TextToSpeech.SUCCESS;if(ttsReady)tts.setLanguage(new Locale("es","ES"));});
 }
 int dp(int value){return (int)(value*getResources().getDisplayMetrics().density+0.5f);}
 EditText field(String hint,String value){EditText e=new EditText(this);e.setSingleLine(true);e.setText(value);e.setHint(hint);e.setTextColor(Color.WHITE);e.setHintTextColor(0xff99aacc);root.addView(e);return e;}
 Button button(String s,Runnable r){Button b=new Button(this);b.setText(s);root.addView(b);b.setOnClickListener(v->r.run());return b;}
 void small(LinearLayout l,String s,Runnable r){Button b=new Button(this);b.setText(s);l.addView(b,new LinearLayout.LayoutParams(0,-2,1));b.setOnClickListener(v->r.run());}
 void toast(String s){Toast.makeText(this,s,Toast.LENGTH_LONG).show();}
 void append(String s){runOnUiThread(()->chat.append(s+"\n\n"));}
 void send(){
  String msg=input.getText().toString().trim();if(msg.isEmpty())return;input.setText("");append("TÚ: "+msg);
  String base=endpoint.getText().toString().trim(),m=model.getText().toString().trim(),rawKey=key.getText().toString().trim(),k=rawKey;
  if(rawKey.toLowerCase(Locale.ROOT).contains("curl")||rawKey.toLowerCase(Locale.ROOT).contains("x-goog-api-key")){
   String lower=rawKey.toLowerCase(Locale.ROOT);int hp=lower.indexOf("x-goog-api-key:");
   if(hp>=0){int ks=hp+"x-goog-api-key:".length();while(ks<rawKey.length()&&(rawKey.charAt(ks)==' '||rawKey.charAt(ks)=='\t'||rawKey.charAt(ks)=='\''||rawKey.charAt(ks)=='"'))ks++;int ke=ks;while(ke<rawKey.length()&&!Character.isWhitespace(rawKey.charAt(ke))&&rawKey.charAt(ke)!='\''&&rawKey.charAt(ke)!='"')ke++;k=rawKey.substring(ks,ke);}
   String marker="https://generativelanguage.googleapis.com/";int up=rawKey.indexOf(marker);
   if(up>=0){int us=up+marker.length(),ue=us;while(ue<rawKey.length()&&!Character.isWhitespace(rawKey.charAt(ue))&&rawKey.charAt(ue)!='\''&&rawKey.charAt(ue)!='"')ue++;String path=rawKey.substring(us,ue);String modelMark="/models/";int mp=path.indexOf(modelMark);if(mp>=0){String modelPath=path.substring(mp+modelMark.length());int me=modelPath.indexOf(':');if(me>=0)modelPath=modelPath.substring(0,me);if(!modelPath.isEmpty())m=modelPath;}base="https://generativelanguage.googleapis.com/v1beta";endpoint.setText(base);model.setText(m);getPreferences(0).edit().putString("url",base).putString("model",m).apply();}
   if(k.equals(rawKey)){append("MONDI: No pude extraer la clave del comando. Pega solo la clave API de Google AI Studio, no todo el comando curl.");return;}
   key.setText(k);toast("Comando curl detectado: he separado clave, URL y modelo.");
  }
  if(k.isEmpty()){append("MONDI: Pulsa «Crear clave gratis de Gemini» y crea una clave en Google AI Studio. Pégala aquí; no la envíes por chat.");return;}
  if(m.isEmpty()){append("MONDI: Falta el modelo Gemini.");return;}
  if(!base.startsWith("https://generativelanguage.googleapis.com/")){append("MONDI: La URL debe ser la URL base de Gemini: https://generativelanguage.googleapis.com/v1beta");return;}
  String url=base.replaceAll("/+$","")+"/models/"+m+":generateContent";
  new Thread(()->{HttpURLConnection c=null;try{
   c=(HttpURLConnection)new URL(url).openConnection();c.setRequestMethod("POST");c.setConnectTimeout(20000);c.setReadTimeout(60000);c.setDoOutput(true);
   c.setRequestProperty("Content-Type","application/json; charset=utf-8");c.setRequestProperty("x-goog-api-key",k);
   JSONObject req=new JSONObject();JSONArray contents=new JSONArray();JSONArray systemParts=new JSONArray();systemParts.put(new JSONObject().put("text","Eres MONDI AI, asistente personal en español. Sé útil, claro y seguro."));req.put("systemInstruction",new JSONObject().put("parts",systemParts));
   synchronized(conversation){for(int j=Math.max(0,conversation.length()-16);j<conversation.length();j++){JSONObject old=conversation.getJSONObject(j);String role="assistant".equals(old.optString("role"))?"model":"user";JSONArray parts=new JSONArray();parts.put(new JSONObject().put("text",old.optString("content","")));contents.put(new JSONObject().put("role",role).put("parts",parts));}}
   JSONArray userParts=new JSONArray();userParts.put(new JSONObject().put("text",msg));contents.put(new JSONObject().put("role","user").put("parts",userParts));req.put("contents",contents);
   try(OutputStream o=c.getOutputStream()){o.write(req.toString().getBytes("UTF-8"));}
   int status=c.getResponseCode();InputStream stream=status<400?c.getInputStream():c.getErrorStream();ByteArrayOutputStream buf=new ByteArrayOutputStream();if(stream!=null){try(InputStream in=stream){byte[] chunk=new byte[4096];int n;while((n=in.read(chunk))!=-1&&buf.size()<1000000)buf.write(chunk,0,n);}}
   String response=buf.toString("UTF-8");if(status>=400){String detail=response,code="";try{JSONObject err=new JSONObject(response).optJSONObject("error");if(err!=null){detail=err.optString("message",response);code=err.optString("status",err.optString("code",""));}}catch(Exception ignored){}throw new Exception("Gemini HTTP "+status+(code.isEmpty()?"":" ("+code+")")+": "+detail);}
   JSONArray candidates=new JSONObject(response).getJSONArray("candidates");JSONArray parts=candidates.getJSONObject(0).getJSONObject("content").getJSONArray("parts");StringBuilder answerBuilder=new StringBuilder();for(int i=0;i<parts.length();i++){String t=parts.getJSONObject(i).optString("text","");if(!t.isEmpty()){if(answerBuilder.length()>0)answerBuilder.append("\n");answerBuilder.append(t);}}String answer=answerBuilder.toString().trim();if(answer.isEmpty())throw new Exception("Gemini no devolvió texto. Revisa el límite gratuito del modelo.");
   synchronized(conversation){conversation.put(new JSONObject().put("role","user").put("content",msg));conversation.put(new JSONObject().put("role","assistant").put("content",answer));}lastAnswer=answer;append("MONDI: "+answer);if(tts!=null)runOnUiThread(()->{if(ttsReady)tts.speak(answer,TextToSpeech.QUEUE_FLUSH,null,"mondi");else toast("Activa o instala una voz española en los ajustes de Android.");});
  }catch(Exception ex){append("Error de Gemini: "+ex.getMessage());}finally{if(c!=null)c.disconnect();}}).start();
 }
 void checkUpdates(){
  append("MONDI: Buscando la última versión publicada en GitHub…");
  new Thread(()->{HttpURLConnection c=null;try{
   URL u=new URL("https://api.github.com/repos/juanantoniomo/Mondi-ia-/releases?per_page=1");
   c=(HttpURLConnection)u.openConnection();c.setConnectTimeout(12000);c.setReadTimeout(12000);c.setRequestProperty("Accept","application/vnd.github+json");
   int status=c.getResponseCode();if(status==404){append("MONDI: Todavía no hay versiones publicadas. La APK se podrá actualizar cuando se publique una versión en Releases.");return;}
   if(status!=200)throw new IOException("GitHub HTTP "+status);
   ByteArrayOutputStream out=new ByteArrayOutputStream();try(InputStream in=c.getInputStream()){byte[] b=new byte[4096];int n;while((n=in.read(b))!=-1&&out.size()<100000)out.write(b,0,n);}
   JSONArray releases=new JSONArray(out.toString("UTF-8"));if(releases.length()==0){append("MONDI: Todavía no hay versiones publicadas.");return;}JSONObject release=releases.getJSONObject(0);String tag=release.optString("tag_name","desconocida");
   JSONArray assets=release.optJSONArray("assets");String download="";if(assets!=null)for(int i=0;i<assets.length();i++){JSONObject asset=assets.getJSONObject(i);if("app-release.apk".equals(asset.optString("name"))){download=asset.optString("browser_download_url","");break;}}
   if(!download.startsWith("https://github.com/juanantoniomo/Mondi-ia-/releases/download/")){append("MONDI: La última publicación todavía no contiene una APK de actualización firmada.");return;}
   String installed=getPackageManager().getPackageInfo(getPackageName(),0).versionName;
   String apkUrl=download;
   runOnUiThread(()->new AlertDialog.Builder(this).setTitle("Actualización MONDI AI").setMessage("Versión instalada: "+installed+"\nÚltima publicada: "+tag+"\n\nDescargarás la APK de publicación. Si conserva la misma firma, Android la instalará encima de MONDI y mantendrá los datos; solo tendrás que confirmar la instalación.").setPositiveButton("Descargar APK",(d,w)->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(apkUrl)))).setNegativeButton("Cerrar",null).show());
  }catch(Exception e){append("MONDI: No se pudo comprobar la actualización: "+e.getMessage());}finally{if(c!=null)c.disconnect();}}).start();
 }
 void terminal(){final EditText command=new EditText(this);command.setHint("Comando Android: pwd, ls, id...");final TextView output=new TextView(this);output.setTextIsSelectable(true);output.setText("Terminal Android local. No es Ubuntu ni tiene root. Los comandos se ejecutan con los permisos de MONDI AI.");ScrollView sv=new ScrollView(this);sv.addView(output);LinearLayout layout=new LinearLayout(this);layout.setOrientation(1);layout.setPadding(20,12,20,12);layout.addView(command);layout.addView(sv,new LinearLayout.LayoutParams(-1,400));new AlertDialog.Builder(this).setTitle("Terminal local").setView(layout).setPositiveButton("Ejecutar",(dialog,which)->{String cmd=command.getText().toString().trim();if(cmd.isEmpty())return;new Thread(()->{try{java.lang.Process process=new ProcessBuilder("/system/bin/sh","-c",cmd).redirectErrorStream(true).start();ByteArrayOutputStream out=new ByteArrayOutputStream();InputStream in=process.getInputStream();byte[] b=new byte[2048];int n,total=0;while((n=in.read(b))!=-1&&total<30000){int count=Math.min(n,30000-total);out.write(b,0,count);total+=count;}boolean finished=process.waitFor(15,java.util.concurrent.TimeUnit.SECONDS);if(!finished)process.destroyForcibly();int status=finished?process.exitValue():-1;String result=out.toString("UTF-8")+"\nCódigo de salida: "+status;runOnUiThread(()->new AlertDialog.Builder(this).setTitle("Resultado del comando").setMessage(result).setPositiveButton("Cerrar",null).show());}catch(Exception e){runOnUiThread(()->toast(e.toString()));}}).start();}).setNegativeButton("Cancelar",null).show();}


 void codeEditor(){
 final File workspace=new File(getFilesDir(),"workspace");workspace.mkdirs();
 LinearLayout layout=new LinearLayout(this);layout.setOrientation(1);layout.setPadding(16,12,16,12);
 EditText filename=new EditText(this);filename.setSingleLine(true);filename.setHint("archivo.sh");filename.setText("hola.sh");layout.addView(filename);
 EditText source=new EditText(this);source.setGravity(Gravity.TOP);source.setMinLines(10);source.setText("#!/system/bin/sh\necho Hola desde MONDI AI\npwd\n");layout.addView(source,new LinearLayout.LayoutParams(-1,0,1));
 TextView info=new TextView(this);info.setText("Editor local: guarda archivos en el espacio privado de MONDI. Ejecutar solo admite scripts .sh con /system/bin/sh. No incluye Python, Node ni Ubuntu.");layout.addView(info);
 final AlertDialog dialog=new AlertDialog.Builder(this).setTitle("Editor MONDI").setView(layout).setPositiveButton("Cerrar",null).create();
 LinearLayout controls=new LinearLayout(this);
 Button load=new Button(this);load.setText("Abrir");controls.addView(load,new LinearLayout.LayoutParams(0,-2,1));
 Button save=new Button(this);save.setText("Guardar");controls.addView(save,new LinearLayout.LayoutParams(0,-2,1));
 Button run=new Button(this);run.setText("Ejecutar .sh");controls.addView(run,new LinearLayout.LayoutParams(0,-2,1));
 layout.addView(controls);
 java.util.function.Supplier<File> selected=()->{
 String name=filename.getText().toString().trim();
 if(!name.matches("[A-Za-z0-9_.-]{1,80}")||name.startsWith(".")){toast("Nombre de archivo no válido");return null;}
 return new File(workspace,name);
 };
 load.setOnClickListener(v->{File f=selected.get();if(f==null)return;try{
 byte[] bytes=java.nio.file.Files.readAllBytes(f.toPath());if(bytes.length>100000){toast("Archivo demasiado grande");return;}
 source.setText(new String(bytes,java.nio.charset.StandardCharsets.UTF_8));
 }catch(Exception e){toast("No se pudo abrir: "+e.getMessage());}});
 save.setOnClickListener(v->{File f=selected.get();if(f==null)return;try{
 java.nio.file.Files.write(f.toPath(),source.getText().toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));toast("Guardado: "+f.getName());
 }catch(Exception e){toast("No se pudo guardar: "+e.getMessage());}});
 run.setOnClickListener(v->{File f=selected.get();if(f==null)return;if(!f.getName().endsWith(".sh")){toast("Solo scripts .sh");return;}
 new AlertDialog.Builder(this).setTitle("Confirmar ejecución").setMessage("Se ejecutará el código escrito usando el shell Android con permisos de MONDI AI. Ejecuta solo código que conozcas.")
 .setPositiveButton("Ejecutar",(d,w)->{try{java.nio.file.Files.write(f.toPath(),source.getText().toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));}catch(Exception e){toast(e.getMessage());return;}
 new Thread(()->{try{
 java.lang.Process proc=new ProcessBuilder("/system/bin/sh",f.getAbsolutePath()).directory(workspace).redirectErrorStream(true).start();
 boolean finished=proc.waitFor(15,java.util.concurrent.TimeUnit.SECONDS);if(!finished)proc.destroyForcibly();
 ByteArrayOutputStream out=new ByteArrayOutputStream();InputStream in=proc.getInputStream();byte[] buf=new byte[2048];int n,total=0;
 while((n=in.read(buf))!=-1&&total<20000){int take=Math.min(n,20000-total);out.write(buf,0,take);total+=take;}
 String result=out.toString("UTF-8")+(finished?"\nSalida: "+proc.exitValue():"\nTiempo agotado");
 runOnUiThread(()->new AlertDialog.Builder(this).setTitle("Resultado").setMessage(result).setPositiveButton("Cerrar",null).show());
 }catch(Exception e){runOnUiThread(()->toast(e.toString()));}}).start();
 }).setNegativeButton("Cancelar",null).show();
 });
 dialog.show();
 }

 void programmingToolkit(){
 String[] names={"Instalar herramientas Ubuntu","Python y pip","Git","Node.js","Compilar C","Utilidades Linux","Ejemplo Python","Ejemplo JavaScript","Ejemplo Bash","Flujo Git","Diagnóstico Android"};
 String[] scripts={"apt update && apt upgrade -y\napt install -y python3 python3-pip git curl wget nano vim build-essential","python3 --version\npython3 -m venv entorno\n. entorno/bin/activate\npython -m pip install --upgrade pip","git --version\ngit config --global user.name \"Tu nombre\"\ngit status","apt install -y nodejs npm\nnode --version\nnpm --version","apt install -y build-essential\nprintf '#include <stdio.h>\\nint main(){puts(\"Hola MONDI\");return 0;}\\n' > hola.c\ngcc hola.c -o hola\n./hola","apt install -y zip unzip jq openssh-client procps iproute2","print('Hola desde MONDI AI')\nfor i in range(3): print(i)","console.log('Hola desde MONDI AI');\nconst suma = (a,b) => a+b;\nconsole.log(suma(2,3));","#!/usr/bin/env bash\nset -e\necho 'Hola desde MONDI AI'\npwd\nls -la","git init\ngit add .\ngit commit -m \"Primer cambio\"","uname -a\nwhoami\npwd\ndf -h"};
 new AlertDialog.Builder(this).setTitle("Programación y Linux").setItems(names,(dialog,index)->{
 String content=scripts[index];
 new AlertDialog.Builder(this).setTitle(names[index]).setMessage(content)
 .setPositiveButton("Copiar",(d,w)->{
 android.content.ClipboardManager cb=(android.content.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
 cb.setPrimaryClip(ClipData.newPlainText(names[index],content));toast("Copiado");
 }).setNegativeButton("Cerrar",null).show();
 }).setNegativeButton("Cerrar",null).show();
 }
 void linuxSetup(){String guide="LINUX EN ANDROID\n\nMONDI usa Android como sistema principal. Para ejecutar herramientas GNU/Linux sin root puedes usar Termux y proot-distro.\n\n1. Instala Termux desde F-Droid.\n2. Abre Termux y ejecuta:\npkg update && pkg upgrade -y\npkg install proot-distro -y\nproot-distro install ubuntu\nproot-distro login ubuntu\n\nUbuntu corre en un contenedor de espacio de usuario; no da root real ni control ilimitado del móvil.\n\nPor seguridad, MONDI no ejecutará comandos Linux recibidos por la IA sin confirmación explícita.";new AlertDialog.Builder(this).setTitle("Linux / Ubuntu").setMessage(guide).setPositiveButton("Copiar comandos",(d,w)->{android.content.ClipboardManager cb=(android.content.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);cb.setPrimaryClip(ClipData.newPlainText("Ubuntu en Termux","pkg update && pkg upgrade -y\npkg install proot-distro -y\nproot-distro install ubuntu\nproot-distro login ubuntu"));toast("Comandos copiados");}).setNeutralButton("Abrir Termux",(d,w)->openTermux()).setNegativeButton("Cerrar",null).show();}
 void openTermux(){try{Intent i=getPackageManager().getLaunchIntentForPackage("com.termux");if(i!=null){startActivity(i);return;}}catch(Exception ignored){}new AlertDialog.Builder(this).setTitle("Linux con Termux").setMessage("Para Ubuntu o herramientas Linux completas, instala Termux desde F-Droid y configura proot-distro. MONDI AI no instala ni controla Termux automáticamente.").setPositiveButton("Abrir F-Droid",(d,w)->startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://f-droid.org/packages/com.termux/")))).setNegativeButton("Cancelar",null).show();}
 void voice(){Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"es-ES");try{startActivityForResult(i,9);}catch(Exception e){toast("Reconocimiento de voz no disponible");}}
 void contacts(){Intent i=new Intent(Intent.ACTION_PICK,ContactsContract.CommonDataKinds.Phone.CONTENT_URI);try{startActivityForResult(i,10);}catch(Exception e){toast("No se puede abrir contactos");}}
 @Override protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==9){if(c==RESULT_OK&&d!=null){ArrayList<String> a=d.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);if(a!=null&&!a.isEmpty()){input.setText(a.get(0));if(voiceSend)send();}}voiceSend=false;}else if(r==10&&c==RESULT_OK&&d!=null&&d.getData()!=null){try(Cursor cursor=getContentResolver().query(d.getData(),new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER},null,null,null)){if(cursor!=null&&cursor.moveToFirst()){String name=cursor.getString(0),number=cursor.getString(1);input.setText(number);append("Contacto seleccionado: "+name+" ("+number+")");}}catch(Exception e){toast("No se pudo leer el contacto seleccionado");}}}
 protected void onDestroy(){if(tts!=null)tts.shutdown();super.onDestroy();}
}
