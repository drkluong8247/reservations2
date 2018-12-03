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
        int insertId = -1;
        boolean success = false;

        // Gets the value from the form
        String mornNight = req.getParameter("AmPm");
        String hour = req.getParameter("hour");
        String minute = req.getParameter("minute");

        String month = req.getParameter("month");
        String day = req.getParameter("day");
        String year = req.getParameter("year");

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
        }
        catch (URISyntaxException uriExc)
        {
            out.println("database connection URI exception");
        }
        catch (SQLException sqlExc)
        {
            out.println("SQL Exception?");
        }

        out.println ("<html>");
        out.println ("<head>");
        out.println ("<title>Reservation successful</title>");
        out.println ("</head>");

        out.println("<body>");
        out.println("<p>");
        out.println("INSERT ID: " + insertId);
        out.println("</p>");

        out.println("<p>");
        out.println("Time is " + hour + ":" + minute + " " + mornNight);
        out.println("</p>");

        out.println("<p>");
        out.println("Date is " + month + "/" + day + "/" + year);
        out.println("</p>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
        out.close();
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }
}
