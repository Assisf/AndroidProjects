package com.example.firebaseteste;

public class Usuario
{
    private String user;
    private String id;
    private String numero;

    @Override
    public String toString() {
        return
                "contato='" + user + '\'' +
                "numero=(" + numero + ")" + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPasswd() {
        return numero;
    }

    public void setPasswd(String passwd) {
        this.numero = passwd;
    }



    public Usuario() {
        this.user = user;
        this.numero = numero;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
