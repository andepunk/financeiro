package br.com.sisnema.financeiroweb.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
	

	private static final String username = "alunosisnema";
	private static final String password = "sisnema123";

	public boolean enviarEmail(String de, String para, String assunto, String mensagem) throws UtilException{
		try {

			Session session = obterSessaoEmail();
			session.setDebug(true);
			
			String user = session.getProperty("mail.smtp.user");
			String pass = session.getProperty("mail.smtp.password");
			String smtp = session.getProperty("mail.smtp.host");
			
			if(para == null){
				para = username +"@gmail.com";
			}
			
			MimeMessage email = new MimeMessage(session);
			email.setFrom(new InternetAddress(de));
			email.setRecipient(Message.RecipientType.TO, new InternetAddress(para));
			email.setSubject(assunto);
			email.setContent(mensagem, "text/plain");
			email.setSentDate(new Date());

			Transport envio = session.getTransport("smtp");
			envio.connect(smtp, user, pass);
			
			email.saveChanges(); 
			envio.sendMessage(email, email.getAllRecipients());
			envio.close();

			return true;
			
		} catch (AddressException e) {
			throw new UtilException("Erro ao montar mensagem de e-mail! Erro: " + e.getMessage());

		} catch (Exception e) {
			throw new UtilException("Erro ao enviar e-mail! Erro: " + e.getMessage());
		}
	}

	private Session obterSessaoEmail() {
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		return session;
	}
}
