package lecteur_de_fichiers;

public class FilereaderTextNormal extends FilereaderText
{
    public FilereaderTextNormal(String chemin)
    {
        super(chemin);
    }
	
	
    public void f_display()
    {
        String content = f_read();
        System.out.println("Contenu du fichier "+this.chemin+" à l'endroit :");
        System.out.println(content);
    }
}
