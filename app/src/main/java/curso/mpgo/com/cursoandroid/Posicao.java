package curso.mpgo.com.cursoandroid;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ricardoogliari on 5/10/16.
 */
public class Posicao extends Forma {
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
}
