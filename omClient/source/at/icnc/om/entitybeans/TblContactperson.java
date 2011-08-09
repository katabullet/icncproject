package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the TBL_CONTACTPERSON database table.
 * 
 */
@Entity
@Table(name="TBL_CONTACTPERSON")
public class TblContactperson implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idContactperson;
	private String email;
	private String firstname;
	private String lastname;
	private String telephone;
	private List<TblCustomer> tblCustomers;

    public TblContactperson() {
    }


	@Id
	@SequenceGenerator(name="TBL_CONTACTPERSON_IDCONTACTPERSON_GENERATOR", sequenceName="TBL_CONTACTPERSON_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CONTACTPERSON_IDCONTACTPERSON_GENERATOR")
	@Column(name="ID_CONTACTPERSON", unique=true, nullable=false, precision=22)
	public long getIdContactperson() {
		return this.idContactperson;
	}

	public void setIdContactperson(long idContactperson) {
		this.idContactperson = idContactperson;
	}


	@Column(length=45)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Column(length=45)
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	@Column(length=45)
	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	@Column(length=45)
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	//bi-directional many-to-one association to TblCustomer
	@OneToMany(mappedBy="tblContactperson")
	public List<TblCustomer> getTblCustomers() {
		return this.tblCustomers;
	}

	public void setTblCustomers(List<TblCustomer> tblCustomers) {
		this.tblCustomers = tblCustomers;
	}
	
}