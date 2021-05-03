package ca.sheridancollege.mahajade.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Post implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	
	@NonNull private String username;

	@NonNull private String message;
	
	@NonNull @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) private LocalDate date;
	
	@NonNull @DateTimeFormat(iso=DateTimeFormat.ISO.TIME) private LocalTime time;

}
