import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
			System.out.println(httpRequest.getRequestURI());
			if(this.isUrlAllowedWithoutLogin(httpRequest.getRequestURI())) {
				chain.doFilter(request, response);
				return;
			}
			if(httpRequest.getSession().getAttribute("user")==null) {
				httpResponse.sendRedirect("login.html");
			}
			else {
				
				chain.doFilter(request, response);
			}
			
			
		}
		private boolean isUrlAllowedWithoutLogin(String URL) {
			URL=URL.toLowerCase();
			return URL.endsWith("login.html")||URL.endsWith("login.js")||URL.endsWith("api/login");
		}

}
