package com.cristianagoncalvesproject.ClinicaMedica.service;

import com.cristianagoncalvesproject.ClinicaMedica.model.Agendamento;
import com.cristianagoncalvesproject.ClinicaMedica.repository.AgendamentoRepository;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AgendamentoRegraService {

    private final AgendamentoRepository agendamentoRepository;

    public AgendamentoRegraService(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    public void validar(Agendamento agendamento) {
        LocalDateTime data = agendamento.getData();

        if (!data.isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O agendamento deve ser para uma data futura.");
        }

        DayOfWeek diaDaSemana = data.getDayOfWeek();
        if (diaDaSemana == DayOfWeek.SATURDAY || diaDaSemana == DayOfWeek.SUNDAY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamentos não são permitidos aos finais de semana.");
        }

        if (data.getHour() < 8 || data.getHour() > 17) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O horário de atendimento é das 08h às 17h.");
        }

        Long medicoId = agendamento.getMedico().getId();
        if (agendamentoRepository.existsByMedico_IdAndData(medicoId, data)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O médico já possui um agendamento neste horário.");
        }
    }
}