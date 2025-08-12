package com.aluracursos.screenmatch.dto;

import com.aluracursos.screenmatch.model.Categoria;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record SerieDTO(Long id,
                       String titulo,
                       Integer totalDeTemporadas,
                       Double evaluacion,
                       Categoria genero,
                       String sinopsis,
                       String poster,
                       String actores) {
}
