package com.proyecto.habitos.service;

import com.proyecto.habitos.model.Usuario;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.Color;

@Service
public class PdfService {

    public ByteArrayInputStream generarReporteUsuario(Usuario usuario) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. Fuentes estilizadas
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(79, 70, 229));
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.GRAY);
            Font fontCeldaHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
            Font fontCeldaDatos = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);

            // 2. Título principal
            Paragraph titulo = new Paragraph("REPORTE DE PERFIL DE USUARIO", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            document.add(titulo);

            // 3. Subtítulo o descripción institucional
            Paragraph subtitulo = new Paragraph("Sistema de Seguimiento de Hábitos Personales - Reporte Oficial", fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(30);
            document.add(subtitulo);

            // 4. Crear Tabla de Datos (2 columnas)
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            // Estilo para los encabezados de la tabla
            PdfPCell cellHeader1 = new PdfPCell(new Phrase("Campo", fontCeldaHeader));
            cellHeader1.setBackgroundColor(new Color(79, 70, 229));
            cellHeader1.setPadding(8);
            cellHeader1.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cellHeader2 = new PdfPCell(new Phrase("Información Registrada", fontCeldaHeader));
            cellHeader2.setBackgroundColor(new Color(79, 70, 229));
            cellHeader2.setPadding(8);
            cellHeader2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(cellHeader1);
            table.addCell(cellHeader2);

            // Insertar Filas de Datos
            table.addCell(new PdfPCell(new Phrase("ID Usuario", fontCeldaDatos)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(usuario.getIdUsuario()), fontCeldaDatos)));

            table.addCell(new PdfPCell(new Phrase("Nombre Completo", fontCeldaDatos)));
            table.addCell(new PdfPCell(new Phrase(usuario.getNombre(), fontCeldaDatos)));

            table.addCell(new PdfPCell(new Phrase("Correo Electrónico", fontCeldaDatos)));
            table.addCell(new PdfPCell(new Phrase(usuario.getCorreo(), fontCeldaDatos)));

            table.addCell(new PdfPCell(new Phrase("Fecha de Registro", fontCeldaDatos)));
            table.addCell(new PdfPCell(new Phrase(usuario.getFechaRegistro().toString(), fontCeldaDatos)));

            // Aplicar un pequeño padding a todas las celdas de datos para que no se vean amontonadas
            for (PdfPCell cell : table.getRows().get(1).getCells()) {
                if (cell != null) cell.setPadding(6);
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al estructurar el documento PDF: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}