version: '3.7'
services:
  trustoracle-server:
    volumes:
      - ./trustoracle-ecdsa.yml:/dist/conf/application-ecdsa.yml
      - ${sdk_certificate_root}/:/dist/conf/cert/1
      #- /sdk_path_of_chain_2/:/dist/conf/cert/2
