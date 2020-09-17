package helloEn;

import hello.service.HelloService;

public class HelloImplEn implements HelloService{

	private static final String MESSAGE_EN = "Hello World";
	
	@Override
	public String getHelloMessage() {
		return MESSAGE_EN;
	}

}
