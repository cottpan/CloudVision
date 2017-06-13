package com.google.sample.cloudvision;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpGetTask extends AsyncTask<Void,Void,String>{
    String baseUrl = "https://www.googleapis.com/language/translate/v2?key=AIzaSyD_aAUV8ejwHKrgic1I0QAHvqqN35gEAeg";
    String srcLang = "&source=en";
    String targetLang = "&target=ja";
    String transChar = "&q=";
    String transText;
    String Url;

    public HttpGetTask(String transText){
        String encodedString = null;
        try {
            encodedString = URLEncoder.encode(transText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.transText = encodedString;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(Void...arg0) {
        return exec_get();
    }

    @Override
    protected void onPostExecute(String string){
    }

    private String exec_get(){
        HttpURLConnection http = null;
        InputStream in = null;
        Url = baseUrl + srcLang + targetLang + transChar + transText;
        String src = "";
        try{
            URL url = new URL(Url);
            http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.connect();

            in = http.getInputStream();

            byte[] line = new byte[2048];
            int size;
            while (true){
                size = in.read(line);
                if (size <= 0){
                    break;
                }
                src += new String(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(http != null){
                    http.disconnect();
                }
                if(in != null) {
                    in.close();
                }
            }catch (Exception ignored){

            }
        }
        String ret = "";
        try {
            JSONObject rootObj = new JSONObject(src);
            JSONObject value = rootObj.getJSONObject("data");
            JSONArray transArray = value.getJSONArray("translations");
            for (int i=0; i < transArray.length(); i++) {
                JSONObject obj = transArray.getJSONObject(i);
                ret = obj.getString("translatedText");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
