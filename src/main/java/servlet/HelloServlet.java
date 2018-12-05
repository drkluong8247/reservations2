package servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.*;

@WebServlet(
        name = "MyServlet",
        urlPatterns = {"/hello"}
    )
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        ServletOutputStream out = resp.getOutputStream();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Reservation successful</title>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/styles.css\">");
        out.println("</head>");

        out.println("<body>");
        out.println("<a href=\"/\">Go back</a>");
        out.println("<p>Hi! This page is an easter egg ( ). Click the link above to go back and make a reservation.</p>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
        out.close();
    }

}
