package org.mxhero.console.backend;

import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.flex.remoting.RemotingInclude;
import org.springframework.stereotype.Service;

@Service
@RemotingDestination(channels={"flex-amf","secure-flex-amf"})
public class SayHello {

	@RemotingInclude
	public String say(){
		System.out.println("Hello");
		return "Hello";
	}
	
}
