package org.example.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EventDocumentResponse {

    private List<EventDocument> data;
    private int page;
    private int pageSize;
    private long total;
}
