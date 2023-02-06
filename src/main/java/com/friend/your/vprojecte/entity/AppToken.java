package com.friend.your.vprojecte.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "tokens")
public class AppToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tokenCode;
    private LocalDateTime expiresAt;
    private boolean activated;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tokens_inactive_accounts",
            joinColumns = @JoinColumn(name = "token_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private InactiveAccount tokenAccount;

    public AppToken(String tokenCode, LocalDateTime expiresAt, InactiveAccount tokenAccount) {
        this.tokenCode = tokenCode;
        this.expiresAt = expiresAt;
        this.tokenAccount = tokenAccount;
    }
}
