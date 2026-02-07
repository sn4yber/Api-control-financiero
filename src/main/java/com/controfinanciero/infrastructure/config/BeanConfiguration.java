package com.controfinanciero.infrastructure.config;

import com.controfinanciero.application.usecase.Meta.CrearMetaFinancieraUseCase;
import com.controfinanciero.application.usecase.Meta.ObtenerMetasFinancierasUseCase;
import com.controfinanciero.application.usecase.movimiento.CrearMovimientoFinancieroUseCase;
import com.controfinanciero.application.usecase.movimiento.ObtenerMovimientosFinancierosUseCase;
import com.controfinanciero.domain.repository.MetaFinancieraRepository;
import com.controfinanciero.application.usecase.categoria.CrearCategoriaUseCase;
import com.controfinanciero.application.usecase.categoria.ObtenerCategoriasUseCase;
import com.controfinanciero.application.usecase.contexto.CrearContextoFinancieroUseCase;
import com.controfinanciero.application.usecase.contexto.ObtenerContextoFinancieroUseCase;
import com.controfinanciero.application.usecase.fuente.CrearFuenteIngresoUseCase;
import com.controfinanciero.application.usecase.fuente.ObtenerFuentesIngresoUseCase;
import com.controfinanciero.domain.repository.CategoriaRepository;
import com.controfinanciero.domain.repository.ContextoFinancieroRepository;
import com.controfinanciero.domain.repository.FuenteIngresoRepository;
import com.controfinanciero.domain.repository.MovimientoFinancieroRepository;
import com.controfinanciero.domain.repository.UsuarioRepository;
import com.controfinanciero.domain.service.CalculadorProgresoMeta;
import com.controfinanciero.domain.service.CalculadorSaldo;
import com.controfinanciero.domain.service.GeneradorResumen;
import com.controfinanciero.infrastructure.service.BudgetMonitorService;
import com.controfinanciero.infrastructure.service.GoalMonitorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de beans de la aplicación.
 * Define los domain services como beans de Spring.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public CalculadorSaldo calculadorSaldo() {
        return new CalculadorSaldo();
    }

    @Bean
    public CalculadorProgresoMeta calculadorProgresoMeta() {
        return new CalculadorProgresoMeta();
    }

    @Bean
    public GeneradorResumen generadorResumen(CalculadorSaldo calculadorSaldo) {
        return new GeneradorResumen(calculadorSaldo);
    }

    // Use Cases - Contexto Financiero
    @Bean
    public CrearContextoFinancieroUseCase crearContextoFinancieroUseCase(
            ContextoFinancieroRepository contextoRepository,
            UsuarioRepository usuarioRepository
    ) {
        return new CrearContextoFinancieroUseCase(contextoRepository, usuarioRepository);
    }

    @Bean
    public ObtenerContextoFinancieroUseCase obtenerContextoFinancieroUseCase(
            ContextoFinancieroRepository contextoRepository
    ) {
        return new ObtenerContextoFinancieroUseCase(contextoRepository);
    }

    // Use Cases - Categoría
    @Bean
    public CrearCategoriaUseCase crearCategoriaUseCase(
            CategoriaRepository categoriaRepository,
            UsuarioRepository usuarioRepository
    ) {
        return new CrearCategoriaUseCase(categoriaRepository, usuarioRepository);
    }

    @Bean
    public ObtenerCategoriasUseCase obtenerCategoriasUseCase(
            CategoriaRepository categoriaRepository
    ) {
        return new ObtenerCategoriasUseCase(categoriaRepository);
    }

    // Use Cases - Fuente Ingreso
    @Bean
    public CrearFuenteIngresoUseCase crearFuenteIngresoUseCase(
            FuenteIngresoRepository fuenteIngresoRepository,
            UsuarioRepository usuarioRepository
    ) {
        return new CrearFuenteIngresoUseCase(fuenteIngresoRepository, usuarioRepository);
    }

    @Bean
    public ObtenerFuentesIngresoUseCase obtenerFuentesIngresoUseCase(
            FuenteIngresoRepository fuenteIngresoRepository
    ) {
        return new ObtenerFuentesIngresoUseCase(fuenteIngresoRepository);
    }

    // Use Cases - Meta Financiera
    @Bean
    public CrearMetaFinancieraUseCase crearMetaFinancieraUseCase(
            MetaFinancieraRepository metaRepository,
            UsuarioRepository usuarioRepository
    ) {
        return new CrearMetaFinancieraUseCase(metaRepository, usuarioRepository);
    }

    @Bean
    public ObtenerMetasFinancierasUseCase obtenerMetasFinancierasUseCase(
            MetaFinancieraRepository metaRepository
    ) {
        return new ObtenerMetasFinancierasUseCase(metaRepository);
    }

    // Use Cases - Movimiento Financiero
    @Bean
    public CrearMovimientoFinancieroUseCase crearMovimientoFinancieroUseCase(
            MovimientoFinancieroRepository movimientoRepository,
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository,
            FuenteIngresoRepository fuenteIngresoRepository,
            MetaFinancieraRepository metaRepository,
            BudgetMonitorService budgetMonitorService,
            GoalMonitorService goalMonitorService
    ) {
        return new CrearMovimientoFinancieroUseCase(
                movimientoRepository,
                usuarioRepository,
                categoriaRepository,
                fuenteIngresoRepository,
                metaRepository,
                budgetMonitorService,
                goalMonitorService
        );
    }

    @Bean
    public ObtenerMovimientosFinancierosUseCase obtenerMovimientosFinancierosUseCase(
            MovimientoFinancieroRepository movimientoRepository,
            CategoriaRepository categoriaRepository,
            FuenteIngresoRepository fuenteIngresoRepository,
            MetaFinancieraRepository metaRepository
    ) {
        return new ObtenerMovimientosFinancierosUseCase(
                movimientoRepository,
                categoriaRepository,
                fuenteIngresoRepository,
                metaRepository
        );
    }
}

