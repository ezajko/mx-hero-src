
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;

import org.junit.Test;

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
		System.out.println("action="+new ParameterList(";action=replyTimeout;unixtime=1332533032;locale=pt_BR").get("action"));
		System.out.println("unixtime="+new ParameterList(";action=replyTimeout;unixtime=1332533032;locale=pt_BR").get("unixtime"));
		System.out.println("locale="+new ParameterList(";action=replyTimeout;unixtime=1332533032;locale=pt_BR").get("locale"));
		System.out.println("notexists="+new ParameterList(";action=replyTimeout;unixtime=1332533032;locale=pt_BR").get("notexists"));
		System.out.println("replyTimeout=\\\"\\\\\"1332446788\\\\\",\\\\\"pt_BR\\\\\"\\\";");
		//System.out.println(Arrays.deepToString(HeaderUtils.parseParameters("replyTimeout=\\\"\\\\\"1332446788\\\\\",\\\\\"pt_BR\\\\\"\\\";","replyTimeout")));
		//System.out.println(Arrays.deepToString(HeaderUtils.parseParameters("replyTimeout=\\\"\\\\\"123123123123\\\\\",\\\\\"en_US\\\\\"\\\"","replyTimeout")));
	}
	
}
