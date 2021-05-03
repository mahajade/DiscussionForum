package ca.sheridancollege.mahajade.beans;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable{

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	
}
