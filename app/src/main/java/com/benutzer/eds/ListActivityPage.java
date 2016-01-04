package com.benutzer.eds;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ListActivityPage extends ActionBarActivity {
    TextView textView;
    //String responseString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_activity_page);

        textView = (TextView) findViewById(R.id.textViewListPageId);
        new PostTask().execute();
        //
        //textView.setText(responseString);
    }

    private void throwTextView(String responseString){
        textView.setText(responseString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_activity_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PostTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... data) {
            HttpResponse response = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.168.1.4:7070/user");
            try {
                /*StringEntity se = new StringEntity("");

                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);*/
                httppost.setHeader("Content-type", "application/json");
            }catch(/*UnsupportedEncoding*/Exception e){
                e.printStackTrace();
            }/*catch (Exception e){
                e.printStackTrace();
            }*/

            try {
                //add data
                /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("eid", "100"));
                nameValuePairs.add(new BasicNameValuePair("sid", "1"));
                nameValuePairs.add(new BasicNameValuePair("lim", "10"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/
                //execute http post
                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eid", 100);
                jsonObject.accumulate("sid", 1);
                jsonObject.accumulate("lim", 10);

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                httppost.setEntity(new StringEntity(json));
                response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }


            //responseString = response.toString();
            return convertResponser(response);
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null){
                throwTextView("NULL RECEVED");
            }
            else {
                throwTextView(s);
            }
        }

        private String convertResponser(HttpResponse response){
            InputStream in =  null;
            String html = null;
            try {
                in = response.getEntity().getContent();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();

            }


            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(in));
            } catch (Exception e) {

                this.publishProgress();
                this.cancel(true);

                e.printStackTrace();
            }

            StringBuilder str = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            html = str.toString();

            return html;
        }
    }
}
