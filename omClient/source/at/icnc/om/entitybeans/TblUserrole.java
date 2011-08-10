package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the TBL_USERROLE database table.
 * 
 */
@Entity
@Table(name="TBL_USERROLE")
public class TblUserrole extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idUserrole;
	private String descriptionUr;
	private Set<TblUser> tblUsers;

    public TblUserrole() {
    }


	@Id
	@SequenceGenerator(name="TBL_USERROLE_IDUSERROLE_GENERATOR", sequenceName="TBL_USERROLE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_USERROLE_IDUSERROLE_GENERATOR")
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
	
}