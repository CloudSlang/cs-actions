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



package io.cloudslang.content.database.services.dbconnection;

/**
 * Created by victor on 13.01.2017.
 */
public class PooledDataSourceCleaner implements Runnable {
    //logging
//    protected static final Log logger = LogFactory.getLog(PooledDataSourceCleaner.class);

    //interval when this cleaner wakes up in seconds
    //this number is configurable
    private long interval = 60 * 60 * 12; // 12 hours

    //DBConnectionPoolManager handle
    private DBConnectionManager manager = null;
    private STATE_CLEANER state = STATE_CLEANER.SHUTDOWN;

    /**
     * constructor
     *
     * @param aManager  a ref to DBConnectionManager
     * @param aInterval an interval in seconds for cleaner to run
     */
    PooledDataSourceCleaner(DBConnectionManager aManager, long aInterval) {
        this.manager = aManager;
        //can be configured in databasePooling.properties
        //db.connectionpool.clean.interval
        this.interval = aInterval;
    }

    /**
     * wake up and clean the pools if the pool has empty connection table.
     */
    public void run() {
//    todo    if (logger.isDebugEnabled()) {
//            logger.debug("start running PooledDataSourceCleaner");
//        }

        state = STATE_CLEANER.RUNNING;
        while (state != STATE_CLEANER.SHUTDOWN) {
            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
//              todo  if (logger.isDebugEnabled()) {
//                    logger.debug("Get interrupted, shutdown the PooledDataSourceCleaner");
//                }
                break;//get out if anyone interrupt
            }
            this.manager.cleanDataSources();

            //if no pool at all, going to stop itself
            if (this.manager.getDbmsPoolSize() == 0) {
//             todo   if (logger.isDebugEnabled()) {
//                    logger.debug("Empty pools, shutdown the PooledDataSourceCleaner");
//                }
                state = STATE_CLEANER.SHUTDOWN;//stop spinning
                break; //get out
            }
        }
    }

    /**
     * force shutdown and derefrence manager
     */
    protected void shutdown() {
//    todo    logger.info("Force shutdown the PooledDataSourceCleaner");
        state = STATE_CLEANER.SHUTDOWN;
        this.manager = null;
    }

    /**
     * @return the state of this runnable
     */
    protected STATE_CLEANER getState() {
        return this.state;
    }

    //state to indicate if this runnable is running or shutdown
    public enum STATE_CLEANER {
        RUNNING, SHUTDOWN
    }
}//end PooledDataSourceCleaner class
