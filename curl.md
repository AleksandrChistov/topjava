# Test Meals by CURL
## get
`curl -v http://localhost:8080/topjava/rest/meals/100003`
## delete
`curl -v -X DELETE http://localhost:8080/topjava/rest/meals/100003`
## update
`curl -v -X PUT -d "{\"dateTime\":\"2024-08-22T20:05\",\"description\":\"Eggs\",\"calories\":\"300\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/rest/meals/100015`
## createWithLocation
`curl -v -d "{\"dateTime\":\"2024-08-22T20:05\",\"description\":\"Hamburger\",\"calories\":\"300\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/topjava/rest/meals`
## getAll
`curl -v http://localhost:8080/topjava/rest/meals`
## getAllBetween
`curl -v "http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=00:00&endDate=2020-01-31&endTime=10:01"`
