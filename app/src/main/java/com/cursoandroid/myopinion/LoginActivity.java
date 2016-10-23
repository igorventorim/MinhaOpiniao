package com.cursoandroid.myopinion;
import com.cursoandroid.myopinion.database.UsuarioDAO;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cursoandroid.myopinion.domain.Usuario;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private UserLoginTask mAuthTask = null;

    Intent telaPrincipal,telaCadastro;
    SharedPreferences sharedPref;
    CallbackManager callbackManager;
    private UsuarioDAO usuarioDAO;

    private final static int TELA_CADASTRO = 1;
    private final static String EMAIL = "email";
    private final static String SENHA = "senha";

    // UI references.
    private LoginButton loginButton;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView esqueciSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuarioDAO = new UsuarioDAO(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_logged), Context.MODE_PRIVATE);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        callbackManager = CallbackManager.Factory.create();

       loginButtonCallBacks();


        ((ImageView) findViewById(R.id.i_logo)).setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.logo, 200, 200));
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        esqueciSenha = (TextView) findViewById(R.id.esqueci_senha);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        final Intent resetPwd = new Intent(this,RecuperarSenhaActivity.class);
        esqueciSenha.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(resetPwd);
            }
        });
        telaPrincipal = new Intent(this, MainActivity.class);
        telaCadastro = new Intent(this,CadastroActivity.class);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                            attemptLogin();
            }
        });


        Button cadastrar = (Button) findViewById(R.id.cadastrar);
        cadastrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(telaCadastro,TELA_CADASTRO);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        if(isLogged() || isLoggedInFacebook())
        {
            startActivity(telaPrincipal);
            finish();
        }
    }


    public boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public void loginButtonCallBacks()
    {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken accessToken = loginResult.getAccessToken();

                GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
//                        fbUser.setEmail(user.optString("email"));
//                        fbUser.setName(user.optString("name"));
//                        fbUser.setId(user.optString("id"));
                        telaPrincipal.putExtra("email",user.optString("email"));
                    }
                }).executeAsync();
                Log.d("Result",request.toString());
                Toast.makeText(getApplicationContext(),getString(R.string.logged_sucess),Toast.LENGTH_LONG).show();
                startActivity(telaPrincipal);
                finish();
            }
            @Override
            public void onCancel() {
//                Toast.makeText(getApplicationContext(),"LOGADO COM SUCESSO",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
//                Toast.makeText(getApplicationContext(),"LOGADO COM SUCESSO",Toast.LENGTH_LONG).show();
            }


        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TELA_CADASTRO && resultCode == RESULT_OK)
        {
            mEmailView.setText(data.getStringExtra(EMAIL));
            mPasswordView.setText(data.getStringExtra(SENHA));
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private int id;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            ArrayList<String> DUMMY_CREDENTIALS = null;
            try {
                // Simulate network access.
                DUMMY_CREDENTIALS = usuarioDAO.getCredenciais();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    id = Integer.parseInt(pieces[2]);
                    return pieces[1].equals(mPassword);
                }
            }

            telaCadastro.putExtra("email",mEmail);
            telaCadastro.putExtra("senha",mPassword);
            startActivity(telaCadastro);
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                writeUserSharedPreferences(id);
                Toast.makeText(getApplicationContext(),getString(R.string.logged_sucess),Toast.LENGTH_LONG).show();
                startActivity(telaPrincipal);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Salvando estado de logado do usuário
     * @param id do usuário
     */
    private void writeUserSharedPreferences(int id)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.saved_logged),true);
        editor.putInt(getString(R.string.user_id),id);
        editor.commit();
    }

    /**
     * Verificando se o usuário está logado
     * @return variável booleana correspondente ao usuário está logado ou não
     */
    private boolean isLogged()
    {
        return sharedPref.getBoolean(getString(R.string.saved_logged),false);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mPasswordView.setError(null);
    }

    private void callMain()
    {
        startActivity(telaPrincipal);
    }
}

