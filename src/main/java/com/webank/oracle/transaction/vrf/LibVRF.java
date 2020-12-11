package com.webank.oracle.transaction.vrf;

import static com.webank.oracle.base.enums.ReqStatusEnum.VRF_LIB_FILE_NOT_EXISTS;
import static com.webank.oracle.base.enums.ReqStatusEnum.VRF_LIB_LOAD_ERROR;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.webank.oracle.event.exception.NativeCallException;

public interface LibVRF extends Library {

     Logger logger = LoggerFactory.getLogger(LibVRF.class);

    /**
     * @param sk
     * @param preseed
     * @return
     */
    public String VRFProoFGenerate(String sk, String preseed);

    public class InstanceHolder {
        private static LibVRF instance = null;

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

            String libFilePath = getFilePath(String.format("libvrf.%s", libExtension));
            if (Files.notExists(Paths.get((libFilePath)))) {
                throw new NativeCallException(VRF_LIB_FILE_NOT_EXISTS);
            }

            logger.info("Load vrf lib from:[{}]", libFilePath);
            instance = Native.loadLibrary(libFilePath, LibVRF.class);
            if (instance == null) {
                throw new NativeCallException(VRF_LIB_LOAD_ERROR);
            }
        }
        public static String getFilePath(String file){
            return LibVRF.class.getClassLoader().getResource(file).getPath();
        }

        public static LibVRF getInstance() {
            return instance;
        }
    }
}