package authorize;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


/**
 * This is a single page utility class, that 
 * @author root
 *
 */
public class Authorize {
	
	public static final String	PRODUCTION_URL	= "https://test.authorize.net/gateway/transact.dll";
	public static final String	SANDBOX_URL		= "https://test.authorize.net/gateway/transact.dll";
	
	
	public static Map<String, String> process(String url, Map<String, String> parameters) throws Exception {

		String paramArray[] = new String[parameters.size() * 2];
		int index = 0;
		for (String key : parameters.keySet()) {
			paramArray[index++] = key;
			paramArray[index++] = parameters.get(key);
		}

		String result = post(url, paramArray);
		String values[] = result.split("[|]");
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < Math.min(RESPONSE.length, values.length); i++) {
			map.put(RESPONSE[i], values[i]);
		}

		return map;
	}
	
	public static String mapToString(Map<String, String> map) {
		String result = null;
		for (String key : map.keySet()) {
			String value = map.get(key);

			if (value != null && !value.trim().equals("")) {
				if (result == null) {
					result = key + "=" + value;
				} else {
					result += "\n" + key + "=" + value;
				}
			}

		}

		return result;
	}
	
	public static Map<String, String> map(Map base, String... kvs) {
		Map<String, String> result = new HashMap<String, String>();
		result.putAll(base);
		result.putAll(map(kvs));
		return result;
	}

	public static Map<String, String> map(String... kvs) {
		Map<String, String> result = new HashMap<String, String>();

		String key = null;
		String value = null;
		for (String obj : kvs) {
			if (key == null) {
				key = obj;
			} else {
				value = obj;
				result.put(key, value);
				key = null;
				value = null;
			}
		}

		return result;
	}
	
	
	private static String post(final String url, String... args) throws Exception {		
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		String request = null;
		boolean key = true;
		for (String string : args) {
			if (key) {
				if (request != null) {
					request += "&" + URLEncoder.encode(string, "UTF-8");
				} else {
					request = URLEncoder.encode(string, "UTF-8");
				}

				key = false;
			} else {
				request = request + "=" + URLEncoder.encode(string, "UTF-8");

				key = true;
			}
		}

		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(request);
		wr.flush();
		wr.close();

		InputStream in = connection.getInputStream();
		String result = streamToString(in);
		connection.disconnect();
		return result;

	}
	
	private static String streamToString(InputStream in) throws UnsupportedEncodingException, IOException {
		Charset charset = Charset.forName("UTF8");
		InputStreamReader stream = new InputStreamReader(in, charset);
		BufferedReader reader = new BufferedReader(stream);
		StringBuffer buffer = null;
		String read = "";
		while ((read = reader.readLine()) != null) {
			if (buffer == null) {
				buffer = new StringBuffer(read);
			} else {
				buffer.append("\n").append(read);
			}
		}
		reader.close();
		if (buffer == null) return "";
		return buffer.toString();
	}
	
	
	private static final String	RESPONSE[]	= { 
		"Response Code",
		 "Response Subcode",
		 "Response Reason Code",
		 "Response Reason Text",
		 "Authorization Code",
		 "AVS Response",
		 "Transaction ID",
		 "Invoice Number",
		 "Description",
		 "Amount",
		 "Method",
		 "Transaction Type",
		 "Customer ID",
		 "First Name",
		 "Last Name",
		 "Company",
		 "Address",
		 "City",
		 "State",
		 "ZIP Code",
		 "Country",
		 "Phone",
		 "Fax",
		 "Email Address",
		 "Ship To First Name",
		 "Ship To Last Name",
		 "Ship To Company",
		 "Ship To Address",
		 "Ship To City",
		 "Ship To State",
		 "Ship To ZIP Code",
		 "Ship To Country",
		 "Tax",
		 "Duty",
		 "Freight",
		 "Tax Exempt",
		 "Purchase Order Number",
		 "MD5 Hash",
		 "Card Code Response",
		 "Cardholder Authentication Verification Response", //40
		 "",
		 "",
		 "",
		 "",
		 "",
		 "",
		 "",
		 "",
		 "",
		 "",
		 "Account Number", //51
		 "Card Type",
		 "Split Tender ID",
		 "Requested Amount",
		 "Balance On Card"
	};
}
