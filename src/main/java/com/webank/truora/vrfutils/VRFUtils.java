package com.webank.truora.vrfutils;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.webank.truora.base.utils.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.fisco.bcos.sdk.v3.crypto.vrf.VRFException;

import java.math.BigInteger;

@Slf4j

public class VRFUtils {

    public static int debuglevel  = 0;

    public static byte[] calculateTheHashOfPK(String pk) {
        int len = pk.length();
        String pkx = pk.substring(0,len/2);
        String pky = pk.substring(len/2);
        BigInteger Bx = new BigInteger(pkx,16);
        BigInteger By = new BigInteger(pky,16);
        byte[] hashres = CryptoUtil.soliditySha3(Bx,By);
        log.info("calculateTheHashOfPK {},hashinhex:{}",pk, Hex.encodeHexString(hashres));
        return hashres;
    }
    public static String prove(String hexkey,String actualSeed) throws VRFException {
        int mode = 1; //2022.11 应默认mode为1
        String resstr = "";
        if (mode == 0){
            // 重构前的老库，供测试用
            LibVRFK1 ecvrf = LibVRFK1.InstanceHolder.getInstance();
            resstr = ecvrf.prove(hexkey,actualSeed);
        }
        if (mode == 1) {
            // 重新编译的vrf-rs库
            LibECVrf ecvrf = LibECVrf.InstanceHolder.getInstance();
            long buffersize = 200; //分配内存空间，以待传入ffi方法,prove的字节数为81，转hex后为162
            Pointer outbuffer = new Memory(buffersize);
            int resk1 = ecvrf.prove_hex(hexkey, actualSeed, outbuffer, buffersize, debuglevel);
            if (resk1 < 0) {
                throw new VRFException(String.format("VRF Prove error %s", resk1));
            }
            resstr = new String(outbuffer.getByteArray(0, resk1));
            log.info("VRFUtils prove res :{}, output:{}",resk1,resstr);

        }
        return resstr;
    }

    public static String verify(String pubkey,String pi,String data) throws  VRFException{
        LibECVrf ecvrf = LibECVrf.InstanceHolder.getInstance();
        long buffersize = 200;
        Pointer outbuffer = new Memory(buffersize);
        int res = ecvrf.verify_hex(pubkey,pi,data,outbuffer,buffersize,debuglevel);
        if (res < 0) {
            throw new VRFException(String.format("VRF verify  error %s", res));
        }
        String resstr = new String(outbuffer.getByteArray(0, res));
        log.info("VRFUtils verify res :{}, output:{}",res,resstr);
        return resstr;

    }

    public static String derive_public_key(String privkey)
    {
        LibECVrf ecvrf = LibECVrf.InstanceHolder.getInstance();
        long buffersize = 200;
        Pointer outbuffer = new Memory(buffersize);
        int res = ecvrf.derive_public_key_hex(privkey,outbuffer,buffersize,debuglevel);
        if (res < 0) {
            throw new VRFException(String.format("VRF  derive_public_key  error %s", res));
        }
        String resstr = new String(outbuffer.getByteArray(0, res));
        log.info("VRFUtils derive_public_key res :{}, output:{}",res,resstr);
        return resstr;
    }

    public static String proof_to_hash_hex(String proof)
    {
        LibECVrf ecvrf = LibECVrf.InstanceHolder.getInstance();
        long buffersize = 200;
        Pointer outbuffer = new Memory(buffersize);
        int res = ecvrf.proof_to_hash_hex(proof,outbuffer,buffersize,debuglevel);
        if (res < 0) {
            throw new VRFException(String.format("VRF  proof_to_hash_hex  error %s", res));
        }
        String resstr = new String(outbuffer.getByteArray(0, res));
        log.info("VRFUtils proof_to_hash_hex res :{}, output:{}",res,resstr);
        return resstr;
    }
}
