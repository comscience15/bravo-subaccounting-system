package com.bravo.webapp.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AjaxAuthenticationSuccessHandler implements
		AuthenticationSuccessHandler {
	public final static String DEF_JSON_SUCCESSFUL_MESSAGE = "{\"success\":true}";

	private String jsonSuccessfulMessage = DEF_JSON_SUCCESSFUL_MESSAGE;

	private AuthenticationSuccessHandler defaultSuccessHandler;

	// private ObjectMapper mapper = new ObjectMapper();

	// @Autowired
	// private SecurityContextRepository repository;

	public AjaxAuthenticationSuccessHandler(
			AuthenticationSuccessHandler defaultSuccessHandler) {
		this.defaultSuccessHandler = defaultSuccessHandler;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// save the updated context to the session
		// repository.saveContext(SecurityContextHolder.getContext(), request,
		// response);

//		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			// response.setContentType("application/json");
			// LoginStatus status = new LoginStatus(true, auth.getName());
			// response.getWriter().print(mapper.writeValueAsString(status));
			// response.getWriter().flush();

			System.out.println("XML Http Request:");
			response.setContentType("application/json");
			response.setContentLength(jsonSuccessfulMessage.length());
			PrintWriter pw = response.getWriter();
			pw.print(jsonSuccessfulMessage);
			pw.flush();
//		} else {
//			System.out.println("Default Http Request:");
//			defaultSuccessHandler.onAuthenticationSuccess(request, response,
//					authentication);
//		}

	}

	public String getJsonMessage() {
		return jsonSuccessfulMessage;
	}

	public void setJsonMessage(String jsonMessage) {
		this.jsonSuccessfulMessage = jsonMessage;
	}

}
