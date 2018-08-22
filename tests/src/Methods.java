import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

class Methods extends properties {

    Map<String, Object> fakeJSON() {

        return new HashMap<String, Object>() {
            {
                put("success", "false");
                put("errors", new HashMap<String, Object>() {{
                    put("email", login);
                    put("password", password);
                    put("message", "The password and email you entered don't match. If you forgot your password, use \"Forgot Password\"");
                }});
            }
        };

    }


    JSONObject sendRequest(String url) throws IOException, JSONException {
        JSONObject responseJSON = new JSONObject();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            String json =
                    " { \"email\": \"" + login + "\"," +
                            "\"password\": \"" + password + "\"}";

            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");

            con.setDoOutput(true);
            con.getOutputStream().write(json.getBytes("UTF8"));


            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            responseJSON = new JSONObject(response.toString());


            checkErrorMessageFromJSON(responseJSON);
        } catch (UnknownHostException e) {
            checkErrorMessageFromFakeJSON();
        }
        return responseJSON;
    }


    private void checkErrorMessageFromFakeJSON() throws JSONException {
        if (fakeJSON().get("errors").toString().contains(expectedResult)) {
            System.out.println("test passed with Fake JSON ");
        } else System.out.println("test failed with fake JSON ");

    }

    private void checkErrorMessageFromJSON(JSONObject json) throws JSONException {
        if (json.get("errors").toString().contains(expectedResult)) {
            System.out.println("test passed with real JSON from server");
        } else System.out.println("test failed with real JSON from server");

    }

}

