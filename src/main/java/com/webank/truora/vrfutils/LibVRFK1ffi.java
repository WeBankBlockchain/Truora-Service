package com.webank.truora.vrfutils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.webank.truora.base.exception.NativeCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.webank.truora.base.enums.ReqStatusEnum.VRF_LIB_FILE_NOT_EXISTS;
import static com.webank.truora.base.enums.ReqStatusEnum.VRF_LIB_LOAD_ERROR;

/*2022.11 LibVRFK1的扩展版，采用vrf-rs库自行扩展ffi接口后自行编译出来的so，接口是prove_hexstr
* LibVRFK1使用的libvrfjna.so编译工程已经outdate，所以做了扩展
* */
public interface LibVRFK1ffi extends Library {

     Logger logger = LoggerFactory.getLogger(LibVRFK1ffi.class);

     //此方法已经屏蔽
     //String prove(String sk, String preSeed);
    /*基于vrf-rs开源库，增加了ffi接口prove_hexstr,供jna调用，调用代码参见VRFUtils，
    需要java端先开辟返回的缓冲区，buffersize不应小于162字节
    最后一个参数是指示rust端是否打印debug信息，注意，如果不为0，会直接在控制台打印（未结合日志组件），**仅供调试时使用**否则很丑
    */
    int prove_hex(String sk, String preSeed, Pointer outbuffer, long buffersize, int debuglevel);
    //pi即prove的结果，preseed是数据,公钥应依赖vrf库从私钥重新生成，而不是使用默认算法生成的公钥
    int verify_hex(String pk, String pi,String preSeed, Pointer outbuffer,long buffersize,int debuglevel);

    int derive_public_key_hex(String privkey,Pointer outbuffer,long buffersize,int debuglevel);

    int proof_to_hash_hex(String proof,Pointer outbuffer,long buffersize,int debuglevel);
    public class InstanceHolder {
        private static LibVRFK1ffi instance = null;

        static {
            String os = System.getProperty("os.name").toLowerCase();
            String libname ="libecvrf";
            String libExtension;
            if (os.contains("mac os")) {
                libExtension = "dylib";
            } else if (os.contains("windows")) {
                libname = "ecvrf";
                libExtension = "dll";
            } else {
                libExtension = "so";
            }

            String libFilePath = getFilePath(String.format("%s.%s", libname,libExtension));
            if (os.contains("windows")&&libFilePath.startsWith("/")) {
                //在windows上有可能出现/d:/code/lib/xxx 这样多一个/开头的路径，导致加载失败，所以substring一下
                libFilePath = libFilePath.substring(1);
            }
            Path filepath = Paths.get((libFilePath));
            if (Files.notExists(filepath)) {
                throw new NativeCallException(VRF_LIB_FILE_NOT_EXISTS);
            }

            logger.info("Load vrflib from:[{}]", libFilePath);
            //打开jna加载时的调试信息，开发时使用
		    //System.setProperty("jna.debug_load","true");
	        System.load(libFilePath);
            instance = Native.loadLibrary(libFilePath, LibVRFK1ffi.class);
            if (instance == null) {
                throw new NativeCallException(VRF_LIB_LOAD_ERROR);
            }
        }
        public static String getFilePath(String file){
            return LibVRFK1ffi.class.getClassLoader().getResource(file).getPath();
        }

        public static LibVRFK1ffi getInstance() {
            return instance;
        }
    }


}
