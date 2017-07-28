package de.arraying.arraybot.misc

import com.zaxxer.hikari.HikariDataSource
import de.arraying.arraybot.cache.entities.iface.Cachable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*

/**
 * Copyright 2017 Arraying
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class SQLQuery(query: String,
               dataSource: HikariDataSource) {

    private val statement: PreparedStatement = dataSource.connection.prepareStatement(query)

    /**
     * Sets the parameters.
     */
    fun with(vararg parameters: Any?): SQLQuery {
        for(i in 0..parameters.size-1) {
            statement.setObject(i+1, parameters[i])
        }
        return this
    }

    /**
     * Executes the select query.
     */
    fun select(target: Class<out Cachable>): Array<out Any> {
        if(target.constructors.isEmpty()) {
            throw IllegalArgumentException("There must be at least one constructor for the provided class.")
        }
        val constructor = target.constructors[0]
        val constructorParameters = constructor.parameters.size
        val objects = LinkedList<Any>()
        val resultSet = statement.executeQuery()
        while(resultSet.next()) {
            val values = (1..constructorParameters).mapTo(LinkedList<Any?>()) {
                resultSet.getObject(it)
            }
            val instance = constructor.newInstance(*values.toTypedArray())
            objects.add(instance)
        }
        resultSet.statement.connection.close()
        return objects.toTypedArray()
    }

    /**
     * Executes the select query, allowing custom result set handling.
     */
    fun select(consumer: (ResultSet) -> Unit) {
        val resultSet = statement.executeQuery()
        consumer.invoke(resultSet)
        if(!resultSet.statement.connection.isClosed) {
           resultSet.statement.connection.close()
        } else if(!resultSet.statement.isClosed) {
            resultSet.statement.close()
        } else if(!resultSet.isClosed) {
            resultSet.close()
        }
    }

    /**
     * Executes the insert query.
     */
    fun update() {
        statement.execute()
        statement.connection.close()
    }

}