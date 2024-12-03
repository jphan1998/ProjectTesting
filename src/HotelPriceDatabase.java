import java.sql.*;
import java.time.LocalDate;

public class HotelPriceDatabase {
    private static final String DB_URL = "jdbc:sqlite:hotel_prices.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "CREATE TABLE IF NOT EXISTS hotel_prices ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "city TEXT NOT NULL,"
                    + "hotel_name TEXT NOT NULL,"
                    + "price INTEGER NOT NULL,"
                    + "search_date TEXT NOT NULL,"
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Database initialized successfully");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void savePrice(String city, String hotelName, int price, LocalDate searchDate) {
        String sql = "INSERT INTO hotel_prices (city, hotel_name, price, search_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, city);
            pstmt.setString(2, hotelName);
            pstmt.setInt(3, price);
            pstmt.setString(4, searchDate.toString());

            pstmt.executeUpdate();
            System.out.println("Saved to database: " + city + " - " + hotelName + " - $" + price + " - " + searchDate);
        } catch (SQLException e) {
            System.out.println("Error saving price for " + hotelName + " in " + city);
            e.printStackTrace();
        }
    }

    public static void verifyPrices(String city, LocalDate searchDate) {
        String sql = "SELECT hotel_name, price FROM hotel_prices WHERE city = ? AND search_date = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, city);
            pstmt.setString(2, searchDate.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\nDatabase entries for " + city + " on " + searchDate + ":");
                while (rs.next()) {
                    String hotelName = rs.getString("hotel_name");
                    int price = rs.getInt("price");
                    System.out.println(hotelName + ": $" + price);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error verifying prices in database");
            e.printStackTrace();
        }
    }
}