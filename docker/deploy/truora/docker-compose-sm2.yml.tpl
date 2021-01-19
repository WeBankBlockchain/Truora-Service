version: '3.7'
services:
  truora-server:
    volumes:
      - ./truora-sm2.yml:/dist/conf/application-sm2.yml
      - ${sdk_certificate_root}/:/dist/conf/cert/1
      #- /gm_sdk_path_of_chain_2/:/dist/conf/cert/2
