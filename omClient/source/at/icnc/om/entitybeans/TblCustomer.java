package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the TBL_CUSTOMER database table.
 * 
 */
@Entity
@Table(name="TBL_CUSTOMER")
public class TblCustomer implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idCustomer;
	private String customername;
	private Date customerstatedate;
	private String sapcnr;
	private TblConcern tblConcern;
	private TblContactperson tblContactperson;
	private TblCustomerstate tblCustomerstate;
	private TblUser tblUser;
	private List<TblOrder> tblOrders;

    public TblCustomer() {
    }


	@Id
	@SequenceGenerator(name="TBL_CUSTOMER_IDCUSTOMER_GENERATOR", sequenceName="TBL_CUSTOMER_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CUSTOMER_IDCUSTOMER_GENERATOR")
	@Column(name="ID_CUSTOMER", unique=true, nullable=false, precision=22)
	public long getIdCustomer() {
		return this.idCustomer;
	}

	public void setIdCustomer(long idCustomer) {
		this.idCustomer = idCustomer;
	}


	@Column(length=45)
	public String getCustomername() {
		return this.customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}


    @Temporal( TemporalType.DATE)
	public Date getCustomerstatedate() {
		return this.customerstatedate;
	}

	public void setCustomerstatedate(Date customerstatedate) {
		this.customerstatedate = customerstatedate;
	}


	@Column(length=45)
	public String getSapcnr() {
		return this.sapcnr;
	}

	public void setSapcnr(String sapcnr) {
		this.sapcnr = sapcnr;
	}


	//bi-directional many-to-one association to TblConcern
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_CONCERN")
	public TblConcern getTblConcern() {
		return this.tblConcern;
	}

	public void setTblConcern(TblConcern tblConcern) {
		this.tblConcern = tblConcern;
	}
	

	//bi-directional many-to-one association to TblContactperson
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_CONTACTPERSON")
	public TblContactperson getTblContactperson() {
		return this.tblContactperson;
	}

	public void setTblContactperson(TblContactperson tblContactperson) {
		this.tblContactperson = tblContactperson;
	}
	

	//bi-directional many-to-one association to TblCustomerstate
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_CUSTOMERSTATE")
	public TblCustomerstate getTblCustomerstate() {
		return this.tblCustomerstate;
	}

	public void setTblCustomerstate(TblCustomerstate tblCustomerstate) {
		this.tblCustomerstate = tblCustomerstate;
	}
	

	//bi-directional many-to-one association to TblUser
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_SALESMAN")
	public TblUser getTblUser() {
		return this.tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}
	

	//bi-directional many-to-one association to TblOrder
	@OneToMany(mappedBy="tblCustomer")
	public List<TblOrder> getTblOrders() {
		return this.tblOrders;
	}

	public void setTblOrders(List<TblOrder> tblOrders) {
		this.tblOrders = tblOrders;
	}
	
}