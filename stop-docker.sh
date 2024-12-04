if [[ -z "${RUN_IN_PIPELINE}" ]]; then
    docker rm $(docker stop $(docker ps -a -q --filter ancestor=todo-app --format="{{.ID}}"))
fi