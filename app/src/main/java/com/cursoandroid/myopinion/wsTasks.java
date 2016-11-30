package com.cursoandroid.myopinion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.cursoandroid.myopinion.adapter.EstabelecimentoAdapter;
import com.cursoandroid.myopinion.domain.Estabelecimento;
import com.cursoandroid.myopinion.domain.Usuario;
import com.facebook.Profile;
import com.github.ornolfr.ratingview.RatingView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by igor on 25/11/16.
 */

public class wsTasks extends Activity {

    private EstabelecimentoAdapter.MyViewHolder holder;
    private AccessServiceAPI m_AccessServiceAPI;
    Context context;
    private Usuario user;
    private ArrayList<Estabelecimento> listEstabelecimentos;
    MainActivity main;
    RecyclerView recyclerView;
    private Estabelecimento e;
    RatingView ratingView;
    AccountHeader accountHeader;
    public wsTasks(Context context)
    {
        this.context = context;
    }

    public wsTasks(Estabelecimento e,RatingView ratingView){ this.e = e; this.ratingView = ratingView; }

    public wsTasks(Context context, Usuario user, AccountHeader acc, RecyclerView recyclerView,MainActivity main,ArrayList<Estabelecimento> list)
    {
        this.context = context;
        this.user = user;
        accountHeader = acc;
        this.recyclerView = recyclerView;
        this.main = main;
        this.listEstabelecimentos = list;
//        this.profile = iProfile;
    }

    public wsTasks(EstabelecimentoAdapter.MyViewHolder holder, Estabelecimento e){ this.holder = holder; this.e = e;}

    public void execTaskUserRegister(String nome, String email, String dtNasc, String senha, String cep){
        new TaskUserRegister().execute(nome,email,dtNasc,senha,cep,""/*Base64.encodeToString(foto, Base64.DEFAULT)*/);
    }

    public void execTaskLoadUser(int id)
    {
        new TaskLoadUser().execute(String.valueOf(id));
    }

    public void execTaskAddEstabelecimento(String nome, String tipoEstabelecimento, float rate, String estado, String cidade, String bairro, double latitude, double longitude, byte[] foto, String responsavel, int avaliations, boolean notificacao, int idUsuario)
    {
        new TaskAddEstabelecimento().execute(nome,tipoEstabelecimento,String.valueOf(latitude),String.valueOf(longitude),String.valueOf(booleanToInt(notificacao)),""/*Base64.encodeToString(foto,Base64.DEFAULT)*/,String.valueOf(rate),String.valueOf(avaliations),estado,cidade,bairro,String.valueOf(idUsuario),responsavel);
    }

    public void exectTaskLoadEstabelecimentos() { new TaskLoadEstabelecimentos().execute();}

    public void execTaskUpdateEstabelecimento(long idEstabelecimento,double rate, int avaliations){
        new TaskUpdateEstabelecimento().execute(String.valueOf(idEstabelecimento),String.valueOf(rate),String.valueOf(avaliations));
    }

    public void execTaskLoadEstabelecimento() { new TaskLoadEstabelecimento().execute(String.valueOf(e.getId()));}

    public void execTaskLoadFavorites(String listaFavoritos){ new TaskLoadFavorites().execute(listaFavoritos); }

    public void execTaskLoadNearby(double latitude, double longitude){ new TaskLoadNearby().execute(String.valueOf(latitude),String.valueOf(longitude));}

    public void execNewEstabelecimentos(){ new TaskLoadNewEstabelecimentos().execute(); }

    public void execTaskLoadImgEstabelecimento(String nome){ new TaskLoadImgEstabelecimento().execute(nome);}

    public void execTaskLoadImgFacebook(){ new TaskLoadImgFacebook().execute(); }

    public void execTaskForgotPassword(String email){ new TaskForgotPassword().execute(email);}

    private int booleanToInt(boolean b)
    {
        if(b){ return 1; }
        else{ return 0; }

    }

