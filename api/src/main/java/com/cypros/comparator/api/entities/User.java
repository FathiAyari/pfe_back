package com.cypros.comparator.api.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "last_name", nullable = false)
  private String lastName;
  @Column(name = "phone_number")
  private String phoneNumber;
  @Column(name = "email", nullable = false)
  private String email;
  @Column(name = "password")
  private String password;

  @OneToMany(mappedBy = "user")
  private List<FriendRequest> friendRequests;
  @OneToMany(mappedBy = "user")
  private List<LoyaltyCard> loyaltyCards;
  @OneToMany(mappedBy = "owner")
  private List<ShoppingList> ownedShoppingLists;
  @OneToMany(mappedBy = "sharedWith")
  private List<ShoppingList> sharedWithShoppingLists;
  @OneToMany(mappedBy = "user")
  private List<SavedItems> savedItemsList;

  @ManyToMany
  @JoinTable(name = "users_friends", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "friend_id"))
  private List<User> friends;

  @ManyToMany
  @JoinTable(name = "users_friends", joinColumns = @JoinColumn(name = "friend_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> friendsOf;
  @OneToMany(mappedBy = "receiver")
  private List<Notifications> notificationsList;
  @OneToMany(mappedBy = "sender")
  private List<Notifications> SentNotificationsList;
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }
}
