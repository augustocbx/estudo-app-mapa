package curso.mpgo.com.cursoandroid;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

/**
 * Created by ricardoogliari on 5/10/16.
 */
public class Poligono extends Forma {
    LatLng position;

    public java.util.List<Ponto> pontos;
    private PolygonOptions polygonOptions;

    public Poligono(){
        polygonOptions = new PolygonOptions();
    }

    @Override
    public LatLng getPosition() {
        Ponto ponto = pontos.get(0);
        position = new LatLng(ponto.latitude, ponto.longitude);
        return position;
    }

    public PolygonOptions getPolygonOptions(){
        System.out.println(this.pontos);
        for (Ponto ponto : this.pontos){
            System.out.println(ponto);
            System.out.println(ponto.latitude);
            polygonOptions.add(new LatLng(ponto.latitude, ponto.longitude));
        }
        polygonOptions
                .strokeColor(Color.BLUE)
                .fillColor(Color.LTGRAY);
        return polygonOptions;
    }
}
