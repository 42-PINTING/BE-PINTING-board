REPO_URL = git@github.com:42-PINTING/env

build:
	cd ./board && ./gradlew clean build -x test

up:
	make build
	docker compose --env-file ./env/.env up -d

down:
	docker compose --env-file ./env/.env -f ./compose.yaml down

start:
	docker compose --env-file ./env/.env start board_db board

stop:
	docker compose --env-file ./env/.env stop board_db board

fclean:
	rm -rf ./data
	docker compose --env-file ./env/.env -f ./compose.yaml down --rmi all --volumes

clean:
	make down
	docker rmi -f pinting/board:1.0.0

re:
	make clean
	make up

env_update:
	@if [ -d "env" ]; then \
		echo "env 파일이 있어요.\ngit pull을 실행합니다."; \
		cd env && git pull; \
	else \
		echo "env 파일이 없어요.\ngit clone을 실행합니다."; \
		git clone $(REPO_URL); \
	fi

.PHONY: build up env_update up re