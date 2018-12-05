package servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import java.io.*;

@WebServlet(
        name = "RetrieveRestaurantsServlet",
        urlPatterns = {"/retrieve"}
    )
public class RetrieveRestaurantsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType("text/xml; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Connection c = null;

        out.println("<restaurantlist>");
        try
        {
            c = getConnection();
            String restaurantQuery = "SELECT * FROM restaurants";
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
            System.out.println("Database SQL exception");
        }
        finally
        {
            if(c != null)
            {
                try
                {
                    c.close();
                }
                catch (SQLException e)
                {
                    System.out.println("Database close SQL exception");
                }
            }
        }

        out.println("</restaurantlist>");
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType("text/xml; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<restaurantlist>");
        try
        {
            Connection c = getConnection();
            String restaurantQuery = "SELECT * FROM restaurants";
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

        // Gets opening and closing times
        String openTime = getHumanReadableTime(results.getTime("opentime").toString());
        output += encloseInXml("opentime", openTime);

        String closeTime = getHumanReadableTime(results.getTime("closetime").toString());
        output += encloseInXml("closetime", closeTime);

        // Gets id
        String restaurantID = "" + results.getInt("restaurantID");
        output += encloseInXml("restaurantid", restaurantID);

        output += "</restaurant>";
        out.println(output);
    }

    private String getHumanReadableTime(String time)
    {
        int firstColon = time.indexOf(":");
        int secondColon = time.indexOf(":", firstColon+1);

        String hour = time.substring(0, firstColon);
        String minute = time.substring(firstColon+1, secondColon);

        int hourVal = Integer.parseInt(hour);

        String mornNight = "AM";
        if (hourVal == 0)
        {
            hour = "12";
        }
        else if (hourVal >= 12) {
            mornNight = "PM";
            if(hourVal > 12)
            {
                hourVal -= 12;
                hour = "" + hourVal;
            }
        }

        String result = hour + " : " + minute + " " + mornNight;
        if(hour.equals("12") && minute.equals("00"))
        {
            if(mornNight.equals("AM")) {
                result += " (midnight)";
            }
            else if(mornNight.equals("PM")) {
                result += " (noon)";
            }
        }

        return result;
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
