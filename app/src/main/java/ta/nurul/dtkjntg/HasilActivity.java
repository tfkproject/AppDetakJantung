package ta.nurul.dtkjntg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class HasilActivity extends AppCompatActivity {

    String nama_pasien, bmp_pasien;

    TextView txtNama, txtBMP, txtHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        getSupportActionBar().setTitle("Hasil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nama_pasien = getIntent().getStringExtra("key_nama");
        bmp_pasien = getIntent().getStringExtra("key_bmp");

        txtNama = (TextView) findViewById(R.id.txt_nama);
        txtNama.setText(nama_pasien);

        txtBMP = (TextView) findViewById(R.id.txt_bmp);
        txtBMP.setText(bmp_pasien+" BPM");

        txtHasil = (TextView) findViewById(R.id.txt_hasil);
        int hasil = Integer.valueOf(bmp_pasien);
        if(hasil >= 100){
            txtHasil.setText("Kondisi detak jantung anda sangat baik, tetap jaga kondisi kebugaran anda. Dan pertahankan kesehatan anda.");
        }
        else if(hasil >= 60 && hasil < 100){
            txtHasil.setText("Kondisi detak jantung anda baik, jaga pola makan anda, giat berolahraga dan istirahat yang cukup. Buat diri anda tetap sehat.");
        }
        else if(hasil < 60){
            txtHasil.setText("Kondisi detak jantung anda dibawah normal, (dibawah 60 BPM) bisa menjadi tanda dari masalah pada sistem listrik jantung, disebut bradycardia. Ini berarti bahwa alat pacu jantung alami jantung tidak bekerja dengan baik atau bahwa jalur listrik jantung anda tergangu.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
