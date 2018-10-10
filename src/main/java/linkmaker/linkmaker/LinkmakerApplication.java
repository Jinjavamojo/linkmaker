package linkmaker.linkmaker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.smartcardio.CommandAPDU;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@SpringBootApplication
public class LinkmakerApplication /*implements CommandLineRunner*/ {


	private static final String URL = "http://api.timezonedb.com/v2.1/list-time-zone";
	private static final String KEY = "B8DDW1KFOESH";

	public static void main(String[] args) {
		//SpringApplication.run(LinkmakerApplication.class, args);
		LinkmakerApplication.apacheHTTPGetClient();

	}

	protected static void apacheHTTPGetClient() {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(URL);

		try {
			HttpResponse response = client.execute(get);

			// Print out the response message
			System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void apacheHTTPPostClient() {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(URL);

		// Create some NameValuePair for HttpPost parameters
		List<NameValuePair> arguments = new ArrayList<>(3);
//		arguments.add(new BasicNameValuePair("firstName", "System"));
//		arguments.add(new BasicNameValuePair("lastName", "Administrator"));

		try {
			post.setEntity(new UrlEncodedFormEntity(arguments));
			HttpResponse response = client.execute(post);

			// Print out the response message
			//System.out.println(EntityUtils.toString(response.getEntity()));
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			int g = 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void sendGET() throws IOException {
		String GET_URL = "http://api.timezonedb.com/v2.1/list-time-zone";
		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("GET request not worked");
		}

	}
}
