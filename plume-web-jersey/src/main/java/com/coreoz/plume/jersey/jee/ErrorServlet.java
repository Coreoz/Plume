package com.coreoz.plume.jersey.jee;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorServlet extends HttpServlet {

	private static final long serialVersionUID = 6186826368888103404L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try(PrintWriter printWriter = resp.getWriter()) {
			int errorCode = resp.getStatus();
			String errorMessage = HttpStatus.getStatusText(errorCode);
			printWriter.println("Error " + errorCode + " : " + errorMessage);
		}
	}

}
