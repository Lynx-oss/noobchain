package model;

import utility.StringUtil;

import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey reciepient;
    public float value;
    public String parentTransactionId;

    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient)+ Float.toString(value)+ parentTransactionId);
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
    }

    public boolean isMine(PublicKey publicKey){
        return publicKey.equals(reciepient);
    }
}
