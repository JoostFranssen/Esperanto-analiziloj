package nl.sogyo.esperanto.servilo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class Servilo {

	public static void main(String[] args) {
		Server servilo = new Server(8090);
		
		ServletContextHandler serviletKuntekstTraktilo = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		
		serviletKuntekstTraktilo.setContextPath("/");
		servilo.setHandler(serviletKuntekstTraktilo);
		
		ServletHolder serviletTenilo = serviletKuntekstTraktilo.addServlet(ServletContainer.class, "/*");
		serviletTenilo.setInitOrder(1);
		serviletTenilo.setInitParameter("jersey.config.server.provider.packages", "nl.sogyo.esperanto.servilo");
		
		try {
			servilo.start();
			servilo.join();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			servilo.destroy();
		}
	}
}
