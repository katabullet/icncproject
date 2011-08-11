package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the OMINVOICESTATE database table.
 * 
 */
@Entity
@Table(name="OMINVOICESTATE")
public class TblInvoicestate extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idInvoicestate;
	private String descriptionIs;
	private Set<TblInvoice> tblInvoices;

    public TblInvoicestate() {
    }


	@Id
	@SequenceGenerator(name="OMINVOICESTATE_IDINVOICESTATE_GENERATOR", sequenceName="OMINVOICESTATE_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMINVOICESTATE_IDINVOICESTATE_GENERATOR")
	@Column(name="ID_INVOICESTATE", unique=true, nullable=false, precision=22)
	public long getIdInvoicestate() {
		return this.idInvoicestate;
	}

	public void setIdInvoicestate(long idInvoicestate) {
		this.idInvoicestate = idInvoicestate;
	}


	@Column(name="DESCRIPTION_IS", length=45)
	public String getDescriptionIs() {
		return this.descriptionIs;
	}

	public void setDescriptionIs(String descriptionIs) {
		this.descriptionIs = descriptionIs;
	}


	//bi-directional many-to-one association to TblInvoice
	@OneToMany(mappedBy="tblInvoicestate")
	public Set<TblInvoice> getTblInvoices() {
		return this.tblInvoices;
	}

	public void setTblInvoices(Set<TblInvoice> tblInvoices) {
		this.tblInvoices = tblInvoices;
	}
	
}