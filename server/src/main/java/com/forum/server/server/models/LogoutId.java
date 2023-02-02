package com.forum.server.server.models;

import java.io.Serializable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LogoutId implements Serializable{
  private String username;
  private String token;

  public LogoutId(String username, String token) {
    this.username = username;
    this.token = token;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LogoutId other = (LogoutId) obj;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    if (token == null) {
      if (other.token != null)
        return false;
    } else if (!token.equals(other.token))
      return false;
    return true;
  }

  // equals() and hashCode()
}

