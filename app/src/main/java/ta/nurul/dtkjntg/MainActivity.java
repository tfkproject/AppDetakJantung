package ta.nurul.dtkjntg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    List<ItemBpm> items;
    ArrayList<Entry> entries;
    AdapterBPM adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private ProgressDialog pDialog;

    TextView txtTanggal;
    TextView txtNama;
    TextView txtBMP;
    TextView txtKondisi;
    TextView txtSelesai;

    CountDownTimer timer;
    long milliLeft;
    String bmp, nama_pasien;

    LineChart chart;

    private static String url_bpm = Config.HOST+"data_bpm.php";
    private static String url_pasien_tambah = Config.HOST+"input_data_pasien.php";
    private static String url_grap = Config.HOST+"untuk_graph.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        nama_pasien = getIntent().getStringExtra("key_nama");
        //tanggal
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        txtTanggal = (TextView) findViewById(R.id.txt_tgl);
        txtTanggal.setText(formattedDate);

        txtNama = (TextView) findViewById(R.id.txt_nama);
        txtNama.setText(nama_pasien);

        txtBMP = (TextView) findViewById(R.id.txt_heart_rate);
        txtKondisi = (TextView) findViewById(R.id.txt_kondisi);

        txtSelesai = (TextView) findViewById(R.id.lbl_selesai);

        /*//panggil RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //set LayoutManager
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        items = new ArrayList<>();*/


        tugas();

        /*//set adapter
        adapter = new AdapterBPM(getApplicationContext(), items);
        recyclerView.setAdapter(adapter);*/

        timerStart(60 * 1000); //setelah satu menit

        chart = (LineChart) findViewById(R.id.chart);

        ///
        entries = new ArrayList<>();



    }

    public class dataBMP extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        protected String doInBackground(Void... params) {

            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(url_bpm, dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        JSONArray products = ob.getJSONArray("field");

                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            bmp = c.getString("heart_rate");

                            //entries.add(new Entry(i, Float.valueOf(bmp)));


                        }
                    } else {
                        // no data found
                        psn = ob.getString("message");
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }

            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //adapter.notifyDataSetChanged();
            //pDialog.dismiss();

            txtBMP.setText(bmp);

        }

    }

    public class postData extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, nama, bpm;

        public postData(String nama, String bpm){
            this.nama = nama;
            this.bpm = bpm;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();
                detail.put("nama", nama);
                detail.put("bpm", bpm);

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(url_pasien_tambah, dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        psn = ob.getString("pesan");


                    } else {
                        // no data found
                        psn = ob.getString("pesan");
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }

            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //adapter.notifyDataSetChanged();
            pDialog.dismiss();
        }

    }

    public class grafik extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn, id_pasien;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        protected String doInBackground(Void... params) {

            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(url_grap, dataToSend);

                    //dapatkan respon
                    Log.e("Respon grap", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        JSONArray products = ob.getJSONArray("field");

                        entries.clear();
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            String id_heart_rate = c.getString("id_heart_rate");
                            String heart_rate = c.getString("heart_rate");
                            String time = c.getString("time");
                            entries.add(new Entry(i, Float.valueOf(heart_rate)));
                        }
                    } else {
                        // no data found
                        psn = ob.getString("message");
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }

            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //...
            LineDataSet dataSet = new LineDataSet(entries, "Perubahan bpm");
            dataSet.setColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
            dataSet.setValueTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark));

            //****
            // Controlling X axis
            XAxis xAxis = chart.getXAxis();
            // Set the xAxis position to bottom. Default is top
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //Customizing x axis value
            final String[] rentang_detik = new String[]{"dtk ke 0", "dtk ke 10", "dtk ke 20", "dtk ke 30", "dtk ke 40", "dtk ke 50", "dtk ke 60"};

            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return rentang_detik[(int) value];
                }
            };
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);

            //***
            // Controlling right side of y axis
            YAxis yAxisRight = chart.getAxisRight();
            yAxisRight.setEnabled(false);

            //***
            // Controlling left side of y axis
            YAxis yAxisLeft = chart.getAxisLeft();
            yAxisLeft.setGranularity(1f);

            // Setting Data
            LineData data = new LineData(dataSet);
            chart.setData(data);
        /*chart.animateX(2500);*/
            //refresh
            chart.invalidate();
            ///
        }

    }

    private static String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_menu = item.getItemId();
        if(id_menu == R.id.action_refresh){
            tugas();
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void tugas(){
        //items.clear();
        //new ambilData().execute();
        //new grafik
        new grafik().execute();
        new dataBMP().execute();
    }

    private void timerStart(long millisUntilFinished){
        timer = new CountDownTimer(millisUntilFinished, 1000) { // adjust the milli seconds here //90000

            public void onTick(long millisUntilFinished) {
                milliLeft = millisUntilFinished;
                tugas();
                if(bmp != null){
                    if(Integer.valueOf(bmp) >= 100){
                        txtKondisi.setText("Sangat baik");
                    }
                    else if(Integer.valueOf(bmp) >= 60 && Integer.valueOf(bmp) < 100){
                        txtKondisi.setText("Baik");
                    }
                    else if(Integer.valueOf(bmp) < 60){
                        txtKondisi.setText("Buruk");
                    }
                }
                else{
                    txtKondisi.setText("Tidak ada data");
                }

                txtSelesai.setText("Hasil akan keluar setelah "+String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))+" detik lagi");
            }

            public void onFinish() {
                new postData(nama_pasien, bmp).execute();

                Intent intent = new Intent(MainActivity.this, HasilActivity.class);
                intent.putExtra("key_bmp", bmp);
                intent.putExtra("key_nama", nama_pasien);
                startActivity(intent);
            }
        };
        timer.start();
    }
}
