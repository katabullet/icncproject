package at.icnc.om.entitybeans;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the OMDOCUMENTS database table.
 * 
 */
@Entity
@Table(name="OMDOCUMENTS")
public class TblDocument extends Selectable implements Serializable {
	private static final long serialVersionUID = 1L;
	private long idDocument;
	private byte[] document;
	private TblOrder omorder1;
	private TblOrder omorder2;

    public TblDocument() {
    }


	@Id
	@SequenceGenerator(name="OMDOCUMENTS_IDDOCUMENT_GENERATOR", sequenceName="OMDOCUMENTS_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OMDOCUMENTS_IDDOCUMENT_GENERATOR")
	@Column(name="ID_DOCUMENT", unique=true, nullable=false, precision=22)
	public long getIdDocument() {
		return this.idDocument;
	}

	public void setIdDocument(long idDocument) {
		this.idDocument = idDocument;
	}


    @Lob()
	public byte[] getDocument() {
		return this.document;
	}

	public void setDocument(byte[] document) {
		this.document = document;
	}


	//bi-directional one-to-one association to TblOrder
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_DOCUMENT", nullable=false, insertable=false, updatable=false)
	public TblOrder getOmorder1() {
		return this.omorder1;
	}

	public void setOmorder1(TblOrder omorder1) {
		this.omorder1 = omorder1;
	}
	

	//bi-directional many-to-one association to TblOrder
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FK_ORDER")
	public TblOrder getOmorder2() {
		return this.omorder2;
	}

	public void setOmorder2(TblOrder omorder2) {
		this.omorder2 = omorder2;
	}
	
}