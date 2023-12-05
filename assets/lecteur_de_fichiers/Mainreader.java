package lecteur_de_fichiers;




public class Mainreader
{
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

        String monFichier = "fichiers/test.txt";

        
        System.out.println("\n\nLecteur 1 : Affichage normal du fichier\n-----------------------------------");
        FilereaderText filereader1 = new FilereaderTextNormal(monFichier);
        filereader1.f_open();
        filereader1.f_display();
        filereader1.f_close();
        
        
        System.out.println("\n\nLecteur 2 : Affichage lignes à l'envers\n-----------------------------------");
        FilereaderText filereader2 = new FilereaderTextReversedln(monFichier);
        filereader2.f_open();
        filereader2.f_display();
        filereader2.f_close();
       
        System.out.println("\n\nLecteur 3 : Affichage à l'envers\n-----------------------------------");
        FilereaderText filereader3 = new FilereaderTextReversed(monFichier);
        filereader3.f_open();
        filereader3.f_display();
        filereader3.f_close();
       
        System.out.println("\n\nLecteur 3 : Test comparateur de contenu\n-----------------------------------");
        // suppression de la méthode incompatible avec jre 1.8
	}
}
