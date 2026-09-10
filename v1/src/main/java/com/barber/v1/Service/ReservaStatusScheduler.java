package com.barber.v1.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.barber.v1.Model.Reserva;
import com.barber.v1.Repository.ReservaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j 
@Service
@RequiredArgsConstructor
public class ReservaStatusScheduler {

    private final ReservaRepository reservaRepo;

    @Scheduled(fixedRate = 60_000) // Se ejecuta cada 1 minuto (60,000 milisegundos)
    @Transactional
    public void completarReservasVencidas() {
        LocalDateTime ahora = LocalDateTime.now();

        // Buscamos las reservas vencidas que estaban pendientes o pagadas
        List<Reserva> vencidas = reservaRepo.findByEstadoInAndFechaHoraBefore(
            List.of(Reserva.Estado.PENDIENTE, Reserva.Estado.PAGADO),
            ahora
        );

        if (!vencidas.isEmpty()) {
            log.info("Proceso automático: Se encontraron {} reservas vencidas para marcar como completadas.", vencidas.size());

            for (Reserva r : vencidas) {
                r.setEstado(Reserva.Estado.COMPLETADA);
            }
            
            reservaRepo.saveAll(vencidas);
            log.info("Proceso automático finalizado con éxito.");
        }
    }
}