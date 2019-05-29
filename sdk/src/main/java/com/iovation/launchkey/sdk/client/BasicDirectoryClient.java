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

package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.crypto.Crypto;
import com.iovation.launchkey.sdk.crypto.JCECrypto;
import com.iovation.launchkey.sdk.domain.PublicKey;
import com.iovation.launchkey.sdk.domain.directory.Device;
import com.iovation.launchkey.sdk.domain.directory.DeviceStatus;
import com.iovation.launchkey.sdk.domain.directory.DirectoryUserDeviceLinkData;
import com.iovation.launchkey.sdk.domain.directory.Session;
import com.iovation.launchkey.sdk.domain.servicemanager.Service;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import com.iovation.launchkey.sdk.domain.webhook.ServerSentEventSuccessfulDeviceLinkCompletionPackage;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;
import com.iovation.launchkey.sdk.error.*;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.*;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

public class BasicDirectoryClient extends ServiceManagingBaseClient implements DirectoryClient {
    public final Transport transport;
    public final EntityIdentifier directory;

    public BasicDirectoryClient(UUID directoryId, Transport transport) {
        this.transport = transport;
        this.directory = new EntityIdentifier(EntityIdentifier.EntityType.DIRECTORY, directoryId);
    }

    public DirectoryUserDeviceLinkData linkDevice(String userId) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidCredentialsException,
            CommunicationErrorException, MarshallingError, CryptographyError {
        return linkDevice(userId, null);
    }

    public DirectoryUserDeviceLinkData linkDevice(String userId, Integer ttl) throws PlatformErrorException,
            UnknownEntityException, InvalidResponseException, InvalidStateException, InvalidCredentialsException,
            CommunicationErrorException, MarshallingError, CryptographyError {
        DirectoryV3DevicesPostRequest request = new DirectoryV3DevicesPostRequest(userId, ttl);
        DirectoryV3DevicesPostResponse response = transport.directoryV3DevicesPost(request, directory);
        return new DirectoryUserDeviceLinkData(response.getCode(), response.getQRCode(), response.getDeviceId());
    }

    @Override
    public List<Device> getLinkedDevices(String userId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        DirectoryV3DevicesListPostRequest request = new DirectoryV3DevicesListPostRequest(userId);
        DirectoryV3DevicesListPostResponse response = transport.directoryV3DevicesListPost(request, directory);
        List<Device> devices = new ArrayList<>();
        for (DirectoryV3DevicesListPostResponseDevice responseDevice : response.getDevices()) {
            devices.add(new Device(
                    responseDevice.getId().toString(),
                    responseDevice.getName(),
                    DeviceStatus.fromCode(responseDevice.getStatus()),
                    responseDevice.getType(),
                    responseDevice.getCreated(),
                    responseDevice.getUpdated()
            ));
        }
        return devices;
    }

    @Override
    public void unlinkDevice(String userId, String deviceId) throws PlatformErrorException,
            UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError, CryptographyError {
        UUID deviceUUID;
        try {
            deviceUUID = UUID.fromString(deviceId);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid Device ID", e);

        }
        DirectoryV3DevicesDeleteRequest request = new DirectoryV3DevicesDeleteRequest(userId, deviceUUID);
        transport.directoryV3devicesDelete(request, directory);
    }

    @Override
    public List<Session> getAllServiceSessions(String userId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final DirectoryV3SessionsListPostRequest request = new DirectoryV3SessionsListPostRequest(userId);
        final DirectoryV3SessionsListPostResponse response = transport.directoryV3SessionsListPost(request, directory);
        List<Session> sessions = new ArrayList<>();
        for (DirectoryV3SessionsListPostResponseSession session : response.getSessions()) {
            sessions.add(new Session(session.getServiceId(), session.getServiceName(), session.getServiceIcon(),
                    session.getAuthRequest(), session.getCreated()));
        }
        return sessions;
    }

    @Override
    public void endAllServiceSessions(String userId) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidCredentialsException,
            CommunicationErrorException, CryptographyError, MarshallingError {
        DirectoryV3SessionsDeleteRequest request = new DirectoryV3SessionsDeleteRequest((userId));
        transport.directoryV3SessionsDelete(request, directory);
    }

