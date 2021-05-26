package io.github.dnowo.DoitApp.model.dto;

import io.github.dnowo.DoitApp.model.Job;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobPagesDto {
    private List<Job> jobs;
    private Long totalItems;
    private Long totalPages;
    private Long currentPage;
}
