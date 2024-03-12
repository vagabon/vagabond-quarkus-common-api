package org.vagabond.common.profile;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.vagabond.engine.auth.entity.BaseProfileEntity;

@Entity
@Table(name = "profile", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ProfileEntity extends BaseProfileEntity {

}
