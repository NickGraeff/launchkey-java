/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 *
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.util.Date;

/**
 * Response data from a "ping" call
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PingResponse {

    /**
     * When the public key was generated. Returned only if a key is returned.
     */
    private final Date dateStamp;

    /**
     * When the Ping response was generated by LaunchKey Engine.
     */
    private final Date launchKeyTime;

    /**
     * RSA Public key string returned by the Ping call.
     */
    private final String publicKey;

    /**
     * @return When the public key was generated. Returned only if a key is returned.
     */
    public final Date getDateStamp() {
        return dateStamp;
    }

    /**
     * @return When the Ping response was generated by from LaunchKey Engine.
     */
    public final Date getLaunchKeyTime() {
        return launchKeyTime;
    }

    /**
     * @return RSA Public key string returned bye the Ping call.
     */
    public final String getPublicKey() {
        return publicKey;
    }

    /**
     * @param dateStamp When the public key was generated. Returned only if a key is returned.
     * @param launchKeyTime When the Ping response was generated by LaunchKey Engine.
     * @param publicKey RSA Public key string returned by the Ping call.
     * @throws ParseException
     */
    @JsonCreator
    public PingResponse(@JsonProperty("date_stamp") String dateStamp, @JsonProperty("launchkey_time") String launchKeyTime, @JsonProperty("key") String publicKey) throws ParseException {
        this.dateStamp = LaunchKeyDateFormat.getInstance().parseObject(dateStamp);
        this.launchKeyTime = LaunchKeyDateFormat.getInstance().parseObject(launchKeyTime);
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "PingResponse{" +
                "dateStamp=" + dateStamp +
                ", launchKeyTime=" + launchKeyTime +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PingResponse)) return false;

        PingResponse that = (PingResponse) o;

        if (dateStamp != null ? !dateStamp.equals(that.dateStamp) : that.dateStamp != null) return false;
        if (launchKeyTime != null ? !launchKeyTime.equals(that.launchKeyTime) : that.launchKeyTime != null) return false;
        return !(publicKey != null ? !publicKey.equals(that.publicKey) : that.publicKey != null);

    }

    @Override
    public int hashCode() {
        int result = dateStamp != null ? dateStamp.hashCode() : 0;
        result = 31 * result + (launchKeyTime != null ? launchKeyTime.hashCode() : 0);
        result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
        return result;
    }
}
