package util.mina.udp.memoryserver.client;

/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.IoBufferAllocator;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Sends its memory usage to the MemoryMonitor server.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class JediAcademy extends IoHandlerAdapter {

	private final static Logger LOGGER = LoggerFactory.getLogger(JediAcademy.class);

	private IoSession session;

	private IoConnector connector;

	/**
	 * Default constructor.
	 */
	public JediAcademy() {

		LOGGER.debug("UDPClient::UDPClient");
		LOGGER.debug("Created a datagram connector");
		
		connector = new NioDatagramConnector();
		
		LOGGER.debug("Setting the handler");
		
		connector.setHandler(this);
		// connector.getFilterChain().addLast("codec", new
		// ProtocolCodecFilter(new
		// TextLineCodecFactory(Charset.forName("UTF-8"))));
		LOGGER.debug("About to connect to the server...");
		
		
		ConnectFuture connFuture = connector.connect(new InetSocketAddress("179.113.253.219", 29071));

		LOGGER.debug("About to wait.");
		connFuture.awaitUninterruptibly();

		LOGGER.debug("Adding a future listener.");
		connFuture.addListener(new IoFutureListener<ConnectFuture>() {
			public void operationComplete(ConnectFuture future) {
				if (future.isConnected()) {
					LOGGER.debug("...connected");
					session = future.getSession();
					try {
						sendData();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					LOGGER.error("Not connected...exiting");
				}
			}
		});
	}
	
	

	private void sendData() throws InterruptedException {
		String theString = "rcon rconbobomg devmap legosw\n";

		byte[] stringBytes = theString.getBytes();

		byte[] allBytes = new byte[stringBytes.length + 4];
		allBytes[0] = (byte) 0xFF;
		allBytes[1] = (byte) 0xFF;
		allBytes[2] = (byte) 0xFF;
		allBytes[3] = (byte) 0xFF;

		byte[] ba = (theString).getBytes();

		System.arraycopy(ba, 0, allBytes, 4, stringBytes.length);
		
		

		for (int iByte = 0; iByte < allBytes.length; iByte++)
			System.out.print((char) allBytes[iByte]);

		IoBuffer buffer = IoBuffer.allocate(allBytes.length);
		buffer.put(allBytes);
		buffer.flip();

		session.write(buffer);
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new InterruptedException(e.getMessage());
		}
		
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("messageReceived");
		IoBuffer buffer = (IoBuffer) message;
		
		String string = new String(buffer.array());
		String[] strings = StringUtils.split(string, "InitGame");
		
		if (strings != null) {
			
			for (String string2 : strings) {
				if (string2 != null) {
					if (!StringUtils.startsWithIgnoreCase(string2, "/") && !string2.contains("print") && !StringUtils.startsWithIgnoreCase(string2, "./")) {
						if (StringUtils.startsWithIgnoreCase(string2, ":")) {
							System.out.println("InitGame"+string2);
						}
					}
					
				}
				
			}
		}
		
		SimpleBufferAllocator allocator = (SimpleBufferAllocator) buffer.getAllocator();
		
		
//		String message1= (String)message;
//		IoBuffer buf = IoBuffer.allocate(message1.length()).setAutoExpand(true);
//		buf.putString(message1, Charset.forName("UTF-8").newEncoder());
//		LOGGER.debug("Session recv...");
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		System.out.println("messageSent");
		IoBuffer buffer = (IoBuffer) message;
		System.out.println(new String(buffer.array()));
		SimpleBufferAllocator allocator = (SimpleBufferAllocator) buffer.getAllocator();
		LOGGER.debug("Message sent...");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LOGGER.debug("Session closed...");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		LOGGER.debug("Session created...");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		LOGGER.debug("Session idle...");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		LOGGER.debug("Session opened...");
	}

	public static void main(String[] args) {
		new JediAcademy();
	}
}
