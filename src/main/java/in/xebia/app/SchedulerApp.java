package in.xebia.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SchedulerApp {

	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("applicationContext.xml");
	}

}
