package com.cypros.comparator.api.entities;

import com.cypros.comparator.api.enumerate.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data

@Table(name = "notifications")
public class Notifications {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "message")
  private String message;
  @Column(name = "type")
  private NotificationType type;
  @Column(name = "external_id")
  private Long external_id;
  @Column(name = "seen")
  private boolean seen;
  @Column(name = "sent_at")
  private Date sentAt;
  @ManyToOne
  private User sender;
  @ManyToOne
  private User receiver;


}
