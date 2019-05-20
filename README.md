Nullafi Java SDK
===============

A Node.js interface to the [Nullafi API](http://enterprise-api.nullafi.com/docs).

- [Installation](#installation)
- [Getting Started](#getting-started)
- [Copyright and License](#copyright-and-license)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

Instalation
------------
- Import the JAR Package File (`lib/nullafi-java-sdk-1.0-SNAPSHOT-jar-with-dependencies.jar`) to your Application

Pre Requsites
------------

- Java SE Development Kit >= 8 

Getting Started
---------------

To get started with the SDK, get a API Key from the Configuration page
of your app in the [Settings Page - API Key][settings-api-key].
You can use this token to make calls for your own Nullafi account.

```java
import com.joinesty.Client;
import com.joinesty.NullafiSDK;
import com.joinesty.domains.staticVault.StaticVault;
import com.joinesty.domains.staticVault.managers.firstName.FirstNameManager;
import com.joinesty.domains.staticVault.managers.firstName.FirstNameResponse;

public class Main {

    private static final String API_KEY = "YOUR_API_KEY";

    public static void main(String[] args) {
        // Initialize the SDK with your API credentials
        NullafiSDK nullafiSDK = new NullafiSDK(API_KEY);

        // Create a basic API client, which does not automatically refresh the access token
        Client client = nullafiSDK.createClient();

        // Get your own user object from the Nullafi API
        // All client methods return a promise that resolves to the results of the API call,
        // or rejects when an error occurs
        final String name = "my-static-vault";
        final String[] tags = new String[1];

        StaticVault staticVault = client.createStaticVault(name, tags);

        final String firstName = "John Doe";
        final String[] firstNameTags = new String[1];
        FirstNameManager firstNameManager = new FirstNameManager(staticVault);
        FirstNameResponse firstNameResponse = firstNameManager.create(firstName, firstNameTags);
        System.out.println(firstNameResponse.toString());

        /*
            output example:
            FirstNameResponse(
                id=4b3d19db-ee07-4f65-9104-3380c9180bb8,
                firstnameToken=Danny,
                firstname=ba5dRFldazo=,
                iv=MBYPhs7hYnZbq8/jFv8m/Q==,
                authTag=v9fCucCcyxihMqgXp8ZsXQ==,
                tags=[null],
                createdAt=Mon May 20 12:05:53 BRT 2019,
                updatedAt=null
            )
         */

    }
}

```

[settings-api-key]: https://dashboard.nullafi.com/admin/settings/api


Copyright and License
---------------------

Copyright 2019 Joinesty, Inc. All rights reserved.
