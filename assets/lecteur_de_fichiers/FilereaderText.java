package lecteur_de_fichiers;



import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;



public abstract class FilereaderText
{
	// protected pour le laisser priver mais accessible ou sous classes
    protected String chemin;

    
    public FilereaderText(String chemin)
    {
        this.chemin = chemin;
    }

    public void f_open()
    {
        System.out.println("Ouverture du fichier : "+this.chemin);
    }
    

    public void f_close()
    {
        System.out.println("Fermeture du fichier : "+this.chemin);
    }
    

    public String f_read() // méthode differente du lecteur de fichier après passage de java en 1.8 (incompatible)
    {
        Path filePath = Paths.get(this.chemin);

        try
        {
            String content = Files.lines(filePath).collect(Collectors.joining(System.lineSeparator()));
            //System.out.println("Lecture du fichier réussie : "+this.chemin);
            return content;
        }
        catch (IOException error)
        {
            System.out.println("Lecture du fichier échouée : " + this.chemin);
            error.printStackTrace();
            return "N/A";
        }
    }
    
    
    // methode qui differe selon l'affichage que l'on souhaite dans les classes hérités
    public abstract void f_display();
    
}
