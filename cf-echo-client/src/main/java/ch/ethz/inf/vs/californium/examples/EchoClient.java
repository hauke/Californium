/*******************************************************************************
 * Copyright (c) 2012, Institute for Pervasive Computing, ETH Zurich.
 * Copyright (c) 2013, Hauke Mehrtens <hauke@hauke-m.de>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * 
 * This file is part of the Californium (Cf) CoAP framework.
 ******************************************************************************/
package ch.ethz.inf.vs.californium.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

import ch.ethz.inf.vs.californium.coap.EndpointAddress;
import ch.ethz.inf.vs.californium.layers.DTLSLayer;
import ch.ethz.inf.vs.californium.layers.GenericMessage;
import ch.ethz.inf.vs.californium.layers.GenericMessageFactory;
import ch.ethz.inf.vs.californium.layers.MessageReceiver;

public class EchoClient {
	static class MyMessageReceiver implements MessageReceiver<GenericMessage> {

		@Override
		public void receiveMessage(GenericMessage msg) {
			System.out.println("Received Message: "
					+ new String(msg.toByteArray()));
		}
	}

	/*
	 * Application entry point.
	 */
	public static void main(String args[]) throws IOException,
			InterruptedException {

		DTLSLayer<GenericMessage> dtls = new DTLSLayer<>(new GenericMessageFactory());
		dtls.registerReceiver(new MyMessageReceiver());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			try {
				String line = br.readLine();
				System.out.println("send line: \"" + line + "\"");

				GenericMessage msg = new GenericMessage(line.getBytes());
				msg.setPeerAddress(new EndpointAddress(InetAddress
						.getByName("127.0.0.1"), 5683));
				dtls.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

}
