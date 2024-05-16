package com.auth.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Table(name = "users")
@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String document;

	@Column
	private String password;

	private List<String> roles;

	public UserEntity(String document, String password, List<String> roles) {
		this.document = document;
		this.password = password;
		this.roles = roles;
	}

	
}