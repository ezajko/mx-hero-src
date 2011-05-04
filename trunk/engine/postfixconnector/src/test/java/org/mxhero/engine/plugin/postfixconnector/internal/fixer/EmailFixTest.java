package org.mxhero.engine.plugin.postfixconnector.internal.fixer;

import java.util.Arrays;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.junit.Test;

public class EmailFixTest {

	@Test
	public void testValid() throws AddressException{
		FixEmails fixer = new FixEmails();
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("\"Guy Mahoney\" Neal@boutiquesearchfirm.com"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("Guy Mahoney Neal@boutiquesearchfirm.com"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("Guy Mahoney <Neal@boutiquesearchfirm.com>"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("\"Guy Mahoney\" <Neal@boutiquesearchfirm.com>"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("Neal@boutiquesearchfirm.com"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("\"Neal jon\"@boutiquesearchfirm.com"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("\"Neal    jon\"@boutiquesearchfirm.com"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("\"Guy Mahoney\" \"Neal jon\"@boutiquesearchfirm.com"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("\"Neal jon\"@boutiquesearchfirm.com; test@example.org, more@test.com"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("<<Neal@boutiquesearchfirm.com>>"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("\"\"Guy Mahoney\"\" <<Neal@boutiquesearchfirm.com>>"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("\"Extra.com.br\" <news@extra.com.br>"))));
		System.out.println(Arrays.toString(InternetAddress.parse(fixer.parse("katia.venancio@inova.net"))));
	}
	
}
