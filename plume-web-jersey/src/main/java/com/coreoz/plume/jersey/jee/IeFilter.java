package com.coreoz.plume.jersey.jee;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Ajoute le header X-UA-Compatible:IE=Edge à chaque requête HTTP pour être sûr
 * qu'IE s'execute avec la dernière version de son moteur de rendu
 */
public class IeFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.addHeader("X-UA-Compatible", "IE=Edge");
		
		chain.doFilter(request, response);
	}
	
	// inutile
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}

}
