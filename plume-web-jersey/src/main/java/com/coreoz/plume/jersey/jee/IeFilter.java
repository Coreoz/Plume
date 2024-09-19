package com.coreoz.plume.jersey.jee;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Add a <code>X-UA-Compatible: IE=Edge</code> header to each HTTP request
 * to make sure IE is executing with its latest rendering engine
 */
public class IeFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.addHeader("X-UA-Compatible", "IE=Edge");

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// unused
	}

	@Override
	public void destroy() {
		// unused
	}

}
