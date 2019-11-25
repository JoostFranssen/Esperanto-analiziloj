package nl.sogyo.esperanto.servilo;

import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * La servilo, per kiu oni povas konekti al la aplikaĵo je konektinterfaco 8090
 * @author jfranssen
 *
 */
public class AnalizilServer {

	public static void main(String[] args) {
		new AnalizilServer();
	}
		
	private AnalizilServer() {
		Server server = new Server(8090);
		
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		
		contextHandler.setContextPath("/");
		server.setHandler(contextHandler);
		
		ServletHolder dynamicServletHolder = contextHandler.addServlet(ServletContainer.class, "/api/*");
		dynamicServletHolder.setInitOrder(1);
		dynamicServletHolder.setInitParameter("jersey.config.server.provider.packages", "nl.sogyo.esperanto.servilo");
		
		ServletHolder staticServletHolder = contextHandler.addServlet(DefaultServlet.class, "/*");
		
		URL website = getClass().getClassLoader().getResource("retejo");
		staticServletHolder.setInitParameter("resourceBase", website.toExternalForm());
		
		try {
			server.start();
			server.join();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			server.destroy();
		}
	}
}
