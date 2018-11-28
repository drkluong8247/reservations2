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
            throws ServletException, IOException {
        ServletOutputStream out = resp.getOutputStream();

        // Just testing the database get connection
        try
        {
            Connection c = getConnection();
        }
        catch (URISyntaxException uriExc)
        {
            out.println("database connection URI exception");
        }
        catch (SQLException sqlExc)
        {
            out.println("SQL Exception?");
        }

        String mornNight = req.getParameter("AmPm");
        String hour = req.getParameter("hour");
        String minute = req.getParameter("minute");

        out.println("submit the stuff yo");

        out.println("Time is " + hour + ":" + minute + " " + mornNight);
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
