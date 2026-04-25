package org.vagabond.common.news.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "news", schema = "public")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class NewsEntity extends BaseNewsEntity {

}
