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

        Connection c = null;

        out.println("<restaurantlist>");
        try
        {
            c = getConnection();

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

        // Gets a price range
        String priceRange = "$" + results.getInt("minprice") + " - $" + results.getInt("maxprice") + " per person";
        output += encloseInXml("pricerange", priceRange);

        // Gets id
        String restaurantID = "" + results.getInt("restaurantID");
        output += encloseInXml("restaurantid", restaurantID);

        // Gets opening and closing hours. Get additional information for selection.
        String openTime = getHumanReadableTime(results.getTime("opentime").toString());
        int openHour = findHour(results.getTime("opentime").toString());
        int openMinute = findMinute(results.getTime("opentime").toString());
        output += encloseInXml("opentime", openTime);
        output += encloseInXml("openhour", openHour + "");
        output += encloseInXml("openminute", openMinute + "");

        String closeTime = getHumanReadableTime(results.getTime("closetime").toString());
        int closeHour = findHour(results.getTime("closetime").toString());
        int closeMinute = findMinute(results.getTime("closetime").toString());
        output += encloseInXml("closetime", closeTime);
        output += encloseInXml("closehour", closeHour + "");
        output += encloseInXml("closeminute", closeMinute + "");

        output += "</restaurant>";
        out.println(output);
    }

    private int findHour(String time)
    {
        int firstColon = time.indexOf(":");
        if(firstColon <= 0)
        {
            firstColon = 1;
        }
        String hour = time.substring(0, firstColon);

        int result = 0;
        try
        {
            result = Integer.parseInt(hour);
        }
        catch (NumberFormatException e)
        {
            // Couldn't find an hour, so return 0
        }

        return result;
    }

    private int findMinute(String time)
    {
        int firstColon = time.indexOf(":");
        int secondColon = time.indexOf(":", firstColon + 1);
        if(firstColon < 0)
        {
            firstColon = 0;
        }
        if(secondColon <= 0)
        {
            secondColon = 1;
        }

        String minute = time.substring(firstColon+1, secondColon);

        int result = 0;
        try
        {
            result = Integer.parseInt(minute);
        }
        catch (NumberFormatException e)
        {
            // Couldn't find a minute value, so return 0
        }

        return result;
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
