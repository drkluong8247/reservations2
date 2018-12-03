package servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import java.io.*;

@WebServlet(
        name = "SelectRestaurantServlet",
        urlPatterns = {"/select"}
    )
public class SelectRestaurantServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        //Don't retrieve anything on a get
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType("text/xml; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        int i = 1;
        String rIndex = req.getParameter("id");
        i = Integer.parseInt(rIndex);

        out.println("<restaurantlist>");
        try
        {
            Connection c = getConnection();

            String restaurantQuery = "SELECT * FROM restaurants WHERE restaurantID=" + i;
            PreparedStatement s = c.prepareStatement(restaurantQuery);
            ResultSet r = s.executeQuery();
            while(r.next())
            {
                addRestaurantResult(r, out);
            }
        }
        catch (URISyntaxException uriExc)
        {
        }
        catch (SQLException sqlExc)
        {
        }

        out.println("</restaurantlist>");
        out.flush();
        out.close();
    }

    private void addRestaurantResult(ResultSet results, PrintWriter out)
        throws SQLException
    {
        String output = "<restaurant>";

        // Gets restaurant name
        output += encloseInXml("name", results.getString("restaurantName"));

        // Gets restaurant rating
        double rating = results.getDouble("rating");
        String rateString = rating + " / 5.0";
        output += encloseInXml("rating", rateString);

        // Gets restaurant food type
        output += encloseInXml("foodtype", results.getString("foodType"));

        // Gets restaurant address
        output += encloseInXml("address", results.getString("address"));

        // Gets id
        String restaurantID = "" + results.getInt("restaurantID");
        output += encloseInXml("restaurantid", restaurantID);

        output += "</restaurant>";
        out.println(output);
    }

    private String encloseInXml(String tag, String value)
    {
        return "<" + tag + ">" + value + "</" + tag + ">";
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }
}
