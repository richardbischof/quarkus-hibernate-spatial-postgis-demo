package org.acme.getting.started;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vividsolutions.jts.geom.Point;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Person extends PanacheEntity {

    @Id
    @GeneratedValue
    private Long id;

    public String name;

    @Column(columnDefinition = "geometry(Point,25832)")
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    public Point geom;


}
