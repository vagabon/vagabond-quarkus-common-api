package org.vagabond.common.user;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.vagabond.common.profile.ProfileEntity;
import org.vagabond.engine.auth.entity.BaseUserEntity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseUserEntity<ProfileEntity> {

    public String avatar;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_profile", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"))
    public List<ProfileEntity> profiles;

    @Column(name = "activation_token")
    public String activationToken;
    @Column(name = "identity_token")
    public String identityToken;
    @Column(name = "identity_token_date_end")
    public LocalDateTime identityTokenDateEnd;
    @Column(name = "email_activation")
    public Boolean emailActivation;

    @Column(name = "google_id")
    public String googleId;
    @Column(name = "facebook_id")
    public String facebookId;

    @Transient
    public boolean isCreated;
}