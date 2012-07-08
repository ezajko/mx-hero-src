import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


public class TestReplace {

	private static final String RECIPIENT_KEY = "mxrecipient";
	private static final String SUBJECT_KEY = "subject";
	private static final String DATE_KEY = "date";
	private String content = "Contract sent to recipient has not been accepted yet: \n\n Recipient: ${mxrecipient} \n Subject: ${Subject} \n Date: ${Date}";
	
	private String replaceTextVars(String subject, String recipient,
			String content, String date) {
		String tagregex = "\\$\\{[^\\{]*\\}";
		Pattern p2 = Pattern.compile(tagregex);
		StringBuffer sb = new StringBuffer();
		Matcher m2 = p2.matcher(content);
		int lastIndex = 0;
		while (m2.find()) {
			lastIndex = m2.end();
			String key = content.substring(m2.start() + 2, m2.end() - 1);
			if (key.equalsIgnoreCase(RECIPIENT_KEY)) {
				m2.appendReplacement(sb, recipient);
			} else if (key.equalsIgnoreCase(SUBJECT_KEY)) {
				m2.appendReplacement(sb, subject);
			} else if (key.equalsIgnoreCase(DATE_KEY)) {
				m2.appendReplacement(sb, date);
			}else{
				m2.appendReplacement(sb,"\\${"+key+"}");
			}
		}
		sb.append(content.substring(lastIndex));
		return sb.toString();
	}
	
	@Test
	public void test(){
		System.out.println(replaceTextVars("Test", "recipient", content, "2012-07-08"));
	}
}
