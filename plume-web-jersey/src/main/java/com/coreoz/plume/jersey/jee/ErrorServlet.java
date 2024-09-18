package com.coreoz.plume.jersey.jee;

import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
