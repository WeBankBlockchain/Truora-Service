package com.webank.truora.contract.bcos3.simplevrf;

import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class VRFUtil extends Contract {
    public static final String[] BINARY_ARRAY = {"6080604052348015600f57600080fd5b50603f80601d6000396000f3fe6080604052600080fdfea2646970667358221220c174aa1091bbeff94d2f0ce592fda430e5cae2079a12e1c66e7bf76f32d3520264736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"6080604052348015600f57600080fd5b50603f80601d6000396000f3fe6080604052600080fdfea264697066735822122065252e07605e1c087b94b933f2a9b2efbfd412a26893754ef5558bf10d88c4dc64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    protected VRFUtil(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public static VRFUtil load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new VRFUtil(contractAddress, client, credential);
    }

    public static VRFUtil deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(VRFUtil.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }
}
