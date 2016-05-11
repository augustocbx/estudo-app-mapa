package curso.mpgo.com.cursoandroid;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by ricardoogliari on 5/10/16.
 */
public class Poligono extends Forma {
    LatLng position;

    public java.util.List<Posicao> pontos;
    private PolygonOptions polygonOptions;

    public Poligono(){
        polygonOptions = new PolygonOptions();
    }

    @Override
    public LatLng getPosition() {
        Posicao posicao = pontos.get(0);
        position = new LatLng(posicao.latitude, posicao.longitude);
        return position;
    }

    public PolygonOptions getPolygonOptions(){
        System.out.println(this.pontos);
        for (Posicao posicao : this.pontos){
            System.out.println(posicao);
            System.out.println(posicao.latitude);
            polygonOptions.add(new LatLng(posicao.latitude, posicao.longitude));
        }
        polygonOptions
                .strokeColor(Color.BLUE)
                .fillColor(Color.LTGRAY);
        return polygonOptions;
    }
}
