version: '3.7'
services:
  trustoracle-server:
    volumes:
      - ./trustoracle-sm2.yml:/dist/conf/application-sm2.yml
      - ${sdk_certificate_root}/gmca.crt:/dist/conf/1/gmca.crt
      - ${sdk_certificate_root}/gmensdk.crt:/dist/conf/1/gmensdk.crt
      - ${sdk_certificate_root}/gmensdk.key:/dist/conf/1/gmensdk.key
      - ${sdk_certificate_root}/gmsdk.crt:/dist/conf/1/gmsdk.crt
      - ${sdk_certificate_root}/gmsdk.key:/dist/conf/1/gmsdk.key
