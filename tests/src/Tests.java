import org.json.JSONException;

import java.io.IOException;

class Tests extends Methods {

    void checkResponseFromServer() throws IOException, JSONException {
       // List of test cases
        sendRequest(mainUrl + "api/auth/login");

    }
}
