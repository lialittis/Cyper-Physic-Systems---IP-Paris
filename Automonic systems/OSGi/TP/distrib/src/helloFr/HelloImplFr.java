package helloFr;

import hello.service.HelloService;

public class HelloImplFr implements HelloService{
private static final String MESSAGE_FR = "Bonjour";
	
	@Override
	public String getHelloMessage() {
		return MESSAGE_FR;
	}
}
