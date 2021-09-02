/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.rft.remote_copy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.DigestOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class contains all static methods for basic File operations:
 *
 * Services provided:
 *   1) Read/Write to text files.
 *   2) Creating/Copying/Deleting Files/Folders
 *   3) File name/path operations
 *
 * This file has a test case in the ../test folder
 */
public class FSUtil {
    static final String CLASS_FILE_SUFFIX = ".class";
    static final int BUFFER_SIZE = 16384; // 16 * 1024

    /**
     * Do the heavy lifting of correctly building a destination file
     * that is ready to be written to (builds intermediate folders).
     *
     */
    public static void createFile(String path) throws IOException {
        File f = new File(path);
        createFile(f);
    }

    /**
     * Do the heavy lifting of correctly building a destination file
     * that is ready to be written to (builds intermediate folders).
     *
     * @param f object
     */
    public static void createFile(File f) throws IOException {
        f = f.getCanonicalFile();

        // Ensure there is a blank file
        if (f.exists()) {
            f.delete();
        }
        else {
            // Create any necessary intermediate folders
            File folder = f.getParentFile().getCanonicalFile();
            if (!folder.exists())
                folder.mkdirs();
        }

        // Finally, create an empty file
        f.createNewFile();
    }

    /**
     * Recursively deletes directories.
     * Can delete a single file too.
     *
     * @param path to delete
     */
    public static void recursiveDelete(String path) {
        recursiveDelete(new File(path));
    }

    /**
     * Recursively deletes directories.
     * Can delete a single file too.
     *
     * @param f file|folder to delete
     */
    public static void recursiveDelete(File f) {
        // Directories must be empty before they can be deleted
        if (f.isDirectory())
            for (File file : f.listFiles())
                recursiveDelete(file);

        f.delete();
    }

    /**
     * Copy a file.
     *
     * [dest] can be either a file (exists or not) or an existing directory.
     * In the latter case the filename  of the source is used for the destination filename.
     *
     * @param src source
     * @param dest destination
     *
     * @throws IOException
     */
    public static void copyFile(String src, String dest) throws IOException {
        copyFile(new File(src), new File(dest));
    }
    public static void copyFile(File src, String dest) throws IOException {
        copyFile(src, new File(dest));
    }
    public static void copyFile(String src, File dest) throws IOException {
        copyFile(new File(src), dest);
    }
    public static void copyFile(File src, File dest) throws IOException {
        // Create destination folders as necessary
        dest.getParentFile().mkdirs();

        // Uses NIO
        FileChannel input  = null;
        FileChannel output = null;

        try{
            input  = new FileInputStream(src).getChannel();

            if (dest.isDirectory())
                dest = new File(dest, src.getName());
            output = new FileOutputStream(dest).getChannel();

            output.transferFrom(input, 0, input.size());
        }
        finally{
            if (input != null)
                input.close();
            if (output != null)
                output.close();
        }
    }

    /**
     * Copy directory trees.
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void recursiveCopy(File src, File dest) throws IOException {
        // Copy directory
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdirs();
            }

            // Process children
            for (String child : src.list())
                recursiveCopy(new File(src, child), new File(dest, child));
        }
        else { // Copy file
            copyFile(src, dest);
        }
    }

    /**
     * Writes string to a file.
     *
     * Overwrites existing file.
     *
     * @param fileName
     * @param data
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(String fileName, String data) throws FileNotFoundException, IOException {
        write(new File(fileName), data, Charset.defaultCharset(), false);
    }

    /**
     * Writes a string to the end of a file.
     *
     * @param data
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void append(String fileName, String data) throws FileNotFoundException, IOException {
        append(new File(fileName), data);
    }

    /**
     * Writes string to a file.
     *
     * Overwrites existing file.
     *
     * @param file
     * @param data
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void write(File file, String data) throws FileNotFoundException, IOException {
        write(file, data, Charset.defaultCharset());
    }

    public static void write(File file, String data, String charset) throws FileNotFoundException, IOException {
        write(file, data, Charset.forName(charset));
    }

    public static void write(File file, String data, Charset charset) throws FileNotFoundException, IOException {
        write(file, data, charset, false);
    }



    /**
     * Writes a string to the end of a file.
     *
     * @param file
     * @param data
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void append(File file, String data) throws FileNotFoundException, IOException {
        append(file, data, Charset.defaultCharset());
    }

    public static void append(File file, String data, String charset) throws FileNotFoundException, IOException {
        append(file, data, Charset.forName(charset));
    }



    public static void append(File file, String data, Charset charset) throws FileNotFoundException, IOException {
        boolean append = true;
        write(file, data, charset, append);
    }


    /**
     * Writes string to a file.
     *
     * NOTE: This will "stomp-over" an existing file. when append is false
     *
     * @param file
     * @param data
     * @param charset
     * @param append
     *
     * @throws FileNotFoundException
     * @throws IOException
     */

