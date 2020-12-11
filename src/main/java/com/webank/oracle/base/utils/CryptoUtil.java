package com.webank.oracle.base.utils;

import com.google.common.base.Preconditions;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Uint;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint64;
import org.fisco.bcos.web3j.crypto.ECKeyPair;
import org.fisco.bcos.web3j.crypto.Hash;
import org.fisco.bcos.web3j.crypto.Keys;
import org.fisco.bcos.web3j.crypto.Sign;
import org.fisco.bcos.web3j.rlp.RlpEncoder;
import org.fisco.bcos.web3j.rlp.RlpList;
import org.fisco.bcos.web3j.rlp.RlpString;
import org.fisco.bcos.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CryptoUtil {
    private static final BigInteger MASK_256 = BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE);
    private static final String ALGORITHM = "ECDSA";
    private static final String CURVE_NAME = "secp256k1";

    private static final ECDomainParameters dp;
    private static final ECCurve curve;

    private static final ECNamedCurveSpec p;
    public static final int SIGNATURE_LENGTH = 65;

    static {
        X9ECParameters xp = ECUtil.getNamedCurveByName(CURVE_NAME);
        p = new ECNamedCurveSpec(CURVE_NAME, xp.getCurve(), xp.getG(), xp.getN(), xp.getH(), null);
        curve = EC5Util.convertCurve(p.getCurve());
        ECPoint g = EC5Util.convertPoint(curve, p.getGenerator(), false);
        BigInteger n = p.getOrder();
        BigInteger h = BigInteger.valueOf(p.getCofactor());
        dp = new ECDomainParameters(curve, g, n, h);
    }

    public static String getContractAddress(String fromAddress, long nonce) {
        byte[] bytes = RlpEncoder.encode(new RlpList(RlpString.create(Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(fromAddress))), RlpString.create(nonce)));
        byte[] sha3 = Hash.sha3(bytes);
        String hash = Numeric.toHexStringNoPrefix(sha3);
        return hash.substring(24);
    }

//    public static String getContractCode(Web3j web3j, String contractAddress) throws IOException {
//        EthGetCode ethGetCode = web3j
//                .ethGetCode(contractAddress, DefaultBlockParameterName.LATEST)
//                .send();
//
//        if (ethGetCode.hasError()) {
//            throw new IllegalStateException("Failed to get code for " + contractAddress + ": " + ethGetCode.getError().getMessage());
//        }
//
//        return Numeric.cleanHexPrefix(ethGetCode.getCode());
//    }

    public static byte[] sign(ECKeyPair keyPair, byte[] hash) {
        Sign.SignatureData signatureData = Sign.getSignInterface().signMessage(hash, keyPair);
        return signatureEncode(signatureData);
    }

    public static Address verifySignature(byte[] signature, byte[] hash) throws SignatureException {
        Sign.SignatureData signatureData = signatureSplit(signature);
        BigInteger publicKey = Sign.signedMessageToKey(hash, signatureData);
        return getAddress(publicKey);
    }

    public static Address getAddress(BigInteger publicKey) {
        //TODO get rid of conversions string<->byte[]
        return new Address(Keys.getAddress(publicKey));
    }

    public static byte[] toBytes(Object obj) {
        if (obj instanceof byte[]) {
            int length = ((byte[]) obj).length;
          //!!!!!!!!!!!
           // Preconditions.checkArgument(length <= 32);
            if (length < 32) {
                return Arrays.copyOf((byte[]) obj, 32);
            }
            return (byte[]) obj;
        } else if (obj instanceof BigInteger) {
            BigInteger value = (BigInteger) obj;
            if (value.signum() < 0) {
                value = MASK_256.and(value);
            }
            return Numeric.toBytesPadded(value, 32);
        } else if (obj instanceof Address) {
            Uint uint = ((Address) obj).toUint160();
            return Numeric.toBytesPadded(uint.getValue(), 20);
        } else if (obj instanceof Uint256) {
            Uint uint = (Uint) obj;
            return Numeric.toBytesPadded(uint.getValue(), 32);
        } else if (obj instanceof Uint64) {
            Uint uint = (Uint) obj;
            return Numeric.toBytesPadded(uint.getValue(), 8);
        } else if (obj instanceof Number) {
            long l = ((Number) obj).longValue();
            return toBytes(BigInteger.valueOf(l));
        }
        throw new IllegalArgumentException(obj.getClass().getName());
    }

    public static byte[] signatureEncode(Sign.SignatureData signatureData) {
        Preconditions.checkArgument(signatureData.getR().length == 32);
        Preconditions.checkArgument(signatureData.getS().length == 32);
        Preconditions.checkArgument(signatureData.getV() == 27 || signatureData.getV() == 28);
        ByteBuffer buffer = ByteBuffer.allocate(SIGNATURE_LENGTH);
        buffer.put(signatureData.getR());
        buffer.put(signatureData.getS());
        buffer.put(signatureData.getV());
        assert buffer.position() == SIGNATURE_LENGTH;
        return buffer.array();
    }

    public static Sign.SignatureData signatureSplit(byte[] signature) {
        Preconditions.checkArgument(signature.length == SIGNATURE_LENGTH);
        byte v = signature[64];
        Preconditions.checkArgument(v == 27 || v ==28);
        byte[] r = Arrays.copyOfRange(signature, 0, 32);
        byte[] s = Arrays.copyOfRange(signature, 32, 64);
        return new Sign.SignatureData(v, r, s);
    }

    public static byte[] soliditySha3(Object... data) {
        if (data.length == 1) {
            return Hash.sha3(toBytes(data[0]));
        }
        List<byte[]> arrays = Stream.of(data).map(CryptoUtil::toBytes).collect(Collectors.toList());
        ByteBuffer buffer = ByteBuffer.allocate(arrays.stream().mapToInt(a -> a.length).sum());
        for (byte[] a : arrays) {
            buffer.put(a);
        }
        byte[] array = buffer.array();
        assert buffer.position() == array.length;
        return Hash.sha3(array);
    }

    public static byte[] solidityBytes(Object... data) {
        if (data.length == 1) {
            return toBytes(data[0]);
        }
        List<byte[]> arrays = Stream.of(data).map(CryptoUtil::toBytes).collect(Collectors.toList());
        ByteBuffer buffer = ByteBuffer.allocate(arrays.stream().mapToInt(a -> a.length).sum());
        for (byte[] a : arrays) {
            buffer.put(a);
        }
        byte[] array = buffer.array();
        assert buffer.position() == array.length;
        return array;
    }

    public static byte[] soliditySha256(Object... data) {
        if (data.length == 1) {
            return Hash.sha256(toBytes(data[0]));
        }
        List<byte[]> arrays = Stream.of(data).map(CryptoUtil::toBytes).collect(Collectors.toList());
        ByteBuffer buffer = ByteBuffer.allocate(arrays.stream().mapToInt(a -> a.length).sum());
        for (byte[] a : arrays) {
            buffer.put(a);
        }
        byte[] array = buffer.array();
        assert buffer.position() == array.length;
        return Hash.sha256(array);
    }

    public static KeyPair decodeKeyPair(ECKeyPair ecKeyPair) {
        byte[] bytes = Numeric.toBytesPadded(ecKeyPair.getPublicKey(), 64);
        BigInteger x = Numeric.toBigInt(Arrays.copyOfRange(bytes, 0, 32));
        BigInteger y = Numeric.toBigInt(Arrays.copyOfRange(bytes, 32, 64));
        ECPoint q = curve.createPoint(x, y);
        BCECPublicKey publicKey = new BCECPublicKey(ALGORITHM, new ECPublicKeyParameters(q, dp), BouncyCastleProvider.CONFIGURATION);
        BCECPrivateKey privateKey = new BCECPrivateKey(ALGORITHM, new ECPrivateKeyParameters(ecKeyPair.getPrivateKey(), dp), publicKey, p, BouncyCastleProvider.CONFIGURATION);
        return new KeyPair(publicKey, privateKey);
    }

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        KeyPair keyPair1 = decodeKeyPair(ecKeyPair);
        System.out.println(toString(keyPair1));
    }

    public static String toString(KeyPair keyPair) {
        return keyPair.getPrivate() + ":" + keyPair.getPublic();
    }

