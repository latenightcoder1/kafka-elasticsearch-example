package org.example.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentSearchRequest {

    private Pagination pagination;
    private List<TimeRange> ranges;
    private List<Sorting> sortings;
    private List<SearchElement> searchElements;
}
