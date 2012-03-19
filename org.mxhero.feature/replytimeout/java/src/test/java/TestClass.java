
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.ParseException;

import org.junit.Test;
import org.mxhero.engine.commons.util.HeaderUtils;

public class TestClass {

	@Test
	public void test() throws ParseException{
		String regex = "(?i)\\s*\\[\\s*mxreply\\s+(\\d+)\\s*([dh]|[\\.\\/\\-])\\s*(\\d*)\\s*\\]\\s*";
		String input = " [mxreply 6d]  sdasdasd [mxreply 7d] ";
		Matcher matcher = Pattern.compile(regex).matcher(input);
		if(matcher.find()){
			System.out.println("*"+matcher.group().trim()+"*");
			System.out.println(matcher.group().trim().replaceFirst("\\[\\s*mxreply\\s*", "").replaceFirst("\\s*\\]", ""));
		}
		System.out.println("*"+input.replaceAll(regex, "")+"*");
		System.out.println(Arrays.deepToString(HeaderUtils.parseParameters("replyTimeout=\\\"\\\\\"123123123123\\\\\",\\\\\"en_US\\\\\"\\\"","replyTimeout")));
	}
	
}
