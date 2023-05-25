package com.piramal.lms.jobs.model;

import com.mongodb.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Entity
@Table(name = "accounting")
@Document(collection = "accounting")
public class AccountingMongo {
    @NotNull
    @Column(name = "loan_account_no")
    private String loan_account_no;
    @Id
    @Column(name = "accounting_id")
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Number _id;
    @NotNull
    @Column(name = "linked_transaction_id")
    private Number linked_transaction_id;
    @NotNull
    @Column(name = "accounting_event")
    private String accounting_event;
    @NotNull
    @Column(name = "accounting_Date")
    private Date accounting_Date;
    @NotNull
    @Column(name = "value_Date")
    private Date value_Date;
    @NotNull
    @Column(name = "transaction_type")
    private String transaction_type;
    @NotNull
    @Column(name = "transaction_desc")
    private String transaction_desc;
    @NotNull
    @Column(name = "ledger_code")
    private String ledger_code;
    @NotNull
    @Column(name = "user_branch")
    private String user_branch;
    @NotNull
    @Column(name = "accounting_branch")
    private String accounting_branch;
    @NotNull
    @Column(name = "accounting_code")
    private String accounting_code;
    @NotNull
    @Column(name = "amount")
    private Number amount;
    @Nullable
    @Column(name = "old_link_transaction_id")
    private Number old_link_transaction_id;
    @NotNull
    @Column(name = "Created_By")
    private String CreatedBy;
    @NotNull
    @Column(name = "Created_Dt")
    private Date CreatedDt;
    @Nullable
    @Column(name = "Update_By")
    private String UpdateBy;
    @Nullable
    @Column(name = "UpDate_Dt")
    private Date UpDateDt;
}
