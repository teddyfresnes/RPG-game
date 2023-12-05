package lecteur_de_fichiers;


import java.util.ArrayList;
import java.util.List;


public class FilereaderTextCsv extends FilereaderText
{
    public FilereaderTextCsv(String chemin)
    {
        super(chemin);
    }
	
	
    public void f_display()
    {
        String[][] csvData = f_import_csv();

        System.out.println("Contenu du fichier " + this.chemin + " à l'endroit :");
        for (String[] row : csvData)
        {
            for (String cell : row)
            {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }
    
    
    public String[][] f_import_csv()
    {
        String content = f_read();
        
        String[] lines = content.split("\\r?\\n"); // divise les lignes
        List<String[]> data = new ArrayList<>();
        for (String line : lines)
        {
            String[] cells = line.split(";");
            data.add(cells);
        }
        String[][] result = new String[data.size()][];
        result = data.toArray(result);

        return result;
    }
    
    
    public int[] findFirstNumberCoordinates(int targetNumber) // chercher un nombre dans une cellule
    {
        String[][] csvData = f_import_csv();

        for (int i = 0; i < csvData.length; i++)
        {
            for (int j = 0; j < csvData[i].length; j++)
            {
                if (csvData[i][j].equals(Integer.toString(targetNumber)))
                {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
}
