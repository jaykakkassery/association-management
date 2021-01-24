# association-management
Association Management

- Pull the latest
- Run local mysql
- setup the project in intelliJ
- Right click on build.sbt and click Run. (At present it wont run from sbt console. Need to check that)
- Now evolution runs and table gets created and app run on localhost:9000
- addAssociation post request from Postman
  http://localhost:9000/addAssociation
  {
   "id": 0,
  "na me": "JUMA",
  "address1": "2255 Lodge Ct",
  "address2": "Copper",
  "city": "Cumming",
  "state": "GA",
  "zip": "30041",
  "contactNo": 6787029647,
  "email": "jp@gmail.com"
  }
  - addUserPerformance POST request from postmane
  http://localhost:9000/addUserPerformance
 
  {
    "id": 0,
    "userId": 1,
    "associationId": 4,
     "eventId": 1,
    "performanceName": "Pattu padi uraka",
    "performanceOrder": 1
  }

