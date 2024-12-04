if [[ -z "${RUN_IN_PIPELINE}" ]]; then
    docker load < todo-app.tar
    docker run -p 8080:4242 -d todo-app
fi