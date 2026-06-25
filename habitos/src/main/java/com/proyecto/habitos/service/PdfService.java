package com.proyecto.habitos.service;

import com.proyecto.habitos.model.Habito;
import com.proyecto.habitos.model.Usuario;
import com.proyecto.habitos.model.Registro;
import com.proyecto.habitos.repository.RegistroRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.Color;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final RegistroRepository registroRepository;

    public ByteArrayInputStream generarReporteUsuario(Usuario usuario, List<Habito> habitos) {
        Document document = new Document(PageSize.A4.rotate()); // 🎯 Rotamos a Horizontal para que quepa el calendario
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 1. Paleta de Colores Modérna
            Color indigoPrincipal = new Color(79, 70, 229);
            Color verdeCompletado = new Color(34, 197, 94); // Verde tracker
            Color grisVacio = new Color(243, 244, 246);

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, indigoPrincipal);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.GRAY);
            Font fontTextoBlanco = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
            Font fontCeldas = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.BLACK);

            // 2. Encabezado
            Paragraph titulo = new Paragraph("TRACKER MENSUAL DE HÁBITOS", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph subtitulo = new Paragraph("Usuario: " + usuario.getNombre() + "  |  Mes: " + YearMonth.now().getMonth() + " " + YearMonth.now().getYear(), fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20);
            document.add(subtitulo);

            // 3. Configurar dimensiones de la Tabla Calendario
            LocalDate hoy = LocalDate.now();
            int diasEnElMes = YearMonth.now().lengthOfMonth();

            // Columnas: 1 para el nombre del Hábito + número de días del mes actual
            float[] columnasAnchos = new float[1 + diasEnElMes];
            columnasAnchos[0] = 120f; // Espacio ancho para el nombre del hábito
            for (int i = 1; i <= diasEnElMes; i++) {
                columnasAnchos[i] = 20f; // Cuadritos del calendario
            }

            PdfPTable table = new PdfPTable(columnasAnchos);
            table.setWidthPercentage(100);

            // 4. Fila Header: Título "Hábito" y los números de los días (1, 2, 3... 31)
            PdfPCell cellHabitoHeader = new PdfPCell(new Phrase("Mis Hábitos", fontTextoBlanco));
            cellHabitoHeader.setBackgroundColor(indigoPrincipal);
            cellHabitoHeader.setPadding(6);
            cellHabitoHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellHabitoHeader);

            for (int dia = 1; dia <= diasEnElMes; dia++) {
                PdfPCell cellDia = new PdfPCell(new Phrase(String.valueOf(dia), fontTextoBlanco));
                cellDia.setBackgroundColor(indigoPrincipal);
                cellDia.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellDia.setPadding(4);
                table.addCell(cellDia);
            }

            // 5. Llenar la grilla por cada hábito del usuario
            for (Habito habito : habitos) {
                // Celda con el nombre del hábito
                PdfPCell cellNombreHabito = new PdfPCell(new Phrase(habito.getNombre(), fontCeldas));
                cellNombreHabito.setPadding(6);
                table.addCell(cellNombreHabito);

                // Evaluamos cada día del mes para ver si el hábito fue completado
                for (int dia = 1; dia <= diasEnElMes; dia++) {
                    LocalDate fechaEvaluada = LocalDate.of(hoy.getYear(), hoy.getMonth(), dia);

                    // Buscamos si existe registro completado en esa fecha para ese hábito
                    boolean completado = registroRepository
                            .findByHabitoIdHabitoAndFecha(habito.getIdHabito(), fechaEvaluada)
                            .stream()
                            .anyMatch(r -> "COMPLETADO".equalsIgnoreCase(r.getEstado()));

                    PdfPCell cellCuadroDia = new PdfPCell();
                    if (completado) {
                        cellCuadroDia.setBackgroundColor(verdeCompletado); // 🟩 Pintar cuadrito verde
                    } else {
                        cellCuadroDia.setBackgroundColor(grisVacio); // ⬜ Cuadrito vacío si no se hizo
                    }

                    cellCuadroDia.setPadding(4);
                    table.addCell(cellCuadroDia);
                }
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error creando el tracker en PDF: " + e.getMessage());
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}