    /* CLASSES ASSÍNCRONAS */

    public class TaskUserRegister extends AsyncTask<String, Void, Integer>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action","addUser");
            postParam.put("nome",params[0]);
            postParam.put("email",params[1]);
            postParam.put("dtNasc",params[2]);
            postParam.put("senha",params[3]);
            postParam.put("cep",params[4]);
            postParam.put("foto",params[5]);

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(WSConfig.SERVICE_API_URL, postParam);
                return Integer.parseInt(jsonString.trim());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return WSConfig.RESULT_ERROR;
        }

    }

    public class TaskLoadUser extends AsyncTask<String, Void, Integer>
    {
        Bitmap foto;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action","getUser");
            postParam.put("idusuario",params[0]);

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(WSConfig.SERVICE_API_URL, postParam);

                JSONObject jsonObject = new JSONObject(jsonString);
                user.setId(Long.parseLong(params[0]));
                user.setNome(String.valueOf(jsonObject.getString("nome")));
                Log.d("TESTE",user.getNome());
                user.setEmail(String.valueOf(jsonObject.getString("email")));
                user.setDtNasc(String.valueOf(jsonObject.getString("dtNasc")));
                user.setSenha(String.valueOf(jsonObject.getString("nome")));
//                user.setFoto(Base64.decode(String.valueOf(jsonObject.getString("foto")), Base64.DEFAULT));
                user.setCep(String.valueOf(jsonObject.getString("cep")));

//                URL url = new URL("https://image.freepik.com/icones-gratis/usuario-masculino-fechar-se-forma-para-facebook_318-37635.jpg");
//                foto = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                return WSConfig.RESULT_SUCCESS;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return WSConfig.RESULT_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            ArrayList<IProfile> arrayList = new ArrayList<>();
            user.setFoto( BitmapUtil.getBitmapAsByteArray(BitmapFactory.decodeResource(context.getResources(),R.drawable.avatar)));
            IProfile profile = new ProfileDrawerItem().withIdentifier(100).withEmail(user.getEmail()).withName(user.getNome()).withIcon(user.getFotoBitmap());;//.withName(user.getNome());
            arrayList.add(profile);
            accountHeader.setProfiles(arrayList);
            finish();
        }
    }


    public class TaskAddEstabelecimento extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {

            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "addEstabelecimento");
            postParam.put("nome", params[0]);
            postParam.put("tpEstabelecimento", params[1]);
            postParam.put("latitude", params[2]);
            postParam.put("longitude", params[3]);
            postParam.put("notificacao", params[4]);
            postParam.put("foto", params[5]);
            postParam.put("rate", params[6]);
            postParam.put("avaliations", params[7]);
            postParam.put("estado", params[8]);
            postParam.put("cidade", params[9]);
            postParam.put("bairro", params[10]);
            postParam.put("idusuario", params[11]);
            postParam.put("responsavel",params[12]);

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(WSConfig.SERVICE_API_URL, postParam);
                return Integer.parseInt(jsonString.trim());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return WSConfig.RESULT_ERROR;
        }



    }


    public class TaskLoadEstabelecimentos extends AsyncTask<String, Void, Integer>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
            listEstabelecimentos.clear();
