package com.webank.truora.vrfutils;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import org.fisco.bcos.sdk.v3.crypto.vrf.VRFException;


public class VRFUtils {

    public static int debuglevel  = 0;
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
        return resstr;
    }
}
