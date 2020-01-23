package espol.edu.bagtraking.Modelo;

import java.util.HashMap;

public class Usuario {
    private String usuario;
    private String nombre;
    private String uid;
    private String mail;

    public String getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUid() {
        return uid;
    }

    public String getMail() {
        return mail;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhone() {
        return phone;
    }

    private String photo;
    private String phone;
    private HashMap<String, String> info_user;
    public Usuario(String usuario, String nombre, String uid, String mail, String photo, String phone) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.uid = uid;
        this.mail = mail;
        this.photo = photo;
        this.phone = phone;
        info_user = new HashMap<>();
    }



    public HashMap<String, String> getDictionary(){
        info_user.put("user_iduser",this.mail);
        info_user.put("user_name",this.nombre);
        info_user.put("user_email", this.mail);
        info_user.put("user_photo", this.photo);
        info_user.put("user_id", this.uid);
        info_user.put("user_phone", this.phone);
        return info_user;
    }

}
