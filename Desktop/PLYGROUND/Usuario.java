package com.example.playground;

public class Usuario {

    private int id;
    private String correo;
    private String password;

    public Usuario() {
    }

    public Usuario(String correo, String password) {
        this.correo = correo;
        this.password = password;
    }

    public Usuario(int id, String correo, String password) {
        this.id = id;
        this.correo = correo;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean esCorreoValido() {
        return correo != null && correo.contains("@") && correo.contains(".");
    }

    public boolean esPasswordValida() {
        return password != null && password.length() >= 6;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", correo='" + correo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;
        return id == usuario.id && correo.equals(usuario.correo);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + correo.hashCode();
        return result;
    }
}