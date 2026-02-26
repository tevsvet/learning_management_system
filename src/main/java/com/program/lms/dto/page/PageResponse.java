package com.program.lms.dto.page;

import java.util.List;

public record PageResponse<T>(

        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) { }
