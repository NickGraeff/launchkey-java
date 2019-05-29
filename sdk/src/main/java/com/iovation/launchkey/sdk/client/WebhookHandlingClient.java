package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.domain.webhook.AuthorizationResponseWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.ServiceUserSessionEndWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;
import com.iovation.launchkey.sdk.error.*;

import java.util.List;
import java.util.Map;

public interface WebhookHandlingClient {
    /**
     * Handle a server side event callback
     *
     * @param headers A generic map of request headers. These will be used to access and validate the JWT.
     * @param body The body of the webhook request. This will be used to determine the data for the webhook and have be
     * validated against the JWT.
     * @return Returns a {@link ServiceUserSessionEndWebhookPackage} when the server sent event was initialed from the
     * user logging out from a linked mobile device, a {@link DirectoryClient#endAllServiceSessions(String)} request, or an
     * {@link AuthorizationResponseWebhookPackage} when the server sent event was initiated by the user
     * responding to a {@link ServiceClient#authorize(String)} request.
     * @throws NoKeyFoundException When the Entity and Key ID identifying the public key used to encrypt authorization
     * response is not found in the known keys mapping.
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * @deprecated Please use {@link #handleWebhook(Map, String, String, String)}
     */
    @Deprecated
    WebhookPackage handleWebhook(Map<String, List<String>> headers, String body)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException;

    /**
     * Handle a server side event callback
     *
     * @param headers A generic map of request headers. These will be used to access and validate the JWT.
     * @param body The body of the webhook request. This will be used to determine the data for the webhook and have be
     * validated against the JWT.
     * @param method The method of the webhook request. This will be validated against the JWT.
     * @param path The body of the webhook request. This will be validated against the JWT.
     * @return Returns a {@link ServiceUserSessionEndWebhookPackage} when the server sent event was initialed from the
     * user logging out from a linked mobile device, a {@link DirectoryClient#endAllServiceSessions(String)} request, or an
     * {@link AuthorizationResponseWebhookPackage} when the server sent event was initiated by the user
     * responding to a {@link ServiceClient#authorize(String)} request.
     * @throws NoKeyFoundException When the Entity and Key ID identifying the public key used to encrypt authorization
     * response is not found in the known keys mapping.
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    WebhookPackage handleWebhook(Map<String, List<String>> headers, String body, String method, String path)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException;
}
