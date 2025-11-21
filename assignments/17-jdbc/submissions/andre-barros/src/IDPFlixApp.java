import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class IDPFlixApp {

    private static final String URL = "jdbc:postgresql://localhost:5432/dvdrental";
    private static final String USER = "postgres";
    private static final String PASS = "andre3006";

    public static void main(String[] args) {

        List<Film> films = FileReaderUtil.loadFilmsFromFile("../../../data/new_films.txt");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

            String insertSQL = """
                    INSERT INTO film (title, language_id, rental_duration, rental_rate, replacement_cost)
                    VALUES (?, ?, ?, ?, ?)
                    """;

            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                for (Film f : films) {
                    ps.setString(1, f.getTitle());
                    ps.setInt(2, f.getLanguageId());
                    ps.setInt(3, f.getRentalDuration());
                    ps.setDouble(4, f.getRentalRate());
                    ps.setDouble(5, f.getReplacementCost());
                    ps.executeUpdate();
                }
            }

            String updateSQL = "UPDATE film SET rental_rate = rental_rate * 1.1";

            try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                ps.executeUpdate();
            }

            String selectSQL = """
                    SELECT title, rental_rate
                    FROM film
                    WHERE rental_duration = 99
                    """;

            try (PreparedStatement ps = conn.prepareStatement(selectSQL);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    System.out.println(rs.getString("title") + " | R$ " + rs.getDouble("rental_rate"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