    public static void write(File file, String data, Charset charset, boolean append) throws FileNotFoundException, IOException {
        // File doesn't exist yet or we are stomping over an existing file
        if (!file.exists() || !append)
            createFile(file);
        FileOutputStream foStream = new FileOutputStream(file, append);
        OutputStreamWriter osWriter = new OutputStreamWriter(foStream, charset);
        try{
            osWriter.write(data);
            osWriter.flush();

        } finally {
            osWriter.close();
            foStream.close();
        }
    }

    /**
     * Writes string to a file.
     *
     * Overwrites existing file.
     *
     * @param file
     * @param data
     * @param charset
     * @param digestType Type of message digest to create checksum with. Ex: MD5, SHA1.
     *
     * @returns Hex String representation of the checksum of the data written to file.
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     *
     */
    public static String write(File file, String data, String charset, String digestType) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
        return write(file, data, Charset.forName(charset), MessageDigest.getInstance(digestType));
    }

    /**
     * Writes a file to disk and returns the checksum of the written file.
     *
     * NOTE: This will "stomp-over" an existing file.
     *
     * @param file
     * @param data
     * @param charset
     * @param digest  MessageDigest to create checksum with.
     * @return Hex String representation of the checksum of the data written to file.
     *
     * @throws IOException
     */
    public static String write(File file, String data, Charset charset, MessageDigest digest) throws IOException {
        createFile(file);
        FileOutputStream foStream = new FileOutputStream(file);
        DigestOutputStream doStream = new DigestOutputStream(foStream, digest);
        OutputStreamWriter osWriter = new OutputStreamWriter(doStream, charset);
        try{
            osWriter.write(data);
            osWriter.flush();
            doStream.flush();  //make sure all data is through this stream before we build the hash.
            byte[] digestBytes = digest.digest();
            StringBuilder hash = new StringBuilder();
            for (byte b : digestBytes) {
                hash.append(Integer.toHexString((b & 0xFF) | 0x100).toLowerCase().substring(1,3));
            }
            return hash.toString();
        } finally {
            osWriter.close();
            doStream.close();
            foStream.close();
        }
    }

    /**
     * Prepend to the existing data in a file.
     *
     * Don't try this on a HUGE file.
     *
     * @param file
     * @param data
     *
     * @throws IOException
     */
    public static void prepend(File file, String data) throws IOException {
        write(file, data + readTextFile(file));
    }

    /**
     * Print the canonical-path "squashes '.' and '..' into the unique representation.
     * Handles the IOException too.
     *
     * NOTE: If the canonical path cannot be calculated your File object
     *       has an invalid path specification
     *
     * TODO: hmm.... maybe this just makes debugging harder?
     *
     * @param f File object
     * @return Unique Path if possible
     */
    public static String getPath(File f) {
        // Sanity check
        if (f == null)
            return null;

        String path;

        try {
            path = f.getCanonicalPath();
        }
        catch (IOException e) {
            path = f.getAbsolutePath();
        }

        return path;
    }
    public static String getPath(String s) {
        return getPath(new File(s));
    }
    public static String getPath(URL url) {
        File f = toFile(url);
        return getPath(f);
    }

    /**
     * Method to determine if the Abstract path for a File object
     * is valid or not.
     *
     * NOTE:  This does not mean the path exists.
     *
     * @param f to check
     * @return true if path is valid
     */
    public static boolean validPath(File f) {
        if (f == null)
            return false;

        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    public static boolean validPath(String path) {
        return validPath(new File(path));
    }
    public static boolean validPath(URL url) {
        File f = toFile(url);

        return validPath(f);
    }

    public static File toFile(URL url) {
        File f = new File(url.getPath());
        return f;
    }

    /**
     * Simple helper method to convert file separators in file names
     *
     * @param path to convert
     * @return
     */
    public static String toNativePath(String path) {
        String nativePath = null;

        if (path != null) {

            nativePath = path.replace("/",  File.separator)   // Unix    --> native
                    .replace("\\", File.separator); // Windows --> native
        }

        return nativePath;
    }

    /**
     * Changes any occurrences of windows file separators into Abstract (Java) file separators
     *
     * @param path to convert
     * @return
     */
    public static String toAbstractPath(String path) {
        String javaPath = null;

        if (path != null) {

            javaPath = path.replace("\\", "/");
        }

        return javaPath;
    }

    /**
     * Converts an Absolute path to Relative path,
     * by subtracting a base path.
     *
     * NOTE:  File separator is irrelevant
     *
     * @param file absolute path
     * @param base absolute path to subtract from the <code>path</code>
     *
     * @return relative path in Platform native syntax
     *
     * @throws IOException
     */
    public static String getRelativePath(File file, File base) throws IOException {
        // Sanity checks
        if (file == null)
            throw new IOException("File cannot be null");
        if (base == null)
            throw new IOException("Base cannot be null");

        return getRelativePath(file.getCanonicalPath(),
                base.getCanonicalPath());
    }

    /**
     * Not exposed because of potential for '\' vs '/' file separator wars.
     */
    private static String getRelativePath(String file, String base) {
        // Number of characters to skip in the full path (from the beginning)
        int offset = base.length() + File.separator.length();

        // Strip off leading path elements (base), to make a relative path
        String relativePath = file.substring(offset);

        return relativePath;
    }

    /**
     * Helper method that converts a path to a fully qualified
     * Class Name.
     *
     * It's dumb and expects to be given a path like this:
     *   --> com/opsware/pas/content/MyClass.class
     * Returns:
     *   --> com.opsware.pas.content.MyClass
     *
     * @param path
     * @return Fully Qualified Class Name for path
     */
    public static String convertToClassName(String path) {
        // File name length, minus file suffix
        int end = path.length() - CLASS_FILE_SUFFIX.length();

        // %~0dpf
        String fileName = path.substring(0, end);

        // Un processed
        String className = fileName;

        // Process the file name
        String[] fileSeparators = {"/", "\\", File.separator};
        for (String separator : fileSeparators) {
            // 1) Chop off leading file separator if present
            if (className.startsWith(separator))
                className = className.substring(separator.length());

            // 2) Convert any '/' or '\' to '.'
            className = className.replace(separator, ".");
        }

        return className;
    }

    /**
     * Outputs a stream to a file.
     *
     * NOTE: this will delete an existing file.
     *
     * @param data input
     * @param file output
     * @param date desired last modified time
     *
     * @throws IOException
     */
    public static void output(InputStream data, File file, Date date) throws IOException {
        // Sanity check arguments
        if (data == null)
            throw new IOException("InputStream cannot be null.");
        if (file == null)
            throw new IOException("Output File cannot be null.");
        if (date == null)
            throw new IOException("Date cannot be null");

        // Create output file and get InputStream to it
        FSUtil.createFile(file);
        FileOutputStream out = new FileOutputStream(file);

        // Buffer for reading data from the resource
        byte[] buf = new byte[BUFFER_SIZE];

        // Extract the resource to the output file
        try {
            int n;
            while ( (n = data.read(buf)) != -1)
                out.write(buf, 0, n);
        }
        finally { // Cleanup
            IOUtil.close(out);
        }

        // Set modification time
        file.setLastModified(date.getTime());
    }

    /**
     * Extracts a Classpath resource to a temporary file.
     *
     * @return temp File object for file created from the resource
     *
     * @throws IOException If something goes wrong.
     */
    public static File getFileFromResource(String resource,
                                           String tempPrefix,
                                           String tempSuffix) throws IOException {
        // We want the "resource name", might need to skip over .jar file name
        // Has no effect if not run from a .jar file
        int offset = resource.indexOf(";") + 1;
        resource = resource.substring(offset);

        // Acquire an input stream for the resource
        InputStream inStream = FSUtil.class.getClassLoader().getResourceAsStream(resource);
        if (inStream == null)
            throw new IOException("Cannot get an InputStream for resource: " + resource);

        File out;
        try {
            // Create a temp file
            out = File.createTempFile(tempPrefix, tempSuffix);

            // Extract into it
            FSUtil.output(inStream, out, new Date());
        }
        finally { // Cleanup input stream
            IOUtil.close(inStream);
        }

        return out;
    }

    /**
     * Utility method to turn a File Object for text file into a string.
     *
     * @param path path to text file
     *
     * @return file contents as String
     *
     * @throws IOException
     */
    public static String readTextFile(String path) throws IOException {
        return readTextFile(new File(path));
    }


    public static String readTextFile(File f) throws IOException {
        return readTextFile(f, Charset.defaultCharset());
    }

    /**
     * Utility method to turn a File Object for text file into a string.
     *
     * @param f text file to read
     * @param charset Charset to use while decoding file
     *
     * @return file contents as String
     *
     * @throws IOException
     */
    public static String readTextFile(File f, Charset charset) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        String text = "";
        try {
            // Read the text file using the specified Charset
            text = IOUtil.decodeBytes(IOUtil.InputStreamToByteBuffer(fis), charset);
        }
        finally {
            fis.close();
        }
        return text;
    }

    /**
     * Return a File object pointing to a new empty folder in the system temp area.
     * This folder is set to "Self-Destruct" when the JVM shutsdown.
     *
     * @param prefix prepended to the temp folder name
     * @param suffix file extension
     *
     * @return File object for the created temp directory
     *
     * @throws IOException
     */
    public static File makeTempFolder(String prefix, String suffix) throws IOException {
        // Make a temporary directory using the prefix and file extension (suffix)
        File temp = File.createTempFile(prefix, suffix);

        // Make sure the directory is empty
        temp.delete();
        temp.mkdirs();

        // Delete it when the current JVM process shuts down
        temp.deleteOnExit();

        return temp;
    }

    /**
     * Joins paths
     *
     * @param base parent folder
     * @param variable number of path elements to join together
     *
     * @return canonical path formed by joining the path elements
     */
    public static String joinPaths(String base, String... pathElements) throws IOException {
        File joined = new File(base);

        for (String element : pathElements)
            joined =  new File(joined, element);

        return joined.getCanonicalPath();
    }

    /**
     * Joins relative paths
     *
     * @param base parent folder
     * @param variable number of path elements to join together
     *
     * @return path formed by joining the path elements
     */
    public static String joinRelativePaths(String base, String... pathElements) {
        File joined = new File(base);

        for (String element : pathElements)
            joined =  new File(joined, element);

        return joined.getPath();
    }

    /**
     * Quotes a string argument if necessary.
     *
     * @param arg String to be quoted
     *
     * @return quoted String (if necessary)
     */
    public static String quote(String arg) {
        String qq = "\"";

        if (arg.contains(" ") || arg.contains("\t") || arg.contains("\n"))
            return String.format("%s%s%s", qq, arg, qq);
        else
            return arg;
    }

    /**
     * Handles the minutiae of simple file download.
     * Most likely will not Handle HTTPS.
     *
     * @param url
     *
     * @return temporary file if successful, null otherwise
     */
    public static File downloadToTempFile(String url) {
        URL fileURL;
        try {
            fileURL = new URL(url);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

        //
        // Crack filename into:
        // 1) name      --> prefix
        // 2) extension --> suffix
        //
        String filename = new File(fileURL.getPath()).getName();

        String[] parts = filename.split("\\.");
        String prefix  = parts[0] + "_";
        String suffix  = "." + parts[parts.length - 1];

        URLConnection urlConnection;
        InputStream data;
        File file;
        try {
            urlConnection = fileURL.openConnection();
            data = urlConnection.getInputStream();

            file = File.createTempFile(prefix, suffix);

            FSUtil.output(data, file, new Date());

            return file.getCanonicalFile();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * General purpose .zip|.jar file exploder method.
     *
     * @param archiveFile
     * @param outputFolder
     *
     * @throws IOException
     */
    public static void unzip(String archiveFile, File outputFolder) throws IOException {
        unzip(new File(archiveFile), outputFolder);
    }
    public static void unzip(File archiveFile, File outputFolder) throws IOException {
        ZipInputStream zis = null;
        try {
            // You gotta love the decorator pattern
            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(archiveFile)));

            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory())
                    continue;

                File outputFile = new File(outputFolder, entry.getName());
                Date date       = new Date(entry.getTime());

                FSUtil.output(zis, outputFile, date);
            }
        }
        finally {
            if (zis != null)
                zis.close();
        }
    }
}
