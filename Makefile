build:;
	 docker build -t flashcards .

run-prod:;
	docker-compose -f ./docker-compose.prod.yaml up

stop-prod:;
	docker-compose -f ./docker-compose.prod.yaml down

run-dev-db:;
	docker-compose -f ./docker-compose.dev.yaml up

stop-dev-db:;
	docker-compose -f ./docker-compose.dev.yaml down
