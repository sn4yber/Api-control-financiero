package com.controfinanciero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 🚀 API de Control Financiero Personal
 *
 * Funcionalidades Elite:
 * - 🤖 Movimientos Recurrentes Automáticos
 * - 🚨 Alertas de Presupuesto en Tiempo Real
 * - 📧 Resúmenes Mensuales por Email
 * - 📄 Exportación a PDF/Excel Profesional
 * - 🧠 Predicción de Gastos con IA
 * - 🚨 Detección de Anomalías Financieras
 * - 📜 Auditoría Completa de Cambios
 * - 🔒 Seguridad Avanzada con Rate Limiting
 * - 📊 Dashboard con Métricas en Tiempo Real
 */
@SpringBootApplication
@EnableScheduling  // Habilita tareas programadas (Cron Jobs)
@EnableAsync       // Habilita procesamiento asíncrono
public class ControlFinacieroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControlFinacieroApplication.class, args);
	}

}
