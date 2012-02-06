package org.mxhero.features.instantalias;

import org.junit.Test;

public class TestSubstring {

	@Test
	public void test(){
		String mail="mmarmol+ama+zo+n@mxhero.com";
		int separatorInit=mail.indexOf("+");
		int aliasEnd = mail.indexOf("@");
		String realEmail=mail.substring(0, separatorInit).toString()
				+mail.substring(aliasEnd).toString();
		System.out.println(realEmail);
		System.out.println("org.mxhero.feature.instantalias+dsasdasdasdads".startsWith("org.mxhero.feature.instantalias"));
	}
	
}
