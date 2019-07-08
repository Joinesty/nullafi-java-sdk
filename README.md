<!-- Nullafi Java SDK
=============== -->

<!-- A Java interface to the [Nullafi API](http://enterprise-api.nullafi.com/docs).

- [Installation](#installation)
- [Getting Started](#getting-started)
- [Copyright and License](#copyright-and-license) -->


Installation
------------

Nullafi SDK is supported on Java SE Development Kit >= 8.

Getting Started
---------------
To get started with the SDK as a new developer, one must create a developer account. Go to the 
<a href="https://dashboard.nullafi.com/signup" target="_blank">Nullafi Signup Page</a>, and create a new developer account. As an account owner, you can retrieve the API key by going to the settings page, and selecting the 'API Key' tab. You may manage API key generation for the SDK from here. Create a new key and store the key value somewhere secure, as Nullafi will not store this key.

**Note:** Make sure to implement the nullafi-sdk in back end products only. Implementing the nullafi key on a front end product will expose the key to the public, and risk exposing private data.
```java
public class Main {

  private static final String API_KEY = "YOUR_API_KEY";

  public static void main(String[] args) throws Exception {
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

Authentication
------------
When a client is created, the client instance will be authenticated for a 60 minute period. After this time, you may either create a new client or refresh the existing client. 
```java
client.authenticate(API_KEY);
```


Static Vaults
------------
Static vaults are used to hold all created aliases for non transactional data. Static Vaults can be managed through the Static Vault class.

There is no limit on how many types of data may be stored in one static vault. It is up to users to determine how to split their data into vaults. Note that the master key must be stored to retrieve the vault at later times.  
A Static Vault can be created like this:
```java
private StaticVault createStaticVault(Client client) throws Exception {
    String name = "Sample Static Vault Name";

    StaticVault staticVault = client.createStaticVault(name, tags);
    System.out.println("**** StaticVaultExample.createStaticVault:");
    System.out.println("-> Id: " + staticVault.getId());
    System.out.println("-> Name: " + staticVault.getName());
    System.out.println("-> MasterKey: " + staticVault.getMasterKey());
    System.out.println("\n");

    return staticVault;
  }
```
The **ID** as well as the **Master Key** from the output will be used to retrieve the vault. These values must be stored in your database to retrieve the vault.
Retrieving a vault looks like this: 

```java
private StaticVault retrieveStaticVault(Client client, String id, String masterKey) throws Exception {
  StaticVault staticVault = client.retrieveStaticVault(id, masterKey);
  System.out.println("**** StaticVaultExample.retrieveStaticVault:");
  System.out.println("-> Id: " + staticVault.getId());
  System.out.println("-> Name: " + staticVault.getName());
  System.out.println("-> MasterKey: " + staticVault.getMasterKey());
  System.out.println("\n");

  return staticVault;
}
```

You can also delete a vault using the vault ID. Deleting the vault will also remove all aliases stored within, so make sure data is properly saved before deleting a vault. Deleting a vault will return a response with a key of 'ok' and a boolean value. 

```java
private boolean deleteStaticVault(Client client, String id) throws Exception {
  boolean deleted =  client.deleteStaticVault(id);
  System.out.println("**** AddressExample.delete:");
  System.out.println("-> Success: " + deleted);
  System.out.println("\n");

  return deleted;
}
```

Static Data Types
------------
### Address
Generates a fake address that will not trace to a real location. An optional parameter of state may be provided to choose the state associated with the fake address.

Address example:
```java
// Example format/output
// street, city, state abbreviation zipcode, USA
// 43520 Hills Flat, East Aricchester, AK 99761, USA

// example call
String name = "138 Congress St, Portland, ME 04101";
String state = "ME";
List<String> tags = new ArrayList<>();
tags.add("my-address-tag1");
tags.add("my-address-tag2");

AddressResponse created = vault.getAddressManager().create(name, state, tags);
```

Providing an incorrect state abbreviation will return a random state. The list of acceptable inputs is below.
```text
'AK', 'AL', 'AR', 'AZ', 'CA', 'CO', 'CT', 'DC', 'DE', 'FL', 'GA', 'HI', 'IA', 'ID', 'IL', 'IN', 'KS', 'KY',
'LA', 'MA', 'MD', 'ME', 'MI', 'MN', 'MO', 'MS', 'MT', 'NC', 'ND', 'NE', 'NH', 'NJ', 'NM', 'NV', 'NY', 'OH',
'OK', 'OR', 'PA', 'RI', 'SC', 'SD', 'TN', 'TX', 'UT', 'VA', 'VT', 'WA', 'WI', 'WV', 'WY'
```
### Date of birth
Will generate a new date between the year span of 1949 and 2001. Year(YYYY) and month(MM) are both optional parameters that will set the date to the corresponding year and/or month. 

Date of birth example:
```java
// example format/output
// YYYY-MM-DD
// 1980-12-20

//providing the optional year and month arguments 
String name = "1999-07-02";
Integer year = 1999;
Integer month = 7;
List<String> tags = new ArrayList<>();
tags.add("my-dob-tag1");
tags.add("my-dob-tag2");

DateOfBirthResponse created = vault.getDateOfBirthManager().create(name, year, month, tags);
```
### Driver's license
Generates a randomly generated combination of numbers and letters based on the format of each state's format. A state may be provided as an optional parameter to return a license for that state. A list of formats may be viewed [**here**](https://ntsi.com/drivers-license-format/).

Providing an incorrect state abbreviation will return a random state. The list of acceptable inputs is below.
```text
'AK', 'AL', 'AR', 'AZ', 'CA', 'CO', 'CT', 'DC', 'DE', 'FL', 'GA', 'HI', 'IA', 'ID', 'IL', 'IN', 'KS', 'KY',
'LA', 'MA', 'MD', 'ME', 'MI', 'MN', 'MO', 'MS', 'MT', 'NC', 'ND', 'NE', 'NH', 'NJ', 'NM', 'NV', 'NY', 'OH',
'OK', 'OR', 'PA', 'RI', 'SC', 'SD', 'TN', 'TX', 'UT', 'VA', 'VT', 'WA', 'WI', 'WV', 'WY'
```

Example call: 
```java
//example call with optional state
String name = "123456789";
String state = "NY";
List<String> tags = new ArrayList<>();
tags.add("my-driversLicense-tag1");
tags.add("my-driversLicense-tag2");

DriversLicenseResponse created = vault.getDriversLicenseManager().create(name, state, tags);
```
### First name
Generates a random name with the optional input of gender. 

Genders available are:
```text
"male", 
"female"
```
Example call:
```java
FirstNameResponse created = vault.getFirstNameManager().create("John");
```
### Gender
Generates a random gender from a list.
Output options are: 
```text
"Male",
"Female",
"Other",
"Don't want to say"
```

Example call:
```java
GenderResponse created = vault.getGenderManager().create("male");
```
### Generic
Generic takes a regular expression as input and will generate a value matching that expression. Use this to create formats not currently supported. Some example usages are for prescriptions, nations, and non-supported passport numbers. The template used to generate values will not be saved.

Example Generic Values:
```java
// input
// \d{4}
// output
// 1234
// input
// [a-zA-Z]{5}
// output
// AbCde

//example call
GenericResponse created = vault.getGenericManager().create("Abcde", "[a-zA-Z]{5}");
```

### Last name
Generates a random last name with optional input of gender. 

Example call:
```java
//example call
LastNameResponse created = vault.getLastNameManager().create("smith");
```
### Passport number
Generates a random nine digit number. Currently only generates formats matching US passports.

Example call:
```java
//example call
PassportResponse created = await vault.getPassportManager().create('123456789');
```
### Place of birth
Generates a random place of birth. An optional parameter of state may be provided to choose the state associated with the place of birth.

Place of birth example:
```java
// example format/output
// city, state
// Odachester, Washington

//example call with optional state param
String name = "Atlanta, Georgia";
String state = "GA";
List<String> tags = new ArrayList<>();
tags.add("my-pob-tag1");
tags.add("my-pob-tag2");

PlaceOfBirthResponse created = vault.getPlaceOfBirthManager().create(name, state, tags);
```

Providing an incorrect state abbreviation will return a random state. The list of acceptable inputs is below.
```text
'AK', 'AL', 'AR', 'AZ', 'CA', 'CO', 'CT', 'DC', 'DE', 'FL', 'GA', 'HI', 'IA', 'ID', 'IL', 'IN', 'KS', 'KY',
'LA', 'MA', 'MD', 'ME', 'MI', 'MN', 'MO', 'MS', 'MT', 'NC', 'ND', 'NE', 'NH', 'NJ', 'NM', 'NV', 'NY', 'OH',
'OK', 'OR', 'PA', 'RI', 'SC', 'SD', 'TN', 'TX', 'UT', 'VA', 'VT', 'WA', 'WI', 'WV', 'WY'
```
### Race
Generates a random race from a list. 

Race example:
```java
RaceResponse created = vault.getRaceManager().create("Native Hawaiian or Other Pacific Islander");
```

Output options are: 
```text
"American Indian or Alaska Native",
"Asian",
"Black or African American",
"Native Hawaiian or Other Pacific Islander",
"White",
"Other",
"Hispanic or Latino"
``` 
### Random
Generates a random string value consisting of upper and lower case letters that will be 20, 35, 50, or 80 characters long. Similar to the generic generator, but does not need a template.

Random example:
```java
RandomResponse created = vault.getRandomManager().create("random value");
```
### Social security number
Generates a random social security number. An optional parameter of state may be provided to choose the state used to generate the ssn.

Output format:
```Java
// output format
// ###-##-####

//example call
SsnResponse created = vault.getSsnManager().create("123-45-6789");
```
### Tax payer ID
Generates a random tay payer ID. Currently only produces ITIN(Individual Taxpayer Identification Number) values.

Output format: 
```java
// 9#-##-####

//example call
TaxpayerResponse created = vault.getTaxpayerManager().create("92-45-6789");
```
### Vehicle registration
Generates a random vehicle registration. Vehicle registration is 3 Capitalized letters followed by 4 digits.

Example Output: 
```java
// example output
// ABC·1234

//example call
VehicleRegistrationResponse created = vault.getVehicleRegistrationManager().create("DFE-6789");
```

Communication Vaults
------------
Communicataion vaults will store aliases for data types that will need to maintain their transactional integrity. Creating a communication vault is a similar process to a static vault, but the data aliased inside will be different. 

The alias generated for communication emails will be a functioning email. Nullafi will handle receiving messages to this address and relaying them to the real email address. White list senders and domains are added to control who may contact these users. Control for these emails may be found in the <a href="https://dashboard.nullafi.com/login" target="_blank">Nullafi Dashboard</a> under the **'System'** tab.

```java
private CommunicationVault createCommunicationVault(Client client) throws Exception {
  String name = "Sample Communication Vault Name";
  List tags = new ArrayList<String>();
  tags.add("'my-tag-1");
  tags.add("'my-tag-2");

  CommunicationVault communicationVault = client.createCommunicationVault(name, tags);
  System.out.println("**** CommunicationVaultExample.createCommunicationVault:");
  System.out.println("-> Id: " + communicationVault.getId());
  System.out.println("-> Name: " + communicationVault.getName());
  System.out.println("-> MasterKey: " + communicationVault.getMasterKey());
  System.out.println("\n");

  return communicationVault;
}
```
The **ID** as well as the **Master Key** from the output will be used to retrieve the vault. These values must be stored in your database to retrieve the vault.
Retrieving a vault looks like this: 

```java
private CommunicationVault retrieveCommunicationVault(Client client, String id, String masterKey) throws Exception {
  CommunicationVault communicationVault = client.retrieveCommunicationVault(id, masterKey);
  System.out.println("**** CommunicationVaultExample.retrieveCommunicationVault:");
  System.out.println("-> Id: " + communicationVault.getId());
  System.out.println("-> Name: " + communicationVault.getName());
  System.out.println("-> MasterKey: " + communicationVault.getMasterKey());
  System.out.println("\n");

  return communicationVault;
}
```

You can also delete a vault using the vault ID. Deleting the vault will also remove all aliases stored within, so make sure data is properly saved before deleting a vault. Deleting a vault will return a response with a key of 'ok' and a boolean value. 

```java
private boolean deleteCommunicationVault(Client client, String id) throws Exception {
  boolean deleted =  client.deleteCommunicationVault(id);
  System.out.println("**** AddressExample.delete:");
  System.out.println("-> Success: " + deleted);
  System.out.println("\n");

  return deleted;
}
```

Communication Data Types
------------
### Email
Generating email aliases will provide a new functional email to use in place of the real email. These alias addresses will work as relays to the real address, while also providing the ability to white list approved sender domains and addresses. 

Email example:
```java
// input
// realEmail@gmail.com
// output
// cizljfhxrazvcy@fipale.com

// example call
EmailResponse created = vault.getEmailManager().create("email@domain.com");
```

Copyright and License
---------------------

Copyright 2019 Joinesty, Inc. All rights reserved.