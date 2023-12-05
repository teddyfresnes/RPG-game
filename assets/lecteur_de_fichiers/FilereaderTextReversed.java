package lecteur_de_fichiers;


import java.lang.StringBuilder;


public class FilereaderTextReversed extends FilereaderText
{
    public FilereaderTextReversed(String chemin)
    {
        super(chemin);
    }
	
	
    public void f_display()
    {
        String content = f_read();
        String reversedContent = new StringBuilder(content).reverse().toString();
        reversedContent = reversedContent.replace("\n",""); // suppression des saut de ligne en trop
        
        System.out.println("Contenu du fichier " + this.chemin + " avec lignes à l'envers :");
        System.out.println(reversedContent);
    }
}