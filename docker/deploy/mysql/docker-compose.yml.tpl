version: '3.7'
services:
  truora-mysql:
    container_name: truora-mysql
    image: mysql:${mysql_version}
    restart: always
    network_mode: "host"
    environment:
      MYSQL_ROOT_PASSWORD: ${mysql_password}
      MYSQL_DATABASE: truora
      MYSQL_USER: ${mysql_user}
      MYSQL_PASSWORD: ${mysql_password}
    command:
      - 'mysqld'
      - '--character-set-server=utf8mb4'
      - '--collation-server=utf8mb4_unicode_ci'
      - '--default-time-zone=+8:00'
      - '--log-error=/var/log/mysql/error.log'
      - '--log-error-verbosity=3'
      - '--port=${mysql_port}'
    volumes:
      # MySQL数据库挂载到host物理机目录
      - "./mysql/data:/var/lib/mysql"

