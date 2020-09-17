package helloFr;

import hello.service.HelloService;
//import java.util.Properties;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator{

	@Override
	public void start(BundleContext context) throws Exception {
		
		//Properties props = new Properties();
		Hashtable props = new Hashtable();
        	props.put("Language", "French");
        
        	context.registerService(
                	HelloService.class.getName(), new HelloImplFr(), props);
	
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		
		// NOTE: The service is automatically unregistered.
		
	}
	
}
