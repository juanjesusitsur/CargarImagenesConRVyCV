package com.example.volleyapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Heroes> listaHeroes;
    private RequestQueue rq;
    private RecyclerView rvHeroes;
    private AdaptadorHeroes adaptadorheroes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaHeroes = new ArrayList<>();
        rq= Volley.newRequestQueue(this);


            cargarHeroes();
            rvHeroes=findViewById(R.id.rvHeroes);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            rvHeroes.setLayoutManager(linearLayoutManager);
            adaptadorheroes=new AdaptadorHeroes();
            rvHeroes.setAdapter(adaptadorheroes);



    }

    private void cargarHeroes() {
        String url="https://simplifiedcoding.net/demos/view-flipper/heroes.php";

        JsonObjectRequest requerimiento=new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String valor=response.get("heroes").toString();
                            JSONArray arreglo=new JSONArray(valor);
                            for (int i = 0; i < 4; i++) {
                                JSONObject objeto=new JSONObject(arreglo.get(i).toString());

                                String nombre=objeto.getString("name");
                                //String foto=objeto.getJSONObject("imageurl").getString("large");

                                String img=objeto.getString("imageurl");
                                Heroes heroes = new Heroes(nombre,img);
                                listaHeroes.add(heroes);
                            }

                        adaptadorheroes.notifyItemRangeInserted(listaHeroes.size(),1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

      rq.add(requerimiento);
    }

    private class AdaptadorHeroes extends RecyclerView.Adapter<AdaptadorHeroes.AdaptadorHeroesHolder>{


        @NonNull
        @Override
        public AdaptadorHeroesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorHeroesHolder(getLayoutInflater().inflate(R.layout.card_items,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorHeroesHolder holder, int position) {
        holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return listaHeroes.size();
        }

        class AdaptadorHeroesHolder extends RecyclerView.ViewHolder{
            TextView nombre;
            ImageView img;

             public AdaptadorHeroesHolder(@NonNull View itemView) {
                 super(itemView);
                 nombre=itemView.findViewById(R.id.textView);
                 img=itemView.findViewById(R.id.imageView);

             }

            public void imprimir(int position) {
            nombre.setText(listaHeroes.get(position).getNombre());
            recuperarimg(listaHeroes.get(position).getImage(),img);
            }
        }
    }

    private void recuperarimg(String image, ImageView img) {
        ImageRequest peticion=new ImageRequest(image,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(peticion);
    }
}