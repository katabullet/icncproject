package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;


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
	private String _sap_cnr_;
	private TblConcern tblConcern;
	private TblContactperson tblContactperson;
	private TblCustomerstate tblCustomerstate;
	private TblUser tblUser;
	private Set<TblOrder> tblOrders;

    public TblCustomer() {
    }


	@Id
	@SequenceGenerator(name="TBL_CUSTOMER_IDCUSTOMER_GENERATOR", sequenceName="TBL_CUSTOMER_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_CUSTOMER_IDCUSTOMER_GENERATOR")
	@Column(name="ID_CUSTOMER")
	public long getIdCustomer() {
		return this.idCustomer;
	}

	public void setIdCustomer(long idCustomer) {
		this.idCustomer = idCustomer;
	}


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


	@Column(name="SAP-CNR")
	public String get_sap_cnr_() {
		return this._sap_cnr_;
	}

	public void set_sap_cnr_(String _sap_cnr_) {
		this._sap_cnr_ = _sap_cnr_;
	}


	//bi-directional many-to-one association to TblConcern
    @ManyToOne
	@JoinColumn(name="FK_CONCERN")
	public TblConcern getTblConcern() {
		return this.tblConcern;
	}

	public void setTblConcern(TblConcern tblConcern) {
		this.tblConcern = tblConcern;
	}
	

	//bi-directional many-to-one association to TblContactperson
    @ManyToOne
	@JoinColumn(name="FK_CONTACTPERSON")
	public TblContactperson getTblContactperson() {
		return this.tblContactperson;
	}

	public void setTblContactperson(TblContactperson tblContactperson) {
		this.tblContactperson = tblContactperson;
	}
	

	//bi-directional many-to-one association to TblCustomerstate
    @ManyToOne
	@JoinColumn(name="FK_CUSTOMERSTATE")
	public TblCustomerstate getTblCustomerstate() {
		return this.tblCustomerstate;
	}

	public void setTblCustomerstate(TblCustomerstate tblCustomerstate) {
		this.tblCustomerstate = tblCustomerstate;
	}
	

	//bi-directional many-to-one association to TblUser
    @ManyToOne
	@JoinColumn(name="FK_SALESMAN")
	public TblUser getTblUser() {
		return this.tblUser;
	}

	public void setTblUser(TblUser tblUser) {
		this.tblUser = tblUser;
	}
	

	//bi-directional many-to-one association to TblOrder
	@OneToMany(mappedBy="tblCustomer")
	public Set<TblOrder> getTblOrders() {
		return this.tblOrders;
	}

	public void setTblOrders(Set<TblOrder> tblOrders) {
		this.tblOrders = tblOrders;
	}
	
}