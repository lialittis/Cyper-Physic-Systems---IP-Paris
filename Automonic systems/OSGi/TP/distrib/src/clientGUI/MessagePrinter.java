package clientGUI;

import java.awt.Frame;
import java.awt.Graphics;
import hello.service.HelloService;

public class MessagePrinter extends Frame{

	private static final long serialVersionUID = 1L;

	// The hello service object being used.
    private HelloService myHelloService = null;
    
    private String message = null;
	
	public MessagePrinter(HelloService myHelloService){
		this.myHelloService = myHelloService;
		
		setSize (500, 120);
		setVisible (true);
	}

	public void printMessages(){
		
		int i=0;
		
		while(true){
			try{
				
				i++;
				
				if(null == this.myHelloService){
					message = " .. NO Hello Service available .. ";
				}
				else{
					message = ": " + myHelloService.getHelloMessage();
				}
				
				message = Integer.toString(i).concat(message);
				
				this.repaint();
	            Thread.sleep(1000);
			}
			catch(Exception e){ e.printStackTrace(); }
		}
	}
	
	public void paint (Graphics g) {
		g.drawString (this.message, 25, 50);
	}
	
	public void setMyHelloService(HelloService myHelloService) {
		this.myHelloService = myHelloService;
	}
}
