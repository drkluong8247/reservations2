package servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import java.io.*;

@WebServlet(
        name = "SubmitReservationServlet",
        urlPatterns = {"/submit"}
    )
public class SubmitReservationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();
        out.println("submit the stuff yo");
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType("text/html; charset=UTF-8");
        ServletOutputStream out = resp.getOutputStream();
        boolean success = false;

        // Information obtained from database or used later
        int insertId = -1;
        String restaurantName = "";
        String restaurantAddress = "";
        int hourVal = 0;
        int minuteVal = 0;

        // Gets the time from the form
        String mornNight = req.getParameter("AmPm");
        String hour = req.getParameter("hour");
        String minute = req.getParameter("minute");

        // Gets the date from the form
        String month = req.getParameter("month");
        String day = req.getParameter("day");
        String year = req.getParameter("year");

        // Gets the person's name from the form
        String personName = req.getParameter("personName");

        // Gets the number of people in the reservation
        String numPeople = req.getParameter("numPeople");

        // Gets the restaurant from the form
        String restaurantID = req.getParameter("sRestaurantId");

        try
        {
            Connection c = getConnection();
            String queryID = "SELECT max(resId) from reservations";
            PreparedStatement s = c.prepareStatement(queryID);
            ResultSet r = s.executeQuery();
            if(r.next())
            {
                insertId = r.getInt("max") + 1;
            }

            // Converts to integer values for time
            hourVal = Integer.parseInt(hour);
            minuteVal = Integer.parseInt(minute);
            boolean amValue = mornNight.equals("AM");
            if(hourVal == 12)
            {
                if(amValue)
                {
                    hourVal = 0;
                }
            }
            else if(!amValue)
            {
                hourVal += 12;
            }

            // Gets restaurant ID from the form
            int restaurantIDVal = Integer.parseInt(restaurantID);

            // Gets number of people in the reservation
            int numPeopleVal = Integer.parseInt(numPeople);

            String insertStatement = "INSERT INTO reservations (resID, PersonName, restaurantRef, day, hour, minutes, numPeople) VALUES ("
                + insertId + ",'" + personName + "'," + restaurantIDVal + ",'" + formatDate(month, day, year) + "'," + hourVal + ","
                + minuteVal + "," + numPeopleVal + ");";

            System.out.println(insertStatement);

            s = c.prepareStatement(insertStatement);
            if(s.executeUpdate() >= 1)
            {
                System.out.println("success?");
            }

            String queryRestaurant = "SELECT * from restaurants WHERE restaurantID=" + restaurantIDVal;
            s = c.prepareStatement(queryRestaurant);
            r = s.executeQuery();
            if(r.next())
            {
                restaurantName = r.getString("restaurantName");
                restaurantAddress = r.getString("address");
            }
        }
        catch (URISyntaxException uriExc)
        {
            out.println("database connection URI exception");
        }
        catch (SQLException sqlExc)
        {
            out.println("SQL Exception?");
        }

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Reservation successful</title>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/styles.css\">");
        out.println("</head>");

        out.println("<body>");
        out.println("<a href=\"/\">Go back</a>");
        out.println("<p>You have successfully made your reservation!</p>");
        out.println("<p>");
        out.println("Reservation ID:  " + insertId);
        out.println("</p>");

        out.println("<p>");
        out.println("Reservation Time:  " + hour + ":" + minute + " " + mornNight + getAlternateTime(hourVal, minuteVal));
        out.println("</p>");

        out.println("<p>");
        out.println("Reservation Date:  " + month + "/" + day + "/" + year);
        out.println("</p>");

        out.println("<p>");
        out.println("Number of People:  " + numPeople);
        out.println("</p>");

        out.println("<p>");
        out.println("Person Name:  " + personName);
        out.println("</p>");

        out.println("<br>");
        out.println("<p>");
        out.println("Restaurant:  " + restaurantName);
        out.println("</p>");

        out.println("<p>");
        out.println("Restaurant Address:  " + restaurantAddress);
        out.println("</p>");

        out.println("</body>");
        out.println("</html>");
        out.flush();
        out.close();
    }

    private static String getAlternateTime(int hour, int minute)
    {
        // Returns an identifier (noon or midnight) if the specified time matches
        // Otherwise, returns a blank string
        if(minute == 0)
        {
            if (hour == 0)
            {
                return " (midnight)";
            }

            if (hour == 12)
            {
                return " (noon)";
            }
        }

        return "";
    }

    private static String formatDate(String month, String day, String year)
    {
        return month + "/" + day + "/" + year;
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }
}
