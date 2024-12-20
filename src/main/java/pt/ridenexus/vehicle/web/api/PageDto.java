package pt.ridenexus.vehicle.web.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
    private List<T> content;
    private PageInfoDto pageInfo;
}
