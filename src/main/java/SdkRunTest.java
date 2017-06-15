import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * This sample SDK runs on Jetty server integrated in eclipse IDE.
 * It is just to test and see how SAASPASS JS SDK flow works. In your app server, you don't need AppRunTest.
 * @author SAASPASS dev
 *
 */
public class SdkRunTest {
	public static void main(String[] args) throws Exception {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(4000);
		jettyServer.setHandler(context);

		DefaultServlet defaultServlet = new DefaultServlet();
		ServletHolder holderPwd = new ServletHolder("default", defaultServlet);
		holderPwd.setInitParameter("resourceBase", "./src/main/resources/static/");
		context.addServlet(holderPwd, "/*");

		final PackagesResourceConfig prc = new PackagesResourceConfig("myorganization.appserver");
		final Map<String, Object> prcProperties = prc.getProperties();
		prcProperties.put(JSONConfiguration.FEATURE_POJO_MAPPING, true);

		context.addServlet(new ServletHolder(new com.sun.jersey.spi.container.servlet.ServletContainer(prc)), "/api/*");

		try {
			jettyServer.start();
			jettyServer.join();
		} finally {
			jettyServer.destroy();
		}
	}
}
