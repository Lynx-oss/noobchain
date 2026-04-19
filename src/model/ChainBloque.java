package model;
import com.google.gson.GsonBuilder;
import utility.StringUtil;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class ChainBloque {
    public static ArrayList<Bloque> blockChain = new ArrayList<Bloque>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    public static int difficulty = 3;
    public static float minimumTransaction = 0.1f;
    public static Wallet walletA;
    public static Wallet walletB;
    public static Transaction genesisTransaction;

    public static void main(String[] args){

        //testeando las wallets y las signatures
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();
        Wallet coinbase = new Wallet();

        //transaccion genesis a wallet a con 100 noobcoins
        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);
        genesisTransaction.transactionId = "0";
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId));
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        System.out.println("creando y minando el genesis block");
        Bloque genesis = new Bloque("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //probandoo
        Bloque bloque1 = new Bloque(genesis.hash);
        System.out.println("\nWallet A balance is: " + walletA.getBalance());
        System.out.println("\nWalletA esta tranado de mandar coins to wallet b....");
        bloque1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        addBlock(bloque1);
        System.out.println("\nWalletA balance is: " + walletA.getBalance());
        System.out.println("Waller B balance is: " + walletB.getBalance());



        Bloque bloque2 = new Bloque(bloque1.hash);
        System.out.println("\nWalletA tratando de mander coins");
        bloque2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        addBlock(bloque2);
        System.out.println("\nWallet A balance is :  " + walletA.getBalance());
        System.out.println("\nWallet B balance is : " +walletB.getBalance());


        //copie y pegue por flojera
        Bloque block3 = new Bloque(bloque2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        isChainValid();



        System.out.println("Private and public keys");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));


        //creando la transaccipn de prueba de la wallet a a la b
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);

        //verifica si la signature es valida
        System.out.println("is signature verified");
        System.out.println(transaction.verifySignature());





        System.out.println("tratando de minar el primer bloque: ");
        blockChain.get(0).mineBlock(difficulty);
        System.out.println("tratando de minar el segundo bloquye: ");
        blockChain.get(1).mineBlock(difficulty);
        System.out.println("tratando de minar el tercerbloque_: ");
        blockChain.get(2).mineBlock(difficulty);

        System.out.println("\nblockchain es valido: " + isChainValid());

        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println("\nel block chain: ");
        System.out.println(blockChainJson);
    }

    public static boolean isChainValid(){
        Bloque currentBlock;
        Bloque previousBlock;
        String hashTarget = new String (new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));


        for (int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i-1);

            if(!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("los hashes actuales no son iguales");
                return false;
            }

            if(!previousBlock.hash.equals(currentBlock.previousHash)){
                System.out.println("Los hashes previos no son iguales");
                return false;
            }

            if(!currentBlock.hash.substring(0, difficulty).equals(hashTarget)){
                System.out.println("este bloque no fue minado");
                return false;
            }

            TransactionOutput tempOutput;
            for (int j = 0; j < currentBlock.transactions.size(); j++) {
                Transaction currentTransaction = currentBlock.transactions.get(j);

                if(!currentTransaction.verifySignature()){
                    System.out.println("#signature on transaction("+ j +"is invalid");
                    return false;
                }

                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()){
                    System.out.println("#inputs are note equals to outputs on Transaction ("+ j + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs){
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null){
                        System.out.println("#Referenced input on Transaction (" + j + ") is missing");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TransactionOutput output: currentTransaction.outputs){
                    tempUTXOs.put(output.id, output);
                }

                if(currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient){
                    System.out.println("#Transaction(" + j + ") output reciepient is not who it should be");
                    return false;
                }

                if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + j+ ") output 'change' is not sender.");
                    return false;
                }


            }


            
        }
        System.out.println("blockchain is valid");
        return true;
    }

    public static void addBlock(Bloque newBloque){
        newBloque.mineBlock(difficulty);
        blockChain.add(newBloque);
    }

}
