version: '3.7'
services:
  trustoracle-server:
    volumes:
      - ./trustoracle-ecdsa.yml:/dist/conf/application-ecdsa.yml
      - ${sdk_certificate_root}/ca.crt:/dist/conf/1/ca.crt
      - ${sdk_certificate_root}/node.crt:/dist/conf/1/node.crt
      - ${sdk_certificate_root}/node.key:/dist/conf/1/node.key
