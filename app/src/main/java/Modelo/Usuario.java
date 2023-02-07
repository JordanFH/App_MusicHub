package Modelo;

public class Usuario {
    String codusu;
    String nombre;
    String correo;
    String clave;
    String estado;
    int tipo;

    public Usuario() {
    }

    public String getEstado() {
        return estado;
    }

    public String getCodusu() {
        return codusu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setCodusu(String codusu) {
        this.codusu = codusu;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean verificarCredencial() {
        if (estado.equals("Activo"))
            return true;
        else
            return false;
    }

    public String getTipo() {
        String[] stipo = {"Usuario", "Administrador"};
        return stipo[tipo];
    }

    public String mostrarDatos() {
        return "Bienvenido " + getTipo() + ": " + nombre;
    }
}
