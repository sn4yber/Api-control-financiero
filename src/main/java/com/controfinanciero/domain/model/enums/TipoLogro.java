package com.controfinanciero.domain.model.enums;

/**
 * Tipos de logros en el sistema de gamificación
 */
public enum TipoLogro {
    PRIMERA_META("Primera Meta", "Completa tu primera meta financiera", "🏆"),
    RACHA_7_DIAS("Racha de 7 días", "Registra movimientos 7 días consecutivos", "🔥"),
    RACHA_30_DIAS("Racha de 30 días", "Registra movimientos 30 días consecutivos", "🔥🔥"),
    AHORRADOR_NINJA("Ahorrador Ninja", "Cumple 3 metas en un mes", "💰"),
    PRESUPUESTO_MAESTRO("Maestro del Presupuesto", "No excedas ningún presupuesto por 30 días", "📊"),
    META_MILLONARIA("Meta Millonaria", "Alcanza una meta de $1,000,000 o más", "💎"),
    DISCIPLINA_FINANCIERA("Disciplina Financiera", "Registra gastos diariamente por 90 días", "🎯"),
    ANALISTA_PRO("Analista Pro", "Usa análisis de tendencias 10 veces", "📈"),
    INVERSIONISTA("Inversionista", "Mantén un ahorro mensual constante por 6 meses", "💼"),
    CERO_DEUDAS("Cero Deudas", "Salda todas tus deudas", "✨");

    private final String nombre;
    private final String descripcion;
    private final String emoji;

    TipoLogro(String nombre, String descripcion, String emoji) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.emoji = emoji;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEmoji() {
        return emoji;
    }
}

