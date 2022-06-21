/*
 * (c) Copyright 2022 Micro Focus
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.database.utils;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.database.entities.OracleCloudInputs;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static io.cloudslang.content.database.utils.Constants.TEMP_PATH;

public class Utils {

    @NotNull
    public static List<String> getRowsFromGlobalSessionMap(@NotNull final GlobalSessionObject<Map<String, Object>> globalSessionObject, @NotNull final String aKey) {
        final Map<String, Object> globalMap = globalSessionObject.get();
        if (globalMap.containsKey(aKey)) {
            try {
                return (List<String>) globalMap.get(aKey);
            } catch (Exception e) {
                globalMap.remove(aKey);
                globalSessionObject.setResource(new SQLSessionResource(globalMap));
            }
        }
        return new ArrayList<>();
    }

    @NotNull
    public static String getSqlKey(@NotNull final OracleCloudInputs sqlInputs) {

        return computeSessionId(sqlInputs.getConnectionString() + sqlInputs.getUsername() +
                sqlInputs.getWalletPath() + sqlInputs.getSqlCommand() + sqlInputs.getKey());
    }

    @NotNull
    public static String computeSessionId(@NotNull final String aString) {
        final byte[] byteData = DigestUtils.sha256(aString.getBytes());
        final StringBuilder sb = new StringBuilder("SQLQuery:");

        for (final byte aByteData : byteData) {
            final String hex = Integer.toHexString(0xFF & aByteData);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    @NotNull
    public static GlobalSessionObject<Map<String, Object>> getOrDefaultGlobalSessionObj(@NotNull final GlobalSessionObject<Map<String, Object>> globalSessionObject) {
        if (globalSessionObject.get() != null) {
            return globalSessionObject;
        }
        globalSessionObject.setResource(new SQLSessionResource(new HashMap<String, Object>()));
        return globalSessionObject;
    }

    public static String unzip(String fileZip, boolean overwrite) {
        String fileName = new File(fileZip).getName();
        String destDirPath = TEMP_PATH + fileName.replaceFirst("[.][^.]+$", "");
        File destDir = new File(destDirPath);
        if (new File(fileZip).isDirectory())
            return fileZip;
        try {
            if (overwrite && destDir.exists())
                deleteDirectory(destDirPath);
            if (!destDir.exists()) {
                byte[] buffer = new byte[1024];
                ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
                ZipEntry zipEntry = zis.getNextEntry();
                while (zipEntry != null) {
                    File newFile = newFile(new File(destDirPath), zipEntry);
                    if (zipEntry.isDirectory()) {
                        if (!newFile.isDirectory() && !newFile.mkdirs()) {
                            throw new IOException("Failed to create directory " + newFile);
                        }
                    } else {
                        // fix for Windows-created archives
                        File parent = newFile.getParentFile();
                        if (!parent.isDirectory() && !parent.mkdirs()) {
                            throw new IOException("Failed to create directory " + parent);
                        }

                        // write file content
                        FileOutputStream fos = new FileOutputStream(newFile);
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                    }
                    zipEntry = zis.getNextEntry();
                }
                zis.closeEntry();
                zis.close();
            }
            return destDirPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }

    public static void deleteDirectory(String directoryFilePath) throws IOException {
        Path directory = Paths.get(directoryFilePath);

        if (Files.exists(directory)) {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                    Files.delete(path);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path directory, IOException ioException) throws IOException {
                    Files.delete(directory);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
