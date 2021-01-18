version: '3.7'
services:
  webase-front:
    volumes:
      - ./truora-sm2.yml:/dist/conf/application-sm2.yml
      - ${sdk_certificate_root}/gmca.crt:/dist/conf/gmca.crt
      - ${sdk_certificate_root}/gmensdk.crt:/dist/conf/gmensdk.crt
      - ${sdk_certificate_root}/gmensdk.key:/dist/conf/gmensdk.key
      - ${sdk_certificate_root}/gmsdk.crt:/dist/conf/gmsdk.crt
      - ${sdk_certificate_root}/gmsdk.key:/dist/conf/gmsdk.key
