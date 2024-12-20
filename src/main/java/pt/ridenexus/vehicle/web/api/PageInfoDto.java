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
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalElements;
}
