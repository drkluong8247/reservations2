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
        name = "DeleteReservationServlet",
        urlPatterns = {"/delete"}
    )
public class DeleteReservationServlet extends HttpServlet {

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

            String restaurantQuery = "DELETE FROM reservations WHERE resID=?";
            PreparedStatement s = c.prepareStatement(restaurantQuery);
            s.setInt(1, i);
            if (s.executeUpdate() >= 1)
            {
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
