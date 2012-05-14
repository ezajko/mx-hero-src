
import org.junit.Test;
import org.mxhero.engine.plugin.gsync.internal.gdata.AppsForYourDomainClient;

import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthParameters.OAuthType;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;


public class TestClass {

	@Test
	public void test() throws Exception{
		
		GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
		oauthParameters.setOAuthConsumerKey("1561912741.apps.googleusercontent.com");
		oauthParameters.setOAuthConsumerSecret("onSzwtz9Cfhl_pUIR7zAvJUE");
		oauthParameters.setOAuthType(OAuthType.TWO_LEGGED_OAUTH);

		AppsForYourDomainClient client = new AppsForYourDomainClient(
				oauthParameters, "mxhero.com");
		
		ContactFeed contacs = client.retrieveAllContacts();
		for(ContactEntry contact : contacs.getEntries()){
			System.out.println(contact.getEmailAddresses().get(0).getAddress());
		}
		
		
	}
	
}
