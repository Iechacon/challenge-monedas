package com.example.currencyexchange;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class CurrencyExchange {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/da86143955a7f948ade3b536/latest/USD";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    try {
                        Gson gson = new Gson();
                        ExchangeRateResponse exchangeRateResponse = gson.fromJson(responseBody, ExchangeRateResponse.class);
                        if (exchangeRateResponse == null) {
                            System.err.println("Error: Respuesta inválida.");
                            return;
                        }

                        if (exchangeRateResponse.getConversionRates() == null) {
                            System.err.println("Error: Sin datos de tasas.");
                            return;
                        }

                        while (true) {
                            mostrarMenu();
                            int option = scanner.nextInt();

                            if (option == 14) {
                                System.out.println("Saliendo...");
                                break;
                            }

                            System.out.println("Ingresa el monto de dinero:");
                            double amount = scanner.nextDouble();

                            switch (option) {
                                case 1:
                                    convertCurrency(exchangeRateResponse, "USD", "ARS", amount);
                                    break;
                                case 2:
                                    convertCurrency(exchangeRateResponse, "ARS", "USD", amount);
                                    break;
                                case 3:
                                    convertCurrency(exchangeRateResponse, "USD", "BRL", amount);
                                    break;
                                case 4:
                                    convertCurrency(exchangeRateResponse, "BRL", "USD", amount);
                                    break;
                                case 5:
                                    convertCurrency(exchangeRateResponse, "USD", "COP", amount);
                                    break;
                                case 6:
                                    convertCurrency(exchangeRateResponse, "COP", "USD", amount);
                                    break;
                                case 7:
                                    convertCurrency(exchangeRateResponse, "ARS", "BRL", amount);
                                    break;
                                case 8:
                                    convertCurrency(exchangeRateResponse, "BRL", "ARS", amount);
                                    break;
                                case 9:
                                    convertCurrency(exchangeRateResponse, "ARS", "COP", amount);
                                    break;
                                case 10:
                                    convertCurrency(exchangeRateResponse, "COP", "ARS", amount);
                                    break;
                                case 11:
                                    convertCurrency(exchangeRateResponse, "BRL", "COP", amount);
                                    break;
                                case 12:
                                    convertCurrency(exchangeRateResponse, "COP", "BRL", amount);
                                    break;
                                default:
                                    System.out.println("Opción no válida, por favor intenta de nuevo.");
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        System.err.println("Error al parsear la respuesta JSON: " + e.getMessage());
                    }
                })
                .join();
    }

    public static void mostrarMenu() {
        System.out.println("************************");
        System.out.println("Bienvenido al conversor de monedas!");
        System.out.println("1. Dólar => Peso Argentino");
        System.out.println("2. Peso Argentino => Dólar");
        System.out.println("3. Dólar => Real brasileño");
        System.out.println("4. Real brasileño => Dólar");
        System.out.println("5. Dólar => Peso Colombiano");
        System.out.println("6. Peso Colombiano => Dólar");
        System.out.println("7. Peso Argentino => Real Brasileño");
        System.out.println("8. Real Brasileño => Peso Argentino");
        System.out.println("9. Peso Argentino => Peso Colombiano");
        System.out.println("10. Peso Colombiano => Peso Argentino");
        System.out.println("11. Real Brasileño => Peso Colombiano");
        System.out.println("12. Peso Colombiano => Real Brasileño");
        System.out.println("13. Salir");
        System.out.println("Elige una opción válida:");
        System.out.println("************************");
    }

    public static void convertCurrency(ExchangeRateResponse exchangeRateResponse, String fromCurrency, String toCurrency, double amount) {
        Map<String, Double> rates = exchangeRateResponse.getConversionRates();
        if (fromCurrency.equals("USD")) {
            Double rate = rates.get(toCurrency);
            if (rate != null) {
                System.out.println("El valor de "+amount + " " + fromCurrency + " es igual a " + (amount * rate) + " " + toCurrency);
            } else {
                System.out.println("Tasa de cambio no disponible para " + toCurrency);
            }
        } else if (toCurrency.equals("USD")) {
            Double rate = rates.get(fromCurrency);
            if (rate != null) {
                System.out.println(amount + " " + fromCurrency + " = " + (amount / rate) + " " + toCurrency);
            } else {
                System.out.println("Tasa de cambio no disponible para " + fromCurrency);
            }
        } else {
            Double fromRate = rates.get(fromCurrency);
            Double toRate = rates.get(toCurrency);
            if (fromRate != null && toRate != null) {
                System.out.println(amount + " " + fromCurrency + " = " + (amount / fromRate * toRate) + " " + toCurrency);
            } else {
                System.out.println("Tasa de cambio no disponible para " + fromCurrency + " o " + toCurrency);
            }
        }
    }
}
