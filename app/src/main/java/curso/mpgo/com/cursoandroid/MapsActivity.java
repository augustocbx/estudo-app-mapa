package curso.mpgo.com.cursoandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import curso.mpgo.com.cursoandroid.database.CirculoDbHelper;
import curso.mpgo.com.cursoandroid.database.PosicaoDbHelper;
import curso.mpgo.com.cursoandroid.util.Conectividade;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private ClusterManager mClusterManager;

    private PosicaoDbHelper dbHelperPosicao;
    private CirculoDbHelper dbHelperCirculo;

    private List<Posicao> posicoes;

    private ImageView imgIconList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        imgIconList = (ImageView) findViewById(R.id.imgIconList);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        dbHelperPosicao = new PosicaoDbHelper(this);
        dbHelperCirculo = new CirculoDbHelper(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        mMap.clear();
//        mMap.addMarker(new MarkerOptions().position(latLng).title("Estou aqui"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public void getLastLocation() {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng eu = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(eu).title("Estou aqui"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eu, 12));

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        if (Conectividade.haveConnectivity(this)) {
            Call<Posicoes> call = ((CoreApplication) getApplication()).service.searchPositions();
            call.enqueue(new Callback<Posicoes>() {
                @Override
                public void onResponse(Call<Posicoes> call, Response<Posicoes> response) {
                    mClusterManager = new ClusterManager<MyItem>(MapsActivity.this, mMap);
                    dbHelperPosicao.clear();
                    dbHelperCirculo.clear();
                    mMap.setOnCameraChangeListener(mClusterManager);
                    mMap.setOnMarkerClickListener(mClusterManager);

                    populaPosicoes(response.body().posicoes);

                    for (Circulo forma : response.body().circulos) {
                        mMap.addCircle(forma.circleOptions());
                        dbHelperCirculo.create(forma);
                    }
                    for (Poligono poligono : response.body().poligonos) {
                        mMap.addPolygon(poligono.getPolygonOptions());
                    }
                    imgIconList.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<Posicoes> call, Throwable t) {
                    Log.e("CURSO", "Pepino: " + t.getLocalizedMessage());
                }
            });
        } else {
            List<Posicao> posicoes = dbHelperPosicao.read();
            List<Circulo> circulos = dbHelperCirculo.read();

            populaPosicoes(posicoes);
            for (Circulo circulo : circulos) {
                dbHelperCirculo.create(circulo);
                dbHelperCirculo.create(circulo);
            }

            mMap.setOnCameraChangeListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);
        }


        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("CURSO", "Sem esta permissão não podemos pegar informações sobre sua posição geográfica e melhor atendê-lo com nossos serviços.");
                return;
            }
            getLastLocation();
        } else {
            Log.e("CURSO", "Sem esta permissão não podemos pegar informações sobre sua posição geográfica e melhor atendê-lo com nossos serviços.");
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        getLastLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void populaPosicoes(List<Posicao> posicoes){
        populaPosicoes(posicoes, false);
    }
    public void populaPosicoes(List<Posicao> posicoes, boolean salvar){

        this.posicoes = posicoes;
        for (Posicao posicao : posicoes) {
            MyItem offsetItem = new MyItem(posicao.latitude, posicao.longitude);
            mClusterManager.addItem(offsetItem);
        }

        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
    }

    public void mostraLista(View view){
        ContainerPosicao containerPosicao = new ContainerPosicao();
        containerPosicao.posicoes = posicoes;
        System.out.println("posicoes");
        System.out.println(posicoes);
        System.out.println(posicoes.size());

        Intent intent = new Intent(this, ListaLocais.class);
        intent.putExtra("listaItens", containerPosicao);
        startActivity(intent);
    }

}
