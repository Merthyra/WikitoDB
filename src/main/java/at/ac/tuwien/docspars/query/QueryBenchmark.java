package at.ac.tuwien.docspars.query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryBenchmark {


  public static void main(String[] args) throws IOException, SQLException {

    final String queryFile = "queryFile.sql";
    final String queryTerms = "queryTerms_num.txt";
    Connection con = null;
    // PreparedStatement pst = null;
    final String queryRaw = org.apache.commons.io.IOUtils.toString(new BufferedReader(new FileReader(queryFile)));
    // make a connection to the MonetDB server using JDBC URL starting with: jdbc:monetdb://
    con = DriverManager.getConnection("jdbc:monetdb://138.201.173.41:50000/wiki?so_timeout=10000000", "monetdb", "prodyna666");
    Statement st = con.createStatement();
    final List<Long> totalMillis = new ArrayList<>();
    // pst = con.prepareStatement("select did, term from termswithnames where did >= ? and did < ?");
    // pst.setQueryTimeout(10000000);
    int queryNumber = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(queryTerms))) {
      for (String line; (line = br.readLine()) != null;) {
        final String query = queryRaw.replaceAll("%QUERY%", line.replaceAll(" ", ","));
        System.out.println(
            "--------------------------------\n Executed Query" + ++queryNumber + " as\n " + query + "\n--------------------------------");
        long newmillis = System.currentTimeMillis();
        st.executeQuery(query);
        long executeMillis = System.currentTimeMillis() - newmillis;
        totalMillis.add(executeMillis);
        System.out
            .println("--------------------------------\n in seconds-> " + executeMillis / 1000 + "\n--------------------------------");
      }
    }
    System.out.println("Total time elapsed [seconds]:" + totalMillis.stream().collect(Collectors.summingLong(Long::longValue)));
    totalMillis.stream().forEach(elem -> {
      System.out.println(elem);
    });
  }
}