//            listEstabelecimentos = new ArrayList<>();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action","getAllEstabelecimentos");

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(WSConfig.SERVICE_API_URL, postParam);
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);


                    for(int i=0; i< jsonArray.length();i++)
                    {
                        Estabelecimento novo = new Estabelecimento();
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        novo.setId(Long.valueOf(jsonObject.getString("_ID")));
                        novo.setNome(String.valueOf(jsonObject.getString("nome")));
                        novo.setTipoEstabelecimento(String.valueOf(jsonObject.getString("tipoEstabelecimento")));
                        novo.setLatitude(Double.valueOf(jsonObject.getString("latitude")));
                        novo.setLongitude(Double.valueOf(jsonObject.getString("longitude")));
                        novo.setNotificacao(Integer.valueOf(jsonObject.getString("notificacao")) > 0);
                        novo.setRating(Float.valueOf(jsonObject.getString("rate")));
                        novo.setNumAvaliacoes(Integer.valueOf(jsonObject.getString("avaliations")));
                        novo.setEstado(String.valueOf(jsonObject.getString("estado")));
                        novo.setCidade(String.valueOf(jsonObject.getString("cidade")));
                        novo.setBairro(String.valueOf(jsonObject.getString("bairro")));
                        novo.setResponsavel(String.valueOf(jsonObject.getString("responsavel")));


                        listEstabelecimentos.add(novo);
                    }

                    return WSConfig.RESULT_SUCCESS;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return WSConfig.RESULT_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(integer == WSConfig.RESULT_SUCCESS)
            {
                EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(context,listEstabelecimentos);
                adapter.setmRecyclerViewOnClickListenerHack(main);
                recyclerView.setAdapter(adapter);
            }

            finish();
        }

    }


    public class TaskUpdateEstabelecimento extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();

        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action","updateRate");
            postParam.put("idEstabelecimento",params[0]);
            postParam.put("rate",params[1]);
            postParam.put("avaliations",params[2]);

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(WSConfig.SERVICE_API_URL, postParam);
                return Integer.parseInt(jsonString.trim());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return WSConfig.RESULT_ERROR;
        }

//        @Override
//        protected void onPostExecute(Integer integer) {
//            super.onPostExecute(integer);
//            finish();
//        }

    }


    public class TaskLoadEstabelecimento extends AsyncTask<String, Void, Integer> {

        private AccessServiceAPI m_AccessServiceAPI;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
//            listEstabelecimentos = new ArrayList<>();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "getEstabelecimento");
            postParam.put("idEstabelecimento",params[0]);

            try {
                String jsonString = null;
                jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(WSConfig.SERVICE_API_URL, postParam);

                JSONObject jsonObject = new JSONObject(jsonString);
                e.setId(Long.valueOf(jsonObject.getString("_ID")));
                e.setNome(String.valueOf(jsonObject.getString("nome")));
                e.setTipoEstabelecimento(String.valueOf(jsonObject.getString("tipoEstabelecimento")));
                e.setLatitude(Double.valueOf(jsonObject.getString("latitude")));
                e.setLongitude(Double.valueOf(jsonObject.getString("longitude")));
                e.setNotificacao(Integer.valueOf(jsonObject.getString("notificacao")) > 0);
                e.setRating(Float.valueOf(jsonObject.getString("rate")));
                e.setNumAvaliacoes(Integer.valueOf(jsonObject.getString("avaliations")));
                e.setEstado(String.valueOf(jsonObject.getString("estado")));
                e.setCidade(String.valueOf(jsonObject.getString("cidade")));
                e.setBairro(String.valueOf(jsonObject.getString("bairro")));
                e.setResponsavel(String.valueOf(jsonObject.getString("responsavel")));

                return WSConfig.RESULT_SUCCESS;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return WSConfig.RESULT_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            ratingView.setRating(e.getRating());
            finish();
        }

    }


    public class TaskLoadFavorites extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            Map<String, String> postParam = new HashMap<>();
            postParam.put("action", "getFavorites");
            postParam.put("listaFavoritos",params[0]);

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(WSConfig.SERVICE_API_URL,postParam);

                try {
                    JSONArray jsonArray = new JSONArray(jsonString);


                    for(int i=0; i< jsonArray.length();i++)
                    {
                        Estabelecimento novo = new Estabelecimento();
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        novo.setId(Long.valueOf(jsonObject.getString("_ID")));
                        novo.setNome(String.valueOf(jsonObject.getString("nome")));
                        novo.setTipoEstabelecimento(String.valueOf(jsonObject.getString("tipoEstabelecimento")));
                        novo.setLatitude(Double.valueOf(jsonObject.getString("latitude")));
                        novo.setLongitude(Double.valueOf(jsonObject.getString("longitude")));
                        novo.setNotificacao(Integer.valueOf(jsonObject.getString("notificacao")) > 0);
                        novo.setRating(Float.valueOf(jsonObject.getString("rate")));
                        novo.setNumAvaliacoes(Integer.valueOf(jsonObject.getString("avaliations")));
                        novo.setEstado(String.valueOf(jsonObject.getString("estado")));
                        novo.setCidade(String.valueOf(jsonObject.getString("cidade")));
                        novo.setBairro(String.valueOf(jsonObject.getString("bairro")));
                        novo.setResponsavel(String.valueOf(jsonObject.getString("responsavel")));
                        Log.d("SEILA",novo.getNome());
                        listEstabelecimentos.add(novo);
                    }

                    return WSConfig.RESULT_SUCCESS;
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return WSConfig.RESULT_ERROR;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
            listEstabelecimentos.clear();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(integer == WSConfig.RESULT_SUCCESS)
            {
                EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(context,listEstabelecimentos);
                adapter.setmRecyclerViewOnClickListenerHack(main);
                recyclerView.setAdapter(adapter);
            }
            finish();
        }
    }

    public class TaskLoadNewEstabelecimentos extends AsyncTask<String, Void, Integer>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
            listEstabelecimentos.clear();
