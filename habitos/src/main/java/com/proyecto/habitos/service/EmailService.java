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

            // Estructura HTML bonita para tu correo
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

            helper.setText(contenidoHtml, true); // El true indica que procesará etiquetas HTML

            mailSender.send(message);
            System.out.println("Correo enviado con éxito a: " + correoDestino);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al estructurar o enviar el correo electrónico: " + e.getMessage());
        }
    }
}