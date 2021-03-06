package com.iovation.launchkey.sdk.transport.domain; /**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class DirectoryV3DevicesListPostResponseTest {
    @Test
    public void getDevices() throws Exception {
        List<DirectoryV3DevicesListPostResponseDevice> expected =
                Collections.singletonList(
                        new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 0, null, null));
        DirectoryV3DevicesListPostResponse response = new DirectoryV3DevicesListPostResponse(expected);
        List<DirectoryV3DevicesListPostResponseDevice> actual = response.getDevices();
        assertEquals(expected, actual);
    }

    @Test
    public void equalsIsTrueForEqualObjects() throws Exception {
        UUID uuid = UUID.randomUUID();
        DirectoryV3DevicesListPostResponseDevice device =
                new DirectoryV3DevicesListPostResponseDevice(uuid, "name", "type", 0, null, null);
        DirectoryV3DevicesListPostResponseDevice other =
                new DirectoryV3DevicesListPostResponseDevice(uuid, "name", "type", 0, null, null);

        assertTrue(device.equals(other));
    }

    @Test
    public void equalsIsFalseForUnequalObjects() throws Exception {
        DirectoryV3DevicesListPostResponseDevice device =
                new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 0, null, null);
        DirectoryV3DevicesListPostResponseDevice other =
                new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 0, null, null);

        assertFalse(device.equals(other));
    }

    @Test
    public void hashCodeForEqualObjectsIsEqual() throws Exception {
        DirectoryV3DevicesListPostResponseDevice device =
                new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 0, null, null);

        assertEquals(
                new DirectoryV3DevicesListPostResponse(Collections.singletonList(device)).hashCode(),
                new DirectoryV3DevicesListPostResponse(Collections.singletonList(device)).hashCode()
        );
    }

    @Test
    public void hashCodeForUnequalObjectsIsNotEqual() throws Exception {
        assertNotEquals(
                new DirectoryV3DevicesListPostResponse(Collections.singletonList(
                        new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 0, null, null)
                )).hashCode(),
                new DirectoryV3DevicesListPostResponse(Collections.singletonList(
                        new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 0, null, null)
                )).hashCode()
        );
    }
}