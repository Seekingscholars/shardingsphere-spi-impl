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

package io.shardingsphere.transaction.saga.revert.snapshot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.util.Collection;

/**
 * Snapshot SQL context.
 *
 * @author zhaojun
 */
@RequiredArgsConstructor
@Getter
public class SnapshotSQLContext {
    
    private final Connection connection;
    
    private final String tableName;
    
    private final Collection<Object> parameters;
    
    private final Collection<String> queryColumnNames;
    
    private final String tableAlias;
    
    private final String whereClause;
    
    public SnapshotSQLContext(final Connection connection, final String tableName, final Collection<Object> parameters, final Collection<String> queryColumnNames, final String whereClause) {
        this(connection, tableName, parameters, queryColumnNames, null, whereClause);
    }
}
