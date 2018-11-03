package aleroysm.gmail.com.mysqlexample;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // fields
    ItemAdapter itemAdapter;
    Context thisContext;
    ListView myListView;
    TextView progressTextView;
    Map<String, Double> fruitsMap = new LinkedHashMap<String, Double>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Resources res = getResources();
        myListView = (ListView) findViewById(R.id.myListView);
        progressTextView = (TextView) findViewById(R.id.progressTextView);
        thisContext = this;


        progressTextView.setText("");
        Button btn = (Button) findViewById(R.id.getDataButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetData retrieveData = new GetData();
                retrieveData.execute("");
            }
        });


    }

    private class GetData extends AsyncTask<String, String, String> {


        String msg = "";
        // JDBC driver name and database url
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

        // Example: selected port
        static final String DB_URL = "jdbc:mysql://" +
                DatabaseStrings.DATABASE_URL + "/" +
               DatabaseStrings.DATABASE_NAME;

        @Override
        protected void onPreExecute() {
            progressTextView.setText("Connecting to database...");
        }


        @Override
        protected String doInBackground(String... strings) {

          Connection conn = null;
          Statement stmt = null;

          try(

                  Class.forName(JDBC_DRIVER);
                  conn =DriverManager.getConnection( DB_URL, DatabaseStrings.USERNAME, DatabaseStrings.PASSWORD);

                  stmt = conn.CreateStatement();
                  String sql = "SELECT * FROM Contacts_list";
                  ResultSet rs = stmt.executeQuery(sql);

                  while (rs.next()) {
                      String name = rs.getString("name");
                      double price = rs.getDouble("price");

                      fruitsMap.put(name, price);

                  }

                  msg = "Process complete.";

                  rs.close();
                  stmt.close();
                  conn.close();


                  ) catch (SQLException connError) (
                    msg= "An exception was thrown for JDBC.");
                    connError.printStackTrace();
                    ) catch (ClassNotFoundException e) {
                    msg= "A class not found exception was thrown";
                    e.printStackTrace();

            }       finally {
                    try {
                        if (stmt != null) {
                            stmt.close();
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }


            return null;
        }

        @Override
        protected void onPostExecute; (String msg) {

            progressTextView.setText(this.msg);

            if (fruitsMap.size() > 0) {

                itemAdapter = new ItemAdapter (thisContext, fruitsMap);
                myListView.setAdapter(itemAdapter);
            }

        }
    }




}       // I CAN'T DEBUG THIS CODE. I TRIED TO FOLLOW THE TUTORIAL <https://www.youtube.com/watch?v=bu5Y3uZ6LLM&t=406s>, BUT FAILED :( too early maybe...
