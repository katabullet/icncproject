package iCNC.EntityBeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


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
	private Set<TblCustomer> tblCustomers;

    public TblContactperson() {
    }


	@Id
	@SequenceGenerator(name="TBL_CONTACTPERSON_IDCONTACTPERSON_GENERATOR", sequenceName="TBL_CONTACTPERSON_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CONTACTPERSON_IDCONTACTPERSON_GENERATOR")
	@Column(name="ID_CONTACTPERSON")
	public long getIdContactperson() {
		return this.idContactperson;
	}

	public void setIdContactperson(long idContactperson) {
		this.idContactperson = idContactperson;
	}


	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


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