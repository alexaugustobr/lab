package util.mina.timeserver.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.example.sumup.Client;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaTimeServerClient extends Client {

	private static final String HOSTNAME = "localhost";

	private static final int PORT = 9123;

	private static final long CONNECT_TIMEOUT = 30 * 1000L; // 30 seconds

	// Set this to false to use object serialization instead of custom codec.
	private static final boolean USE_CUSTOM_CODEC = true;

	public static void main(String[] args) throws Throwable {
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);

//		if (USE_CUSTOM_CODEC) {
//			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new SumUpProtocolCodecFactory(false)));
//		} else {
//			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
//		}
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 2);
		// prepare values to sum up
		int[] values = new int[args.length];
		for (int i = 0; i < args.length; i++) {
			values[i] = Integer.parseInt(args[i]);
		}

		//connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(new TimeClientHandler());
		IoSession session;

		for (;;) {
			try {
				ConnectFuture future = connector.connect(new InetSocketAddress(HOSTNAME, PORT));
				future.awaitUninterruptibly();
				session = future.getSession();
				byte[] ba = ("time\n").getBytes();
				IoBuffer buffer = IoBuffer.allocate(ba.length);
				buffer.put(ba);
				buffer.flip();
				session.write(buffer);
				
				ba = ("test\n").getBytes();
				buffer = IoBuffer.allocate(ba.length);
				buffer.put(ba);
				buffer.flip();
				session.write(buffer);
				if (session.isIdle(IdleStatus.BOTH_IDLE)){
					System.out.println("BOTH_IDLE");
					session.close();
				}
				if (session.isIdle(IdleStatus.READER_IDLE)){
					System.out.println("READER_IDLE");
					session.close();
				}
				if (session.isIdle(IdleStatus.WRITER_IDLE)){
					System.out.println("WRITER_IDLE");
					session.close();
				}
				break;
			} catch (RuntimeIoException e) {
				System.err.println(";Failed to connect.");
				e.printStackTrace();
				Thread.sleep(5000);
			}
		}

		// wait until the summation is done
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
}
