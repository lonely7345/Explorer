/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.stratio.explorer.cassandra.lists;

import com.stratio.explorer.lists.CollectionsComparator;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Compare collections of InetSocketAddress
 */
public class InetSocketAddressComparator implements CollectionsComparator<InetSocketAddress> {

    /**
     * Compare two collections of InetSocketAddress
     * @param firstCollection
     * @param secondCollection
     * @return return true if not equals
     */
    @Override
    public boolean notEquals(Collection<InetSocketAddress> firstCollection, Collection<InetSocketAddress> secondCollection) {
        return !(firstCollection.containsAll(secondCollection) && (firstCollection.size() == secondCollection.size()));
    }
}
