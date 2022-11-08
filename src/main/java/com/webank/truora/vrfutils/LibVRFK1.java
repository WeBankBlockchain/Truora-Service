package com.webank.truora.vrfutils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.webank.truora.base.exception.NativeCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.webank.truora.base.enums.ReqStatusEnum.VRF_LIB_FILE_NOT_EXISTS;
import static com.webank.truora.base.enums.ReqStatusEnum.VRF_LIB_LOAD_ERROR;

public interface LibVRFK1 extends Library {

    Logger logger = LoggerFactory.getLogger(LibVRFK1.class);

    String prove(String sk, String preSeed);

    boolean verify(String pk, String preSeed, String pi);

    public class InstanceHolder {
        private static LibVRFK1 instance = null;

        static {
            String os = System.getProperty("os.name").toLowerCase();
            String libname = "libvrfjna";
            String libExtension;
            if (os.contains("mac os")) {
                libExtension = "dylib";
            } else if (os.contains("windows")) {
                libname = "vrfjna";
                libExtension = "dll";
            } else {
                libExtension = "so";
            }

            String libFilePath = getFilePath(String.format("%s.%s", libname, libExtension));

            if (os.contains("windows") && libFilePath.startsWith("/")) {
                libFilePath = libFilePath.substring(1);
            }
            Path filepath = Paths.get((libFilePath));
            if (Files.notExists(filepath)) {
                throw new NativeCallException(VRF_LIB_FILE_NOT_EXISTS);
            }

            logger.info("Load vrf lib from:[{}]", libFilePath);
            System.setProperty("jna.debug_load", "true");
            System.load(libFilePath);
            instance = Native.loadLibrary(libFilePath, LibVRFK1.class);
            if (instance == null) {
                throw new NativeCallException(VRF_LIB_LOAD_ERROR);
            }
        }

        public static String getFilePath(String file) {
            return LibVRFK1.class.getClassLoader().getResource(file).getPath();
        }

        public static LibVRFK1 getInstance() {
            return instance;
        }
    }
}
