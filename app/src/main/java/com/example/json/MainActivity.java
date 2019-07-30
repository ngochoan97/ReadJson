package com.example.json;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String JsonAPI = "https://demo5639557.mockable.io/getWeather";
    String json = "";
    TextView tvId, tvMain, tvDes, tvIcon, tvTemp, tvPre, tvHumi, tvTemMin, tvTempMax;
    Main main;
    ArrayList<Weather> listWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvId = findViewById(R.id.tvId);
        tvMain = findViewById(R.id.tvMain);
        tvDes = findViewById(R.id.tvDescription);
        tvIcon = findViewById(R.id.tvIcon);
        tvTemp = findViewById(R.id.tvTemp);
        tvPre = findViewById(R.id.tvPressure);
        tvHumi = findViewById(R.id.tvHumidity);
        tvTemMin = findViewById(R.id.tvMin);
        tvTempMax = findViewById(R.id.tvMax);
        new getJson().execute();

        ActionBar sc = getSupportActionBar();
        sc.hide();
    }

    public void getData() {
        try {
            URL url = new URL(JsonAPI);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            int byteChar;
            while ((byteChar = inputStream.read()) != -1) {
                json += (char) byteChar;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class getJson extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getData();
            getMain();
            getWeather();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TempC = Integer.parseInt(   Math.round(((Double.parseDouble(main.getTemp()) - 273)*10)/10)+"");
            TemMin= Integer.parseInt(   Math.round(((Double.parseDouble(main.getTemp_min()) - 273)*10)/10)+"");
            TemMax=Integer.parseInt(   Math.round(((Double.parseDouble(main.getTemp_max()) - 273)*10)/10)+"");
            tvId.setText("ID: "+listWeather.get(0).getId());
            tvMain.setText("Main: "+listWeather.get(0).getMain());
            tvDes.setText("Description: "+listWeather.get(0).getDescription());
            tvIcon.setText("Icon: "+listWeather.get(0).getIcon());
            tvTemp.setText(TempC+"°C");
            tvTempMax.setText("Max: "+TemMax+"°C");
            tvTemMin.setText("Min: "+TemMin+"°C");
            tvHumi.setText("Humidity: "+main.getHumidity());
            tvPre.setText("Pressure: "+main.getPressure());

        }
    }

    int TempC,TemMax,TemMin;

    public void getMain() {
        main = new Main();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject objectMain = object.getJSONObject("main");
            main.setTemp(objectMain.getString("temp"));
            //     Toast.makeText(this, ""+main.getTemp(), Toast.LENGTH_SHORT).show();
            main.setPressure(objectMain.getString("pressure"));
            main.setHumidity(objectMain.getString("humidity"));
            main.setTemp_min(objectMain.getString("temp_min"));
            main.setTemp_max(objectMain.getString("temp_max"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getWeather() {
        listWeather = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("weather");
            for (int i = 0; i <= jsonArray.length(); i++) {
                String id = jsonArray.getJSONObject(i).getString("id");
                String main = jsonArray.getJSONObject(i).getString("main");
                String description = jsonArray.getJSONObject(i).getString("description");
                String icon = jsonArray.getJSONObject(i).getString("icon");
                Weather weather = new Weather();
                weather.setId(id);
                weather.setMain(main);
                weather.setDescription(description);
                weather.setIcon(icon);
                listWeather.add(weather);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
