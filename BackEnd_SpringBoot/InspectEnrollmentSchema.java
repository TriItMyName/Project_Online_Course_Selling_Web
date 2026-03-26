import java.sql.*;
public class InspectEnrollmentSchema {
  public static void main(String[] args) throws Exception {
    String url = "jdbc:mysql://localhost:3306/webkhoahocon";
    try (Connection c = DriverManager.getConnection(url, "root", "123456")) {
      DatabaseMetaData md = c.getMetaData();
      try (ResultSet rs = md.getColumns(c.getCatalog(), null, "enrollment", null)) {
        while (rs.next()) {
          System.out.println(rs.getString("COLUMN_NAME") + "|" + rs.getString("TYPE_NAME") + "|" + rs.getString("IS_NULLABLE"));
        }
      }
    }
  }
}
