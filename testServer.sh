echo "Rule no 1. Ensure that the server is running 📡 "
echo "Sending request to 'localhost:8080/api/v1/event'"

for i in {1..1}
do
  curl --request POST -sL -i -v \
       --header "Content-Type: application/json" \
       --url 'http://localhost:8080/api/v1/event'\
       --data '{
                "userName": "Grace",
                "message": "Yes it works",
                "mood": "I want to shit 👿 ",
                "planToAchieve": "Tomorrow I want just to sleep"
       }'

  echo "Request number $i"
done