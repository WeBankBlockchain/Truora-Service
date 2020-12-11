package com.webank.oracle.base.utils;

import com.webank.oracle.base.exception.OracleException;
import com.webank.oracle.base.pojo.vo.ConstantCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

/**
 * file utils.
 */
@Slf4j
@Component
public class OracleFileUtils {

    /**
     * write constant to file.
     */
    public static void writeConstantToFile(File file, String constant) {
        try {
            OracleFileUtils.createFileIfNotExist(file, false, true);
        } catch (IOException ioe) {
            String errorMsg = format("Failed to create file: %s", file.getAbsolutePath());
            log.error(errorMsg);
            throw new OracleException(ConstantCode.SYSTEM_EXCEPTION.getCode(), errorMsg, ioe);
        }

        try (FileOutputStream stream = new FileOutputStream(file)) {
            org.apache.commons.io.IOUtils.write(constant.getBytes("UTF-8"), stream);
            stream.flush();
        } catch (IOException ioe) {
            String errorMsg = format("Failed to write file: %s", file.getAbsolutePath());
            log.error(errorMsg);
            throw new OracleException(ConstantCode.SYSTEM_EXCEPTION.getCode(), errorMsg, ioe);
        }
    }


    /**
     * create file if not exist.
     */
    public static void createFileIfNotExist(File targetFile, boolean deleteOld, boolean isFile) throws IOException {
        Objects.requireNonNull(targetFile, "fail to create file:targetFile is null");
        File parentFile = targetFile.getParentFile();
        if (!parentFile.exists())
            parentFile.mkdirs();

        if (deleteOld)
            targetFile.deleteOnExit();

        if (targetFile.exists())
            return;

        if (isFile) {
            targetFile.createNewFile();
        } else {
            targetFile.mkdir();
        }
    }

    /**
     * read constant of file.
     *
     * @param file
     * @return
     */
    public static String readFileToString(File file) {
        StringBuffer xml = new StringBuffer();
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            lines.forEach(str -> xml.append(str));
        } catch (IOException e) {
            log.error("readFileConstant exception", e);
            return null;
        }
        return xml.toString();
    }

}
