package client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import clientGUI.MessagePrinter;

import hello.service.HelloService;

public class Activator implements BundleActivator, ServiceListener, Runnable{
	
	private BundleContext myContext = null;
	private ServiceReference myHelloReference = null;
	private HelloService myHelloService = null;
	private MessagePrinter myClientGUI = null;

	private Thread clientThread = null;
	
	private final static String PREFERED_LANGUAGE = "French";//"French";
	
	@Override
	public void start(BundleContext context) throws Exception {
		
		this.myContext = context;
		
		// We synchronize while registering the service listener and
        // performing our initial dictionary service lookup since we
        // don't want to receive service events when looking up the
        // dictionary service, if one exists.
		synchronized (this){
			
			 // Listen for events pertaining to hello services.
			myContext.addServiceListener(this,
                "(&(objectClass=" + HelloService.class.getName() + ")" +
                "(Language=*))");
			
			// Query for all hello service references matching any language.
	        ServiceReference[] refs = context.getServiceReferences(
	            HelloService.class.getName(), "(Language=*)");
	        
	        //TODO
	        // If we found any dictionary services, then just get
			// a reference to the first one so we can use it.
	        if (refs != null)
	        {
	        	ServiceReference ref = this.findServiceWithLanguage(refs, PREFERED_LANGUAGE);
	        	if( null != ref ){
	        		this.myHelloReference = ref;
	        	}
	        	else{
	        		this.myHelloReference = refs[0];
	        	}
	        	
	        	this.myHelloService = (HelloService)context.getService(this.myHelloReference);
	        	
	        }
	        
	        this.clientThread = new Thread(this);
	        clientThread.start();
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
		if(null != this.myClientGUI){ this.myClientGUI.dispose();};
    	this.clientThread = null;
    	
        // NOTE: The service is automatically released.
		
	}

	@Override
    /**
     * Implements ServiceListener.serviceChanged(). Checks
     * to see if the hello service we are using is leaving or tries to get
     * a hello service if we need one.
     * @param event: the fired service event.
    **/
    public synchronized void serviceChanged(ServiceEvent event){
		// If a hello service was registered, see if we
        // need one. If so, get a reference to it.
        if (event.getType() == ServiceEvent.REGISTERED){
            if (null == this.myHelloReference) {
                // Get a reference to the hello service object.
                this.myHelloReference = event.getServiceReference();
                Object helloObj = this.myContext.getService(this.myHelloReference);
                if( null != helloObj )
                this.setMyHelloService( (HelloService)helloObj );
			}
			// @Tianchi: Add one check, if a hello service was registered, see
			// if it is the service preferred, if yes, change the service to it.
			else{

				/** One possible method
				ServiceReference[] refs = null;
				try
                {
                    refs = this.myContext.getServiceReferences(
                        HelloService.class.getName(), "(Language=*)");
                }
                catch (InvalidSyntaxException ex){
                    // This will never happen.
                }
				ServiceReference ref = this.findServiceWithLanguage(refs, PREFERED_LANGUAGE);
				if(event.getServiceReference() == ref)
				{
					this.myHelloReference = event.getServiceReference();
					Object helloObj = this.myContext.getService(this.myHelloReference);
                	if( null != helloObj )
                	this.setMyHelloService( (HelloService)helloObj );
				}
				 */

				ServiceReference myHelloServiceReference = event.getServiceReference();
				if(myHelloServiceReference.getProperty("Language").equals(PREFERED_LANGUAGE))
				{
					this.myHelloReference = event.getServiceReference();
					Object helloObj = this.myContext.getService(this.myHelloReference);
                	if( null != helloObj )
                	this.setMyHelloService( (HelloService)helloObj );
				}
				
			}
		}
		

        // If a hello service was unregistered, see if it
        // was the one we were using. If so, unget the service
        // and try to query to get another one.
        else if (event.getType() == ServiceEvent.UNREGISTERING){
            if (event.getServiceReference() == this.myHelloReference){
            	
                // Unget service object and null references.
            	this.myContext.ungetService(this.myHelloReference);
                this.myHelloReference = null;
                this.setMyHelloService(null);

                // Query to see if we can get another service.
                ServiceReference[] refs = null;
                try
                {
                    refs = this.myContext.getServiceReferences(
                        HelloService.class.getName(), "(Language=*)");
                }
                catch (InvalidSyntaxException ex){
                    // This will never happen.
                }
                if (refs != null)
                {
                    // Get a reference to the first service object.
                    this.myHelloReference = refs[0];
                    this.setMyHelloService( (HelloService)this.myContext.getService(this.myHelloReference) );
                    
                }
            }
        }
		
	}
	
	private ServiceReference findServiceWithLanguage(ServiceReference[] refs, String preferredLanguage){
		
		System.out.println( "client Hello: trying to find Hello Service with preferred language: " + preferredLanguage );
		
		if( null != refs){
			ServiceReference ref = null;
			String language = null;
			for(int i=0; i<refs.length; i++){
				ref = refs[i];
				language = (String)ref.getProperty("Language");
				if(language.equals(preferredLanguage)){
					System.out.println( "client Hello: found Hello Service with preferred language: " + language );
					return ref;
				}
			}
			System.out.println( "client Hello: coul NOT find Hello Service with preferred language => will return null.. " );
			return null;
		}
		System.out.println( "client Hello: cannot find Hello Service " +
				"with preferred language in a NULL list of service references" );
		return null;
		
	}
	
	public void setMyHelloService(HelloService helloService){
		this.myHelloService = helloService;
		if(null != this.myClientGUI) {this.myClientGUI.setMyHelloService(helloService);}
	}

	@Override
	public void run() {
		
		this.myClientGUI = new MessagePrinter(this.myHelloService);
		this.myClientGUI.printMessages();
	}

}
