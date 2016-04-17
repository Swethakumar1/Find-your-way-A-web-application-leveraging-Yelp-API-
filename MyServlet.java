package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.process.PlaceOfInterest;
import com.process.SearchBeanLocal;


/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//@EJB 
	//private SearchYelp searchBean;
	
	//public MyServlet(){
	//	super();
	//}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String find = request.getParameter("find");
         String location = request.getParameter("near");
         String sortBy = request.getParameter("sort");
         String distance = request.getParameter("radius");
         if(find == null || find.isEmpty() || location == null || location.isEmpty())
         {
         	request.setAttribute("errorMessage", "Find & Near cannot be empty");
            request.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(request, response);
         }
        
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try{
			InitialContext ic = new InitialContext();
			SearchBeanLocal searchBean = (SearchBeanLocal) ic.lookup("java:comp/env/ejb/SearchBean");
			List<PlaceOfInterest> placesOfInterest = searchBean.getLocation(find, location, sortBy, distance);
			if(placesOfInterest.size() > 0){
				out.println("<html><body>");
				out.println("<table>");
				out.println("<tr>");
				out.println("<th>" + "Name" + "</th>");
				out.println("<th>" + "Address" + "</th>");
				out.println("<th>" + "Physical Coordinates" + "</th>");
				out.println("<th>" + "Phone Number" + "</th>");
				out.println("<th>" + "Rating" + "</th>");
				out.println("<th>" + "Review Count" + "</th>");
				out.println("</tr>");
				for(PlaceOfInterest placeOfInterest : placesOfInterest){
					out.println("<tr>");
					out.println("<td>" + placeOfInterest.getName() + "</td>");
					out.println("<td>" + placeOfInterest.getAddress() + "</td>");
					out.println("<td>" + placeOfInterest.getCoordinates() + "</td>");
					out.println("<td>" + placeOfInterest.getPhoneNumber() + "</td>");
					out.println("<td>" + placeOfInterest.getRating() + "</td>");
					out.println("<td>" + placeOfInterest.getReviewCount() + "</td>");
					out.println("</tr>");
				}
	            out.println("</table>");
	            out.println("</body></html>");
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{
			out.close();
		}
	}
}
