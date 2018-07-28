package ta.nurul.dtkjntg;

/**
 * Created by taufik on 01/07/18.
 */

public class ItemPasien {
    String id_pasien, nama, bpm, time;

    public ItemPasien(String id_pasien, String nama, String bpm, String time) {
        this.id_pasien = id_pasien;
        this.nama = nama;
        this.bpm = bpm;
        this.time = time;
    }

    public String getId_pasien() {
        return id_pasien;
    }

    public String getNama() {
        return nama;
    }

    public String getBpm() {
        return bpm;
    }

    public String getTime() {
        return time;
    }
}
