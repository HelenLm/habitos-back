package com.proyecto.habitos.dto;

public record EmailRequestDTO(
        String destinatario,
        String asunto,
        String mensaje
) {}