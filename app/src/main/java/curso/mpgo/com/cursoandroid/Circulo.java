package curso.mpgo.com.cursoandroid;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ricardoogliari on 5/10/16.
 */
public class Circulo extends Forma {

    private LatLng position;

    public float latitude;
    public float longitude;
    public double raio;
    private CircleOptions circleOptions;

    @Override
    public LatLng getPosition() {
        position = new LatLng(latitude, longitude);
        return position;
    }

    public CircleOptions circleOptions() {
        circleOptions = new CircleOptions().center(getPosition()).radius(raio);
        return circleOptions;
    }
}
