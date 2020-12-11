/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package com.webank.oracle.test.util;

import static org.fisco.solc.compiler.SolidityCompiler.Options.ABI;
import static org.fisco.solc.compiler.SolidityCompiler.Options.BIN;
import static org.fisco.solc.compiler.SolidityCompiler.Options.INTERFACE;
import static org.fisco.solc.compiler.SolidityCompiler.Options.METADATA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator;
import org.fisco.solc.compiler.CompilationResult;
import org.fisco.solc.compiler.SolidityCompiler;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompilerTest {

    @Test
    public void solc_getVersion_shouldWork() throws IOException {
        final String version = SolidityCompiler.runGetVersionOutput(true);

        assertThat(version, containsString("version:"));
    }


    @Test
    public void compileFilesTest() throws IOException {

        File solFileList = new File("src/test/resources/contract");
        File[] solFiles = solFileList.listFiles();

        for (File solFile : solFiles) {
            if (!solFile.getName().endsWith(".sol") || solFile.getName().contains("Lib")) {
                continue;
            }
            // choose file
            if(!solFile.getName().equals("APISampleOracle.sol")){
                continue;
            }
            SolidityCompiler.Result res =
                    SolidityCompiler.compile(solFile, false,true, ABI, BIN, INTERFACE, METADATA);
               SolidityCompiler.Result gmres =
                    SolidityCompiler.compile(solFile, true,true, ABI, BIN, INTERFACE, METADATA);

           // System.out.println("result : "+ res.getOutput() );
            CompilationResult result = CompilationResult.parse(res.getOutput());
            CompilationResult gmresult = CompilationResult.parse(gmres.getOutput());
            log.info("contractname  " + solFile.getName());
            Path source = Paths.get(solFile.getPath());
            String contractname = solFile.getName().split("\\.")[0];
            CompilationResult.ContractMetadata a =
                    result.getContract(source, contractname);
            CompilationResult.ContractMetadata agm =
                    gmresult.getContract(source, contractname);
            FileUtils.writeStringToFile(
                    new File("src/test/resources/solidity/" + contractname + ".abi"), a.abi);
            FileUtils.writeStringToFile(
                    new File("src/test/resources/solidity/" + contractname + ".bin"), a.bin);
            FileUtils.writeStringToFile(
                    new File("src/test/resources/solidity/" + contractname + "_gm"+".bin"), agm.bin);
            String binFile;
            String abiFile;
            String gmBinFile;
            String tempDirPath = new File("src/test/java/").getAbsolutePath();
            String packageName = "com.webank.oracle.test.temp";
            String filename = contractname;
            abiFile = "src/test/resources/solidity/" + filename + ".abi";
            binFile = "src/test/resources/solidity/" + filename + ".bin";
            gmBinFile = "src/test/resources/solidity/" + filename +"_gm"+ ".bin";
            SolidityFunctionWrapperGenerator.main(
                    Arrays.asList(
                                    "-a",
                                    abiFile,
                                    "-b",
                                    binFile,
                                    "-s",
                                    gmBinFile,
                                    "-p",
                                    packageName,
                                    "-o",
                                    tempDirPath)
                            .toArray(new String[0]));
        }
    }

    @Test
    public void compileFilesWithImportTest() throws IOException {

        Path source = Paths.get("src", "test", "resources", "contract", "test2.sol");

        SolidityCompiler.Result res =
                SolidityCompiler.compile(source.toFile(), false, true, ABI, BIN, INTERFACE, METADATA);
        CompilationResult result = CompilationResult.parse(res.getOutput());

        CompilationResult.ContractMetadata a = result.getContract(source, "test2");
    }


}
