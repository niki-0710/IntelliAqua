package com.example.kaustav.intelliaqua;

import android.app.AlertDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Kaustav on 21-03-2019.
 */

public class FetchData extends AsyncTask<Void, Void, Void> {
    String data="";
    String dataParsed="";
    String singleParsed="";
    int hr,min,sec;
    String time="";
    String date="";
    String parsedSingle = "";
    String parsedData = "";
    String parsedSingle2 = "";
    String parsedData2 = "";

    String parsedSinglTemp = "";
    String parsedDataTemp = "";

    String parsedSingleHumid = "";
    String parsedDataHumid = "";

    @Override
    protected Void doInBackground(Void... params) {

        try {
            URL url= new URL("http://api.thingspeak.com/channels/414819/feeds.json?api_key=QSSI85KIN4SSKEF8&results=2");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));

            String line ="";
            while(line !=null)
            {
                line=bufferedReader.readLine();
                data=data+line;

            }
            /*JSONArray JA = new JSONArray(data);

            for(int i=0;i<JA.length();i++)
            {
                JSONObject JO= (JSONObject) JA.get(i);
                singleParsed="Temperature"+JO.get("field1")+"\n"+
                             "Humidity"+JO.get("field2")+"\n";

                dataParsed=dataParsed+singleParsed;
            }*/

            JSONObject JO = (JSONObject) new JSONTokener(data).nextValue();
            JSONArray JA = (JSONArray) JO.get("feeds");
            for (int i =0;i<JA.length();i++){
                JSONObject JO1 = (JSONObject) JA.get(i);
                time=JO1.getString("created_at");
                date=time.substring(0,10);
                hr=Integer.parseInt(time.substring(11,13));
                min=Integer.parseInt(time.substring(14,16));
                sec=Integer.parseInt(time.substring(17,19));

                min = min+30;
                if(min>60){
                    min=min-60;
                    hr++;
                }
                hr=hr+5;
                /*parsedSingle = "Collected on : "+ date +" , "+hr+":"+min+":"+sec+" hrs\n"+
                        "\t\tTemperature: " + JO1.getString("field1").toString() + " °C" +
                        "\n\t\tHumidity:" + JO1.getString("field2").toString() +" g/cubic m" + "\n\n";
                parsedData = parsedData + parsedSingle;*/

                parsedSingle = "Temperature: " + JO1.getString("field1").toString() + " °C\n" ;

                parsedSingle2 = "Humidity  : \n" + JO1.getString("field2").toString() + " %\n" ;

                parsedSinglTemp=JO1.getString("field1").toString();
                parsedSingleHumid=JO1.getString("field2").toString();

                /*double t=Double.parseDouble(parsedDataTemp);
                double h=Double.parseDouble(parsedDataHumid);

                if((t>=25)||(h<=30))
                {
                    //Dispaly the alert for opinion
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(SecondActivity.this,FetchData.class);
                }*/



                //parsedData = parsedData + parsedSingle;

                parsedData =  parsedSingle;
                parsedData2 =  parsedSingle2;




            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        SecondActivity.Temp.setText(this.parsedData);
        SecondActivity.Humid.setText(this.parsedData2);

    }


}