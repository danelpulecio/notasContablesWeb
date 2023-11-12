package co.com.bbva.app.notas.contables.util;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.Collection;

public class EMailSender {

	private String is_serverName;
	private String is_userName;
	private String is_password;
	private boolean ab_isInitiated;

	public EMailSender() {
		is_serverName = null;
		is_userName = null;
		is_password = null;
		ab_isInitiated = false;
	}

	private boolean init() {
		is_serverName = Messages.getString("EMailSender.MAIL_SERVER"); //$NON-NLS-1$
		is_userName = Messages.getString("EMailSender.USER_NAME"); //$NON-NLS-1$
		is_password = Messages.getString("EMailSender.USER_PASS"); //$NON-NLS-1$
		ab_isInitiated = true;

		return ab_isInitiated;
	}

	public void sendEmail(String as_toAccount, String as_senderAccount, String as_subject, String as_body) throws EmailException {
		if (!as_toAccount.equals("") && !as_senderAccount.equals("")) {
			if (init()) {
				Collection<InternetAddress> lc_toAddress = getToAddresses(as_toAccount);
				SimpleEmail lse_email = new SimpleEmail();
				lse_email.setHostName(is_serverName);
				lse_email.setAuthentication(is_userName, is_password);
				lse_email.setTo(lc_toAddress);
				lse_email.setFrom(as_senderAccount);
				lse_email.setSubject(as_subject);
				lse_email.setMsg(as_body);
				lse_email.setCharset("UTF-8");
				lse_email.send();
			}
		}
	}

	public void sendEmailWithAttachment(String as_toAccount, String as_senderAccount, String as_subject, String as_body, String as_path) {
		if (init()) {
			try {
				Collection<InternetAddress> lc_toAddress = getToAddresses(as_toAccount);

				EmailAttachment lae_email = new EmailAttachment();
				lae_email.setPath(as_path);

				MultiPartEmail lse_email = new MultiPartEmail();
				lse_email.attach(lae_email);
				lse_email.setHostName(is_serverName);
				lse_email.setTo(lc_toAddress);
				lse_email.setFrom(as_senderAccount);
				lse_email.setSubject(as_subject);
				lse_email.setMsg(as_body);
				lse_email.setCharset("UTF-8");
				lse_email.send();
			} catch (EmailException e) {
			}
		}
	}

	private Collection<InternetAddress> getToAddresses(String as_to) {
		ArrayList<InternetAddress> la_toAddresses = new ArrayList<InternetAddress>();
		try {
			InternetAddress lia_address = new InternetAddress(as_to);
			la_toAddresses.add(lia_address);
		} catch (javax.mail.internet.AddressException e) {
		}
		return la_toAddresses;
	}

}