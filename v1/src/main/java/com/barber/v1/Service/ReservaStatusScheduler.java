package com.barber.v1.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.barber.v1.Model.Reserva;
import com.barber.v1.Repository.ReservaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservaStatusScheduler {

  private final ReservaRepository reservaRepo;

  @Scheduled(fixedRate = 60_000)
  public void completarReservasVencidas() {
    LocalDateTime ahora = LocalDateTime.now();

    // Ahora incluyo PENDIENTE y PAGADO
    List<Reserva> vencidas = reservaRepo.findByEstadoInAndFechaHoraBefore(
      List.of(Reserva.Estado.PENDIENTE, Reserva.Estado.PAGADO),
      ahora
    );

    for (Reserva r : vencidas) {
      r.setEstado(Reserva.Estado.COMPLETADA);
    }
    reservaRepo.saveAll(vencidas);
  }
}
