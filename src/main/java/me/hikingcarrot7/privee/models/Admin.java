package me.hikingcarrot7.privee.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends PriveeUser {

}
