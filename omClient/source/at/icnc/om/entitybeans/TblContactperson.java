package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the OMCONTACTPERSON database table.
 * 
 */
@Entity
@Table(name="OMCONTACTPERSON")
public class TblContactperson extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idContactperson;
	private String email;
	private String firstname;
	private String lastname;
	private String telephone;
	private Set<TblCustomer> tblCustomers;

    public TblContactperson() {
    }


	@Id
	@SequenceGenerator(name="OMCONTACTPERSON_IDCONTACTPERSON_GENERATOR", sequenceName="OMCONTACTPERSON_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMCONTACTPERSON_IDCONTACTPERSON_GENERATOR")
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
	public Set<TblCustomer> getTblCustomers() {
		return this.tblCustomers;
	}

	public void setTblCustomers(Set<TblCustomer> tblCustomers) {
		this.tblCustomers = tblCustomers;
	}
	
}