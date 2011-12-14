package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Set;


/**
 * The persistent class for the OMUSERROLE database table.
 * 
 */
@Entity
@Table(name="OMUSERROLE")
public class TblUserrole extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idUserrole;
	private String descriptionUr;
	private Set<TblUser> tblUsers;
	// Variable for version for OptimisticLockException
	private Integer version;

    public TblUserrole() {
    }


	@Id
	@SequenceGenerator(name="OMUSERROLE_IDUSERROLE_GENERATOR", sequenceName="OMUSERROLE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMUSERROLE_IDUSERROLE_GENERATOR")
	@Column(name="ID_USERROLE", unique=true, nullable=false, precision=22)
	public long getIdUserrole() {
		return this.idUserrole;
	}

	public void setIdUserrole(long idUserrole) {
		this.idUserrole = idUserrole;
	}


	@Column(name="DESCRIPTION_UR", length=45)
	public String getDescriptionUr() {
		return this.descriptionUr;
	}

	public void setDescriptionUr(String descriptionUr) {
		this.descriptionUr = descriptionUr;
	}


	//bi-directional many-to-one association to TblUser
	@OneToMany(mappedBy="tblUserrole")
	public Set<TblUser> getTblUsers() {
		return this.tblUsers;
	}

	public void setTblUsers(Set<TblUser> tblUsers) {
		this.tblUsers = tblUsers;
	}
	
	/*
	 * Getter and Setter of Version for OptimisticLockException
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	@Version
	public Integer getVersion() {
		return version;
	}
}