upstream oracle_server{
    server 127.0.0.1:${truora_service_port};
}

add_header X-Frame-Options SAMEORIGIN;

server {
    listen       ${truora_web_port} default_server;
    server_name  0.0.0.0;
    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
        try_files $${my_empty_variable}uri $${my_empty_variable}uri/ /index.html =404;
    }

    location /oracle {
        proxy_pass    http://oracle_server/;
        proxy_set_header                Host                    $${my_empty_variable}host;
        proxy_set_header                X-Real-IP               $${my_empty_variable}remote_addr;
        proxy_set_header                X-Forwarded-For         $${my_empty_variable}proxy_add_x_forwarded_for;
    }

    error_page 404 /404.html;
    location = /40x.html {
    }

    error_page 500 502 503 504 /50x.html;
    location = /50x.html {
    }

    access_log /var/log/nginx/oracle-access.log;
    error_log /var/log/nginx/oracle-error.log;
}
