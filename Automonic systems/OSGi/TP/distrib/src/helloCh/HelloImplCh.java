package helloCh;

import hello.service.HelloService;

public class HelloImplCh implements HelloService{
private static final String MESSAGE_CH = "Ni hao";
	
	@Override
	public String getHelloMessage() {
		return MESSAGE_CH;
	}
}
