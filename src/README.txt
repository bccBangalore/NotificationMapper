Installation Steps:

1) mvn spring-boot:run









1) Call SMS Service with payload as array of resolved messages & mob No : Approved
2) Template Specific models- Easy onboarding of new templates : Approved
3) Template Storage ?

---->
JSON Mandatory,Optional field validation
Validations on JSON, Error Handling
Specify subprops for model props

Remove Deserialisation-model creation from JSON object
Remove Template Id's- Rely on Template body
Track source system

Cloud URL:
http://Notification-ELB-380387493.us-east-1.elb.amazonaws.com/mappedNotification

Subscriber/Observer Pattern

Ex Payload:
{
  "templateId":"JathaNotification",
  "sewadars": [
    {

      "sewadarCode": "M1234",
      "sewadarName": "Rohit Wadhwa",
      "jathaVenue":"Beas",
      "sewaDepartment":"Accomodation",
      "sewaStartDate":"10th Dec, 2018",
      "sewaEndDate":"25th Dec, 2018",
      "mobileNo":"9902942170"
    },
    {

      "sewadarCode": "M1234",
      "sewadarName": "Kumaresh T",
      "jathaVenue":"Hyderabad",
      "sewaDepartment":"Langar",
      "sewaStartDate":"10th Dec, 2018",
      "sewaEndDate":"25th Dec, 2018",
      "mobileNo":"9902942170"
    }
  ]
}

1) Tracking retry per sms call along with sendDetail in every call - Done
2) FreeMarker template Exception Handling fr mandatory fields - Done
3) Logging                                                    - Done
4) Error Handling/Error Code Mapping fr front end app         - Done
5) Code segregation in modules                                - Done
6) Real Time Bhashsms.com                                     - Pending

7) Real time call testing with SmsNotification                - Done
8) AWS deployment

--
Send Exception Back to calling application