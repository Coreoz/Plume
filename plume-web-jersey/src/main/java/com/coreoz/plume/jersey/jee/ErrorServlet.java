package com.coreoz.plume.jersey.jee;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
public class ErrorServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try(PrintWriter printWriter = resp.getWriter()) {
			int errorCode = resp.getStatus();
			String errorMessage = HttpStatus.getStatusText(errorCode);
			printWriter.println("Error " + errorCode + " : " + errorMessage);
		} catch (Exception e) {
            logger.error("Error displaying error message for error code {}", resp.getStatus());
        }
	}
}
