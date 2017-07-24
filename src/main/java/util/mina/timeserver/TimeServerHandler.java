package util.mina.timeserver;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class TimeServerHandler extends IoHandlerAdapter {
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("Client message received: "+message.toString());
		String str = message.toString();
		String response = "";
		
		if (str.trim().equalsIgnoreCase("quit")) {
			session.close();
			return;
		} else if (str.trim().equalsIgnoreCase("time")) {
			Date date = new Date();
			response = date.toString();
		} else {
			response = MessageFormat.format("Command {0} not found on the server", str.trim());
		}
		session.write("response begin");
		session.write(response);
		session.write("response end");
		System.out.println("Message written...");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		//System.out.println("IDLE " + session.getIdleCount(status));
	}
}