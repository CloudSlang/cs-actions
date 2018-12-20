package io.cloudslang.content.postgres.services;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

import static io.cloudslang.content.postgres.utils.Constants.*;
import static io.cloudslang.content.postgres.utils.Constants.WORK_MEM;


@SuppressWarnings("ALL")
public class ConfigService {

    /**
     * Method to validate inputs and consolidate to a map.
     * @param listenAddresses     The list of addresses where the PostgreSQL database listens
     * @param port                 The port the PostgreSQL database should listen.
     * @param ssl                  Enable SSL connections.
     * @param sslCaFile            Name of the file containing the SSL server certificate authority (CA).
     * @param sslCertFile          Name of the file containing the SSL server certificate.
     * @param sslKeyFile           Name of the file containing the SSL server private key.
     * @param maxConnections       The maximum number of client connections allowed.
     * @param sharedBuffers        Determines how much memory is dedicated to PostgreSQL to use for caching data.
     * @param effectiveCacheSize   Effective cache size.
     * @param autovacuum           Enable/disable autovacuum. The autovacuum process takes care of several maintenance
     *                             chores inside your database that you really need.
     * @param workMem              Memory used for sorting and queries.
     *
     */
    public static Map<String, Object> validateAndBuildKeyValuesMap(String listenAddresses, String port, String ssl, String sslCaFile, String sslCertFile,
                                                                   String sslKeyFile, String maxConnections, String sharedBuffers,
                                                                   String effectiveCacheSize, String autovacuum, String workMem) {

        Map<String, Object> keyValues = new HashMap<>();
        if(listenAddresses != null && listenAddresses.length() > 0) {
            keyValues.put(LISTEN_ADDRESSES, listenAddresses);
        }

        if(StringUtils.isNumeric(port)) {
            keyValues.put(PORT, Integer.parseInt(port));
        }

        if(StringUtils.isNotEmpty(ssl)) {
            keyValues.put(SSL, ssl);
        }

        if(StringUtils.isNotEmpty(sslCaFile)) {
            keyValues.put(SSL_CA_FILE, sslCaFile);
        }

        if(StringUtils.isNotEmpty(sslCertFile)) {
            keyValues.put(SSL_CERT_FILE, sslCertFile);
        }

        if(StringUtils.isNotEmpty(sslKeyFile)) {
            keyValues.put(SSL_KEY_FILE, sslKeyFile);
        }

        if(StringUtils.isNumeric(maxConnections)) {
            keyValues.put(MAX_CONNECTIONS, Integer.parseInt(maxConnections));
        }

        if(StringUtils.isNotEmpty(sharedBuffers)) {
            keyValues.put(SHARED_BUFFERS, sharedBuffers);
        }

        if(StringUtils.isNotEmpty(effectiveCacheSize)) {
            keyValues.put(EFFECTIVE_CACHE_SIZE, effectiveCacheSize);
        }

        if(StringUtils.isNotEmpty(autovacuum)) {
            keyValues.put(AUTOVACUUM, autovacuum);
        }

        if(StringUtils.isNotEmpty(workMem)) {
            keyValues.put(WORK_MEM, workMem);
        }

        return keyValues;
    }

    /**
     * Method to modify the Postgres config postgresql.conf based on key-value pairs
     *
     * @param filename             The filename of the config to be updated.
     * @param keyValuePairs        A map of key-value pairs.
     *
     */
    public static void changeProperty(String filename, Map<String, Object> keyValuePairs) throws IOException {
        if(keyValuePairs.size() == 0) {
            return;
        }

        final File file = new File(filename);
        final File tmpFile = new File(file + ".tmp");

        PrintWriter pw = new PrintWriter(tmpFile);
        BufferedReader br = new BufferedReader(new FileReader(file));

        Set<String> keys = keyValuePairs.keySet();
        List<String> inConfig = new ArrayList<>();

        for (String line; (line = br.readLine()) != null; ) {
            int keyPos = line.indexOf('=');
            if (keyPos > -1) {
                String key = line.substring(0, keyPos).trim();

                if (!key.trim().startsWith("#") && keys.contains(key) && !inConfig.contains(key)) {
                    // Check if the line has any comments.  Split by '#' to funs all tokens.
                    String[] keyValuePair = line.split("=");

                    StringBuilder lineBuilder = new StringBuilder();
                    lineBuilder.append(keyValuePair[0].trim()).append(" = ").append(keyValuePairs.get(key));

                    line = lineBuilder.toString();
                    inConfig.add(key);
                }
            }
            pw.println(line);
        }

        for (String key : keys) {
            if (!inConfig.contains(key)) {
                StringBuilder lineBuilder = new StringBuilder();
                lineBuilder.append(key).append(" = ").append(keyValuePairs.get(key));
                pw.println(lineBuilder.toString());
            }
        }

        br.close();
        pw.close();
        file.delete();
        tmpFile.renameTo(file);
    }

    /**
     * Method to modify the Postgres config pg_hba.config
     *
     * @param filename             The filename of the config to be updated.
     * @param allowedHosts         A wildcard or a comma-separated list of hostnames or IPs (IPv4 or IPv6).
     * @param allowedUsers         A comma-separated list of PostgreSQL users. If no value is specified for this input,
     *                             all users will have access to the server.
     *
     */
    public static void changeProperty(String filename, String[] allowedHosts, String[] allowedUsers) throws IOException {

        if ((allowedHosts == null || allowedHosts.length == 0) && (allowedUsers == null || allowedUsers.length == 0)) {
            return;
        }

        final File file = new File(filename);
        final File tmpFile = new File(file + ".tmp");

        PrintWriter pw = new PrintWriter(tmpFile);
        BufferedReader br = new BufferedReader(new FileReader(file));

        Object[] allowedArr = new Object[allowedHosts.length * allowedUsers.length];
        boolean[] skip = new boolean[allowedArr.length];
        int ctr = 0;
        for(int i = 0; i < allowedHosts.length; i++){
            for (int j = 0; j < allowedUsers.length; j++) {
                allowedArr[ctr++] = new String[] { allowedHosts[i], allowedUsers[j] };
            }
        }

        for (String line; (line = br.readLine()) != null; ) {
            if (line.startsWith("host")) {
                for (int x = 0; x < allowedArr.length; x++) {
                    if (!skip[x]) {
                        String[] allowedItem = (String[]) allowedArr[x];
                        if (line.contains(allowedItem[0]) && line.contains(allowedItem[1])) {
                            skip[x] = true;
                            break;
                        }
                    }
                }
            }
            pw.println(line);
        }

        StringBuilder addUserHostLineBuilder  = new StringBuilder();
        for (int x = 0; x < allowedArr.length; x++) {
            if (!skip[x]) {
                String[] allowedItem = (String[]) allowedArr[x];
                addUserHostLineBuilder.append("host").append("\t").append("all").append("\t").append(allowedItem[1])
                        .append("\t").append(allowedItem[0]).append("\t").append("trust").append("\n");
            }
        }
        pw.write(addUserHostLineBuilder.toString());

        br.close();
        pw.close();
        file.delete();
        tmpFile.renameTo(file);

    }

}