//            listEstabelecimentos = new ArrayList<>();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action","getNewEstabelecimentos");

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(WSConfig.SERVICE_API_URL, postParam);
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);


                    for(int i=0; i< jsonArray.length();i++)
                    {
                        Estabelecimento novo = new Estabelecimento();
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        novo.setId(Long.valueOf(jsonObject.getString("_ID")));
                        novo.setNome(String.valueOf(jsonObject.getString("nome")));
                        novo.setTipoEstabelecimento(String.valueOf(jsonObject.getString("tipoEstabelecimento")));
                        novo.setLatitude(Double.valueOf(jsonObject.getString("latitude")));
                        novo.setLongitude(Double.valueOf(jsonObject.getString("longitude")));
                        novo.setNotificacao(Integer.valueOf(jsonObject.getString("notificacao")) > 0);
                        novo.setRating(Float.valueOf(jsonObject.getString("rate")));
                        novo.setNumAvaliacoes(Integer.valueOf(jsonObject.getString("avaliations")));
                        novo.setEstado(String.valueOf(jsonObject.getString("estado")));
                        novo.setCidade(String.valueOf(jsonObject.getString("cidade")));
                        novo.setBairro(String.valueOf(jsonObject.getString("bairro")));
                        novo.setResponsavel(String.valueOf(jsonObject.getString("responsavel")));
                        listEstabelecimentos.add(novo);
                    }

                    return WSConfig.RESULT_SUCCESS;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return WSConfig.RESULT_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(integer == WSConfig.RESULT_SUCCESS)
            {
                EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(context,listEstabelecimentos);
                adapter.setmRecyclerViewOnClickListenerHack(main);
                recyclerView.setAdapter(adapter);
            }

            finish();
        }


    }

    public class TaskLoadNearby extends AsyncTask<String, Void, Integer>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
            listEstabelecimentos.clear();
