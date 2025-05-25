package gui;

public class Pais
{
    private String nombre;
    private String nombreImagen;
    
    public Pais(String nombre, String nombreImagen)
    {
        this.nombre = nombre;
        this.nombreImagen = nombreImagen;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getNombreImagen()
    {
        return nombreImagen;
    }
}
