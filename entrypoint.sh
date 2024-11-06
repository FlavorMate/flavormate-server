#!/bin/sh

CN="postgresql://$DB_USER:$DB_PASSWORD@$DB_HOST/$DB_DATABASE"

psql "$CN" -tAXqc "UPDATE flyway_schema_history SET version = '1.0.0' WHERE version = '1';"
psql "$CN" -tAXqc "UPDATE flyway_schema_history SET version = '1.1.0' WHERE version = '1.1';"

V1_1_0=$(psql "$CN" -tAXqc "SELECT COUNT(*) FROM flyway_schema_history WHERE version = '1.1.0';")

if [ "$V1_1_0" = "1" ]; then
  COUNT=$(psql "$CN" -tAXqc "SELECT COUNT(*) FROM flyway_schema_history;")

  echo "Check if V1.0.1 has to be inserted"
  V1_0_1=$(psql "$CN" -tAXqc "SELECT COUNT(*) FROM flyway_schema_history WHERE version = '1.0.1';")

  if [ "$V1_0_1" != "1" ]; then
    echo "Inserting V1.0.1"
    COUNT=$((COUNT+1))
    psql "$CN" -tAXqc "INSERT INTO flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, execution_time, success) VALUES ($COUNT,'1.0.1','insert category groups', 'SQL', 'V1.0.1__insert_category_groups.sql', 1844403329, '$DB_USER',2,true);"
  fi

  echo "Check if V1.0.2 has to be inserted"
    V1_0_2=$(psql "$CN" -tAXqc "SELECT COUNT(*) FROM flyway_schema_history WHERE version = '1.0.2';")

    if [ "$V1_0_2" != "1" ]; then
      echo "Inserting V1.0.2"
      COUNT=$((COUNT+1))
      psql "$CN" -tAXqc "INSERT INTO flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, execution_time, success) VALUES ($COUNT,'1.0.2','insert categories', 'SQL', 'V1.0.2__insert_categories.sql', -1715267108, '$DB_USER',2,true);"
    fi
fi

java -jar -Dspring.profiles.active=release /opt/app/flavormate-server.jar
