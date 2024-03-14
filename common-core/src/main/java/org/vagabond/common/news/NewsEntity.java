package org.vagabond.common.news;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "news", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class NewsEntity extends BaseNewsEntity {

}
