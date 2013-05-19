/*******************************************************************************
 * Copyright (c) 2012, Institute for Pervasive Computing, ETH Zurich.
 * Copyright (c) 2013, Hauke Mehrtens <hauke@hauke-m.de>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
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

import java.io.IOException;
import java.net.SocketException;

import ch.ethz.inf.vs.californium.layers.DTLSLayer;
import ch.ethz.inf.vs.californium.layers.GenericMessage;
import ch.ethz.inf.vs.californium.layers.GenericMessageFactory;
import ch.ethz.inf.vs.californium.layers.Layer;
import ch.ethz.inf.vs.californium.layers.MessageReceiver;

public class EchoServer {

	static class MyMessageReceiver implements MessageReceiver<GenericMessage> {
		private Layer<GenericMessage> layer;

		public MyMessageReceiver(Layer<GenericMessage> layer) {
			this.layer = layer;
		}

		@Override
		public void receiveMessage(GenericMessage msg) {
			try {
				System.out.println("received Message: "
						+ new String(msg.toByteArray()));
				GenericMessage newMsg = new GenericMessage(msg.toByteArray());
				newMsg.setPeerAddress(msg.getPeerAddress());
				layer.sendMessage(newMsg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/*
	 * Application entry point.
	 */
	public static void main(String[] args) throws SocketException {

		DTLSLayer<GenericMessage> dtls = new DTLSLayer<>(5683, false,
				new GenericMessageFactory());
		dtls.registerReceiver(new MyMessageReceiver(dtls));
	}
}
