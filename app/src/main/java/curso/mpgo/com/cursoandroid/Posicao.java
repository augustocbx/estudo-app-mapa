package curso.mpgo.com.cursoandroid;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by ricardoogliari on 5/10/16.
 */
public class Posicao extends Forma implements Serializable{
    public int id;
    public LatLng position;
    public double latitude;
    public double longitude;

    public Posicao(double lat, double lng) {
        position = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return position = new LatLng(latitude, longitude);
    }

    @Override
    public String toString() {
        return name;
    }
}
