package model;

import utility.StringUtil;

import java.util.ArrayList;
import java.util.Date;

public class Bloque {
    public String hash;
    public String previousHash;
    public long timeStamp;
    public int nonce;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    public String merkleRoot;

    public Bloque(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String CalculateHash = StringUtil.applySha256(
                previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + merkleRoot);
        return CalculateHash;
    }

    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDificultyString(difficulty);
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
            if (nonce % 1000 == 0) {
                System.out.println("nonce: " + nonce + " hash: " + hash);
            }
        }

        System.out.println("bloque minado: " + hash);
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null)
            return false;
        if ((!"0".equals(previousHash))) {
            if ((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discared");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Success add to block");
        return true;
    }

}
