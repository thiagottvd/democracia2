docker-compose exec pgserver psql -U postgres -d testdb -f /docker-entrypoint-initdb.d/init-data.sql
