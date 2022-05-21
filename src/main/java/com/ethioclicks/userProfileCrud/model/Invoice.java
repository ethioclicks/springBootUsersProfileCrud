package com.ethioclicks.userProfileCrud.model;

public class Invoice {
    private int id;
    private String invoiceName;
    private String InvoiceDate;
    private String InvoiceFile;
    private boolean archiveThis;

    public Invoice(){
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getInvoiceFile() {
        return InvoiceFile;
    }


    public void setInvoiceFile(String invoiceFile) {
        InvoiceFile = invoiceFile;
    }

    public boolean addToarchive() {
        return archiveThis;
    }

    public void setArchiveThis(boolean archiveThis) {
        this.archiveThis = archiveThis;
    }

    public Invoice(int id, String invoiceName, String invoiceDate, String invoiceFile) {
        this.id = id;
        this.invoiceName = invoiceName;
        this.InvoiceDate = invoiceDate;
        this.InvoiceFile = invoiceFile;
    }
}
