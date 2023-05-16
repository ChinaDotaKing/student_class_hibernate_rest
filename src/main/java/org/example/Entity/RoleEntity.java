package org.example.Entity;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.hibernate.StudentHibernate;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

//
//@Entity
//@Table(name = "roles")
//@Getter
//@Setter
//public class RoleEntity implements Serializable {
//
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 5605260522147928803L;
//
//	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	private int id;
//
//
//	@Column(nullable=false,length=20)
//	private String name;
//
//	@ManyToMany(mappedBy="roles")
//	private Collection<StudentHibernate> users;
//
//
////	@ManyToMany(cascade= { CascadeType.PERSIST },fetch=FetchType.EAGER  )
////	@JoinTable(name="roles_authorities", joinColumns=@JoinColumn
////			(name="roles_id",referencedColumnName="id"),inverseJoinColumns
////			=@JoinColumn(name="authorities_id",referencedColumnName="id"))
////	private Collection<AuthorityHibernate> authorities;
//
//
//	public RoleEntity() {
//
//	}
//
//	public RoleEntity(String name) {
//		// TODO Auto-generated constructor stub
//		this.name=name;
//	}
//
//
//
//
//
//
//}
