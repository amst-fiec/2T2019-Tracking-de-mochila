package espol.edu.bagtraking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {

    private EditText email;
    EditText pass;
    private FirebaseAuth mAuth;
    static final int GOOGLE_SIGN_IN = 123;
    GoogleSignInClient mGoogleSignInClient;
    DatabaseReference db_reference;


    private static final String TAG = "EmailPassword";
    private HashMap<String, String>  info_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.text_email);
        pass = findViewById(R.id.text_pass);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        if(msg != null){
            if(msg.equals("cerrarSesion")){
                cerrarSesion();
            }
        }

        db_reference = FirebaseDatabase.getInstance().getReference().child("Aplicacion");





    }


    private void cerrarSesion() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
        signOut();
    }

    @Override
    public void onStart() {
        super.onStart();/* Check if user is signed in (non-null) and update UI accordingly.*/
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            getUser(currentUser);
            StartThread();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("TAG", "Fallo el inicio de sesión con google.", e);

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            GuardadUsuario(info_user,user);
                            updateUI(user);
                            Toast.makeText(getApplicationContext(),"Bienvenido"+user.getDisplayName(),Toast.LENGTH_LONG).show();

                        } else {
                            System.out.println("error");
                            Toast.makeText(getApplicationContext(),"Usuario o Contraseña Incorrectos!",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {

            HashMap<String, String> info_user = new HashMap<String, String>();
            info_user.put("user_name", user.getDisplayName());
            info_user.put("user_email", user.getEmail());
            info_user.put("user_photo", String.valueOf(user.getPhotoUrl()));
            info_user.put("user_id", user.getUid());
            info_user.put("user_phone", user.getPhoneNumber());



            Intent intent = new Intent(this, perfil_usuario_1.class);
            intent.putExtra("info_user", info_user);
            startActivity(intent);
            finish();

        } else {
            System.out.println("No Login");
        }
    }

    public void login_google(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }
    public void register(View view) {
        Intent signInIntent = new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(signInIntent);
    }

    public void login(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            signIn(email.getText().toString(),pass.getText().toString());
        }

    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Bienvenido...",Toast.LENGTH_LONG).show();
                            getUser(user);
                            StartThread();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Usuario o Contraseña Incorrectos!!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = this.email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            this.email.setError("Required.");
            valid = false;
        } else {
            this.email.setError(null);
        }

        String password = this.pass.getText().toString();
        if (TextUtils.isEmpty(password)) {
            pass.setError("Required.");
            valid = false;
        } else {
            pass.setError(null);
        }

        return valid;
    }
    public void getUser(FirebaseUser user){
        HashMap<String, String> info_user = new HashMap<String, String>();
        db_reference.child("Usuarios").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                info_user.put("user_name", String.valueOf(dataSnapshot.child("user_name").getValue()));
                info_user.put("user_email", String.valueOf(dataSnapshot.child("user_email").getValue()));
                info_user.put("user_photo", String.valueOf(dataSnapshot.child("user_photo").getValue()));
                info_user.put("user_phone", String.valueOf(dataSnapshot.child("user_phone").getValue()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });
        this.info_user = info_user;
    }

    public void StartThread(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                while(info_user.isEmpty()){System.out.println(info_user);}
                Intent intent = new Intent(getApplicationContext(), perfil_usuario_1.class);
                intent.putExtra("info_user", info_user);
                startActivity(intent);
                finish();
            }
        };

        thread.start();
    }
    private void GuardadUsuario(HashMap<String, String> info_user,FirebaseUser user){

        if(info_user==null){
            info_user = new HashMap<>();
        }
        info_user.put("user_iduser",user.getEmail());
        info_user.put("user_name", user.getDisplayName());
        info_user.put("user_email", user.getEmail());
        info_user.put("user_photo", String.valueOf(user.getPhotoUrl()));
        info_user.put("user_id", user.getUid());
        info_user.put("user_phone", user.getPhoneNumber());

        DatabaseReference data = db_reference.child("Usuarios").child(info_user.get("user_id"));
        data.child("user_name").setValue(info_user.get("user_name"));
        data.child("user_email").setValue(info_user.get("user_email"));
        data.child("user_phone").setValue(info_user.get("user_phone"));
        data.child("user_iduser").setValue(info_user.get("user_iduser"));
        data.child("user_photo").setValue(info_user.get("user_photo"));




    }
}