    @Override
    public UUID createService(String name, String description, URI icon, URI callbackURL, Boolean active)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServicesPostRequest request = new ServicesPostRequest(name, icon, description, callbackURL, active);
        final ServicesPostResponse response = transport.directoryV3ServicesPost(request, directory);
        return response.getId();
    }

    @Override
    public void updateService(UUID serviceId, String name, String description, URI icon, URI callbackURL, Boolean active)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServicesPatchRequest request = new ServicesPatchRequest(serviceId, name, description, icon, callbackURL, active);
        transport.directoryV3ServicesPatch(request, directory);
    }

    @Override
    public Service getService(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServicesListPostRequest request = new ServicesListPostRequest(Collections.singletonList(serviceId));
        final ServicesListPostResponse response = transport.directoryV3ServicesListPost(request, directory);
        ServicesListPostResponseService service = response.getServices().get(0);
        return new Service(service.getId(), service.getName(), service.getDescription(), service.getIcon(),
                service.getCallbackURL(), service.isActive());
    }

    @Override
    public List<Service> getServices(List<UUID> serviceIds)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServicesListPostRequest request = new ServicesListPostRequest(serviceIds);
        final ServicesListPostResponse response = transport.directoryV3ServicesListPost(request, directory);
        List<Service> services = new ArrayList<>();
        for (ServicesListPostResponseService service : response.getServices()) {
            services.add(new Service(service.getId(), service.getName(), service.getDescription(), service.getIcon(),
                    service.getCallbackURL(), service.isActive()));
        }
        return services;
    }

    @Override
    public List<Service> getAllServices()
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServicesGetResponse response = transport.directoryV3ServicesGet(directory);
        List<Service> services = new ArrayList<>();
        for (ServicesGetResponseService service : response.getServices()) {
            services.add(new Service(service.getId(), service.getName(), service.getDescription(), service.getIcon(),
                    service.getCallbackURL(), service.isActive()));
        }
        return services;
    }

    @Override
    public List<PublicKey> getServicePublicKeys(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        ServiceKeysListPostRequest request = new ServiceKeysListPostRequest(serviceId);
        final KeysListPostResponse response =
                transport.directoryV3ServiceKeysListPost(request, directory);
        List<PublicKey> publicKeys = new ArrayList<>();
        for (KeysListPostResponsePublicKey publicKey : response.getPublicKeys()) {
            publicKeys.add(new PublicKey(publicKey.getId(), publicKey.isActive(), publicKey.getCreated(),
                    publicKey.getExpires()));
        }
        return publicKeys;
    }

    @Override
    public String addServicePublicKey(UUID serviceId, RSAPublicKey publicKey, Boolean active, Date expires)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        String publicKeyPEM = JCECrypto.getPEMFromRSAPublicKey(publicKey);
        final ServiceKeysPostRequest request = new ServiceKeysPostRequest(serviceId, publicKeyPEM, expires, active);
        final KeysPostResponse response = transport.directoryV3ServiceKeysPost(request, directory);
        return response.getId();
    }

    @Override
    public void updateServicePublicKey(UUID serviceId, String keyId, Boolean active, Date expires)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        transport.directoryV3ServiceKeysPatch(new ServiceKeysPatchRequest(serviceId, keyId, expires, active),
                directory);
    }

    @Override
    public void removeServicePublicKey(UUID serviceId, String keyId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServiceKeysDeleteRequest request = new ServiceKeysDeleteRequest(serviceId, keyId);
        transport.directoryV3ServiceKeysDelete(request, directory);
    }

    @Override
    public ServicePolicy getServicePolicy(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        com.iovation.launchkey.sdk.transport.domain.ServicePolicy policy =
                transport.directoryV3ServicePolicyItemPost(new ServicePolicyItemPostRequest(serviceId), directory);

       return this.getDomainServicePolicyFromTransportServicePolicy(policy);
    }

    @Override
    public void setServicePolicy(UUID serviceId, ServicePolicy policy)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        com.iovation.launchkey.sdk.transport.domain.ServicePolicy transportPolicy =
                getTransportServicePolicyFromDomainServicePolicy(policy);
        transport.directoryV3ServicePolicyPut(new ServicePolicyPutRequest(serviceId, transportPolicy), directory);

    }

    @Override
    public void removeServicePolicy(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        transport.directoryV3ServicePolicyDelete(new ServicePolicyDeleteRequest(serviceId), directory);
    }

    @Override
    public WebhookPackage handleWebhook(Map<String, List<String>> headers, String body)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException {
        return handleWebhook(headers, body, null, null);
    }

    @Override
    public WebhookPackage handleWebhook(Map<String, List<String>> headers, String body, String method, String path)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException {
        ServerSentEvent transportResponse = transport.handleServerSentEvent(headers, method, path, body);
        WebhookPackage response;
        if (transportResponse == null) {
            response = null;
        } else if (transportResponse instanceof ServerSentEventSuccessfulDeviceLinkCompletion) {
            UUID deviceId = UUID.fromString(((ServerSentEventSuccessfulDeviceLinkCompletion) transportResponse).getDeviceId());
            String publicKeyId = ((ServerSentEventSuccessfulDeviceLinkCompletion) transportResponse).getPublicKeyId();
            String publicKey = ((ServerSentEventSuccessfulDeviceLinkCompletion) transportResponse).getPublicKey();
            response = new ServerSentEventSuccessfulDeviceLinkCompletionPackage(
                    deviceId,
                    publicKeyId,
                    publicKey
            );
        } else {
            throw new InvalidRequestException("Unknown response type was returned by the transport", null, null);
        }
        return response;
    }
}
