// package com.barber.v1.Service;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.barber.v1.Model.Asignacion;
// import com.barber.v1.Repository.AsignacionRepository;
// @Service
// public class AsignacionServiceImpl implements AsignacionService {

//      private final AsignacionRepository asignacionRepository;

//     @Autowired
//     public AsignacionServiceImpl(AsignacionRepository asignacionRepository) {
//         this.asignacionRepository = asignacionRepository;
//     }

//     @Override
//     public Asignacion createAsignacion(Asignacion asignacion) {
//         return asignacionRepository.save(asignacion);
//     }

//     @Override
//     public Asignacion updateAsignacion(Long id, Asignacion asignacionActualizada) {
//         return asignacionRepository.findById(id)
//                 .map(existing -> {
//                     existing.setBarbero(asignacionActualizada.getBarbero());
//                     existing.setReserva(asignacionActualizada.getReserva());
//                     return asignacionRepository.save(existing);
//                 })
//                 .orElseThrow(() -> new RuntimeException("Asignación no encontrada con id: " + id));
//     }

//     @Override
//     public void deleteAsignacion(Long id) {
//         asignacionRepository.deleteById(id);
//     }

//     @Override
//     public List<Asignacion> listAsignaciones() {
//         return asignacionRepository.findAll();
//     }

//     @Override
//     public Optional<Asignacion> findById(Long id) {
//         return asignacionRepository.findById(id);
//     }

//     @Override
//     public List<Asignacion> findByBarbero(Long barberoId) {
//         return asignacionRepository.findByBarberoId(barberoId);
//     }

//     @Override
//     public List<Asignacion> findByReserva(Long reservaId) {
//         return asignacionRepository.findByReservaId(reservaId);
//     }

//     @Override
//     public boolean existsAsignacion(Long reservaId, Long barberoId) {
//         return asignacionRepository.existsByReservaIdAndBarberoId(reservaId, barberoId);
//     }



// }
