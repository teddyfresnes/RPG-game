package lecteur_de_fichiers;


public interface Filereader
{
    void f_open(String chemin);
    void f_close();
    String f_read();
}