package iCNC.EntityBeans;

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
public class TblInvoice implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idInvoice;
	private Date duedate;
	private String invoicenumber;
	private BigDecimal sum;
	private TblOrder tblOrder;

    public TblInvoice() {
    }


	@Id
	@SequenceGenerator(name="TBL_INVOICE_IDINVOICE_GENERATOR", sequenceName="TBL_INVOICE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TBL_INVOICE_IDINVOICE_GENERATOR")
	@Column(name="ID_INVOICE")
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


	public String getInvoicenumber() {
		return this.invoicenumber;
	}

	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}


	public BigDecimal getSum() {
		return this.sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}


	//bi-directional many-to-one association to TblOrder
    @ManyToOne
	@JoinColumn(name="FK_ORDER")
	public TblOrder getTblOrder() {
		return this.tblOrder;
	}

	public void setTblOrder(TblOrder tblOrder) {
		this.tblOrder = tblOrder;
	}
	
}