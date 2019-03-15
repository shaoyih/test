import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import java.io.IOException;




@WebFilter(filterName="LoginFilter",urlPatterns="/*")

public class loginFilter implements Filter{
	
	
		public void init(FilterConfig f) {
			
		}
		public void destroy() {
			
		}
		
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException {
			
			HttpServletRequest httpRequest=(HttpServletRequest) request;
			HttpServletResponse httpResponse=(HttpServletResponse) response;
			System.out.println("---------------filter------------");
			System.out.println(httpRequest.getRequestURI());
			System.out.println("---------------filter------------");
			if(this.isUrlAllowedWithoutLogin(httpRequest.getRequestURI())) {
				chain.doFilter(request, response);
				return;
				
			}
			String userAgent = httpRequest.getHeader("User-Agent");
	    	if (userAgent != null && userAgent.contains("Android")) {
	    		if(httpRequest.getSession().getAttribute("user")==null) {
	    			JsonObject responseJsonObject = new JsonObject();
	                responseJsonObject.addProperty("status", "notlogin");
	               

	                response.getWriter().write(responseJsonObject.toString());
	    		}
	    		else {
	    			chain.doFilter(request, response);
	    			
	    		}
	    		return;
	    		
	    	}
			if(httpRequest.getSession().getAttribute("user")!=null && httpRequest.getSession().getAttribute("employee")!=null){
				chain.doFilter(request, response);
				return;
			}
			
			else if(httpRequest.getSession().getAttribute("employee")!=null && isDashBoard(httpRequest.getRequestURI())) {
				chain.doFilter(request, response);
				return;
			}
			
			else if(httpRequest.getSession().getAttribute("user")!=null && !isDashBoard(httpRequest.getRequestURI())) {

				
				chain.doFilter(request, response);
				return;
			}
			else {
				System.out.println(httpRequest.getSession().getAttribute("user"));
				String url = ((HttpServletRequest)request).getRequestURL().toString();
				System.out.println("1");
				System.out.println(url);
				
				if(httpRequest.getSession().getAttribute("user")==null) {
					
					if(url.endsWith("Project1/")) {
					httpResponse.sendRedirect("Project1/login.html");
					}
					else {
						httpResponse.sendRedirect("login.html");
					}
				}
					
				else {
					httpResponse.sendRedirect("_dashboard.html");
				}
			}
			
			
			
		}
		private boolean isUrlAllowedWithoutLogin(String URL) {
			String requestURI=URL.toLowerCase();
			return requestURI.endsWith("login.html") || requestURI.endsWith("login.js")
	                || requestURI.endsWith("api/login")||requestURI.endsWith("login1.jpg")||requestURI.endsWith("login.css")
	                || requestURI.endsWith("_dashboard.html")|| requestURI.endsWith("_dashboard.js")
	                ||requestURI.endsWith("api/employee_login");
		}
		private boolean isDashBoard(String URL) {
			String requestURI=URL.toLowerCase();
			return requestURI.contains("dashboard");
		}

}