package ta.nurul.dtkjntg;

/**
 * Created by taufik on 01/07/18.
 */

public class ItemBpm {
    String id_bpm, bpm, time;

    public ItemBpm(String id_bpm, String suhu, String time) {
        this.id_bpm = id_bpm;
        this.bpm = suhu;
        this.time = time;
    }

    public String getId_bpm() {
        return id_bpm;
    }

    public String getBpm() {
        return bpm;
    }

    public String getTime() {
        return time;
    }
}
