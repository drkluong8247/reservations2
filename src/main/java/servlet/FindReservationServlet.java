package servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import java.io.*;
import java.util.ArrayList;

@WebServlet(
        name = "FindReservationServlet",
        urlPatterns = {"/find"}
    )
public class FindReservationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType("text/xml; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<reservationlist>");
        out.println(encloseInXml("result", "false"));
        out.println("</reservationlist>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType("text/xml; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        int i = -1;
        String rIndex = req.getParameter("id");
        boolean success = false;

        try
        {
            i = Integer.parseInt(rIndex);
        }
        catch (NumberFormatException e)
        {
            // Guess the user didn't enter a valid id.
        }

        Connection c = null;

        out.println("<reservationlist>");
        try
        {
            c = getConnection();

            String restaurantQuery = "SELECT * FROM reservations WHERE resID=?";
            PreparedStatement s = c.prepareStatement(restaurantQuery);
            s.setInt(1, i);
            ResultSet r = s.executeQuery();
            while(r.next())
            {
                addReservationResult(r, out);
                success = true;
            }
        }
        catch (URISyntaxException uriExc)
        {
            success = false;
        }
        catch (SQLException sqlExc)
        {
            success = false;
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

        if(success)
        {
            out.println(encloseInXml("result", "true"));
        }
        else
        {
            out.println(encloseInXml("result", "false"));
        }
        out.println("</reservationlist>");

        out.flush();
        out.close();
    }

    private void addReservationResult(ResultSet results, PrintWriter out)
        throws SQLException
    {
        String output = "<reservation>";

        // Gets restaurant name
        output += encloseInXml("resid", results.getString("resId"));

        // Gets person Name
        output += encloseInXml("personname", results.getString("personName"));

        // Gets number of people
        output += encloseInXml("numpeople", results.getInt("numPeople") + "");

        // Gets Date of reservation
        output += encloseInXml("date", formatDate(results.getDate("day").toString()));

        // Gets Time of reservation
        output += encloseInXml("time", formatTime(results.getInt("hour"), results.getInt("minutes")));

        // Need to retrieve restaurant information (another query)
        int restaurantID = results.getInt("restaurantref");
        Connection c = null;
        String restaurantName = "";
        String restaurantAddress = "";

        try
        {
            c = getConnection();

            String restaurantQuery = "SELECT * FROM restaurants WHERE restaurantID=?";
            PreparedStatement s = c.prepareStatement(restaurantQuery);
            s.setInt(1, restaurantID);
            ResultSet r = s.executeQuery();
            if(r.next())
            {
                restaurantName = r.getString("restaurantname");
                restaurantAddress = r.getString("address");
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

        output += encloseInXml("restaurantname", restaurantName);
        output += encloseInXml("address", restaurantAddress);

        output += "</reservation>";
        out.println(output);
    }

    // Reverse translate the Java database string into a date
    private String formatDate(String date)
    {
        int firstDash = date.indexOf("-");
        int secondDash = date.indexOf("-", firstDash+1);

        String year = date.substring(0, firstDash);
        String month = date.substring(firstDash+1, secondDash);
        String day = date.substring(secondDash+1);

        int i = Integer.parseInt(month);
        month = findMonth(i);

        return month + " " + day + ", " + year;
    }

    private String findMonth(int i)
    {
        ArrayList<String> months = new ArrayList<String>();
        months.add("January");
        months.add("January");
        months.add("Feburary");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        int index = 0;
        if (i <= months.size())
        {
            index = i;
        }

        return months.get(i);
    }

    // Reverse translate the Java database string into a date
    private String formatTime(int hour, int minute)
    {
        int hourVal = hour;
        String minuteString = String.format("%02d", minute);

        String mornNight = "AM";
        if (hourVal == 0)
        {
            hourVal = 12;
        }
        else if (hourVal >= 12) {
            mornNight = "PM";
            if(hourVal > 12)
            {
                hourVal -= 12;
            }
        }

        String result = hourVal + " : " + minuteString + " " + mornNight;
        if(hourVal == 12 && minuteString.equals("00"))
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
