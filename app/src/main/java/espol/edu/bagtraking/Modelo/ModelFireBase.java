package espol.edu.bagtraking.Modelo;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ModelFireBase {
    private FirebaseAuth mAuth;
    private DatabaseReference db_reference;
    private FirebaseUser currentUser;
    private Usuario user;
    public ModelFireBase(String path) {
        this.mAuth =FirebaseAuth.getInstance();
        this.db_reference = FirebaseDatabase.getInstance().getReference().child(path);
        this.currentUser = mAuth.getCurrentUser();
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public DatabaseReference getDb_reference() {
        return db_reference;
    }
    public DatabaseReference getDb_reference(String path) {
        this.db_reference = FirebaseDatabase.getInstance().getReference().child(path);
        return db_reference;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void getUser(HashMap<String, String> info_user){


        db_reference.child("Usuarios");
        db_reference.child(currentUser.getUid());
        db_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = new Usuario(String.valueOf(dataSnapshot.child("user_email").getValue()), String.valueOf(dataSnapshot.child("user_name").getValue())
                        , currentUser.getUid(), String.valueOf(dataSnapshot.child("user_email").getValue()), String.valueOf(dataSnapshot.child("user_photo").getValue()),
                        String.valueOf(dataSnapshot.child("user_phone").getValue()));
                info_user.put("user_iduser",user.getMail());
                info_user.put("user_name",user.getNombre());
                info_user.put("user_email", user.getMail());
                info_user.put("user_photo", user.getPhoto());
                info_user.put("user_id", user.getUid());
                info_user.put("user_phone", user.getPhone());




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.getMessage());
            }
        });

    }
    public void GuardadUsuario(HashMap<String, String> info_user) {

        FirebaseUser user = currentUser;
        if (user != null) {
            if (info_user == null) {
                info_user = new HashMap<>();
            }
            info_user.put("user_iduser", user.getEmail());
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

}
