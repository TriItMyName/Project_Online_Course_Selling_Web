import java.sql.*;
public class InspectTables {
  public static void main(String[] args) throws Exception {
    String url = "jdbc:mysql://localhost:3306/webkhoahocon";
    try (Connection c = DriverManager.getConnection(url, "root", "123456")) {
      try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("show tables")) {
        while (rs.next()) {
          System.out.println(rs.getString(1));
        }
      }
    }
  }
}
