package ru.ert.account.model;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Account
 * @author kuyantsev
 * Date: 06.12.2019
 */
@Entity
@Table(name = "account")
public class Account implements Serializable {
    private long id;
    private BigDecimal balance;

    /**
     * Creates account with id, balance
     * @param balance account balance
     */
    public Account(long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance.setScale(2, BigDecimal.ROUND_UP);
    }

    /**
     * Creates account with balance
     * @param balance account balance
     */
    public Account(BigDecimal balance) {
        this(0, balance);
    }

    public Account() {

    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "balance", nullable = false)
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account [id=" + id + ", balance=" + balance + "]";
    }

}
