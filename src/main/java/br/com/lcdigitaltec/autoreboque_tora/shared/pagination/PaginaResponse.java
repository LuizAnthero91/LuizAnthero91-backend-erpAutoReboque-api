package br.com.lcdigitaltec.autoreboque_tora.shared.pagination;

import org.springframework.data.domain.Page;

import java.util.List;

public record PaginaResponse<T>(

        List<T> content,

        int page,

        int size,

        long totalElements,

        int totalPages,

        boolean first,

        boolean last

) {

    public static <T> PaginaResponse<T> from(Page<T> page) {

        return new PaginaResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}