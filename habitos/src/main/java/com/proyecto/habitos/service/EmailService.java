package com.proyecto.habitos.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreoBienvenida(String correoDestino, String nombreUsuario) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(correoDestino);
            helper.setSubject("¡Bienvenido al Sistema de Seguimiento de Hábitos!");

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

            helper.setText(contenidoHtml, true);
            mailSender.send(message);
            System.out.println("Correo enviado con éxito a: " + correoDestino);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al ajustar o enviar el correo electrónico: " + e.getMessage());
        }
    }

    public void enviarCorreoDinamico(String destinatario, String asunto, String mensaje) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto); // Asunto dinámico desde la API

            // Estructura HTML que envuelve el mensaje que tú escribas en Postman
            String contenidoHtml = "<html>" +
                    "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                    "<div style='max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px;'>" +
                    "<h2 style='color: #4F46E5;'>Notificación de la API</h2>" +
                    "<p>" + mensaje + "</p>" + // Mensaje dinámico desde la API
                    "<br>" +
                    "<p style='font-size: 12px; color: #777;'>Este correo fue enviado mediante pruebas de desarrollo.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            helper.setText(contenidoHtml, true);
            mailSender.send(message);
            System.out.println("Correo dinámico enviado con éxito a: " + destinatario);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo dinámico: " + e.getMessage());
        }
    }
}