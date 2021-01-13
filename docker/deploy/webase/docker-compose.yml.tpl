version: '3.7'
services:
  webase-front:
    extends:
      file: docker-compose-${encryption_type}.yml
      service: webase-front
    image: ${image_organization}/webase-front:${webase_front_version}
    container_name: webase-front
    restart: always
    network_mode: "host"
    environment:
      - "SPRING_PROFILES_ACTIVE=docker"
      - "ENCRYPT_TYPE=${encrypt_type}"
      - "WEBASE_FRONT_PORT=${webase_front_port}"
    volumes:
      - ./webase-front.yml:/dist/conf/application-docker.yml
      - ./h2:/dist/h2/
      - ./log/:/dist/log
