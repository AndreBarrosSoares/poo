import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReaderUtil {

    public static List<Film> loadFilmsFromFile(String filePath) {
        List<Film> films = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));

            for (int i = 1; i < lines.size(); i++) {  
            String line = lines.get(i);
                String[] p = line.split(";");

                Film film = new Film(
                        p[0],
                        Integer.parseInt(p[1]),
                        Integer.parseInt(p[2]),
                        Double.parseDouble(p[3]),
                        Double.parseDouble(p[4])
                );
                films.add(film);
            }

        } catch (Exception e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }

        return films;
    }
}
