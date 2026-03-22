import java.io.*;
import java.sql.*;
public class InspectEnrollmentSchemaToFile {
  public static void main(String[] args) throws Exception {
    try (PrintWriter out = new PrintWriter(new FileWriter("D:/DoAn_SpringBoot/BackEnd/enrollment-schema.txt"))) {
      String url = "jdbc:mysql://localhost:3306/webkhoahocon";
      try (Connection c = DriverManager.getConnection(url, "root", "123456")) {
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("show tables")) {
          out.println("TABLES");
          while (rs.next()) out.println(rs.getString(1));
        }
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("show columns from enrollment")) {
          out.println("COLUMNS");
          while (rs.next()) out.println(rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getString(3));
        }
      }
    }
  }
}
