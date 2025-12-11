package com.bank.entity;

public class TransferRequestDTO {
    private Long id;
    private Long receiverId;
    private Long balance;

    private Type type;

    @Override
    public String toString() {
        return "TransferRequestDTO{" +
                "id=" + id +
                ", receiverId=" + receiverId +
                ", balance=" + balance +
                ", type=" + type +
                '}';
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
}