//    public static X509Certificate genCert(KeyPair keyPair) throws NoSuchAlgorithmException, CertificateEncodingException, NoSuchProviderException, InvalidKeyException, SignatureException {
//        Date startDate = new Date();              // time from which certificate is valid
//        Date expiryDate = new Date(System.currentTimeMillis() + DateUtils.MILLIS_PER_DAY * 365);             // time after which certificate is not valid
//        BigInteger serialNumber = BigInteger.ONE;     // serial number for certificate
//        X509V1CertificateGenerator certGen = new X509V1CertificateGenerator();
//        X500Principal dnName = new X500Principal("CN=Test CA Certificate");
//        certGen.setSerialNumber(serialNumber);
//        certGen.setIssuerDN(dnName);
//        certGen.setNotBefore(startDate);
//        certGen.setNotAfter(expiryDate);
//        certGen.setSubjectDN(dnName);                       // note: same as issuer
//        certGen.setPublicKey(keyPair.getPublic());
//        certGen.setSignatureAlgorithm("SHA256WITHECDSA");
//        return certGen.generate(keyPair.getPrivate(), "BC");
//    }

//    public static Bytes32 getMerkleRoot(List<? extends HashedObject> objects) {
//        if (objects.isEmpty()) {
//            return Bytes32.DEFAULT;
//        }
//        //TODO make incremental version
//        List<byte[]> hashes = objects.stream().map(HashedObject::hash).collect(Collectors.toList());
//        int size = hashes.size();
//        while (size > 1) {
//            for (int i = 0; i < size; i+=2) {
//                if (i < size - 1) {
//                    byte[] hash1 = hashes.get(i);
//                    byte[] hash2 = hashes.get(i + 1);
//                    hashes.set(i/2, sha3(hash1, hash2));
//                } else {
//                    hashes.set(i/2, hashes.get(i));
//                }
//            }
//            size >>= 1;
//        }
//        return new Bytes32(hashes.get(0));
//    }

    public static byte[] sha3(byte[]... input) {
        Keccak.DigestKeccak kecc = new Keccak.Digest256();
        for (byte[] bytes : input) {
            kecc.update(bytes, 0, bytes.length);
        }
        return kecc.digest();
    }

}
