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

package io.shardingsphere.transaction.base.saga;

import com.google.common.collect.Lists;
import io.shardingsphere.transaction.base.context.TransactionContext;
import io.shardingsphere.transaction.base.context.TransactionContextHolder;
import lombok.SneakyThrows;
import org.apache.shardingsphere.core.constant.DatabaseType;
import org.apache.shardingsphere.transaction.core.ResourceDataSource;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SagaShardingTransactionManagerTest {
    
    private SagaShardingTransactionManager transactionManager;
    
    @Mock
    private DataSource dataSource;
    
    @Before
    public void setUp() {
        transactionManager = new SagaShardingTransactionManager();
    }
    
    @After
    public void tearDown() {
        TransactionContextHolder.clear();
    }
    
    @Test
    public void assertInit() {
        Collection<ResourceDataSource> resourceDataSources = Lists.newLinkedList();
        resourceDataSources.add(new ResourceDataSource("ds", dataSource));
        transactionManager.init(DatabaseType.MySQL, resourceDataSources);
        Map<String, DataSource> actual = getDataSourceMap();
        assertThat(actual.get("ds"), is(dataSource));
    }
    
    @SneakyThrows
    @SuppressWarnings("unchecked")
    private Map<String, DataSource> getDataSourceMap() {
        Field field = transactionManager.getClass().getDeclaredField("dataSourceMap");
        field.setAccessible(true);
        return (Map<String, DataSource>) field.get(transactionManager);
    }
    
    @Test
    public void assertGetTransactionType() {
        assertThat(transactionManager.getTransactionType(), is(TransactionType.BASE));
    }
    
    @Test
    public void assertIsInTransaction() {
        assertFalse(transactionManager.isInTransaction());
        TransactionContextHolder.set(new TransactionContext());
        assertTrue(transactionManager.isInTransaction());
    }
    
    @Test
    public void getConnection() {
    }
    
    @Test
    public void begin() {
    }
    
    @Test
    public void commit() {
    }
    
    @Test
    public void rollback() {
    }
    
    @Test
    public void close() {
    }
}