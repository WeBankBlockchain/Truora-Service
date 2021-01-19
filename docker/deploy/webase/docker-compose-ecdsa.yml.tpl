version: '3.7'
services:
  webase-front:
    volumes:
      - ./truora-ecdsa.yml:/dist/conf/application-ecdsa.yml
      - ${sdk_certificate_root}/ca.crt:/dist/conf/ca.crt
      - ${sdk_certificate_root}/node.crt:/dist/conf/node.crt
      - ${sdk_certificate_root}/node.key:/dist/conf/node.key
