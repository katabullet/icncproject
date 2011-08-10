package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the TBL_INVOICE database table.
 * 
 */
@Entity
@Table(name="TBL_INVOICE")
public class TblInvoice extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idInvoice;
	private Date duedate;
	private String invoicenumber;
	private BigDecimal sum;
	private TblInvoicestate tblInvoicestate;
	private TblSettlement tblSettlement;

    public TblInvoice() {
    }


	@Id
	@SequenceGenerator(name="TBL_INVOICE_IDINVOICE_GENERATOR", sequenceName="TBL_INVOICE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_INVOICE_IDINVOICE_GENERATOR")
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


	//bi-directional many-to-one association to TblInvoicestate
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_INVOICESTATE")
	public TblInvoicestate getTblInvoicestate() {
		return this.tblInvoicestate;
	}

	public void setTblInvoicestate(TblInvoicestate tblInvoicestate) {
		this.tblInvoicestate = tblInvoicestate;
	}
	

	//bi-directional many-to-one association to TblSettlement
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_SETTLEMENT")
	public TblSettlement getTblSettlement() {
		return this.tblSettlement;
	}

	public void setTblSettlement(TblSettlement tblSettlement) {
		this.tblSettlement = tblSettlement;
	}
	
}