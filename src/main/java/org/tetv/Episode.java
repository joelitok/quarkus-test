package org.tetv;

import javax.persistence.Entity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Episode extends PanacheEntity{

    public String title;
    public String description;
    
}
