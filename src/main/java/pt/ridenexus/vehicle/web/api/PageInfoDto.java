package pt.ridenexus.vehicle.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoDto {
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private long numberOfElements;
    private int totalPages;
}
