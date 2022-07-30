package com.pwc.assignment.domain.dto.patient;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AvailableDoctorsResponse {
    private List<AvailableDoctorsResponseDoctor> doctors;
}
