/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shardingsphere.orchestration.reg.zookeeper.natived.client.zookeeper.base;

import io.shardingsphere.orchestration.reg.zookeeper.natived.client.action.IZookeeperProvider;
import io.shardingsphere.orchestration.reg.zookeeper.natived.client.retry.DelayPolicyExecutor;
import io.shardingsphere.orchestration.reg.zookeeper.natived.client.zookeeper.section.Connection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.zookeeper.KeeperException;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Base async retry operation.
 *
 * @author lidongbo
 */
@RequiredArgsConstructor
public abstract class BaseOperation implements Delayed {
    
    @Getter
    private final IZookeeperProvider provider;
    
    @Setter
    private DelayPolicyExecutor delayPolicyExecutor;
    
    @Override
    public final long getDelay(final TimeUnit timeUnit) {
        long absoluteBlock = this.delayPolicyExecutor.getNextTick() - System.currentTimeMillis();
        return timeUnit.convert(absoluteBlock, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public final int compareTo(final Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - delayed.getDelay(TimeUnit.MILLISECONDS));
    }

    protected abstract void execute() throws KeeperException, InterruptedException;
    
    /**
     * Queue precedence.
     *
     * @return whether or not continue enqueue
     * @throws KeeperException keeper exception
     * @throws InterruptedException interrupted exception
     */
    public boolean executeOperation() throws KeeperException, InterruptedException {
        boolean result;
        try {
            execute();
            result = true;
        } catch (final KeeperException ex) {
            if (Connection.needReset(ex)) {
                provider.resetConnection();
            }
            result = false;
        }
        if (!result && delayPolicyExecutor.hasNext()) {
            delayPolicyExecutor.next();
            return true;
        }
        return false;
    }
}