//            listEstabelecimentos = new ArrayList<>();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action","getProximos");
            postParam.put("latitude",params[0]);
            postParam.put("longitude",params[1]);

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(WSConfig.SERVICE_API_URL, postParam);
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);


                    for(int i=0; i< jsonArray.length();i++)
                    {
                        Estabelecimento novo = new Estabelecimento();
                        JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                        novo.setId(Long.valueOf(jsonObject.getString("_ID")));
                        novo.setNome(String.valueOf(jsonObject.getString("nome")));
                        novo.setTipoEstabelecimento(String.valueOf(jsonObject.getString("tipoEstabelecimento")));
                        novo.setLatitude(Double.valueOf(jsonObject.getString("latitude")));
                        novo.setLongitude(Double.valueOf(jsonObject.getString("longitude")));
                        novo.setNotificacao(Integer.valueOf(jsonObject.getString("notificacao")) > 0);
                        novo.setRating(Float.valueOf(jsonObject.getString("rate")));
                        novo.setNumAvaliacoes(Integer.valueOf(jsonObject.getString("avaliations")));
                        novo.setEstado(String.valueOf(jsonObject.getString("estado")));
                        novo.setCidade(String.valueOf(jsonObject.getString("cidade")));
                        novo.setBairro(String.valueOf(jsonObject.getString("bairro")));
                        novo.setResponsavel(String.valueOf(jsonObject.getString("responsavel")));
                        listEstabelecimentos.add(novo);
                    }

                    return WSConfig.RESULT_SUCCESS;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            return WSConfig.RESULT_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(integer == WSConfig.RESULT_SUCCESS)
            {
                EstabelecimentoAdapter adapter = new EstabelecimentoAdapter(context,listEstabelecimentos);
                adapter.setmRecyclerViewOnClickListenerHack(main);
                recyclerView.setAdapter(adapter);
            }

            finish();
        }


    }

    public class TaskLoadImgEstabelecimento extends AsyncTask<String, Void, Integer> {

        URL url;
        @Override
        protected Integer doInBackground(String... params) {

            try {
                url = new URL("https://firebasestorage.googleapis.com/v0/b/minha-opiniao-ff4d1.appspot.com/o/estabelecimentos%2F"+params[0].toLowerCase().replace(" ","%20")+".jpg?alt=media&token=5de1dd94-94f5-49f5-9cc0-683cd5dbbc24");
                return WSConfig.RESULT_SUCCESS;
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            return WSConfig.RESULT_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
                if(integer == WSConfig.RESULT_SUCCESS)
                {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference estabelecimentosRef = storage.getReferenceFromUrl("gs://minha-opiniao-ff4d1.appspot.com").child("estabelecimentos");
                    StorageReference downloadImage = estabelecimentosRef.child(e.getNome().toLowerCase()+".jpg");

                    downloadImage.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            // Use the bytes to display the image
                            holder.ivEstabelecimento.setImageBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length));
                            e.setFoto(bytes);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });



                }
        }
    }

    public class TaskLoadImgFacebook extends AsyncTask<String, Void, Integer> {

        Bitmap bitmap;
        @Override
        protected Integer doInBackground(String... params) {

                URL imageURL = null;
                try {
                    imageURL = new URL("https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?type=large");
                     bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    return WSConfig.RESULT_SUCCESS;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return WSConfig.RESULT_ERROR;
        }

        @Override
        protected void onPostExecute(Integer integer) {

            if(integer == WSConfig.RESULT_SUCCESS) {
                ArrayList<IProfile> arrayList = new ArrayList<>();
                super.onPostExecute(integer);
                IProfile profile = new ProfileDrawerItem().withName(Profile.getCurrentProfile().getName()).withEmail("").withIcon(bitmap).withIdentifier(100);
                arrayList.add(profile);
                accountHeader.setProfiles(arrayList);
            }
        }
    }


    public class TaskForgotPassword extends AsyncTask<String, Void, Integer>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_AccessServiceAPI = new AccessServiceAPI();
        }

        @Override
        protected Integer doInBackground(String... params) {
            Map<String, String> postParam = new HashMap<>();
            postParam.put("action","recuperarSenha");
            postParam.put("email",params[0]);

            try {
                String jsonString = m_AccessServiceAPI.getJSONStringWithParam_POST(WSConfig.SERVICE_API_URL, postParam);
                return WSConfig.RESULT_SUCCESS;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return WSConfig.RESULT_ERROR;
        }

    }

}






