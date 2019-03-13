package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(nullable = false, unique = true, updatable = false)
	private long id;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false, unique = true) 
	private String username;
	
	@Column(nullable = false, unique = true) 
	private String token;

	@Column(nullable = false, unique = true, updatable = false)
	@CreationTimestamp
	private LocalDateTime creation_date;

	private String birthday_date;

	@Column(nullable = false)
	private UserStatus status;

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getCreation_date(){
		return creation_date;
	}

	public String getBirthday_date() {
		return birthday_date;
	}

	public void setBirthday_date(String birthday_date) {
		this.birthday_date = birthday_date;
	}

	public String getPassword() {
		return password;
	} //change getName to getPassword

	public void setPassword(String password) {
		this.password = password;
	} //change setName to setPassword

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return this.getId().equals(user.getId());
	}
}
