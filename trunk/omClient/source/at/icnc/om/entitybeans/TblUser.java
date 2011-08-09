package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the TBL_USER database table.
 * 
 */
@Entity
@Table(name="TBL_USER")
public class TblUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idUser;
	private BigDecimal failedlogins;
	private String password;
	private String username;
	private List<TblCustomer> tblCustomers;
	private List<TblProtocol> tblProtocols;
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


	@Column(precision=22)
	public BigDecimal getFailedlogins() {
		return this.failedlogins;
	}

	public void setFailedlogins(BigDecimal failedlogins) {
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
	public List<TblCustomer> getTblCustomers() {
		return this.tblCustomers;
	}

	public void setTblCustomers(List<TblCustomer> tblCustomers) {
		this.tblCustomers = tblCustomers;
	}
	

	//bi-directional many-to-one association to TblProtocol
	@OneToMany(mappedBy="tblUser")
	public List<TblProtocol> getTblProtocols() {
		return this.tblProtocols;
	}

	public void setTblProtocols(List<TblProtocol> tblProtocols) {
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