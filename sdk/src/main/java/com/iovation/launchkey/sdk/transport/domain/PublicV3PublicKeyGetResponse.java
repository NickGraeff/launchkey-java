/**
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

package com.iovation.launchkey.sdk.transport.domain;

public class PublicV3PublicKeyGetResponse {
    public final String publicKey;

    public final String publicKeyFingerprint;

    public PublicV3PublicKeyGetResponse(String publicKey, String publicKeyFingerprint) {
        this.publicKey = publicKey;
        this.publicKeyFingerprint = publicKeyFingerprint;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPublicKeyFingerprint() {
        return publicKeyFingerprint;
    }
}