package com.webank.oracle.transaction.vrf;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.webank.oracle.event.exception.NativeCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

import static com.webank.oracle.base.enums.ReqStatusEnum.VRF_LIB_FILE_NOT_EXISTS;
import static com.webank.oracle.base.enums.ReqStatusEnum.VRF_LIB_LOAD_ERROR;

public interface LibVRFK1 extends Library {

     Logger logger = LoggerFactory.getLogger(LibVRFK1.class);

    String prove(String sk, String preSeed);

    boolean verify(String pk, String preSeed, String pi);

    public class InstanceHolder {
        private static LibVRFK1 instance = null;

        static {
            String os = System.getProperty("os.name").toLowerCase();
            String libExtension;
            if (os.contains("mac os")) {
                libExtension = "dylib";
            } else if (os.contains("windows")) {
                libExtension = "dll";
            } else {
                libExtension = "so";
            }

            String libFilePath = getFilePath(String.format("libvrfjna.%s", libExtension));
            if (Files.notExists(Paths.get((libFilePath)))) {
                throw new NativeCallException(VRF_LIB_FILE_NOT_EXISTS);
            }

            logger.info("Load vrf lib from:[{}]", libFilePath);
            instance = Native.loadLibrary(libFilePath, LibVRFK1.class);
            if (instance == null) {
                throw new NativeCallException(VRF_LIB_LOAD_ERROR);
            }
        }
        public static String getFilePath(String file){
            return LibVRFK1.class.getClassLoader().getResource(file).getPath();
        }

        public static LibVRFK1 getInstance() {
            return instance;
        }
    }
}