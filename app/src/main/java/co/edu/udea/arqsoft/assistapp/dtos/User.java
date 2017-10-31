package co.edu.udea.arqsoft.assistapp.dtos;

/**
 * Created by AW 13 on 30/10/2017.
 */

public class User {

    private int id;
    private String username;
    private String passw;
    private String rol;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
