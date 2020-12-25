version: '3.7'
services:
  trustoracle-web:
    container_name: trustoracle-web
    image: ${image_organization}/trustoracle-web:${trustoracle_version}
    restart: always
    network_mode: "host"
    environment:
      - NODE_ENV=production
    volumes:
      - ./trustoracle-web.conf:/etc/nginx/conf.d/default.conf
      - ./log/nginx:/var/log/nginx/

  trustoracle-server:
    container_name: trustoracle-service
    image: ${image_organization}/trustoracle-service:${trustoracle_version}
    restart: always
    network_mode: "host"
    environment:
      - "SPRING_PROFILES_ACTIVE=docker"
      - "TRUSTORACLE_SERVICE_PORT=${trustoracle_service_port}"
      - "ENCRYPT_TYPE=${encrypt_type}"
      - "MYSQL_IP=${mysql_ip}"
      - "MYSQL_PORT=${mysql_port}"
      - "MYSQL_USER=${mysql_user}"
      - "MYSQL_PASSWORD=${mysql_password}"
    volumes:
      - ./log/server:/dist/log
      - ./trustoracle.yml:/dist/conf/application-docker.yml
      - ${sdk_certificate_root}/ca.crt:/dist/conf/1/ca.crt
      - ${sdk_certificate_root}/node.crt:/dist/conf/1/node.crt
      - ${sdk_certificate_root}/node.key:/dist/conf/1/node.key
      - ./key:/dist/key
