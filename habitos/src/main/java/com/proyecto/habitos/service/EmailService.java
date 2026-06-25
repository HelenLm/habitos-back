package com.proyecto.habitos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    public void enviarCorreoBienvenida(String correoDestino, String nombreUsuario) {
        String asunto = "¡Bienvenido al Sistema de Seguimiento de Hábitos!";

        // Conservamos exactamente tu diseño HTML original
        String contenidoHtml = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px;'>" +
                "<h2 style='color: #4F46E5;'>¡Hola, " + nombreUsuario + "! ✨</h2>" +
                "<p>Nos da muchísimo gusto darte la bienvenida a tu nueva herramienta de crecimiento personal.</p>" +
                "<p>A partir de hoy, podrás registrar tus hábitos diarios, definir metas claras y evaluar tu progreso en tiempo real.</p>" +
                "<br>" +
                "<p style='font-size: 12px; color: #777;'>Este es un correo automático generado por Hábitos.</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        enviarPeticionBrevo(correoDestino, asunto, contenidoHtml);
    }

    public void enviarCorreoDinamico(String destinatario, String asunto, String mensaje) {
        // Conservamos exactamente tu diseño HTML original
        String contenidoHtml = "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px;'>" +
                "<h2 style='color: #4F46E5;'>Notificación de la API</h2>" +
                "<p>" + mensaje + "</p>" +
                "<br>" +
                "<p style='font-size: 12px; color: #777;'>Este correo fue enviado mediante pruebas de desarrollo.</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        enviarPeticionBrevo(destinatario, asunto, contenidoHtml);
    }

    // --- MOTOR INTERNO DE ENVÍO (API BREVO) ---
    private void enviarPeticionBrevo(String destinatario, String asunto, String contenidoHtml) {
        try {
            // Escapamos comillas dobles por si el HTML las contiene, para no romper el JSON
            String htmlEscapado = contenidoHtml.replace("\"", "\\\"");

            // Construimos el JSON con los datos del correo
            String jsonBody = """
            {
                "sender": { "name": "Hábitos App", "email": "deiiviid622@gmail.com" },
                "to": [ { "email": "%s" } ],
                "subject": "%s",
                "htmlContent": "%s"
            }
            """.formatted(destinatario, asunto, htmlEscapado);

            // Creamos el cliente HTTP nativo
            HttpClient client = HttpClient.newHttpClient();

            // Armamos la petición dirigida a la API de Brevo
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", apiKey)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Enviamos la petición
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificamos si se envió correctamente (Código 201 significa "Creado/Enviado")
            if (response.statusCode() == 201) {
                System.out.println("Correo enviado con éxito a: " + destinatario);
            } else {
                System.err.println("Error al enviar correo (Código " + response.statusCode() + "): " + response.body());
            }

        } catch (Exception e) {
            System.err.println("Excepción al intentar enviar el correo vía HTTP: " + e.getMessage());
        }
    }
}