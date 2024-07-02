import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CurrencyConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese la moneda de origen (USD, ARS, BRL): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        System.out.println("Ingrese la moneda de destino (USD, ARS, BRL): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        System.out.println("Ingrese el monto a convertir: ");
        double amount = scanner.nextDouble();

        try {
            // Paso 1: Crear la URL
            String apiKey = "371f815f7ecaa5ac2074c64b";
            String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;

            // Paso 2: Crear el cliente HTTP y la solicitud
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .GET()
                    .build();

            // Paso 3: Enviar la solicitud y leer la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Paso 4: Parsear la respuesta JSON usando JsonParser
            String jsonResponse = response.body();
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

            // Paso 5: Acceder a las propiedades específicas
            String result = jsonObject.get("result").getAsString();
            if ("success".equals(result)) {
                JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

                // Obtener la tasa de cambio específica
                double exchangeRate = conversionRates.get(targetCurrency).getAsDouble();

                // Realizar la conversión
                double convertedAmount = amount * exchangeRate;

                System.out.println(amount + " " + baseCurrency + " es igual a " + convertedAmount + " " + targetCurrency);
            } else {
                System.out.println("Error en la solicitud a la API");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}