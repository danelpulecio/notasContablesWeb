package co.com.bbva.app.notas.contables.jsf.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IE9CompatablityFixServlet implements Filter {
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		((HttpServletResponse) response).setHeader("X-UA-Compatible", "IE=EmulateIE7"); // or IE=EmulateIE8
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}