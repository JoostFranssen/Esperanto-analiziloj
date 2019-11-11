package nl.sogyo.esperanto.servilo;

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
public class Servilo {

	public static void main(String[] args) {
		Server servilo = new Server(8090);
		
		ServletContextHandler kunteksto = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		
		kunteksto.setContextPath("/");
		servilo.setHandler(kunteksto);
		
		ServletHolder dinamikaTenilo = kunteksto.addServlet(ServletContainer.class, "/api/*");
		dinamikaTenilo.setInitOrder(1);
		dinamikaTenilo.setInitParameter("jersey.config.server.provider.packages", "nl.sogyo.esperanto.servilo");
		
		ServletHolder statikaTenilo = kunteksto.addServlet(DefaultServlet.class, "/*");
		statikaTenilo.setInitParameter("resourceBase", "./src/main/resources/retejo/");
		
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
