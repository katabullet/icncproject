package inet.module.mdm.filters;

import java.io.IOException;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.security.auth.Subject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Handles all authentication means for the application.
 * 
 * @author fre80
 * @version $Id: AuthenticationFilter.java 4071 2011-03-03 14:09:00Z fre80 $
 * 
 */
public class AuthenticationFilter implements Filter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// do nothing.

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request,
			final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Subject subject = (Subject) httpRequest.getSession().getAttribute(
				"FILTER_SUBJECT");
		if (subject != null) {
			try {
				Subject.doAs(subject, new PrivilegedExceptionAction<Void>() {

					@Override
					public Void run() throws Exception {
						try {
							chain.doFilter(request, response);
						} catch (Exception e) {
							throw new PrivilegedActionException(e);
						}
						return null;
					}
				});
			} catch (PrivilegedActionException e) {
				throw new ServletException(e);
			}
			return;
		}
		chain.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		// nothing to do.
	}

}
