package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the OMINVOICE database table.
 * 
 */
@Entity
@Table(name="OMINVOICE")
public class TblInvoice extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idInvoice;
	private Date duedate;
	private String invoicenumber;
	private BigDecimal sum;
	private TblInvoicestate tblInvoicestate;
	private TblSettlement tblSettlement;
	private BigDecimal estimation;

    public TblInvoice() {
    }


	@Id
	@SequenceGenerator(name="OMINVOICE_IDINVOICE_GENERATOR", sequenceName="OMINVOICE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMINVOICE_IDINVOICE_GENERATOR")
	@Column(name="ID_INVOICE", unique=true, nullable=false, precision=22)
	public long getIdInvoice() {
		return this.idInvoice;
	}

	public void setIdInvoice(long idInvoice) {
		this.idInvoice = idInvoice;
	}


    @Temporal( TemporalType.DATE)
	public Date getDuedate() {
		return this.duedate;
	}

	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}


	@Column(length=45)
	public String getInvoicenumber() {
		return this.invoicenumber;
	}

	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}


	@Column(precision=22)
	public BigDecimal getSum() {
		return this.sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
	
	@Column(precision=22)
	public BigDecimal getEstimation(){
		return this.estimation;
	}
	
	public void setEstimation(BigDecimal estimation){
		this.estimation = estimation;
	}


	//bi-directional many-to-one association to TblInvoicestate
	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.MERGE})
	@JoinColumn(name="FK_INVOICESTATE")
	public TblInvoicestate getTblInvoicestate() {
		return this.tblInvoicestate;
	}

	public void setTblInvoicestate(TblInvoicestate tblInvoicestate) {
		this.tblInvoicestate = tblInvoicestate;
	}
	

	//bi-directional many-to-one association to TblSettlement
	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.MERGE})
	@JoinColumn(name="FK_SETTLEMENT")
	public TblSettlement getTblSettlement() {
		return this.tblSettlement;
	}

	public void setTblSettlement(TblSettlement tblSettlement) {
		this.tblSettlement = tblSettlement;
	}
	
}