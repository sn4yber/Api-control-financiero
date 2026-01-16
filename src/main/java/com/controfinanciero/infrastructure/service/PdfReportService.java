package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.model.Categoria;
import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.repository.CategoriaRepository;
import com.controfinanciero.domain.repository.MovimientoFinancieroRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 📄 Servicio de Generación de PDFs
 * Crea reportes profesionales en PDF
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PdfReportService {

    private final MovimientoFinancieroRepository movimientoRepo;
    private final CategoriaRepository categoriaRepo;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Genera un Estado de Cuenta en PDF para un periodo específico
     */
    public byte[] generarEstadoCuenta(Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            Paragraph titulo = new Paragraph("ESTADO DE CUENTA FINANCIERO")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(titulo);

            // Información del usuario
            document.add(new Paragraph(String.format("Usuario: %s", usuario.getNombreCompleto()))
                    .setFontSize(12));
            document.add(new Paragraph(String.format("Email: %s", usuario.getEmail()))
                    .setFontSize(10));
            document.add(new Paragraph(String.format("Periodo: %s - %s",
                    fechaInicio.format(DATE_FORMATTER),
                    fechaFin.format(DATE_FORMATTER)))
                    .setFontSize(10)
                    .setMarginBottom(20));

            // Obtener movimientos
            List<MovimientoFinanciero> movimientos = movimientoRepo
                    .findByUsuarioIdAndFechaMovimientoBetweenOrderByFechaMovimientoDesc(
                            usuario.getId(), fechaInicio, fechaFin);

            // Obtener todas las categorías del usuario para mapeo
            List<Categoria> categorias = categoriaRepo.findByUsuarioId(usuario.getId());
            Map<Long, String> categoriasMap = new HashMap<>();
            for (Categoria cat : categorias) {
                categoriasMap.put(cat.getId(), cat.getNombre());
            }

            // Calcular totales
            BigDecimal totalIngresos = BigDecimal.ZERO;
            BigDecimal totalGastos = BigDecimal.ZERO;

            for (MovimientoFinanciero m : movimientos) {
                if (m.getTipoMovimiento().name().equals("INCOME")) {
                    totalIngresos = totalIngresos.add(m.getMonto());
                } else if (m.getTipoMovimiento().name().equals("EXPENSE")) {
                    totalGastos = totalGastos.add(m.getMonto());
                }
            }

            BigDecimal balance = totalIngresos.subtract(totalGastos);

            // Resumen
            Table resumenTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(20);

            resumenTable.addHeaderCell(createHeaderCell("Total Ingresos"));
            resumenTable.addHeaderCell(createHeaderCell("Total Gastos"));
            resumenTable.addHeaderCell(createHeaderCell("Balance"));

            resumenTable.addCell(createCell("$" + totalIngresos, ColorConstants.GREEN));
            resumenTable.addCell(createCell("$" + totalGastos, ColorConstants.RED));
            resumenTable.addCell(createCell("$" + balance,
                    balance.compareTo(BigDecimal.ZERO) >= 0 ? ColorConstants.GREEN : ColorConstants.RED));

            document.add(resumenTable);

            // Tabla de movimientos
            document.add(new Paragraph("Detalle de Movimientos")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(10));

            Table movimientosTable = new Table(UnitValue.createPercentArray(new float[]{1, 2, 1.5f, 1}))
                    .useAllAvailableWidth();

            movimientosTable.addHeaderCell(createHeaderCell("Fecha"));
            movimientosTable.addHeaderCell(createHeaderCell("Descripción"));
            movimientosTable.addHeaderCell(createHeaderCell("Categoría"));
            movimientosTable.addHeaderCell(createHeaderCell("Monto"));

            for (MovimientoFinanciero movimiento : movimientos) {
                movimientosTable.addCell(createCell(movimiento.getFechaMovimiento().format(DATE_FORMATTER)));
                movimientosTable.addCell(createCell(movimiento.getDescripcion()));

                // Obtener nombre de categoría desde el mapa
                String categoriaNombre = movimiento.getCategoriaId() != null
                        ? categoriasMap.getOrDefault(movimiento.getCategoriaId(), "Sin categoría")
                        : "Sin categoría";
                movimientosTable.addCell(createCell(categoriaNombre));

                String montoStr = (movimiento.getTipoMovimiento().name().equals("INCOME") ? "+" : "-")
                        + "$" + movimiento.getMonto();
                movimientosTable.addCell(createCell(montoStr,
                        movimiento.getTipoMovimiento().name().equals("INCOME")
                                ? ColorConstants.GREEN : ColorConstants.RED));
            }

            document.add(movimientosTable);

            // Pie de página
            document.add(new Paragraph(String.format("Generado el %s",
                    LocalDate.now().format(DATE_FORMATTER)))
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(20));

            document.close();

            log.info("✅ PDF generado exitosamente para usuario #{}", usuario.getId());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("❌ Error al generar PDF para usuario #{}: {}", usuario.getId(), e.getMessage(), e);
            throw new RuntimeException("Error al generar PDF", e);
        }
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell createCell(String text) {
        return new Cell().add(new Paragraph(text));
    }

    private Cell createCell(String text, com.itextpdf.kernel.colors.Color color) {
        return new Cell()
                .add(new Paragraph(text).setFontColor(color))
                .setTextAlignment(TextAlignment.RIGHT);
    }
}

