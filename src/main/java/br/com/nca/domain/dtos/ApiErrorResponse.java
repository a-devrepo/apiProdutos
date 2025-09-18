package br.com.nca.domain.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {

	private LocalDateTime timeStamp;
	private Integer status;
	private String message;
	private List<String> errors;
}
