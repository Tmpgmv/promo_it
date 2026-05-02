## О проекте Promo IT

## Порты
**8080** - приложение.
**8081** - Swagger.

## Swagger

Запуск:
```bash
sudo docker run --rm -p 8081:8080 \
-e SWAGGER_JSON=/spec/swagger.yaml \
-v "$PWD":/spec \
--name swaggerui swaggerapi/swagger-ui
```

