#!/bin/bash

header='Accept: application/json'
base_url='http://localhost:8080/api/v1'

curl -X GET --header "$header" "${base_url}/categories"
curl -X GET --header "$header" "${base_url}/categories/608f0617be06df7904e7e372"

curl -X GET --header "$header" "${base_url}/vendors"
curl -X GET --header "$header" "${base_url}/vendors/608f0617be06df7904e7e372"
