package com.hisham.jsonparsingdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText ID,password;

    private  TextView tvData;
    //Context Mycontext = MainActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnHit = (Button) findViewById(R.id.btnHit);
        tvData = (TextView) findViewById(R.id.tvJsonItem);
        ID = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("202.78.200.117/izyapi/");
            }
        });


    }

    public class JSONTask extends AsyncTask<String,String,String>{
  //      String emailID = ID.getText().toString();
  //     String passwordID = password.getText().toString();

        @Override
        protected String doInBackground(String... params){
            BufferedReader reader = null;
            HttpURLConnection connection = null;


            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("users");


                StringBuffer finalBuffer = new StringBuffer();
                for(int i=0;i<parentArray.length();i++) {
                    JSONObject finalobject = parentArray.getJSONObject(i);
                    String databaseku = finalobject.getString("id");
                    String id = finalobject.getString("name");


//                    if (!databaseku.equals(emailID) && !id.equals(passwordID)) {
//
//                        new AlertDialog.Builder(Mycontext).setMessage("Wrong email or password. Fail login").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        }).show();


                   // if (databaseku.length() == id.length())
                    finalBuffer.append(databaseku + "\n" + id + "\n\n");
                }
                return finalBuffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            } finally {
                connection.disconnect();
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
        super.onPostExecute(result);
            tvData.setText(result);
        }
    }
}

