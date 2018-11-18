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