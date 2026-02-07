package com.controfinanciero.infrastructure.service;

import com.controfinanciero.domain.model.Categoria;
import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.model.Usuario;
import com.controfinanciero.domain.repository.CategoriaRepository;
import com.controfinanciero.domain.repository.MovimientoFinancieroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 📊 Servicio de Generación de Excel
 * Exporta datos a hojas de cálculo
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelReportService {

    private final MovimientoFinancieroRepository movimientoRepo;
    private final CategoriaRepository categoriaRepo;

    /**
     * Genera un archivo Excel con los movimientos del periodo
     */
    public byte[] generarReporteExcel(Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            // Hoja de Movimientos
            Sheet sheet = workbook.createSheet("Movimientos");

            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            // Encabezado
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Fecha", "Tipo", "Categoría", "Descripción", "Monto", "Notas"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Obtener datos
            List<MovimientoFinanciero> movimientos = movimientoRepo
                    .findByUsuarioIdAndFechaMovimientoBetween(
                            usuario.getId(), fechaInicio, fechaFin);

            // Obtener categorías para mapeo
            List<Categoria> categorias = categoriaRepo.findByUsuarioId(usuario.getId());
            Map<Long, String> categoriasMap = new HashMap<>();
            for (Categoria cat : categorias) {
                categoriasMap.put(cat.getId(), cat.getNombre());
            }

            // Llenar datos
            int rowNum = 1;
            BigDecimal totalIngresos = BigDecimal.ZERO;
            BigDecimal totalGastos = BigDecimal.ZERO;

            for (MovimientoFinanciero movimiento : movimientos) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(movimiento.getFechaMovimiento().toString());
                row.createCell(1).setCellValue(movimiento.getTipoMovimiento().name());

                String categoriaNombre = movimiento.getCategoriaId() != null
                        ? categoriasMap.getOrDefault(movimiento.getCategoriaId(), "Sin categoría")
                        : "Sin categoría";
                row.createCell(2).setCellValue(categoriaNombre);
                row.createCell(3).setCellValue(movimiento.getDescripcion());

                Cell montoCell = row.createCell(4);
                montoCell.setCellValue(movimiento.getMonto().doubleValue());
                montoCell.setCellStyle(currencyStyle);

                row.createCell(5).setCellValue(movimiento.getNotas() != null ? movimiento.getNotas() : "");

                // Acumular totales
                if (movimiento.getTipoMovimiento().name().equals("INCOME")) {
                    totalIngresos = totalIngresos.add(movimiento.getMonto());
                } else if (movimiento.getTipoMovimiento().name().equals("EXPENSE")) {
                    totalGastos = totalGastos.add(movimiento.getMonto());
                }
            }

            // Fila de totales
            rowNum++;
            Row totalRow = sheet.createRow(rowNum);
            totalRow.createCell(3).setCellValue("TOTAL INGRESOS:");
            Cell totalIngresosCell = totalRow.createCell(4);
            totalIngresosCell.setCellValue(totalIngresos.doubleValue());
            totalIngresosCell.setCellStyle(currencyStyle);

            rowNum++;
            Row totalGastosRow = sheet.createRow(rowNum);
            totalGastosRow.createCell(3).setCellValue("TOTAL GASTOS:");
            Cell totalGastosCell = totalGastosRow.createCell(4);
            totalGastosCell.setCellValue(totalGastos.doubleValue());
            totalGastosCell.setCellStyle(currencyStyle);

            rowNum++;
            Row balanceRow = sheet.createRow(rowNum);
            balanceRow.createCell(3).setCellValue("BALANCE:");
            Cell balanceCell = balanceRow.createCell(4);
            balanceCell.setCellValue(totalIngresos.subtract(totalGastos).doubleValue());
            balanceCell.setCellStyle(currencyStyle);

            // Ajustar anchos de columna
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);

            log.info("✅ Excel generado exitosamente para usuario #{}", usuario.getId());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("❌ Error al generar Excel para usuario #{}: {}", usuario.getId(), e.getMessage(), e);
            throw new RuntimeException("Error al generar Excel", e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$#,##0.00"));
        return style;
    }
}

