Executive Summary

This report aims to state the specification of the ZIPZAP mobile application. Will be included in this report the technical tools applied and the way the application was structured, what it will offer to the users. It will go in depth of why we decided to pursue this idea and its purpose. The technical implementation and technologies used to make the piece of software a useful tool.
1.0	Introduction


1.1.	Background

This software project was chosen to improve the technology to integrate digital public transport payments, taking advantage of the ubiquity of smartphones in modern society*(reference of slide on mid presentation). Since there is currently no existing software in Ireland that offers this service of having a virtual card uploaded to our phones. In Ireland we have mobile applications for bus, rail timetables and top up leap card but this application was designed to integrate all the public services transport services in one app where public transport users would have a better experience.

1.2.	Aims

All people in Republic of Ireland (ROI) who wants to use public transport can rely on ZipZap to pay transport fares without having to worry about being in possession of a physical travel card.
NTA (National Transport Authority) is the government party who regulates transport systems in Ireland, including buses, rail, and Luas. To get the application functional on public transportation NTA must send us the software code of the existing payment machines to connect with our application, now passengers can use Leap Card and ZipZap to pay the fares but. We are developing a beta version of the application to present this project for NTA.
ZipZap is not limited just to public transport but can be exported to other commercial uses where the technology could be suitable, such as loyalty schemes, products and services that accept digital payments, access events with digital tickets in the form of QR codes and so on. 
The business plan is in a separate document where we are detailing all our objectives, goals, and marketing plan to make this application profitable.

1.3.	Technology

The QR code is an enhanced barcode. While the barcode holds information horizontally, the QR code does so both horizontally and vertically. This enables the QR code to hold over a hundred times more information (www.tech.gov.sg, 2020).
There will be a fixed QR code that can be scanned by potential passengers after successfully logging in; once the code has been scanned, the ZipZap account that the user is logged into will be deducted €2 in credit. To make this feature possible, Android Studio was the IDE (Integrated Development Environment) used for programming. Through this IDE, various packages/libraries such as ZXing for the generation and scanning of QR codes, the Google Pay API (along with classes such as PaymentData) was used to request, accept, and process digital payments to add credit to a given account.
The application is divided into Costumer application (general public) and Admin application (where reads QR Code). To manipulate all this data was used mostly Java code language developed in Android Studio IDE (Integrate Development Environment), an open-source software for Android operating system developed by Google and JetBrains. 
The database used was Firebase. Firebase has created a massive buzz in the developer community. Most of the traditional backend services are quite easy to implement and get into production. Firebase is the best fit when there is a short development time, and the application demands data in real time as it is easy to scale. (Ashok Kumar S, 2018).
1.4.	Structure

The application is divided into activities/classes which encapsulate all the interfaces involved in the application such as:
Register – for creating a ZipZap account, this account is then added to Firebase and given a unique user ID.
LoginActivity – for logging in with an existing account.
Scanner – this is where a user can scan a QR code, which then returns a randomly generated user ID, the user’s email address, previous balance, and current balance.
TopUp – a user is given four top-up options: €5, €10, €15, €20. The value of the option is passed to the transaction details after clicking the Pay button.
Info – this serves as an About Me page which will state information about the application.
![Picture3](https://github.com/AloisioPjr/MyZipZapApp/assets/22481231/ad532984-3cf0-4722-9b45-6dbc9d022baf)

![Picture4](https://github.com/AloisioPjr/MyZipZapApp/assets/22481231/2718a5e0-c81c-42e0-ad52-9594370ad3b5)

![Picture5](https://github.com/AloisioPjr/MyZipZapApp/assets/22481231/fd38ee77-fb0f-49d8-a60d-8f87dc3717c2)
![Picture6](https://github.com/AloisioPjr/MyZipZapApp/assets/22481231/62e9d56d-f83c-4226-bb31-d271feeed426)
![Picture7](https://github.com/AloisioPjr/MyZipZapApp/assets/22481231/92c6df87-5a46-43c2-87bc-806786c1720d)
![Picture8](https://github.com/AloisioPjr/MyZipZapApp/assets/22481231/ab676c05-1b24-4fb5-90a3-6d4507e23ef5)
