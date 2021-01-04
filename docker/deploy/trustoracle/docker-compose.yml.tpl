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
      # FISCO-BCOS 节点 IP，默认：127.0.0.1
      - "FISCO_BCOS_IP=${fisco_bcos_ip}"
      # FISCO-BCOS 节点端口，默认：20200
      - "FISCO_BCOS_PORT=${fisco_bcos_port}"
      # FISCO-BCOS 连接群组，默认：1
      - "FISCO_BCOS_GROUP=${fisco_bcos_group}"
      - "MYSQL_IP=${mysql_ip}"
      - "MYSQL_PORT=${mysql_port}"
      - "MYSQL_USER=${mysql_user}"
      - "MYSQL_PASSWORD=${mysql_password}"
      - "MYSQL_DATABASE=${mysql_database}"
    volumes:
      - ./log/server:/dist/log
      - ./trustoracle.yml:/dist/conf/application-docker.yml
      - ${sdk_certificate_root}/ca.crt:/dist/conf/1/ca.crt
      - ${sdk_certificate_root}/node.crt:/dist/conf/1/node.crt
      - ${sdk_certificate_root}/node.key:/dist/conf/1/node.key
      - ./key:/dist/key
