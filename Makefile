build:;
	 docker build --network="host" -t flashcards .

run-prod:;
	docker compose -f ./docker-compose.prod.yaml up -d

stop-prod:;
	docker compose -f ./docker-compose.prod.yaml down

run-dev-db:;
	docker compose -f ./docker-compose.dev.yaml up -d

stop-dev-db:;
	docker compose -f ./docker-compose.dev.yaml stop