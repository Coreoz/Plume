package com.coreoz.plume.db.querydsl.generated;

import javax.annotation.Generated;
import com.querydsl.sql.Column;

/**
 * User is a Querydsl bean type
 */
@Generated("com.coreoz.plume.db.querydsl.generation.IdBeanSerializer")
public class User extends com.coreoz.plume.db.querydsl.crud.CrudEntityQuerydsl {

    @Column("ACTIVE")
    private Boolean active;

    @Column("CREATION_DATE")
    private java.time.LocalDateTime creationDate;

    @Column("ID")
    private Long id;

    @Column("NAME")
    private String name;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public java.time.LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(java.time.LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (id == null) {
            return super.equals(o);
        }
        if (!(o instanceof User)) {
            return false;
        }
        User obj = (User) o;
        return id.equals(obj.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User#" + id;
    }

}

