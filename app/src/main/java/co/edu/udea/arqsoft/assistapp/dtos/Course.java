package co.edu.udea.arqsoft.assistapp.dtos;

import java.util.Date;

/**
 * Created by AW 13 on 25/11/2017.
 */

public class Course {
    private int id;
    private String name;
    private String descripcion;
    private String fechaExpiracion;
    private boolean habilitado;
    private int owner;

    public Course(int id, String name, String descripcion, String fechaExpiracion, boolean habilitado, int owner) {
        this.id = id;
        this.name = name;
        this.descripcion = descripcion;
        this.fechaExpiracion = fechaExpiracion;
        this.habilitado = habilitado;
        this.owner = owner;
    }

    public Course() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}
