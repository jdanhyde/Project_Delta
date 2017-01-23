package com.example.damion.loginscreen;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewUserActivity extends AppCompatActivity {

    private TextView tvMessageLogin;
    private EditText etUser;
    private EditText etPass;
    private String usernameLogin;
    private String passwordLogin;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        Button newUser = (Button) findViewById(R.id.buttonCreate);

        newUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                etUser = (EditText) findViewById(R.id.editText);
                usernameLogin = etUser.getText().toString();
                etPass = (EditText) findViewById(R.id.editText2);
                passwordLogin = etPass.getText().toString();
<<<<<<< HEAD
                //String url = "http://10.230.35.249/Game/userAdd.php?username=" + usernameLogin + "&password=" + passwordLogin;
                String url = "http://www.redwoodmediaco.com/compsci/userAdd.php?username=" + usernameLogin + "&password=" + passwordLogin;
=======
                String url = "http://www.redwoodmediaco.com/compsci/userLogin.php?username=" + usernameLogin + "&password=" + passwordLogin;
>>>>>>> origin/master
                System.out.println("URL: " + url);
                new tryLogin().execute(url);

            }

        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("NewUser Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    public class tryLogin extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            System.out.println(params[0]);
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
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Reuslt: " + result);

            String finalJson = result.toString();
            JSONObject parentObject = null;
            try {
                parentObject = new JSONObject(finalJson);
                System.out.println(parentObject.getString("status"));
                System.out.println(parentObject.getString("message"));

                if (parentObject.getString("status").equals("false")) {
                    tvMessageLogin.setText(parentObject.getString("message"));
                } else if (parentObject.getString("status").equals("true")) {
                    //BEGIN GAME
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}