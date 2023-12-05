package lecteur_de_fichiers;

public class FilereaderTextReversedln extends FilereaderText
{
    public FilereaderTextReversedln(String chemin)
    {
        super(chemin);
    }
	
	
    public void f_display()
    {
        String content = f_read();
        System.out.println("Contenu du fichier " + this.chemin + " avec lignes à l'envers :");

        String[] lignes = content.split("\n");

        for (int i=lignes.length-1; i >= 0; i--)
        {
            System.out.print(lignes[i]); 
            if (i > 0) // pour eviter que les deux dernieres lignes se collent (les deux premieres affichés)
            {
                System.out.print("\n");
            }
        }
    }
}