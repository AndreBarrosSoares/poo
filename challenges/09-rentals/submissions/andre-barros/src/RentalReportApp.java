import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class RentalReportApp {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Uso: java RentalReportApp <valorMinimo>");
            return;
        }

        double minValue = Double.parseDouble(args[0]);

        Properties props = new Properties();

        try (InputStream in = Files.newInputStream(Paths.get("db.properties"))) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("Erro ao carregar db.properties: " + e.getMessage());
            return;
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        String csvPath = props.getProperty("csv.path");

        String sql = """
            SELECT c.first_name AS Nome,
                   c.last_name AS Sobrenome,
                   c.email AS Email,
                   SUM(p.amount) AS ValorGasto
            FROM rental r
            JOIN customer c ON c.customer_id = r.customer_id
            JOIN payment p ON p.customer_id = c.customer_id AND p.rental_id = r.rental_id
            GROUP BY c.first_name, c.last_name, c.email
            HAVING SUM(p.amount) > ?
            ORDER BY SUM(p.amount) DESC;
            """;

        List<RentalSummary> results = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, minValue);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String fn = rs.getString("Nome");
                    String ln = rs.getString("Sobrenome");
                    String em = rs.getString("Email");
                    double amount = rs.getDouble("ValorGasto");

                    results.add(new RentalSummary(fn, ln, em, amount));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro SQL: " + e.getMessage());
            return;
        }

        // GRAVAR CSV
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvPath))) {

            writer.write("Nome,Sobrenome,Email,Valor Gasto");
            writer.newLine();

            for (RentalSummary r : results) {
                writer.write(r.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Erro escrevendo CSV: " + e.getMessage());
        }
    }
}
