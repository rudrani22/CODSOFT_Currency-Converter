import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyConverter {
    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Hi! This is my Currency converter.Which currency do you feel like converting today?\nEnter the currency code you want to convert from (e.g., INR): ");
            String baseCur = scanner.nextLine().toUpperCase();

            System.out.println("Enter the currency code you want to convert to (e.g., USD): ");
            String targetCur = scanner.nextLine().toUpperCase();

            System.out.println("Enter the amount you want to convert: ");
            double amnt = scanner.nextDouble();

            String response = sendHttpGETRequest(baseCur);

            if (response != null) {
                JSONObject obj = new JSONObject(response);
                JSONObject data = obj.getJSONObject("data");

                if (data.has(targetCur)) {
                    double exRate = data.getDouble(targetCur);
                    double convertedAmount = amnt * exRate;

                    System.out.println("Converted amount from " + baseCur + " to " + targetCur + ": " + convertedAmount);
                } else {
                    System.out.println("Sorry\nexchange rate not available :(");
                }
            } else {
                System.out.println("Request failed");
            }
        } catch (JSONException e) {
    
            e.printStackTrace();
        }

        System.out.println("Thank you for using my currency converter!");
    }

    private static String sendHttpGETRequest(String baseCurrencyCode) throws IOException {
        String baseUrl = "https://api.freecurrencyapi.com/v1/latest";
        String accessKey = "fca_live_40YBZ60LfMQZdjgkrrzZWu1oB67tZRyCeUQPT5vG";

        String GETURL = baseUrl + "?apikey=" + accessKey + "&base_currency=" + baseCurrencyCode;
        URL url = new URL(GETURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            System.out.println("Failed to fetch data. HTTP error code: " + responseCode);
            return null;
        }
    }
}
