import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;



public class recaptchaVerify {
	public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
	public static void verify(String gRecapResponse) throws Exception {
		if (gRecapResponse == null || gRecapResponse.length() == 0) {
		throw new Exception("recaptcha verification failed: gRecaptchaResponse is null or empty");
		}
		URL verifyUrl = new URL(SITE_VERIFY_URL);
		//CONNECT TO URL
		HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();
		
		//requesting header (choose post)
		conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        
        // Data will be sent to the server. scret_key+ response
        String postParams = "secret=" + recaptchaConst.SECRET_KEY + "&response=" + gRecapResponse;
        conn.setDoOutput(true);
        //write to stream then send to server
        OutputStream outStream = conn.getOutputStream();
        outStream.write(postParams.getBytes());
        outStream.flush();
        outStream.close();
        //get response back from server
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode=" + responseCode);
        
        
        // Get the InputStream from Connection to read data sent from the server.
        InputStream inputStream = conn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonObject jsonObject = new Gson().fromJson(inputStreamReader, JsonObject.class);
        inputStreamReader.close();
        
        
        System.out.println("Response: " + jsonObject.toString());
        if (jsonObject.get("success").getAsBoolean()) {
        		// verification succeed
        		return;
        }
        
        throw new Exception("recaptcha verification failed: response is " + jsonObject.toString());
	}
	
	 
}
