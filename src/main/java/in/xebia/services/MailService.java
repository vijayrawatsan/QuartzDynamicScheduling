package in.xebia.services;

import org.springframework.stereotype.Service;

@Service
public class MailService {

	public void send(String recipients) {
		System.out.println("Success  " + recipients);
	}

}
