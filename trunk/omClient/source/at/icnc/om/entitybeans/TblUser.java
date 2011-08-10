package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the TBL_USER database table.
 * 
 */
@Entity
@Table(name="TBL_USER")
public class TblUser extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idUser;
	private String failedlogins;
	private String password;
	private String username;
	private Set<TblCustomer> tblCustomers;
	private Set<TblProtocol> tblProtocols;
	private TblUserrole tblUserrole;

    public TblUser() {
    }


	@Id
	@SequenceGenerator(name="TBL_USER_IDUSER_GENERATOR", sequenceName="TBL_USER_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_USER_IDUSER_GENERATOR")
	@Column(name="ID_USER", unique=true, nullable=false, precision=22)
	public long getIdUser() {
		return this.idUser;
	}

	public void setIdUser(long idUser) {
		this.idUser = idUser;
	}


	@Column(length=20)
	public String getFailedlogins() {
		return this.failedlogins;
	}

	public void setFailedlogins(String failedlogins) {
		this.failedlogins = failedlogins;
	}


	@Column(length=45)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Column(nullable=false, length=45)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	//bi-directional many-to-one association to TblCustomer
	@OneToMany(mappedBy="tblUser")
	public Set<TblCustomer> getTblCustomers() {
		return this.tblCustomers;
	}

	public void setTblCustomers(Set<TblCustomer> tblCustomers) {
		this.tblCustomers = tblCustomers;
	}
	

	//bi-directional many-to-one association to TblProtocol
	@OneToMany(mappedBy="tblUser")
	public Set<TblProtocol> getTblProtocols() {
		return this.tblProtocols;
	}

	public void setTblProtocols(Set<TblProtocol> tblProtocols) {
		this.tblProtocols = tblProtocols;
	}
	

	//bi-directional many-to-one association to TblUserrole
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_USERROLE")
	public TblUserrole getTblUserrole() {
		return this.tblUserrole;
	}

	public void setTblUserrole(TblUserrole tblUserrole) {
		this.tblUserrole = tblUserrole;
	}
	
}